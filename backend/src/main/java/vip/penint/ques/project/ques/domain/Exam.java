package vip.penint.ques.project.ques.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.penint.ques.project.system.domain.SysUser;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Penint
 * @Description:问卷表
 * @since 2022-11-06
 */
@ApiModel(value = "问卷表")
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_exam")
public class Exam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "问卷ID")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty(value = "问卷名称")
    @TableField("title")
    private String title;

    @ApiModelProperty(value = "问卷描述")
    @TableField("content")
    private String content;

    @ApiModelProperty(value = "1公开2指定")
    @TableField("open_type")
    private Integer openType;

    @ApiModelProperty(value = "可答卷次数")
    @TableField("num")
    private Integer num;

    @ApiModelProperty(value = "是否限时")
    @TableField("time_limit")
    private Boolean timeLimit = false;

    @ApiModelProperty(value = "开始时间")
    @TableField(value = "start_time", updateStrategy = FieldStrategy.IGNORED)
    private LocalDate startTime;

    @ApiModelProperty(value = "结束时间")
    @TableField(value = "stop_time", updateStrategy = FieldStrategy.IGNORED)
    private LocalDate stopTime;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "发起人ID")
    @TableField("start_user")
    private Long startUser;

    @ApiModelProperty(value = "租户ID")
    @TableField("com_id")
    private String comId;

    @ApiModelProperty(value = "逻辑删除")
    @TableField("del_flag")
    @TableLogic
    private Integer delFlag;

    @ApiModelProperty(value = "问卷状态：0待发布，1已发布，2已结束")
    @TableField("status")
    private String status;

    @ApiModelProperty(value = "答者用户String数组")
    @TableField(value = "user_id_str", updateStrategy = FieldStrategy.IGNORED)
    private String userIdStr;

    @TableField(value = "theme")
    private Integer theme;

    @TableField(exist = false)
    private List<Qu> selectList;

    @TableField(exist = false)
    private List<SysUser> selectUserList;

    @TableField(exist = false)
    private List<String> dateValues;

    @TableField(exist = false)
    private List<Long> uids;
}
