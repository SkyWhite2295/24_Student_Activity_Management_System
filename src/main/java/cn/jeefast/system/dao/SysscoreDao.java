package cn.jeefast.system.dao;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import cn.jeefast.system.entity.Sysscore;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
@Mapper
public interface SysscoreDao extends BaseMapper<Sysscore> {
    List<Sysscore> queryPageList(Page<Sysscore> page, Map<String, Object> map);
}