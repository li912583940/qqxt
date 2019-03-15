package com.sl.ue.service.jl.sqlImpl;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import com.sl.ue.entity.jl.vo.JlQqInfoVO;
import com.sl.ue.entity.jl.vo.JlQqRecVO;
import com.sl.ue.entity.sys.vo.SysQqServerVO;
import com.sl.ue.entity.sys.vo.SysUserVO;
import com.sl.ue.service.base.impl.BaseSqlImpl;
import com.sl.ue.service.jl.JlQqInfoService;
import com.sl.ue.service.jl.JlQqRecService;
import com.sl.ue.service.sys.SysQqServerService;
import com.sl.ue.util.Config;
import com.sl.ue.util.Constants;
import com.sl.ue.util.http.Result;
import com.sl.ue.util.http.WebContextUtil;
import com.sl.ue.util.http.token.JqRoleManager;
import com.sl.ue.util.http.token.TokenUser;

@Service("jlQqRecSQL")
public class JlQqRecServiceImpl extends BaseSqlImpl<JlQqRecVO> implements JlQqRecService{

	@Autowired
	private SysQqServerService sysQqServerSQL;
	@Autowired
	private JlQqInfoService jlQqInfoSQL;
	@Override
	public Map<String, Object> findPojoLeft(JlQqRecVO model, Integer pageSize, Integer pageNum) {
		StringBuffer leftJoinWhere = new StringBuffer();
    	if(StringUtils.isNotBlank(model.getCallTimeStart())){ // 开始时间
    		leftJoinWhere.append(" AND a.Call_Time_Start>='"+ model.getCallTimeStart() + "' ");
    		model.setCallTimeStart(null);
    	}
    	if(StringUtils.isNotBlank(model.getCallTimeEnd())){ // 结束时间
    		leftJoinWhere.append(" AND a.Call_Time_Start<='"+ model.getCallTimeEnd() + "' ");
    		model.setCallTimeEnd(null);
    	}
    	if(StringUtils.isNotBlank(model.getFrName())){
    		String str = model.getFrName();
    		leftJoinWhere.append(" AND (a.FR_Name LIKE '%"+str+"%' OR dbo.f_get_fryp(a.FR_Name,'"+str+"') =1 )");
    		model.setFrName(null);
    	}
    	if(StringUtils.isNotBlank(model.getQsName())){
    		String str = model.getQsName();
    		leftJoinWhere.append(" AND (a.QS_Name LIKE '%"+str+"%' OR dbo.f_get_fryp(a.QS_Name,'"+str+"') =1 )");
    		model.setQsName(null);
    	}
    	if(model.getJfFlag()!=null){
    		if(model.getJfFlag()==0){
    			leftJoinWhere.append(" AND a.Call_Count_Flag=0");
    		}else{
    			leftJoinWhere.append(" AND a.Call_Count_Flag<>0");
    		}
    	}
  
    	model.setLeftJoinWhere(leftJoinWhere.toString());
    	Map<String, Object> map = this.findPojo(model, pageSize, pageNum);
    	List<JlQqRecVO> list = (List<JlQqRecVO>) map.get("list");
    	List<SysQqServerVO> sysQqServerList = sysQqServerSQL.findList(new SysQqServerVO());
    	
    	String callRecPath = Config.getPropertiesValue("callRecfile");
    	for(JlQqRecVO qqRec : list){
    		qqRec.setCallRecfile("");
    		
    		// 录音文件路径处理
    		for(SysQqServerVO qqServer: sysQqServerList){
    			if(qqRec.getJy().equals(qqServer.getServerName())){
    				if(StringUtils.isNotBlank(qqRec.getCallRecfile())){
    					//先查看文件是否存在， 不存在就直接提示了
    					File file = new File(qqRec.getCallRecfile());
    					if(file.exists()){
    						String end =qqRec.getCallRecfile().replace("\\", "/");
            				end = end.substring(end.indexOf("/")+1);
            				end = end.substring(end.indexOf("/"));
            				String url = qqServer.getRecurl()+callRecPath+end;
            				qqRec.setCallRecfileUrl(url);
    					}
    				}
    			}
    		}
    		
    	}
		return map;
	}
	
