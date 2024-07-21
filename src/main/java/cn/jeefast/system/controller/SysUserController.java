package cn.jeefast.system.controller;

import cn.jeefast.system.entity.*;
import cn.jeefast.system.service.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.plugins.Page;

import cn.jeefast.common.annotation.Log;
import cn.jeefast.common.base.BaseController;
import cn.jeefast.common.utils.Query;
import cn.jeefast.common.utils.R;
import cn.jeefast.common.validator.Assert;
import cn.jeefast.common.validator.ValidatorUtils;
import cn.jeefast.common.validator.group.AddGroup;
import cn.jeefast.common.validator.group.UpdateGroup;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 * 系统用户
 *
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends BaseController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysscoreService sysscoreService;
	@Autowired
	private SysUserTokenService sysUserTokenService;

	@Autowired
	private TMaterialFileService tMaterialFileService;

	@Autowired
	private SysRoleService sysRoleService;

	@Value("${server.port}")
	private String serverport;

	@Value("${server.context-path}")
	private String servercontextpath;


	
	/**
	 * 所有用户列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:user:list")
	public R list(@RequestParam Map<String, Object> params) throws UnknownHostException {
		//查询列表数据
		SysUser dluser = getUser();
		//管理远看全部
		List<SysUserRole> sysUserRolesone = sysUserRoleService.selectList(new EntityWrapper<SysUserRole>().eq("user_id",dluser.getUserId()));
		String usernameParam = dluser.getUsername();
		if(sysUserRolesone.size()>0){
			for(int i=0;i<sysUserRolesone.size();i++){
				SysUserRole sysUserRole = sysUserRolesone.get(i);
				if((sysUserRole.getRoleId()+"").equals("1")){
					usernameParam = null;
				}
			}
		}
		params.put("usernameParam",usernameParam);
		params.put("type","3");
		Query query = new Query(params);
		Page<SysUser> pageUtil = new Page<SysUser>(query.getPage(), query.getLimit());
		Page<SysUser> page = sysUserService.queryPageList(pageUtil,query);
		if(page.getRecords().size()>0){
			for(int i=0;i<page.getRecords().size();i++){
				SysUser sysUser = page.getRecords().get(i);
				List<SysUserRole> sysUserRoles = sysUserRoleService.selectList(new EntityWrapper<SysUserRole>().eq("user_id",sysUser.getUserId()));
				String roalArraystr = "";
				if(sysUserRoles != null && sysUserRoles.size()>0){
					for(int j=0;j<sysUserRoles.size();j++){
						SysRole sysRole = sysRoleService.selectById(sysUserRoles.get(j).getRoleId());
						if(sysRole != null){
							//roalArraystr = sysRole.getRoleName()+","+roalArraystr;
							roalArraystr = sysRole.getRoleName()+roalArraystr;
						}
					}
				}
				sysUser.setRoalArraystr(roalArraystr);//显示角色信息

				/**
				 * 设置头像信息
				 */
