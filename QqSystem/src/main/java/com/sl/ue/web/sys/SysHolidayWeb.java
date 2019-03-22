package com.sl.ue.web.sys;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sl.ue.entity.sys.vo.SysHolidayVO;
import com.sl.ue.service.sys.SysHolidayService;
import com.sl.ue.util.http.Result;

@RestController
@RequestMapping("/sysHoliday")
public class SysHolidayWeb extends Result{

    @Autowired
    private SysHolidayService sysHolidaySQL;

    @RequestMapping("/findList")
    public String findList(SysHolidayVO model,Integer pageSize, Integer pageNum){
        List<SysHolidayVO> list = sysHolidaySQL.findList(model, pageSize, pageNum, "ASC");
        this.putData(list);
        return this.toResult();
    }

    @RequestMapping("/findPojo")
    public String findPojo(SysHolidayVO model, Integer pageSize, Integer pageNum){
        Map<String, Object> map = sysHolidaySQL.findPojo(model, pageSize, pageNum);
        this.putPojo(map);
        return this.toResult();
    }

    @RequestMapping("/findCount")
    public String findCount(SysHolidayVO model){
        Integer count = sysHolidaySQL.count(model);
        this.putJson("count", count);
        return this.toResult();
    }

    @RequestMapping("/findOne")
    public String findOne(Integer id){
        SysHolidayVO model = sysHolidaySQL.findOne(id);
        this.putJson(model);
        return this.toResult();
    }

    @RequestMapping("/add")
    public String add(String holidays){
    	if(StringUtils.isBlank(holidays)){
    		this.error(error_102);
    		return this.toResult();
    	}
    	for(String holiday : holidays.split(",")){
    		SysHolidayVO model = new SysHolidayVO();
    		model.setHolidayDate(holiday);
    		int count = sysHolidaySQL.count(model);
    		if(count<=0){
    			sysHolidaySQL.add(model);
    		}
    	}
        return this.toResult();
    }

    @RequestMapping("/edit")
    public String edit(SysHolidayVO model){
        sysHolidaySQL.edit(model);
        return this.toResult();
    }

    @RequestMapping("/delete")
    public String del(Integer id){
        sysHolidaySQL.deleteKey(id);
        return this.toResult();
    }

    @RequestMapping("/emptyDate")
    public String emptyDate(){
    	sysHolidaySQL.delete(new SysHolidayVO());
    	return this.toResult();
    }
}
