package com.sl.ue.entity.sys;

import com.sl.ue.util.anno.DbField;
import com.sl.ue.util.anno.Id;
import com.sl.ue.util.anno.Table;

@Table("SYS_HOLIDAY")
public class SysHoliday implements java.io.Serializable{

    /** 序列化 */
    private static final long serialVersionUID = 1L;

    /**  */
    @DbField("Holiday_Date")
    private String holidayDate;

    /**  */
    @DbField("Holiday_Name")
    private String holidayName;

    /**  */
    @Id
    @DbField("WebID")
    private Integer webid;

    public String getHolidayDate() {
        return this.holidayDate;
    }

    public void setHolidayDate(String holidayDate) {
        this.holidayDate = holidayDate;
    }
    public String getHolidayName() {
        return this.holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }
    public Integer getWebid() {
        return this.webid;
    }

    public void setWebid(Integer webid) {
        this.webid = webid;
    }
}
