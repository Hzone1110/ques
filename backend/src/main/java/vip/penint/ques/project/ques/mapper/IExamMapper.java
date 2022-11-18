package vip.penint.ques.project.ques.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import vip.penint.ques.project.ques.domain.Exam;
import vip.penint.ques.project.ques.domain.vo.MyAnswerVO;

/**
 * <p>
 * 问卷表 Mapper 接口
 * </p>
 *
 * @author Penint
 * @since 2022-11-06
 */
public interface IExamMapper extends BaseMapper<Exam> {

    IPage<Exam> myList(@Param("exam") Exam exam, @Param("userId") Long userId, IPage<Exam> page);

    IPage<MyAnswerVO> myAnswer(@Param("exam") Exam exam, @Param("userId") Long userId, IPage<MyAnswerVO> page);

}
