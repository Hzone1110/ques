package vip.penint.ques.project.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.penint.ques.common.utils.SecurityUtils;
import vip.penint.ques.framework.web.controller.BaseController;
import vip.penint.ques.framework.web.domain.AjaxResult;
import vip.penint.ques.framework.web.page.TableDataInfo;
import vip.penint.ques.project.system.domain.SysUser;
import vip.penint.ques.project.system.service.ISysUserService;

import java.util.List;

@RestController
    @RequestMapping("/answer/user")
public class AnswerUserController extends BaseController {

    @Autowired
    private ISysUserService userService;


    /**
     * 获取用户列表
     */
    @PreAuthorize("@ss.hasPermi('answer:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectAnswerList(user,false);
        return getDataTable(list);
    }


    /**
     * 随机分配答者用户标签
     */
    @PutMapping("/allotTag")
    public AjaxResult allotTag(){
        userService.allotTag();
        return AjaxResult.success();
    }
}

