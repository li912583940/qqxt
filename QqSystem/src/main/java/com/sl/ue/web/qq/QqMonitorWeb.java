package com.sl.ue.web.qq;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sl.ue.service.sys.SysQqLineService;
import com.sl.ue.util.http.Result;

@RestController
@RequestMapping("/qqMonitor")
public class QqMonitorWeb extends Result {

	@Autowired
	private SysQqLineService sysQqLineSQL;
	
	
	@RequestMapping("/findPojo")
	public String findPojo(Integer pageSize, Integer pageNum){
		Map<String, Object> map = sysQqLineSQL.findPojoMonitor(pageSize, pageNum);
		this.putPojo(map);
		return this.toResult();
	}
	
	
	/**
	 * 说明 [更新监听警察信息]
	 * @param webid
	 * @param state 1:添加警察信息， 0:清除警察信息
	 * @return
	 * L_晓天  @2018年12月28日
	 */
	@RequestMapping("/updateYJ")
	public String updateYJ(Integer webid, Integer state){
		return sysQqLineSQL.updateYJ(webid, state);
	}
	
	/**
	 * 说明 [获取当前用户在此次通话的注释]
	 * L_晓天  @2018年11月21日
	 */
	@RequestMapping("/getZs")
	public String getZs(String monitorCallid){
		return sysQqLineSQL.getZs(monitorCallid);
	}
	/**
	 * 说明 [监听注释]
	 * L_晓天  @2018年11月20日
	 */
	@RequestMapping("/addMonitorFlag")
	public String addMonitorFlag(String monitorCallid, String writeTxt){
		return sysQqLineSQL.addMonitorFlag(monitorCallid, writeTxt);
	}
	


}
