package com.board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;



public class BoardDAO {
	
	private Connection conn = null; //미리 변수선언(밑에서 사용)
	
	public BoardDAO(Connection conn){ //DB자료 읽고 사용
		this.conn = conn;
	}
	
	public int getMaxNum(String board){
			
		int maxNum = 0;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
			
		try { //예외를 검사하고 처리
				
			sql = "select nvl(max(num),0) from " + board; 
				
			pstmt = conn.prepareStatement(sql);
				
			rs = pstmt.executeQuery();
				
			if(rs.next()) 
				maxNum = rs.getInt(1);
				
				rs.close();
				pstmt.close();
						
		} catch (Exception e) {
				System.out.println(e.toString());
		}		
		
		return maxNum;
	}
		
	
	public int insertData(BoardDTO dto, String board){
			
		int result = 0;
			
		PreparedStatement pstmt = null;
		String sql;
			
		try {
				
			sql = "insert into " + board + " (num,hitCount,category,"
					+ "userId,subject,content,created,saveFileName,reportCount,recommendCount,board,savePicture) "
					+ "values (?,?,?,?,?,?,sysdate,?,?,?,?,?)";
				
			pstmt = conn.prepareStatement(sql);
				
			pstmt.setInt(1, dto.getNum());
			pstmt.setInt(2, dto.getHitCount());
			pstmt.setString(3, dto.getCategory());
			pstmt.setString(4, dto.getUserId());
			pstmt.setString(5, dto.getSubject());
			pstmt.setString(6, dto.getContent());
			pstmt.setString(7, dto.getSaveFileName());
			pstmt.setInt(8, dto.getReportCount());
			pstmt.setInt(9, dto.getRecommendCount());
			pstmt.setString(10, dto.getBoard());
			pstmt.setString(11, dto.getSavePicture());
			
			result = pstmt.executeUpdate();
			
			pstmt.close();
								
		} catch (Exception e) {
				System.out.println(e.toString());
		}
		
		return result;
	}	
	