//				List<TMaterialFile> tMaterialFileList = tMaterialFileService.selectList(new EntityWrapper<TMaterialFile>().eq("parentid",sysUser.getUserId()));
//				SysUserToken sysUserToken = sysUserTokenService.selectOne(new EntityWrapper<SysUserToken>().eq("user_id", getUserId()));
//				InetAddress address = InetAddress.getLocalHost();
//				sysUser.setPhotopath(tMaterialFileList != null && tMaterialFileList.size()>0?"http://"+address.getHostAddress() +":"+serverport+"/"+servercontextpath+"/upload/"+tMaterialFileList.get(0).getSfilename()+"?token="+sysUserToken.getToken():"img/usermm.jpg");
				sysUser.setPhotopath("img/usermm.jpg");
			}
		}
		return R.ok().put("page", page);
	}

	/**
	 * 所有用户列表
	 */
	@RequestMapping("/guanliyuan")
	@RequiresPermissions("sys:user:guanliyuan")
	public R guanliyuan(@RequestParam Map<String, Object> params) throws UnknownHostException {
		//查询列表数据
		SysUser dluser = getUser();
		//管理远看全部
		List<SysUserRole> sysUserRolesone = sysUserRoleService.selectList(new EntityWrapper<SysUserRole>().eq("user_id",dluser.getUserId()));
		String usernameParam = dluser.getUsername();
		if(sysUserRolesone.size()>0){
			for(int i=0;i<sysUserRolesone.size();i++){
				SysUserRole sysUserRole = sysUserRolesone.get(i);
				if((sysUserRole.getRoleId()+"").equals("1")){
					usernameParam = null;
				}
			}
		}
		params.put("usernameParam",usernameParam);
		params.put("type","1");
		Query query = new Query(params);
		Page<SysUser> pageUtil = new Page<SysUser>(query.getPage(), query.getLimit());
		Page<SysUser> page = sysUserService.queryPageList(pageUtil,query);
		if(page.getRecords().size()>0){
			for(int i=0;i<page.getRecords().size();i++){
				SysUser sysUser = page.getRecords().get(i);
				List<SysUserRole> sysUserRoles = sysUserRoleService.selectList(new EntityWrapper<SysUserRole>().eq("user_id",sysUser.getUserId()));
				String roalArraystr = "";
				if(sysUserRoles != null && sysUserRoles.size()>0){
					for(int j=0;j<sysUserRoles.size();j++){
						SysRole sysRole = sysRoleService.selectById(sysUserRoles.get(j).getRoleId());
						if(sysRole != null){
							roalArraystr = sysRole.getRoleName()+roalArraystr;
						}
					}
				}
				sysUser.setRoalArraystr(roalArraystr);//显示角色信息

				/**
				 * 设置头像信息
				 */
//				List<TMaterialFile> tMaterialFileList = tMaterialFileService.selectList(new EntityWrapper<TMaterialFile>().eq("parentid",sysUser.getUserId()));
//				SysUserToken sysUserToken = sysUserTokenService.selectOne(new EntityWrapper<SysUserToken>().eq("user_id", getUserId()));
//				InetAddress address = InetAddress.getLocalHost();
				//sysUser.setPhotopath(tMaterialFileList != null && tMaterialFileList.size()>0?"http://"+address.getHostAddress() +":"+serverport+"/"+servercontextpath+"/upload/"+tMaterialFileList.get(0).getSfilename()+"?token="+sysUserToken.getToken():"img/usermm.jpg");
				sysUser.setPhotopath("img/usermm.jpg");
			}
		}
		return R.ok().put("page", page);
	}

	/**
	 * 所有用户列表
	 */
	@RequestMapping("/xuesheng")
	@RequiresPermissions("sys:user:xuesheng")
	public R xuesheng(@RequestParam Map<String, Object> params) throws UnknownHostException {
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
		params.put("type","2");
		Query query = new Query(params);
		Page<SysUser> pageUtil = new Page<SysUser>(query.getPage(), query.getLimit());
		Page<SysUser> page = sysUserService.queryPageList(pageUtil,query);
		if(page.getRecords().size()>0){
			for(int i=0;i<page.getRecords().size();i++){
				SysUser sysUser = page.getRecords().get(i);
				List<SysUserRole> sysUserRoles = sysUserRoleService.selectList(new EntityWrapper<SysUserRole>().eq("user_id",sysUser.getUserId()));
				String roalArraystr = "";
				if(sysUserRoles != null && sysUserRoles.size()>0){
					for(int j=0;j<sysUserRoles.size();j++){
						SysRole sysRole = sysRoleService.selectById(sysUserRoles.get(j).getRoleId());
						if(sysRole != null){
							roalArraystr = sysRole.getRoleName()+roalArraystr;
						}
					}
				}
				sysUser.setRoalArraystr(roalArraystr);//显示角色信息

				/**
				 * 设置头像信息
				 */
				List<TMaterialFile> tMaterialFileList = tMaterialFileService.selectList(new EntityWrapper<TMaterialFile>().eq("parentid",sysUser.getUserId()));
				SysUserToken sysUserToken = sysUserTokenService.selectOne(new EntityWrapper<SysUserToken>().eq("user_id", getUserId()));
				InetAddress address = InetAddress.getLocalHost();
				//sysUser.setPhotopath(tMaterialFileList != null && tMaterialFileList.size()>0?"http://"+address.getHostAddress() +":"+serverport+"/"+servercontextpath+"/upload/"+tMaterialFileList.get(0).getSfilename()+"?token="+sysUserToken.getToken():"img/usermm.jpg");
				sysUser.setPhotopath("img/usermm.jpg");

			}
		}
		return R.ok().put("page", page);
	}
	
	/**
	 * 获取登录的用户信息
	 */
	@RequestMapping("/info")
	public R info() throws UnknownHostException {
		SysUser user = getUser();
		SysUserToken sysUserToken = sysUserTokenService.selectOne(new EntityWrapper<SysUserToken>().eq("user_id", user.getUserId()));
		List<TMaterialFile> tMaterialFileList = tMaterialFileService.selectList(new EntityWrapper<TMaterialFile>().eq("parentid",user.getUserId()));
		InetAddress address = InetAddress.getLocalHost();
		return R.ok().put("user", getUser()).put("lj",tMaterialFileList != null && tMaterialFileList.size()>0?"http://"+address.getHostAddress() +":"+serverport+"/"+servercontextpath+"/upload/"+tMaterialFileList.get(0).getSfilename()+"?token="+sysUserToken.getToken():"img/usermm.jpg");
	}
	
