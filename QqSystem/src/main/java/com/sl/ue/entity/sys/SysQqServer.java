package com.sl.ue.entity.sys;

import com.sl.ue.util.anno.DbField;
import com.sl.ue.util.anno.Id;
import com.sl.ue.util.anno.Table;

@Table("SYS_QQ_SERVER")
public class SysQqServer implements java.io.Serializable{

    /** 序列化 */
    private static final long serialVersionUID = 1L;

    /**  */
    @DbField("Server_Name")
    private String serverName;

    /**  */
    @DbField("IP")
    private String ip;

    /**  */
    @DbField("Port")
    private Integer port;

    /**  */
    @DbField("AudioPort")
    private Integer audioport;

    /**  */
    @DbField("RecUrl")
    private String recurl;

    /**  */
    @DbField("QQResetDate")
    private String qqresetdate;

    /**  */
    @Id
    @DbField("WebID")
    private Integer webid;

    public String getServerName() {
        return this.serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    public Integer getPort() {
        return this.port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
    public Integer getAudioport() {
        return this.audioport;
    }

    public void setAudioport(Integer audioport) {
        this.audioport = audioport;
    }
    public String getRecurl() {
        return this.recurl;
    }

    public void setRecurl(String recurl) {
        this.recurl = recurl;
    }
    public String getQqresetdate() {
        return this.qqresetdate;
    }

    public void setQqresetdate(String qqresetdate) {
        this.qqresetdate = qqresetdate;
    }
    public Integer getWebid() {
        return this.webid;
    }

    public void setWebid(Integer webid) {
        this.webid = webid;
    }
}
