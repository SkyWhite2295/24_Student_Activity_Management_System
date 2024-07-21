package cn.jeefast.system.service.impl;
import com.baomidou.mybatisplus.plugins.Page;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import cn.jeefast.system.entity.SysHdbmb;
import cn.jeefast.system.dao.SysHdbmbDao;
import cn.jeefast.system.service.SysHdbmbService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 活动报名表 服务实现类
 * </p>
 *
 */
@Service
public class SysHdbmbServiceImpl extends ServiceImpl<SysHdbmbDao, SysHdbmb> implements SysHdbmbService {
    @Autowired
    private SysHdbmbDao sysHdbmbDao;

    @Override
    public Page<SysHdbmb> queryPageList(Page<SysHdbmb> page, Map<String, Object> map) {
        page.setRecords(sysHdbmbDao.queryPageList(page, map));
        return page;
    }

    @Override
    public void deleteBatch(String[] hdbmbIds) {
        sysHdbmbDao.deleteBatch(hdbmbIds);
    }
}
