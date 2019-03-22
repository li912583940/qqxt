package com.sl.ue.web.jl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sl.ue.entity.jl.vo.JlQqCzVO;
import com.sl.ue.service.jl.JlQqCzService;
import com.sl.ue.util.http.Result;

@RestController
@RequestMapping("/jlQqCz")
public class JlQqCzWeb extends Result{

    @Autowired
    private JlQqCzService jlQqCzSQL;

    @RequestMapping("/findList")
    public String findList(JlQqCzVO model,Integer pageSize, Integer pageNum){
        List<JlQqCzVO> list = jlQqCzSQL.findList(model, pageSize, pageNum, "ASC");
        this.putData(list);
        return this.toResult();
    }

    @RequestMapping("/findPojo")
    public String findPojo(JlQqCzVO model, Integer pageSize, Integer pageNum){
        Map<String, Object> map = jlQqCzSQL.findPojo(model, pageSize, pageNum);
        this.putPojo(map);
        return this.toResult();
    }

    @RequestMapping("/findCount")
    public String findCount(JlQqCzVO model){
        Integer count = jlQqCzSQL.count(model);
        this.putJson("count", count);
        return this.toResult();
    }

    @RequestMapping("/findOne")
    public String findOne(Integer id){
        JlQqCzVO model = jlQqCzSQL.findOne(id);
        this.putJson(model);
        return this.toResult();
    }

    @RequestMapping("/add")
    public String add(JlQqCzVO model){
        jlQqCzSQL.add(model);
        return this.toResult();
    }

    @RequestMapping("/edit")
    public String edit(JlQqCzVO model){
        jlQqCzSQL.edit(model);
        return this.toResult();
    }

    @RequestMapping("/delete")
    public String del(Integer id){
        jlQqCzSQL.deleteKey(id);
        return this.toResult();
    }

}
