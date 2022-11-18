package vip.penint.ques.project.ques.domain;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Penint
 * @Description:题目表
 * @since 2022-11-03
 */
@ApiModel(value = "题目表")
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_qu")
public class Qu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "题目ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "题目类型")
    @TableField("qu_type")
    private Integer quType;

    @ApiModelProperty(value = "1普通,2较难")
    @TableField("level")
    private Integer level;

    @ApiModelProperty(value = "题目内容")
    @TableField("content")
    private String content;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "租户id")
    @TableField("com_id")
    private String comId;

    @ApiModelProperty(value = "发布人")
    @TableField(exist = false)
    private String publishName;
//
//    @TableField(exist = false)
//    private List<String> repoIds;

    @TableField(exist = false)
    private List<QuAnswer> answerList;

    @TableField(exist = false)
    private Integer quCount;

    @TableField(exist = false)
    private JSONObject stat;

}
