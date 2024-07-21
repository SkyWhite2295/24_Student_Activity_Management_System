package cn.jeefast.system.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

@TableName("sys_UserScore")
public class SysUserScore extends Model<SysUserScore> {

    private static final long serialVersionUID = 1L;

    //主键id
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

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

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Double getmeiyu() {
        return meiyu;
    }

    public Double gettiyu() {
        return tiyu;
    }

    public Double getdeyu() {
        return deyu;
    }

    public Double getzhiyu() {
        return zhiyu;
    }

    public Double getlaoyu() {
        return laoyu;
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

