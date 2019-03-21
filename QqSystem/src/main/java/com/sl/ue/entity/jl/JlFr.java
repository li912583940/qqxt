package com.sl.ue.entity.jl;

import com.sl.ue.util.anno.DbField;
import com.sl.ue.util.anno.Id;
import com.sl.ue.util.anno.Table;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

@Table("JL_FR")
public class JlFr implements java.io.Serializable{

    /** 序列化 */
    private static final long serialVersionUID = 1L;

    /**  */
    @DbField("FR_No")
    private String frNo;

    /**  */
    @DbField("FR_Name")
    private String frName;

    /**  */
    @DbField("FR_Card")
    private String frCard;

    /**  */
    @DbField("JY")
    private String jy;

    /**  */
    @DbField("JQ")
    private String jq;

    /**  */
    @DbField("JB_No")
    private String jbNo;

    /**  */
    @DbField("JB_SetTime")
    private Integer jbSettime;

    /**  */
    @DbField("JB_SetType")
    private Integer jbSettype;

    /**  */
    @DbField("QQ_JB")
    private Integer qqJb;

    /**  */
    @DbField("QQ_Use")
    private Integer qqUse;

    /**  */
    @DbField("QQ_Left")
    private Integer qqLeft;

    /**  */
    @DbField("QQ_YE")
    private Integer qqYe;

    /**  */
    @DbField("QQ_ZH")
    private String qqZh;

    /**  */
    @DbField("QQ_MM")
    private String qqMm;

    /**  */
    @DbField("HJ_JB")
    private Integer hjJb;

    /**  */
    @DbField("HJ_Use")
    private Integer hjUse;

    /**  */
    @DbField("HJ_Left")
    private Integer hjLeft;

    /**  */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DbField("HJ_Last_Time")
    private Date hjLastTime;

    /**  */
    @DbField("Monitor_Flag")
    private String monitorFlag;

    /**  */
    @DbField("State")
    private Integer state;

    /**  */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DbField("OutTime")
    private Date outtime;

    /**  */
    @DbField("SP_State")
    private Integer spState;

    /**  */
    @DbField("SP_UserNo")
    private String spUserno;

    /**  */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DbField("SP_Time")
    private Date spTime;

    /**  */
    @DbField("SP_Info")
    private String spInfo;

    /**  */
    @DbField("SP_Mon")
    private String spMon;

    /**  */
    @DbField("Info_RJSJ")
    private String infoRjsj;

    /**  */
    @DbField("Info_JG")
    private String infoJg;

    /**  */
    @DbField("Info_ZM")
    private String infoZm;

    /**  */
    @DbField("Info_XQ")
    private String infoXq;

    /**  */
    @DbField("Info_DQ")
    private String infoDq;

    /**  */
    @DbField("Info_ZDZF")
    private String infoZdzf;

    /**  */
    @DbField("Info_CSRQ")
    private String infoCsrq;

    /**  */
    @DbField("Info_HOME")
    private String infoHome;

    /**  */
    @DbField("InfoLjsj")
    private String infoljsj;

    /**  */
    @DbField("InfoLjqx")
    private String infoljqx;

    /**  */
    @DbField("InfoLj")
    private Integer infolj;

    /**  */
    @DbField("ZP")
    private Byte[] zp;

    /**  */
    @DbField("FR_MM")
    private String frMm;

    /**  */
    @Id
    @DbField("WebID")
    private Integer webid;

    /**  */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DbField("Create_Time")
    private Date createTime;

    public String getFrNo() {
        return this.frNo;
    }

    public void setFrNo(String frNo) {
        this.frNo = frNo;
    }
    public String getFrName() {
        return this.frName;
    }

    public void setFrName(String frName) {
        this.frName = frName;
    }
    public String getFrCard() {
        return this.frCard;
    }

    public void setFrCard(String frCard) {
        this.frCard = frCard;
    }
    public String getJy() {
        return this.jy;
    }

    public void setJy(String jy) {
        this.jy = jy;
    }
    public String getJq() {
        return this.jq;
    }

    public void setJq(String jq) {
        this.jq = jq;
    }
    public String getJbNo() {
        return this.jbNo;
    }

    public void setJbNo(String jbNo) {
        this.jbNo = jbNo;
    }
    public Integer getJbSettime() {
        return this.jbSettime;
    }

