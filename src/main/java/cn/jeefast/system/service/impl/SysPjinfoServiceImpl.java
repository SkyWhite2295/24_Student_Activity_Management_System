package cn.jeefast.system.service.impl;

import cn.jeefast.system.entity.SysPjinfo;
import cn.jeefast.system.dao.SysPjinfoDao;
import cn.jeefast.system.service.SysPjinfoService;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 */
@Service
public class SysPjinfoServiceImpl extends ServiceImpl<SysPjinfoDao, SysPjinfo> implements SysPjinfoService {

    @Autowired
    private SysPjinfoDao sysPjinfoDao;

    @Override
    public Page<SysPjinfo> queryPageList(Page<SysPjinfo> page, Map<String, Object> map) {
        page.setRecords(sysPjinfoDao.queryPageList(page, map));
        return page;
    }

    @Override
    public void deleteBatch(String[] pjinfoIds) {
        sysPjinfoDao.deleteBatch(pjinfoIds);
    }
}
