package com.sl.ue.entity.jl;

import com.sl.ue.util.anno.DbField;
import com.sl.ue.util.anno.Id;
import com.sl.ue.util.anno.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table("JL_QQ_CZ")
public class JlQqCz implements java.io.Serializable{

    /** 序列化 */
    private static final long serialVersionUID = 1L;

    /**  */
    @Id
    @DbField("CZ_ID")
    private BigDecimal czId;

    /**  */
    @DbField("FR_No")
    private String frNo;

    /**  */
    @DbField("FR_Name")
    private String frName;

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
    @DbField("CZSJ")
    private Date czsj;

    /**  */
    @DbField("CZJE")
    private Integer czje;

    /**  */
    @DbField("CZR_No")
    private String czrNo;

    /**  */
    @DbField("CZR_Name")
    private String czrName;

    /**  */
    @DbField("SCSJ")
    private Date scsj;

    /**  */
    @DbField("SCR_No")
    private String scrNo;

    /**  */
    @DbField("SCR_Name")
    private String scrName;

    /**  */
    @DbField("CZZT")
    private Integer czzt;

    /**  */
    @DbField("Info1")
    private String info1;

    /**  */
    @DbField("CZ_State")
    private Integer czState;

    public BigDecimal getCzId() {
        return this.czId;
    }

    public void setCzId(BigDecimal czId) {
        this.czId = czId;
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
    public Date getCzsj() {
        return this.czsj;
    }

    public void setCzsj(Date czsj) {
        this.czsj = czsj;
    }
    public Integer getCzje() {
        return this.czje;
    }

    public void setCzje(Integer czje) {
        this.czje = czje;
    }
    public String getCzrNo() {
        return this.czrNo;
    }

    public void setCzrNo(String czrNo) {
        this.czrNo = czrNo;
    }
    public String getCzrName() {
        return this.czrName;
    }

    public void setCzrName(String czrName) {
        this.czrName = czrName;
    }
    public Date getScsj() {
        return this.scsj;
    }

    public void setScsj(Date scsj) {
        this.scsj = scsj;
    }
    public String getScrNo() {
        return this.scrNo;
    }

    public void setScrNo(String scrNo) {
        this.scrNo = scrNo;
    }
    public String getScrName() {
        return this.scrName;
    }

    public void setScrName(String scrName) {
        this.scrName = scrName;
    }
    public Integer getCzzt() {
        return this.czzt;
    }

    public void setCzzt(Integer czzt) {
        this.czzt = czzt;
    }
    public String getInfo1() {
        return this.info1;
    }

    public void setInfo1(String info1) {
        this.info1 = info1;
    }
    public Integer getCzState() {
        return this.czState;
    }

    public void setCzState(Integer czState) {
        this.czState = czState;
    }
}
