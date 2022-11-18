package vip.penint.ques.project.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vip.penint.ques.common.constant.UserConstants;
import vip.penint.ques.common.utils.SecurityUtils;
import vip.penint.ques.common.utils.ServletUtils;
import vip.penint.ques.common.utils.StringUtils;
import vip.penint.ques.common.utils.poi.ExcelUtil;
import vip.penint.ques.framework.aspectj.lang.annotation.Log;
import vip.penint.ques.framework.aspectj.lang.enums.BusinessType;
import vip.penint.ques.framework.security.LoginUser;
import vip.penint.ques.framework.security.service.TokenService;
import vip.penint.ques.framework.web.controller.BaseController;
import vip.penint.ques.framework.web.domain.AjaxResult;
import vip.penint.ques.framework.web.page.TableDataInfo;
import vip.penint.ques.project.system.domain.SysRole;
import vip.penint.ques.project.system.domain.SysUser;
import vip.penint.ques.project.system.domain.vo.AnswerExportVo;
import vip.penint.ques.project.system.service.ISysRoleService;
import vip.penint.ques.project.system.service.ISysUserService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户信息
 */
@RestController
@RequestMapping("/selfcom/user")
public class SelfcomUserController extends BaseController {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private TokenService tokenService;

    /**
     * 获取用户列表
     */
    @PreAuthorize("@ss.hasPermi('selfcom:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user) {
        user.setComId(SecurityUtils.getCurrComId());
        startPage();
        List<SysUser> list = userService.selectUserList(user);
        return getDataTable(list);
    }

    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('selfcom:user:export')")
    @GetMapping("/export")
    public AjaxResult export(SysUser user) {
        List<SysUser> list = userService.selectUserList(user);
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.exportExcel(list, "用户数据");
    }

    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('selfcom:user:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport, boolean answer) throws Exception {
       if (answer) {
           ExcelUtil<AnswerExportVo> util = new ExcelUtil<>(AnswerExportVo.class);

           List<AnswerExportVo> userList = util.importExcel(file.getInputStream());

           LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
           String operName = loginUser.getUsername();
           String message = userService.importUser(userList, updateSupport, operName, answer);
           return AjaxResult.success(message);
       }else {
           ExcelUtil<SysUser> util = new ExcelUtil<>(SysUser.class);

           List<SysUser> userList = util.importExcel(file.getInputStream());

           LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
           String operName = loginUser.getUsername();
           String message = userService.importUsers(userList, updateSupport, operName, answer);
           return AjaxResult.success(message);
       }
    }

    @GetMapping("/importTemplate")
    public AjaxResult importTemplate(boolean answer) {
        if (answer) {
            ExcelUtil<AnswerExportVo> util = new ExcelUtil<>(AnswerExportVo.class);
            return util.importTemplateExcel("答者数据");
        }
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.importTemplateExcel("用户数据");
    }

    /**
     * 根据用户编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('selfcom:user:query')")
    @GetMapping(value = {"/", "/{userId}"})
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) String userId) {
        Long userIdc = StringUtils.isBlank(userId) ? SecurityUtils.getCurrUserId() : Long.valueOf(userId);
        AjaxResult ajax = AjaxResult.success();
        List<SysRole> roles = roleService.selectRoleAll();

        ajax.put("roles", SysUser.isAdmin(userIdc) ? roles
                : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
//    ajax.put("posts", postService.selectPostAllByCom(SecurityUtils.getCurrComId()));
        if (StringUtils.isNotNull(userIdc)) {
            ajax.put(AjaxResult.DATA_TAG, userService.selectUserById(userIdc));
//      ajax.put("postIds", postService.selectPostListByUserId(userIdc));
            ajax.put("roleIds", roleService.selectRoleListByUserId(userIdc));
        }
        ajax.put(AjaxResult.DATA_TAG, userService.selectUserById(userIdc));

        return ajax;
    }

    /**
     * 新增用户
     */
    @PreAuthorize("@ss.hasPermi('selfcom:user:add')")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysUser user) {
        // 手机号也就是账号
        user.setPhonenumber(user.getUserName());
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user.getUserName()))) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，该登录账号已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setCreateBy(SecurityUtils.getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        if (!SecurityUtils.isAnswer(user)) {
            user.setComId(SecurityUtils.getCurrComId());
        }
        user.setAdminFlag(0);
        return toAjax(userService.insertUser(user));
    }

    /**
     * 修改用户
     */
    @PreAuthorize("@ss.hasPermi('selfcom:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysUser user) {
        // 手机号也就是账号
        user.setPhonenumber(user.getUserName());
        if (!SecurityUtils.isAnswer(user)) {
            userService.checkUserAllowed(user);
        }
        if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        if (UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.updateUser(user));
    }

    /**
     * 删除用户
     */
    @PreAuthorize("@ss.hasPermi('selfcom:user:remove')")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds) {

        return toAjax(userService.deleteUserByIds(userIds));
    }

    /**
     * 重置密码
     */
    @PreAuthorize("@ss.hasPermi('selfcom:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.resetPwd(user));
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('selfcom:user:updateStatus')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysUser user) {
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.updateUserStatus(user));
    }
}