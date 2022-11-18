package vip.penint.ques.project.system.mapper;

import vip.penint.ques.project.system.domain.SysTempMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统模板菜单表(SysTempMenu)表数据库访问层
 */
public interface SysTempMenuMapper {


    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<SysTempMenu> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param sysTempMenu 实例对象
     * @return 对象列表
     */
    List<SysTempMenu> queryAll(SysTempMenu sysTempMenu);

    /**
     * 新增数据
     *
     * @param sysTempMenu 实例对象
     * @return 影响行数
     */
    int insert(SysTempMenu sysTempMenu);

    /**
     * 修改数据
     *
     * @param sysTempMenu 实例对象
     * @return 影响行数
     */
    int update(SysTempMenu sysTempMenu);

    /**
     * 根据模板删除菜单信息
     *
     * @param tempId
     * @return
     */
    int deleteByTempId(@Param("tempId") String tempId);
}