package cn.jeefast.system.controller;


import cn.jeefast.common.base.BaseController;
import cn.jeefast.common.utils.R;
import cn.jeefast.common.validator.Assert;
import cn.jeefast.system.entity.*;
import cn.jeefast.system.service.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/sysIndexQd")
public class SysIndexQdController extends BaseController {

    @Value("${server.port}")
    private String serverport;

    @Value("${server.context-path}")
    private String servercontextpath;


    @Autowired
    private SysUserTokenService sysUserTokenService;

    @Autowired
    private SysUserService sysUserService;


    @Autowired
    private TMaterialFileService tMaterialFileService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     * 获取用户信息
     */
    @RequestMapping("/getUser")
    public R getUser(@RequestBody Map<String,Object> tjarray) throws UnknownHostException {
        System.out.println("tjarraytjarray"+tjarray);

        SysUserToken sysUserToken = sysUserTokenService.selectOne(new EntityWrapper<SysUserToken>().eq("token",tjarray.get("token")+""));
        SysUser user = sysUserService.selectById(sysUserToken.getUserId());

        /**
         * 设置头像信息
         */
        List<TMaterialFile> tMaterialFileList = tMaterialFileService.selectList(new EntityWrapper<TMaterialFile>().eq("parentid",user.getUserId()));
//        SysUserToken sysUserToken = sysUserTokenService.selectOne(new EntityWrapper<SysUserToken>().eq("user_id", getUserId()));
        System.out.println("tMaterialFileListtMaterialFileList"+tMaterialFileList);
        InetAddress address = InetAddress.getLocalHost();
        user.setPhotopath("img/usermm.jpg");
        System.out.println("useruseruseruser"+user);


        //获取用户所属的角色列表
        List<Long> roleIdList = sysUserRoleService.queryRoleIdList(user.getUserId());
        user.setRoleIdList(roleIdList);

        //获取附件列表
        List<TMaterialFile> tMaterialFiles = tMaterialFileService.selectList(new EntityWrapper<TMaterialFile>().eq("parentid",user.getUserId()));
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
        user.setFiles(json);

        return R.ok().put("sysuser",user);

    }

    /**
     * 头像更新
     */
    @RequestMapping("/userSaveFile")
    public R userSaveFile(@RequestBody JSONObject param){
        System.out.println("paramparamparam"+ JSON.toJSONString(param));
        String userid = param.getString("userid");
        JSONArray allFiles = param.getJSONArray("allFiles");
        System.out.println("useriduseriduserid"+userid);
        System.out.println("allFilesallFilesallFiles"+JSON.toJSONString(allFiles));
        List<TMaterialFile> tMaterialFiles = tMaterialFileService.selectList(new EntityWrapper<TMaterialFile>().eq("parentid",userid));
        if (tMaterialFiles.size()>0){
            for (int i = 0; i < tMaterialFiles.size(); i++) {
                tMaterialFileService.deleteById(tMaterialFiles.get(i).getId());
            }
        }
        if(allFiles.size()>0){
            for (int i = 0; i < allFiles.size(); i++) {
                Map map = (Map) allFiles.get(i);
                TMaterialFile tMaterialFile = tMaterialFileService.selectById(map.get("id").toString());
                tMaterialFile.setParentid(userid);
                tMaterialFileService.updateById(tMaterialFile);
            }
        }
        return R.ok();
    }

    /**
     * 个人信息更新
     */
    @RequestMapping("/updateuser")
    public R updateuser(@RequestBody SysUser sysUser){
        System.out.println("sysUsersysUser"+sysUser);
        sysUserService.updateById(sysUser);
        return R.ok();

    }

