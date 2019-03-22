package com.sl.ue.service.jl;

import java.util.Map;

import com.sl.ue.entity.jl.vo.JlJqVO;
import com.sl.ue.service.base.BaseService;

public interface JlJqService extends BaseService<JlJqVO>{

	public Map<String, Object> findPojoJoin(JlJqVO model, Integer pageSize, Integer pageNum);
	

}