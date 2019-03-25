package com.sl.ue.entity.sys;

import com.sl.ue.util.anno.DbField;
import com.sl.ue.util.anno.Id;
import com.sl.ue.util.anno.Table;

@Table("SYS_QQ_FL")
public class SysQqFl implements java.io.Serializable{

    /** 序列化 */
    private static final long serialVersionUID = 1L;

    /**  */
    @DbField("FL_FLAG")
    private String flFlag;

    /**  */
    @DbField("FL_MC")
    private String flMc;

    /**  */
    @DbField("FL_UNIT")
    private Integer flUnit;

    /**  */
    @DbField("FL_COUNT_IN")
    private Integer flCountIn;

    /**  */
    @DbField("FL_COUNT_OUT")
    private Integer flCountOut;

    /**  */
    @DbField("FL_BEGIN_TIME")
    private Integer flBeginTime;

    /**  */
    @DbField("FL_BEGIN_UNIT")
    private Integer flBeginUnit;

    /**  */
    @DbField("FL_BEGIN_COUNT_IN")
    private Integer flBeginCountIn;

    /**  */
    @DbField("FL_BEGIN_COUNT_OUT")
    private Integer flBeginCountOut;

    /**  */
    @Id
    @DbField("WebID")
    private Integer webid;

    public String getFlFlag() {
        return this.flFlag;
    }

    public void setFlFlag(String flFlag) {
        this.flFlag = flFlag;
    }
    public String getFlMc() {
        return this.flMc;
    }

    public void setFlMc(String flMc) {
        this.flMc = flMc;
    }
    public Integer getFlUnit() {
        return this.flUnit;
    }

    public void setFlUnit(Integer flUnit) {
        this.flUnit = flUnit;
    }
    public Integer getFlCountIn() {
        return this.flCountIn;
    }

    public void setFlCountIn(Integer flCountIn) {
        this.flCountIn = flCountIn;
    }
    public Integer getFlCountOut() {
        return this.flCountOut;
    }

    public void setFlCountOut(Integer flCountOut) {
        this.flCountOut = flCountOut;
    }
    public Integer getFlBeginTime() {
        return this.flBeginTime;
    }

    public void setFlBeginTime(Integer flBeginTime) {
        this.flBeginTime = flBeginTime;
    }
    public Integer getFlBeginUnit() {
        return this.flBeginUnit;
    }

    public void setFlBeginUnit(Integer flBeginUnit) {
        this.flBeginUnit = flBeginUnit;
    }
    public Integer getFlBeginCountIn() {
        return this.flBeginCountIn;
    }

    public void setFlBeginCountIn(Integer flBeginCountIn) {
        this.flBeginCountIn = flBeginCountIn;
    }
    public Integer getFlBeginCountOut() {
        return this.flBeginCountOut;
    }

    public void setFlBeginCountOut(Integer flBeginCountOut) {
        this.flBeginCountOut = flBeginCountOut;
    }
    public Integer getWebid() {
        return this.webid;
    }

    public void setWebid(Integer webid) {
        this.webid = webid;
    }
}
