package cn.jeefast.system.dao;

import cn.jeefast.system.entity.SysUserScore;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;

import java.util.List;
import java.util.Map;

public interface SysUserScoreDao extends BaseMapper<SysUserScore> {
    List<SysUserScore> queryPageList(Page<SysUserScore> page, Map<String, Object> map);

    int deleteBatch(Object[] id);
}
