package cn.jeefast.system.controller;


import cn.jeefast.common.base.BaseController;
import cn.jeefast.common.utils.R;
import cn.jeefast.system.entity.SysUser;
import cn.jeefast.system.entity.SysUserToken;
import cn.jeefast.system.service.SysUserService;
import cn.jeefast.system.service.SysUserTokenService;
import cn.jeefast.system.service.WebSocketServer;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/sysLiaotian")
public class SysLiaotianontroller extends BaseController {

    @Autowired
    private SysUserTokenService sysUserTokenService;

    @Autowired
    private SysUserService sysUserService;


    @RequestMapping(value="/currentuser",method = RequestMethod.GET)
    @ResponseBody
    public R currentuser(HttpSession httpSession){
        Long uid = (Long)httpSession.getAttribute("uid");
        System.out.println("uiduiduiduiduiduid"+uid);
        SysUser sysUser = getUser();
        return R.ok().put("name",sysUser.getRealname()).put("uid",uid);
    }


    @RequestMapping("/onlineusers")
    @ResponseBody
    public Set<String> onlineusers(@RequestParam("currentuser") String currentuser) {
        ConcurrentHashMap<String, Session> map = WebSocketServer.getSessionPools();
        Set<String> set = map.keySet();
        Iterator<String> it = set.iterator();
        Set<String> nameset = new HashSet<String>();
        while (it.hasNext()) {
            String entry = it.next();
            if (!entry.equals(currentuser))
                nameset.add(entry);
        }
        return nameset;
    }

    @RequestMapping("/aaaa")
    @ResponseBody
    public R aaaa(@RequestParam("aaaa") String aaaa) {
        SysUserToken sysUserToken = sysUserTokenService.selectOne(new EntityWrapper<SysUserToken>().eq("token",aaaa));
        SysUser user = sysUserService.selectById(sysUserToken.getUserId());
        System.out.println("useruseruseruser"+user);
        return R.ok().put("sysuser",user);
    }

    @RequestMapping("/getUser")
    @ResponseBody
    public R getUser(@RequestParam("tjarray") Map<String,Object> tjarray) {
        SysUserToken sysUserToken = sysUserTokenService.selectOne(new EntityWrapper<SysUserToken>().eq("token",tjarray.get("token")+""));
        SysUser user = sysUserService.selectById(sysUserToken.getUserId());
        System.out.println("useruseruseruser"+user);
        return R.ok().put("sysuser",user);
    }
}
