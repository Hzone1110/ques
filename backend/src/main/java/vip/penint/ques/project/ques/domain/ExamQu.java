package vip.penint.ques.project.ques.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
    
/**
 * @Description:试卷考题
 * @author Penint
 * @since 2022-11-06
 */
@ApiModel(value ="试卷考题")
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_exam_qu")
public class ExamQu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty(value = "问卷ID")
    @TableField("exam_id")
    private String examId;

    @ApiModelProperty(value = "题目ID")
    @TableField("qu_id")
    private String quId;

    @ApiModelProperty(value = "题目类型")
    @TableField("qu_type")
    private Integer quType;

    @ApiModelProperty(value = "问题排序")
    @TableField("sort")
    private Integer sort;

    @ApiModelProperty(value = "逻辑删除")
    @TableField("del_flag")
    @TableLogic
    private Integer delFlag;


}
