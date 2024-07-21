package cn.jeefast.system.service;

import cn.jeefast.system.entity.SysHdwjtj;
import com.baomidou.mybatisplus.plugins.Page;
import java.util.Map;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 活动附件表 服务类
 * </p>
 *
 */
public interface SysHdwjtjService extends IService<SysHdwjtj> {
    Page<SysHdwjtj> queryPageList(Page<SysHdwjtj> page, Map<String, Object> map);
    void deleteBatch(String[] hdwjtjIds);
}
