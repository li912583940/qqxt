package com.sl.ue.entity.jl.vo;

import com.sl.ue.entity.jl.JlQs;

public class JlQsVO extends JlQs{

    /** 序列化 */
    private static final long serialVersionUID = 1L;

    private String frName; // 犯人姓名
    
    private String jzBase64;
    
    private String zpBase64;
    
    public String getFrName() {
		return frName;
	}

	public void setFrName(String frName) {
		this.frName = frName;
	}
	
	public String getJzBase64() {
		return jzBase64;
	}

	public void setJzBase64(String jzBase64) {
		this.jzBase64 = jzBase64;
	}

	public String getZpBase64() {
		return zpBase64;
	}

	public void setZpBase64(String zpBase64) {
		this.zpBase64 = zpBase64;
	}

    /*---------------------------  处理关联表  -----------------------------*/


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
