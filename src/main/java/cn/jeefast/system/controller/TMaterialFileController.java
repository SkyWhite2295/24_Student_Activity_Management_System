package cn.jeefast.system.controller;

import cn.jeefast.common.annotation.Log;
import cn.jeefast.common.base.BaseController;
import cn.jeefast.common.utils.Query;
import cn.jeefast.common.utils.R;
import cn.jeefast.common.validator.ValidatorUtils;
import cn.jeefast.common.validator.group.AddGroup;
import cn.jeefast.common.validator.group.UpdateGroup;
import cn.jeefast.system.entity.TMaterialFile;
import cn.jeefast.system.service.TMaterialFileService;
import cn.jeefast.system.util.FileUpLoadUtil;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/tMaterialFile")
public class TMaterialFileController extends BaseController {
	
	@Autowired
	private TMaterialFileService tMaterialFileService;



	@Log("所有附件列表")
	@RequestMapping("/list")
	//@RequiresPermissions("basic")
	public R list(@RequestParam Map<String, Object> params) {
		Query query = new Query(params);
		Page<TMaterialFile> pageUtil = new Page<TMaterialFile>(query.getPage(), query.getLimit());
		Page<TMaterialFile> page = tMaterialFileService.queryPageList(pageUtil, query);
		return R.ok().put("page", page);
	}
	
	@Log("展示附件show")
	@RequestMapping("/show")
	//@RequiresPermissions("basic")
	public R show(@RequestParam Map<String, Object> params) {
		Query query = new Query(params);
		Page<TMaterialFile> pageUtil = new Page<TMaterialFile>(query.getPage(), query.getLimit());
		Page<TMaterialFile> page = tMaterialFileService.queryPageList(pageUtil, query);
		return R.ok().put("page", page);
	}
	
	@Log("获取附件信息")
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("basic")
	public R info(@PathVariable("id") String id) {
		TMaterialFile tMaterialFile = tMaterialFileService.selectById(id);
		return R.ok().put("tMaterialFile", tMaterialFile);
	}
	
	@Log("保存文件上传")
	@RequestMapping("/save")
	//@RequiresPermissions("basic")
	public R save(@RequestBody TMaterialFile tMaterialFile) {
		ValidatorUtils.validateEntity(tMaterialFile, AddGroup.class);
	
		tMaterialFileService.insert(tMaterialFile);
		
		return R.ok();
	}
	
	@Log("修改文件上传")
	@RequestMapping("/update")
	//@RequiresPermissions("platform:programme:update")
	public R update(@RequestBody TMaterialFile tMaterialFile) {
		ValidatorUtils.validateEntity(tMaterialFile, UpdateGroup.class);
		tMaterialFileService.updateById(tMaterialFile);
		
		return R.ok();
	}
	
	@Log("删除文件上传")
	@RequestMapping("/delete")
	//@RequiresPermissions("basic")
	public R delete(@RequestBody String[] ids) {
		tMaterialFileService.deleteBatch(ids);
		return R.ok();
	}
	
	/**
	 * 导入文件
	 */
	@Log("上传文件")
	@RequestMapping("uploadFile")
	//@RequiresPermissions("basic")
	public R uploadFile(MultipartFile file) {
		String path = tMaterialFileService.uploadFile(file);
		return R.ok().put("fileName", file.getOriginalFilename()).put("path", path);
	}
	
	/**
	 * 导入文件
	 * @throws Exception 
	 */
	@Log("上传文件")
	@RequestMapping("importPsot")
	//@RequiresPermissions("sys:tMaterialFile:importPsot")
	public R importPsot(HttpServletRequest request) throws Exception {
		System.out.println("requestrequestrequestrequest"+request);
//		Boolean result=tMaterialFileService.verification(request.getParameter("fileList111"), getUser());
//		System.out.println("resultresultresult"+result);
//		if (result==false) {
//			return R.error("false");
//		}
//		String dataPath = sysSwitchService.selectOne(new EntityWrapper<SysSwitch>()).getFileuplpadurl();

		String dataPath = "D:\\upload";
		Map<String, String> map = tMaterialFileService.importPsot(request);
//		if (map.get("mibiaotest")!=null) {
//			return R.error("false1");
//		}
		return R.ok().put("fileName", map.get("fileName")).put("path", map.get("path")).put("id", map.get("id")).put("dataPath", dataPath).put("mbfklj", map.get("mbfklj"));
	}
	
