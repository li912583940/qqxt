package com.sl.ue.service.jl.sqlImpl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.sl.ue.entity.jl.vo.JlQqTpdhVO;
import com.sl.ue.service.base.impl.BaseSqlImpl;
import com.sl.ue.service.jl.JlQqTpdhService;

@Service("jlQqTpdhSQL")
public class JlQqTpdhServiceImpl extends BaseSqlImpl<JlQqTpdhVO> implements JlQqTpdhService{

	public Map<String, Object> findPojoJoin(JlQqTpdhVO model, Integer pageSize, Integer pageNum){
		StringBuffer leftJoinField = new StringBuffer();
		leftJoinField.append(",b.FR_Name AS frName");
		model.setLeftJoinField(leftJoinField.toString());
		
		StringBuffer leftJoinTable = new StringBuffer();
		leftJoinTable.append(" LEFT JOIN JL_FR b ON a.FR_No=b.FR_No");
		model.setLeftJoinTable(leftJoinTable.toString());
		return this.findPojo(model, pageSize, pageNum);
	}
}
