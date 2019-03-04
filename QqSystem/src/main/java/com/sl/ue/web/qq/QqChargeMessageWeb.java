package com.sl.ue.web.qq;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sl.ue.entity.jl.vo.JlFrVO;
import com.sl.ue.service.jl.JlFrService;
import com.sl.ue.util.http.Result;

@RestController
@RequestMapping("/qqChargeMessage")
public class QqChargeMessageWeb extends Result{

	@Autowired
	private JlFrService jlFrSQL;
	
	@RequestMapping("/findPojo")
	public String findPojo(JlFrVO model, Integer pageSize, Integer pageNum){
		Map<String, Object> map = jlFrSQL.findPojoCharge(model, pageSize, pageNum);
		this.putPojo(map);
		return this.toResult();
	}
	
	@RequestMapping("/requestRecharge")
	public String requestRecharge(Integer webId, Integer czje){
		
		return this.toResult();
	}
}