    /**
     * 个人更改密码
     */
    @RequestMapping("/updatepassword")
    public R updatepassword(@RequestBody JSONObject tjarray){
        String token = tjarray.getString("token");
        SysUserToken sysUserToken = sysUserTokenService.selectOne(new EntityWrapper<SysUserToken>().eq("token",token));
        SysUser user = sysUserService.selectById(sysUserToken.getUserId());


        String oldpassword = tjarray.getString("oldpassword");
        String newPassword =  tjarray.getString("password");


        Assert.isBlank(newPassword, "新密码不为能空");

        //sha256加密
        oldpassword = new Sha256Hash(oldpassword, user.getSalt()).toHex();
        //sha256加密
        newPassword = new Sha256Hash(newPassword, user.getSalt()).toHex();
        if(!oldpassword.equals(user.getPassword())){
            return R.error("原密码不正确无法修改密码");
        }

//        SysUser user = new SysUser();
        user.setUserId(user.getUserId());
        user.setPassword(newPassword);
        //更新密码
        sysUserService.updateById(user);
        return R.ok();

    }


    private SysUser getUserInfo(SysUser sysUser) throws UnknownHostException {
//        sysUser.setKeshiname("心理医学科");
        InetAddress address = InetAddress.getLocalHost();
//        /**
//         * 设置头像信息
//         */
//        List<TMaterialFile> tMaterialFileList = tMaterialFileService.selectList(new EntityWrapper<TMaterialFile>().eq("parentid",sysUser.getUserId()));
//        if(tMaterialFileList.size()>0){
//            sysUser.setPhotopath("http://"+address.getHostAddress() +":"+serverport+"/"+servercontextpath+"/upload/"+tMaterialFileList.get(0).getSfilename());
//        }
        sysUser.setPhotopath("img/usermm.jpg");
        return sysUser;
    }

    /**
     * 获取心理咨询师
     */
    @RequestMapping("/getYisheng")
    public R getYisheng(@RequestBody JSONObject param) throws UnknownHostException {
        String id = param.getString("id");
        SysUser yisheng = sysUserService.selectOne(new EntityWrapper<SysUser>().eq("user_id",id));
        yisheng = getUserInfo(yisheng);
        return R.ok().put("yisheng",yisheng);
    }


    @Autowired
    private SysXinwenService sysXinwenService;

    /**
     * 资讯信息列表
     */
    @RequestMapping("/getXinwenListAll")
    public R getXinwenListAll(@RequestBody JSONObject tjarray){
        String type = tjarray.getString("type");
        String qname = tjarray.getString("qname");
        Wrapper w =new EntityWrapper<SysXinwen>();
        if(qname != null){
            w.like("title",qname);
        }
        if(type != null){
            w.eq("type",type);
            if(type.equals("2")){

            }else {
                w.isNotNull("spzt").eq("spzt","1");
            }
        }

        List<SysXinwen> xinwenList = sysXinwenService.selectList(w.orderBy(true,"updatetime",false));
        System.out.println("xinwenListxinwenList"+xinwenList);
        if(xinwenList.size()>0){
            for (int i = 0; i < xinwenList.size(); i++) {
                SysXinwen sysXinwen = xinwenList.get(i);

                List<SysHdbmb> sysHdbmbs = sysHdbmbService.selectList(new EntityWrapper<SysHdbmb>().eq("xinwenid",sysXinwen.getId()));
                sysXinwen.setYbmrs(sysHdbmbs.size());
                if(sysXinwen.getHdlb() != null ){
                    if(sysXinwen.getHdlb().equals("1")){
                        sysXinwen.setHdlbname("娱乐");
                    }else if(sysXinwen.getHdlb().equals("2")){
                        sysXinwen.setHdlbname("思想");
                    }else if(sysXinwen.getHdlb().equals("3")){
                        sysXinwen.setHdlbname("综合素质");
                    }else if(sysXinwen.getHdlb().equals("4")){
                        sysXinwen.setHdlbname("校方");
                    }else if(sysXinwen.getHdlb().equals("5")){
                        sysXinwen.setHdlbname("社团");
                    }else if(sysXinwen.getHdlb().equals("6")){
                        sysXinwen.setHdlbname("个人");
                    }else{
                        sysXinwen.setHdlbname("未知");
                    }
                }

            }
        }

        return R.ok().put("xinwenList",xinwenList);
    }

