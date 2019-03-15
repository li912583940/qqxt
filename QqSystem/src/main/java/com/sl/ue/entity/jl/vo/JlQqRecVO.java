package com.sl.ue.entity.jl.vo;

import com.sl.ue.entity.jl.JlQqRec;

public class JlQqRecVO extends JlQqRec{

    /** 序列化 */
    private static final long serialVersionUID = 1L;

    private String callRecfileUrl;

    private Integer jfFlag; // 是否计费 
    
    private Integer countIn; // 内部话费总额
    private Integer countOut; // 外部话费总额
    private Integer telCountNum; // 拨打次数
    
    /*---------------------------  处理关联表  -----------------------------*/
    
    public String getCallRecfileUrl() {
		return callRecfileUrl;
	}

	public void setCallRecfileUrl(String callRecfileUrl) {
		this.callRecfileUrl = callRecfileUrl;
	}

	public Integer getJfFlag() {
		return jfFlag;
	}

	public void setJfFlag(Integer jfFlag) {
		this.jfFlag = jfFlag;
	}
	
	public Integer getCountIn() {
		return countIn;
	}

	public void setCountIn(Integer countIn) {
		this.countIn = countIn;
	}

	public Integer getCountOut() {
		return countOut;
	}

	public void setCountOut(Integer countOut) {
		this.countOut = countOut;
	}

	public Integer getTelCountNum() {
		return telCountNum;
	}

	public void setTelCountNum(Integer telCountNum) {
		this.telCountNum = telCountNum;
	}

	private String leftJoinField; // 关联表字段

	private String leftJoinTable; // 关联表

    private String leftJoinWhere; // 关联表条件

    public String getLeftJoinField() {
        return leftJoinField;
    }

    public void setLeftJoinField(String leftJoinField) {
        this.leftJoinField = leftJoinField;
    }

    public String getLeftJoinTable() {
        return leftJoinTable;
    }

    public void setLeftJoinTable(String leftJoinTable) {
        this.leftJoinTable = leftJoinTable;
    }

    public String getLeftJoinWhere() {
        return leftJoinWhere;
    }

    public void setLeftJoinWhere(String leftJoinWhere) {
        this.leftJoinWhere = leftJoinWhere;
    }


}
