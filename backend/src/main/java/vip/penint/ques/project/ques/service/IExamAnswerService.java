package vip.penint.ques.project.ques.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.penint.ques.framework.web.domain.AjaxResult;
import vip.penint.ques.project.ques.domain.ExamAnswer;
import vip.penint.ques.project.ques.domain.vo.AnswerListVO;
import vip.penint.ques.project.ques.domain.vo.MyAnswerVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 问卷回答表 服务类
 * </p>
 *
 * @author Penint
 * @since 2022-11-09
 */
public interface IExamAnswerService extends IService<ExamAnswer> {

    AjaxResult add(ExamAnswer examAnswer);

    List<ExamAnswer> selectByUidAndExamId(String examId);

    IPage<AnswerListVO> examAnswerList(String examId, IPage<MyAnswerVO> page);

    List<Map<String,Object>> allQues();

    JSONObject answerCount();
}
