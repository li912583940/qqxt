package com.sl.ue.web.jl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sl.ue.entity.jl.vo.JlQqMonVO;
import com.sl.ue.service.jl.JlQqMonService;
import com.sl.ue.util.http.Result;

@RestController
@RequestMapping("/jlQqMon")
public class JlQqMonWeb extends Result{

    @Autowired
    private JlQqMonService jlQqMonSQL;

    @RequestMapping("/findList")
    public String findList(JlQqMonVO model,Integer pageSize, Integer pageNum){
        List<JlQqMonVO> list = jlQqMonSQL.findList(model, pageSize, pageNum, "ASC");
        this.putData(list);
        return this.toResult();
    }

    @RequestMapping("/findPojo")
    public String findPojo(JlQqMonVO model, Integer pageSize, Integer pageNum){
        Map<String, Object> map = jlQqMonSQL.findPojo(model, pageSize, pageNum);
        this.putPojo(map);
        return this.toResult();
    }

    @RequestMapping("/findCount")
    public String findCount(JlQqMonVO model){
        Integer count = jlQqMonSQL.count(model);
        this.putJson("count", count);
        return this.toResult();
    }

    @RequestMapping("/findOne")
    public String findOne(Integer id){
        JlQqMonVO model = jlQqMonSQL.findOne(id);
        this.putJson(model);
        return this.toResult();
    }

    @RequestMapping("/add")
    public String add(JlQqMonVO model){
        jlQqMonSQL.add(model);
        return this.toResult();
    }

    @RequestMapping("/edit")
    public String edit(JlQqMonVO model){
        jlQqMonSQL.edit(model);
        return this.toResult();
    }

    @RequestMapping("/delete")
    public String del(Integer id){
        jlQqMonSQL.deleteKey(id);
        return this.toResult();
    }

}
