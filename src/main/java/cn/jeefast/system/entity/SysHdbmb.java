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
 * 活动报名表
 * </p>
 *
 */
@TableName("sys_hdbmb")
public class SysHdbmb extends Model<SysHdbmb> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(type = IdType.UUID)
	private String id;
	/**
	 * 活动id
	 */
	private String xinwenid;
	/**
	 * 报名人员
	 */
	private String username;
	/**
	 * 报名时间
	 */
	private Date bmsj;
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

	private String qdzt;

	private Double df;

	private String title;

	private String tg;

	private Float hdfz;

	private String xflx;

	public String getXflx() {
		return xflx;
	}

	public void setXflx(String xflx) {
		this.xflx = xflx;
	}

	public Float getHdfz() {
		return hdfz;
	}

	public void setHdfz(Float hdfz) {
		this.hdfz = hdfz;
	}

	public String getTg() {
		return tg;
	}

	public void setTg(String tg) {
		this.tg = tg;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Double getDf() {
		return df;
	}

	public void setDf(Double df) {
		this.df = df;
	}

	public String getQdzt() {
		return qdzt;
	}

	public void setQdzt(String qdzt) {
		this.qdzt = qdzt;
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

	public Date getBmsj() {
		return bmsj;
	}

	public void setBmsj(Date bmsj) {
		this.bmsj = bmsj;
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
		return "SysHdbmb{" +
				"id='" + id + '\'' +
				", xinwenid='" + xinwenid + '\'' +
				", username='" + username + '\'' +
				", bmsj=" + bmsj +
				", updatetime=" + updatetime +
				", updateuser='" + updateuser + '\'' +
				", createtime=" + createtime +
				", createuser='" + createuser + '\'' +
				", qdzt='" + qdzt + '\'' +
				", title='" + title + '\'' +
				", tg='" + tg + '\'' +
				", xflx='" + xflx + '\'' +
				", df=" + df +
				", hdfz=" + hdfz +
				'}';
	}
}
