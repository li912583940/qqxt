package com.sl.ue.entity.jl.vo;

import com.sl.ue.entity.jl.JlYj;

public class JlYjVO extends JlYj{

    /** 序列化 */
    private static final long serialVersionUID = 1L;

    private String jqName;


    public String getJqName() {
  		return jqName;
  	}

  	public void setJqName(String jqName) {
  		this.jqName = jqName;
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
