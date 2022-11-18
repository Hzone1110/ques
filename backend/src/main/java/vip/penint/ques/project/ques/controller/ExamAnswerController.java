package vip.penint.ques.project.ques.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.penint.ques.framework.web.domain.AjaxResult;
import vip.penint.ques.project.ques.domain.ExamAnswer;
import vip.penint.ques.project.ques.service.IExamAnswerService;

/**
 * <p>
 * 问卷回答表 前端控制器
 * </p>
 *
 * @author Penint
 * @since 2022-11-09
 */
@RestController
@RequestMapping("/examAnswer")
public class ExamAnswerController {


    @Autowired
    private IExamAnswerService examAnswerService;


    @PostMapping
    public AjaxResult add(@RequestBody ExamAnswer examAnswer) {

        return examAnswerService.add(examAnswer);
    }
}

