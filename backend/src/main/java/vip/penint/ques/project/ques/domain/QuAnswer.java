package vip.penint.ques.project.ques.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Penint
 * @Description:候选答案
 * @since 2022-11-03
 */
@ApiModel(value = "候选答案")
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_qu_answer")
public class QuAnswer implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "答案ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "问题ID")
    @TableField("qu_id")
    private String quId;

    @ApiModelProperty(value = "选择内容")
    @TableField("content")
    private String content;


    @TableField(exist = false)
    private Integer checkNum;
    @TableField(exist = false)
    private Integer all;
}
