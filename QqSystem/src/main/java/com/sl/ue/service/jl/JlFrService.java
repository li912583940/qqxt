package com.sl.ue.service.jl;

import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sl.ue.entity.jl.vo.JlFrVO;
import com.sl.ue.service.base.BaseService;

public interface JlFrService extends BaseService<JlFrVO>{

	/**
	 * 说明 [话费充值查询]
	 * @param model
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * L_晓天  @2019年3月4日
	 */
	public Map<String, Object> findPojoCharge(JlFrVO model, Integer pageSize, Integer pageNum);
	
	/**
	 * 说明 [充值金额]
	 * @param id 犯人表主键
	 * @param czje 充值金额 元
	 * @return
	 * L_晓天  @2019年3月5日
	 */
	public String requestRecharge(Integer id, Integer czje);
	
	/**
	 * 说明 [出狱退费]
	 * @param id 罪犯表主键
	 * @return
	 * L_晓天  @2019年3月5日
	 */
	public String requestRefund(Integer id);
	
	/**
	 * 说明 [查询充值明细]
	 * @param frNo
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * L_晓天  @2019年3月6日
	 */
	public Map<String, Object> findDetailsPojo(String frNo, Integer pageSize, Integer pageNum);
	
	/**
	 * 说明 [修改充值金额]
	 * @param czId
	 * @param czje
	 * @return
	 * L_晓天  @2019年3月7日
	 */
	public String requestDetailsUpdate(BigDecimal czId, Integer czje);
	
	
	public Map<String, Object> findPojoJoin(JlFrVO model, Integer pageSize, Integer pageNum);
	
	/**
	 * 说明 [导出excel]
	 * @作者 LXT @2018年9月30日
	 */
	public void exportExcel(JlFrVO model, HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 说明 [导入excel]
	 * @param request
	 * @param response
	 * @return
	 * L_晓天  @2018年12月19日
	 */
	public String importExcel(HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 说明 [根据犯人编号查看当前罪犯是否存在]
	 * @param frNo
	 * @return
	 * L_晓天  @2019年1月20日
	 */
	public boolean frExist(String frNo);
}