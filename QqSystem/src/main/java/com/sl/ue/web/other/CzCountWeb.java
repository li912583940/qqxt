package com.sl.ue.web.other;


import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sl.ue.entity.jl.vo.JlQqCzVO;
import com.sl.ue.service.jl.JlQqCzService;
import com.sl.ue.util.http.Result;

@RestController
@RequestMapping("/czCount")
public class CzCountWeb extends Result{

	@Autowired
	private JlQqCzService jlQqCzSQL;
	
	@RequestMapping("/findPojo")
    public String findPojo(JlQqCzVO model, Integer pageSize, Integer pageNum){
		return jlQqCzSQL.findPojoByCzCount(model, pageSize, pageNum);
    }
	
	/**
	 * 说明 [导出excel]
	 * L_晓天  @2019年3月21日
	 */
	@RequestMapping("/exportExcel")
    public void exportExcel(JlQqCzVO model,
    		HttpServletRequest request, HttpServletResponse response) {
		jlQqCzSQL.exportExcel(model, request, response);
    }
	
	@RequestMapping("/findPrint")
    public String findPrint(BigDecimal czId, String frNo) {
		return jlQqCzSQL.findPrint(czId, frNo);
    }
}
