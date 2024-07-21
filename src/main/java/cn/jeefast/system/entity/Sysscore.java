package cn.jeefast.system.entity;
import java.io.Serializable;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;

import java.util.Date;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
@TableName("sys_score")
public class Sysscore extends Model<Sysscore> {

    private static final long serialVersionUID = 1L;

    //主键id
    @TableId(value= "id",type = IdType.INPUT)
    private String id;

    //所属学生
    private String username;

    //美育得分
    private Double meiyu;

    //体育得分
    private Double tiyu;

    //德育得分
    private Double deyu;

    //智育得分
    private Double zhiyu;

    //劳育得分
    private Double laoyu;

    public String getId() {
        return id;
    }
    public void setID(String id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Double getMeiyu() {
        return meiyu;
    }
    public void setMeiyu(Double meiyu) {
        this.meiyu = meiyu;
    }
    public Double getTiyu() {
        return tiyu;
    }
    public void setTiyu(Double tiyu) {
        this.tiyu = tiyu;
    }
    public Double getDeyu() {
        return deyu;
    }
    public void setDeyu(Double deyu) {
        this.deyu = deyu;
    }
    public Double getZhiyu() {
        return zhiyu;
    }
    public void setZhiyu(Double zhiyu) {
        this.zhiyu = zhiyu;
    }
    public Double getLaoyu() {
        return laoyu;
    }
    public void setLaoyu(Double laoyu) {
        this.laoyu = laoyu;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Sysscore{" +
                ", id=" + id +
                ", username=" + username +
                ", meiyu=" + meiyu +
                ", tiyu=" + tiyu +
                ", deyu=" + deyu +
                ", laoyu=" + laoyu +
                ", zhiyu=" + zhiyu +
                "}";
    }
}