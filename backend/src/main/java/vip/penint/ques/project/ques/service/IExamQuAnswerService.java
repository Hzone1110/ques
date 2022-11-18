package vip.penint.ques.project.ques.service;

import vip.penint.ques.project.ques.domain.ExamQuAnswer;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 问卷题目备选答案 服务类
 * </p>
 *
 * @author Penint
 * @since 2022-11-06
 */
public interface IExamQuAnswerService extends IService<ExamQuAnswer> {

    void removeByExamId(String examId);
}
