package com.sl.ue.entity.sys;

import com.sl.ue.util.anno.DbField;
import com.sl.ue.util.anno.Id;
import com.sl.ue.util.anno.Table;
import java.util.Date;

@Table("SYS_QQ_LINE")
public class SysQqLine implements java.io.Serializable{

    /** 序列化 */
    private static final long serialVersionUID = 1L;

    /**  */
    @DbField("Line_No")
    private Integer lineNo;

    /**  */
    @DbField("In_Board")
    private String inBoard;

    /**  */
    @DbField("In_Line")
    private Integer inLine;

    /**  */
    @DbField("Out_Board")
    private String outBoard;

    /**  */
    @DbField("Out_Line")
    private Integer outLine;

    /**  */
    @DbField("Out_CallID")
    private String outCallid;

    /**  */
    @DbField("Mon_Board")
    private String monBoard;

    /**  */
    @DbField("Mon_Line")
    private Integer monLine;

    /**  */
    @DbField("JY")
    private String jy;

    /**  */
    @DbField("JQ")
    private String jq;

    /**  */
    @DbField("DKQ")
    private String dkq;

    /**  */
    @DbField("LocalTele")
    private String localtele;

    /**  */
    @DbField("State")
    private Integer state;

    /**  */
    @DbField("Monitor_State")
    private String monitorState;

    /**  */
    @DbField("Monitor_Tele")
    private String monitorTele;

    /**  */
    @DbField("Monitor_Type")
    private String monitorType;

    /**  */
    @DbField("Monitor_FR")
    private String monitorFr;

    /**  */
    @DbField("Monitor_QS")
    private String monitorQs;

    /**  */
    @DbField("Monitor_GX")
    private String monitorGx;

    /**  */
    @DbField("Monitor_YJ")
    private String monitorYj;

    /**  */
    @DbField("Monitor_Time")
    private String monitorTime;

    /**  */
    @DbField("Monitor_CallID")
    private String monitorCallid;

    /**  */
    @DbField("Monitor_FrNo")
    private String monitorFrno;

    /**  */
    @Id
    @DbField("WebID")
    private Integer webid;

    /**  */
    @DbField("In_Voip")
    private Integer inVoip;

    /**  */
    @DbField("Out_Voip")
    private Integer outVoip;

    /**  */
    @DbField("Mon_Voip")
    private Integer monVoip;

    /**  */
    @DbField("In_Dsp")
    private Integer inDsp;

    /**  */
    @DbField("Out_Dsp")
    private Integer outDsp;

    /**  */
    @DbField("Mon_Dsp")
    private Integer monDsp;

    /**  */
    @DbField("Hy_Dsp")
    private Integer hyDsp;

    /**  */
    @DbField("Hy_Dsp1")
    private Integer hyDsp1;

    /**  */
    @DbField("YJ_No_Pwd")
    private String yjNoPwd;

    /**  */
    @DbField("UseTimeLen_Pwd")
    private Integer usetimelenPwd;

    /**  */
    @DbField("LastTime_Pwd")
    private Date lasttimePwd;

    public Integer getLineNo() {
        return this.lineNo;
    }

    public void setLineNo(Integer lineNo) {
        this.lineNo = lineNo;
    }
    public String getInBoard() {
        return this.inBoard;
    }

    public void setInBoard(String inBoard) {
        this.inBoard = inBoard;
    }
    public Integer getInLine() {
        return this.inLine;
    }

    public void setInLine(Integer inLine) {
        this.inLine = inLine;
    }
    public String getOutBoard() {
        return this.outBoard;
    }

    public void setOutBoard(String outBoard) {
        this.outBoard = outBoard;
    }
    public Integer getOutLine() {
        return this.outLine;
    }

    public void setOutLine(Integer outLine) {
        this.outLine = outLine;
    }
    public String getOutCallid() {
        return this.outCallid;
    }

    public void setOutCallid(String outCallid) {
        this.outCallid = outCallid;
    }
    public String getMonBoard() {
        return this.monBoard;
    }

    public void setMonBoard(String monBoard) {
        this.monBoard = monBoard;
    }
    public Integer getMonLine() {
        return this.monLine;
    }

