package cn.jeefast.system.controller;


import cn.jeefast.common.annotation.Log;
import cn.jeefast.common.utils.Query;
import cn.jeefast.common.utils.R;
import cn.jeefast.common.validator.ValidatorUtils;
import cn.jeefast.system.entity.SysPjinfo;
import cn.jeefast.system.service.SysDeptService;
import cn.jeefast.system.service.SysPjinfoService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.jeefast.common.base.BaseController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统评价前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/sysPjinfo")
public class SysPjinfoController extends BaseController {

    @Autowired
    private SysPjinfoService sysPjinfoService;

    @Autowired
    private SysDeptService sysDeptService;

    /**
     * 系统评价列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:pjinfo:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        Page<SysPjinfo> pageUtil = new Page<SysPjinfo>(query.getPage(), query.getLimit());
        Page<SysPjinfo> page = sysPjinfoService.queryPageList(pageUtil, query);
        if (page.getRecords().size() > 0) {
            for (int i = 0; i < page.getRecords().size(); i++) {
                SysPjinfo newSysPjinfo = page.getRecords().get(i);
                newSysPjinfo.setDeptName(newSysPjinfo.getDeptid() != null && newSysPjinfo.getDeptid() != "" ? sysDeptService.selectById(newSysPjinfo.getDeptid()).getName() : null);
            }
        }
        return R.ok().put("page", page);
    }

    /**
     * 系统评价信息
     */
    @RequestMapping("/info/{pjinfoId}")
    @RequiresPermissions("sys:pjinfo:info")
    public R info(@PathVariable("pjinfoId") String pjinfoId) {
        System.out.println("pjinfoIdpjinfoIdpjinfoId"+pjinfoId);
        SysPjinfo pjinfo = sysPjinfoService.selectById(pjinfoId);
        return R.ok().put("pjinfo", pjinfo);
    }

    /**
     * 系统评价信息
     */
    @RequestMapping("/infoprent/{pjinfoId}")
    @RequiresPermissions("sys:pjinfo:infoprent")
    public R infoprent(@PathVariable("pjinfoId") String pjinfoId) {
        System.out.println("pjinfoIdpjinfoIdpjinfoId"+pjinfoId);
        SysPjinfo pjinfo = sysPjinfoService.selectById(pjinfoId);
        List<SysPjinfo> sysPjinfoList = new ArrayList<>();
        if(pjinfo.getPrentid() != null){
            sysPjinfoList = sysPjinfoService.selectList(new EntityWrapper<SysPjinfo>().eq("prentid",pjinfo.getId()));
        }
        System.out.println("sysPjinfoListsysPjinfoListsysPjinfoList"+sysPjinfoList);
        System.out.println("sizesizesizesizesizesizesize"+sysPjinfoList.size());
        pjinfo.setSysPjinfoList(sysPjinfoList);

        System.out.println("pjinfopjinfopjinfopjinfopjinfopjinfopjinfopjinfopjinfopjin"+pjinfo);
        return R.ok().put("pjinfo", pjinfo).put("allFiles",sysPjinfoList);
    }

    /**
     * 保存系统评价
     */
    @Log("保存系统评价")
    @RequestMapping("/save")
    @RequiresPermissions("sys:pjinfo:save")
    public R save(@RequestBody SysPjinfo pjinfo) {
        ValidatorUtils.validateEntity(pjinfo);
        pjinfo.setUsername(getUser().getUsername());
        sysPjinfoService.insert(pjinfo);
        return R.ok();
    }

    /**
     * 修改系统评价
     */
    @Log("修改系统评价")
    @RequestMapping("/update")
    @RequiresPermissions("sys:pjinfo:update")
    public R update(@RequestBody SysPjinfo pjinfo) {
        ValidatorUtils.validateEntity(pjinfo);
        pjinfo.setUsername(getUser().getUsername());
        pjinfo.setPrentid("0");
        sysPjinfoService.updateById(pjinfo);

        return R.ok();
    }

    /**
     * 保存评论
     */
    @Log("保存评论")
    @RequestMapping("/savePlxx")
    @RequiresPermissions("sys:pjinfo:savePlxx")
    public R savePlxx(@RequestBody SysPjinfo pjinfo) {
        System.out.println("pjinfopjinfopjinfopjinfo"+pjinfo);
        ValidatorUtils.validateEntity(pjinfo);
        pjinfo.setUsername(getUser().getUsername());
        pjinfo.setPrentid(pjinfo.getId()+"");
        pjinfo.setId(null);
        sysPjinfoService.insert(pjinfo);

        return R.ok();
    }



    /**
     * 删除系统评价
     */
    @Log("删除系统评价")
    @RequestMapping("/delete")
    @RequiresPermissions("sys:pjinfo:delete")
    public R delete(@RequestBody String[] pjinfoIds) {
        sysPjinfoService.deleteBatch(pjinfoIds);
        return R.ok();
    }


}
