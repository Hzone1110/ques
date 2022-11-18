package vip.penint.ques.project.system.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vip.penint.ques.common.constant.Constants;
import vip.penint.ques.common.enums.UserStatus;
import vip.penint.ques.common.exception.BaseException;
import vip.penint.ques.common.utils.SecurityUtils;
import vip.penint.ques.common.utils.ServletUtils;
import vip.penint.ques.common.utils.StringUtils;
import vip.penint.ques.framework.aspectj.lang.annotation.Log;
import vip.penint.ques.framework.aspectj.lang.enums.BusinessType;
import vip.penint.ques.framework.redis.RedisCache;
import vip.penint.ques.framework.security.LoginBody;
import vip.penint.ques.framework.security.LoginUser;
import vip.penint.ques.framework.security.service.SysLoginService;
import vip.penint.ques.framework.security.service.SysPermissionService;
import vip.penint.ques.framework.security.service.TokenService;
import vip.penint.ques.framework.web.domain.AjaxResult;
import vip.penint.ques.project.system.domain.SysCompany;
import vip.penint.ques.project.system.domain.SysMenu;
import vip.penint.ques.project.system.domain.SysUser;
import vip.penint.ques.project.system.service.ISysCompanyService;
import vip.penint.ques.project.system.service.ISysMenuService;
import vip.penint.ques.project.system.service.ISysUserService;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 登录验证
 */
@RestController
public class SysLoginController {
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysCompanyService companyService;

    /**
     * 登录方法
     *
     * @param loginBody 登陆信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // 用户信息
        SysUser user = loginUser.getUser();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(user);
        return AjaxResult.success(menuService.buildMenus(menus));
    }


    /**
     * 获取登录验证码
     *
     * @param mobile
     * @return
     */
    @GetMapping("/getSmsCode")
    public AjaxResult getSmsCode(@RequestParam("mobile") String mobile) {

        // 先查询手机号是否在库里
        SysUser user = userService.selectByMobile(mobile);
        if (StringUtils.isNull(user)) {
            return AjaxResult.error("手机号尚未注册");
        } else {
            if (redisCache.hasKey(Constants.MOBILE_REDIS_KEY + mobile)) {
                return AjaxResult.error("当前手机号已获取过验证码，请等待五分钟后再试");
            }
//            String code = SmsUtil.randomCode();
            // 将短信验证码存入redis中
            redisCache.setCacheObject(Constants.MOBILE_REDIS_KEY + mobile, "111111", 300, TimeUnit.SECONDS);
            return AjaxResult.success();
        }
    }


    /**
     * 短信验证码登录
     *
     * @return
     */
    @PostMapping("/smsLogin")
    public AjaxResult smsLogin(@RequestBody LoginBody loginBody) {


        String mobile = loginBody.getUsername();
        String code = loginBody.getCode();

        // 先查询手机号是否在库里
        SysUser user = userService.selectByMobile(mobile);
        if (StringUtils.isNull(user)) {
            return AjaxResult.error("手机号尚未注册");
        } else {
            if (redisCache.hasKey(Constants.MOBILE_REDIS_KEY + mobile)) {
                String redisCode = redisCache.getCacheObject(Constants.MOBILE_REDIS_KEY + mobile);
                if (redisCode.equals(code)) {


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
                        throw new BaseException("对不起，您的账号：" + mobile + " 已被删除");
                    }
                    if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
                        throw new BaseException("对不起，您的账号：" + mobile + " 已停用");
                    }
                    AjaxResult ajax = AjaxResult.success();
                    LoginUser loginUser = new LoginUser();


                    // 角色集合
                    Set<String> pers = permissionService.getMenuPermission(user);
                    loginUser.setUser(user);
                    loginUser.setPermissions(pers);
                    // 生成令牌
                    String token = tokenService.createToken(loginUser);

                    ajax.put(Constants.TOKEN, token);
                    redisCache.deleteObject(Constants.MOBILE_REDIS_KEY + mobile);
                    return ajax;


                } else {
                    return AjaxResult.error("验证失败，请确认验证码是否正确");
                }
            } else {
                return AjaxResult.error("验证失败，请重新获取验证码");
            }
        }

    }


    /**
     * 注册租户
     */
    @PostMapping("/registerCompany")
    public AjaxResult registerCompany(@RequestBody SysCompany sysCompany){
        companyService.insertSysCompanyRegister(sysCompany);
        return AjaxResult.success();
    }

    /**
     * 注册答者
     */
    @PostMapping("/registerAnswerer")
    public AjaxResult registerAnswerer(@RequestBody SysUser user){
        userService.registerAnswerer(user);
        return AjaxResult.success();
    }


    /**
     * 获取重置密码验证码
     *
     * @param mobile
     * @return
     */
    @GetMapping("/getRePwdCode")
    public AjaxResult getRePwdCode(@RequestParam("mobile") String mobile) {

        // 先查询手机号是否在库里
        SysUser user = userService.selectByMobile(mobile);
        if (StringUtils.isNull(user)) {
            return AjaxResult.error("手机号尚未注册");
        } else {
            if (redisCache.hasKey(Constants.PWD_RESET_REDIS_KEY + mobile)) {
                return AjaxResult.error("当前手机号已获取过验证码，请等待五分钟后再试");
            }
            // 将短信验证码存入redis中
            redisCache.setCacheObject(Constants.PWD_RESET_REDIS_KEY + mobile, "111111", 300, TimeUnit.SECONDS);
            return AjaxResult.success();
        }
    }

    @PostMapping("/resetPwd")
    public  AjaxResult resetPwd(@RequestBody JSONObject data){
        String mobile = data.getString("mobile");
        String code = data.getString("code");
        String password = data.getString("password");

        // 先查询手机号是否在库里
        SysUser user = userService.selectByMobile(mobile);
        if (StringUtils.isNull(user)) {
            return AjaxResult.error("手机号尚未注册");
        } else {
            if (redisCache.hasKey(Constants.PWD_RESET_REDIS_KEY + mobile)) {
                String redisCode = redisCache.getCacheObject(Constants.PWD_RESET_REDIS_KEY + mobile);
                if (redisCode.equals(code)) {

                    user.setPassword(SecurityUtils.encryptPassword(password));
                    userService.resetPwd(user);
                    return AjaxResult.success();
                } else {
                    return AjaxResult.error("验证失败，请确认验证码是否正确");
                }
            } else {
                return AjaxResult.error("验证失败，请重新获取验证码");
            }
        }
    }

}
