package cn.jeefast.system.dao;

import cn.jeefast.system.entity.SysZcyxyzm;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import com.baomidou.mybatisplus.plugins.Page;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 注册邮箱验证码记录表 Mapper 接口
 * </p>
 *
 */
public interface SysZcyxyzmDao extends BaseMapper<SysZcyxyzm> {
    List<SysZcyxyzm> queryPageList(Page<SysZcyxyzm> page, Map<String, Object> map);
    int save(SysZcyxyzm sysZcyxyzm);
    int deleteBatch(Object[] id);
}