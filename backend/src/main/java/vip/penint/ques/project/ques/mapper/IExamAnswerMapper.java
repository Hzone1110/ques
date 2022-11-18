package vip.penint.ques.project.ques.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import vip.penint.ques.project.ques.domain.ExamAnswer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import vip.penint.ques.project.ques.domain.vo.AnswerListVO;
import vip.penint.ques.project.ques.domain.vo.MyAnswerVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 问卷回答表 Mapper 接口
 * </p>
 *
 * @author Penint
 * @since 2022-11-09
 */
public interface IExamAnswerMapper extends BaseMapper<ExamAnswer> {

    IPage<AnswerListVO> examAnswerList(@Param("examId") String examId, IPage<MyAnswerVO> page);

    List<Map<String, Object>> allQues();

    Integer selectListByDay(@Param("day") String day);
}
