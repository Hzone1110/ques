package vip.penint.ques.project.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.penint.ques.common.constant.UserConstants;
import vip.penint.ques.common.exception.CustomException;
import vip.penint.ques.common.utils.SecurityUtils;
import vip.penint.ques.common.utils.StringUtils;
import vip.penint.ques.framework.aspectj.lang.annotation.DataScope;
import vip.penint.ques.project.system.domain.SysRole;
import vip.penint.ques.project.system.domain.SysUser;
import vip.penint.ques.project.system.domain.SysUserRole;
import vip.penint.ques.project.system.domain.vo.AnswerExportVo;
import vip.penint.ques.project.system.mapper.SysRoleMapper;
import vip.penint.ques.project.system.mapper.SysUserMapper;
import vip.penint.ques.project.system.mapper.SysUserRoleMapper;
import vip.penint.ques.project.system.service.ISysUserService;

import java.util.*;

/**
 * 用户 业务层处理
 */
@Service
public class SysUserServiceImpl implements ISysUserService {

    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Value("${sys.default.pwd}")
    private String defaultPwd;

    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUserList(SysUser user) {
        return userMapper.selectUserList(user);
    }

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByUserName(String userName) {
        return userMapper.selectUserByUserName(userName);
    }

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserById(Long userId) {
        return userMapper.selectUserById(userId);
    }

