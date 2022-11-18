package vip.penint.ques.project.ques.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import vip.penint.ques.common.domain.QueryRequest;
import vip.penint.ques.project.ques.domain.Qu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 题目表 服务类
 * </p>
 *
 * @author Penint
 * @since 2022-11-03
 */
public interface IQuService extends IService<Qu> {


    /**
     * 分页查询题目列表
     *
     * @param qu 题目
     * @param queryRequest 封装的公共查询条件
     * @return 题目集合
     */
    IPage<Qu> selectTQuList(Qu qu, QueryRequest queryRequest);

    Qu getInfoById(String id);

    void saveQu(Qu qu) throws Exception;

    void delByIds(List<String> ids);

    List<Map<String,Object>> quCount();

    Integer selectListByDay(String day);
}
