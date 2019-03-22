package com.sl.ue.web.jl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sl.ue.entity.jl.vo.JlJqWeekTimeVO;
import com.sl.ue.entity.sys.vo.SysQqServerVO;
import com.sl.ue.service.jl.JlJqWeekTimeService;
import com.sl.ue.service.sys.SysQqServerService;
import com.sl.ue.util.http.Result;

@RestController
@RequestMapping("/jlJqWeekTime")
public class JlJqWeekTimeWeb extends Result{

    @Autowired
    private JlJqWeekTimeService jlJqWeekTimeSQL;
    @Autowired
    private SysQqServerService sysQqServerSQL;
    @RequestMapping("/findList")
    public String findList(JlJqWeekTimeVO model,Integer pageSize, Integer pageNum){
    	if(StringUtils.isBlank(model.getJqNo())){
    		this.error(error_102);
    		return this.toResult();
    	}
    	StringBuffer leftJoinField = new StringBuffer();
		leftJoinField.append(",b.JQ_Name AS jqName");
		model.setLeftJoinField(leftJoinField.toString());
		
		StringBuffer leftJoinTable = new StringBuffer();
		leftJoinTable.append(" LEFT JOIN JL_JQ b ON a.JQ_No=b.JQ_No");
		model.setLeftJoinTable(leftJoinTable.toString());
        List<JlJqWeekTimeVO> list = jlJqWeekTimeSQL.findList(model, pageSize, pageNum, "ASC");
        this.putData(list);
        return this.toResult();
    }

    @RequestMapping("/findPojo")
    public String findPojo(JlJqWeekTimeVO model, Integer pageSize, Integer pageNum){
        Map<String, Object> map = jlJqWeekTimeSQL.findPojo(model, pageSize, pageNum);
        this.putPojo(map);
        return this.toResult();
    }

    @RequestMapping("/findCount")
    public String findCount(JlJqWeekTimeVO model){
        Integer count = jlJqWeekTimeSQL.count(model);
        this.putJson("count", count);
        return this.toResult();
    }

    @RequestMapping("/findOne")
    public String findOne(Integer id){
        JlJqWeekTimeVO model = jlJqWeekTimeSQL.findOne(id);
        this.putJson(model);
        return this.toResult();
    }

    @RequestMapping("/add")
    public String add(JlJqWeekTimeVO model){
    	if(model.getJqWeek()!=null){
    		JlJqWeekTimeVO query = new JlJqWeekTimeVO();
    		query.setJqNo(model.getJqNo());
    		query.setJqWeek(model.getJqWeek());
    		Integer count = jlJqWeekTimeSQL.count(query);
    		if(count>0){
    			this.error(error_103, "星期已存在，请选择别的星期");
    			return this.toResult();
    		}
    	}
    	List<SysQqServerVO> list = sysQqServerSQL.findList(new SysQqServerVO());
    	if(list.size()>0){
    		model.setJy(list.get(0).getServerName());
    	}else{
    		model.setJy("Server1");
    	}
        jlJqWeekTimeSQL.add(model);
        return this.toResult();
    }

    @RequestMapping("/edit")
    public String edit(JlJqWeekTimeVO model){
    	if(model.getTimeIndex()!=null){
    		JlJqWeekTimeVO old = jlJqWeekTimeSQL.findOne(model.getTimeIndex());
    		if(old.getJqWeek().intValue() != model.getJqWeek().intValue()){
    			JlJqWeekTimeVO query = new JlJqWeekTimeVO();
        		query.setJqNo(model.getJqNo());
        		query.setJqWeek(model.getJqWeek());
        		Integer count = jlJqWeekTimeSQL.count(query);
        		if(count>0){
        			this.error(error_103, "星期已存在，请选择别的星期");
        			return this.toResult();
        		}
    		}
    	}
        jlJqWeekTimeSQL.edit(model);
        return this.toResult();
    }

    @RequestMapping("/delete")
    public String del(Integer id){
        jlJqWeekTimeSQL.deleteKey(id);
        return this.toResult();
    }

}