    /**
     * 查询用户所属角色组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserRoleGroup(String userName) {
        List<SysRole> list = roleMapper.selectRolesByUserName(userName);
        StringBuffer idsStr = new StringBuffer();
        for (SysRole role : list) {
            idsStr.append(role.getRoleName()).append(",");
        }
        if (StringUtils.isNotEmpty(idsStr.toString())) {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }


    /**
     * 校验用户名称是否唯一
     *
     * @param userName 用户名称
     * @return 结果
     */
    @Override
    public String checkUserNameUnique(String userName) {
        int count = userMapper.checkUserNameUnique(userName);
        if (count > 0) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     */
    @Override
    public String checkPhoneUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkPhoneUnique(user.getPhonenumber());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     */
    @Override
    public String checkEmailUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkEmailUnique(user.getEmail());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    @Override
    public void checkUserAllowed(SysUser user) {

        // 数据库中查询的当前操作的用户数据
        SysUser userDB = userMapper.selectUserById(user.getUserId());

        SysUser loginUser = SecurityUtils.getSysUser();
        // 如果登录用户是平台超级管理员，则允许随意操作
        if (SecurityUtils.isSuperAdmin(loginUser)) {
            if (user.isStop()) {
                throw new CustomException("不能停用平台超级管理员账号");
            }
            return;
        }
        // 登录用户是公司管理员(分两层写清晰一些)
        if (SecurityUtils.isComAdmin(loginUser)) {
            // 被操作用户也是管理员账号，并且操作是停用
            if (SecurityUtils.isComAdmin(userDB) && user.isStop()) {
                throw new CustomException("不能停用管理员账号");
            }
            // 只能操作自己公司的数据
            if (loginUser.getComId().equals(userDB.getComId())) {
                return;
            }
        }
        // 登录用户是普通用户，只能操作自己的数据
        if (!SecurityUtils.isComAdmin(loginUser) && !SecurityUtils.isSuperAdmin(loginUser)) {
            if (loginUser.getUserId().equals(user.getUserId())) {
                return;
            }
        }
        throw new CustomException("无权操作");
    }

    /**
     * 新增保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertUser(SysUser user) {
        // 新增用户信息
        int rows = userMapper.insertUser(user);
        // 新增用户与角色管理
        insertUserRole(user);
        return rows;
    }

    /**
     * 修改保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateUser(SysUser user) {
        Long userId = user.getUserId();
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 新增用户与角色管理
        insertUserRole(user);
        // 删除用户与岗位关联
//    userPostMapper.deleteUserPostByUserId(userId);
        // 新增用户与岗位管理
//    insertUserPost(user);
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户状态
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserStatus(SysUser user) {
        SysUser sysUser = userMapper.selectUserById(user.getUserId());
        if (SecurityUtils.isSuperAdmin(sysUser) || SecurityUtils.isComAdmin(sysUser)) {
            throw new CustomException("不能禁用管理员账号");
        }

        return userMapper.updateUser(user);
    }

    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserProfile(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户头像
     *
     * @param userName 用户名
     * @param avatar   头像地址
     * @return 结果
     */
    @Override
    public boolean updateUserAvatar(String userName, String avatar) {
        return userMapper.updateUserAvatar(userName, avatar) > 0;
    }

    /**
     * 重置用户密码
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int resetPwd(SysUser user) {
        return userMapper.updateUser(user);
    }

    /**
     * 重置用户密码
     *
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    @Override
    public int resetUserPwd(String userName, String password) {
//        if (SecurityUtils.isSuperAdmin() || SecurityUtils.isSuperAdmin()) {
//            throw new CustomException("无权重置密码");
//        }
        return userMapper.resetUserPwd(userName, password);
    }

    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    public void insertUserRole(SysUser user) {
        Long[] roles = user.getRoleIds();
        if (StringUtils.isNotNull(roles)) {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>();
            for (Long roleId : roles) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(user.getUserId());
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0) {
                userRoleMapper.batchUserRole(list);
            }
        }
    }

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public int deleteUserById(Long userId) {
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        return userMapper.deleteUserById(userId);
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Override
    public int deleteUserByIds(Long[] userIds) {
        for (Long userId : userIds) {
            SysUser sysUser = userMapper.selectUserById(userId);
            if (SecurityUtils.isSuperAdmin(sysUser) || SecurityUtils.isComAdmin(sysUser)) {
                throw new CustomException("不能删除管理员账号");
            }

        }
        return userMapper.deleteUserByIds(userIds);
    }

    /**
     * 导入用户数据
     *
     * @param userList        用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importUser(List<AnswerExportVo> userList, Boolean isUpdateSupport, String operName, Boolean answer) {
        if (StringUtils.isNull(userList) || userList.size() == 0) {
            throw new CustomException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        String password = defaultPwd;
        for (AnswerExportVo auser : userList) {
            try {

                SysUser user = new SysUser();
                BeanUtils.copyProperties(auser, user);

                user.setUserName(user.getPhonenumber());
                // 验证是否存在这个用户
                SysUser u = userMapper.selectUserByUserName(user.getUserName());
                if (StringUtils.isNull(u)) {
                    user.setPassword(SecurityUtils.encryptPassword(password));
                    user.setCreateBy(operName);
                    if (Boolean.TRUE.equals(answer)) {
                        user.setAnswerFlag(1);
                    } else {
                        user.setComId(SecurityUtils.getCurrComId());
                    }
                    checkUser(user);
                    this.insertUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 导入成功");
                } else if (isUpdateSupport) {
                    user.setUpdateBy(operName);
                    this.updateUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + user.getUserName() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + auser.getPhonenumber() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new CustomException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    @Override
    public String importUsers(List<SysUser> userList, boolean isUpdateSupport, String operName, boolean answer) {
        if (StringUtils.isNull(userList) || userList.size() == 0) {
            throw new CustomException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        String password = defaultPwd;
        for (SysUser user : userList) {
            try {
                user.setUserName(user.getPhonenumber());
                // 验证是否存在这个用户
                SysUser u = userMapper.selectUserByUserName(user.getUserName());
                if (StringUtils.isNull(u)) {
                    user.setPassword(SecurityUtils.encryptPassword(password));
                    user.setCreateBy(operName);
                    if (Boolean.TRUE.equals(answer)) {
                        user.setAnswerFlag(1);
                    } else {
                        user.setComId(SecurityUtils.getCurrComId());
                    }
                    checkUser(user);
                    this.insertUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 导入成功");
                } else if (isUpdateSupport) {
                    user.setUpdateBy(operName);
                    this.updateUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + user.getUserName() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getUserName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new CustomException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    @Override
    public SysUser selectByMobile(String mobile) {
        return userMapper.selectByMobile(mobile);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerAnswerer(SysUser user) {
        user.setUserName(user.getPhonenumber());
        user.setRemark("答者账号");
        user.setAdminFlag(0);
        user.setSuperAdminFlag(0);
        user.setAnswerFlag(1);
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        checkUser(user);
        this.insertUser(user);
    }

    private void checkUser(SysUser user) {
        if (UserConstants.NOT_UNIQUE.equals(this.checkUserNameUnique(user.getUserName()))) {
            throw new CustomException("新增用户'" + user.getUserName() + "'失败，该登录账号已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(this.checkPhoneUnique(user))) {
            throw new CustomException("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(this.checkEmailUnique(user))) {
            throw new CustomException("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
    }


    @Override
    public List<SysUser> selectAnswerList(SysUser user, Boolean random) {
        if (StringUtils.isNotEmpty(user.getAttributes())){
            String[] split = user.getAttributes().split(",");
            user.setAtts(split);
        }
        return userMapper.selectAnswerList(user, random);
    }

    @Override
    public List<Map<String, Objects>> selectAllCompany() {
        return userMapper.selectAllCompany();
    }

    @Override
    public void allotTag() {
        String[] atts = new String[]{"1", "2", "3", "4"};
        // 获取所有的答者
        List<SysUser> list = this.selectAnswerList(new SysUser(), true);
        for (int i = 0; i < list.size(); i++) {
            SysUser user = list.get(i);
            SysUser a = new SysUser();
            a.setUserId(user.getUserId());
            Integer random = getRandom(null);
            a.setAttributes(atts[random] + "," + atts[getRandom(random)]);
            this.resetPwd(a);
        }
    }

    private static Integer getRandom(Integer num) {
        Random random = new Random();
        int i = random.nextInt(4);

        if (ObjectUtil.isNotNull(num)) {
            while (i == num) {
                i = random.nextInt(4);
            }
        }
        return i;
    }



    public static void main(String[] args) {

        for (int i = 0; i < 100; i++) {
            System.out.println(getRandom(null));

        }
    }
}