    public void setJbSettime(Integer jbSettime) {
        this.jbSettime = jbSettime;
    }
    public Integer getJbSettype() {
        return this.jbSettype;
    }

    public void setJbSettype(Integer jbSettype) {
        this.jbSettype = jbSettype;
    }
    public Integer getQqJb() {
        return this.qqJb;
    }

    public void setQqJb(Integer qqJb) {
        this.qqJb = qqJb;
    }
    public Integer getQqUse() {
        return this.qqUse;
    }

    public void setQqUse(Integer qqUse) {
        this.qqUse = qqUse;
    }
    public Integer getQqLeft() {
        return this.qqLeft;
    }

    public void setQqLeft(Integer qqLeft) {
        this.qqLeft = qqLeft;
    }
    public Integer getQqYe() {
        return this.qqYe;
    }

    public void setQqYe(Integer qqYe) {
        this.qqYe = qqYe;
    }
    public String getQqZh() {
        return this.qqZh;
    }

    public void setQqZh(String qqZh) {
        this.qqZh = qqZh;
    }
    public String getQqMm() {
        return this.qqMm;
    }

    public void setQqMm(String qqMm) {
        this.qqMm = qqMm;
    }
    public Integer getHjJb() {
        return this.hjJb;
    }

    public void setHjJb(Integer hjJb) {
        this.hjJb = hjJb;
    }
    public Integer getHjUse() {
        return this.hjUse;
    }

    public void setHjUse(Integer hjUse) {
        this.hjUse = hjUse;
    }
    public Integer getHjLeft() {
        return this.hjLeft;
    }

    public void setHjLeft(Integer hjLeft) {
        this.hjLeft = hjLeft;
    }
    public Date getHjLastTime() {
        return this.hjLastTime;
    }

    public void setHjLastTime(Date hjLastTime) {
        this.hjLastTime = hjLastTime;
    }
    public String getMonitorFlag() {
        return this.monitorFlag;
    }

    public void setMonitorFlag(String monitorFlag) {
        this.monitorFlag = monitorFlag;
    }
    public Integer getState() {
        return this.state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
    public Date getOuttime() {
        return this.outtime;
    }

    public void setOuttime(Date outtime) {
        this.outtime = outtime;
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
    public String getInfoRjsj() {
        return this.infoRjsj;
    }

    public void setInfoRjsj(String infoRjsj) {
        this.infoRjsj = infoRjsj;
    }
    public String getInfoJg() {
        return this.infoJg;
    }

    public void setInfoJg(String infoJg) {
        this.infoJg = infoJg;
    }
    public String getInfoZm() {
        return this.infoZm;
    }

    public void setInfoZm(String infoZm) {
        this.infoZm = infoZm;
    }
    public String getInfoXq() {
        return this.infoXq;
    }

    public void setInfoXq(String infoXq) {
        this.infoXq = infoXq;
    }
    public String getInfoDq() {
        return this.infoDq;
    }

    public void setInfoDq(String infoDq) {
        this.infoDq = infoDq;
    }
    public String getInfoZdzf() {
        return this.infoZdzf;
    }

    public void setInfoZdzf(String infoZdzf) {
        this.infoZdzf = infoZdzf;
    }
    public String getInfoCsrq() {
        return this.infoCsrq;
    }

    public void setInfoCsrq(String infoCsrq) {
        this.infoCsrq = infoCsrq;
    }
    public String getInfoHome() {
        return this.infoHome;
    }

    public void setInfoHome(String infoHome) {
        this.infoHome = infoHome;
    }
    public String getInfoljsj() {
        return this.infoljsj;
    }

    public void setInfoljsj(String infoljsj) {
        this.infoljsj = infoljsj;
    }
    public String getInfoljqx() {
        return this.infoljqx;
    }

    public void setInfoljqx(String infoljqx) {
        this.infoljqx = infoljqx;
    }
    public Integer getInfolj() {
        return this.infolj;
    }

    public void setInfolj(Integer infolj) {
        this.infolj = infolj;
    }
    public Byte[] getZp() {
        return this.zp;
    }

    public void setZp(Byte[] zp) {
        this.zp = zp;
    }
    public String getFrMm() {
        return this.frMm;
    }

    public void setFrMm(String frMm) {
        this.frMm = frMm;
    }
    public Integer getWebid() {
        return this.webid;
    }

    public void setWebid(Integer webid) {
        this.webid = webid;
    }
    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
