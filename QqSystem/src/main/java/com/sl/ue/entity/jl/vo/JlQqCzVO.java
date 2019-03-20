package com.sl.ue.entity.jl.vo;

import com.sl.ue.entity.jl.JlQqCz;

public class JlQqCzVO extends JlQqCz{

	private String callTimeStart;  // 开始日期
	private String callTimeEnd; // 结束日期
	private Integer type; //充值方式  1:充值, 2:退费
    /** 序列化 */
    private static final long serialVersionUID = 1L;



    /*---------------------------  处理关联表  -----------------------------*/

    private String leftJoinField; // 关联表字段

    private String leftJoinTable; // 关联表

    private String leftJoinWhere; // 关联表条件

    
    public String getCallTimeStart() {
		return callTimeStart;
	}

	public void setCallTimeStart(String callTimeStart) {
		this.callTimeStart = callTimeStart;
	}

	public String getCallTimeEnd() {
		return callTimeEnd;
	}

	public void setCallTimeEnd(String callTimeEnd) {
		this.callTimeEnd = callTimeEnd;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

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
