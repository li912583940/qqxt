package com.sl.ue.service.jl.sqlImpl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.stereotype.Service;

import com.sl.ue.entity.jl.vo.JlJbVO;
import com.sl.ue.entity.sys.vo.SysLogVO;
import com.sl.ue.entity.sys.vo.SysQqServerVO;
import com.sl.ue.entity.sys.vo.SysUserVO;
import com.sl.ue.service.base.impl.BaseSqlImpl;
import com.sl.ue.service.jl.JlJbService;
import com.sl.ue.service.sys.SysLogService;
import com.sl.ue.service.sys.SysQqServerService;
import com.sl.ue.util.DateUtil;
import com.sl.ue.util.http.Result;
import com.sl.ue.util.http.token.TokenUser;

@Service("jlJbSQL")
public class JlJbServiceImpl extends BaseSqlImpl<JlJbVO> implements JlJbService{

	@Autowired
	private SysLogService sysLogSQL;
	@Autowired
	private SysQqServerService sysQqServerSQL;
	@Override
	public Map<String, Object> findPojoJoin(JlJbVO model, Integer pageSize, Integer pageNum) {
		StringBuffer where = new StringBuffer();
		if(StringUtils.isNotBlank(model.getJbName())){
			String str = model.getJbName();
			where.append(" AND a.JB_Name LIKE '%"+str+"%' ");
			model.setJbName(null);
		}
		model.setLeftJoinWhere(where.toString());
		Map<String, Object> map = this.findPojo(model, pageSize, pageNum);
		return map;
	}

	public String resetQqCount(){
		Result result = new Result();
		List<SysQqServerVO> qqServerList = sysQqServerSQL.findList(new SysQqServerVO());
		Integer resu= 0;
		for(SysQqServerVO t : qqServerList ){
			resu = (Integer) jdbcTemplate.execute(  // 调用存储过程 获取会见批次号
				     new CallableStatementCreator() {
						@Override
						public CallableStatement createCallableStatement(Connection con) throws SQLException {
							 String storedProc = "{call reset_qq_count_by_server(?,?)}";// 调用的sql   
					           CallableStatement cs = con.prepareCall(storedProc); 
					           cs.setString(1, t.getServerName());
					           cs.registerOutParameter(2, java.sql.Types.INTEGER);// 注册输出参数的类型   
					           return cs;   
						}
					}, 
				    new CallableStatementCallback<Integer>() {  
						@Override
				        public Integer doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {   
				           cs.execute();   
				           return cs.getInt(2);// 获取输出参数的值   
				        }   
			});
		}
		result.putJson(resu);
		SysUserVO sysUser = TokenUser.getUser();
		SysLogVO sysLog = new SysLogVO();
		sysLog.setType("正常");
		sysLog.setLogTime(DateUtil.getDefaultNow());
		sysLog.setUserNo(sysUser.getUserNo());
		sysLog.setUserName(sysUser.getUserName());
		sysLog.setModel("级别管理");
		sysLog.setInfo("复位亲情电话次数-"+(resu!=0?"成功":"失败"));
		sysLog.setOp("复位亲情电话次数");
		sysLogSQL.add(sysLog);
		return result.toResult();
	}
	
	
	public String qzResetQqCount(){
		Result result = new Result();
		Integer resu = (Integer) jdbcTemplate.execute(  // 调用存储过程 获取会见批次号
			     new CallableStatementCreator() {
					@Override
					public CallableStatement createCallableStatement(Connection con) throws SQLException {
						 String storedProc = "{call qzreset_qq_count(?)}";// 调用的sql   
				           CallableStatement cs = con.prepareCall(storedProc); 
				           cs.registerOutParameter(1, java.sql.Types.INTEGER);// 注册输出参数的类型   
				           return cs;   
					}
				}, 
			    new CallableStatementCallback<Integer>() {  
					@Override
			        public Integer doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {   
			           cs.execute();   
			           return cs.getInt(1);// 获取输出参数的值   
			        }   
		});
		SysUserVO sysUser = TokenUser.getUser();
		SysLogVO sysLog = new SysLogVO();
		sysLog.setType("正常");
		sysLog.setLogTime(DateUtil.getDefaultNow());
		sysLog.setUserNo(sysUser.getUserNo());
		sysLog.setUserName(sysUser.getUserName());
		sysLog.setModel("级别管理");
		sysLog.setInfo("强制复位亲情电话次数");
		sysLog.setOp("强制复位亲情电话次数");
		sysLogSQL.add(sysLog);
		return result.toResult();
	}
	
	public String addCountByJb(Integer count, String jbs){
		Result result = new Result();
		if(count<-1){
			result.error(Result.error_102, "电话次数只能设置大于-1的值");
			return result.toResult();
		}
		if(StringUtils.isBlank(jbs)){
			result.error(Result.error_102, "请至少选择一个级别");
			return result.toResult();
		}
		String sql = "update JL_JB set QQ_Count="+count+" where webid in ("+jbs+")";
		this.jdbcTemplate.execute(sql);
		return result.toResult();
	}
}
