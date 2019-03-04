package com.sl.ue.service.sys.sqlImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sl.ue.entity.jl.vo.JlQqMonVO;
import com.sl.ue.entity.sys.vo.SysQqLineVO;
import com.sl.ue.entity.sys.vo.SysUserVO;
import com.sl.ue.service.base.impl.BaseSqlImpl;
import com.sl.ue.service.jl.JlQqMonService;
import com.sl.ue.service.sys.SysQqLineService;
import com.sl.ue.util.http.Result;
import com.sl.ue.util.http.token.TokenUser;

@Service("sysQqLineSQL")
public class SysQqLineServiceImpl extends BaseSqlImpl<SysQqLineVO> implements SysQqLineService{
	
	private JlQqMonService jlQqMonSQL;
	
	@Override
	public Map<String, Object> findPojoMonitor(Integer pageSize, Integer pageNum) {
		
		StringBuffer leftJoinField = new StringBuffer();
		leftJoinField.append(",b.IP AS ip");
		leftJoinField.append(",b.Port AS port");
		leftJoinField.append(",b.AudioPort AS audioPort");
		leftJoinField.append(",c.Write_Txt");
		leftJoinField.append(",d.JQ_Name");
		
		StringBuffer leftJoinTable = new StringBuffer();
		leftJoinTable.append(" LEFT JOIN SYS_QQ_SERVER AS b ON a.JY=b.Server_Name");
		leftJoinTable.append(" LEFT JOIN JL_QQ_MON AS c ON a.Monitor_CallID=c.Call_ID");
		leftJoinTable.append(" LEFT JOIN JL_JQ as d ON a.JQ=d.JQ_No");
		
		StringBuffer leftJoinWhere = new StringBuffer();
		leftJoinWhere.append("");
		
		SysQqLineVO sysHjLine = new SysQqLineVO();
		sysHjLine.setLeftJoinField(leftJoinField.toString());
		sysHjLine.setLeftJoinTable(leftJoinTable.toString());
		Map<String, Object> map = this.findPojo(sysHjLine, pageSize, pageNum, "ASC");
		return map;
	}

	public String updateYJ(Integer webId, Integer state){
		Result result = new Result();
		if(webId == null){
			result.error(Result.error_102, "webId为NULL");
			return result.toResult();
		}
		SysQqLineVO sysQqLine = this.findOne(webId);
		
		if(sysQqLine == null){
			result.error(Result.error_103, "查询不到此记录");
			return result.toResult();
		}
		if(state==1){
			SysUserVO user = TokenUser.getUser();
			sysQqLine.setMonitorYj(user.getUserName());
		}else{
			sysQqLine.setMonitorYj("");
		}
		this.edit(sysQqLine);
		return result.toResult();
	}
	
	public String getZs(String monitorCallid){
		Result result = new Result();
		if(StringUtils.isBlank(monitorCallid)){
			return result.toResult();
		}
		SysUserVO sysUser = TokenUser.getUser();
		JlQqMonVO jlQqMon = new JlQqMonVO();
		jlQqMon.setCallId(monitorCallid);
		jlQqMon.setUserNo(sysUser.getUserNo());
		List<JlQqMonVO> jlQqMonList = jlQqMonSQL.findList(jlQqMon);
		if(jlQqMonList.size()>0){
			jlQqMon = jlQqMonList.get(0);
		}
		result.putJson(jlQqMon);
		return result.toResult();
	}
	
	public String addMonitorFlag(String monitorCallid, String writeTxt){
		Result result = new Result();
		if(StringUtils.isBlank(monitorCallid)){
			result.error(Result.error_102);
			return result.toResult();
		}
		SysUserVO sysUser = TokenUser.getUser();
		JlQqMonVO jlQqMon = new JlQqMonVO();
		jlQqMon.setCallId(monitorCallid);
		jlQqMon.setUserNo(sysUser.getUserNo());
		List<JlQqMonVO> jlQqMonList = jlQqMonSQL.findList(jlQqMon);
		if(jlQqMonList.size()>0){
			jlQqMon = jlQqMonList.get(0);
			jlQqMon.setWriteTxt(writeTxt);
			jlQqMonSQL.edit(jlQqMon);
		}else{
			jlQqMon.setWriteTxt(writeTxt);
			jlQqMonSQL.add(jlQqMon);
		}
		return result.toResult();
	}
	

}
