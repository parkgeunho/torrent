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
		
		String url;	// forward 주소
		
		// 파일을 저장할 경로
		String root = getServletContext().getRealPath("/");
		String path = root + File.separator + "contents";
		
		if(uri.indexOf("main.do") != -1){
			
			//이미지 불러올라고 경로 설정
			
			
			String imagePath= cp + "/contents/";
			
			req.setAttribute("imagePath",imagePath);
			//
			
			
	        System.out.println("여기로오니?");
	         HttpSession session = req.getSession();
	         CustomInfo info = (CustomInfo)session.getAttribute("customInfo");
	         MemberDTO dto1;
	         if(info !=null){
	         dto1 = dao1.getReadData(info.getUserId());   
	            req.setAttribute("dto1", dto1);
	         }
			
			//자게 출력용
			List<BoardDTO> freeForum = dao.getListData("freeForum");
			
			req.setAttribute("freeForum", freeForum);
			//자게 출력용
			
	
			//to용
			
			
			List<BoardDTO> top = dao.mainTop100getLists();
			
			req.setAttribute("top", top);
			
			//to용
			
			
			//공지용
			
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
				boardTitle = "공지사항";
			else if(board.equals("TOP100"))
				boardTitle = "TOP100";
			else if(board.equals("newMovie"))
				boardTitle = "최신외국영화";
			else if(board.equals("oldMovie"))
				boardTitle = "지난외국영화";
			else if(board.equals("korMovie"))
				boardTitle = "한국 영화";
			else if(board.equals("hdMovie"))
				boardTitle = "DVD 고화질 영화";
			else if(board.equals("threeDMovie"))
				boardTitle = "3D 영화";
			else if(board.equals("korTV"))
				boardTitle = "한국 TV";
			else if(board.equals("forTV"))
				boardTitle = "외국 TV";
			else if(board.equals("aniOver"))
				boardTitle = "애니메이션 (종방)";
			else if(board.equals("aniOn"))
				boardTitle = "애니메이션 (방영중)";
			else if(board.equals("game"))
				boardTitle = "게임";
			else if(board.equals("comnov"))
				boardTitle = "만화/소설";
			else if(board.equals("util"))
				boardTitle = "유틸";
			else if(board.equals("korMusic"))
				boardTitle = "한국 음악";
			else if(board.equals("forMusic"))
				boardTitle = "외국 음악";
			else if(board.equals("reviewForum"))
				boardTitle = "감상 후기";
			else if(board.equals("requestForum"))
				boardTitle = "요청 게시판";
			else if(board.equals("freeForum"))
				boardTitle = "자유 게시판";
			
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
			
			System.out.println("옵니까?");
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
			
			// 파일 업로드
			String encType = "UTF-8";
			int maxFileSize = 10*1024*1024;		// 10GB
			
			System.out.println("num" + num);
			path += File.separator + board + File.separator + num;
			
			File f = new File(path);
			if(!f.exists())
				f.mkdirs();
			
			MultipartRequest mr = new MultipartRequest(req, path, maxFileSize, encType, new DefaultFileRenamePolicy());
			
			// DB에 저장
			if(mr.getFile("upload") != null){
				System.out.println("업로드 오류 찾기");
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
			
			
			// 다운로드시 포인트 차감
			HttpSession session = req.getSession();
			CustomInfo info = (CustomInfo)session.getAttribute("customInfo");
			
			MemberDTO dto1 = dao1.getReadData(info.getUserId());
			//포인트 차감 시킬 양 정하기
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
					boardTitle = "공지사항";
				else if(board.equals("TOP100"))
					boardTitle = "TOP100";
				else if(board.equals("newMovie"))
					boardTitle = "최신외국영화";
				else if(board.equals("oldMovie"))
					boardTitle = "지난외국영화";
				else if(board.equals("korMovie"))
					boardTitle = "한국 영화";
				else if(board.equals("hdMovie"))
					boardTitle = "DVD 고화질 영화";
				else if(board.equals("threeDMovie"))
					boardTitle = "3D 영화";
				else if(board.equals("korTV"))
					boardTitle = "한국 TV";
				else if(board.equals("forTV"))
					boardTitle = "외국 TV";
				else if(board.equals("aniOver"))
					boardTitle = "애니메이션 (종방)";
				else if(board.equals("aniOn"))
					boardTitle = "애니메이션 (방영중)";
				else if(board.equals("game"))
					boardTitle = "게임";
				else if(board.equals("comnov"))
					boardTitle = "만화/소설";
				else if(board.equals("util"))
					boardTitle = "유틸";
				else if(board.equals("korMusic"))
					boardTitle = "한국 음악";
				else if(board.equals("forMusic"))
					boardTitle = "외국 음악";
				else if(board.equals("reviewForum"))
					boardTitle = "감상 후기";
				else if(board.equals("requestForum"))
					boardTitle = "요청 게시판";
				else if(board.equals("freeForum"))
					boardTitle = "자유 게시판";
			
			int currentPage = 1;
			
			if(pageNum!=null && !pageNum.equals(""))
				currentPage = Integer.parseInt(pageNum);
			
			int dataCount;
			// 전체 데이터 갯수
			if(board.equalsIgnoreCase("TOP100")){
				dataCount = dao.top100getDataCount(searchKey, searchValue, category);
			}else{
				dataCount = dao.getDataCount(searchKey, searchValue, board, category);
			}
			
			// 전체 페이지 수
			int numPerPage = 25;
			int totalPage = myUtil.getPageCount(numPerPage, dataCount);
			
			// 전체 페이지가 현재 페이지보다 큰 경우
			if(currentPage > totalPage)
				currentPage = totalPage;
			
			// 가져올 rownum의 start와 end
			int start = (currentPage-1)*numPerPage + 1;
			int end = currentPage*numPerPage;
			
			System.out.println("BOARD확안 : "  + board);
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
						
			// 포워딩 페이지에 넘길 데이터
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
	            boardTitle = "공지사항";
	         else if(board.equals("TOP100"))
	            boardTitle = "TOP100";
	         else if(board.equals("newMovie"))
	            boardTitle = "최신외국영화";
	         else if(board.equals("oldMovie"))
	            boardTitle = "지난외국영화";
	         else if(board.equals("korMovie"))
	            boardTitle = "한국 영화";
	         else if(board.equals("hdMovie"))
	            boardTitle = "DVD 고화질 영화";
	         else if(board.equals("threeDMovie"))
	            boardTitle = "3D 영화";
	         else if(board.equals("korTV"))
	            boardTitle = "한국 TV";
	         else if(board.equals("forTV"))
	            boardTitle = "외국 TV";
	         else if(board.equals("aniOver"))
	            boardTitle = "애니메이션 (종방)";
	         else if(board.equals("aniOn"))
	            boardTitle = "애니메이션 (방영중)";
	         else if(board.equals("game"))
	            boardTitle = "게임";
	         else if(board.equals("comnov"))
	            boardTitle = "만화/소설";
	         else if(board.equals("util"))
	            boardTitle = "유틸";
	         else if(board.equals("korMusic"))
	            boardTitle = "한국 음악";
	         else if(board.equals("forMusic"))
	            boardTitle = "외국 음악";
	         else if(board.equals("reviewForum"))
	            boardTitle = "감상 후기";
	         else if(board.equals("requestForum"))
	            boardTitle = "요청 게시판";
	         else if(board.equals("freeForum"))
	            boardTitle = "자유 게시판";
	         
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
	         
	         //메인에 오늘본 게시물을 출력하기 위한 쿠키 추가
	         String cookieSubject = dao.raw(dto.getSubject(),15);

	         Cookie c = new Cookie(URLEncoder.encode(cookieSubject, "UTF-8"),URLEncoder.encode("?board="+dto.getBoard()+"&num="+dto.getNum(), "UTF-8"));
	         
	         c.setMaxAge(60*60*24);
	         c.setPath("/");  
	         
	         resp.addCookie(c);
	      
	         //메인에 오늘본 게시물을 출력하기 위한 쿠키 추가
	         
	         
	         // 댓글
	         
	         List<BoardDTO> lists = dao.getReadReply(num, board);
	         req.setAttribute("lists", lists);
	         int totalCount = dao.getReplyCount(num,board);
	         // 댓글
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
			
			// 파일 삭제
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
				System.out.println("다운로드에러");
				
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
			
		}else if(uri.indexOf("logout.do")!=-1) {//로그아웃!
			
			System.out.println("log.do");
			HttpSession session = req.getSession();
			session.removeAttribute("customInfo");
			
			
			
			System.out.println("logout.do");
			url = cp+"/sboard/main.do";
			resp.sendRedirect(url);
			return;
			
		}else if(uri.indexOf("control.do")!=-1) { //관리자 페이지
			
			
			System.out.println("관리자 들어갑니다~");
			
			//공지용
			
			List<BoardDTO> notice = dao.getListNotice("notice");
	         req.setAttribute("notice", notice);
			//
			
			//회원관리 관련--------------------------------------------------------------
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
			
			// 전체 데이터 갯수
			int dataCount = dao1.getDataCount(searchValue);
			
			
			System.out.println("dataCount : " + dataCount);
			// 전체 페이지 수
			int numPerPage = 15;
			int totalPage = myUtil.getPageCount(numPerPage, dataCount);
			
			// 전체 페이지가 현재 페이지보다 큰 경우
			if(currentPage > totalPage)
				currentPage = totalPage;
			
			// 가져올 rownum의 start와 end
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
			//회원관리 관련----------------------------------------------------------------------------
			
			//세션에서 아이디 확인해서 뿌려줄 데이터 생성 시작
			HttpSession session = req.getSession();
			System.out.println("쓰발");
			CustomInfo info = (CustomInfo)session.getAttribute("customInfo");
			String userId = req.getParameter("userId");
			
			if(userId!=null){
				
				req.setAttribute("userId", userId);
				
			}
						
			if(info!=null){
			
			System.out.println("관리자 확인하기~");
			MemberDTO dto1 = dao1.getReadData(info.getUserId());
			req.setAttribute("dto1", dto1);
			}
			
			//세션에서 아이디 확인해서 뿌려줄 데이터 생성 끝
			
			
			//신고게시물 확인
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
			
			// 전체 데이터 갯수
			int reportCount = dao.reportgetDataCount(reportKey, reportValue);
			
			// 전체 페이지 수
			int reportnumPerPage = 6;
			int reporttotalPage = myUtil.getPageCount(reportnumPerPage, reportCount);
			
			// 전체 페이지가 현재 페이지보다 큰 경우
			if(reportPage > reporttotalPage)
				reportPage = reporttotalPage;
			
			// 가져올 rownum의 start와 end
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
			
			
			//신고
			
			
			
			
			
			lists.trimToSize();
			report.trimToSize();
			int list = lists.size(); //회원정보 크기
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
		}else if(uri.indexOf("report.do")!=-1){ // 신고기능
			
			System.out.println("report시작");
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
		
		
		
		}else if(uri.indexOf("handling.do")!=-1){ //신고 하면 블라인드 처리하게만들기
			
			System.out.println("handling시작");
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
		
			
			
			//추천하기
		}else if(uri.indexOf("cu.do")!=-1){//추천만들기
			
			
			
			
			System.out.println("추천들어갑니다");
			
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
				
				System.out.println("넘어오니?");
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
		//댓글
		
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
