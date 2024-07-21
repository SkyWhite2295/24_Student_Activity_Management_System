package cn.jeefast.system.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 */
@TableName("sys_pjinfo")
public class SysPjinfo extends Model<SysPjinfo> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 评价标题
     */
	private String title;
    /**
     * 评价内容
     */
	private String content;
    /**
     * 评价人
     */
	private String author;

	private String pjtype;
	private String deptid;
	private String username;
	private String prentid;

	@TableField(exist=false)
	private List<SysPjinfo> sysPjinfoList;

	public List<SysPjinfo> getSysPjinfoList() {
		return sysPjinfoList;
	}

	public void setSysPjinfoList(List<SysPjinfo> sysPjinfoList) {
		sysPjinfoList = sysPjinfoList;
	}

	public String getPrentid() {
		return prentid;
	}

	public void setPrentid(String prentid) {
		this.prentid = prentid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@TableField(exist = false)
	private String deptName;

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getPjtype() {
		return pjtype;
	}

	public void setPjtype(String pjtype) {
		this.pjtype = pjtype;
	}

	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "SysPjinfo{" +
				"id=" + id +
				", title='" + title + '\'' +
				", content='" + content + '\'' +
				", author='" + author + '\'' +
				", pjtype='" + pjtype + '\'' +
				", deptid='" + deptid + '\'' +
				", username='" + username + '\'' +
				", prentid='" + prentid + '\'' +
				", SysPjinfoList=" + sysPjinfoList +
				", deptName='" + deptName + '\'' +
				'}';
	}
}
