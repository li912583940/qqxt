package com.sl.ue.web.jl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sl.ue.entity.jl.vo.JlQqTpdhVO;
import com.sl.ue.service.jl.JlQqTpdhService;
import com.sl.ue.util.http.Result;

@RestController
@RequestMapping("/jlQqTpdh")
public class JlQqTpdhWeb extends Result{

    @Autowired
    private JlQqTpdhService jlQqTpdhSQL;

    @RequestMapping("/findList")
    public String findList(JlQqTpdhVO model,Integer pageSize, Integer pageNum){
        List<JlQqTpdhVO> list = jlQqTpdhSQL.findList(model, pageSize, pageNum, "ASC");
        this.putData(list);
        return this.toResult();
    }

    @RequestMapping("/findPojo")
    public String findPojo(JlQqTpdhVO model, Integer pageSize, Integer pageNum){
        Map<String, Object> map = jlQqTpdhSQL.findPojo(model, pageSize, pageNum);
        this.putPojo(map);
        return this.toResult();
    }

    @RequestMapping("/findCount")
    public String findCount(JlQqTpdhVO model){
        Integer count = jlQqTpdhSQL.count(model);
        this.putJson("count", count);
        return this.toResult();
    }

    @RequestMapping("/findOne")
    public String findOne(Integer id){
        JlQqTpdhVO model = jlQqTpdhSQL.findOne(id);
        this.putJson(model);
        return this.toResult();
    }

    @RequestMapping("/add")
    public String add(JlQqTpdhVO model){
        jlQqTpdhSQL.add(model);
        return this.toResult();
    }

    @RequestMapping("/edit")
    public String edit(JlQqTpdhVO model){
        jlQqTpdhSQL.edit(model);
        return this.toResult();
    }

    @RequestMapping("/delete")
    public String del(Integer id){
        jlQqTpdhSQL.deleteKey(id);
        return this.toResult();
    }

}
