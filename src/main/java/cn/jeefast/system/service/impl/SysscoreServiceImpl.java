package cn.jeefast.system.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import cn.jeefast.system.entity.Sysscore;
import cn.jeefast.system.dao.SysscoreDao;
import cn.jeefast.system.service.SysscoreService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
@Service
public class SysscoreServiceImpl  extends ServiceImpl<SysscoreDao, Sysscore> implements SysscoreService{
    @Autowired
    private SysscoreDao sysscoreDao;

    @Override
    public Page<Sysscore> queryPageList(Page<Sysscore> page, Map<String, Object> map) {
        page.setRecords(sysscoreDao.queryPageList(page, map));
        return page;
    }

}