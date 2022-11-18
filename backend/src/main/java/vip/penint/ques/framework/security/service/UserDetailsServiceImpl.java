package vip.penint.ques.framework.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vip.penint.ques.common.enums.UserStatus;
import vip.penint.ques.common.exception.BaseException;
import vip.penint.ques.common.utils.StringUtils;
import vip.penint.ques.framework.security.LoginUser;
import vip.penint.ques.project.system.domain.SysCompany;
import vip.penint.ques.project.system.domain.SysUser;
import vip.penint.ques.project.system.service.ISysCompanyService;
import vip.penint.ques.project.system.service.ISysUserService;

import java.util.Date;

/**
 * 用户验证处理
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private ISysCompanyService companyService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userService.selectUserByUserName(username);
        if (StringUtils.isNull(user)) {
            log.info("登录用户：{} 不存在.", username);
            throw new UsernameNotFoundException("登录用户：" + username + " 不存在");
        }
        String comId = user.getComId();
        if (StringUtils.isNotBlank(comId)) {
            SysCompany sysCompany = companyService.selectSysCompanyById(comId);
            if (sysCompany.getActiveFlag() == 0) {
                throw new BaseException("对不起，该租户账号暂未激活，请联系管理员");
            }
            if (sysCompany.getActiveTime().before(new Date())) {
                throw new BaseException("对不起，该租户账号已过有效期，请联系管理员");
            }
        }
        if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            log.info("登录用户：{} 已被删除.", username);
            throw new BaseException("对不起，您的账号：" + username + " 已被删除");
        }
        if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", username);
            throw new BaseException("对不起，您的账号：" + username + " 已停用");
        }

        return createLoginUser(user);
    }

    public UserDetails createLoginUser(SysUser user) {
        return new LoginUser(user, permissionService.getMenuPermission(user));
    }
}