	public Map<String, Object> findPojoByTelCost(JlQqRecVO model, Integer pageSize, Integer pageNum){
		StringBuffer leftJoinField = new StringBuffer();
		leftJoinField.append(",a.JQ_Name AS jqName");
		leftJoinField.append(",a.FR_No AS frNo");
		leftJoinField.append(",a.FR_Name AS frName");
		leftJoinField.append(",sum(a.Call_Count_IN) as countIn");
		leftJoinField.append(",sum(a.Call_Count_OUT) as countOut");
		leftJoinField.append(",count(a.Call_Count_IN) as telCountNum");
		
		StringBuffer group = new StringBuffer();
		
		StringBuffer leftJoinWhere = new StringBuffer();
    	if(StringUtils.isNotBlank(model.getCallTimeStart())){ // 开始时间
    		leftJoinWhere.append(" AND a.Call_Time_Start>='"+ model.getCallTimeStart() + "' ");
    	}
    	if(StringUtils.isNotBlank(model.getCallTimeEnd())){ // 结束时间
    		leftJoinWhere.append(" AND a.Call_Time_Start<='"+ model.getCallTimeEnd() + "' ");
    	}
    	if(StringUtils.isNotBlank(model.getJqNo())){
    		leftJoinWhere.append(",a.JQ_No='"+model.getJqNo()+"'");
    	}
    	if(StringUtils.isNotBlank(model.getFrNo())){
    		leftJoinWhere.append(",a.FR_No='"+model.getFrNo()+"'");
    	}
    	if(StringUtils.isNotBlank(model.getFrName())){
    		String str = model.getFrName();
    		leftJoinWhere.append(" AND (a.FR_Name LIKE '%"+str+"%' OR dbo.f_get_fryp(a.FR_Name,'"+str+"') =1 )");
    	}
    	if(model.getJfFlag()!=null){
    		if(model.getJfFlag()==0){
    			leftJoinWhere.append(" AND a.Call_Count_Flag=0");
    		}else{
    			leftJoinWhere.append(" AND a.Call_Count_Flag<>0");
    		}
    	}
    	if(model.getCallCountType()!=null){
    		leftJoinWhere.append(" AND a.Call_Count_Type="+model.getCallCountType());
    	}
    	if(model.getCallCountFlag()!=null){
    		leftJoinWhere.append(" AND a.Call_Count_Flag="+model.getCallCountFlag());
    	}
    	StringBuffer sql = new StringBuffer();
    	sql.append("select ROW_NUMBER() OVER(ORDER BY a.JQ_No ASC) AS rowid");
    	sql.append(leftJoinField.toString());
    	sql.append(" FROM JL_QQ_REC AS a where 1=1");
    	sql.append(leftJoinWhere.toString());
    	sql.append(" GROUP BY a.FR_No,a.JQ_No,a.JQ_Name,a.FR_Name");
    	int startNum = (pageNum-1)*pageSize;
		int endNum = pageNum*pageSize;
    	String sqlStr = "select * from("+sql.toString()+") t where t.rowid>"+startNum+" AND t.rowid<="+endNum;
    	System.out.println(sqlStr);
    	List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sqlStr);
    	for(Map<String, Object> map: list){
    		if(StringUtils.isNotBlank(model.getCallTimeStart())){
    			map.put("callTimeStart", model.getCallTimeStart());
    		}
    		if(StringUtils.isNotBlank(model.getCallTimeEnd())){
    			map.put("callTimeEnd", model.getCallTimeEnd());
    		}
    	}
    	Map<String, Object> resultMap = new HashMap<>(); // 封装结果集
    	resultMap.put("list", list);
    	