    @RequestMapping("/getWdhdbmListAll")
    public R getWdhdbmListAll(@RequestBody JSONObject param) {
        String token = param.getString("token");
        SysUserToken sysUserToken = sysUserTokenService.selectOne(new EntityWrapper<SysUserToken>().eq("token", token));
        SysUser user = sysUserService.selectById(sysUserToken.getUserId());
        List<SysHdbmb> hdbmbs = sysHdbmbService.selectList(new EntityWrapper<SysHdbmb>().eq("username",user.getUsername()));
        List<String> xinwenids = new ArrayList<>();
        if(hdbmbs.size()>0){
            for (int i = 0; i < hdbmbs.size(); i++) {
                SysHdbmb sysHdbmb = hdbmbs.get(i);
                xinwenids.add(sysHdbmb.getXinwenid());
            }
        }
        List<SysXinwen> xinwenList = new ArrayList<>();
        if(xinwenids.size()>0){
            xinwenList = sysXinwenService.selectList(new EntityWrapper<SysXinwen>().in("id",xinwenids).orderBy(true,"updatetime",false));
            for (int i = 0; i < xinwenList.size(); i++) {
                SysXinwen sysXinwen = xinwenList.get(i);

                List<SysHdbmb> sysHdbmbs = sysHdbmbService.selectList(new EntityWrapper<SysHdbmb>().eq("xinwenid",sysXinwen.getId()));
                sysXinwen.setYbmrs(sysHdbmbs.size());

                if(sysXinwen.getHdlb().equals("1")){
                    sysXinwen.setHdlbname("娱乐");
                }else if(sysXinwen.getHdlb().equals("2")){
                    sysXinwen.setHdlbname("思想");
                }else if(sysXinwen.getHdlb().equals("3")){
                    sysXinwen.setHdlbname("综合素质");
                }else if(sysXinwen.getHdlb().equals("4")){
                    sysXinwen.setHdlbname("校方");
                }else if(sysXinwen.getHdlb().equals("5")){
                    sysXinwen.setHdlbname("社团");
                }else if(sysXinwen.getHdlb().equals("6")){
                    sysXinwen.setHdlbname("个人");
                }else{
                    sysXinwen.setHdlbname("未知");
                }
            }

        }
        return R.ok().put("xinwenList",xinwenList);
    }

