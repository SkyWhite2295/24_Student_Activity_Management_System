package cn.jeefast.system.service;


import cn.jeefast.system.entity.Sysscore;
import com.baomidou.mybatisplus.plugins.Page;
import java.util.Map;
import com.baomidou.mybatisplus.service.IService;


public interface SysscoreService extends IService<Sysscore>{
    Page<Sysscore> queryPageList(Page<Sysscore> page, Map<String, Object> map);
}