    	String countsql = "select ISNULL(count(*),0) AS count from JL_QQ_REC a where 1=1 " + leftJoinWhere.toString()+" GROUP BY a.FR_No"+group.toString();
    	System.out.println(countsql);
    	SqlRowSet rowSet =this.jdbcTemplate.queryForRowSet(countsql);
    	Integer count = 0 ;
		while(rowSet.next()) {
			count = rowSet.getInt("count");
		}
		resultMap.put("count", count);
    	return resultMap;
		
	}
	
	public String getZs(String callId){
		Result result = new Result();
		if(StringUtils.isBlank(callId)){
			result.error(Result.error_102);
			return result.toResult();
		}
		SysUserVO sysUser = TokenUser.getUser();
		JlQqInfoVO jlQqInfo = new JlQqInfoVO();
		jlQqInfo.setCallId(callId);
		jlQqInfo.setUserNo(sysUser.getUserNo());
		List<JlQqInfoVO> jlQqInfoList = jlQqInfoSQL.findList(jlQqInfo);
		if(jlQqInfoList.size()>0){
			jlQqInfo = jlQqInfoList.get(0);
		}
		result.putJson(jlQqInfo);
		return result.toResult();
	}

	public String addRecordFlag(String callId, String writeTxt){
		Result result = new Result();
		if(StringUtils.isBlank(callId)){
			result.error(Result.error_102);
			return result.toResult();
		}
		SysUserVO sysUser = TokenUser.getUser();
		JlQqInfoVO jlQqInfo = new JlQqInfoVO();
		jlQqInfo.setCallId(callId);
		jlQqInfo.setUserNo(sysUser.getUserNo());
		List<JlQqInfoVO> jlHjInfoList = jlQqInfoSQL.findList(jlQqInfo);
		if(jlHjInfoList.size()>0){
			jlQqInfo = jlHjInfoList.get(0);
			jlQqInfo.setWriteTxt(writeTxt);
			jlQqInfoSQL.edit(jlQqInfo);
		}else{
			jlQqInfo.setUserName(sysUser.getUserName());
			jlQqInfo.setWriteTxt(writeTxt);
			jlQqInfo.setCreateTime(new Date());
			jlQqInfoSQL.add(jlQqInfo);
		}
		return result.toResult();
	}
	
	public void exportExcel(JlQqRecVO model, HttpServletRequest request, HttpServletResponse response){
    	StringBuffer leftJoinWhere = new StringBuffer();
    	if(StringUtils.isNotBlank(model.getCallTimeStart())){ // 开始时间
    		leftJoinWhere.append(" AND a.Call_Time_Start>='"+ model.getCallTimeStart() + "' ");
    		model.setCallTimeStart(null);
    	}
    	if(StringUtils.isNotBlank(model.getCallTimeEnd())){ // 结束时间
    		leftJoinWhere.append(" AND a.Call_Time_Start<='"+ model.getCallTimeEnd() + "' ");
    		model.setCallTimeEnd(null);
    	}
    	if(StringUtils.isNotBlank(model.getFrName())){
    		String str = model.getFrName();
    		leftJoinWhere.append(" AND (a.FR_Name LIKE '%"+str+"%' OR dbo.f_get_fryp(a.FR_Name,'"+str+"') =1 )");
    		model.setFrName(null);
    	}
    	if(StringUtils.isNotBlank(model.getQsName())){
    		String str = model.getQsName();
    		leftJoinWhere.append(" AND (a.QS_Name LIKE '%"+str+"%' OR dbo.f_get_fryp(a.QS_Name,'"+str+"') =1 )");
    		model.setQsName(null);
    	}
    	if(model.getJfFlag()!=null){
    		if(model.getJfFlag()==0){
    			leftJoinWhere.append(" AND a.Call_Count_Flag=0");
    		}else{
    			leftJoinWhere.append(" AND a.Call_Count_Flag<>0");
    		}
    	}
		model.setLeftJoinWhere(leftJoinWhere.toString());
		
		List<JlQqRecVO> jlQqRecList = this.findList(model);
		
		String fileName =  "通话记录.xls";
		
		OutputStream out = null;
		
		try {
			
			// EXCEL START
			HSSFWorkbook book = new HSSFWorkbook();
			HSSFSheet sheet = book.createSheet();
			CellStyle cellStyle = book.createCellStyle();
			cellStyle.setDataFormat(book.createDataFormat().getFormat("yyyy-MM-dd"));
			// 设置标题
			List<String> title = new ArrayList<String>();
			title.add("通话开始时间");
			title.add("通话结束时间");
			title.add("通话时长(秒)");
			title.add("电话号码");
			title.add("通话类型");
			title.add("监区");
			title.add("罪犯编号");
			title.add("罪犯姓名");
			title.add("亲属姓名");
			title.add("关系");
			title.add("警察信息");
			title.add("本机号码");
			// 标题 start
			HSSFRow row1 = sheet.createRow(0);
			for(int i=0; i<title.size(); i++){
				String t = title.get(i);
				HSSFCell cell = row1.createCell(i);
				cell.setCellValue(t);
			}
			// 标题 end
			
			// 记录 start
			for(int i=0; i<jlQqRecList.size(); i++){
				JlQqRecVO jlQqRec = jlQqRecList.get(i);
				HSSFRow row2 = sheet.createRow(i+1);
				
				HSSFCell cell0 = row2.createCell(0);
				cell0.setCellValue(jlQqRec.getCallTimeStart());
					
				HSSFCell cell1 = row2.createCell(1);
				cell1.setCellValue(jlQqRec.getCallTimeEnd());
				
				HSSFCell cell2 = row2.createCell(2);
				cell2.setCellValue(jlQqRec.getCallTimeLen());
				
				HSSFCell cell3 = row2.createCell(3);
				cell3.setCellValue(jlQqRec.getTele());
				
				HSSFCell cell4 = row2.createCell(4);
				cell4.setCellValue(jlQqRec.getType()==0?"亲情电话":"特批电话");
				
				HSSFCell cell5 = row2.createCell(5);
				cell5.setCellValue(jlQqRec.getJqName());
				
				HSSFCell cell6 = row2.createCell(6);
				cell6.setCellValue(jlQqRec.getFrNo());
				
				HSSFCell cell7 = row2.createCell(7);
				cell7.setCellValue(jlQqRec.getFrName());
				
				HSSFCell cell8 = row2.createCell(8);
				cell8.setCellValue(jlQqRec.getQsName());
				
				HSSFCell cell9 = row2.createCell(9);
				cell9.setCellValue(jlQqRec.getGx());
				
				HSSFCell cell10 = row2.createCell(10);
				cell10.setCellValue(jlQqRec.getYjName());
				
				HSSFCell cell11 = row2.createCell(11);
				cell11.setCellValue(jlQqRec.getLocaltele());
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
    
    
    public String getWeekCount(){
    	Result result = new Result();
    	String where = "";
    	String token = WebContextUtil.getRequest().getHeader(Constants.TOKEN_NAME);
		JqRoleManager jqRoleManager = new JqRoleManager();
		String jqs = jqRoleManager.getJqs(token);
		if("admin".equals(jqs)){
		}else if(StringUtils.isBlank(jqs)){
			where+=" AND 1<>1 ";
		}else if(StringUtils.isNotBlank(jqs)){
			where+=" AND JQ_No in ("+jqs+") ";
		}
		
    	String sql = "SELECT ISNULL(count(*),0) AS count,timedate from"
    			+ " (SELECT  CONVERT(VARCHAR(10),Call_Time_Start,111) as timedate"
    			+ 	" FROM JL_QQ_REC"
    			+ 	" where Call_Time_Start>= convert(varchar(10),dateadd(day,-7,getdate()),120)"
    			+   where
    			+ 	" ) as aa"
    			+ " group by aa.timedate";
    	List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
    	result.putData(list);
    	return result.toResult();
    }
}
