package com.sl.ue.service.jl.sqlImpl;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.sl.ue.entity.jl.vo.JlFrVO;
import com.sl.ue.entity.jl.vo.JlQqCzVO;
import com.sl.ue.entity.jl.vo.JlQqRecVO;
import com.sl.ue.service.base.impl.BaseSqlImpl;
import com.sl.ue.service.jl.JlFrService;
import com.sl.ue.service.jl.JlQqCzService;
import com.sl.ue.util.Constants;
import com.sl.ue.util.DateUtil;
import com.sl.ue.util.http.Result;
import com.sl.ue.util.http.WebContextUtil;
import com.sl.ue.util.http.token.JqRoleManager;

@Service("jlQqCzSQL")
public class JlQqCzServiceImpl extends BaseSqlImpl<JlQqCzVO> implements JlQqCzService{

	@Autowired
	private JlFrService jlFrSQL;
	
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
		
		model.setLeftJoinWhere(leftJoinWhere.toString());
		
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
		String countInSql = "SELECT ISNULL(sum(a.CZJE), 0) AS sumje FROM JL_QQ_CZ a WHERE 1=1"+countInWhere;
		List<Map<String, Object>> countInList = this.jdbcTemplate.queryForList(countInSql);
		Integer countIn = 0;
		if(countInList.size()>0){
			countIn = (Integer) countInList.get(0).get("sumje");
			countMap.put("countIn", countIn);
		}
		
		// 退费总额
		String thcountOutZeWhere = newWhere.toString();
		thcountOutZeWhere+=" AND a.CZJE<0";
		String thcountOutZeSql = "SELECT ISNULL(sum(a.CZJE), 0) AS sumje FROM JL_QQ_CZ a WHERE 1=1"+thcountOutZeWhere;
		List<Map<String, Object>> thcountOutZeList = this.jdbcTemplate.queryForList(thcountOutZeSql);
		Integer thcountOutZe = 0;
		if(thcountOutZeList.size()>0){
			thcountOutZe = (Integer) thcountOutZeList.get(0).get("sumje");
			countMap.put("thcountOutZe", thcountOutZe);
		}
		
		//实际充值金额
		Integer sjcountInZe = countIn + thcountOutZe;
		countMap.put("sjcountInZe", sjcountInZe);
		
