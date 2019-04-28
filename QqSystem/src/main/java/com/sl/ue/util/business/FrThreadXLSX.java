package com.sl.ue.util.business;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.sl.ue.entity.jl.vo.JlFrVO;
import com.sl.ue.entity.jl.vo.JlJbVO;
import com.sl.ue.entity.jl.vo.JlJqVO;
import com.sl.ue.entity.sys.vo.SysQqServerVO;
import com.sl.ue.service.jl.JlFrService;
import com.sl.ue.service.jl.JlJbService;
import com.sl.ue.service.jl.JlJqService;
import com.sl.ue.service.sys.SysQqServerService;
import com.sl.ue.util.ExcelUtil;
import com.sl.ue.util.component.SpringTool;

/**
 * 说明 [罪犯excel导入线程--后缀为xlsx的excel表格]
 * L_晓天  @2018年12月20日
 */
public class FrThreadXLSX implements Runnable{

	private Thread thread;
	private String path;

	public FrThreadXLSX(String path) {
		this.path = path;
	}
	public void start() {
		if(thread == null){
			thread = new Thread(this, path);
			thread.start();
		}
	}
	@Override
	public void run() {
		JlFrService jlFrSQL = (JlFrService) SpringTool.getBean("jlFrSQL");
		/** servername 开始 */
		SysQqServerService sysQqServerSQL = (SysQqServerService) SpringTool.getBean("sysQqServerSQL");
		List<SysQqServerVO> sysQqServerList = sysQqServerSQL.findList(new SysQqServerVO(),null,null,"ASC");
		String serverName = "Server1";
		if(sysQqServerList.size()>0)serverName = sysQqServerList.get(0).getServerName();
		/** servername 结束 */
		
		/** 监区 开始 */
		JlJqService jlJqSQL = (JlJqService) SpringTool.getBean("jlJqSQL");
		List<JlJqVO> jlJqList = jlJqSQL.findList(new JlJqVO());
		/** 监区 结束 */
		
		/** 级别 开始 */
		JlJbService jlJbSQL = (JlJbService) SpringTool.getBean("jlJbSQL");
		List<JlJbVO> jlJbList = jlJbSQL.findList(new JlJbVO());
		/** 级别 结束 */
		
		XSSFWorkbook workbook = null;
		try {
			workbook = new XSSFWorkbook(new FileInputStream(path));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		int sheets = workbook.getNumberOfSheets();
		for(int i=0;i<sheets;i++){
			XSSFSheet sheet = workbook.getSheetAt(i); // 读取工作表
			if(sheet == null){
				break;
			}
			
			int index = 1;
			while (true) {
				XSSFRow row = sheet.getRow(index);
				if(row == null){
					break;
				}
				// 0罪犯编号，1罪犯姓名，2罪犯卡号  3监区名称，4分管等级，5电话卡号  6电话密码
				JlFrVO jlFr = new JlFrVO();
				String frNo = ExcelUtil.getCellValue(row.getCell(0)); //0罪犯编号
				jlFr.setFrNo(frNo!=null?frNo.trim():null);
				if(StringUtils.isNotBlank(frNo)){
					List<JlFrVO> list = jlFrSQL.findList(jlFr);
					if(list.size()>0){
					}else{
						jlFr.setFrName(ExcelUtil.getCellValue(row.getCell(1))); //1姓名
						jlFr.setJy(serverName);
						jlFr.setFrCard(ExcelUtil.getCellValue(row.getCell(2))); //2罪犯卡号
						String jq = ExcelUtil.getCellValue(row.getCell(3)); //3监区名称
						jlFr.setJq("-1");
						jlFr.setJqName("未定义");
						for(JlJqVO t : jlJqList){
							if(StringUtils.isNotBlank(jq) && jq.equals(t.getJqName())){
								jlFr.setJq(t.getJqNo());
								jlFr.setJqName(t.getJqName());
							}
						}
						String jb = ExcelUtil.getCellValue(row.getCell(4)); //4分管等级
						jlFr.setJbNo("-1");
						jlFr.setJbName("未定义");
						for(JlJbVO t : jlJbList){
							if(StringUtils.isNotBlank(jb) && jb.equals(t.getJbName())){
								jlFr.setJbNo(t.getJbNo());
								jlFr.setJbName(t.getJbName());
							}
						}
						jlFr.setQqZh(ExcelUtil.getCellValue(row.getCell(5))); //5电话卡号
						jlFr.setQqMm(ExcelUtil.getCellValue(row.getCell(6))); //6电话密码
						
						jlFrSQL.add(jlFr);
					}
				}else{
					break;
				}
				index++;
			}
		}
		
		//最后删除文件
		File file = new File(path);
		if(file.exists() && file.isFile())file.delete();
	}

}
