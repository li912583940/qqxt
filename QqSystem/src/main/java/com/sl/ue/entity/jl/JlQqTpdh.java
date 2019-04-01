package com.sl.ue.entity.jl;

import com.sl.ue.util.anno.DbField;
import com.sl.ue.util.anno.Id;
import com.sl.ue.util.anno.Table;
import java.util.Date;

@Table("JL_QQ_TPDH")
public class JlQqTpdh implements java.io.Serializable{

    /** 序列化 */
    private static final long serialVersionUID = 1L;

    /**  */
    @Id
    @DbField("TPID")
    private Integer tpid;

    /**  */
    @DbField("FR_No")
    private String frNo;

    /**  */
    @DbField("TP_Tele")
    private String tpTele;

    /**  */
    @DbField("TPXM")
    private String tpxm;

    /**  */
    @DbField("TPGX")
    private String tpgx;

    /**  */
    @DbField("TPCS")
    private Integer tpcs;

    /**  */
    @DbField("SYCS")
    private Integer sycs;

    /**  */
    @DbField("TPSC")
    private Integer tpsc;

    /**  */
    @DbField("TPR_No")
    private String tprNo;

    /**  */
    @DbField("TPR_Name")
    private String tprName;

    /**  */
    @DbField("TPSJ")
    private Date tpsj;

    /**  */
    @DbField("SP_State")
    private Integer spState;

    /**  */
    @DbField("SP_UserNo")
    private String spUserno;

    /**  */
    @DbField("SP_Time")
    private Date spTime;

    /**  */
    @DbField("SP_Info")
    private String spInfo;

    /**  */
    @DbField("SP_Mon")
    private String spMon;

    /**  */
    @DbField("Talk_TimeLen")
    private Integer talkTimelen;

    /**  */
    @DbField("Talk_TimeOver")
    private Date talkTimeover;

    public Integer getTpid() {
        return this.tpid;
    }

    public void setTpid(Integer tpid) {
        this.tpid = tpid;
    }
    public String getFrNo() {
        return this.frNo;
    }

    public void setFrNo(String frNo) {
        this.frNo = frNo;
    }
    public String getTpTele() {
        return this.tpTele;
    }

    public void setTpTele(String tpTele) {
        this.tpTele = tpTele;
    }
    public String getTpxm() {
        return this.tpxm;
    }

    public void setTpxm(String tpxm) {
        this.tpxm = tpxm;
    }
    public String getTpgx() {
        return this.tpgx;
    }

    public void setTpgx(String tpgx) {
        this.tpgx = tpgx;
    }
    public Integer getTpcs() {
        return this.tpcs;
    }

    public void setTpcs(Integer tpcs) {
        this.tpcs = tpcs;
    }
    public Integer getSycs() {
        return this.sycs;
    }

    public void setSycs(Integer sycs) {
        this.sycs = sycs;
    }
    public Integer getTpsc() {
        return this.tpsc;
    }

    public void setTpsc(Integer tpsc) {
        this.tpsc = tpsc;
    }
    public String getTprNo() {
        return this.tprNo;
    }

    public void setTprNo(String tprNo) {
        this.tprNo = tprNo;
    }
    public String getTprName() {
        return this.tprName;
    }

    public void setTprName(String tprName) {
        this.tprName = tprName;
    }
    public Date getTpsj() {
        return this.tpsj;
    }

    public void setTpsj(Date tpsj) {
        this.tpsj = tpsj;
    }
    public Integer getSpState() {
        return this.spState;
    }

    public void setSpState(Integer spState) {
        this.spState = spState;
    }
    public String getSpUserno() {
        return this.spUserno;
    }

    public void setSpUserno(String spUserno) {
        this.spUserno = spUserno;
    }
    public Date getSpTime() {
        return this.spTime;
    }

    public void setSpTime(Date spTime) {
        this.spTime = spTime;
    }
    public String getSpInfo() {
        return this.spInfo;
    }

    public void setSpInfo(String spInfo) {
        this.spInfo = spInfo;
    }
    public String getSpMon() {
        return this.spMon;
    }

    public void setSpMon(String spMon) {
        this.spMon = spMon;
    }
    public Integer getTalkTimelen() {
        return this.talkTimelen;
    }

    public void setTalkTimelen(Integer talkTimelen) {
        this.talkTimelen = talkTimelen;
    }
    public Date getTalkTimeover() {
        return this.talkTimeover;
    }

    public void setTalkTimeover(Date talkTimeover) {
        this.talkTimeover = talkTimeover;
    }
}
