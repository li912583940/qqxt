package com.sl.ue.service.jl.sqlImpl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.sl.ue.entity.jl.vo.JlFrVO;
import com.sl.ue.entity.jl.vo.JlJqVO;
import com.sl.ue.entity.jl.vo.JlQqCzVO;
import com.sl.ue.entity.sys.vo.SysLogVO;
import com.sl.ue.entity.sys.vo.SysUserVO;
import com.sl.ue.service.base.impl.BaseSqlImpl;
import com.sl.ue.service.jl.JlFrService;
import com.sl.ue.service.jl.JlJqService;
import com.sl.ue.service.jl.JlQqCzService;
import com.sl.ue.service.sys.SysLogService;
import com.sl.ue.util.Config;
import com.sl.ue.util.DateUtil;
import com.sl.ue.util.business.FrThreadXLS;
import com.sl.ue.util.business.FrThreadXLSX;
import com.sl.ue.util.http.Result;
import com.sl.ue.util.http.token.TokenManager;
import com.sl.ue.util.http.token.TokenUser;

@Service("jlFrSQL")
public class JlFrServiceImpl extends BaseSqlImpl<JlFrVO> implements JlFrService{
	@Autowired
	private SysLogService sysLogSQL;
	@Autowired
	private JlQqCzService jlQqCzSQL;
	@Autowired
	private JlJqService jlJqSQL;
	@Autowired
	private JlFrService jlFrSQL;
	
	@Override
	public Map<String, Object> findPojoJoin(JlFrVO model, Integer pageSize, Integer pageNum) {
		StringBuffer field = new StringBuffer(); // sql关联字段
		field.append(",b.JQ_Name");
		field.append(",c.JB_Name");
		
		StringBuffer table = new StringBuffer(); // sql关联表
		table.append(" left join JL_JQ b ON a.JQ=b.JQ_No");
		table.append(" left join JL_JB c ON a.JB_No=c.JB_No");
		
		StringBuffer Where = new StringBuffer(); // sql条件
    	if(StringUtils.isNotBlank(model.getFrName())){
    		String str = model.getFrName();
    		Where.append(" AND (a.FR_Name LIKE '%"+str+"%' OR dbo.f_get_fryp(a.FR_Name,'"+str+"') =1 )");
    		model.setFrName(null);
    	}
    	model.setLeftJoinField(field.toString());
		model.setLeftJoinTable(table.toString());
    	model.setLeftJoinWhere(Where.toString());
		Map<String, Object> map = this.findPojo(model, pageSize, pageNum);
		return map;
	}
	
