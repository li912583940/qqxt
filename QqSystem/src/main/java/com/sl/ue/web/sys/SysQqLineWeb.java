package com.sl.ue.web.sys;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sl.ue.entity.sys.vo.SysQqLineVO;
import com.sl.ue.service.sys.SysQqLineService;
import com.sl.ue.util.http.Result;

@RestController
@RequestMapping("/sysQqLine")
public class SysQqLineWeb extends Result{

    @Autowired
    private SysQqLineService sysQqLineSQL;

    @RequestMapping("/findList")
    public String findList(SysQqLineVO model,Integer pageSize, Integer pageNum){
        List<SysQqLineVO> list = sysQqLineSQL.findList(model, pageSize, pageNum, "ASC");
        this.putData(list);
        return this.toResult();
    }

    @RequestMapping("/findPojo")
    public String findPojo(SysQqLineVO model, Integer pageSize, Integer pageNum){
        Map<String, Object> map = sysQqLineSQL.findPojoLeft(model, pageSize, pageNum);
        this.putPojo(map);
        return this.toResult();
    }

    @RequestMapping("/findCount")
    public String findCount(SysQqLineVO model){
        Integer count = sysQqLineSQL.count(model);
        this.putJson("count", count);
        return this.toResult();
    }

    @RequestMapping("/findOne")
    public String findOne(Integer id){
        SysQqLineVO model = sysQqLineSQL.findOne(id);
        this.putJson(model);
        return this.toResult();
    }

    @RequestMapping("/add")
    public String add(SysQqLineVO model){
        sysQqLineSQL.add(model);
        return this.toResult();
    }

    @RequestMapping("/edit")
    public String edit(SysQqLineVO model){
        sysQqLineSQL.edit(model);
        return this.toResult();
    }

    @RequestMapping("/delete")
    public String del(Integer id){
        sysQqLineSQL.deleteKey(id);
        return this.toResult();
    }

}
