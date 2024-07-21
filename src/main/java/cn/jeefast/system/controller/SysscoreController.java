//package cn.jeefast.system.controller;
//import java.util.*;
//import cn.jeefast.common.annotation.Log;
//import cn.jeefast.common.utils.Query;
//import cn.jeefast.common.utils.R;
//import cn.jeefast.common.validator.ValidatorUtils;
//import cn.jeefast.system.entity.Sysscore;
//import cn.jeefast.system.entity.TMaterialFile;
//import cn.jeefast.system.service.TMaterialFileService;
//import com.alibaba.fastjson.JSONArray;
//import com.baomidou.mybatisplus.mapper.EntityWrapper;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import com.baomidou.mybatisplus.plugins.Page;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//
//import cn.jeefast.system.service.SysscoreService;
//import org.springframework.web.bind.annotation.RestController;
//import cn.jeefast.common.base.BaseController;
//@RestController
//@RequestMapping("/sysXuefen")
//public class SysscoreController extends BaseController {
//    @Autowired
//    private SysscoreService sysscoreService;
//
//    @Autowired
//    private TMaterialFileService tMaterialFileService;
//
//
//
//    /**
//     * 学分管理列表
//     */
//    @RequestMapping("/list")
//    public R list(@RequestParam Map<String, Object> params) {
//        //查询列表数据
//        Query query = new Query(params);
//        Page<Sysscore> pageUtil = new Page<Sysscore>(query.getPage(), query.getLimit());
//        Page<Sysscore> page = sysscoreService.queryPageList(pageUtil, query);
//        return R.ok().put("page", page);
//    }
//
//
//    /**
//     * 学分管理
//     */
//    @RequestMapping("/info/{xuefenId}")
//    @RequiresPermissions("sys:xuefen:info")
//    public R info(@PathVariable("xuefenId") String xuefenId) {
//        Sysscore xuefen = sysscoreService.selectById(xuefenId);
//
//        return R.ok().put("xuefen", xuefen);
//    }
//
//    /**
//     * 获取所有学生的学分
//     */
//    @RequestMapping("/getXuefens")
//    public R getXuefens() {
//        List<Sysscore> xuefens = sysscoreService.selectList(new EntityWrapper<Sysscore>().orderBy(true,"username",false));
//        return R.ok().put("xuefens", xuefens);
//    }
//
//}