package vip.penint.ques.project.system.domain.vo;

import lombok.Data;
import vip.penint.ques.common.constant.Constants;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonProperty;
import vip.penint.ques.framework.aspectj.lang.annotation.Excel;
import vip.penint.ques.framework.aspectj.lang.annotation.Excels;
import vip.penint.ques.framework.web.domain.BaseEntity;

/**
 * 用户对象 sys_user
 */
@Data
public class AnswerExportVo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 手机号码 */
    @Excel(name = "手机号码")
    private String phonenumber;

    /** 用户昵称 */
    @Excel(name = "用户名称")
    private String nickName;

    /** 用户性别 */
    @Excel(name = "用户性别", readConverterExp = "0=男,1=女,2=未知")
    private String sex;

    @Excel(name = "年龄")
    private Integer age;

    /** 用户邮箱 */
    @Excel(name = "用户邮箱")
    private String email;

    @Excel(name = "职位")
    private String job;

    @Excel(name = "爱好")
    private String like;


}
