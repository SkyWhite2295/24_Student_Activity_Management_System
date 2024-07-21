package cn.jeefast.system.service.impl;

import cn.jeefast.system.dao.SysXinwenDao;
import cn.jeefast.system.entity.SysXinwen;
import cn.jeefast.system.service.SysXinwenService;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 活动信息 服务实现类
 * </p>
 *
 * @author theodo
 * @since 2022-03-30
 */
@Service
public class SysXinwenServiceImpl extends ServiceImpl<SysXinwenDao, SysXinwen> implements SysXinwenService {

    @Autowired
    private SysXinwenDao sysXinwenDao;

    @Override
    public Page<SysXinwen> queryPageList(Page<SysXinwen> page, Map<String, Object> map) {
        page.setRecords(sysXinwenDao.queryPageList(page, map));
        return page;
    }

    @Override
    public void deleteBatch(String[] xinwenIds) {
        sysXinwenDao.deleteBatch(xinwenIds);
    }

}