	public List<BoardDTO> getLists(int start, int end, String searchKey, String searchValue, String board, String category){ //매개변수를 줌
			
		List<BoardDTO> lists = new ArrayList<BoardDTO>();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null; //select 사용때 필요
		String sql;
		
		System.out.println(searchValue);
		System.out.println(category);
		
		try {	
			
			searchValue = "%" + searchValue + "%";	
			category = "%" + category + "%";
			
			if(!board.equals("notice") && board.indexOf("ani")==-1 && !board.equals("freeForum")){
				
				sql = "select * from (";
				sql+= "select rownum rnum,data.* from (";
				sql+= "select num,hitCount,category,userId,subject,content,saveFileName,";
				sql+= "to_char(created,'YYYY-MM-DD') created,reportCount,recommendCount,board ";
				sql+= "from " + board + " where category like ? and " +searchKey+ " like ? order by num desc) data) ";
				sql+= "where rnum >=? and rnum<=?";
				
				pstmt = conn.prepareStatement(sql);
				
				//매개변수 넣는 곳
				pstmt.setString(1, category);
				pstmt.setString(2, searchValue);
				pstmt.setInt(3, start);
				pstmt.setInt(4, end);
				
			}else{
				
				sql = "select * from (";
				sql+= "select rownum rnum,data.* from (";
				sql+= "select num,hitCount,category,userId,subject,content,saveFileName,";
				sql+= "to_char(created,'YYYY-MM-DD') created,reportCount,recommendCount,board ";
				sql+= "from " + board + " where " +searchKey+ " like ? order by num desc) data) ";
				sql+= "where rnum >=? and rnum<=?";
				
				pstmt = conn.prepareStatement(sql);
				
				//매개변수 넣는 곳
				pstmt.setString(1, searchValue);
				pstmt.setInt(2, start);
				pstmt.setInt(3, end);
			}
			
			rs = pstmt.executeQuery(); //rs의 값을 받음(값의 갯수는 모르기 때문에 while 사용)
			
			while(rs.next()){ //rs.next는 bof(rs)~eof까지 
				
				BoardDTO dto = new BoardDTO();
				
				dto.setNum(rs.getInt("num"));
				dto.setHitCount(Integer.parseInt(rs.getString("hitCount")));
				dto.setCategory(rs.getString("category"));
				dto.setUserId(rs.getString("userId"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setSaveFileName(rs.getString("saveFileName"));
				dto.setCreated(rs.getString("created"));
				dto.setReportCount(Integer.parseInt(rs.getString("reportCount")));
				dto.setRecommendCount(Integer.parseInt(rs.getString("recommendCount")));
				dto.setBoard(rs.getString("board"));
				
				lists.add(dto);
								
			}
			
			pstmt.close();
			rs.close();
			
		} catch (Exception e) {
			System.out.println(category);
		}
		
		return lists;
	}
		
	public int getDataCount(String searchKey, String searchValue, String board, String category){  //String searchKey,String searchValue는 검색
		
		int totalData = 0;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			
			searchValue = "%" + searchValue + "%";
			category = "%" + category + "%";
			
			if(!board.equals("notice") && board.indexOf("ani")==-1 && !board.equals("freeForum")){
				
				sql = "select nvl(count(*),0) from " + board;
				sql += " where category like ? and " +searchKey+ " like ?";
				
				pstmt = conn.prepareStatement(sql);

				pstmt.setString(1, category);
				pstmt.setString(2, searchValue);
				
			}else{
				
				sql = "select nvl(count(*),0) from " + board;
				sql += " where " +searchKey+ " like ?";
				
				pstmt = conn.prepareStatement(sql);

				pstmt.setString(1, searchValue);
			}
			
			rs = pstmt.executeQuery();
		
			if(rs.next())
				totalData = rs.getInt(1);
			
			pstmt.close();
			
			rs.close();
							
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return totalData;
	}			
	
	public BoardDTO getReadData(int num, String board){
			
		BoardDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			
			sql = "select num,hitCount,category,userId,subject,content,created,saveFileName,reportCount,recommendCount,board,savePicture ";
			sql+= "from " + board + " where num=?"; //where 조건 ()
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, num);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				
				dto = new BoardDTO();
				
				dto.setNum(rs.getInt("num"));
				dto.setHitCount(Integer.parseInt(rs.getString("hitCount")));
				dto.setCategory(rs.getString("category"));
				dto.setUserId(rs.getString("userId"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setSaveFileName(rs.getString("saveFileName"));
				dto.setCreated(rs.getString("created"));
				dto.setReportCount(Integer.parseInt(rs.getString("reportCount")));
				dto.setRecommendCount(Integer.parseInt(rs.getString("recommendCount")));
				dto.setBoard(rs.getString("board"));
				dto.setSavePicture(rs.getString("savePicture"));
			}
			
			rs.close();
			pstmt.close();	
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return dto;
	}		
		
	public int updateHitCount(int num, String board){
			
		int result = 0;
			
		PreparedStatement pstmt = null;
		String sql;
	
		try {
			
			sql = "update " + board + " set hitCount = hitCount + 1 ";
			sql+= "where num=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, num);
			
			result = pstmt.executeUpdate();
			
			pstmt.close();				
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return result;		
	}		
	
	public int updatedData(BoardDTO dto, String board){
		
		int result = 0;
		
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			
			sql = "update " + board + " set subject=?, category=?, content=?, saveFileName=?, savePicture=? ";
			sql+= "where num=?";
			
			pstmt = conn.prepareStatement(sql);
				
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getCategory());
			pstmt.setString(3, dto.getContent());
			pstmt.setString(4, dto.getSaveFileName());
			pstmt.setString(5, dto.getSavePicture());
			pstmt.setInt(6, dto.getNum());
			
			result = pstmt.executeUpdate();
			
			pstmt.close();
							
		} catch (Exception e) {
			System.out.println(e.toString());
		}			
	
		return result;
	}		
		
	public int deleteData(int num, String board){
			
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			
			sql = "delete " + board + " where num=?";
			
			pstmt = conn.prepareStatement(sql); //sql문 검사
			
			pstmt.setInt(1, num);
			
			result = pstmt.executeUpdate();
				
			pstmt.close();
								
				
		} catch (Exception e) {
				System.out.println(e.toString());
		}			

		return result;			
	}	
	
