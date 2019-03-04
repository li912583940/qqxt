package com.sl.ue.web.jl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sl.ue.entity.jl.vo.JlQqInfoVO;
import com.sl.ue.entity.sys.vo.SysUserVO;
import com.sl.ue.service.jl.JlQqInfoService;
import com.sl.ue.service.sys.SysUserService;
import com.sl.ue.util.http.Result;

@RestController
@RequestMapping("/jlQqInfo")
public class JlQqInfoWeb extends Result{

    @Autowired
    private JlQqInfoService jlQqInfoSQL;
    @Autowired
    private SysUserService sysUserSQL;
    
    @RequestMapping("/findList")
    public String findList(JlQqInfoVO model,Integer pageSize, Integer pageNum){
        List<JlQqInfoVO> list = jlQqInfoSQL.findList(model, pageSize, pageNum, "ASC");
        this.putData(list);
        return this.toResult();
    }

    @RequestMapping("/findPojo")
    public String findPojo(JlQqInfoVO model, Integer pageSize, Integer pageNum){
        Map<String, Object> map = jlQqInfoSQL.findPojo(model, pageSize, pageNum);
        this.putPojo(map);
        return this.toResult();
    }

    @RequestMapping("/findCount")
    public String findCount(JlQqInfoVO model){
        Integer count = jlQqInfoSQL.count(model);
        this.putJson("count", count);
        return this.toResult();
    }

    @RequestMapping("/findOne")
    public String findOne(Integer id){
        JlQqInfoVO model = jlQqInfoSQL.findOne(id);
        this.putJson(model);
        return this.toResult();
    }

    @RequestMapping("/add")
    public String add(JlQqInfoVO model){
        jlQqInfoSQL.add(model);
        return this.toResult();
    }

    @RequestMapping("/edit")
    public String edit(JlQqInfoVO model){
        jlQqInfoSQL.edit(model);
        return this.toResult();
    }

    @RequestMapping("/delete")
    public String del(Integer id){
        jlQqInfoSQL.deleteKey(id);
        return this.toResult();
    }

}
