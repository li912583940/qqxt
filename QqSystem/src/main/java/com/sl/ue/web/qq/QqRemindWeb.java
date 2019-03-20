package com.sl.ue.web.qq;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sl.ue.entity.jl.vo.JlQqRecVO;
import com.sl.ue.service.jl.JlQqRecService;
import com.sl.ue.util.http.Result;

@RestController
@RequestMapping("/qqRemind")
public class QqRemindWeb extends Result {

	@Autowired
	private JlQqRecService jlQqRecSQL;
	
	@RequestMapping("/findPojo")
	public String findPojo(String callTimeStart, String callTimeEnd, Integer pageSize, Integer pageNum){
		Map<String, Object> map = jlQqRecSQL.findPojoRemind(callTimeStart, callTimeEnd, pageSize, pageNum);
		this.putPojo(map);
		return this.toResult();
	}
	
	/**
	 * 说明 [导出excel]
	 * L_晓天  @2018年11月30日
	 */
	@RequestMapping("/exportExcel")
	public void exportExcel(String callTimeStart, String callTimeEnd, HttpServletRequest request, HttpServletResponse response){
		jlQqRecSQL.exportExcelByRemind(callTimeStart, callTimeEnd, request, response);
	}
}
