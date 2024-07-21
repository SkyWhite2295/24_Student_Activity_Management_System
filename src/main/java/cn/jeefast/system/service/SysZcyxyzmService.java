package cn.jeefast.system.service;

import cn.jeefast.system.entity.SysZcyxyzm;
import com.baomidou.mybatisplus.plugins.Page;
import java.util.Map;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 注册邮箱验证码记录表 服务类
 * </p>
 *
 */
public interface SysZcyxyzmService extends IService<SysZcyxyzm> {
    Page<SysZcyxyzm> queryPageList(Page<SysZcyxyzm> page, Map<String, Object> map);
    int save(SysZcyxyzm sysZcyxyzm);
    void deleteBatch(String[] zcyxyzmIds);
}
