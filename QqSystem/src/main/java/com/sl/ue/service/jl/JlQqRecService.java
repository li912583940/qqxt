package com.sl.ue.service.jl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sl.ue.entity.jl.vo.JlQqRecVO;
import com.sl.ue.service.base.BaseService;

public interface JlQqRecService extends BaseService<JlQqRecVO>{

	public Map<String, Object> findPojoLeft(JlQqRecVO model, Integer pageSize, Integer pageNum);
	
	public Map<String, Object> findPojoByTelCost(JlQqRecVO model, Integer pageSize, Integer pageNum);
	
	/**
	 * 说明 [获取当前用户在此次亲情电话记录的注释]
	 * L_晓天  @2018年11月24日
	 */
	public String getZs(String callId);
	
	/**
	 * 说明 [添加当前用户的在亲情电话记录中的注释]
	 * L_晓天  @2018年11月24日
	 */
	public String addRecordFlag(String callId, String writeTxt);
	
	/**
	 * 说明 [导出excel]
	 * L_晓天  @2018年11月30日
	 */
	public void exportExcel(JlQqRecVO model, HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 说明 [获取一个星期内每天亲情电话总数]
	 * L_晓天  @2018年12月2日
	 */
	public String getWeekCount();
	
}