	public int top100getDataCount(String searchKey, String searchValue, String category){  //String searchKey,String searchValue는 검색
		
		int totalData = 0;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			
			searchValue = "%" + searchValue + "%";
			category = "%" + category + "%";
			
			sql= "select nvl(count(*),0) from (";
			sql+= "select rownum rnum, data.* from (";
			sql+= "select num,hitCount,category,userId,subject,content,saveFileName,";
			sql+= "to_char(created,'YYYY-MM-DD') created,reportCount,recommendCount,board ";
			sql+= "from (SELECT * FROM newMovie ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM oldMovie ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM korMovie ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM hdMovie ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM threeDMovie ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM korTV ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM forTV ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM aniOver ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM aniOn ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM game ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM comnov ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM util ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM korMusic ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM forMusic) where " +searchKey+ " like ? and reportCount!=2 order by hitCount desc) data) ";
			sql+= "where rnum >=1 and rnum<=100";
							
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, searchValue);
			
			rs = pstmt.executeQuery();
		
			if(rs.next())
				totalData = rs.getInt(1);
			
			pstmt.close();
			
			rs.close();
							
			
		} catch (Exception e) {
			System.out.println(1);
		}
		
		return totalData;
	}
	
	public List<BoardDTO> top100getLists(int start, int end, String searchKey, String searchValue, String category){ //매개변수를 줌
			
		List<BoardDTO> lists = new ArrayList<BoardDTO>();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null; //select 사용때 필요
		String sql;
		
		try {	
			
			searchValue = "%" + searchValue + "%";	
			category = "%" + category + "%";
			
			sql = "select * from (";
			sql+= "select rownum rnum1, data1.* from (";
			sql+= "select * from (";
			sql+= "select rownum rnum, data.* from (";
			sql+= "select num,hitCount,category,userId,subject,content,saveFileName,";
			sql+= "to_char(created,'YYYY-MM-DD') created,reportCount,recommendCount,board ";
			sql+= "from (SELECT * FROM newMovie ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM oldMovie ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM korMovie ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM hdMovie ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM threeDMovie ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM korTV ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM forTV ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM aniOver ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM aniOn ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM game ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM comnov ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM util ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM korMusic ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM forMusic) where " +searchKey+ " like ? and reportCount!=2 order by hitCount desc) data) ";
			sql+= "where rnum >=1 and rnum<=100 order by hitCount desc) data1) ";
			sql+= "where rnum1 >=? and rnum1<=?";
			
			pstmt = conn.prepareStatement(sql);
			
			//매개변수 넣는 곳
			pstmt.setString(1, searchValue);
			pstmt.setInt(2, start);
			pstmt.setInt(3, end);
			
			rs = pstmt.executeQuery(); //rs의 값을 받음(값의 갯수는 모르기 때문에 while 사용)
			
			while(rs.next()){ //rs.next는 bof(rs)~eof까지 
				
				BoardDTO dto = new BoardDTO();
				
				dto.setRnum(rs.getInt("rnum1"));
				dto.setNum(rs.getInt("num"));
				dto.setHitCount(Integer.parseInt(rs.getString("hitCount")));
				dto.setCategory(rs.getString("category"));
				dto.setUserId(rs.getString("userId"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setSaveFileName(rs.getString("saveFileName"));
				dto.setCreated(rs.getString("created"));
				dto.setBoard(rs.getString("board"));
				dto.setReportCount(Integer.parseInt(rs.getString("reportCount")));
				dto.setRecommendCount(Integer.parseInt(rs.getString("recommendCount")));
				
				lists.add(dto);
								
			}
			
			pstmt.close();
			rs.close();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return lists;
	}
	
	//메인에 뿌릴 페이징없이 데이터가져오기
	//메인에뿔리거
	public List<BoardDTO> getListData(String board){ //매개변수를 줌
				
				List<BoardDTO> lists = new ArrayList<BoardDTO>();
				
				PreparedStatement pstmt = null;
				ResultSet rs = null; //select 사용때 필요
				String sql;
				
				try {	
						
					
				
					sql= "select num,hitCount,category,userId,subject,content,saveFileName,";
					sql+= "to_char(created,'YYYY-MM-DD') created,reportCount,recommendCount ";
					sql+= "from " + board + " order by num desc";
					
					pstmt = conn.prepareStatement(sql);
					
					rs = pstmt.executeQuery(); //rs의 값을 받음(값의 갯수는 모르기 때문에 while 사용)
					
					while(rs.next()){ //rs.next는 bof(rs)~eof까지 
						
						BoardDTO dto = new BoardDTO();
						
						dto.setNum(rs.getInt("num"));
						dto.setHitCount(Integer.parseInt(rs.getString("hitCount")));
						dto.setCategory(rs.getString("category"));
						dto.setUserId(rs.getString("userId"));
						dto.setSubject(raw2(rs.getString("subject"), 30));
						dto.setContent(rs.getString("content"));
						dto.setSaveFileName(rs.getString("saveFileName"));
						dto.setCreated(rs.getString("created"));
						dto.setReportCount(Integer.parseInt(rs.getString("reportCount")));
						dto.setRecommendCount(Integer.parseInt(rs.getString("recommendCount")));
						
						lists.add(dto);
										
					}
					
					pstmt.close();
					rs.close();
					
				} catch (Exception e) {
					System.out.println(e.toString());
			}
				return lists;
		}
	
	
	

	//신고하기 기능 
	public int upDateReport(int num,String board,int report){
				
				
				int result=0;
				PreparedStatement pstmt = null;
				String sql;
				
				try {
					
					sql = "update "+board+" set REPORTCOUNT=?,reportdate=SYSDATE where num=?";
					
					pstmt = conn.prepareStatement(sql);
					
					pstmt.setInt(1, report);
					pstmt.setInt(2, num);

					result = pstmt.executeUpdate();

					pstmt.close();
					
				} catch (Exception e) {
					System.out.println("신고기능");
					System.out.println(e.toString());
				
				}
				return result;		
	}
	
	
	//중복 추천 있나 확인 하기 
	public boolean seachRoc(int num,String id,String board){
		
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			
			sql = "select userId,num,board from ROC where userid =? and num=? and board=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			pstmt.setInt(2, num);
			pstmt.setString(3, board);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				return false;
				
			}
			
			
			pstmt.close();
			rs.close();
			
		} catch (Exception e) {
			System.out.println("중복확인");
		}
		
		
		return true;
	}
	
	
	//추천 테이블에 데이터 올리기
	public int rocUpdate(int num,String Id,String board){
		
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			
			sql = "insert into ROC (userid,num,board) values (?,?,?)";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, Id);
			pstmt.setInt(2, num);
			pstmt.setString(3, board);
			
			result = pstmt.executeUpdate();
			
			pstmt.close();
			
			
		} catch (Exception e) {
			System.out.println("추천 데스!");
			System.out.println(e.toString());
		}
		return result;
		
	}
	
	
	
	
	//게시물 추천 수 올리기
	public int rocCount(int num,String board){
		
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			
			sql = "update "+board+" set recommendCount=recommendCount+1 where num=?";
			
			pstmt= conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			
			result = pstmt.executeUpdate();
			
			pstmt.close();
			
		} catch (Exception e) {
			System.out.println("추천수올리기");
			System.out.println(e.toString());
		}
		
		
		return result;
		
		
	}
	
	
	
	
	//신고게시물 페이징 처리를 위한 갯수 구하기
	public int reportgetDataCount(String searchKey, String searchValue){  //String searchKey,String searchValue는 검색
						
		int totalData = 0;
						
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
						
		try {
							
				searchValue = "%" + searchValue + "%";
							
							
				sql= "select nvl(count(*),0) from (";
				sql+= "select num,hitCount,category,userId,subject,content,saveFileName,";
				sql+= "to_char(created,'YYYY-MM-DD') created,reportCount,recommendCount,reportdate,board ";
				sql+= "from (SELECT * FROM newMovie ";
				sql+= "UNION ALL ";
				sql+= "SELECT * FROM oldMovie ";
				sql+= "UNION ALL ";
				sql+= "SELECT * FROM korMovie ";
				sql+= "UNION ALL ";
				sql+= "SELECT * FROM hdMovie ";
				sql+= "UNION ALL ";
				sql+= "SELECT * FROM threeDMovie ";
				sql+= "UNION ALL ";
				sql+= "SELECT * FROM korTV ";
				sql+= "UNION ALL ";
				sql+= "SELECT * FROM forTV ";
				sql+= "UNION ALL ";
				sql+= "SELECT * FROM aniOver ";
				sql+= "UNION ALL ";
				sql+= "SELECT * FROM aniOn ";
				sql+= "UNION ALL ";
				sql+= "SELECT * FROM game ";
				sql+= "UNION ALL ";
				sql+= "SELECT * FROM comnov ";
				sql+= "UNION ALL ";
				sql+= "SELECT * FROM util ";
				sql+= "UNION ALL ";
				sql+= "SELECT * FROM korMusic ";
				sql+= "UNION ALL ";
				sql+= "SELECT * FROM forMusic) where reportCount!=0 and " + searchKey + " like ? order by reportDate desc)";
				
										
				pstmt = conn.prepareStatement(sql);
					
				pstmt.setString(1, searchValue);
							
				rs = pstmt.executeQuery();
					
				if(rs.next())
					totalData = rs.getInt(1);
							
					pstmt.close();
							
					rs.close();
											
							
					} catch (Exception e) {
						System.out.println("갯수");
						System.out.println(e.toString());
					}
						
				return totalData;
	}
	
	
	
	//신고 게시물보기
	public ArrayList<BoardDTO> reportgetLists(int start, int end, String searchKey, String searchValue){ //매개변수를 줌
		
		ArrayList<BoardDTO> report = new ArrayList<BoardDTO>();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null; //select 사용때 필요
		String sql;
		
		try {	
			
			searchValue = "%" + searchValue + "%";	
			

			sql= "select * from (";
			sql+= "select rownum rnum, data.* from (";
			sql+= "select num,hitCount,category,userId,subject,content,saveFileName,";
			sql+= "to_char(created,'YYYY-MM-DD') created,reportCount,recommendCount,to_char(reportdate,'YYYY-MM-DD') reportdate,board ";
			sql+= "from (SELECT * FROM newMovie ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM oldMovie ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM korMovie ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM hdMovie ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM threeDMovie ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM korTV ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM forTV ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM aniOver ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM aniOn ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM game ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM comnov ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM util ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM korMusic ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM forMusic) where reportCount!=0 and " + searchKey + " like ? order by reportdate desc) data) ";
			sql+= "where rnum>=? and rnum<=?";
			
			
			pstmt = conn.prepareStatement(sql);
			
			//매개변수 넣는 곳
			pstmt.setString(1, searchValue);
			pstmt.setInt(2, start);
			pstmt.setInt(3, end);
			
			rs = pstmt.executeQuery(); //rs의 값을 받음(값의 갯수는 모르기 때문에 while 사용)
			
			while(rs.next()){ //rs.next는 bof(rs)~eof까지 
				
				BoardDTO dto = new BoardDTO();
				
				dto.setRnum(rs.getInt("rnum"));
				dto.setNum(rs.getInt("num"));
				dto.setHitCount(Integer.parseInt(rs.getString("hitCount")));
				dto.setCategory(rs.getString("category"));
				dto.setUserId(rs.getString("userId"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setSaveFileName(rs.getString("saveFileName"));
				dto.setCreated(rs.getString("created"));
				dto.setBoard(rs.getString("board"));
				dto.setReportDate(rs.getString("reportdate"));
				dto.setReportCount(Integer.parseInt(rs.getString("reportCount")));
				dto.setRecommendCount(Integer.parseInt(rs.getString("recommendCount")));
				
				
				report.add(dto);
								
			}
			
			pstmt.close();
			rs.close();
			
		} catch (Exception e) {
			System.out.println("불러오기");
			System.out.println(e.toString());
		}
		return report;
	}
	
	
	
	



	public List<BoardDTO> getReadReply(int num, String board){
		
		List<BoardDTO> lists = new ArrayList<BoardDTO>();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null; //select 사용때 필요
		String sql;
				
		try {	
					
			sql = "select * from (select * from reply where num=?) where category=?";
				
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, num);
			pstmt.setString(2, board);
					
			rs = pstmt.executeQuery(); 
			 			
			while(rs.next()){  
			
				BoardDTO dto = new BoardDTO();
				
				dto.setNum(rs.getInt("num"));
				dto.setCategory(rs.getString("category"));
				dto.setUserId(rs.getString("userId"));
				dto.setContent(rs.getString("content"));
				dto.setCreated(rs.getString("created"));
							
				lists.add(dto);
								
			}
			
			pstmt.close();
			rs.close();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return lists;

	}

	public int insertReply(BoardDTO dto){
			
			System.out.println("insertReply");
			
			int result = 0;
			
			PreparedStatement pstmt = null;
			String sql;
				
			try {
						
				System.out.println("insertReply1");
				
				sql = "insert into reply (num,category,userId,content,created) values (?,?,?,?,sysdate)";
				
				pstmt = conn.prepareStatement(sql);
					
				pstmt.setInt(1, dto.getNum());
				pstmt.setString(2, dto.getCategory());
				pstmt.setString(3, dto.getUserId());
				pstmt.setString(4, dto.getContent());
							
				result = pstmt.executeUpdate();
				
				pstmt.close();
				
				System.out.println(result);
				
			} catch (Exception e) {
					System.out.println(e.toString());
			}
			
			return result;
				
		}
//탑 100 넣기
	
	
public List<BoardDTO> mainTop100getLists(){ //매개변수를 줌
		
		List<BoardDTO> lists = new ArrayList<BoardDTO>();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null; //select 사용때 필요
		String sql;
		
		try {	
			
			sql = "select * from (";
			sql+= "select rownum rnum1, data1.* from (";
			sql+= "select * from (";
			sql+= "select rownum rnum, data.* from (";
			sql+= "select num,hitCount,category,userId,subject,content,saveFileName,savePicture,";
			sql+= "to_char(created,'YYYY-MM-DD') created,reportCount,recommendCount,board ";
			sql+= "from (SELECT * FROM newMovie ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM oldMovie ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM korMovie ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM hdMovie ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM threeDMovie ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM korTV ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM forTV ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM aniOver ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM aniOn ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM game ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM comnov ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM util ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM korMusic ";
			sql+= "UNION ALL ";
			sql+= "SELECT * FROM forMusic) where reportCount!=2  order by hitCount desc) data) ";
			sql+= "where rnum >=1 and rnum<=100 order by hitCount desc) data1) ";
			sql+= "where rnum1 >=1 and rnum1<=5";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery(); //rs의 값을 받음(값의 갯수는 모르기 때문에 while 사용)
			
			while(rs.next()){ //rs.next는 bof(rs)~eof까지 
				
				BoardDTO dto = new BoardDTO();
				
				dto.setRnum(rs.getInt("rnum1"));
				dto.setNum(rs.getInt("num"));
				dto.setHitCount(Integer.parseInt(rs.getString("hitCount")));
				dto.setCategory(rs.getString("category"));
				dto.setUserId(rs.getString("userId"));
				dto.setSubject(raw1(rs.getString("subject"), 25));
				dto.setContent(rs.getString("content"));
				dto.setSaveFileName(rs.getString("saveFileName"));
				dto.setSavePicture(rs.getString("savePicture"));
				dto.setCreated(rs.getString("created"));
				dto.setBoard(rs.getString("board"));
				dto.setReportCount(Integer.parseInt(rs.getString("reportCount")));
				dto.setRecommendCount(Integer.parseInt(rs.getString("recommendCount")));
				
				lists.add(dto);
								
			}
			
			pstmt.close();
			rs.close();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return lists;
	}
	

public int getReplyCount(int num,String board){
    
    int totalData = 0;
    
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    String sql;
    
    try {
                
       sql = "select nvl(count(*),0) from (select * from reply where num=?) where category=?";
       
       pstmt = conn.prepareStatement(sql);
       
       pstmt.setInt(1, num);
       pstmt.setString(2, board);
       
       rs = pstmt.executeQuery();
    
       if(rs.next())
          totalData = rs.getInt(1);
       
       pstmt.close();
       
       rs.close();
                         
    } catch (Exception e) {
       System.out.println(e.toString());
    }
    
    return totalData;
 }
	
	
	
public int deleteReply(int rnum, int num, String board){
    
    int result = 0;
    PreparedStatement pstmt = null;
    String sql;
    
    try {
                
       sql = "delete reply where rnum=? and num=? and board=?";
       
       pstmt = conn.prepareStatement(sql); 
       
       pstmt.setInt(1, rnum);
                
       result = pstmt.executeUpdate();
          
       pstmt.close();
                                  
    } catch (Exception e) {
          System.out.println(e.toString());
    }         

    return result;   
 
 }   
	
	
	
public List<BoardDTO> getListNotice(String board){ //매개변수를 줌
	
	List<BoardDTO> lists = new ArrayList<BoardDTO>();
	
	PreparedStatement pstmt = null;
	ResultSet rs = null; //select 사용때 필요
	String sql;
	
	try {	
			
		
	
		sql= "select num,hitCount,category,userId,subject,content,saveFileName,";
		sql+= "to_char(created,'YYYY-MM-DD') created,reportCount,recommendCount ";
		sql+= "from " + board + " order by num desc";
		
		pstmt = conn.prepareStatement(sql);
		
		rs = pstmt.executeQuery(); //rs의 값을 받음(값의 갯수는 모르기 때문에 while 사용)
		
		while(rs.next()){ //rs.next는 bof(rs)~eof까지 
			
			BoardDTO dto = new BoardDTO();
			
			dto.setNum(rs.getInt("num"));
			dto.setHitCount(Integer.parseInt(rs.getString("hitCount")));
			dto.setCategory(rs.getString("category"));
			dto.setUserId(rs.getString("userId"));
			dto.setSubject(raw(rs.getString("subject"), 15));
			dto.setContent(rs.getString("content"));
			dto.setSaveFileName(rs.getString("saveFileName"));
			dto.setCreated(rs.getString("created"));
			dto.setReportCount(Integer.parseInt(rs.getString("reportCount")));
			dto.setRecommendCount(Integer.parseInt(rs.getString("recommendCount")));
			
			lists.add(dto);
							
		}
		
		pstmt.close();
		rs.close();
		
	} catch (Exception e) {
		System.out.println(e.toString());
}
	return lists;
}
	
	

	
	
	
	
	
	
	
	
	
	
	public String raw2(String str,int n) { // 임시로 넣은 출력문
        StringBuffer padded = new StringBuffer(str);
        if (padded.toString().getBytes().length > n) {
            padded.setLength(27);
       padded.append(". . .");
        }
        return padded.toString();
    }
	
	public String raw(String str,int n) { // 임시로 넣은 출력문
        StringBuffer padded = new StringBuffer(str);
        if (padded.toString().getBytes().length > n) {
            padded.setLength(10);
       padded.append(". . .");
        }
        return padded.toString();
    }
	
	public String raw1(String str,int n) { // 임시로 넣은 출력문
        StringBuffer padded = new StringBuffer(str);
        if (padded.toString().getBytes().length > n) {
            padded.setLength(22);
       padded.append(". . .");
        }
        return padded.toString();
    }
	
	
	
	
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

