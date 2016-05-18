package com.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

public class FileManager {
	
	// 파일 다운로드
	// saveFileName : 서버에 저장된 파일명
	// originalFileName : 클라이언트가 업로드한 파일명
	// path : 서버에 저장된 실제 경로
	
	public static boolean doFileDownload(HttpServletResponse response, 
			String saveFileName, /*String originalFileName, */String path){
		
		try {
			
			String filePath = path + File.separator + saveFileName;
			
			/*if(originalFileName == null || originalFileName.equals(""))
				originalFileName = saveFileName;*/
			
			// 한글 이름의 파일을 다운받을 때 파일 이름을 깨지지 않게 함
			saveFileName = new String(saveFileName.getBytes("euc-kr"),"ISO-8859-1");
			
			File f = new File(filePath);
			
			if(!f.exists())
				return false;
			
			response.setContentType("application/octet-stream");
			response.setHeader("Content-disposition", "attachment;filename=" + saveFileName);
			
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
			
			OutputStream out = response.getOutputStream();
			
			int n;
			
			byte bytes[] = new byte[4096];
			
			while((n=bis.read(bytes, 0, 4096))!=-1){
				
				out.write(bytes,0,n);
			}
			
			out.flush();
			out.close();
			bis.close();
			
		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
		
		return true;
	}
	
	// 파일 삭제
	public static void doFileDelete(String fileName, String path){
		
		try {
			
			String filePath = path + File.separator + fileName;
			
			File f = new File(filePath);
			
			if(f.exists())
				f.delete();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}



















