package vip.penint.ques.project.ques.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
    
/**
 * @Description:问卷题目备选答案
 * @author Penint
 * @since 2022-11-06
 */
@ApiModel(value ="问卷题目备选答案")
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_exam_qu_answer")
public class ExamQuAnswer implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增ID")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty(value = "问卷ID")
    @TableField("exam_id")
    private String examId;

    @ApiModelProperty(value = "回答项ID")
    @TableField("answer_id")
    private String answerId;

    @ApiModelProperty(value = "题目ID")
    @TableField("qu_id")
    private String quId;

    @ApiModelProperty(value = "排序")
    @TableField("sort")
    private Integer sort;

    @ApiModelProperty(value = "选项标签")
    @TableField("abc")
    private String abc;

    @ApiModelProperty(value = "逻辑删除")
    @TableField("del_flag")
    @TableLogic
    private Integer delFlag;


}
