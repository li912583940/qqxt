package com.sl.ue.web.jl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sl.ue.entity.jl.vo.JlQqRecVO;
import com.sl.ue.service.jl.JlQqRecService;
import com.sl.ue.util.http.Result;

@RestController
@RequestMapping("/jlQqRec")
public class JlQqRecWeb extends Result{

    @Autowired
    private JlQqRecService jlQqRecSQL;

    @RequestMapping("/findList")
    public String findList(JlQqRecVO model,Integer pageSize, Integer pageNum){
        List<JlQqRecVO> list = jlQqRecSQL.findList(model, pageSize, pageNum, "ASC");
        this.putData(list);
        return this.toResult();
    }

    @RequestMapping("/findPojo")
    public String findPojo(JlQqRecVO model, Integer pageSize, Integer pageNum){
        Map<String, Object> map = jlQqRecSQL.findPojoLeft(model, pageSize, pageNum);
        this.putPojo(map);
        return this.toResult();
    }

    @RequestMapping("/findCount")
    public String findCount(JlQqRecVO model){
        Integer count = jlQqRecSQL.count(model);
        this.putJson("count", count);
        return this.toResult();
    }

    @RequestMapping("/findOne")
    public String findOne(Integer id){
        JlQqRecVO model = jlQqRecSQL.findOne(id);
        this.putJson(model);
        return this.toResult();
    }

    @RequestMapping("/add")
    public String add(JlQqRecVO model){
        jlQqRecSQL.add(model);
        return this.toResult();
    }

    @RequestMapping("/edit")
    public String edit(JlQqRecVO model){
        jlQqRecSQL.edit(model);
        return this.toResult();
    }

    @RequestMapping("/delete")
    public String del(Integer id){
        jlQqRecSQL.deleteKey(id);
        return this.toResult();
    }

    /**
	 * 说明 [获取当前用户在此次会见记录的注释]
	 * L_晓天  @2018年11月21日
	 */
	@RequestMapping("/getZs")
	public String getZs(String callId){
		return jlQqRecSQL.getZs(callId);
	}
	
	/**
	 * 说明 [添加当前用户的在会见记录中的注释]
	 * L_晓天  @2018年11月24日
	 */
	@RequestMapping("/addRecordFlag")
	public String addRecordFlag(String callId, String writeTxt){
		return jlQqRecSQL.addRecordFlag(callId, writeTxt);
	}
	
	/**
	 * 说明 [导出excel]
	 * L_晓天  @2018年11月30日
	 */
	@RequestMapping("/exportExcel")
    public void exportExcel(JlQqRecVO model,
    		HttpServletRequest request, HttpServletResponse response) {
		jlQqRecSQL.exportExcel(model, request, response);
    }
	
	/**
	 * 说明 [获取一个星期内每天会见总数]
	 * L_晓天  @2018年12月2日
	 */
	@RequestMapping("/getWeekCount")
	public String getWeekCount(){
		return jlQqRecSQL.getWeekCount();
	}
	
	@RequestMapping("/getFileUrl")
	//@IgnoreSecurity
	public String getFileUrl(Long id){
		return jlQqRecSQL.getFileUrl(id);
	}
	
	/**
	 * 说明 [下载录音]
	 * @param request
	 * @param response
	 * L_晓天  @2019年4月9日
	 */
	@RequestMapping("/downAudio")
	public void downAudio(Long webid, HttpServletRequest request, HttpServletResponse response){
		jlQqRecSQL.downAudio(webid, request, response);
	}
}
