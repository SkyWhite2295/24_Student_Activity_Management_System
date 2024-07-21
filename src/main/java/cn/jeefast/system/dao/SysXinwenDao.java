package cn.jeefast.system.dao;

import cn.jeefast.system.entity.SysXinwen;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 活动信息 Mapper 接口
 * </p>
 *
 * @author theodo
 * @since 2022-03-30
 */
public interface SysXinwenDao extends BaseMapper<SysXinwen> {
    List<SysXinwen> queryPageList(Page<SysXinwen> page, Map<String, Object> map);

    int deleteBatch(Object[] id);
}