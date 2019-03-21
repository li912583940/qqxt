package com.sl.ue.service.jl;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sl.ue.entity.jl.vo.JlQqCzVO;
import com.sl.ue.service.base.BaseService;

public interface JlQqCzService extends BaseService<JlQqCzVO>{

	/**
	 * 说明 [充值统计 pojo]
	 * @param model
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * L_晓天  @2019年3月20日
	 */
	public String findPojoByCzCount(JlQqCzVO model, Integer pageSize, Integer pageNum);
	
	/**
	 * 说明 [导出excel]
	 * L_晓天  @2019年3月21日
	 */
	public void exportExcel(JlQqCzVO model, HttpServletRequest request, HttpServletResponse response);
	
	public String findPrint(BigDecimal czId, String frNo);
}