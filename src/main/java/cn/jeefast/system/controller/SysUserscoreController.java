package cn.jeefast.system.controller;
import cn.jeefast.common.annotation.Log;
import cn.jeefast.common.utils.Query;
import cn.jeefast.common.utils.R;
import cn.jeefast.common.validator.ValidatorUtils;
import cn.jeefast.system.entity.SysUser;
import cn.jeefast.system.entity.SysUserScore;
import cn.jeefast.system.entity.SysUserToken;
import cn.jeefast.system.service.SysUserScoreService;
import cn.jeefast.system.service.SysUserService;
import cn.jeefast.system.service.SysUserTokenService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.jeefast.common.base.BaseController;

import java.net.UnknownHostException;
import java.sql.SQLOutput;
import java.util.*;
@RestController
@RequestMapping("/sysUserScore")
public class SysUserscoreController extends BaseController{
    @Autowired
    private SysUserScoreService sysUserScoreService;
    @Autowired
    private SysUserTokenService sysUserTokenService;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 类型表
     */
    @RequestMapping("/list")

    public R list(@RequestParam Map<String, Object> params,@RequestParam String token) throws UnknownHostException {
        //查询列表数据
        SysUserToken sysUserToken = sysUserTokenService.selectOne(new EntityWrapper<SysUserToken>().eq("token",token));
        SysUser user = sysUserService.selectById(sysUserToken.getUserId());
        params.put("username", user.getUsername());
        Query query = new Query(params);
        System.out.println(query);
        Page<SysUserScore> pageUtil = new Page<SysUserScore>(query.getPage(), query.getLimit());

        Page<SysUserScore> page = sysUserScoreService.queryPageList(pageUtil, query);
        return R.ok().put("page", page);
    }

    /**
     * 类型表信息
     */
    @RequestMapping("/info/{typeId}")

    public R info(@PathVariable("typeId") String typeId) {
        SysUserScore type = sysUserScoreService.selectById(typeId);
        return R.ok().put("type", type);
    }

    /**
     * 类型表信息
     */
    @RequestMapping("/getTypes")
    public R getTypes() {
        List<SysUserScore> types = sysUserScoreService.selectList(new EntityWrapper<>());
        return R.ok().put("types", types);
    }

    //    @RequestMapping("/list")
//    @RequiresPermissions("sys:type:list")
//    public R list(@RequestParam Map<String, Object> params) throws UnknownHostException {
//        //查询列表数据
//        Query query = new Query(params);
//        Page<SysType> pageUtil = new Page<SysType>(query.getPage(), query.getLimit());
//
//        Page<SysType> page = sysTypeService.queryPageList(pageUtil, query);
//        return R.ok().put("page", page);
//    }
    @RequestMapping("/getUserscore")
    @ResponseBody
    public R getUserscore(@RequestParam("token") String token) {
        SysUserToken sysUserToken = sysUserTokenService.selectOne(new EntityWrapper<SysUserToken>().eq("token",token));
        SysUser user = sysUserService.selectById(sysUserToken.getUserId());
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("username", user.getRealname());
        //System.out.println(user.getRealname());
        Query query = new Query(queryMap);
        Page<SysUserScore> pageUtil = new Page<SysUserScore>(query.getPage(), query.getLimit());
        Page<SysUserScore> page = sysUserScoreService.queryPageList(pageUtil, query);
        return R.ok().put("page", page);
    }
}
