package cn.jeefast.system.controller;


import cn.jeefast.common.annotation.Log;
import cn.jeefast.common.utils.Query;
import cn.jeefast.common.utils.R;
import cn.jeefast.common.validator.ValidatorUtils;
import cn.jeefast.system.entity.SysXinwen;
import cn.jeefast.system.entity.TMaterialFile;
import cn.jeefast.system.service.SysXinwenService;
import cn.jeefast.system.service.TMaterialFileService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.jeefast.common.base.BaseController;

import java.util.*;

/**
 * <p>
 * 活动信息 前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/sysXinwen")
public class SysXinwenController extends BaseController {

    @Autowired
    private SysXinwenService sysXinwenService;

    @Autowired
    private TMaterialFileService tMaterialFileService;

    /**
     * 活动信息列表      list
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:xinwen:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        params.put("type","1");
        Query query = new Query(params);
        Page<SysXinwen> pageUtil = new Page<SysXinwen>(query.getPage(), query.getLimit());
        Page<SysXinwen> page = sysXinwenService.queryPageList(pageUtil, query);
        return R.ok().put("page", page);
    }

    /**
     * 通知公告列表
     */
    @RequestMapping("/tzgglist")
    @RequiresPermissions("sys:xinwen:tzgglist")
    public R tzgglist(@RequestParam Map<String, Object> params) {
        //查询列表数据
        params.put("type","2");
        Query query = new Query(params);
        Page<SysXinwen> pageUtil = new Page<SysXinwen>(query.getPage(), query.getLimit());
        Page<SysXinwen> page = sysXinwenService.queryPageList(pageUtil, query);
        return R.ok().put("page", page);
    }


    /**
     * 活动信息详情        info
     */
    @RequestMapping("/info/{xinwenId}")
    @RequiresPermissions("sys:xinwen:info")
    public R info(@PathVariable("xinwenId") String xinwenId) {
        SysXinwen xinwen = sysXinwenService.selectById(xinwenId);

        System.out.println("xinwenxinwenxinwenxinwen"+xinwen);

        return R.ok().put("xinwen", xinwen);
    }

    /**
     * 保存活动信息        save
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:xinwen:save")
    public R save(@RequestBody SysXinwen xinwen) {
        ValidatorUtils.validateEntity(xinwen);
        xinwen.setZuozhe(getUser().getRealname());
        xinwen.setCreateuser(getUser().getUsername());
        xinwen.setCreatetime(new Date());
        xinwen.setUpdateuser(getUser().getUsername());
        xinwen.setUpdatetime(new Date());
        sysXinwenService.insert(xinwen);
        return R.ok();
    }

    /**
     * 修改活动信息        update
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:xinwen:update")
    public R update(@RequestBody SysXinwen xinwen) {
        ValidatorUtils.validateEntity(xinwen);
        xinwen.setUpdateuser(getUser().getUsername());
        xinwen.setUpdatetime(new Date());
        sysXinwenService.updateById(xinwen);
        return R.ok();
    }

    /**
     * 删除活动信息       delete
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:xinwen:delete")
    public R delete(@RequestBody String[] xinwenIds) {
        sysXinwenService.deleteBatch(xinwenIds);
        return R.ok();
    }

    /**
     * 审批信息            sp
     */
    @Log("审批信息")
    @RequestMapping("/sp")
    public R sp(@RequestBody JSONObject param) {
        JSONArray ids = param.getJSONArray("ids");
        String spzt = param.getString("spzt");
        if(ids.size()>0){
            for (int i = 0; i < ids.size(); i++) {
                SysXinwen sysXinwen = sysXinwenService.selectById(ids.get(i).toString());
                sysXinwen.setSpzt(spzt);
                sysXinwenService.updateById(sysXinwen);
            }
        }
        return R.ok();
    }
}
