package cn.jeefast.system.service.impl;
import com.baomidou.mybatisplus.plugins.Page;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import cn.jeefast.system.entity.SysZcyxyzm;
import cn.jeefast.system.dao.SysZcyxyzmDao;
import cn.jeefast.system.service.SysZcyxyzmService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 注册邮箱验证码记录表 服务实现类
 * </p>
 *
 */
@Service
public class SysZcyxyzmServiceImpl extends ServiceImpl<SysZcyxyzmDao, SysZcyxyzm> implements SysZcyxyzmService {
    @Autowired
    private SysZcyxyzmDao sysZcyxyzmDao;

    @Override
    public Page<SysZcyxyzm> queryPageList(Page<SysZcyxyzm> page, Map<String, Object> map) {
        page.setRecords(sysZcyxyzmDao.queryPageList(page, map));
        return page;
    }

    @Override
    public int save(SysZcyxyzm sysZcyxyzm) {
        System.out.println("aaaaaaa"+sysZcyxyzm);
        return sysZcyxyzmDao.save(sysZcyxyzm);
    }

    @Override
    public void deleteBatch(String[] zcyxyzmIds) {
        sysZcyxyzmDao.deleteBatch(zcyxyzmIds);
    }
}
