package com.board;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



import javax.servlet.jsp.JspWriter;

import org.apache.jasper.tagplugins.jstl.core.Out;

import com.member.CustomInfo;
import com.member.MemberDAO;
import com.member.MemberDTO;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.util.DBCPConn;
import com.util.FileManager;
import com.util.MyUtil;

public class BoardServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	protected void forward(HttpServletRequest req, HttpServletResponse resp,
			String url)	throws ServletException, IOException {
		
		RequestDispatcher rd = req.getRequestDispatcher(url);
		rd.forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");
		
		Connection conn = DBCPConn.getConnection();
		BoardDAO dao = new BoardDAO(conn);
		MemberDAO dao1 = new MemberDAO(conn);
		
		MyUtil myUtil = new MyUtil();
		
		// http://192.168.16.8:8080/study/sboard/created.do
		String cp = req.getContextPath();	// http://192.168.16.8:8080/study
		String uri = req.getRequestURI();	// /sboard/created.do
		
		String url;	// forward �ּ�
		
		// ������ ������ ���
		String root = getServletContext().getRealPath("/");
		String path = root + File.separator + "contents";
		
		if(uri.indexOf("main.do") != -1){
			
			//�̹��� �ҷ��ö�� ��� ����
			
			
			String imagePath= cp + "/contents/";
			
			req.setAttribute("imagePath",imagePath);
			//
			
			
	        System.out.println("����ο���?");
	         HttpSession session = req.getSession();
	         CustomInfo info = (CustomInfo)session.getAttribute("customInfo");
	         MemberDTO dto1;
	         if(info !=null){
	         dto1 = dao1.getReadData(info.getUserId());   
	            req.setAttribute("dto1", dto1);
	         }
			
			//�ڰ� ��¿�
			List<BoardDTO> freeForum = dao.getListData("freeForum");
			
			req.setAttribute("freeForum", freeForum);
			//�ڰ� ��¿�
			
	
			//to��
			
			
			List<BoardDTO> top = dao.mainTop100getLists();
			
			req.setAttribute("top", top);
			
			//to��
			
			
			//������
			
			List<BoardDTO> notice = dao.getListNotice("notice");
	         req.setAttribute("notice", notice);
			//
			
			
			int length = 6;
			
			req.setAttribute("length", length);
			
			
			url = "/board/main.jsp";
			forward(req, resp, url);
			
		}else if(uri.indexOf("write.do") != -1){
			
			
			
		    HttpSession session = req.getSession();
	         CustomInfo info = (CustomInfo)session.getAttribute("customInfo");
	         MemberDTO dto1;
	         if(info !=null){
	         dto1 = dao1.getReadData(info.getUserId());   
	            req.setAttribute("dto1", dto1);
	         }
			String pageNum = req.getParameter("pageNum");
			String board = req.getParameter("board");
			if(board.equals("threeDMovie"))
				board = "threeDMovie";
				
			String searchKey = req.getParameter("searchKey");
			String searchValue = req.getParameter("searchValue");
			
			String boardTitle = "";

			if(board.equals("notice"))
				boardTitle = "��������";
			else if(board.equals("TOP100"))
				boardTitle = "TOP100";
			else if(board.equals("newMovie"))
				boardTitle = "�ֽſܱ���ȭ";
			else if(board.equals("oldMovie"))
				boardTitle = "�����ܱ���ȭ";
			else if(board.equals("korMovie"))
				boardTitle = "�ѱ� ��ȭ";
			else if(board.equals("hdMovie"))
				boardTitle = "DVD ��ȭ�� ��ȭ";
			else if(board.equals("threeDMovie"))
				boardTitle = "3D ��ȭ";
			else if(board.equals("korTV"))
				boardTitle = "�ѱ� TV";
			else if(board.equals("forTV"))
				boardTitle = "�ܱ� TV";
			else if(board.equals("aniOver"))
				boardTitle = "�ִϸ��̼� (����)";
			else if(board.equals("aniOn"))
				boardTitle = "�ִϸ��̼� (�濵��)";
			else if(board.equals("game"))
				boardTitle = "����";
			else if(board.equals("comnov"))
				boardTitle = "��ȭ/�Ҽ�";
			else if(board.equals("util"))
				boardTitle = "��ƿ";
			else if(board.equals("korMusic"))
				boardTitle = "�ѱ� ����";
			else if(board.equals("forMusic"))
				boardTitle = "�ܱ� ����";
			else if(board.equals("reviewForum"))
				boardTitle = "���� �ı�";
			else if(board.equals("requestForum"))
				boardTitle = "��û �Խ���";
			else if(board.equals("freeForum"))
				boardTitle = "���� �Խ���";
			
			String params = "?board=" + board;
			if(pageNum != null)
				params += "&pageNum=" + pageNum;
			if(searchKey != null)
				params += "&searchKey=" + searchKey;
			if(searchValue != null)
				params += "&searchValue=" + searchValue;
			
			/*HttpSession session = req.getSession();
			
			CustomInfo info = (CustomInfo)session.getAttribute("customInfo");
			
			if(info == null){
				
				url = "/member/login.jsp";
				forward(req, resp, url);
				return;
			}*/

			req.setAttribute("boardTitle", boardTitle);
			req.setAttribute("params", params);
			req.setAttribute("board", board);
			
			url = "/board/write.jsp";
			forward(req, resp, url);
			
		}else if(uri.indexOf("created_ok.do") != -1){
			
			System.out.println("�ɴϱ�?");
			String pageNum = req.getParameter("pageNum");
			String board = req.getParameter("board");
			String category = req.getParameter("category");
			String searchKey = req.getParameter("searchKey");
			String searchValue = req.getParameter("searchValue");
			
			if(board.equals("threeDMovie"))
				board = "threeDMovie";
			
			int num = (dao.getMaxNum(board)+1);
			
			if(searchValue != null)
				searchValue = URLDecoder.decode(searchValue, "UTF-8");
			
			
				
			System.out.println(board);
			String params = "?board=" + board;
			if(category != null)
				params += "&category=" + category;
			if(pageNum != null)
				params += "&pageNum=" + pageNum;
			if(searchKey != null)
				params += "&searchKey=" + searchKey;
			if(searchValue != null)
				params += "&searchValue=" + searchValue;
			
			// ���� ���ε�
			String encType = "UTF-8";
			int maxFileSize = 10*1024*1024;		// 10GB
			
			System.out.println("num" + num);
			path += File.separator + board + File.separator + num;
			
			File f = new File(path);
			if(!f.exists())
				f.mkdirs();
			
			MultipartRequest mr = new MultipartRequest(req, path, maxFileSize, encType, new DefaultFileRenamePolicy());
			
			// DB�� ����
			if(mr.getFile("upload") != null){
				System.out.println("���ε� ���� ã��");
				int maxNum = dao.getMaxNum(board);
							
				BoardDTO dto = new BoardDTO();
							
				dto.setNum(maxNum + 1);
				dto.setBoard(req.getParameter("board"));
				dto.setSubject(mr.getParameter("subject"));
				dto.setUserId(mr.getParameter("userId"));
				dto.setCategory(mr.getParameter("category"));
				dto.setContent(mr.getParameter("content"));
				dto.setSaveFileName(mr.getFilesystemName("upload"));
				dto.setSavePicture(mr.getFilesystemName("image"));
				
				dao.insertData(dto, board);
			}else{
				
				int maxNum = dao.getMaxNum(board);
				
				BoardDTO dto = new BoardDTO();
							
				dto.setNum(maxNum + 1);
				dto.setBoard(req.getParameter("board"));
				dto.setSubject(mr.getParameter("subject"));
				dto.setUserId(mr.getParameter("userId"));
				dto.setCategory(mr.getParameter("category"));
				dto.setContent(mr.getParameter("content"));
				dto.setSaveFileName(mr.getFilesystemName("upload"));
				dto.setSavePicture(mr.getFilesystemName("image"));
				dao.insertData(dto, board);
				
			}
			
			
			// �ٿ�ε�� ����Ʈ ����
			HttpSession session = req.getSession();
			CustomInfo info = (CustomInfo)session.getAttribute("customInfo");
			
			MemberDTO dto1 = dao1.getReadData(info.getUserId());
			//����Ʈ ���� ��ų �� ���ϱ�
			String point =Integer.toString((Integer.parseInt(dto1.getUserPoint())+5));
			
			dao1.updatePoint(info.getUserId(), point);
			
			
			url = cp + "/sboard/board.do?board=" + board + "&num=" + num;
			resp.sendRedirect(url);
			
		}else if(uri.indexOf("board.do") != -1){
			
			HttpSession session = req.getSession();

	         CustomInfo info = (CustomInfo)session.getAttribute("customInfo");
	         MemberDTO dto1;
	         if(info !=null){
	         dto1 = dao1.getReadData(info.getUserId());   
	            req.setAttribute("dto1", dto1);
	         }
			
			String pageNum = req.getParameter("pageNum");
			String board = req.getParameter("board");
			if(board.equals("threeDMovie"))
				board = "threeDMovie";
				
			String category = req.getParameter("category");
			String searchKey = req.getParameter("searchKey");
			String searchValue = req.getParameter("searchValue");
			
			if(searchKey==null){
				searchKey = "subject";
				searchValue = "";
				
			}else{
				if(req.getMethod().equalsIgnoreCase("get"))
					searchValue = URLDecoder.decode(searchValue, "UTF-8");
			}
			
			if(category==null){
				category = "";
			}else{
				if(req.getMethod().equalsIgnoreCase("get"))
					category = URLDecoder.decode(category, "UTF-8");
			}
	
			
			String boardTitle = "";

				if(board.equals("notice"))
					boardTitle = "��������";
				else if(board.equals("TOP100"))
					boardTitle = "TOP100";
				else if(board.equals("newMovie"))
					boardTitle = "�ֽſܱ���ȭ";
				else if(board.equals("oldMovie"))
					boardTitle = "�����ܱ���ȭ";
				else if(board.equals("korMovie"))
					boardTitle = "�ѱ� ��ȭ";
				else if(board.equals("hdMovie"))
					boardTitle = "DVD ��ȭ�� ��ȭ";
				else if(board.equals("threeDMovie"))
					boardTitle = "3D ��ȭ";
				else if(board.equals("korTV"))
					boardTitle = "�ѱ� TV";
				else if(board.equals("forTV"))
					boardTitle = "�ܱ� TV";
				else if(board.equals("aniOver"))
					boardTitle = "�ִϸ��̼� (����)";
				else if(board.equals("aniOn"))
					boardTitle = "�ִϸ��̼� (�濵��)";
				else if(board.equals("game"))
					boardTitle = "����";
				else if(board.equals("comnov"))
					boardTitle = "��ȭ/�Ҽ�";
				else if(board.equals("util"))
					boardTitle = "��ƿ";
				else if(board.equals("korMusic"))
					boardTitle = "�ѱ� ����";
				else if(board.equals("forMusic"))
					boardTitle = "�ܱ� ����";
				else if(board.equals("reviewForum"))
					boardTitle = "���� �ı�";
				else if(board.equals("requestForum"))
					boardTitle = "��û �Խ���";
				else if(board.equals("freeForum"))
					boardTitle = "���� �Խ���";
			
			int currentPage = 1;
			
			if(pageNum!=null && !pageNum.equals(""))
				currentPage = Integer.parseInt(pageNum);
			
			int dataCount;
			// ��ü ������ ����
			if(board.equalsIgnoreCase("TOP100")){
				dataCount = dao.top100getDataCount(searchKey, searchValue, category);
			}else{
				dataCount = dao.getDataCount(searchKey, searchValue, board, category);
			}
			
			// ��ü ������ ��
			int numPerPage = 25;
			int totalPage = myUtil.getPageCount(numPerPage, dataCount);
			
			// ��ü �������� ���� ���������� ū ���
			if(currentPage > totalPage)
				currentPage = totalPage;
			
			// ������ rownum�� start�� end
			int start = (currentPage-1)*numPerPage + 1;
			int end = currentPage*numPerPage;
			
			System.out.println("BOARDȮ�� : "  + board);
			List<BoardDTO> lists;
			if(board.equalsIgnoreCase("TOP100")){
				lists = dao.top100getLists(start, end, searchKey, searchValue, category);
			}else{
				lists = dao.getLists(start, end, searchKey, searchValue, board, category);
			}
			
			String listUrlParams = "?board=" + board;
			if(category != null)
				listUrlParams += "&category=" + URLEncoder.encode(category, "UTF-8");
			if(searchValue != null)
				listUrlParams += "&searchKey=" + searchKey;
				listUrlParams += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
			
			String listUrl = cp + "/sboard/board.do" + listUrlParams;
						
			String pageIndexList = myUtil.pageIndexList(currentPage, totalPage, listUrl);
			
			String params = "?board=" + board;
			if(category != null)
				params += "&category=" + category;
			if(pageNum != null)
				params += "&pageNum=" + pageNum;
			if(searchKey != null)
				params += "&searchKey=" + searchKey;
			if(searchValue != null)
				params += "&searchValue=" + searchValue;
			
			String downloadPath = cp + "/sboard/download.do";
			String deletePath = cp + "/sboard/delete.do";
			String imagePath = cp + "/torrent/saveFile";
						
			// ������ �������� �ѱ� ������
			req.setAttribute("imagePath", imagePath);
			req.setAttribute("downloadPath", downloadPath);
			req.setAttribute("deletePath", deletePath);
						
			req.setAttribute("boardTitle", boardTitle);
			req.setAttribute("lists", lists);
			req.setAttribute("dataCount", dataCount);
			req.setAttribute("pageIndexList", pageIndexList);
			req.setAttribute("board", board);
			req.setAttribute("category", category);
			req.setAttribute("pageNum", pageNum);
			req.setAttribute("searchKey", searchKey);
			req.setAttribute("searchValue", searchValue);
			req.setAttribute("params", params);
			
			url = "/board/board.jsp";
			forward(req, resp, url);
			
		}else if(uri.indexOf("view.do") != -1){
	         
	         int num = Integer.parseInt(req.getParameter("num"));
	         String pageNum = req.getParameter("pageNum");
	         String board = req.getParameter("board");
	         String category = req.getParameter("category");
	         String searchKey = req.getParameter("searchKey");
	         String searchValue = req.getParameter("searchValue");
	         String virtualBoard = req.getParameter("virtualBoard");
	         
	         String boardTitle = "";

	         if(board.equals("notice"))
	            boardTitle = "��������";
	         else if(board.equals("TOP100"))
	            boardTitle = "TOP100";
	         else if(board.equals("newMovie"))
	            boardTitle = "�ֽſܱ���ȭ";
	         else if(board.equals("oldMovie"))
	            boardTitle = "�����ܱ���ȭ";
	         else if(board.equals("korMovie"))
	            boardTitle = "�ѱ� ��ȭ";
	         else if(board.equals("hdMovie"))
	            boardTitle = "DVD ��ȭ�� ��ȭ";
	         else if(board.equals("threeDMovie"))
	            boardTitle = "3D ��ȭ";
	         else if(board.equals("korTV"))
	            boardTitle = "�ѱ� TV";
	         else if(board.equals("forTV"))
	            boardTitle = "�ܱ� TV";
	         else if(board.equals("aniOver"))
	            boardTitle = "�ִϸ��̼� (����)";
	         else if(board.equals("aniOn"))
	            boardTitle = "�ִϸ��̼� (�濵��)";
	         else if(board.equals("game"))
	            boardTitle = "����";
	         else if(board.equals("comnov"))
	            boardTitle = "��ȭ/�Ҽ�";
	         else if(board.equals("util"))
	            boardTitle = "��ƿ";
	         else if(board.equals("korMusic"))
	            boardTitle = "�ѱ� ����";
	         else if(board.equals("forMusic"))
	            boardTitle = "�ܱ� ����";
	         else if(board.equals("reviewForum"))
	            boardTitle = "���� �ı�";
	         else if(board.equals("requestForum"))
	            boardTitle = "��û �Խ���";
	         else if(board.equals("freeForum"))
	            boardTitle = "���� �Խ���";
	         
	         if(searchValue != null)
	            searchValue = URLDecoder.decode(searchValue, "UTF-8");
	         
	         String params = "";
	         if(virtualBoard == null)
	            params = "?board=" + board;
	         else if(virtualBoard.equals("TOP100"))
	            params = "?board=" + virtualBoard;
	         if(category != null)
	            params += "&category=" + category;
	         if(pageNum != null)
	            params += "&pageNum=" + pageNum;
	         if(searchKey != null)
	            params += "&searchKey=" + searchKey;
	         if(searchValue != null)
	            params += "&searchValue=" + URLDecoder.decode(searchValue, "UTF-8");
	         
	         BoardDTO dto = dao.getReadData(num, board);
	         
	         if(dto == null){
	            
	            url = cp + "/sboard/board.do";
	            resp.sendRedirect(url);
	         }
	         
	         HttpSession session = req.getSession();
	         
	         CustomInfo info = (CustomInfo)session.getAttribute("customInfo");
	         MemberDTO dto1 = dao1.getReadData(info.getUserId());
	         
	         if(dto1!=null)      
	            req.setAttribute("dto1", dto1);

	         if( !Integer.toString(num).equals
	               ((String)session.getAttribute(info.getUserId() +"_"+ board +"_"+ Integer.toString(num))) ){ 
	   
	            System.out.println(session.getAttribute(board+"_"+num));
	            session.setAttribute(info.getUserId() +"_"+ board +"_"+ Integer.toString(num), Integer.toString(num)); 
	   
	            dao.updateHitCount(num, board);
	         } 
	         
	         //���ο� ���ú� �Խù��� ����ϱ� ���� ��Ű �߰�
	         String cookieSubject = dao.raw(dto.getSubject(),15);

	         Cookie c = new Cookie(URLEncoder.encode(cookieSubject, "UTF-8"),URLEncoder.encode("?board="+dto.getBoard()+"&num="+dto.getNum(), "UTF-8"));
	         
	         c.setMaxAge(60*60*24);
	         c.setPath("/");  
	         
	         resp.addCookie(c);
	      
	         //���ο� ���ú� �Խù��� ����ϱ� ���� ��Ű �߰�
	         
	         
	         // ���
	         
	         List<BoardDTO> lists = dao.getReadReply(num, board);
	         req.setAttribute("lists", lists);
	         int totalCount = dao.getReplyCount(num,board);
	         // ���
	         dto.setContent(dto.getContent().replaceAll("\n", "<br/>"));
	         path += File.separator + board + File.separator + dto.getNum();
	         String imagePath= cp + "/contents/"+board+"/"+dto.getNum();
	         String downloadPath = cp + "/sboard/download.do";
	         String deletePath = cp + "/sboard/delete.do";

	         req.setAttribute("totalCount", totalCount);
	         req.setAttribute("imagePath", imagePath);
	         req.setAttribute("boardTitle", boardTitle);
	         req.setAttribute("downloadPath", downloadPath);
	         req.setAttribute("deletePath", deletePath);
	         req.setAttribute("board", board);
	         req.setAttribute("virtualBoard", virtualBoard);
	         req.setAttribute("category", category);
	         req.setAttribute("pageNum", pageNum);
	         req.setAttribute("searchKey", searchKey);
	         req.setAttribute("searchValue", searchValue);
	         req.setAttribute("dto", dto);
	         req.setAttribute("params", params);
	         
	         url = "/board/view.jsp";
	         forward(req, resp, url);
	         return;
	         
	      }else if(uri.indexOf("updated.do") != -1){
	    	  
	    	  HttpSession session = req.getSession();
		         CustomInfo info = (CustomInfo)session.getAttribute("customInfo");
		         MemberDTO dto1;
		         if(info !=null){
		         dto1 = dao1.getReadData(info.getUserId());   
		            req.setAttribute("dto1", dto1);
		         }
	    	  
			
			int num = Integer.parseInt(req.getParameter("num"));
			String pageNum = req.getParameter("pageNum");
			String board = req.getParameter("board");
			String category = req.getParameter("category");
			String searchKey = req.getParameter("searchKey");
			String searchValue = req.getParameter("searchValue");
			String virtualBoard = req.getParameter("virtualBoard");
			
			if(searchKey==null){
				searchKey = "subject";
				searchValue = "";
				
			}else{
				if(req.getMethod().equalsIgnoreCase("get"))
					searchValue = URLDecoder.decode(searchValue, "UTF-8");
			}
			if(category==null){
				category = "";
			}
			
			BoardDTO dto = dao.getReadData(num, board);
			
			if(dto==null){
				
				url = cp + "/sboard/board.do?board=" + board;
				resp.sendRedirect(url);
			}
			
			String params = "?board=" + board;
			if(category != null)
				params += "&category=" + category;
			if(pageNum != null)
				params += "&pageNum=" + pageNum;
			if(searchKey != null)
				params += "&searchKey=" + searchKey;
			if(searchValue != null)
				params += "&searchValue=" + searchValue;

			req.setAttribute("board", board);
			req.setAttribute("num", num);
			req.setAttribute("dto", dto);
			req.setAttribute("params", params);
			req.setAttribute("pageNum", pageNum);
			
			url = "/board/update.jsp";
			forward(req, resp, url);
			
		}else if(uri.indexOf("updated_ok.do") != -1){
			
			
			 HttpSession session = req.getSession();
	         CustomInfo info = (CustomInfo)session.getAttribute("customInfo");
	         MemberDTO dto1;
	         if(info !=null){
	         dto1 = dao1.getReadData(info.getUserId());   
	            req.setAttribute("dto1", dto1);
	         }
			
			int num = Integer.parseInt(req.getParameter("num"));
			String pageNum = req.getParameter("pageNum");
			String board = req.getParameter("board");
			String category = req.getParameter("category");
			String searchKey = req.getParameter("searchKey");
			String searchValue = req.getParameter("searchValue");
			String virtualBoard = req.getParameter("virtualBoard");	
			
			String encType = "UTF-8";
			int maxFileSize = 10*1024*1024;	

			path += "/" + board + "/" + num;	
			
			MultipartRequest mr = new MultipartRequest(req, path, maxFileSize, encType, new DefaultFileRenamePolicy());
			
			BoardDTO dto = new BoardDTO();
			
			dto.setNum(Integer.parseInt(req.getParameter("num")));
			dto.setBoard(req.getParameter("board"));
			dto.setSubject(mr.getParameter("subject"));
			dto.setCategory(mr.getParameter("category"));
			dto.setContent(mr.getParameter("content"));
			dto.setSaveFileName(mr.getFilesystemName("upload"));
			dto.setSavePicture(mr.getFilesystemName("image"));
			
			dao.updatedData(dto, board);
			
			String params = "?board=" + board;
			if(category != null)
				params += "&category=" + category;
			if(pageNum != null)
				params += "&pageNum=" + pageNum;
			if(searchKey != null)
				params += "&searchKey=" + searchKey;
			if(searchValue != null)
				params += "&searchValue=" + searchValue;
			
			url = cp + "/sboard/board.do" + params;
			resp.sendRedirect(url);
			
		}else if(uri.indexOf("delete.do") != -1){
			
			int num = Integer.parseInt(req.getParameter("num"));
			String pageNum = req.getParameter("pageNum");
			String board = req.getParameter("board");
			String category = req.getParameter("category");
			String searchKey = req.getParameter("searchKey");
			String searchValue = req.getParameter("searchValue");
			String virtualBoard = req.getParameter("virtualBoard");
			
			String params = "";
			if(virtualBoard == null)
				params = "?board=" + board;
			else if(virtualBoard.equals("TOP100"))
				params = "?board=" + virtualBoard;
			
			if(category != null)
				params += "&category=" + category;
			if(pageNum != null)
				params += "&pageNum=" + pageNum;
			if(searchKey != null)
				params += "&searchKey=" + searchKey;
			if(searchValue != null)
				params += "&searchValue=" + searchValue;
			
			BoardDTO dto = dao.getReadData(num, board);
			
			path += "/" + board + "/" + num;
			
			// ���� ����
			FileManager.doFileDelete(dto.getSaveFileName(), path);
			
			dao.deleteData(num, board);
			
			url = cp + "/sboard/board.do" + params;
			resp.sendRedirect(url);
			
		}else if(uri.indexOf("download.do") != -1){
			
			
		
			int num = Integer.parseInt(req.getParameter("num"));
			String pageNum = req.getParameter("pageNum");
			String board = req.getParameter("board");
			String category = req.getParameter("category");
			String searchKey = req.getParameter("searchKey");
			String searchValue = req.getParameter("searchValue");
			
			path += "/" + board + "/" + num;
			
			String params = "?board=" + board;
			if(category != null)
				params += "&category=" + category;
			if(pageNum != null)
				params += "&pageNum=" + pageNum;
			if(searchKey != null)
				params += "&searchKey=" + searchKey;
			if(searchValue != null)
				params += "&searchValue=" + searchValue;
			params += "&num=" + num;
			
			
			
			HttpSession session = req.getSession();
			CustomInfo info = (CustomInfo)session.getAttribute("customInfo");
			
			MemberDTO dto1 = dao1.getReadData(info.getUserId());
			
			int nowPoint = Integer.parseInt(dto1.getUserPoint());
			
			if(nowPoint<10){
				System.out.println("�ٿ�ε忡��");
				
				url = cp + "/sboard/view.do" + params;
				resp.sendRedirect(url);
				
				return;
			}
	
			BoardDTO dto = dao.getReadData(num, board);
			
			if(dto==null)
				return;
			
			boolean flag = FileManager.doFileDownload(resp, 
					dto.getSaveFileName(), path);
	
			if(flag==false){
				
				url = cp + "/sboard/view.do" + params;
				resp.sendRedirect(url);
				
				return;
			}
			
			String point =Integer.toString((Integer.parseInt(dto1.getUserPoint())-10));
			
			dao1.updatePoint(info.getUserId(), point);
			
			url = cp + "/sboard/view.do" + params;
			resp.sendRedirect(url);
			
		}else if(uri.indexOf("logout.do")!=-1) {//�α׾ƿ�!
			
			System.out.println("log.do");
			HttpSession session = req.getSession();
			session.removeAttribute("customInfo");
			
			
			
			System.out.println("logout.do");
			url = cp+"/sboard/main.do";
			resp.sendRedirect(url);
			return;
			
		}else if(uri.indexOf("control.do")!=-1) { //������ ������
			
			
			System.out.println("������ ���ϴ�~");
			
			//������
			
			List<BoardDTO> notice = dao.getListNotice("notice");
	         req.setAttribute("notice", notice);
			//
			
			//ȸ������ ����--------------------------------------------------------------
			String pageNum = req.getParameter("pageNum");
			String searchValue = req.getParameter("searchValue");
			
			if(searchValue==null){
				searchValue= "";
			}
			
			System.out.println(searchValue);
			System.out.println("pageNum : "  + pageNum);
		
			if(searchValue!=null){
				if(req.getMethod().equalsIgnoreCase("GET"))
					searchValue = URLDecoder.decode(searchValue, "UTF-8");
			}
			int currentPage = 1;
			
			if(pageNum!=null)
				currentPage = Integer.parseInt(pageNum);
			
			// ��ü ������ ����
			int dataCount = dao1.getDataCount(searchValue);
			
			
			System.out.println("dataCount : " + dataCount);
			// ��ü ������ ��
			int numPerPage = 15;
			int totalPage = myUtil.getPageCount(numPerPage, dataCount);
			
			// ��ü �������� ���� ���������� ū ���
			if(currentPage > totalPage)
				currentPage = totalPage;
			
			// ������ rownum�� start�� end
			int start = (currentPage-1)*numPerPage + 1;
			int end = currentPage*numPerPage;
			
			
			String params = "";
			
			System.out.println("params : " +params);
			if(searchValue != null)
			 params = "?searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
			
			String listUrl = cp + "/sboard/control.do" + params;
						
			String pageIndexList = myUtil.pageIndexList(currentPage, totalPage, listUrl);
			
			if(pageNum != null)
				params += "&pageNum=" + currentPage;
			
			req.setAttribute("pageIndexList", pageIndexList);
			
			
			
			System.out.println("searchValue 1 : " + searchValue);
			
			if(searchValue==null){
				searchValue= "";
			}
			System.out.println("searchValue : " + searchValue);
			System.out.println("start : "  + start);
			System.out.println("end : " + end);
			ArrayList<MemberDTO> lists= dao1.getList(start, end, searchValue);	
			//ȸ������ ����----------------------------------------------------------------------------
			
			//���ǿ��� ���̵� Ȯ���ؼ� �ѷ��� ������ ���� ����
			HttpSession session = req.getSession();
			System.out.println("����");
			CustomInfo info = (CustomInfo)session.getAttribute("customInfo");
			String userId = req.getParameter("userId");
			
			if(userId!=null){
				
				req.setAttribute("userId", userId);
				
			}
						
			if(info!=null){
			
			System.out.println("������ Ȯ���ϱ�~");
			MemberDTO dto1 = dao1.getReadData(info.getUserId());
			req.setAttribute("dto1", dto1);
			}
			
			//���ǿ��� ���̵� Ȯ���ؼ� �ѷ��� ������ ���� ��
			
			
			//�Ű�Խù� Ȯ��
			String pageNum1 = req.getParameter("pageNum1");
			String board = req.getParameter("board");
			String reportKey = req.getParameter("reportKey");
			String reportValue = req.getParameter("reportValue");
			String rnum= req.getParameter("rnum");
			String num = req.getParameter("num");
			
			System.out.println(reportKey);
			System.out.println(reportValue);
			if(reportKey==null||reportKey.equals("non")){
				reportKey = "subject";
				reportValue = "";
				
			}else {
				if(req.getMethod().equalsIgnoreCase("GET"))
					reportValue = URLDecoder.decode(reportValue, "UTF-8");
			}
			
			int reportPage = 1;
			
			if(pageNum1!=null)
				reportPage = Integer.parseInt(pageNum1);
			
			// ��ü ������ ����
			int reportCount = dao.reportgetDataCount(reportKey, reportValue);
			
			// ��ü ������ ��
			int reportnumPerPage = 6;
			int reporttotalPage = myUtil.getPageCount(reportnumPerPage, reportCount);
			
			// ��ü �������� ���� ���������� ū ���
			if(reportPage > reporttotalPage)
				reportPage = reporttotalPage;
			
			// ������ rownum�� start�� end
			int reportstart = (reportPage-1)*reportnumPerPage + 1;
			int reportend = reportPage*reportnumPerPage;
			
			ArrayList<BoardDTO> report = dao.reportgetLists(reportstart, reportend, reportKey, reportValue);
			
			
			if(params==null || params==""){
				params = "?board=" + board;
			}else if(params!=null && board!=null)
				params = "&board=" + board;
			
			if(reportKey != null)
				params += "&reportKey=" + reportKey;
			if(reportValue != null)
				params += "&reportValue=" + URLEncoder.encode(reportValue, "UTF-8");
			
			String reportlistUrl = cp + "/sboard/control.do" + params;
						
			String reportIndexList = myUtil.reportPageIndexList(reportPage, reporttotalPage, reportlistUrl);
			
			if(pageNum1 != null)
				params += "&pageNum1=" + reportPage;
			
			
			//�Ű�
			
			
			
			
			
			lists.trimToSize();
			report.trimToSize();
			int list = lists.size(); //ȸ������ ũ��
			int repostsize = report.size();
			
			int max = 15;
			int remax = 6;
			
			req.setAttribute("repostsize", repostsize);
			req.setAttribute("reportValue", reportValue);
			req.setAttribute("reportKey", reportKey);
			req.setAttribute("report", report);
			req.setAttribute("reportPage", reportPage);
			req.setAttribute("reportIndexList", reportIndexList);
			req.setAttribute("remax", remax);
			req.setAttribute("num", num);
			req.setAttribute("rnum", rnum);
			
			req.setAttribute("params", params);
			req.setAttribute("searchValue", searchValue);
			req.setAttribute("currentPage", currentPage);
			req.setAttribute("max", max);
			
			req.setAttribute("list", list);
			
			req.setAttribute("lists", lists);
			
			url = "/board/control.jsp";
			forward(req, resp, url);
			return;
		}else if(uri.indexOf("report.do")!=-1){ // �Ű���
			
			System.out.println("report����");
			int num = Integer.parseInt(req.getParameter("num"));
			String pageNum = req.getParameter("pageNum");
			String board = req.getParameter("board");
			String category = req.getParameter("category");
			String searchKey = req.getParameter("searchKey");
			String searchValue = req.getParameter("searchValue");
			String virtualBoard = req.getParameter("virtualBoard");
			
			
			
			System.out.println(num);
			System.out.println(board);
			
			dao.upDateReport(num, board, 1);
			
			
			String params = "?board=" + board;
			if(pageNum != null)
				params += "&pageNum=" + pageNum;
			if(searchKey != null)
				params += "&searchKey=" + searchKey;
			if(searchValue != null)
				params += "&searchValue=" + searchValue;
			
			
			
			url = cp + "/sboard/view.do"+params+"&num="+num;
			resp.sendRedirect(url);
		
		
		
		}else if(uri.indexOf("handling.do")!=-1){ //�Ű� �ϸ� ����ε� ó���ϰԸ����
			
			System.out.println("handling����");
			int num = Integer.parseInt(req.getParameter("num"));
			String pageNum = req.getParameter("pageNum");
			String pageNum1 = req.getParameter("pageNum1");
			String board = req.getParameter("board");
			String searchValue = req.getParameter("searchValue");
			String reportKey = req.getParameter("reportKey");
			String reportValue = req.getParameter("reportValue");
			int reportnum = Integer.parseInt(req.getParameter("reportnum"));
			
			
			System.out.println("board : " + board);
			
			String params = "";
			if(searchValue != null)
				 params = "?searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
			
			if(pageNum!=null)
				params = "&pageNum=" + pageNum;
			
			
			if(reportKey==null){
				reportKey = "subject";
				reportValue = "";
				
			}else {
				if(req.getMethod().equalsIgnoreCase("GET"))
					reportValue = URLDecoder.decode(reportValue, "UTF-8");
			}
			
			if(reportKey != null)
				params += "&reportKey=" + reportKey;
			if(reportValue != null)
				params += "&reportValue=" + URLEncoder.encode(reportValue, "UTF-8");
			
			if(pageNum1 != null)
				params += "&pageNum1=" + pageNum1;
			
			
			
			
			
			dao.upDateReport(num, board, reportnum);
			
			url = cp + "/sboard/control.do" + params;
			resp.sendRedirect(url);
		
			
			
			//��õ�ϱ�
		}else if(uri.indexOf("cu.do")!=-1){//��õ�����
			
			
			
			
			System.out.println("��õ���ϴ�");
			
			int num = Integer.parseInt(req.getParameter("num"));
			String pageNum = req.getParameter("pageNum");
			String board = req.getParameter("board");
			String category = req.getParameter("category");
			String searchKey = req.getParameter("searchKey");
			String searchValue = req.getParameter("searchValue");
			
			
			
			
			
		    HttpSession session = req.getSession();
	         CustomInfo info = (CustomInfo)session.getAttribute("customInfo");
	         String Id = "";
	         if(info !=null){
	        	  Id = info.getUserId();
	         }
			
			
			
			
			/*HttpSession session = req.getSession();
			CustomInfo info = (CustomInfo)session.getAttribute("customInfo");
			*/
			
			System.out.println("id : "+ Id);
			
			System.out.println(dao.seachRoc(num, Id, board));
			
			if(dao.seachRoc(num, Id, board)){
				
				System.out.println("�Ѿ����?");
				dao.rocUpdate(num, Id, board);
				dao.rocCount(num, board);
				
			}
			
			
			
			String params = "?board=" + board;
			if(category != null)
				params += "&category=" + category;
			if(pageNum != null)
				params += "&pageNum=" + pageNum;
			if(searchKey != null)
				params += "&searchKey=" + searchKey;
			if(searchValue != null)
				params += "&searchValue=" + searchValue;
			

			
			
			
			
			
			url = cp + "/sboard/view.do"+params+"&num="+num;
			resp.sendRedirect(url);
			return;
			
		}
		//���
		
		else if(uri.indexOf("reply_ok.do") != -1){
            
	         String board = req.getParameter("category");   
	         int num = Integer.parseInt(req.getParameter("num"));
	                  
	         int maxnum = dao.getReplyCount(num, board);
	         
	         BoardDTO dto = new BoardDTO();
	         
	         dto.setRnum(maxnum + 1);
	         dto.setCategory(board);
	         dto.setUserId(req.getParameter("userId"));
	         dto.setContent(req.getParameter("content").replaceAll("\n", "<br/>"));
	         dto.setNum(num);
	          
	         dao.insertReply(dto);
	                  
	         url = cp + "/sboard/view.do?board="+board+"&num="+num;
	         resp.sendRedirect(url);
	         return;
	            
	      }else if(uri.indexOf("replydelete.do") != -1){
	   
	         String board = req.getParameter("board");   
	         int num = Integer.parseInt(req.getParameter("num"));
	         int rnum = Integer.parseInt(req.getParameter("rnum"));
	      
	         dao.deleteReply(rnum,num,board);
	               
	         url = cp + "/sboard/view.do?board="+board+"&num="+num;
	         resp.sendRedirect(url);
	         return;
	               
	      }
		
		
	}
}
