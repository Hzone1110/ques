package vip.penint.ques.project.ques.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vip.penint.ques.common.domain.QueryRequest;
import vip.penint.ques.common.utils.SecurityUtils;
import vip.penint.ques.framework.web.domain.AjaxResult;
import vip.penint.ques.project.ques.domain.Exam;
import vip.penint.ques.project.ques.service.IExamService;
import vip.penint.ques.project.system.domain.SysUser;
import vip.penint.ques.project.system.service.ISysUserService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 问卷表 前端控制器
 * </p>
 *
 * @author Penint
 * @since 2022-11-06
 */
@RestController
@RequestMapping("/exam")
public class ExamController {

    @Autowired
    private IExamService examService;

    @Autowired
    private ISysUserService userService;

    /**
     * 新增问卷
     */
    @PostMapping("/addExam")
    public AjaxResult addExam(@RequestBody Exam exam) {
        examService.addExam(exam);
        return AjaxResult.success();
    }

    /**
     * 修改主题
     */
    @PutMapping("/updateTheme")
    public AjaxResult updateTheme(@RequestBody Exam exam) {
        examService.updateTheme(exam);
        return AjaxResult.success();
    }


    /**
     * 查询问卷列表
     */
    @GetMapping("/list")
    public AjaxResult list(Exam exam, QueryRequest queryRequest) {

        // 超级管理员角色查询所有考试，非超级管理员账号只能查看自己发起的考试
        if (SecurityUtils.isComUser()) {
            exam.setStartUser(SecurityUtils.getCurrUserId());
        } else if (SecurityUtils.isComAdmin()) {
            // 查询出所有的用户
            SysUser user = SecurityUtils.getSysUser();
            SysUser u = new SysUser();
            u.setComId(user.getComId());
            List<SysUser> list = userService.selectUserList(u);
            List<Long> uIds = list.stream().map(SysUser::getUserId).collect(Collectors.toList());

            exam.setUids(uIds);

        }
        return AjaxResult.success(examService.selectExamList(exam, queryRequest));

    }

    /**
     * 查询问卷详情
     */
    @GetMapping("/info/{examId}")
    public AjaxResult info(@PathVariable String examId) {
        return AjaxResult.success(examService.selectExamInfo(examId));
    }

    /**
     * 删除
     */
    @DeleteMapping("/del/{examId}")
    public AjaxResult del(@PathVariable String examId) {
        examService.delExam(examId);
        return AjaxResult.success();
    }


    /**
     * 状态修改
     */
    @PutMapping("/editStatus/{examId}/{status}")
    public AjaxResult editStatus(@PathVariable String examId, @PathVariable String status) {
        examService.editStatus(examId, status);
        return AjaxResult.success();
    }

    /**
     * 问卷详情
     */
    @GetMapping("/previewInfo/{examId}")
    public AjaxResult previewInfo(@PathVariable String examId) {
        return AjaxResult.success(examService.previewInfo(examId));
    }

    /**
     * 获取我可以回答的问卷列表
     */
    @GetMapping("/myList")
    public AjaxResult myList(Exam exam, QueryRequest queryRequest) {
        return AjaxResult.success(examService.myList(exam, queryRequest));
    }

    /**
     * 获取我已回答的列表
     */
    @GetMapping("/myAnswer")
    public AjaxResult myAnswer(Exam exam, QueryRequest queryRequest) {
        return AjaxResult.success(examService.myAnswer(exam, queryRequest));
    }

    /**
     * 获取我回答的详情
     */
    @GetMapping("/myAnswerInfo/{answerId}")
    public AjaxResult myAnswerInfo(@PathVariable String answerId) {
        return AjaxResult.success(examService.myAnswerInfo(answerId));
    }

    /**
     * 获取某个问卷答卷列表
     */
    @GetMapping("/examAnswerList/{examId}")
    public AjaxResult examAnswerList(@PathVariable String examId, QueryRequest queryRequest) {
        return AjaxResult.success(examService.examAnswerList(examId, queryRequest));
    }


    @GetMapping("/examAnswerStat/{examId}/{quId}")
    public AjaxResult examAnswerStat(@PathVariable String examId, @PathVariable String quId) {
        return AjaxResult.success(examService.examAnswerStat(examId, quId));
    }

}

