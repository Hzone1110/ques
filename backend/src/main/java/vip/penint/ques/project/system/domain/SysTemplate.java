package vip.penint.ques.project.system.domain;

import lombok.Data;
import vip.penint.ques.framework.aspectj.lang.annotation.Excel;
import vip.penint.ques.framework.web.domain.BaseEntity;

import java.util.List;

/**
 * 权限模板对象 sys_template
 */
@Data
public class SysTemplate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String id;

    /** 模板名称 */
    @Excel(name = "模板名称")
    private String name;

    private List<Integer> menuIds;

}
