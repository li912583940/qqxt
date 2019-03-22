package com.sl.ue.entity.jl;

import com.sl.ue.util.anno.DbField;
import com.sl.ue.util.anno.Id;
import com.sl.ue.util.anno.Table;

@Table("JL_JQ_WEEK_TIME")
public class JlJqWeekTime implements java.io.Serializable{

    /** 序列化 */
    private static final long serialVersionUID = 1L;

    /**  */
    @Id
    @DbField("Time_Index")
    private Integer timeIndex;

    /**  */
    @DbField("JQ_No")
    private String jqNo;

    /**  */
    @DbField("JY")
    private String jy;

    /**  */
    @DbField("JQ_Week")
    private Integer jqWeek;

    /**  */
    @DbField("JQ_StartTime")
    private String jqStarttime;

    /**  */
    @DbField("JQ_EndTime")
    private String jqEndtime;

    public Integer getTimeIndex() {
        return this.timeIndex;
    }

    public void setTimeIndex(Integer timeIndex) {
        this.timeIndex = timeIndex;
    }
    public String getJqNo() {
        return this.jqNo;
    }

    public void setJqNo(String jqNo) {
        this.jqNo = jqNo;
    }
    public String getJy() {
        return this.jy;
    }

    public void setJy(String jy) {
        this.jy = jy;
    }
    public Integer getJqWeek() {
        return this.jqWeek;
    }

    public void setJqWeek(Integer jqWeek) {
        this.jqWeek = jqWeek;
    }
    public String getJqStarttime() {
        return this.jqStarttime;
    }

    public void setJqStarttime(String jqStarttime) {
        this.jqStarttime = jqStarttime;
    }
    public String getJqEndtime() {
        return this.jqEndtime;
    }

    public void setJqEndtime(String jqEndtime) {
        this.jqEndtime = jqEndtime;
    }
}
