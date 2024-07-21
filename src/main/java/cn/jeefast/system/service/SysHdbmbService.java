package cn.jeefast.system.service;

import cn.jeefast.system.entity.SysHdbmb;
import com.baomidou.mybatisplus.plugins.Page;
import java.util.Map;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 活动报名表 服务类
 * </p>
 *
 */
public interface SysHdbmbService extends IService<SysHdbmb> {
    Page<SysHdbmb> queryPageList(Page<SysHdbmb> page, Map<String, Object> map);
    void deleteBatch(String[] hdbmbIds);
}
