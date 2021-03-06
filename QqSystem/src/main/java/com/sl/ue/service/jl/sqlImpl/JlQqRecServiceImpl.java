package com.sl.ue.service.jl.sqlImpl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
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
import com.sl.ue.entity.sys.vo.SysLogVO;
import com.sl.ue.entity.sys.vo.SysQqLineVO;
import com.sl.ue.entity.sys.vo.SysQqServerVO;
import com.sl.ue.entity.sys.vo.SysUserVO;
import com.sl.ue.service.base.impl.BaseSqlImpl;
import com.sl.ue.service.jl.JlQqInfoService;
import com.sl.ue.service.jl.JlQqRecService;
import com.sl.ue.service.sys.SysLogService;
import com.sl.ue.service.sys.SysQqLineService;
import com.sl.ue.service.sys.SysQqServerService;
import com.sl.ue.util.Config;
import com.sl.ue.util.Constants;
import com.sl.ue.util.DateUtil;
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
	@Autowired
	private SysQqLineService sysQqLineSQL;
	@Autowired
	private SysLogService sysLogSQL;
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
    		qqRec.setCallRecfileUrl("");
    		
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
	
	public String findPojoByTelCost(JlQqRecVO model, Integer pageSize, Integer pageNum){
		Result result = new Result();
		StringBuffer leftJoinField = new StringBuffer();
		leftJoinField.append(",a.JQ_Name AS jqName");
		leftJoinField.append(",a.FR_No AS frNo");
		leftJoinField.append(",a.FR_Name AS frName");
		leftJoinField.append(",sum(a.Call_Count_IN) as countIn");
		leftJoinField.append(",sum(a.Call_Count_OUT) as countOut");
		leftJoinField.append(",count(a.Call_Count_IN) as telCountNum");
		
		StringBuffer leftJoinWhere = new StringBuffer();
    	if(StringUtils.isNotBlank(model.getCallTimeStart())){ // 开始时间
    		leftJoinWhere.append(" AND a.Call_Time_Start>='"+ model.getCallTimeStart() + "' ");
    	}
    	if(StringUtils.isNotBlank(model.getCallTimeEnd())){ // 结束时间
    		leftJoinWhere.append(" AND a.Call_Time_Start<='"+ model.getCallTimeEnd() + "' ");
    	}
    	if(StringUtils.isNotBlank(model.getJqNo())){
    		leftJoinWhere.append(" AND a.JQ_No='"+model.getJqNo()+"'");
    	}
    	if(StringUtils.isNotBlank(model.getFrNo())){
    		String str = model.getFrNo();
    		leftJoinWhere.append(" AND a.FR_No LIKE '%"+str+"%'");
    		model.setFrNo(null);
    	}
    	if(StringUtils.isNotBlank(model.getFrName())){
    		String str = model.getFrName();
    		leftJoinWhere.append(" AND (a.FR_Name LIKE '%"+str+"%' OR dbo.f_get_fryp(a.FR_Name,'"+str+"') =1 )");
    		model.setFrName(null);
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
    	/** 监区权限 开始 */
    	String token = WebContextUtil.getRequest().getHeader(Constants.TOKEN_NAME);
		JqRoleManager jqRoleManager = new JqRoleManager();
		String jqs = jqRoleManager.getJqs(token);
		if("admin".equals(jqs)){
			
		}else if(StringUtils.isBlank(jqs)){
			leftJoinWhere.append(" AND 1<>1 ");
		}else if(StringUtils.isNotBlank(jqs)){
			leftJoinWhere.append(" AND a.JQ_No in ("+jqs+") ");
		}
		/** 监区权限 结束 */
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
    	
    	String countsql = "select ISNULL(count(*),0) AS count from JL_QQ_REC a where 1=1 " + leftJoinWhere.toString()+" GROUP BY a.FR_No";
    	System.out.println(countsql);
    	SqlRowSet rowSet =this.jdbcTemplate.queryForRowSet(countsql);
    	Integer count = 0 ;
		while(rowSet.next()) {
			count = rowSet.getInt("count");
		}
		resultMap.put("count", count);
		result.putPojo(resultMap);
		
		// 话费总额
		StringBuffer countSql = new StringBuffer();
		countSql.append("SELECT ");
		countSql.append("sum(a.Call_Count_IN) AS countIn");
		countSql.append(",sum(a.Call_Count_OUT) AS countOut");
		countSql.append(",sum(dbo.f_get_callTimeLen(a.Call_Time_Len)) AS telCallLen");
		countSql.append(",count(a.Call_Count_IN) as countNum");
		countSql.append(" FROM JL_QQ_REC a");
		countSql.append(" WHERE 1=1");
		countSql.append(leftJoinWhere.toString());
		List<Map<String, Object>> sumList = this.jdbcTemplate.queryForList(countSql.toString());
		if(sumList.size()>0){
			result.putJson("costRecSum", sumList.get(0));
		}
    	return result.toResult();
		
	}
	
	public String findDetailsPojo(String callTimeStart, String callTimeEnd,String frNo, Integer pageSize, Integer pageNum){
		Result result = new Result();
		StringBuffer leftJoinWhere = new StringBuffer();
		if(StringUtils.isNotBlank(callTimeStart)){
			leftJoinWhere.append(" AND a.Call_Time_Start>='"+callTimeStart+"'");
		}
		if(StringUtils.isNotBlank(callTimeEnd)){
			leftJoinWhere.append(" AND a.Call_Time_Start<='"+callTimeEnd+"'");
		}
		JlQqRecVO jlQqRec = new JlQqRecVO();
		jlQqRec.setFrNo(frNo);
		jlQqRec.setLeftJoinWhere(leftJoinWhere.toString());
		Map<String, Object> map = this.findPojo(jlQqRec, pageSize, pageNum);
		result.putPojo(map);
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("sum(a.Call_Count_IN) AS countIn");
		sql.append(",sum(a.Call_Count_OUT) AS countOut");
		sql.append(",sum(dbo.f_get_callTimeLen(a.Call_Time_Len)) AS telCallLen");
		sql.append(" FROM JL_QQ_REC a");
		sql.append(" WHERE 1=1");
		sql.append(" AND a.FR_No='"+frNo+"'");
		if(StringUtils.isNotBlank(callTimeStart)){
			sql.append(" AND a.Call_Time_Start>='"+callTimeStart+"'");
		}
		if(StringUtils.isNotBlank(callTimeEnd)){
			sql.append(" AND a.Call_Time_Start<='"+callTimeEnd+"'");
		}
		List<Map<String, Object>> sumList = this.jdbcTemplate.queryForList(sql.toString());
		if(sumList.size()>0){
			result.putJson("costSum", sumList.get(0));
		}
		return result.toResult();
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
    
    public void exportCostExcel(JlQqRecVO model, HttpServletRequest request, HttpServletResponse response){
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
    	/** 监区权限 开始 */
    	String token = WebContextUtil.getRequest().getHeader(Constants.TOKEN_NAME);
		JqRoleManager jqRoleManager = new JqRoleManager();
		String jqs = jqRoleManager.getJqs(token);
		if("admin".equals(jqs)){
			
		}else if(StringUtils.isBlank(jqs)){
			leftJoinWhere.append(" AND 1<>1 ");
		}else if(StringUtils.isNotBlank(jqs)){
			leftJoinWhere.append(" AND a.JQ_No in ("+jqs+") ");
		}
		/** 监区权限 结束 */
    	StringBuffer sql = new StringBuffer();
    	sql.append("select ROW_NUMBER() OVER(ORDER BY a.JQ_No ASC) AS rowid");
    	sql.append(leftJoinField.toString());
    	sql.append(" FROM JL_QQ_REC AS a where 1=1");
    	sql.append(leftJoinWhere.toString());
    	sql.append(" GROUP BY a.FR_No,a.JQ_No,a.JQ_Name,a.FR_Name");
    	System.out.println(sql.toString());
    	List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql.toString());
  
		
		String fileName =  "话费帐单.xls";
		
		OutputStream out = null;
		
		try {
			// EXCEL START
			HSSFWorkbook book = new HSSFWorkbook();
			HSSFSheet sheet = book.createSheet("话费帐单");
			CellStyle cellStyle = book.createCellStyle();
			cellStyle.setDataFormat(book.createDataFormat().getFormat("yyyy-MM-dd"));
			// 设置标题
			List<String> title = new ArrayList<String>();
			title.add("监区名称");
			title.add("罪犯编号");
			title.add("罪犯姓名");
			title.add("内部话费总额(元)");
			title.add("外部话费总额(元)");
			title.add("拨打次数");
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
				Map<String, Object> map = list.get(i);
				HSSFRow row2 = sheet.createRow(i+1);
				
				HSSFCell cell0 = row2.createCell(0);
				cell0.setCellValue(map.get("jqName")+"");
					
				HSSFCell cell1 = row2.createCell(1);
				cell1.setCellValue(map.get("frNo")+"");
				
				HSSFCell cell2 = row2.createCell(2);
				cell2.setCellValue(map.get("frName")+"");
				
				HSSFCell cell3 = row2.createCell(3);
				Double countIn = Double.parseDouble(map.get("countIn")+"") ;
				countIn = countIn/1000;
				cell3.setCellValue(countIn+"");
				
				HSSFCell cell4 = row2.createCell(4);
				Double countOut = Double.parseDouble(map.get("countOut")+"");
				countOut = countOut/1000;
				cell4.setCellValue(countOut+"");
				
				HSSFCell cell5 = row2.createCell(5);
				cell5.setCellValue(map.get("telCountNum")+"");
				
			}
			
			// 话费总额
			StringBuffer countSql = new StringBuffer();
			countSql.append("SELECT ");
			countSql.append("sum(a.Call_Count_IN) AS countIn");
			countSql.append(",sum(a.Call_Count_OUT) AS countOut");
			countSql.append(",sum(dbo.f_get_callTimeLen(a.Call_Time_Len)) AS telCallLen");
			countSql.append(",count(a.Call_Count_IN) as countNum");
			countSql.append(" FROM JL_QQ_REC a");
			countSql.append(" WHERE 1=1");
			countSql.append(leftJoinWhere.toString());
			List<Map<String, Object>> sumList = this.jdbcTemplate.queryForList(countSql.toString());
			
			HSSFSheet sheet1 =book.createSheet("话费总计");
			CellStyle cellStyle1 = book.createCellStyle();
			cellStyle1.setDataFormat(book.createDataFormat().getFormat("yyyy-MM-dd"));
			// 设置标题
			List<String> title1 = new ArrayList<String>();
			title1.add("内部话费总额(元)");
			title1.add("外部话费总额(元)");
			title1.add("总时长(分钟)");
			title1.add("总次数");
			// 标题 start
			HSSFRow rowFr = sheet1.createRow(0);
			for(int i=0; i<title1.size(); i++){
				String t = title1.get(i);
				HSSFCell cell = rowFr.createCell(i);
				cell.setCellValue(t);
			}
			// 标题 end
			
			// 记录 start
			for(int i=0; i<sumList.size(); i++){
				Map<String, Object> map = sumList.get(i);
				HSSFRow rowFr2 = sheet1.createRow(i+1);
				
				HSSFCell cell0 = rowFr2.createCell(0);
				Double countIn = Double.parseDouble(map.get("countIn")+"");
				countIn = countIn/1000;
				cell0.setCellValue(countIn+"");
					
				HSSFCell cell1 = rowFr2.createCell(1);
				Double countOut = Double.parseDouble(map.get("countOut")+"");
				countOut = countOut/1000;
				cell1.setCellValue(countOut+"");
				
				HSSFCell cell2 = rowFr2.createCell(2);
				cell2.setCellValue(map.get("telCallLen")+"");
				
				HSSFCell cell3 = rowFr2.createCell(3);
				cell3.setCellValue(map.get("countNum")+"");
				
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
    
    public void exportFrCostExcel(String callTimeStart, String callTimeEnd, String frNo, HttpServletRequest request, HttpServletResponse response){
    	StringBuffer leftJoinWhere = new StringBuffer();
		if(StringUtils.isNotBlank(callTimeStart)){
			leftJoinWhere.append(" AND a.Call_Time_Start>='"+callTimeStart+"'");
		}
		if(StringUtils.isNotBlank(callTimeEnd)){
			leftJoinWhere.append(" AND a.Call_Time_Start<='"+callTimeEnd+"'");
		}
		JlQqRecVO jlQqRec = new JlQqRecVO();
		jlQqRec.setFrNo(frNo);
		jlQqRec.setLeftJoinWhere(leftJoinWhere.toString());
		List<JlQqRecVO> list = this.findList(jlQqRec);
		
		String fileName =  "编号"+frNo+"话费帐单.xls";
		
		OutputStream out = null;
		
		try {
			// EXCEL START
			HSSFWorkbook book = new HSSFWorkbook();
			HSSFSheet sheet = book.createSheet("编号"+frNo+"话费帐单");
			CellStyle cellStyle = book.createCellStyle();
			cellStyle.setDataFormat(book.createDataFormat().getFormat("yyyy-MM-dd"));
			// 设置标题
			List<String> title = new ArrayList<String>();
			title.add("监区名称");
			title.add("罪犯编号");
			title.add("罪犯姓名");
			title.add("开始时间");
			title.add("结束时间");
			title.add("亲属姓名");
			title.add("关系");
			title.add("通话时长");
			title.add("主叫号码");
			title.add("被叫号码");
			title.add("内部话费(元)");
			title.add("外部话费(元)");
			
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
				JlQqRecVO t = list.get(i);
				HSSFRow row2 = sheet.createRow(i+1);
				
				HSSFCell cell0 = row2.createCell(0);
				cell0.setCellValue(t.getJqName());
					
				HSSFCell cell1 = row2.createCell(1);
				cell1.setCellValue(t.getFrNo());
				
				HSSFCell cell2 = row2.createCell(2);
				cell2.setCellValue(t.getFrName());
				
				HSSFCell cell3 = row2.createCell(3);
				cell3.setCellValue(t.getCallTimeStart());
				
				HSSFCell cell4 = row2.createCell(4);
				cell4.setCellValue(t.getCallTimeEnd());
				
				HSSFCell cell5 = row2.createCell(5);
				cell5.setCellValue(t.getQsName());
				
				HSSFCell cell6 = row2.createCell(6);
				cell6.setCellValue(t.getGx());
				
				HSSFCell cell7 = row2.createCell(7);
				cell7.setCellValue(t.getCallTimeLen()+"");
				
				HSSFCell cell8 = row2.createCell(8);
				cell8.setCellValue(t.getLocaltele());
				
				HSSFCell cell9 = row2.createCell(9);
				cell9.setCellValue(t.getTele());
				
				HSSFCell cell10 = row2.createCell(10);
				cell10.setCellValue(Float.parseFloat(t.getCallCountIn().toString())/1000+"");
				
				HSSFCell cell11 = row2.createCell(11);
				cell11.setCellValue(Float.parseFloat(t.getCallCountOut().toString())/1000+"");
			}
			
			// 话费总额
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT ");
			sql.append("sum(a.Call_Count_IN) AS countIn");
			sql.append(",sum(a.Call_Count_OUT) AS countOut");
			sql.append(",sum(dbo.f_get_callTimeLen(a.Call_Time_Len)) AS telCallLen");
			sql.append(" FROM JL_QQ_REC a");
			sql.append(" WHERE 1=1");
			sql.append(" AND a.FR_No='"+frNo+"'");
			if(StringUtils.isNotBlank(callTimeStart)){
				sql.append(" AND a.Call_Time_Start>='"+callTimeStart+"'");
			}
			if(StringUtils.isNotBlank(callTimeEnd)){
				sql.append(" AND a.Call_Time_Start<='"+callTimeEnd+"'");
			}
			List<Map<String, Object>> sumList = this.jdbcTemplate.queryForList(sql.toString());
			
			HSSFSheet sheet1 =book.createSheet("编号"+frNo+"话费总计");
			CellStyle cellStyle1 = book.createCellStyle();
			cellStyle1.setDataFormat(book.createDataFormat().getFormat("yyyy-MM-dd"));
			// 设置标题
			List<String> title1 = new ArrayList<String>();
			title1.add("内部话费总额(元)");
			title1.add("外部话费总额(元)");
			title1.add("总时长(分钟)");
			// 标题 start
			HSSFRow rowFr = sheet1.createRow(0);
			for(int i=0; i<title1.size(); i++){
				String t = title1.get(i);
				HSSFCell cell = rowFr.createCell(i);
				cell.setCellValue(t);
			}
			// 标题 end
			
			// 记录 start
			for(int i=0; i<sumList.size(); i++){
				Map<String, Object> map = sumList.get(i);
				HSSFRow rowFr2 = sheet1.createRow(i+1);
				
				HSSFCell cell0 = rowFr2.createCell(0);
				Double countIn = Double.parseDouble(map.get("countIn")+"");
				countIn = countIn/1000;
				cell0.setCellValue(countIn+"");
					
				HSSFCell cell1 = rowFr2.createCell(1);
				Double countOut = Double.parseDouble(map.get("countOut")+"");
				countOut = countOut/1000;
				cell1.setCellValue(countOut+"");
				
				HSSFCell cell2 = rowFr2.createCell(2);
				cell2.setCellValue(map.get("telCallLen")+"");
				
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
    
    public Map<String, Object> findPojoRemind(String callTimeStart, String callTimeEnd, Integer pageSize, Integer pageNum){
    	StringBuffer sql = new StringBuffer();
    	sql.append("SELECT 1 FROM JL_QQ_REC a");
    	sql.append(" WHERE a.FR_No=jl.FR_No");
    	if(StringUtils.isNotBlank(callTimeStart)){
			sql.append(" AND a.Call_Time_Start>='"+callTimeStart+"'");
		}
		if(StringUtils.isNotBlank(callTimeEnd)){
			sql.append(" AND a.Call_Time_Start<='"+callTimeEnd+"'");
		}
		/** 监区权限 开始 */
    	String token = WebContextUtil.getRequest().getHeader(Constants.TOKEN_NAME);
		JqRoleManager jqRoleManager = new JqRoleManager();
		String jqs = jqRoleManager.getJqs(token);
		if("admin".equals(jqs)){
			
		}else if(StringUtils.isBlank(jqs)){
			sql.append(" AND 1<>1 ");
		}else if(StringUtils.isNotBlank(jqs)){
			sql.append(" AND a.JQ_No in ("+jqs+") ");
		}
		/** 监区权限 结束 */
		
		String sqlStr = "SELECT ROW_NUMBER() OVER(ORDER BY jl.JQ ASC) AS rowid, jl.FR_No AS frNo,jl.FR_Name AS frName,jq.JQ_Name AS jqName FROM JL_FR jl left join JL_JQ jq ON jq.JQ_No=jl.JQ where not exists("+sql.toString()+")";
		int startNum = (pageNum-1)*pageSize;
		int endNum = pageNum*pageSize;
    	String sqlStr1 = "select * from("+sqlStr+") t where t.rowid>"+startNum+" AND t.rowid<="+endNum;
    	System.out.println(sqlStr1);
    	List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sqlStr1);
    	
    	Map<String, Object> map = new HashMap();
    	map.put("list", list);
    	
    	String countSql = "SELECT ISNULL(count(*),0) AS count FROM JL_FR jl where not exists("+sql.toString()+")";
    	System.out.println(countSql);
    	SqlRowSet rowSet =this.jdbcTemplate.queryForRowSet(countSql);
    	Integer count = 0 ;
		while(rowSet.next()) {
			count = rowSet.getInt("count");
		}
		map.put("count", count);
    	return map;
		
    }
    
    public void exportExcelByRemind(String callTimeStart, String callTimeEnd, HttpServletRequest request, HttpServletResponse response){
    	StringBuffer sql = new StringBuffer();
    	sql.append("SELECT 1 FROM JL_QQ_REC a");
    	sql.append(" WHERE a.FR_No=jl.FR_No");
    	if(StringUtils.isNotBlank(callTimeStart)){
			sql.append(" AND a.Call_Time_Start>='"+callTimeStart+"'");
		}
		if(StringUtils.isNotBlank(callTimeEnd)){
			sql.append(" AND a.Call_Time_Start<='"+callTimeEnd+"'");
		}
		/** 监区权限 开始 */
    	String token = WebContextUtil.getRequest().getHeader(Constants.TOKEN_NAME);
		JqRoleManager jqRoleManager = new JqRoleManager();
		String jqs = jqRoleManager.getJqs(token);
		if("admin".equals(jqs)){
			
		}else if(StringUtils.isBlank(jqs)){
			sql.append(" AND 1<>1 ");
		}else if(StringUtils.isNotBlank(jqs)){
			sql.append(" AND a.JQ_No in ("+jqs+") ");
		}
		/** 监区权限 结束 */
		String sqlStr = "SELECT jl.FR_No AS frNo,jl.FR_Name AS frName,jq.JQ_Name AS jqName FROM JL_FR jl left join JL_JQ jq ON jq.JQ_No=jl.JQ where not exists("+sql.toString()+")";
		System.out.println(sqlStr);
    	List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sqlStr);
		
		String fileName =  "未打电话罪犯名单.xls";
		
		OutputStream out = null;
		
		try {
			// EXCEL START
			HSSFWorkbook book = new HSSFWorkbook();
			HSSFSheet sheet = book.createSheet("未打电话罪犯名单");
			CellStyle cellStyle = book.createCellStyle();
			cellStyle.setDataFormat(book.createDataFormat().getFormat("yyyy-MM-dd"));
			// 设置标题
			List<String> title = new ArrayList<String>();
			title.add("监区名称");
			title.add("罪犯编号");
			title.add("罪犯姓名");
			
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
				Map<String, Object> map = list.get(i);
				HSSFRow row2 = sheet.createRow(i+1);
				
				HSSFCell cell0 = row2.createCell(0);
				cell0.setCellValue(map.get("jqName")+"");
					
				HSSFCell cell1 = row2.createCell(1);
				cell1.setCellValue(map.get("frNo")+"");
				
				HSSFCell cell2 = row2.createCell(2);
				cell2.setCellValue(map.get("frName")+"");
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
    
    public String getFileUrl(Long id){
    	Result result =new Result();
    	JlQqRecVO model = this.findOne(id);
    	if(model == null){
    		return result.toResult();
    	}
    	String callRecPath = Config.getPropertiesValue("callRecfile");
    	
    	String qqServerPath = ""; //录音文件网络地址
    	List<SysQqServerVO> list = sysQqServerSQL.findList(new SysQqServerVO());
    	for(SysQqServerVO qqServer : list){
    		if(qqServer.getServerName().equals(model.getJy())){
    			qqServerPath = qqServer.getRecurl();
    		}
    	}
    	
    	if(StringUtils.isNotBlank(model.getCallRecfile())){
    		String end =model.getCallRecfile().replace("\\", "/");
			end = end.substring(end.indexOf("/")+1);
			end = end.substring(end.indexOf("/"));
			String url = qqServerPath + callRecPath + end;
			result.putJson("callUrl",url);
			System.out.println(url);
		}
    	
		return result.toResult();
    }
    
    public void downAudio(Long webid, HttpServletRequest request, HttpServletResponse response){
    	Result result = new Result();
    	JlQqRecVO jlQqRec = this.findOne(webid);
    	if(jlQqRec==null){
    		result.error(Result.error_103,"数据库查询不到此记录");
    		return;
    	}
    	SysUserVO user = TokenUser.getUser();
    	SysLogVO sysLog = new SysLogVO();
    	sysLog.setType("严重");
		sysLog.setOp("下载录音");
		sysLog.setInfo("下载罪犯编号: "+jlQqRec.getFrNo()+"，罪犯姓名: "+jlQqRec.getFrName()+"；时间: "+jlQqRec.getCallTimeStart()+" 的录音");
		sysLog.setModel("通话录音");
		sysLog.setUserNo(user.getUserNo());
		sysLog.setUserName(user.getUserName());
		sysLog.setLogTime(DateUtil.getDefaultNow());
		sysLog.setUserIp(request.getRemoteAddr());
		sysLogSQL.add(sysLog);
		
		
		InputStream inputStream = null;
    	if(StringUtils.isNotBlank(jlQqRec.getCallRecfile())){
    		if("Server1".equals(jlQqRec.getJy())){
    			File file = new File(jlQqRec.getCallRecfile());
        		if(!file.exists()){
        			return;
        		}
        		try {
					inputStream = new FileInputStream(file);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
    		}else{
    			String fileUrl="";
	    		List<SysQqServerVO> list = sysQqServerSQL.findList(new SysQqServerVO());
	    		for(SysQqServerVO qqServer : list){
	    			if(qqServer.getServerName().equals(jlQqRec.getJy())){
	    				String end =jlQqRec.getCallRecfile().replace("\\", "/");
	        			end = end.substring(end.indexOf("/")+1);
	        			end = end.substring(end.indexOf("/"));
	        			fileUrl = qqServer.getRecurl()+"/file"+end;
	    			}
	    		}
	    		int HttpResult; // 服务器返回的状态
	            try
	            {
	                URL url =new URL(fileUrl); // 创建URL
	                URLConnection urlconn = url.openConnection(); // 试图连接并取得返回状态码
	                urlconn.connect();
	                HttpURLConnection httpconn =(HttpURLConnection)urlconn;
	                HttpResult = httpconn.getResponseCode();
	                if(HttpResult != HttpURLConnection.HTTP_OK) {
	                    System.out.print("无法连接到");
	                } else {
	                    inputStream = urlconn.getInputStream();
	                }
	            }
	            catch (Exception e) {
	                e.printStackTrace();
	            }
    			
    		}
    	}
    	
    	
    	OutputStream out = null;
		BufferedInputStream in =null;
		try {
			String fileName ="录音.mp3";
			// 处理不同浏览器中文名称编码
			String userAgent=request.getHeader("USER-AGENT");
			if(userAgent.indexOf("Chrome")!=-1 || userAgent.indexOf("Safari")!=-1 || userAgent.indexOf("Firefox")!=-1){
				fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			}else{
				fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
				//fileName = URLEncoder.encode(fileName,"UTF8");
			}
			response.setHeader("Content-Disposition", "attachment;filename="+fileName);
			response.setHeader("Cache-Control","no-cache");//设置头
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/octet-stream");
			out = response.getOutputStream();
			//in = new BufferedInputStream(new FileInputStream(file));
			in = new BufferedInputStream(inputStream);
			int l = 1024*1024;//1M 默认，可在配置文件中设置此值大小
			//int byteCount = 0;
			byte[] buffer = new byte[l];
			int bytesRead = -1;//文件大小
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
				//byteCount += bytesRead;
			}
			out.flush();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				if(in!=null){
					in.close();
				}
			}
			catch (IOException ex) {
			}
			try {
				if(out!=null){
					out.close();
				}
			}
			catch (IOException ex) {
			}
		}
    }
}
