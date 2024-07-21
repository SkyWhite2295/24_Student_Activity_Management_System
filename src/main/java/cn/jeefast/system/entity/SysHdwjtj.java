package cn.jeefast.system.entity;

import java.io.Serializable;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotations.TableField;

import java.util.Date;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * <p>
 * 活动附件表
 * </p>
 *
 */
@TableName("sys_hdwjtj")
public class SysHdwjtj extends Model<SysHdwjtj> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	@TableId(type = IdType.UUID)
	private String id;
    /**
     * 所属活动
     */
	private String xinwenid;

	@TableField(exist = false)
	private String xinwenname;
    /**
     * 附件上传人
     */
	private String username;
    /**
     * 审批状态,1:审批,2:驳回
     */
	private String spzt;
    /**
     * 审批人员
     */
	private String spry;
    /**
     * 更新时间
     */
	private Date updatetime;
    /**
     * 更新人
     */
	private String updateuser;
    /**
     * 创建时间
     */
	private Date createtime;
    /**
     * 创建人员
     */
	private String createuser;


	public String getXinwenname() {
		return xinwenname;
	}

	public void setXinwenname(String xinwenname) {
		this.xinwenname = xinwenname;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getXinwenid() {
		return xinwenid;
	}

	public void setXinwenid(String xinwenid) {
		this.xinwenid = xinwenid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSpzt() {
		return spzt;
	}

	public void setSpzt(String spzt) {
		this.spzt = spzt;
	}

	public String getSpry() {
		return spry;
	}

	public void setSpry(String spry) {
		this.spry = spry;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public String getUpdateuser() {
		return updateuser;
	}

	public void setUpdateuser(String updateuser) {
		this.updateuser = updateuser;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getCreateuser() {
		return createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	/**
	* 附件信息
	*/
	@TableField(exist = false)
	private JSONArray files;

	public JSONArray getFiles() {
		return files;
	}

	public void setFiles(JSONArray files) {
		this.files = files;
	}



	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "SysHdwjtj{" +
				"id='" + id + '\'' +
				", xinwenid='" + xinwenid + '\'' +
				", xinwenname='" + xinwenname + '\'' +
				", username='" + username + '\'' +
				", spzt='" + spzt + '\'' +
				", spry='" + spry + '\'' +
				", updatetime=" + updatetime +
				", updateuser='" + updateuser + '\'' +
				", createtime=" + createtime +
				", createuser='" + createuser + '\'' +
				", files=" + files +
				'}';
	}
}
