package cn.jeefast.system.dao;

import cn.jeefast.system.entity.SysHdwjtj;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import com.baomidou.mybatisplus.plugins.Page;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 活动附件表 Mapper 接口
 * </p>
 *
 */
public interface SysHdwjtjDao extends BaseMapper<SysHdwjtj> {
    List<SysHdwjtj> queryPageList(Page<SysHdwjtj> page, Map<String, Object> map);

    int deleteBatch(Object[] id);
}