		result.putJson("czCountSum", countMap);
		return result.toResult();
	}

	@Override
	public void exportExcel(JlQqCzVO model, HttpServletRequest request, HttpServletResponse response) {
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
		
		model.setLeftJoinWhere(leftJoinWhere.toString());
		
		List<JlQqCzVO> list= this.findList(model);
		
		String fileName =  "充值统计.xls";
		
		OutputStream out = null;
		
		try {
			
			// EXCEL START
			HSSFWorkbook book = new HSSFWorkbook();
			HSSFSheet sheet = book.createSheet("充值统计");
			CellStyle cellStyle = book.createCellStyle();
			cellStyle.setDataFormat(book.createDataFormat().getFormat("yyyy-MM-dd"));
			// 设置标题
			List<String> title = new ArrayList<String>();
			title.add("监区编号");
			title.add("监区名称");
			title.add("犯人编号");
			title.add("犯人姓名");
			title.add("充值金额");
			title.add("充值时间");
			title.add("充值人编号");
			title.add("充值人名称");
			title.add("退还时间");
			title.add("退还人编号");
			title.add("退还人名称");
			// 标题 start
			HSSFRow row1 = sheet.createRow(0);
			for(int i=0; i<title.size(); i++){
				String t = title.get(i);
				HSSFCell cell = row1.createCell(i);
				cell.setCellValue(t);
			}
			// 标题 end
			
			// 记录 start
			for(int i=0; i<list.size(); i++){
				JlQqCzVO jlQqCz = list.get(i);
				HSSFRow row2 = sheet.createRow(i+1);
				
				HSSFCell cell0 = row2.createCell(0);
				cell0.setCellValue(jlQqCz.getJqNo());
					
				HSSFCell cell1 = row2.createCell(1);
				cell1.setCellValue(jlQqCz.getJqName());
				
				HSSFCell cell2 = row2.createCell(2);
				cell2.setCellValue(jlQqCz.getFrNo());
				
				HSSFCell cell3 = row2.createCell(3);
				cell3.setCellValue(jlQqCz.getFrName());
				
				HSSFCell cell4 = row2.createCell(4);
				if(jlQqCz.getCzje() != null && jlQqCz.getCzje() != 0){
					Double czje = jlQqCz.getCzje().doubleValue();
					czje = czje/1000;
					cell4.setCellValue(czje+"");
				}else{
					cell4.setCellValue("0.0");
				}
				
				HSSFCell cell5 = row2.createCell(5);
				cell5.setCellValue(DateUtil.getDefault(jlQqCz.getCzsj()));
				
				HSSFCell cell6 = row2.createCell(6);
				cell6.setCellValue(jlQqCz.getCzrNo());
				
				HSSFCell cell7 = row2.createCell(7);
				cell7.setCellValue(jlQqCz.getCzrName());
				
				HSSFCell cell8 = row2.createCell(8);
				cell8.setCellValue(jlQqCz.getScsj()!=null?DateUtil.getDefault(jlQqCz.getScsj()):"");
				
				HSSFCell cell9 = row2.createCell(9);
				cell9.setCellValue(jlQqCz.getScrNo()!=null?jlQqCz.getScrNo():"");
				
				HSSFCell cell10 = row2.createCell(10);
				cell10.setCellValue(jlQqCz.getScrName()!=null?jlQqCz.getScrName():"");
				
			}
			
			// excel 第二页
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
			String countInSql = "SELECT ISNULL(sum(a.CZJE), 0) AS sumje FROM JL_QQ_CZ a WHERE 1=1"+countInWhere;
			List<Map<String, Object>> countInList = this.jdbcTemplate.queryForList(countInSql);
			Integer countIn = 0;
			if(countInList.size()>0){
				countIn = (Integer) countInList.get(0).get("sumje");
			}
			
			// 退费总额
			String thcountOutZeWhere = newWhere.toString();
			thcountOutZeWhere+=" AND a.CZJE<0";
			String thcountOutZeSql = "SELECT ISNULL(sum(a.CZJE), 0) AS sumje FROM JL_QQ_CZ a WHERE 1=1"+thcountOutZeWhere;
			List<Map<String, Object>> thcountOutZeList = this.jdbcTemplate.queryForList(thcountOutZeSql);
			Integer thcountOutZe = 0;
			if(thcountOutZeList.size()>0){
				thcountOutZe = (Integer) thcountOutZeList.get(0).get("sumje");
			}
			
			//实际充值金额
			Integer sjcountInZe = countIn + thcountOutZe;
			
			HSSFSheet sheet1 =book.createSheet("充值总计");
			CellStyle cellStyle1 = book.createCellStyle();
			cellStyle1.setDataFormat(book.createDataFormat().getFormat("yyyy-MM-dd"));
			// 设置标题
			List<String> title1 = new ArrayList<String>();
			title1.add("充值总额");
			title1.add("退费总额");
			title1.add("实际金额");
			// 标题 start
			HSSFRow rowFr = sheet1.createRow(0);
			for(int i=0; i<title1.size(); i++){
				String t = title1.get(i);
				HSSFCell cell = rowFr.createCell(i);
				cell.setCellValue(t);
			}
			// 标题 end
			
			// 记录 start
			HSSFRow rowFr2 = sheet1.createRow(1);
			HSSFCell cell0 = rowFr2.createCell(0);
			if(countIn != 0){
				Double countInD = countIn.doubleValue();
				countInD = countInD/1000;
				cell0.setCellValue(countInD+"");
			}else{
				cell0.setCellValue("0.0");
			}
			
			
			HSSFCell cell1 = rowFr2.createCell(1);
			if(thcountOutZe != 0){
				Double thcountOutZeD = thcountOutZe.doubleValue();
				thcountOutZeD = thcountOutZeD/1000;
				cell1.setCellValue(thcountOutZeD+"");
			}else{
				cell1.setCellValue("0.0");
			}
			
			
			HSSFCell cell2 = rowFr2.createCell(2);
			if(sjcountInZe != 0){
				Double sjcountInZeD = sjcountInZe.doubleValue();
				sjcountInZeD = sjcountInZeD/1000;
				cell2.setCellValue(sjcountInZeD+"");
			}else{
				cell2.setCellValue("0.0");
			}
			
			
			// 处理不同浏览器中文名称编码
			String userAgent=request.getHeader("USER-AGENT");
			if(userAgent.indexOf("Chrome")!=-1 || userAgent.indexOf("Safari")!=-1 || userAgent.indexOf("Firefox")!=-1){
				fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			}else{
				fileName = URLEncoder.encode(fileName,"UTF8");
			}
			response.setHeader("Content-Disposition", "attachment;filename="+fileName);
			response.setHeader("Cache-Control","no-cache");//设置头
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/octet-stream");
			out = response.getOutputStream();
			book.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(out != null){
				try {
					out.close();
				} catch (IOException e2) {
				}
			}
		}
		
	}

	@Override
	public String findPrint(BigDecimal czId, String frNo) {
		Result result = new Result();
		if(StringUtils.isBlank(frNo) || czId == null){
			result.error(Result.error_102);
			return result.toResult();
		}
		JlQqCzVO jlQqCz = this.findOne(czId);
		JlFrVO jlFr = new JlFrVO();
		jlFr.setFrNo(frNo);
		List<JlFrVO> jlFrList = jlFrSQL.findList(jlFr);
		if(jlFrList.size()<=0){
			result.error(Result.error_103, "罪犯记录表不存在此罪犯");
			return result.toResult();
		}
		jlFr = jlFrList.get(0);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("frNo", jlQqCz.getFrNo());
		resultMap.put("frName", jlQqCz.getFrName());
		resultMap.put("createTime", jlFr.getCreateTime()!=null?DateUtil.getDefault(jlFr.getCreateTime()):"");
		resultMap.put("endTime", jlQqCz.getCzsj()!=null?DateUtil.getDefault(jlQqCz.getCzsj()):"");
		resultMap.put("jqName", jlQqCz.getJqName());
	}
}
