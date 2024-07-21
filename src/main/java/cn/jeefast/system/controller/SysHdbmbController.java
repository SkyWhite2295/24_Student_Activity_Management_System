package cn.jeefast.system.controller;

import java.util.*;

import cn.jeefast.common.annotation.Log;
import cn.jeefast.common.utils.Query;
import cn.jeefast.common.utils.R;
import cn.jeefast.common.validator.ValidatorUtils;
import cn.jeefast.system.entity.SysHdbmb;
import cn.jeefast.system.entity.SysUser;
import cn.jeefast.system.entity.SysUserRole;
import cn.jeefast.system.entity.TMaterialFile;
import cn.jeefast.system.service.SysUserRoleService;
import cn.jeefast.system.service.TMaterialFileService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.plugins.Page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;


import cn.jeefast.system.service.SysHdbmbService;
import org.springframework.web.bind.annotation.RestController;
import cn.jeefast.common.base.BaseController;

/**
 * <p>
 * 活动报名表 前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/sysHdbmb")
public class SysHdbmbController extends BaseController {
    @Autowired
    private SysHdbmbService sysHdbmbService;

    @Autowired
    private TMaterialFileService tMaterialFileService;

    @Autowired
    private SysUserRoleService sysUserRoleService;


    /**
     * 活动报名表列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:hdbmb:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        SysUser dluser = getUser();
        //管理远看全部
        List<SysUserRole> sysUserRolesone = sysUserRoleService.selectList(new EntityWrapper<SysUserRole>().eq("user_id",dluser.getUserId()));
        String usernameParam = dluser.getUsername();
        if(sysUserRolesone.size()>0){
            for(int i=0;i<sysUserRolesone.size();i++){
                SysUserRole sysUserRole = sysUserRolesone.get(i);
                if((sysUserRole.getRoleId()+"").equals("1")|(sysUserRole.getRoleId()+"").equals("3")){
                    usernameParam = null;
                }
            }
        }
        params.put("usernameParam",usernameParam);
        Query query = new Query(params);
        Page<SysHdbmb> pageUtil = new Page<SysHdbmb>(query.getPage(), query.getLimit());
        Page<SysHdbmb> page = sysHdbmbService.queryPageList(pageUtil, query);
        return R.ok().put("page", page);
    }


    /**
     * 活动报名表
     */
    @RequestMapping("/info/{hdbmbId}")
    @RequiresPermissions("sys:hdbmb:info")
    public R info(@PathVariable("hdbmbId") String hdbmbId) {
        SysHdbmb hdbmb = sysHdbmbService.selectById(hdbmbId);
        //获取附件列表
        List<TMaterialFile> tMaterialFiles = tMaterialFileService.selectList(new EntityWrapper<TMaterialFile>().eq("parentid",hdbmb.getId()));
        List<Map<String,Object>> mapList = new ArrayList<>();
        if(!tMaterialFiles.isEmpty()){
            for(TMaterialFile tMaterialFile:tMaterialFiles){
                Map<String,Object> map =new HashMap<>();
                map.put("id",tMaterialFile.getId());
                map.put("filePath",tMaterialFile.getSfilename());
                map.put("fileName",tMaterialFile.getSaccessoryname());
                mapList.add(map);
            }

        }
        JSONArray json = (JSONArray) JSONArray.toJSON(mapList);

        hdbmb.setFiles(json);
        return R.ok().put("hdbmb", hdbmb);
    }

    /**
     * 获取所有活动报名表
     */
    @RequestMapping("/getHdbmbs")
    public R getHdbmbs() {
        List<SysHdbmb> hdbmbs = sysHdbmbService.selectList(new EntityWrapper<SysHdbmb>().orderBy(true,"updatetime",false));
        return R.ok().put("hdbmbs", hdbmbs);
    }

    /**
     * 保存活动报名表
     */
    @Log("保存活动报名表")
    @RequestMapping("/save")
    @RequiresPermissions("sys:hdbmb:save")
    public R save(@RequestBody SysHdbmb hdbmb) {
        ValidatorUtils.validateEntity(hdbmb);
        hdbmb.setCreatetime(new Date());
        hdbmb.setCreateuser(getUser().getUsername());
        hdbmb.setUpdatetime(new Date());
        hdbmb.setUpdateuser(getUser().getUsername());
        sysHdbmbService.insert(hdbmb);

        if(hdbmb.getFiles() != null){
            tMaterialFileService.setTMaterialFilePrintId(hdbmb.getFiles(),hdbmb.getId());
        }
        return R.ok();
    }

    /**
     * 修改活动报名表
     */
    @Log("修改活动报名表")
    @RequestMapping("/update")
    @RequiresPermissions("sys:hdbmb:update")
    public R update(@RequestBody SysHdbmb hdbmb) {
        ValidatorUtils.validateEntity(hdbmb);
        hdbmb.setUpdatetime(new Date());
        hdbmb.setUpdateuser(getUser().getUsername());
        sysHdbmbService.updateById(hdbmb);
        if(hdbmb.getFiles() != null){
            tMaterialFileService.setTMaterialFilePrintId(hdbmb.getFiles(),hdbmb.getId());
        }
        return R.ok();
    }

    /**
     * 删除活动报名表
     */
    @Log("删除活动报名表")
    @RequestMapping("/delete")
    @RequiresPermissions("sys:hdbmb:delete")
    public R delete(@RequestBody String[] hdbmbIds) {
        sysHdbmbService.deleteBatch(hdbmbIds);
        return R.ok();
    }

    /**
     * 保存分数
     */
    @Log("保存分数")
    @RequestMapping("/saveSzfs")
    public R saveSzfs(@RequestBody JSONObject param) {
        String szfsid  =param.getString("szfsid");
        Double df = param.getDouble("df");
        SysHdbmb sysHdbmb = sysHdbmbService.selectById(szfsid);
        sysHdbmb.setDf(df);
        sysHdbmbService.updateById(sysHdbmb);
        return R.ok();
    }

    /**
     * 通过信息            tg
     */
    @Log("通过信息  ")
    @RequestMapping("/tg")
    public R tg(@RequestBody JSONObject param) {
        JSONArray ids = param.getJSONArray("ids");
        String tg = param.getString("tg");

        String warning1=null;

        if(ids.size()>0){
            for (int i = 0; i < ids.size(); i++) {
                SysHdbmb sysHdbmb = sysHdbmbService.selectById(ids.get(i).toString());
                if((sysHdbmb.getTg()+"").equals("1") || (sysHdbmb.getTg()+"").equals("2")){
                    warning1="选中条目中含有已通过/未通过的记录！此类记录将不允许重新设置通过状态,请刷新*看最新状态!";
                }
                else{
                    sysHdbmb.setTg(tg);
                    sysHdbmbService.updateById(sysHdbmb);
                }
            }
        }
        if(warning1==null) return R.ok();
        else return R.error(warning1);
    }





}