    /**
     * 资讯信息
     */
    @RequestMapping("/getXinwen")
    public R getXinwen(@RequestBody Map<String,Object> tjarray) throws Exception {
        System.out.println("tjarraytjarraytjarray"+tjarray);
        if(tjarray.get("id") == null){
            return R.error("请选择要查看的活动信息");
        }
        String id = tjarray.get("id")+"";
        SysXinwen xinwen =sysXinwenService.selectById(id);
        System.out.println("xinwenxinwenxinwen"+xinwen);

        //获取附件列表
        List<TMaterialFile> tMaterialFiles = tMaterialFileService.selectList(new EntityWrapper<TMaterialFile>().eq("parentid",xinwen.getId()));
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

        xinwen.setFiles(json);

//        if(xinwen.getType() != null && !xinwen.getType().equals("")){
//            String[] cmds = {"curl", "icanhazip.com"};
//            String ip = execCurl(cmds);
//            System.out.println("ipipip" + ip);
//            String qip = "";
//            if(ip.length()>0){
//                for (int i = 0; i < ip.length(); i++) {
//                    String temp = ip.substring(i,i+1);
//                    System.out.println("temptemptemp"+temp);
//                    if(temp.trim().length()>0){
//                        System.out.println("bbbbbbbbbbbb"+temp);
//                        qip = qip+temp;
//                    }
//                }
//            }
//            System.out.println("qipqipqipqip" + qip);
//
////        SysTempinfo sysTempinfo = new SysTempinfo();
////        sysTempinfo.setId(uid);
////        sysTempinfo.setName(ip);
////        sysTempinfoService.insert(sysTempinfo);
//            String path = "E:/GeoLite2-City.mmdb";
//            // 创建 GeoLite2 数据库
//            File database = new File(path);
//            // 读取数据库内容
//            DatabaseReader reader = new DatabaseReader.Builder(database).build();
////        String qid = sysTempinfoService.selectById(uid).getName();
//            String site = IPUtils.getCountry(reader, qip) + "-" + IPUtils.getProvince(reader, qip) + "-" + IPUtils.getCity(reader, qip) + "-" + IPUtils.getLongitude(reader, qip) + "-" + IPUtils.getLatitude(reader, qip);
//
//            String[] sitestr = site.split("-");
//            if(sitestr.length>0){
//                xinwen.setQzbx(sitestr[sitestr.length-1]);
//                xinwen.setQzby(sitestr[sitestr.length-2]);
//            }
//        }

        return R.ok().put("xinwen",xinwen);

    }

//    @Autowired
//    private SysYqfthfbService sysYqfthfbService;
//
//    @Autowired
//    private SysYqfatieService sysYqfatieService;

//    /**
//     * 发帖记录
//     */
//    @RequestMapping("/fabu")
//    public R fabu(@RequestBody JSONObject param){
//        String token = param.getString("token");
//        SysUserToken sysUserToken = sysUserTokenService.selectOne(new EntityWrapper<SysUserToken>().eq("token",token));
//        SysUser user = sysUserService.selectById(sysUserToken.getUserId());
//        String name = param.getString("name");
//        String content = param.getString("content");
//        SysYqfatie sysYqfatie = new SysYqfatie();
//        sysYqfatie.setName(name);
//        sysYqfatie.setContent(content);
//        sysYqfatie.setUpdatetime(new Date());
//        sysYqfatie.setUpdateuser(user.getUsername());
//        sysYqfatie.setCreatetime(new Date());
//        sysYqfatie.setCreateuser(user.getUsername());
//        sysYqfatieService.insert(sysYqfatie);
//        return R.ok();
//    }
//
//
//    /**
//     * 发帖记录
//     */
//    @RequestMapping("/getYqfatieList")
//    public R getYqfatieList(@RequestBody JSONObject param){
//        List<SysYqfatie> yqfatieList = sysYqfatieService.selectList(new EntityWrapper<SysYqfatie>().orderBy(true,"updatetime",false));
//        return R.ok().put("yqfatieList",yqfatieList);
//    }
//
//    /**
//     * 发帖记录
//     */
//    @RequestMapping("/getWdYqfatieList")
//    public R getWdYqfatieList(@RequestBody JSONObject param){
//        String token = param.getString("token");
//        SysUserToken sysUserToken = sysUserTokenService.selectOne(new EntityWrapper<SysUserToken>().eq("token",token));
//        SysUser user = sysUserService.selectById(sysUserToken.getUserId());
//        List<SysYqfatie> yqfatieList = sysYqfatieService.selectList(new EntityWrapper<SysYqfatie>().eq("createuser",user.getUsername()).orderBy(true,"updatetime",false));
//        return R.ok().put("yqfatieList",yqfatieList);
//    }
//
//    /**
//     * 发帖记录
//     */
//    @RequestMapping("/getWdScYqfatieList")
//    public R getWdScYqfatieList(@RequestBody JSONObject param){
//        String token = param.getString("token");
//        SysUserToken sysUserToken = sysUserTokenService.selectOne(new EntityWrapper<SysUserToken>().eq("token",token));
//        SysUser user = sysUserService.selectById(sysUserToken.getUserId());
//        List<SysShoucang> sysShoucangs = sysShoucangService.selectList(new EntityWrapper<SysShoucang>().eq("username",user.getUsername()));
//        List<String> scftids = new ArrayList<>();
//        if(sysShoucangs.size()>0){
//            for (int i = 0; i < sysShoucangs.size(); i++) {
//                SysShoucang sysShoucang = sysShoucangs.get(i);
//                scftids.add(sysShoucang.getYqfatieid());
//            }
//        }
//        List<SysYqfatie> yqfatieList = new ArrayList<>();
//        if(scftids.size()>0){
//            yqfatieList = sysYqfatieService.selectList(new EntityWrapper<SysYqfatie>().in("id",scftids).orderBy(true,"updatetime",false));
//        }
//        System.out.println("yqfatieListyqfatieListyqfatieList"+yqfatieList);
//        return R.ok().put("yqfatieList",yqfatieList);
//    }
//
//    /**
//     * 发帖记录
//     */
//    @RequestMapping("/getWdGzYqfatieList")
//    public R getWdGzYqfatieList(@RequestBody JSONObject param){
//        String token = param.getString("token");
//        SysUserToken sysUserToken = sysUserTokenService.selectOne(new EntityWrapper<SysUserToken>().eq("token",token));
//        SysUser user = sysUserService.selectById(sysUserToken.getUserId());
//        List<SysGuanzhu> guanzhus = sysGuanzhuService.selectList(new EntityWrapper<SysGuanzhu>().eq("username",user.getUsername()));
//        List<String> gzzzusers = new ArrayList<>();
//        if(guanzhus.size()>0){
//            for (int i = 0; i < guanzhus.size(); i++) {
//                SysGuanzhu sysGuanzhu = guanzhus.get(i);
//                gzzzusers.add(sysGuanzhu.getGzzz());
//            }
//        }
//        List<SysYqfatie> yqfatieList = new ArrayList<>();
//        if(gzzzusers.size()>0){
//            yqfatieList = sysYqfatieService.selectList(new EntityWrapper<SysYqfatie>().in("createuser",gzzzusers).orderBy(true,"updatetime",false));
//        }
//        System.out.println("aaaaaaaayqfatieListyqfatieListyqfatieList"+yqfatieList);
//        return R.ok().put("yqfatieList",yqfatieList);
//    }
//
//
//    /**
//     * 论坛回复表
//     */
//    @RequestMapping("/getYqfthfbList")
//    public R getYqfthfbList(@RequestBody JSONObject param){
//        String yqfatieid = param.getString("yqfatieid");
//        List<SysYqfthfb> yqfthfbList = sysYqfthfbService.selectList(new EntityWrapper<SysYqfthfb>().eq("yqfatieid",yqfatieid).orderBy(true,"updatetime",false));
//        return R.ok().put("yqfthfbList",yqfthfbList);
//    }
//
//    @RequestMapping("/saveYqfthfb")
//    public R saveYqfthfb(@RequestBody JSONObject param){
//        String token = param.getString("token");
//        SysUserToken sysUserToken = sysUserTokenService.selectOne(new EntityWrapper<SysUserToken>().eq("token",token));
//        SysUser user = sysUserService.selectById(sysUserToken.getUserId());
//        String content = param.getString("hfcontent");
//        String hfpjid = param.getString("hfpjid");
//
//        SysYqfthfb sysYqfthfb = new SysYqfthfb();
//        sysYqfthfb.setYqfatieid(hfpjid);
//        sysYqfthfb.setContent(content);
//        sysYqfthfb.setUpdatetime(new Date());
//        sysYqfthfb.setUpdateuser(user.getUsername());
//        sysYqfthfb.setCreatetime(new Date());
//        sysYqfthfb.setCreateuser(user.getUsername());
//        sysYqfthfbService.insert(sysYqfthfb);
//        return R.ok();
//    }
//
//    @RequestMapping("/shanchu")
//    public R shanchu(@RequestBody JSONObject param){
//        String tzid = param.getString("tzid");
//        sysYqfatieService.deleteById(tzid);
//        return R.ok();
//    }
//
//    @RequestMapping("/scsc")
//    public R scsc(@RequestBody JSONObject param){
//        String token = param.getString("token");
//        SysUserToken sysUserToken = sysUserTokenService.selectOne(new EntityWrapper<SysUserToken>().eq("token", token));
//        SysUser user = sysUserService.selectById(sysUserToken.getUserId());
//        String yqfatieid = param.getString("yqfatieid");
//        sysShoucangService.delete(new EntityWrapper<SysShoucang>().eq("yqfatieid",yqfatieid).eq("username",user.getUsername()));
//        return R.ok();
//    }
//
//    @RequestMapping("/qxgz")
//    public R qxgz(@RequestBody JSONObject param){
//        String token = param.getString("token");
//        SysUserToken sysUserToken = sysUserTokenService.selectOne(new EntityWrapper<SysUserToken>().eq("token", token));
//        SysUser user = sysUserService.selectById(sysUserToken.getUserId());
//        String yqfatieid = param.getString("yqfatieid");
//        SysYqfatie sysYqfatie = sysYqfatieService.selectById(yqfatieid);
//        if(sysYqfatie != null){
//            SysGuanzhu sysGuanzhu = sysGuanzhuService.selectOne(new EntityWrapper<SysGuanzhu>().eq("gzzz",sysYqfatie.getCreateuser()).eq("username",user.getUsername()));
//            sysGuanzhuService.deleteById(sysGuanzhu.getId());
//        }
//
//        return R.ok();
//    }
//
//    @Autowired
//    private SysShoucangService sysShoucangService;
//
//    @RequestMapping("/sctz")
//    public R sctz(@RequestBody JSONObject param) {
//        String token = param.getString("token");
//        SysUserToken sysUserToken = sysUserTokenService.selectOne(new EntityWrapper<SysUserToken>().eq("token", token));
//        SysUser user = sysUserService.selectById(sysUserToken.getUserId());
//        String yqfatieid = param.getString("yqfatieid");
//        System.out.println("yqfatieidyqfatieidyqfatieid"+yqfatieid);
//        SysShoucang sysShoucang = sysShoucangService.selectOne(new EntityWrapper<SysShoucang>().eq("username",user.getUsername()).eq("yqfatieid",yqfatieid));
//        if(sysShoucang != null){
//            return R.error("您已收藏无需重复收藏");
//        }else{
//            sysShoucang = new SysShoucang();
//            sysShoucang.setUsername(user.getUsername());
//            sysShoucang.setYqfatieid(yqfatieid);
//            sysShoucang.setUpdatetime(new Date());
//            sysShoucang.setUpdateuser(user.getUsername());
//            sysShoucang.setCreatetime(new Date());
//            sysShoucang.setCreateuser(user.getUsername());
//            sysShoucangService.insert(sysShoucang);
//            return R.ok();
//        }
//
//    }
//
//    @Autowired
//    private SysGuanzhuService sysGuanzhuService;
//
//    @RequestMapping("/gzzz")
//    public R gzzz(@RequestBody JSONObject param){
//        String token = param.getString("token");
//        SysUserToken sysUserToken = sysUserTokenService.selectOne(new EntityWrapper<SysUserToken>().eq("token",token));
//        SysUser user = sysUserService.selectById(sysUserToken.getUserId());
//        String yqfatieid = param.getString("yqfatieid");
//        SysYqfatie sysYqfatie = sysYqfatieService.selectById(yqfatieid);
//        SysGuanzhu sysGuanzhu = sysGuanzhuService.selectOne(new EntityWrapper<SysGuanzhu>().eq("username",user.getUsername()).eq("gzzz",sysYqfatie.getCreateuser()));
//        if (sysGuanzhu == null) {
//            sysGuanzhu = new SysGuanzhu();
//            sysGuanzhu.setGzzz(sysYqfatie.getCreateuser());
//            sysGuanzhu.setUpdatetime(new Date());
//            sysGuanzhu.setUpdateuser(user.getUsername());
//            sysGuanzhu.setCreatetime(new Date());
//            sysGuanzhu.setCreateuser(user.getUsername());
//            sysGuanzhu.setUsername(user.getUsername());
//            sysGuanzhuService.insert(sysGuanzhu);
//            return R.ok();
//        }else{
//            return R.error("您已关注无需重复关注！");
//        }
//    }