	@Override
	public void exportExcel(JlFrVO model, HttpServletRequest request, HttpServletResponse response) {
		StringBuffer field = new StringBuffer();
		field.append(",b.JQ_Name");
		field.append(",c.JB_Name");
		
		
		StringBuffer table = new StringBuffer();
		table.append(" left join JL_JQ b ON a.JQ=b.JQ_No");
		table.append(" left join JL_JB c ON a.JB_No=c.JB_No");
		
		StringBuffer Where = new StringBuffer(); // sql条件
    	if(StringUtils.isNotBlank(model.getFrName())){
    		String str = model.getFrName();
    		Where.append(" AND (a.FR_Name LIKE '%"+str+"%' OR dbo.f_get_fryp(a.FR_Name,'"+str+"') =1 )");
    		model.setFrName(null);
    	}
    	
		model.setLeftJoinField(field.toString());
		model.setLeftJoinTable(table.toString());
		model.setLeftJoinWhere(Where.toString());
		
		List<JlFrVO> frList = this.findList(model);
//		if(frList == null || frList.size() == 0){
//			response.setContentType("application/octet-stream");
//			return ;
//		}
		
		String fileName =  "罪犯信息.xls";
		
		OutputStream out = null;
		
		try {
			
			// EXCEL START
			HSSFWorkbook book = new HSSFWorkbook();
			HSSFSheet sheet = book.createSheet();
			CellStyle cellStyle = book.createCellStyle();
			cellStyle.setDataFormat(book.createDataFormat().getFormat("yyyy-MM-dd"));
			// 设置标题
			List<String> title = new ArrayList<String>();
			title.add("罪犯编号");
			title.add("罪犯姓名");
			title.add("罪犯卡号");
			title.add("监区");
			title.add("分管等级");
			title.add("电话卡号");
			title.add("电话密码");
			// 标题 start
			HSSFRow row1 = sheet.createRow(0);
			for(int i=0; i<title.size(); i++){
				String t = title.get(i);
				HSSFCell cell = row1.createCell(i);
				cell.setCellValue(t);
			}
			// 标题 end
			
			// 记录 start
			for(int i=0; i<frList.size(); i++){
				JlFrVO jlFr = frList.get(i);
				HSSFRow row2 = sheet.createRow(i+1);
				
				HSSFCell cell0 = row2.createCell(0);
				cell0.setCellValue(jlFr.getFrNo());
					
				HSSFCell cell1 = row2.createCell(1);
				cell1.setCellValue(jlFr.getFrName()!=null?jlFr.getFrName():"");
				
				HSSFCell cell2 = row2.createCell(2);
				cell2.setCellValue(StringUtils.isNotBlank(jlFr.getFrCard())?jlFr.getFrCard():"");
				
				HSSFCell cell3 = row2.createCell(3);
				cell3.setCellValue(StringUtils.isNotBlank(jlFr.getJqName())?jlFr.getJqName():"");
				
				HSSFCell cell4 = row2.createCell(4);
				cell4.setCellValue(StringUtils.isNotBlank(jlFr.getJbName())?jlFr.getJbName():"");
				
				HSSFCell cell5 = row2.createCell(5);
				cell5.setCellValue(StringUtils.isNotBlank(jlFr.getQqZh())?jlFr.getQqZh():"");
				
				HSSFCell cell6 = row2.createCell(6);
				cell6.setCellValue(StringUtils.isNotBlank(jlFr.getQqMm())?jlFr.getQqMm():"");
				
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

	
	public String importExcel(HttpServletRequest request, HttpServletResponse response){
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		MultipartFile mFile = null;
		for (Iterator<?> i = fileMap.keySet().iterator(); i.hasNext();) {
			Object obj = i.next();
			mFile = (MultipartFile) fileMap.get(obj);
		}
		String datePath = Config.getPropertiesValue("file.path")+"/excelfile";
	    String nowStr = DateUtil.getFormat(new Date(), "yyyyMMddHHmmss");
		BufferedInputStream in =null;
		BufferedOutputStream out = null;
		String excelFilePath = null;
		String fileExt = null;
		try {
			// 得到上传的文件的文件名
			String filename = mFile.getOriginalFilename();
			// 获取文件后缀名
			if (filename == null || ("").equals(filename)) {
				return "上传文件不存在";
			}
			File file = new File(datePath);
			if(!file.exists()){
				file.mkdirs();
			}
			boolean isPattern = false;
			fileExt = filename.substring(filename.lastIndexOf(".")+1);
			if("xls".equals(fileExt) || "xlsx".equals(fileExt)){
				isPattern = true;
			}
			if(isPattern == false){
				return "不支持此格式";
			}
			excelFilePath = datePath+"/"+nowStr+"."+fileExt;//excel文件临时存储路径
			System.out.println("路径： "+excelFilePath);
			File saveFile = new File(excelFilePath);
			int l = 10*1024*1024;//1M 默认，可在配置文件中设置此值大小
			String typeBufferSize = Config.getPropertiesValue("file.buffer.size");
			if(!"".equals(typeBufferSize)){
				int tfz =Integer.parseInt(typeBufferSize.trim());
				l = tfz*1024;
			}
			in = new BufferedInputStream(mFile.getInputStream());
			out = new BufferedOutputStream(new FileOutputStream(saveFile));
			int byteCount = 0;
			byte[] buffer = new byte[l];
			int bytesRead = -1;//文件大小
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
				byteCount += bytesRead;
			}
			out.flush();
		} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}finally {
			// 启动线程 处理罪犯信息
			if(StringUtils.isNotBlank(excelFilePath) && StringUtils.isNotBlank(fileExt)){
				if("xls".equals(fileExt)){
					FrThreadXLS frThread = new FrThreadXLS(excelFilePath);
					frThread.start();
				}else if("xlsx".equals(fileExt)){
					FrThreadXLSX frThread = new FrThreadXLSX(excelFilePath);
					frThread.start();
				}
				
			}
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
		
		return "";
	
	}
	
	
	public boolean frExist(String frNo){
		String sql = "SELECT ISNULL(count(*),0) as count FROM JL_FR where FR_No='"+frNo+"'";
		Integer count = this.jdbcTemplate.queryForObject(sql, Integer.class);
		if(count>0){
			return true;
		}else{
			return false;
		}
	}
	
	public Map<String, Object> findPojoCharge(JlFrVO model, Integer pageSize, Integer pageNum){
		StringBuffer leftJoinField = new StringBuffer(); // sql关联字段
		leftJoinField.append(",b.JQ_Name");
		
		StringBuffer leftJoinTable = new StringBuffer(); // sql关联表
		leftJoinTable.append(" left join JL_JQ b ON a.JQ=b.JQ_No");
		
		StringBuffer leftJoinWhere = new StringBuffer(); // sql条件
    	if(StringUtils.isNotBlank(model.getFrName())){
    		String str = model.getFrName();
    		leftJoinWhere.append(" AND (a.FR_Name LIKE '%"+str+"%' OR dbo.f_get_fryp(a.FR_Name,'"+str+"') =1 )");
    		model.setFrName(null);
    	}
    	model.setLeftJoinField(leftJoinField.toString());
		model.setLeftJoinTable(leftJoinTable.toString());
    	model.setLeftJoinWhere(leftJoinWhere.toString());
		Map<String, Object> map = this.findPojo(model, pageSize, pageNum);
		return map;
	}
	
	public String requestRecharge(Integer id, Integer czje){
		Result result = new Result();
		if(czje==null || czje==0){
			result.error(Result.error_102, "充值必须大于0");
			return result.toResult();
		}
		JlFrVO  jlFr = this.findOne(id);
		if(jlFr==null){
			result.error(Result.error_103, "数据错误，查询记录为空");
			return result.toResult();
		}
		JlJqVO jlJq = new JlJqVO(); 
		jlJq.setJqNo(jlFr.getJq());
		List<JlJqVO> jlJqList = jlJqSQL.findList(jlJq);
		jlJq = jlJqList.get(0);
		
		SysUserVO sysUser = TokenUser.getUser();
		Date nowDate = new Date();
		JlQqCzVO jlQqCz = new JlQqCzVO();
		jlQqCz.setFrNo(jlFr.getFrNo());
		jlQqCz.setFrName(jlFr.getFrName());
		jlQqCz.setJy(jlFr.getJy());
		jlQqCz.setJqNo(jlFr.getJq());
		jlQqCz.setJqName(jlJq.getJqName());
		jlQqCz.setCzsj(nowDate);
		jlQqCz.setCzje(czje);
		jlQqCz.setCzrNo(sysUser.getUserNo());
		jlQqCz.setCzrName(sysUser.getUserName());
		jlQqCz.setCzzt(1);
		jlQqCzSQL.add(jlQqCz);
		
		jlFr.setQqYe((jlFr.getQqYe()==null?0:jlFr.getQqYe())+czje);
		this.edit(jlFr);
		
		SysLogVO sysLog = new SysLogVO();
		sysLog.setType("正常");
		String now = DateUtil.getDefault(nowDate);
		sysLog.setLogTime(now);
		sysLog.setUserNo(sysUser.getUserNo());
		sysLog.setUserName(sysUser.getUserName());
		sysLog.setModel("话费充值");
		sysLog.setInfo("罪犯编号为"+jlFr.getFrNo()+" 罪犯姓名为"+jlFr.getFrName()+"在"+now+"充值"+((double)czje/1000)+"元");
		sysLog.setOp("话费充值");
		sysLogSQL.add(sysLog);
		return result.toResult();
	}
	
	public String requestRefund(Integer id){
		Result result = new Result();
		if(id==null){
			result.error(Result.error_102);
			return result.toResult();
		}
		JlFrVO jlFr = this.findOne(id);
		
		JlJqVO jlJq = new JlJqVO();
		jlJq.setJqNo(jlFr.getJq());
		List<JlJqVO> jlJqList = jlJqSQL.findList(jlJq);
		if(jlJqList.size()>0){
			jlJq = jlJqList.get(0);
		}
		
		SysUserVO sysUser = TokenUser.getUser();
		
		Date nowDate = new Date();
		
		JlQqCzVO jlQqCz = new JlQqCzVO();
		jlQqCz.setFrNo(jlFr.getFrNo());
		jlQqCz.setFrName(jlFr.getFrName());
		jlQqCz.setJy(jlFr.getJy());
		jlQqCz.setJqNo(jlFr.getJq());
		jlQqCz.setJqName(jlJq.getJqName());
		jlQqCz.setCzsj(nowDate);
		if(jlFr.getQqYe()!=null && jlFr.getQqYe()!=0){
			jlQqCz.setCzje(-jlFr.getQqYe());
		}else{
			jlQqCz.setCzje(0);
		}
		jlQqCz.setCzrNo(sysUser.getUserNo());
		jlQqCz.setCzrName(sysUser.getUserName());
		jlQqCz.setCzzt(1);
		jlQqCz.setCzState(1);
		jlQqCzSQL.add(jlQqCz);
		
		SysLogVO sysLog = new SysLogVO();
		sysLog.setType("正常");
		sysLog.setLogTime(DateUtil.getDefault(nowDate));
		sysLog.setUserName(sysUser.getUserName());
		sysLog.setUserNo(sysUser.getUserNo());
		sysLog.setModel("话费充值");
		sysLog.setInfo("罪犯编号为"+jlFr.getFrNo()+" 罪犯姓名为"+jlFr.getFrName()+"在"+DateUtil.getDefault(nowDate)+"退还"+(jlFr.getQqYe()/1000)+"元");
		sysLog.setOp("话费退还");
		sysLogSQL.add(sysLog);
		
		String qqYe = jlFr.getQqYe()+"";
		
		jlFr.setQqYe(0);
		jlFr.setState(1);
		jlFr.setOuttime(nowDate);
		this.edit(jlFr);
		
		List<String> list = new ArrayList<String>();
		list.add(jlFr.getFrNo());
		list.add(qqYe);
		list.add(jlFr.getFrName());
		list.add(jlJq.getJqName());
		list.add(jlQqCz.getCzId()+"");
		result.putData("czList", list);
		return result.toResult();
	}
	
	public Map<String, Object> findDetailsPojo(String frNo, Integer pageSize, Integer pageNum){
		JlQqCzVO jlQqCz = new JlQqCzVO();
		jlQqCz.setFrNo(frNo);
		return jlQqCzSQL.findPojo(jlQqCz, pageSize, pageNum);
	}
	
	public String requestDetailsUpdate(BigDecimal czId, Integer czje){
		Result result = new Result();
		if(czId==null){
			result.error(Result.error_102);
			return result.toResult();
		}
		JlQqCzVO jlQqCz = jlQqCzSQL.findOne(czId);
		if(jlQqCz==null){
			result.error(Result.error_103, "数据库记录不存在，无法继续操作");
			return result.toResult();
		}
		Date nowDate = new Date();
		Date gDate = DateUtil.addHours(jlQqCz.getCzsj(), 3); //过期时间 充值时间3小时后无法修改
		if(nowDate.before(gDate)){
			SysUserVO sysUser = TokenUser.getUser();
			int oldJe = jlQqCz.getCzje();
			JlFrVO jlFr = new JlFrVO();
			jlFr.setFrNo(jlQqCz.getFrNo());
			List<JlFrVO> jlFrList = jlFrSQL.findList(jlFr);
			if(jlFrList.size()>0){
				jlFr = jlFrList.get(0);
				int nowCzje = czje;
				jlFr.setQqYe(jlFr.getQqYe()+nowCzje-oldJe);
				jlFrSQL.edit(jlFr);
				
				jlQqCz.setCzje(nowCzje);
				jlQqCz.setScrNo(sysUser.getUserNo());
				jlQqCz.setScrName(sysUser.getUserName());
				jlQqCz.setScsj(nowDate);
				jlQqCz.setCzzt(0);
				jlQqCzSQL.edit(jlQqCz);
				
				SysLogVO sysLog = new SysLogVO();
				sysLog.setType("正常");
				sysLog.setLogTime(DateUtil.getDefault(nowDate));
				sysLog.setInfo("罪犯编号为"+jlFr.getFrNo()+" 罪犯姓名为"+jlFr.getFrName()+"在"+DateUtil.getDefault(nowDate)+"充值"+((double)oldJe/1000)+"元修改为"+((double)czje/1000)+"元");
				sysLog.setModel("话费充值");
				sysLog.setOp("修改充值记录");
				sysLog.setUserName(sysUser.getUserName());
				sysLog.setUserNo(sysUser.getUserNo());
				sysLogSQL.add(sysLog);
			}else{
				result.msg("数据库记录已不存在，无法修改");
			}
		}else{
			result.msg("充值已超过3小时，无法修改");
		}
		return result.toResult();
	}
	
	public String setState(JlFrVO model){
		Result result = new Result();
		if(model.getWebid()==null){
			result.error(Result.error_102);
    		return result.toResult();
    	}
    	JlFrVO old = jlFrSQL.findOne(model.getWebid());
    	if(old == null){
    		result.error(Result.error_103, "数据库错误，当前记录已不存在。");
    		return result.toResult();
    	}
    	if(model.getState()==0){
    		if(old.getOuttime()!=null){
    			String sql = "update JL_FR set OutTime=null,state=0 where WebID="+model.getWebid();
    			this.jdbcTemplate.execute(sql);
    		}
    	}else if(model.getState()==1){
    		if(model.getOuttime()==null){
    			model.setOuttime(new Date());
    		}
    		jlFrSQL.edit(model);
    	}
    	return result.toResult();
	}
}
