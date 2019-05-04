package com.sl.ue.service.jl;

import java.util.Map;

import com.sl.ue.entity.jl.vo.JlJbVO;
import com.sl.ue.service.base.BaseService;

public interface JlJbService extends BaseService<JlJbVO>{

	public Map<String, Object> findPojoJoin(JlJbVO model, Integer pageSize, Integer pageNum);
	
	/**
	 * 说明 [复位]
	 * @return
	 * L_晓天  @2019年3月22日
	 */
	public String resetQqCount();
	
	/**
	 * 说明 [强制复位]
	 * @return
	 * L_晓天  @2019年3月22日
	 */
	public String qzResetQqCount();
	
	/**
	 * 说明 [按监区批量设置电话次数]
	 * @param count
	 * @param jqs
	 * @return
	 * L_晓天  @2019年5月3日
	 */
	public String addCountByJb(Integer count, String jbs);
}