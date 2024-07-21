package cn.jeefast.system.service;

import cn.jeefast.system.entity.SysPjinfo;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 */
public interface SysPjinfoService extends IService<SysPjinfo> {
    Page<SysPjinfo> queryPageList(Page<SysPjinfo> page, Map<String, Object> map);
    void deleteBatch(String[] borrowIds);
	
}
