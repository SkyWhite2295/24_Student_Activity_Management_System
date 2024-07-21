package cn.jeefast.system.dao;

import cn.jeefast.system.entity.SysHdbmb;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import com.baomidou.mybatisplus.plugins.Page;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 活动报名表 Mapper 接口
 * </p>
 *
 */
public interface SysHdbmbDao extends BaseMapper<SysHdbmb> {
    List<SysHdbmb> queryPageList(Page<SysHdbmb> page, Map<String, Object> map);

    int deleteBatch(Object[] id);
}