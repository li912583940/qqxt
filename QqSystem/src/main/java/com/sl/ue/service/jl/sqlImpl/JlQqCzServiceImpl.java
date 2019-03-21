package com.sl.ue.service.jl.sqlImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.sl.ue.entity.jl.vo.JlQqCzVO;
import com.sl.ue.service.base.impl.BaseSqlImpl;
import com.sl.ue.service.jl.JlQqCzService;
import com.sl.ue.util.Constants;
import com.sl.ue.util.http.Result;
import com.sl.ue.util.http.WebContextUtil;
import com.sl.ue.util.http.token.JqRoleManager;

@Service("jlQqCzSQL")
public class JlQqCzServiceImpl extends BaseSqlImpl<JlQqCzVO> implements JlQqCzService{

	public String findPojoByCzCount(JlQqCzVO model, Integer pageSize, Integer pageNum){
		Result result = new Result();
		
		StringBuffer leftJoinWhere = new StringBuffer();
		StringBuffer newWhere = new StringBuffer();
		if(StringUtils.isNotBlank(model.getCallTimeStart())){
			leftJoinWhere.append(" AND a.CZSJ>='"+model.getCallTimeStart()+"'");
			newWhere.append(" AND a.CZSJ>='"+model.getCallTimeStart()+"'");
		}
		if(StringUtils.isNotBlank(model.getCallTimeEnd())){
			leftJoinWhere.append(" AND a.CZSJ<='"+model.getCallTimeEnd()+"'");
			newWhere.append(" AND a.CZSJ<='"+model.getCallTimeEnd()+"'");
		}
		if(StringUtils.isNotBlank(model.getFrName())){
			String str = model.getFrName();
			leftJoinWhere.append(" AND (a.FR_Name LIKE '%"+str+"%' OR dbo.f_get_fryp(a.FR_Name,'"+str+"') =1 )");
			newWhere.append(" AND (a.FR_Name LIKE '%"+str+"%' OR dbo.f_get_fryp(a.FR_Name,'"+str+"') =1 )");
			model.setFrName(null);
		}
		if(model.getType() != null){
			if(model.getType() == 1){ // 1:充值
				leftJoinWhere.append(" AND a.CZJE>=0");
			}else if(model.getType() == 2){ // 2:退费
				leftJoinWhere.append(" AND a.CZJE<0");
			}
		}
		
		Map<String, Object> map = this.findPojo(model, pageSize, pageNum);
		result.putPojo(map);
		
		Map<String, Integer> countMap = new HashMap<>();
		
		if(StringUtils.isNotBlank(model.getFrNo())){
			newWhere.append(" AND a.FR_No='"+model.getFrNo()+"'");
		}
		if(StringUtils.isNotBlank(model.getJqNo())){
			newWhere.append(" AND a.JQ_No='"+model.getJqNo()+"'");
		}else{
			/** 监区权限 开始 */
			String token = WebContextUtil.getRequest().getHeader(Constants.TOKEN_NAME);
			JqRoleManager jqRoleManager = new JqRoleManager();
			String jqs = jqRoleManager.getJqs(token);
			if("admin".equals(jqs)){
				
			}else if(StringUtils.isBlank(jqs)){
				newWhere.append(" AND 1<>1 ");
			}else if(StringUtils.isNotBlank(jqs)){
				newWhere.append(" AND a.JQ_No in ("+jqs+") ");
			}
			/** 监区权限 结束 */
		}
		
		// 充值总额
		String countInWhere = newWhere.toString();
		countInWhere+=" AND a.CZJE>=0";
		String countInSql = "SELECT sum(a.CZJE) AS count FROM JL_QQ_CZ a WHERE 1=1"+countInWhere;
		List<Map<String, Object>> countInList = this.jdbcTemplate.queryForList(countInSql);
		Integer countIn = 0;
		if(countInList.size()>0){
			countIn = (Integer) countInList.get(0).get("count");
			countMap.put("countIn", countIn);
		}
		
		// 退费总额
		String thcountOutZeWhere = newWhere.toString();
		thcountOutZeWhere+=" AND a.CZJE<0";
		String thcountOutZeSql = "SELECT sum(a.CZJE) AS count FROM JL_QQ_CZ a WHERE 1=1"+thcountOutZeWhere;
		List<Map<String, Object>> thcountOutZeList = this.jdbcTemplate.queryForList(thcountOutZeSql);
		Integer thcountOutZe = 0;
		if(thcountOutZeList.size()>0){
			thcountOutZe = (Integer) thcountOutZeList.get(0).get("count");
			countMap.put("thcountOutZe", thcountOutZe);
		}
		
		//实际充值金额
		Integer sjcountInZe = countIn - thcountOutZe;
		countMap.put("sjcountInZe", sjcountInZe);
		
		result.putJson("czCountSum", countMap);
		return result.toResult();
	}
}
