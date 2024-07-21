package cn.jeefast.system.service;

import cn.jeefast.system.entity.SysUserScore;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

import java.util.Map;

public interface SysUserScoreService extends IService<SysUserScore> {
    Page<SysUserScore> queryPageList(Page<SysUserScore> page, Map<String, Object> map);
    void deleteBatch(String[] ids);
}