    @Autowired
    private SysHdbmbService sysHdbmbService;

    @RequestMapping("/quxiao")
    public R quxiao(@RequestBody JSONObject param){
        String id = param.getString("id");
        String token = param.getString("token");
        SysUserToken sysUserToken = sysUserTokenService.selectOne(new EntityWrapper<SysUserToken>().eq("token",token));
        SysUser user = sysUserService.selectById(sysUserToken.getUserId());

        sysHdbmbService.delete(new EntityWrapper<SysHdbmb>().eq("xinwenid",id).eq("username",user.getUsername()));
        System.out.println("11111111111111111111");
        return R.ok();
    }

    @RequestMapping("/baoming")
    public R baoming(@RequestBody JSONObject param){

        String token = param.getString("token");
        SysUserToken sysUserToken = sysUserTokenService.selectOne(new EntityWrapper<SysUserToken>().eq("token",token));
        SysUser user = sysUserService.selectById(sysUserToken.getUserId());
        String id = param.getString("id");

        SysXinwen sysXinwen = sysXinwenService.selectById(id);

        SysHdbmb sysHdbmb = sysHdbmbService.selectOne(new EntityWrapper<SysHdbmb>().eq("username",user.getUsername()).eq("xinwenid",sysXinwen.getId()));
        if((user.getType()+"").equals("1")){
            return R.error("管理员不可报名！");
        }
        else if((user.getType()+"").equals("3")){
            return R.error("老师不可报名！");
        }
        else if(sysHdbmb == null){
            sysHdbmb = new SysHdbmb();
            sysHdbmb.setXinwenid(sysXinwen.getId());
            sysHdbmb.setUsername(user.getUsername());
            sysHdbmb.setBmsj(new Date());
            sysHdbmb.setUpdatetime(new Date());
            sysHdbmb.setUpdateuser(user.getUsername());
            sysHdbmb.setCreatetime(new Date());
            sysHdbmb.setCreateuser(user.getUsername());
            sysHdbmb.setTitle(sysXinwen.getTitle());
            sysHdbmb.setHdfz(sysXinwen.getHdfz());
            sysHdbmb.setXflx(sysXinwen.getXflx());
            sysHdbmbService.insert(sysHdbmb);
            return R.ok();
        }else{
            return R.error("您已报名请勿重复报名");
        }
    }


