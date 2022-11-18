package vip.penint.ques.project.ques.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.penint.ques.common.domain.QueryRequest;
import vip.penint.ques.project.ques.domain.Exam;
import vip.penint.ques.project.ques.domain.ExamAnswer;
import vip.penint.ques.project.ques.domain.vo.AnswerListVO;
import vip.penint.ques.project.ques.domain.vo.MyAnswerVO;

/**
 * <p>
 * 问卷表 服务类
 * </p>
 *
 * @author Penint
 * @since 2022-11-06
 */
public interface IExamService extends IService<Exam> {

    void addExam(Exam exam);

    void updateTheme(Exam exam);

    IPage<Exam> selectExamList(Exam exam, QueryRequest queryRequest);

    Exam selectExamInfo(String examId);

    void delExam(String examId);

    void editStatus(String examId, String status);


    Exam previewInfo(String examId);

    IPage<Exam> myList(Exam exam, QueryRequest queryRequest);

    IPage<MyAnswerVO> myAnswer(Exam exam, QueryRequest queryRequest);

    ExamAnswer myAnswerInfo(String answerId);

    IPage<AnswerListVO> examAnswerList(String examId, QueryRequest queryRequest);

    JSONObject examAnswerStat(String examId, String quId);

}
