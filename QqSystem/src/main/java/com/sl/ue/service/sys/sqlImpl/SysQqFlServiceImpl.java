package com.sl.ue.service.sys.sqlImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sl.ue.entity.sys.vo.SysParamVO;
import com.sl.ue.entity.sys.vo.SysQqFlVO;
import com.sl.ue.service.base.impl.BaseSqlImpl;
import com.sl.ue.service.sys.SysParamService;
import com.sl.ue.service.sys.SysQqFlService;
import com.sl.ue.util.http.Result;

@Service("sysQqFlSQL")
public class SysQqFlServiceImpl extends BaseSqlImpl<SysQqFlVO> implements SysQqFlService{

	@Autowired
	private SysParamService sysParamSQL;
	
	@Override
	public String findSysParam() {
		Result result = new Result();
		SysParamVO sysParam = new SysParamVO();
		sysParam.setParamName("System_Set");
		List<SysParamVO> list = sysParamSQL.findList(sysParam);
		if(list.size()>0){
			result.putJson(list.get(0));
		}
		return result.toResult();
	}

}
