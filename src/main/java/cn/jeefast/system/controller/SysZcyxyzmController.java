package cn.jeefast.system.controller;

import java.util.*;

import cn.jeefast.common.annotation.Log;
import cn.jeefast.common.utils.Query;
import cn.jeefast.common.utils.R;
import cn.jeefast.common.validator.ValidatorUtils;
import cn.jeefast.system.entity.SysZcyxyzm;
import cn.jeefast.system.entity.TMaterialFile;
import cn.jeefast.system.service.TMaterialFileService;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.plugins.Page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;


import cn.jeefast.system.service.SysZcyxyzmService;
import org.springframework.web.bind.annotation.RestController;
import cn.jeefast.common.base.BaseController;

/**
 * <p>
 * 注册邮箱验证码记录表 前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/sysZcyxyzm")
public class SysZcyxyzmController extends BaseController {
        @Autowired
        private SysZcyxyzmService sysZcyxyzmService;

        @Autowired
        private TMaterialFileService tMaterialFileService;



        /**
         * 注册邮箱验证码记录表列表
         */
        @RequestMapping("/list")
        @RequiresPermissions("sys:zcyxyzm:list")
        public R list(@RequestParam Map<String, Object> params) {
            //查询列表数据
            Query query = new Query(params);
            Page<SysZcyxyzm> pageUtil = new Page<SysZcyxyzm>(query.getPage(), query.getLimit());
            Page<SysZcyxyzm> page = sysZcyxyzmService.queryPageList(pageUtil, query);
            return R.ok().put("page", page);
        }


        /**
        * 注册邮箱验证码记录表
        */
        @RequestMapping("/info/{zcyxyzmId}")
        @RequiresPermissions("sys:zcyxyzm:info")
        public R info(@PathVariable("zcyxyzmId") String zcyxyzmId) {
             SysZcyxyzm zcyxyzm = sysZcyxyzmService.selectById(zcyxyzmId);
             //获取附件列表
             List<TMaterialFile> tMaterialFiles = tMaterialFileService.selectList(new EntityWrapper<TMaterialFile>().eq("parentid",zcyxyzm.getId()));
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

                zcyxyzm.setFiles(json);
                return R.ok().put("zcyxyzm", zcyxyzm);
        }

        /**
         * 获取所有注册邮箱验证码记录表
         */
        @RequestMapping("/getZcyxyzms")
        public R getZcyxyzms() {
             List<SysZcyxyzm> zcyxyzms = sysZcyxyzmService.selectList(new EntityWrapper<SysZcyxyzm>().orderBy(true,"updatetime",false));
             return R.ok().put("zcyxyzms", zcyxyzms);
        }

        /**
        * 保存注册邮箱验证码记录表
        */
        @Log("保存注册邮箱验证码记录表")
        @RequestMapping("/save")
        @RequiresPermissions("sys:zcyxyzm:save")
        public R save(@RequestBody SysZcyxyzm zcyxyzm) {
                ValidatorUtils.validateEntity(zcyxyzm);
                zcyxyzm.setCreatetime(new Date());
                zcyxyzm.setCreateuser(getUser().getUsername());
                zcyxyzm.setUpdatetime(new Date());
                zcyxyzm.setUpdateuser(getUser().getUsername());
                sysZcyxyzmService.insert(zcyxyzm);

                if(zcyxyzm.getFiles() != null){
                        tMaterialFileService.setTMaterialFilePrintId(zcyxyzm.getFiles(),zcyxyzm.getId());
                }
                return R.ok();
                }

        /**
         * 修改注册邮箱验证码记录表
         */
        @Log("修改注册邮箱验证码记录表")
        @RequestMapping("/update")
        @RequiresPermissions("sys:zcyxyzm:update")
        public R update(@RequestBody SysZcyxyzm zcyxyzm) {
                ValidatorUtils.validateEntity(zcyxyzm);
                zcyxyzm.setUpdatetime(new Date());
                zcyxyzm.setUpdateuser(getUser().getUsername());
                sysZcyxyzmService.updateById(zcyxyzm);
                if(zcyxyzm.getFiles() != null){
                        tMaterialFileService.setTMaterialFilePrintId(zcyxyzm.getFiles(),zcyxyzm.getId());
                }
                return R.ok();
                }

        /**
         * 删除注册邮箱验证码记录表
         */
        @Log("删除注册邮箱验证码记录表")
        @RequestMapping("/delete")
        @RequiresPermissions("sys:zcyxyzm:delete")
        public R delete(@RequestBody String[] zcyxyzmIds) {
                sysZcyxyzmService.deleteBatch(zcyxyzmIds);
                return R.ok();
        }






}