    /**
     * 验证签到
     */
    @RequestMapping("/getQdstatus")
    public R getQdstatus(@RequestBody JSONObject param) {
        String token = param.getString("token");
        SysUserToken sysUserToken = sysUserTokenService.selectOne(new EntityWrapper<SysUserToken>().eq("token",token));
        SysUser user = sysUserService.selectById(sysUserToken.getUserId());

        String qdid = param.getString("qdid");

        SysHdbmb sysHdbmb = sysHdbmbService.selectOne(new EntityWrapper<SysHdbmb>().eq("xinwenid",qdid).eq("username",user.getUsername()));
        if(sysHdbmb.getQdzt() != null && sysHdbmb.getQdzt().equals("1")){
            return R.error("已签到，请勿重复签到");
        }
        return R.ok();
    }

    /**
     * 保存签到
     */
    @RequestMapping("/saveZxqd")
    public R saveZxqd(@RequestBody JSONObject param) {
        String token = param.getString("token");
        SysUserToken sysUserToken = sysUserTokenService.selectOne(new EntityWrapper<SysUserToken>().eq("token",token));
        SysUser user = sysUserService.selectById(sysUserToken.getUserId());

        String qdid = param.getString("qdid");
        String srqdm = param.getString("srqdm");

        SysXinwen sysXinwen = sysXinwenService.selectById(qdid);
        if(sysXinwen.getQdm() == null || sysXinwen.getQdm().trim().equals("")){
            return R.error("该活动无签到码，请联系负责人维护签到码");
        }

        SysHdbmb sysHdbmb = sysHdbmbService.selectOne(new EntityWrapper<SysHdbmb>().eq("xinwenid",qdid).eq("username",user.getUsername()));
        if(sysHdbmb.getQdzt() != null && sysHdbmb.getQdzt().equals("1")){
            return R.error("已签到，请勿重复签到");
        }

        if(!srqdm.equals(sysXinwen.getQdm())){
            return R.error("签到码不一致，签到失败");
        }
        sysHdbmb.setQdzt("1");
        sysHdbmbService.updateById(sysHdbmb);
        return R.ok();
    }

