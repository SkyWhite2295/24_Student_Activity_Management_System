package cn.jeefast.system.entity;

import java.io.Serializable;

import java.util.Date;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * <p>
 * 活动信息
 * </p>
 *
 * @author theodo
 * @since 2022-03-30
 */
@TableName("sys_xinwen")
public class SysXinwen extends Model<SysXinwen> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(type = IdType.UUID)
	private String id;
	/**
	 * 活动信息标题
	 */
	private String title;
	/**
	 * 活动信息内容
	 */
	private String content;
	/**
	 * 作者
	 */
	private String zuozhe;
	/**
	 * 创建人员
	 */
	private String createuser;
	/**
	 * 更新人员
	 */
	private String updateuser;
	/**
	 * 更新时间
	 */
	private Date updatetime;
	/**
	 * 创建时间
	 */
	private Date createtime;

	private String type;//分类1：活动信息2：通知公告

	private String zbx;
	private String zby;

	@TableField(exist = false)
	private String qzbx;

	@TableField(exist = false)
	private String qzby;

	@TableField(exist = false)
	private JSONArray files;

	private Date hdsj;
	private String hddd;
	private String spzt;
	private String hdlb;

	@TableField(exist = false)
	private String hdlbname;


	private Integer hdrs;
	private Float hdfz;
	private String qdm;

	private String jbzlx;

	private String xflx;

	@TableField(exist = false)
	private String xflxname;

	@TableField(exist = false)
	private Integer ybmrs;


	public String getXflxname() {
		return xflxname;
	}

	public void setXflxname(String xflxname) {
		this.xflxname = xflxname;
	}

	public String getXflx() {
		return xflx;
	}

	public void setXflx(String xflx) {
		this.xflx = xflx;
	}

	public Integer getYbmrs() {
		return ybmrs;
	}

	public void setYbmrs(Integer ybmrs) {
		this.ybmrs = ybmrs;
	}

	public String getHdlbname() {
		return hdlbname;
	}

	public void setHdlbname(String hdlbname) {
		this.hdlbname = hdlbname;
	}

	public Integer getHdrs() {
		return hdrs;
	}

	public void setHdrs(Integer hdrs) {
		this.hdrs = hdrs;
	}

	public Float getHdfz() {
		return hdfz;
	}

	public void setHdfz(Float hdfz) {
		this.hdfz = hdfz;
	}

	public String getQdm() {
		return qdm;
	}

	public void setQdm(String qdm) {
		this.qdm = qdm;
	}

	public String getJbzlx() {
		return jbzlx;
	}

	public void setJbzlx(String jbzlx) {
		this.jbzlx = jbzlx;
	}

	public String getHdlb() {
		return hdlb;
	}

	public void setHdlb(String hdlb) {
		this.hdlb = hdlb;
	}

	public String getSpzt() {
		return spzt;
	}

	public void setSpzt(String spzt) {
		this.spzt = spzt;
	}

	public Date getHdsj() {
		return hdsj;
	}

	public void setHdsj(Date hdsj) {
		this.hdsj = hdsj;
	}

	public String getHddd() {
		return hddd;
	}

	public void setHddd(String hddd) {
		this.hddd = hddd;
	}

	public JSONArray getFiles() {
		return files;
	}

	public void setFiles(JSONArray files) {
		this.files = files;
	}

	public String getQzbx() {
		return qzbx;
	}

	public void setQzbx(String qzbx) {
		this.qzbx = qzbx;
	}

	public String getQzby() {
		return qzby;
	}

	public void setQzby(String qzby) {
		qzby = qzby;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getZbx() {
		return zbx;
	}

	public void setZbx(String zbx) {
		this.zbx = zbx;
	}

	public String getZby() {
		return zby;
	}

	public void setZby(String zby) {
		this.zby = zby;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getZuozhe() {
		return zuozhe;
	}

	public void setZuozhe(String zuozhe) {
		this.zuozhe = zuozhe;
	}

	public String getCreateuser() {
		return createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	public String getUpdateuser() {
		return updateuser;
	}

	public void setUpdateuser(String updateuser) {
		this.updateuser = updateuser;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "SysXinwen{" +
				"id='" + id + '\'' +
				", title='" + title + '\'' +
				", content='" + content + '\'' +
				", zuozhe='" + zuozhe + '\'' +
				", createuser='" + createuser + '\'' +
				", updateuser='" + updateuser + '\'' +
				", updatetime=" + updatetime +
				", createtime=" + createtime +
				", type='" + type + '\'' +
				", zbx='" + zbx + '\'' +
				", zby='" + zby + '\'' +
				", qzbx='" + qzbx + '\'' +
				", qzby='" + qzby + '\'' +
				", files=" + files +
				", hdsj=" + hdsj +
				", hddd='" + hddd + '\'' +
				", spzt='" + spzt + '\'' +
				", hdlb='" + hdlb + '\'' +
				", hdlbname='" + hdlbname + '\'' +
				", hdrs=" + hdrs +
				", hdfz=" + hdfz +
				", qdm='" + qdm + '\'' +
				", jbzlx='" + jbzlx + '\'' +
				", xflx='" + xflx + '\'' +
				", xflxname='" + xflxname + '\'' +
				", ybmrs=" + ybmrs +
				'}';
	}
}
