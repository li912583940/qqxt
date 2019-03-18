package com.sl.ue.web.other;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sl.ue.entity.jl.vo.JlFrVO;
import com.sl.ue.entity.jl.vo.JlQqRecVO;
import com.sl.ue.service.jl.JlQqRecService;
import com.sl.ue.util.http.Result;

@RestController
@RequestMapping("/telCost")
public class TelCostWeb extends Result{

	@Autowired
    private JlQqRecService jlQqRecSQL;
	
	@RequestMapping("/findPojo")
	public String findPojo(JlQqRecVO model,Integer pageSize, Integer pageNum){
		return jlQqRecSQL.findPojoByTelCost(model, pageSize, pageNum);
	}
	
	@RequestMapping("/findDetailsPojo")
	public String findDetailsPojo(String callTimeStart, String callTimeEnd, String frNo, Integer pageSize, Integer pageNum){
		return jlQqRecSQL.findDetailsPojo(callTimeStart, callTimeEnd, frNo, pageSize, pageNum);
	}
	
	/**
     * 说明 [导出话费帐单excel]
     * L_晓天  @2018年11月30日
     */
    @RequestMapping("/exportCostExcel")
    public void exportCostExcel(JlQqRecVO model,
    		HttpServletRequest request, HttpServletResponse response) {
    	jlQqRecSQL.exportCostExcel(model, request, response);
    }
    
    /**
     * 说明 [犯人详细账单excel]
     * L_晓天  @2018年11月30日
     */
    @RequestMapping("/exportFrCostExcel")
    public void exportFrCostExcel(String callTimeStart, String callTimeEnd, String frNo,
    		HttpServletRequest request, HttpServletResponse response) {
    	jlQqRecSQL.exportFrCostExcel(callTimeStart, callTimeEnd, frNo, request, response);
    }
}
