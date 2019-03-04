package com.sl.ue.entity.jl;

import com.sl.ue.util.anno.DbField;
import com.sl.ue.util.anno.Id;
import com.sl.ue.util.anno.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table("JL_QQ_INFO")
public class JlQqInfo implements java.io.Serializable{

    /** 序列化 */
    private static final long serialVersionUID = 1L;
    @DbField("Call_ID")
    private String callId;
    @DbField("User_No")
    private String userNo;
    @DbField("User_Name")
	private String userName;
    @DbField("Write_Txt")
    private String writeTxt;
    @DbField("Create_Time")
	private Date createTime;
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
    
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
    
}
