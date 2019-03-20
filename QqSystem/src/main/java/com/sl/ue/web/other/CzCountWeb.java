package com.sl.ue.web.other;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sl.ue.entity.jl.vo.JlQqCzVO;
import com.sl.ue.service.jl.JlQqCzService;
import com.sl.ue.util.http.Result;

public class CzCountWeb extends Result{

	@Autowired
	private JlQqCzService jlQqCzSQL;
	
	@RequestMapping("/findPojo")
    public String findPojo(JlQqCzVO model, Integer pageSize, Integer pageNum){
		return jlQqCzSQL.findPojoByCzCount(model, pageSize, pageNum);
    }
	
	
}
