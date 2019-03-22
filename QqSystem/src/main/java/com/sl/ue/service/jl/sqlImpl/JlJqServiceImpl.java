package com.sl.ue.service.jl.sqlImpl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.sl.ue.entity.jl.vo.JlJqVO;
import com.sl.ue.service.base.impl.BaseSqlImpl;
import com.sl.ue.service.jl.JlJqService;

@Service("jlJqSQL")
public class JlJqServiceImpl extends BaseSqlImpl<JlJqVO> implements JlJqService{

	public Map<String, Object> findPojoJoin(JlJqVO model, Integer pageSize, Integer pageNum){
		if(StringUtils.isNotBlank(model.getJqName())){
			String str = model.getJqName();
			model.setLeftJoinWhere(" AND a.JQ_Name LIKE '%"+str+"%' ");
			model.setJqName(null);
		}
		Map<String, Object> map = this.findPojo(model, pageSize, pageNum);
		return map;
	}
	

}
