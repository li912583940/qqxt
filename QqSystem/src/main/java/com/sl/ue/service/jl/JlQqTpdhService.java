package com.sl.ue.service.jl;

import java.util.Map;

import com.sl.ue.entity.jl.vo.JlQqTpdhVO;
import com.sl.ue.service.base.BaseService;

public interface JlQqTpdhService extends BaseService<JlQqTpdhVO>{

	public Map<String, Object> findPojoJoin(JlQqTpdhVO model, Integer pageSize, Integer pageNum);
}