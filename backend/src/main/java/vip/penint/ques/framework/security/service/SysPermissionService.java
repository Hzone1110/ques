package vip.penint.ques.framework.security.service;

import java.util.HashSet;
import java.util.Set;

import vip.penint.ques.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vip.penint.ques.project.system.domain.SysUser;
import vip.penint.ques.project.system.service.ISysMenuService;
import vip.penint.ques.project.system.service.ISysRoleService;

/**
 * 用户权限处理
 * 
 */
@Component
public class SysPermissionService
{
    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysMenuService menuService;

    /**
     * 获取角色数据权限
     * 
     * @param user 用户信息
     * @return 角色权限信息
     */
    public Set<String> getRolePermission(SysUser user)
    {
        Set<String> roles = new HashSet<>();
        // 管理员拥有所有权限
        if (SecurityUtils.isSuperAdmin(user))
        {
            roles.add("系统管理员");
        }
        else if (!SecurityUtils.isSuperAdmin(user) && SecurityUtils.isComAdmin(user)){
            roles.add("租户管理员");
        }
        else if (SecurityUtils.isAnswer(user)){
            roles.add("答者");
        }
        else
        {
            roles.addAll(roleService.selectRolePermissionByUserId(user.getUserId()));
        }
        return roles;
    }

    /**
     * 获取菜单数据权限
     * 
     * @param user 用户信息
     * @return 菜单权限信息
     */
    public Set<String> getMenuPermission(SysUser user)
    {
        Set<String> perms = new HashSet<String>();
        // 管理员拥有所有权限
        if (SecurityUtils.isComAdmin(user))
        {
            perms.add("*:*:*");
        }
        else if(SecurityUtils.isAnswer(user)){
            perms.addAll(menuService.selectMenuPermsByAnswer());
        }
        else
        {
            perms.addAll(menuService.selectMenuPermsByUserId(user.getUserId()));
        }
        return perms;
    }
}
