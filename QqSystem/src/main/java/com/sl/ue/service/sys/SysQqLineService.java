package com.sl.ue.service.sys;

import java.util.Map;

import com.sl.ue.entity.sys.vo.SysQqLineVO;
import com.sl.ue.service.base.BaseService;

public interface SysQqLineService extends BaseService<SysQqLineVO>{

	public Map<String, Object> findPojoLeft(SysQqLineVO model, Integer pageSize, Integer pageNum);
	
	public Map<String, Object> findPojoMonitor(Integer pageSize, Integer pageNum);
	
	
	/**
	 * 说明 [更新监听警察信息]
	 * @param webid
	 * @return
	 * L_晓天  @2018年12月28日
	 */
	public String updateYJ(Integer webid, Integer state);
	
	/**
	 * 说明 [获取当前用户在此次通话的注释]
	 * L_晓天  @2018年11月21日
	 */
	public String getZs(String monitorCallid);
	
	/**
	 * 说明 [监听注释]
	 * L_晓天  @2018年11月21日
	 */
	public String addMonitorFlag(String monitorCallid, String writeTxt);
	
}