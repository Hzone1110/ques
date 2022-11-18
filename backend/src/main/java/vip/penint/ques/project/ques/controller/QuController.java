package vip.penint.ques.project.ques.controller;


import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vip.penint.ques.common.domain.QueryRequest;
import vip.penint.ques.common.utils.SecurityUtils;
import vip.penint.ques.framework.aspectj.lang.annotation.Log;
import vip.penint.ques.framework.aspectj.lang.enums.BusinessType;
import vip.penint.ques.framework.web.domain.AjaxResult;
import vip.penint.ques.project.ques.domain.Qu;
import vip.penint.ques.project.ques.service.IQuService;

import java.util.Arrays;

/**
 * <p>
 * 题目表 前端控制器
 * </p>
 *
 * @author penint
 * @since 2022-07-18
 */
@RestController
@RequestMapping("/qu")
public class QuController {


    @Autowired
    private IQuService tQuService;


    /**
     * 查询题目列表
     */
    @ApiOperation(value = "查询题目列表")
    @PreAuthorize("@ss.hasPermi('ques:qu:list')")
    @GetMapping("/list")
    public AjaxResult list(Qu qu, QueryRequest queryRequest) {
        return AjaxResult.success(tQuService.selectTQuList(qu, queryRequest));
    }

    /**
     * 获取题目详细信息
     */
    @ApiOperation(value = "获取题目详细信息")
    @PreAuthorize("@ss.hasPermi('ques:qu:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(tQuService.getInfoById(id));
    }

    /**
     * 新增/修改题目
     */
    @ApiOperation(value = "新增题目")
    @PreAuthorize("@ss.hasPermi('ques:qu:add')")
    @Log(title = "题目", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Qu qu) throws Exception {
        if (!SecurityUtils.isSuperAdmin()){
            qu.setComId(SecurityUtils.getCurrComId());
        }
        tQuService.saveQu(qu);
        return AjaxResult.success();
    }

    /**
     * 删除题目
     */
    @ApiOperation(value = "删除题目")
    @PreAuthorize("@ss.hasPermi('ques:qu:remove')")
    @Log(title = "题目", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        tQuService.delByIds(Arrays.asList(ids));
        return AjaxResult.success();
    }


}
