package com.sl.ue.web.sys;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sl.ue.entity.sys.vo.SysQqServerVO;
import com.sl.ue.service.sys.SysQqServerService;
import com.sl.ue.util.http.Result;

@RestController
@RequestMapping("/sysQqServer")
public class SysQqServerWeb extends Result{

    @Autowired
    private SysQqServerService sysQqServerSQL;

    @RequestMapping("/findList")
    public String findList(SysQqServerVO model,Integer pageSize, Integer pageNum){
        List<SysQqServerVO> list = sysQqServerSQL.findList(model, pageSize, pageNum, "ASC");
        this.putData(list);
        return this.toResult();
    }

    @RequestMapping("/findPojo")
    public String findPojo(SysQqServerVO model, Integer pageSize, Integer pageNum){
        Map<String, Object> map = sysQqServerSQL.findPojo(model, pageSize, pageNum);
        this.putPojo(map);
        return this.toResult();
    }

    @RequestMapping("/findCount")
    public String findCount(SysQqServerVO model){
        Integer count = sysQqServerSQL.count(model);
        this.putJson("count", count);
        return this.toResult();
    }

    @RequestMapping("/findOne")
    public String findOne(Integer id){
        SysQqServerVO model = sysQqServerSQL.findOne(id);
        this.putJson(model);
        return this.toResult();
    }

    @RequestMapping("/add")
    public String add(SysQqServerVO model){
        sysQqServerSQL.add(model);
        return this.toResult();
    }

    @RequestMapping("/edit")
    public String edit(SysQqServerVO model){
        sysQqServerSQL.edit(model);
        return this.toResult();
    }

    @RequestMapping("/delete")
    public String del(Integer id){
        sysQqServerSQL.deleteKey(id);
        return this.toResult();
    }

}
