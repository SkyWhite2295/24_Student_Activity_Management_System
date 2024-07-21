package cn.jeefast.system.controller;
import java.util.*;
import cn.jeefast.common.annotation.Log;
import cn.jeefast.common.utils.Query;
import cn.jeefast.common.utils.R;
import cn.jeefast.common.validator.ValidatorUtils;
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

import org.springframework.web.bind.annotation.RestController;
import cn.jeefast.common.base.BaseController;
import cn.jeefast.common.annotation.Log;
import cn.jeefast.system.entity.Sysscore;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;

import cn.jeefast.system.service.SysscoreService;
import org.springframework.web.bind.annotation.RestController;
import cn.jeefast.common.base.BaseController;
@RestController
@RequestMapping("/sysXuefen")
public class SysXuefenController {
    @Autowired
    private SysscoreService sysscoreService;

    @Autowired
    private TMaterialFileService tMaterialFileService;


    /**
     * 学分管理列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        Page<Sysscore> pageUtil = new Page<Sysscore>(query.getPage(), query.getLimit());
        Page<Sysscore> page = sysscoreService.queryPageList(pageUtil, query);
        return R.ok().put("page", page);
    }


    /**
     * 学分管理
     */
    @RequestMapping("/info/{xuefenId}")
    @RequiresPermissions("sys:xuefen:info")
    public R info(@PathVariable("xuefenId") String xuefenId) {
        Sysscore xuefen = sysscoreService.selectById(xuefenId);

        return R.ok().put("xuefen", xuefen);
    }

    /**
     * 获取所有学生的学分
     */
    @RequestMapping("/getXuefens")
    public R getXuefens() {
        List<Sysscore> xuefens = sysscoreService.selectList(new EntityWrapper<Sysscore>().orderBy(true,"username",false));
        return R.ok().put("xuefens", xuefens);
    }

    /**
     * 更新学分
     */
    @RequestMapping("/gx")
    public R gx(@RequestBody JSONObject param) {
        JSONArray ids = param.getJSONArray("ids");
        JSONArray hdfzList = param.getJSONArray("hdfz");
        JSONArray xflxList = param.getJSONArray("xflx");
        JSONArray tgList = param.getJSONArray("tg");
        JSONArray usernameList = param.getJSONArray("username");

        String warning1=null;

        if (ids.size() > 0) {
            for (int i = 0; i < ids.size(); i++) {
                String id = ids.getString(i);
                String hdfz = hdfzList.getString(i);
                String xflx = xflxList.getString(i);
                String tg = tgList.getString(i);
                String username = usernameList.getString(i);
                double gx = Double.parseDouble(hdfz);
                Sysscore sysscore = sysscoreService.selectById(username);

                System.out.println("meiyu:"+sysscore.getMeiyu());
                System.out.println("gx:"+gx);
                System.out.println("tg:"+tg);
                System.out.println("xflx:"+xflx);
                if(tg.equals("通过") || tg.equals("未通过")){
                    warning1="选中条目中含有已通过/未通过的记录！此类记录将不允许重新设置通过状态,请刷新*看最新状态!";
                }
                else {
                    if(xflx.equals("美育")){
                        sysscore.setMeiyu(sysscore.getMeiyu()+gx);
                    }else if(xflx.equals("劳育")){
                        sysscore.setLaoyu(sysscore.getLaoyu()+gx);
                    }else if(xflx.equals("德育")){
                        sysscore.setDeyu(sysscore.getDeyu()+gx);
                    }else if(xflx.equals("体育")){
                        sysscore.setTiyu(sysscore.getTiyu()+gx);
                    }else if(xflx.equals("智育")){
                        System.out.println("sysscore.getZhiyu(): " + sysscore.getZhiyu() );
                        sysscore.setZhiyu(sysscore.getZhiyu()+gx);
                        System.out.println("sysscore.getZhiyu(): " + sysscore.getZhiyu() );
                    }
                }

                // 输出 hdfzList 的内容到控制台
                System.out.println("hdfzList: " + hdfz );
                System.out.println("username: " + username);

                sysscoreService.updateById(sysscore);
            }
        }
        if(warning1==null) return R.ok();
        else return R.error(warning1);

    }


}
