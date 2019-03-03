package com.sl.ue.entity.sys.vo;

import com.sl.ue.entity.sys.SysQqLine;

public class SysQqLineVO extends SysQqLine{

    /** 序列化 */
    private static final long serialVersionUID = 1L;

    private String ip;
    private String port;
    private String audioPort;
    private String writeTxt;
    private String jqName;

    
    /*---------------------------  处理关联表  -----------------------------*/

  	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getAudioPort() {
		return audioPort;
	}

	public void setAudioPort(String audioPort) {
		this.audioPort = audioPort;
	}

	public String getWriteTxt() {
		return writeTxt;
	}

	public void setWriteTxt(String writeTxt) {
		this.writeTxt = writeTxt;
	}

	public String getJqName() {
  		return jqName;
  	}
	
	public void setJqName(String jqName) {
  		this.jqName = jqName;
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
