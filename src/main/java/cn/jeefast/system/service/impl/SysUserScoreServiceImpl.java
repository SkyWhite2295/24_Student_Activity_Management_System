package cn.jeefast.system.service.impl;

import cn.jeefast.system.dao.SysUserScoreDao;
import cn.jeefast.system.entity.SysUserScore;
import cn.jeefast.system.entity.Sysscore;
import cn.jeefast.system.service.SysUserScoreService;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import cn.jeefast.system.entity.SysType;
import cn.jeefast.system.dao.SysTypeDao;
import cn.jeefast.system.service.SysTypeService;
import java.util.Map;
import org.springframework.stereotype.Service;
@Service
public class SysUserScoreServiceImpl extends ServiceImpl<SysUserScoreDao, SysUserScore> implements SysUserScoreService{
    @Autowired
    private SysUserScoreDao sysUserScoreDao;

    @Override
    public Page<SysUserScore> queryPageList(Page<SysUserScore> page, Map<String, Object> map) {
        page.setRecords(sysUserScoreDao.queryPageList(page, map));
        return page;
    }
    @Override
    public void deleteBatch(String[] ids) {
        sysUserScoreDao.deleteBatch(ids);
    }
}
