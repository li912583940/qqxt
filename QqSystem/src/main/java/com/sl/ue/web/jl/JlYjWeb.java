package com.sl.ue.web.jl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sl.ue.entity.jl.vo.JlYjVO;
import com.sl.ue.entity.sys.vo.SysLogVO;
import com.sl.ue.entity.sys.vo.SysUserVO;
import com.sl.ue.service.jl.JlYjService;
import com.sl.ue.service.sys.SysLogService;
import com.sl.ue.util.DateUtil;
import com.sl.ue.util.http.Result;
import com.sl.ue.util.http.token.TokenUser;

/**
 * 说明 [警察]
 * L_晓天  @2018年10月7日
 */
@RestController
@RequestMapping("/jlYj")
public class JlYjWeb extends Result{

    @Autowired
    private JlYjService jlYjSQL;
    @Autowired
	private SysLogService sysLogSQL;
    
    @RequestMapping("/findList")
    public String findList(JlYjVO model,Integer pageSize, Integer pageNum){
        List<JlYjVO> list = jlYjSQL.findList(model, pageSize, pageNum);
        this.putData(list);
        return this.toResult();
    }

    @RequestMapping("/findPojo")
    public String findPojo(JlYjVO model, Integer pageSize, Integer pageNum){
        return jlYjSQL.findPojoJoin(model, pageSize, pageNum);
    }

    @RequestMapping("/findCount")
    public String findCount(JlYjVO model){
        Integer count = jlYjSQL.count(model);
        this.putJson("count", count);
        return this.toResult();
    }

    @RequestMapping("/findOne")
    public String findOne(Integer id){
        JlYjVO model = jlYjSQL.findOne(id);
        this.putJson(model);
        return this.toResult();
    }

    @RequestMapping("/add")
    public String add(JlYjVO model, HttpServletRequest request){
    	JlYjVO jlYjQuery = new JlYjVO();
    	jlYjQuery.setYjNo(model.getYjNo());
    	int count = jlYjSQL.count(jlYjQuery);
    	if(count>0){
    		this.error(error_103, "警察编号已存在");
    		return this.toResult();
    	}
    	if(StringUtils.isNotBlank(model.getTeleuser())){
    		JlYjVO jlYjTeleuser = new JlYjVO();
        	jlYjTeleuser.setTeleuser(model.getTeleuser());
    		int countTeleuser = jlYjSQL.count(jlYjTeleuser);
    		if(countTeleuser>0){
    			this.error(error_103, "授权账号已存在");
        		return this.toResult();
    		}
    	}
    	model.setJy("Server1");
    	
    	SysUserVO user = TokenUser.getUser();
    	SysLogVO sysLog = new SysLogVO();
    	sysLog.setType("正常");
		sysLog.setOp("添加警察信息");
		sysLog.setInfo("新增警察编号: "+model.getYjNo()+"，警察警号:"+model.getYjNum()+"，警察姓名: "+model.getYjName()+"。");
		sysLog.setModel("警察信息");
		sysLog.setUserNo(user.getUserNo());
		sysLog.setUserName(user.getUserName());
		sysLog.setLogTime(DateUtil.getDefaultNow());
		sysLog.setUserIp(request.getRemoteAddr());
		sysLogSQL.add(sysLog);
		
        jlYjSQL.add(model);
        return this.toResult();
    }

    @RequestMapping("/edit")
    public String edit(JlYjVO model, HttpServletRequest request){
    	if(model.getWebid()!=null){
    		JlYjVO oldModel = jlYjSQL.findOne(model.getWebid());
    		if(oldModel.getYjNum()!=null && !oldModel.getYjNum().equals(model.getYjNum())){
    			JlYjVO jlYjQuery = new JlYjVO();
    			jlYjQuery.setYjNum(model.getYjNum());
    			int count = jlYjSQL.count(jlYjQuery);
    			if(count>0){
    				this.error(error_103, "警察警号已存在");
    	    		return this.toResult();
    			}
    		}
    		if(oldModel.getTeleuser()!=null && !oldModel.getTeleuser().equals(model.getTeleuser())){
    			JlYjVO jlYjQuery = new JlYjVO();
    			jlYjQuery.setTeleuser(model.getTeleuser());
    			int count = jlYjSQL.count(jlYjQuery);
    			if(count>0){
    				this.error(error_103, "授权账号已存在");
    	    		return this.toResult();
    			}
    		}
    	}
    	SysUserVO user = TokenUser.getUser();
    	SysLogVO sysLog = new SysLogVO();
    	sysLog.setType("正常");
		sysLog.setOp("编辑警察信息");
		sysLog.setInfo("编辑警察编号: "+model.getYjNo()+"，警察警号:"+model.getYjNum()+"，警察姓名: "+model.getYjName()+"。");
		sysLog.setModel("警察信息");
		sysLog.setUserNo(user.getUserNo());
		sysLog.setUserName(user.getUserName());
		sysLog.setLogTime(DateUtil.getDefaultNow());
		sysLog.setUserIp(request.getRemoteAddr());
		sysLogSQL.add(sysLog);
		
        jlYjSQL.edit(model);
        return this.toResult();
    }

    @RequestMapping("/delete")
    public String del(Integer id, HttpServletRequest request){
    	JlYjVO model = jlYjSQL.findOne(id);
    	SysUserVO user = TokenUser.getUser();
    	SysLogVO sysLog = new SysLogVO();
    	sysLog.setType("严重");
		sysLog.setOp("删除警察信息");
		sysLog.setInfo("删除警察编号: "+model.getYjNo()+"，警察警号:"+model.getYjNum()+"，警察姓名: "+model.getYjName()+"。");
		sysLog.setModel("警察信息");
		sysLog.setUserNo(user.getUserNo());
		sysLog.setUserName(user.getUserName());
		sysLog.setLogTime(DateUtil.getDefaultNow());
		sysLog.setUserIp(request.getRemoteAddr());
		sysLogSQL.add(sysLog);
		
		
        jlYjSQL.deleteKey(id);
        return this.toResult();
    }

}
