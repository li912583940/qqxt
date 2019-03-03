package com.sl.ue.entity.jl;

import com.sl.ue.util.anno.DbField;
import com.sl.ue.util.anno.Id;
import com.sl.ue.util.anno.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table("JL_QQ_REC")
public class JlQqRec implements java.io.Serializable{

    /** 序列化 */
    private static final long serialVersionUID = 1L;

    /**  */
    @DbField("Call_ID")
    private String callId;

    /**  */
    @DbField("Line_No")
    private Integer lineNo;

    /**  */
    @DbField("LocalTele")
    private String localtele;

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
    @DbField("JY")
    private String jy;

    /**  */
    @DbField("JQ_No")
    private String jqNo;

    /**  */
    @DbField("JQ_Name")
    private String jqName;

    /**  */
    @DbField("FR_No")
    private String frNo;

    /**  */
    @DbField("FR_Name")
    private String frName;

    /**  */
    @DbField("QS_SFZ")
    private String qsSfz;

    /**  */
    @DbField("QS_Name")
    private String qsName;

    /**  */
    @DbField("GX")
    private String gx;

    /**  */
    @DbField("YJ_No")
    private String yjNo;

    /**  */
    @DbField("YJ_Name")
    private String yjName;

    /**  */
    @DbField("Tele")
    private String tele;

    /**  */
    @DbField("Type")
    private Integer type;

    /**  */
    @DbField("Call_Time_Start")
    private String callTimeStart;

    /**  */
    @DbField("Call_Time_End")
    private String callTimeEnd;

    /**  */
    @DbField("Call_Time_Len")
    private Integer callTimeLen;

    /**  */
    @DbField("Call_RecFile")
    private String callRecfile;

    /**  */
    @DbField("Delete_Flag")
    private Integer deleteFlag;

    /**  */
    @DbField("Call_Count_IN")
    private Integer callCountIn;

    /**  */
    @DbField("Call_Count_OUT")
    private Integer callCountOut;

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
    @DbField("Call_Count_Flag")
    private Integer callCountFlag;

    /**  */
    @DbField("Call_Count_Time")
    private Date callCountTime;

    /**  */
    @DbField("Call_Count_No")
    private String callCountNo;

    /**  */
    @DbField("Call_Count_Type")
    private Integer callCountType;

    /**  */
    @Id
    @DbField("WebID")
    private BigDecimal webid;

    public String getCallId() {
        return this.callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }
    public Integer getLineNo() {
        return this.lineNo;
    }

    public void setLineNo(Integer lineNo) {
        this.lineNo = lineNo;
    }
    public String getLocaltele() {
        return this.localtele;
    }

    public void setLocaltele(String localtele) {
        this.localtele = localtele;
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
    public String getJy() {
        return this.jy;
    }

    public void setJy(String jy) {
        this.jy = jy;
    }
    public String getJqNo() {
        return this.jqNo;
    }

    public void setJqNo(String jqNo) {
        this.jqNo = jqNo;
    }
    public String getJqName() {
        return this.jqName;
    }

    public void setJqName(String jqName) {
        this.jqName = jqName;
    }
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
    public String getQsSfz() {
        return this.qsSfz;
    }

    public void setQsSfz(String qsSfz) {
        this.qsSfz = qsSfz;
    }
    public String getQsName() {
        return this.qsName;
    }

    public void setQsName(String qsName) {
        this.qsName = qsName;
    }
    public String getGx() {
        return this.gx;
    }

    public void setGx(String gx) {
        this.gx = gx;
    }
    public String getYjNo() {
        return this.yjNo;
    }

    public void setYjNo(String yjNo) {
        this.yjNo = yjNo;
    }
    public String getYjName() {
        return this.yjName;
    }

    public void setYjName(String yjName) {
        this.yjName = yjName;
    }
    public String getTele() {
        return this.tele;
    }

    public void setTele(String tele) {
        this.tele = tele;
    }
    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
    public String getCallTimeStart() {
        return this.callTimeStart;
    }

    public void setCallTimeStart(String callTimeStart) {
        this.callTimeStart = callTimeStart;
    }
    public String getCallTimeEnd() {
        return this.callTimeEnd;
    }

    public void setCallTimeEnd(String callTimeEnd) {
        this.callTimeEnd = callTimeEnd;
    }
    public Integer getCallTimeLen() {
        return this.callTimeLen;
    }

    public void setCallTimeLen(Integer callTimeLen) {
        this.callTimeLen = callTimeLen;
    }
    public String getCallRecfile() {
        return this.callRecfile;
    }

    public void setCallRecfile(String callRecfile) {
        this.callRecfile = callRecfile;
    }
    public Integer getDeleteFlag() {
        return this.deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
    public Integer getCallCountIn() {
        return this.callCountIn;
    }

    public void setCallCountIn(Integer callCountIn) {
        this.callCountIn = callCountIn;
    }
    public Integer getCallCountOut() {
        return this.callCountOut;
    }

    public void setCallCountOut(Integer callCountOut) {
        this.callCountOut = callCountOut;
    }
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
    public Integer getCallCountFlag() {
        return this.callCountFlag;
    }

    public void setCallCountFlag(Integer callCountFlag) {
        this.callCountFlag = callCountFlag;
    }
    public Date getCallCountTime() {
        return this.callCountTime;
    }

    public void setCallCountTime(Date callCountTime) {
        this.callCountTime = callCountTime;
    }
    public String getCallCountNo() {
        return this.callCountNo;
    }

    public void setCallCountNo(String callCountNo) {
        this.callCountNo = callCountNo;
    }
    public Integer getCallCountType() {
        return this.callCountType;
    }

    public void setCallCountType(Integer callCountType) {
        this.callCountType = callCountType;
    }
    public BigDecimal getWebid() {
        return this.webid;
    }

    public void setWebid(BigDecimal webid) {
        this.webid = webid;
    }
}