    @Autowired
    private SysHdwjtjService sysHdwjtjService;

    /**
     * 个人上传文件
     */
    @RequestMapping("/openPreachData")
    public R openPreachData(@RequestBody JSONObject param) {
        String token = param.getString("token");
        SysUserToken sysUserToken = sysUserTokenService.selectOne(new EntityWrapper<SysUserToken>().eq("token",token));
        SysUser user = sysUserService.selectById(sysUserToken.getUserId());

        String xinwenid = param.getString("xinwenid");

        List<SysHdwjtj> hdwjtjs = sysHdwjtjService.selectList(new EntityWrapper<SysHdwjtj>().eq("username",user.getUsername()).eq("xinwenid",xinwenid));


        JSONArray jsonArray = new JSONArray();
        if(hdwjtjs.size()>0){
            for (int i = 0; i < hdwjtjs.size(); i++) {
                SysHdwjtj sysHdwjtj = hdwjtjs.get(i);
                List<TMaterialFile> tMaterialFiles = tMaterialFileService.selectList(new EntityWrapper<TMaterialFile>().eq("parentid",sysHdwjtj.getId()));
                System.out.println("tMaterialFilestMaterialFilestMaterialFiles"+tMaterialFiles);
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
                sysHdwjtj.setFiles(json);
                jsonArray.addAll(json);
            }
        }



        return R.ok().put("allFiles",jsonArray);

    }

