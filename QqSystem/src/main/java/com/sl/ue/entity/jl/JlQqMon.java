package com.sl.ue.entity.jl;

import com.sl.ue.util.anno.DbField;
import com.sl.ue.util.anno.Id;
import com.sl.ue.util.anno.Table;
import java.math.BigDecimal;

@Table("JL_QQ_MON")
public class JlQqMon implements java.io.Serializable{

    /** 序列化 */
    private static final long serialVersionUID = 1L;

    /**  */
    @DbField("Call_ID")
    private String callId;

    /**  */
    @DbField("User_No")
    private String userNo;

    /**  */
    @DbField("Write_Txt")
    private String writeTxt;

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
    public String getUserNo() {
        return this.userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }
    public String getWriteTxt() {
        return this.writeTxt;
    }

    public void setWriteTxt(String writeTxt) {
        this.writeTxt = writeTxt;
    }
    public BigDecimal getWebid() {
        return this.webid;
    }

    public void setWebid(BigDecimal webid) {
        this.webid = webid;
    }
}