//	/**
//	 * 修改登录用户密码
//	 */
//	@Log("修改密码")
//	@RequestMapping("/password")
//	public R password(String password, String newPassword){
//		Assert.isBlank(newPassword, "新密码不为能空");
//
//		//sha256加密
//		password = new Sha256Hash(password, getUser().getSalt()).toHex();
//		//sha256加密
//		newPassword = new Sha256Hash(newPassword, getUser().getSalt()).toHex();
//
//		SysUser user = new SysUser();
//		user.setUserId(getUserId());
//		user.setPassword(newPassword);
//		//更新密码
//		boolean bFlag = sysUserService.updateById(user);
//		if(!bFlag){
//			return R.error("原密码不正确");
//		}
//
//		return R.ok();
//	}

	/**
	 * 修改登录用户密码
	 */
	@Log("修改密码")
	@RequestMapping("/password")
	public R password(String password, String newPassword) {
		Assert.isBlank(newPassword, "新密码不能为空");

		// 获取当前用户信息，这里假设 getUser() 是获取当前登录用户信息的方法
		SysUser currentUser = getUser();
		if (currentUser == null) {
			return R.error("用户未登录");
		}

		// 原始密码加密验证
		String encryptedPassword = new Sha256Hash(password, currentUser.getSalt()).toHex();
		if (!currentUser.getPassword().equals(encryptedPassword)) {
			return R.error("原密码不正确");
		}

		// 新密码加密
		String encryptedNewPassword = new Sha256Hash(newPassword, currentUser.getSalt()).toHex();

		// 更新用户密码
		currentUser.setPassword(encryptedNewPassword);
		boolean updateResult = sysUserService.updateById(currentUser);
		if (!updateResult) {
			return R.error("密码更新失败，请稍后再试");
		}

		return R.ok("密码修改成功");
	}