	/**
	 * 导入文件
	 * @throws Exception 
	 */
	@Log("上传文件")
	@RequestMapping("importPsotfiles")
	//@RequiresPermissions("basic")
	public R importPsotfiles(HttpServletRequest request) throws Exception {

//		String dataPath = sysSwitchService.selectOne(new EntityWrapper<SysSwitch>()).getFileuplpadurl();
//		Map<String, String> map = tMaterialFileService.importPsotfiles(request);
//		return R.ok().put("fileName", map.get("fileName")).put("path", map.get("path")).put("id", map.get("id")).put("dataPath", dataPath);
		return null;
	}
	@Log("文件是否存在")
	@RequestMapping("ishSingleFile/{preachDataId}")
	////@RequiresPermissions("basic")
	public R ishSingleFile(@PathVariable("preachDataId") String preachDataId) {
//		String dataPath = sysSwitchService.selectOne(new EntityWrapper<SysSwitch>()).getFileuplpadurl();
		String dataPath = "D:\\upload";
		TMaterialFile tMaterialFile = tMaterialFileService.selectById(preachDataId);
		System.out.println("dataPathdataPathdataPath"+dataPath +"/"+ tMaterialFile.getSfilename());
		String fileName="";
		if(tMaterialFile!=null) {
			File file = new File(dataPath +"/"+ tMaterialFile.getSfilename());
			if (!file.exists()) {
				fileName = "文件不存在";
				return R.ok().put("fileName", fileName);
			}

		}else {
			fileName = "无下载文件";
		}
		System.out.println("fileNamefileNamefileNamefileNamefileName"+fileName);
		return R.ok().put("fileName", fileName);
//		return null;
	}

	@Log("下载文件")
	@RequestMapping("/downFile")
	public void downFile(HttpServletRequest request, HttpServletResponse response, String id) throws Exception {
		TMaterialFile tMaterialFile = tMaterialFileService.selectById(id);
		try {
			FileUpLoadUtil.downloadAttachments("","D:\\upload", tMaterialFile.getSfilename(), tMaterialFile.getSfilelength(),tMaterialFile, response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	} 
	
	//删除文件
	@Log("删除文件")
	@RequestMapping("deleteFile")
	//@RequiresPermissions("basic")
	public R deleteFile(@RequestBody String filePath) {
		 //          System.out.println("filePathfilePath"+filePath);
		tMaterialFileService.deleteFile(filePath);
		return R.ok();
	}
	
	//删除文件
	@Log("删除文件")
	@RequestMapping("/deleteFileByid/{id}")
	//@RequiresPermissions("basic")
	public R deleteFileByid(@PathVariable("id") String id) {
		tMaterialFileService.deleteFileByid(id);
		return R.ok();
	}

	@Log("删除文件上传")
	@RequestMapping("/deleteByFileId")
	public R deleteByFileId(@RequestBody Map<String,Object> deleteFles) {
		System.out.println("aaaaaaaaididididididid"+deleteFles);
		String id = deleteFles.get("id")+"";
		tMaterialFileService.deletePreachFile(id);
		tMaterialFileService.deleteFileByid(id);
//		tMaterialFileService.deleteBatch(id);
		return R.ok();
	}
	
 
	
	@Log("下载文件")
	@RequestMapping("/ishFile/{id}")
	//@RequiresPermissions("basic")
	public R ishFile(@PathVariable("id") String id) {
//		String dataPath = sysSwitchService.selectOne(new EntityWrapper<SysSwitch>()).getFileuplpadurl();
		String dataPath = "D://uoload";
		TMaterialFile tMaterialFile = tMaterialFileService.selectById(id);
		String fileName="";
		if(tMaterialFile!=null&& tMaterialFile.getSfilename()!=null) {
			File file = new File(dataPath +"/"+ tMaterialFile.getSfilename());
			if (!file.exists()) {
				fileName = "文件不存在";
				return R.ok().put("fileName", fileName);
			}
		}else {
			fileName = "无下载文件";
		}
		return R.ok().put("fileName", fileName);
	}
}
