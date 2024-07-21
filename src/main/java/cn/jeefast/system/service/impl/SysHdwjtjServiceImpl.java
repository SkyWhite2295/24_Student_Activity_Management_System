package cn.jeefast.system.service.impl;
import com.baomidou.mybatisplus.plugins.Page;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import cn.jeefast.system.entity.SysHdwjtj;
import cn.jeefast.system.dao.SysHdwjtjDao;
import cn.jeefast.system.service.SysHdwjtjService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 活动附件表 服务实现类
 * </p>
 *
 */
@Service
public class SysHdwjtjServiceImpl extends ServiceImpl<SysHdwjtjDao, SysHdwjtj> implements SysHdwjtjService {
    @Autowired
    private SysHdwjtjDao sysHdwjtjDao;

    @Override
    public Page<SysHdwjtj> queryPageList(Page<SysHdwjtj> page, Map<String, Object> map) {
        page.setRecords(sysHdwjtjDao.queryPageList(page, map));
        return page;
    }

    @Override
    public void deleteBatch(String[] hdwjtjIds) {
        sysHdwjtjDao.deleteBatch(hdwjtjIds);
    }
}