//	/**
//	 * 个人更改密码
//	 */
//	@RequestMapping("/password")
//	public R updatepassword(@RequestBody JSONObject tjarray){
//		String token = tjarray.getString("token");
//		SysUserToken sysUserToken = sysUserTokenService.selectOne(new EntityWrapper<SysUserToken>().eq("token",token));
//		SysUser user = sysUserService.selectById(sysUserToken.getUserId());
//
//
//		String oldpassword = tjarray.getString("oldpassword");
//		String newPassword =  tjarray.getString("password");
//
//
//		Assert.isBlank(newPassword, "新密码不为能空");
//
//		//sha256加密
//		oldpassword = new Sha256Hash(oldpassword, user.getSalt()).toHex();
//		//sha256加密
//		newPassword = new Sha256Hash(newPassword, user.getSalt()).toHex();
//		if(!oldpassword.equals(user.getPassword())){
//			return R.error("原密码不正确无法修改密码");
//		}
//
////        SysUser user = new SysUser();
//		user.setUserId(user.getUserId());
//		user.setPassword(newPassword);
//		//更新密码
//		sysUserService.updateById(user);
//		return R.ok();
//
//	}
	/**
	 * 用户信息
	 */
	@RequestMapping("/getUsers")
	public R getUsers(@RequestBody JSONObject param){
		String type  = param.getString("type");
		Wrapper wrapper = new EntityWrapper();
		if(type != null && !type.trim().equals("")){
			wrapper.eq("type",type);
		}
		List<SysUser> users = sysUserService.selectList(wrapper.orderBy(true,"create_time",false));
		return R.ok().put("users",users);
	}
	
	/**
	 * 用户信息
	 */
	@RequestMapping("/info/{userId}")
	@RequiresPermissions("sys:user:info")
	public R info(@PathVariable("userId") Long userId){
		SysUser user = sysUserService.selectById(userId);
		
		//获取用户所属的角色列表
		List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
		user.setRoleIdList(roleIdList);

		//获取附件列表
		List<TMaterialFile> tMaterialFiles = tMaterialFileService.selectList(new EntityWrapper<TMaterialFile>().eq("parentid",userId));
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
		return R.ok().put("user", user);
	}
	
	/**
	 * 保存用户
	 */
	@Log("保存用户")
	@RequestMapping("/save")
	@RequiresPermissions("sys:user:save")
	public R save(@RequestBody SysUser user){
		ValidatorUtils.validateEntity(user, AddGroup.class);
		Sysscore sysscore = new Sysscore();
		sysscore.setID(user.getUsername());
		sysscore.setUsername(user.getRealname());
		sysscore.setDeyu(0.0);
		sysscore.setZhiyu(0.0);
		sysscore.setTiyu(0.0);
		sysscore.setLaoyu(0.0);
		sysscore.setMeiyu(0.0);
		user.setCreateTime(new Date());
		user.setCreateUserId(getUserId());
		user.setType(user.getRoleIdList().get(0)+"");
		sysUserService.save(user);
		sysscoreService.insert(sysscore);
		tMaterialFileService.setTMaterialFilePrintId(user.getFiles(),user.getUserId()+"");
		return R.ok();
	}


	
	/**
	 * 修改用户
	 */
	@Log("修改用户")
	@RequestMapping("/update")
	@RequiresPermissions("sys:user:update")
	public R update(@RequestBody SysUser user){
		ValidatorUtils.validateEntity(user, UpdateGroup.class);
		
		user.setCreateUserId(getUserId());
		user.setType(user.getRoleIdList().get(0)+"");
		sysUserService.update(user);
		tMaterialFileService.setTMaterialFilePrintId(user.getFiles(),user.getUserId()+"");
		return R.ok();
	}
	
	/**
	 * 删除用户
	 */
	@Log("删除用户")
	@RequestMapping("/delete")
	@RequiresPermissions("sys:user:delete")
	public R delete(@RequestBody Long[] userIds){
		if(ArrayUtils.contains(userIds, 1L)){
			return R.error("系统管理员不能删除");
		}
		
		if(ArrayUtils.contains(userIds, getUserId())){
			return R.error("当前用户不能删除");
		}
		sysUserService.deleteBatch(userIds);
		
		return R.ok();
	}
	
	/**
	 * 用户
	 */
	@RequestMapping("/getUserByType")
	public R getUserByType(@RequestBody JSONObject param){
		String type = param.getString("type");
		Wrapper wrapper =  new EntityWrapper<>();
		SysUser sysUser = sysUserService.selectById(getUserId());
		//管理远看全部
		List<SysUserRole> sysUserRolesone = sysUserRoleService.selectList(new EntityWrapper<SysUserRole>().eq("user_id",sysUser.getUserId()));
		String usernameParam = sysUser.getUsername();
		if(sysUserRolesone.size()>0){
			for(int i=0;i<sysUserRolesone.size();i++){
				SysUserRole sysUserRole = sysUserRolesone.get(i);
				if((sysUserRole.getRoleId()+"").equals("1")){
					usernameParam = null;
				}
			}
		}
		if(usernameParam != null){
			wrapper.eq("username",usernameParam);
		}
		List<SysUser> users = sysUserService.selectList(wrapper.eq("type",type).orderBy(true,"create_time",false));
		return R.ok().put("users",users);
	}
}
