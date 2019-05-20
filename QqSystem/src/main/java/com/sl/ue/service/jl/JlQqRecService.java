package com.sl.ue.service.jl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sl.ue.entity.jl.vo.JlQqRecVO;
import com.sl.ue.service.base.BaseService;

public interface JlQqRecService extends BaseService<JlQqRecVO>{

	public Map<String, Object> findPojoLeft(JlQqRecVO model, Integer pageSize, Integer pageNum);
	
	/**
	 * 说明 [话务统计]
	 * @param model
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * L_晓天  @2019年3月17日
	 */
	public String findPojoByTelCost(JlQqRecVO model, Integer pageSize, Integer pageNum);
	
	/**
	 * 说明 [话费明细]
	 * @param frNo
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * L_晓天  @2019年3月17日
	 */
	public String findDetailsPojo(String callTimeStart, String callTimeEnd, String frNo, Integer pageSize, Integer pageNum);
	
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
	
	/**
	 * 说明 [导出话费帐单excel]
	 * L_晓天  @2018年11月30日
	 */
	public void exportCostExcel(JlQqRecVO model, HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 说明 [犯人详细账单excel]
	 * L_晓天  @2018年11月30日
	 */
	public void exportFrCostExcel(String callTimeStart, String callTimeEnd, String frNo, HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 说明 [亲情提醒]
	 * @param callTimeStart
	 * @param callTimeEnd
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * L_晓天  @2019年3月19日
	 */
	public Map<String, Object> findPojoRemind(String callTimeStart, String callTimeEnd, Integer pageSize, Integer pageNum);
	
	/**
	 * 说明 [亲情提醒 导出]
	 * @param callTimeStart
	 * @param callTimeEnd
	 * @param request
	 * @param response
	 * L_晓天  @2019年3月19日
	 */
	public void exportExcelByRemind(String callTimeStart, String callTimeEnd, HttpServletRequest request, HttpServletResponse response);

	/**
	 * 说明 []
	 * @param id
	 * @return
	 * L_晓天  @2019年5月2日
	 */
	public String getFileUrl(Long id);
	
	public void downAudio(Long webid, HttpServletRequest request, HttpServletResponse response);
}