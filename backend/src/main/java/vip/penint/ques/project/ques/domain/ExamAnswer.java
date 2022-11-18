package vip.penint.ques.project.ques.domain;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Penint
 * @Description:问卷回答表
 * @since 2022-11-09
 */
@ApiModel(value = "问卷回答表")
@Data
@Accessors
@EqualsAndHashCode(callSuper = false)
@TableName(value = "t_exam_answer",autoResultMap = true)
public class ExamAnswer implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "回答表Id")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty(value = "问卷ID")
    @TableField("exam_id")
    private String examId;

    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private Integer userId;

    @ApiModelProperty(value = "回答内容")
    @TableField(value = "`data`", typeHandler = JacksonTypeHandler.class)
    private JSONObject data;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;


}
