package cn.jeefast.system.controller;

import java.util.*;

import cn.jeefast.common.annotation.Log;
import cn.jeefast.common.utils.Query;
import cn.jeefast.common.utils.R;
import cn.jeefast.common.validator.ValidatorUtils;
import cn.jeefast.system.entity.SysHdwjtj;
import cn.jeefast.system.entity.TMaterialFile;
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


import cn.jeefast.system.service.SysHdwjtjService;
import org.springframework.web.bind.annotation.RestController;
import cn.jeefast.common.base.BaseController;

/**
 * <p>
 * 活动附件表 前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/sysHdwjtj")
public class SysHdwjtjController extends BaseController {
        @Autowired
        private SysHdwjtjService sysHdwjtjService;

        @Autowired
        private TMaterialFileService tMaterialFileService;



        /**
         * 活动附件表列表
         */
        @RequestMapping("/list")
        @RequiresPermissions("sys:hdwjtj:list")
        public R list(@RequestParam Map<String, Object> params) {
            //查询列表数据
            Query query = new Query(params);
            Page<SysHdwjtj> pageUtil = new Page<SysHdwjtj>(query.getPage(), query.getLimit());
            Page<SysHdwjtj> page = sysHdwjtjService.queryPageList(pageUtil, query);
            return R.ok().put("page", page);
        }


        /**
        * 活动附件表
        */
        @RequestMapping("/info/{hdwjtjId}")
        @RequiresPermissions("sys:hdwjtj:info")
        public R info(@PathVariable("hdwjtjId") String hdwjtjId) {
             SysHdwjtj hdwjtj = sysHdwjtjService.selectById(hdwjtjId);
             //获取附件列表
             List<TMaterialFile> tMaterialFiles = tMaterialFileService.selectList(new EntityWrapper<TMaterialFile>().eq("parentid",hdwjtj.getId()));
             List<Map<String,Object>> mapList = new ArrayList<>();
             if(!tMaterialFiles.isEmpty()){
                     for(TMaterialFile tMaterialFile:tMaterialFiles){
                          Map<String,Object> map =new HashMap<>();
                          map.put("id",tMaterialFile.getId());
                          map.put("filePath",tMaterialFile.getSfilename());
                         map.put("username",hdwjtj.getUsername());
                          map.put("fileName",tMaterialFile.getSaccessoryname());
                          mapList.add(map);
                     }

                }
                JSONArray json = (JSONArray) JSONArray.toJSON(mapList);

                hdwjtj.setFiles(json);
                return R.ok().put("hdwjtj", hdwjtj);
        }

        /**
         * 获取所有活动附件表
         */
        @RequestMapping("/getHdwjtjs")
        public R getHdwjtjs() {
             List<SysHdwjtj> hdwjtjs = sysHdwjtjService.selectList(new EntityWrapper<SysHdwjtj>().orderBy(true,"updatetime",false));
             return R.ok().put("hdwjtjs", hdwjtjs);
        }

        /**
        * 保存活动附件表
        */
        @Log("保存活动附件表")
        @RequestMapping("/save")
        @RequiresPermissions("sys:hdwjtj:save")
        public R save(@RequestBody SysHdwjtj hdwjtj) {
                ValidatorUtils.validateEntity(hdwjtj);
                hdwjtj.setCreatetime(new Date());
                hdwjtj.setCreateuser(getUser().getUsername());
                hdwjtj.setUpdatetime(new Date());
                hdwjtj.setUpdateuser(getUser().getUsername());
                sysHdwjtjService.insert(hdwjtj);

                if(hdwjtj.getFiles() != null){
                        tMaterialFileService.setTMaterialFilePrintId(hdwjtj.getFiles(),hdwjtj.getId());
                }
                return R.ok();
                }

        /**
         * 修改活动附件表
         */
        @Log("修改活动附件表")
        @RequestMapping("/update")
        @RequiresPermissions("sys:hdwjtj:update")
        public R update(@RequestBody SysHdwjtj hdwjtj) {
                ValidatorUtils.validateEntity(hdwjtj);
                hdwjtj.setUpdatetime(new Date());
                hdwjtj.setUpdateuser(getUser().getUsername());
                sysHdwjtjService.updateById(hdwjtj);
                if(hdwjtj.getFiles() != null){
                        tMaterialFileService.setTMaterialFilePrintId(hdwjtj.getFiles(),hdwjtj.getId());
                }
                return R.ok();
                }

        /**
         * 删除活动附件表
         */
        @Log("删除活动附件表")
        @RequestMapping("/delete")
        @RequiresPermissions("sys:hdwjtj:delete")
        public R delete(@RequestBody String[] hdwjtjIds) {
                sysHdwjtjService.deleteBatch(hdwjtjIds);
                return R.ok();
        }




    /**
     * 审批信息
     */
    @Log("审批信息")
    @RequestMapping("/sp")
    public R sp(@RequestBody JSONObject param) {
        JSONArray ids = param.getJSONArray("ids");
        String spzt = param.getString("spzt");
        if(ids.size()>0){
            for (int i = 0; i < ids.size(); i++) {
                SysHdwjtj sysHdwjtj = sysHdwjtjService.selectById(ids.get(i).toString());
                sysHdwjtj.setSpzt(spzt);
                sysHdwjtj.setSpry(getUser().getUsername());
                sysHdwjtjService.updateById(sysHdwjtj);
            }
        }
        return R.ok();
    }

}
