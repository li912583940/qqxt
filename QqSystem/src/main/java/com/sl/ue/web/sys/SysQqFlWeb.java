package com.sl.ue.web.sys;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sl.ue.entity.sys.vo.SysQqFlVO;
import com.sl.ue.service.sys.SysQqFlService;
import com.sl.ue.util.http.Result;

@RestController
@RequestMapping("/sysQqFl")
public class SysQqFlWeb extends Result{

    @Autowired
    private SysQqFlService sysQqFlSQL;

    @RequestMapping("/findList")
    public String findList(SysQqFlVO model,Integer pageSize, Integer pageNum){
        List<SysQqFlVO> list = sysQqFlSQL.findList(model, pageSize, pageNum, "ASC");
        this.putData(list);
        return this.toResult();
    }

    @RequestMapping("/findPojo")
    public String findPojo(SysQqFlVO model, Integer pageSize, Integer pageNum){
        Map<String, Object> map = sysQqFlSQL.findPojo(model, pageSize, pageNum);
        this.putPojo(map);
        return this.toResult();
    }

    @RequestMapping("/findCount")
    public String findCount(SysQqFlVO model){
        Integer count = sysQqFlSQL.count(model);
        this.putJson("count", count);
        return this.toResult();
    }

    @RequestMapping("/findOne")
    public String findOne(Integer id){
        SysQqFlVO model = sysQqFlSQL.findOne(id);
        this.putJson(model);
        return this.toResult();
    }

    @RequestMapping("/add")
    public String add(SysQqFlVO model){
        sysQqFlSQL.add(model);
        return this.toResult();
    }

    @RequestMapping("/edit")
    public String edit(SysQqFlVO model){
        sysQqFlSQL.edit(model);
        return this.toResult();
    }

    @RequestMapping("/delete")
    public String del(Integer id){
        sysQqFlSQL.deleteKey(id);
        return this.toResult();
    }

    @RequestMapping("/findSysParam")
    public String findSysParam(){
        return sysQqFlSQL.findSysParam();
    }
}