    public void setMonLine(Integer monLine) {
        this.monLine = monLine;
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
    public String getDkq() {
        return this.dkq;
    }

    public void setDkq(String dkq) {
        this.dkq = dkq;
    }
    public String getLocaltele() {
        return this.localtele;
    }

    public void setLocaltele(String localtele) {
        this.localtele = localtele;
    }
    public Integer getState() {
        return this.state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
    public String getMonitorState() {
        return this.monitorState;
    }

    public void setMonitorState(String monitorState) {
        this.monitorState = monitorState;
    }
    public String getMonitorTele() {
        return this.monitorTele;
    }

    public void setMonitorTele(String monitorTele) {
        this.monitorTele = monitorTele;
    }
    public String getMonitorType() {
        return this.monitorType;
    }

    public void setMonitorType(String monitorType) {
        this.monitorType = monitorType;
    }
    public String getMonitorFr() {
        return this.monitorFr;
    }

    public void setMonitorFr(String monitorFr) {
        this.monitorFr = monitorFr;
    }
    public String getMonitorQs() {
        return this.monitorQs;
    }

    public void setMonitorQs(String monitorQs) {
        this.monitorQs = monitorQs;
    }
    public String getMonitorGx() {
        return this.monitorGx;
    }

    public void setMonitorGx(String monitorGx) {
        this.monitorGx = monitorGx;
    }
    public String getMonitorYj() {
        return this.monitorYj;
    }

    public void setMonitorYj(String monitorYj) {
        this.monitorYj = monitorYj;
    }
    public String getMonitorTime() {
        return this.monitorTime;
    }

    public void setMonitorTime(String monitorTime) {
        this.monitorTime = monitorTime;
    }
    public String getMonitorCallid() {
        return this.monitorCallid;
    }

    public void setMonitorCallid(String monitorCallid) {
        this.monitorCallid = monitorCallid;
    }
    public String getMonitorFrno() {
        return this.monitorFrno;
    }

    public void setMonitorFrno(String monitorFrno) {
        this.monitorFrno = monitorFrno;
    }
    public Integer getWebid() {
        return this.webid;
    }

    public void setWebid(Integer webid) {
        this.webid = webid;
    }
    public Integer getInVoip() {
        return this.inVoip;
    }

    public void setInVoip(Integer inVoip) {
        this.inVoip = inVoip;
    }
    public Integer getOutVoip() {
        return this.outVoip;
    }

    public void setOutVoip(Integer outVoip) {
        this.outVoip = outVoip;
    }
    public Integer getMonVoip() {
        return this.monVoip;
    }

    public void setMonVoip(Integer monVoip) {
        this.monVoip = monVoip;
    }
    public Integer getInDsp() {
        return this.inDsp;
    }

    public void setInDsp(Integer inDsp) {
        this.inDsp = inDsp;
    }
    public Integer getOutDsp() {
        return this.outDsp;
    }

    public void setOutDsp(Integer outDsp) {
        this.outDsp = outDsp;
    }
    public Integer getMonDsp() {
        return this.monDsp;
    }

    public void setMonDsp(Integer monDsp) {
        this.monDsp = monDsp;
    }
    public Integer getHyDsp() {
        return this.hyDsp;
    }

    public void setHyDsp(Integer hyDsp) {
        this.hyDsp = hyDsp;
    }
    public Integer getHyDsp1() {
        return this.hyDsp1;
    }

    public void setHyDsp1(Integer hyDsp1) {
        this.hyDsp1 = hyDsp1;
    }
    public String getYjNoPwd() {
        return this.yjNoPwd;
    }

    public void setYjNoPwd(String yjNoPwd) {
        this.yjNoPwd = yjNoPwd;
    }
    public Integer getUsetimelenPwd() {
        return this.usetimelenPwd;
    }

    public void setUsetimelenPwd(Integer usetimelenPwd) {
        this.usetimelenPwd = usetimelenPwd;
    }
    public Date getLasttimePwd() {
        return this.lasttimePwd;
    }

    public void setLasttimePwd(Date lasttimePwd) {
        this.lasttimePwd = lasttimePwd;
    }
}
