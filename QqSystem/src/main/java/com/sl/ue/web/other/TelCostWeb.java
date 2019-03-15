package com.sl.ue.web.other;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
		Map<String, Object> map = jlQqRecSQL.findPojoByTelCost(model, pageSize, pageNum);
		this.putPojo(map);
		return this.toResult();
	}
}
