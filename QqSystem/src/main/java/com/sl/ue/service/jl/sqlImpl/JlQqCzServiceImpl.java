package com.sl.ue.service.jl.sqlImpl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.sl.ue.entity.jl.vo.JlQqCzVO;
import com.sl.ue.service.base.impl.BaseSqlImpl;
import com.sl.ue.service.jl.JlQqCzService;

@Service("jlQqCzSQL")
public class JlQqCzServiceImpl extends BaseSqlImpl<JlQqCzVO> implements JlQqCzService{

	public String findPojoByCzCount(JlQqCzVO model, Integer pageSize, Integer pageNum){
		StringBuffer leftJoinWhere = new StringBuffer();
		if(StringUtils.isNotBlank(model.getCallTimeStart())){
			
		}
	}
}