    /**
     * 个人上传文件
     */
    @RequestMapping("/saveHdwjtj")
    public R saveHdwjtj(@RequestBody JSONObject param) {
        String token = param.getString("token");
        SysUserToken sysUserToken = sysUserTokenService.selectOne(new EntityWrapper<SysUserToken>().eq("token",token));
        SysUser user = sysUserService.selectById(sysUserToken.getUserId());
        String xinwenid = param.getString("xinwenid");
        JSONArray allFiles = param.getJSONArray("allFiles");

        sysHdwjtjService.delete(new EntityWrapper<SysHdwjtj>().eq("username",user.getUsername()).eq("xinwenid",xinwenid));

        if(allFiles != null){
            SysHdwjtj sysHdwjtj = new SysHdwjtj();
            sysHdwjtj.setXinwenid(xinwenid);
            sysHdwjtj.setUsername(user.getUsername());
            sysHdwjtj.setSpzt(null);
            sysHdwjtj.setSpry(null);
            sysHdwjtj.setUpdatetime(new Date());
            sysHdwjtj.setUpdateuser(user.getUsername());
            sysHdwjtj.setCreatetime(new Date());
            sysHdwjtj.setCreateuser(user.getUsername());
            sysHdwjtjService.insert(sysHdwjtj);

            tMaterialFileService.setTMaterialFilePrintId(allFiles,sysHdwjtj.getId());
        }
        return R.ok();

    }



}
