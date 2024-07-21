package cn.jeefast.system.service;

import cn.jeefast.system.entity.SysXinwen;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

import java.util.Map;

/**
 * <p>
 * 活动信息 服务类
 * </p>
 *
 * @author theodo
 * @since 2022-03-30
 */
public interface SysXinwenService extends IService<SysXinwen> {
    Page<SysXinwen> queryPageList(Page<SysXinwen> page, Map<String, Object> map);
    void deleteBatch(String[] xinwenIds);
}
