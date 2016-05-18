package com.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;


public class MemberDAO {

	private Connection conn = null;

	public MemberDAO(Connection conn){
		this.conn = conn;
	}

	// 입력
	public int insertData(MemberDTO dto){
		
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			
			sql = "insert into member1 (userId,userPwd,userName,"
					+ "userSex,userBirth,userEmail,"
					+ "userTel,addr1,addr2,addr3,userJoinDate,userGrade,userpoint) ";
			sql += "values (?,?,?,?,?,?,?,?,?,?,sysdate,1,0)";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getUserId());
			pstmt.setString(2, dto.getUserPwd());
			pstmt.setString(3, dto.getUserName());
			pstmt.setString(4, dto.getUserSex());
			pstmt.setString(5, dto.getUserBirth());
			pstmt.setString(6, dto.getUserEmail());
			pstmt.setString(7, dto.getUserTel());
			pstmt.setString(8, dto.getAddr1());
			pstmt.setString(9, dto.getAddr2());
			pstmt.setString(10, dto.getAddr3());
						
			result = pstmt.executeUpdate();
			
			pstmt.close();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return result;
	}
	
	public MemberDTO getReadData(String userId){
		
		MemberDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		
		try {
			
			sql = "select userId,userPwd,userName,"
					+ "userSex,to_char(userBirth,'YYYY-MM-DD') userBirth,"
					+ "userEmail,userTel,userPoint,userGrade,userJoinDate,userAuth,addr1,addr2,addr3 ";
			sql += "from member1 where userId=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userId);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				
				dto = new MemberDTO();
				
				dto.setUserId(rs.getString("userId"));
				dto.setUserPwd(rs.getString("userPwd"));
				dto.setUserName(rs.getString("userName"));
				dto.setUserSex(rs.getString("userSex"));
				dto.setUserBirth(rs.getString("userBirth"));
				dto.setUserEmail(rs.getString("userEmail"));
				dto.setUserTel(rs.getString("userTel"));
				dto.setUserPoint(rs.getString("userPoint"));
				dto.setUserGrade(rs.getString("userGrade"));
				dto.setUserAuth(rs.getString("userAuth"));
				dto.setAddr1(rs.getString("addr1"));
				dto.setAddr2(rs.getString("addr2"));
				dto.setAddr3(rs.getString("addr3"));
			}
			
			rs.close();
			pstmt.close();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return dto;		
	}
	
	public MemberDTO getEmail(String userEmail){
		
		MemberDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
				
		try {
			
			sql = "select * from member1 where userEmail=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userEmail);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				
				dto = new MemberDTO();
				
				dto.setUserEmail(rs.getString("userEmail"));

			}
			
			rs.close();
			pstmt.close();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return dto;		
	}
	
	
	public void updateData(MemberDTO dto){
		
		PreparedStatement pstmt = null;
		
		String sql;
		
		try {
			
			sql = "update member1 set userPwd=?, userName=?, userBirth=?, "
					+ "userTel=?, addr1=?, addr2=?, addr3=? "
					+ "where userId=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getUserPwd());
			pstmt.setString(2, dto.getUserName());
			pstmt.setString(3, dto.getUserBirth());
			pstmt.setString(4, dto.getUserTel());
			pstmt.setString(5, dto.getAddr1());
			pstmt.setString(6, dto.getAddr2());
			pstmt.setString(7, dto.getAddr3());
			pstmt.setString(8, dto.getUserId());
			
			pstmt.executeUpdate();
			
			pstmt.close();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return;
		
	}public int emailAuth(MemberDTO dto){
		
		int result = 0;
		
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			
			sql = "update member1 set userAuth=? where userId=?";
						
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, "ok");
			pstmt.setString(2, dto.getUserId());
			
			result = pstmt.executeUpdate();
			
			pstmt.close();
						
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return result;
		
	}public int updatePoint(String userId,String num){
		
		int result = 0;
		
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			
			sql = "update member1 set userPoint=? where userId=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, num);
			
			pstmt.setString(2, userId);
			
			result = pstmt.executeUpdate();
			
			pstmt.close();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return result;
	}
	//회원정보 관리를 위한 회원리스트 출력문 
	public ArrayList<MemberDTO> getList(int start, int end,String searchValue){
		
		ArrayList<MemberDTO> lists = new ArrayList<MemberDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			
			searchValue = "%" + searchValue + "%";	
			
			sql = "select * from (";
			sql +="select rownum rnum,data.* from(";
			sql +="select userId,userPwd,userName,userSex,userBirth,userEmail,userTel,userPoint,userGrade,";
			sql +="userJoinDate,userAuth,addr1,addr2,addr3 from member1 ";
			sql +="where userId like ? order by userGrade desc) data) ";
			sql +="where rnum>=? and rnum<=?";
			
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, searchValue);
			pstmt.setInt(2, start);
			pstmt.setInt(3, end);
			
			
			rs=pstmt.executeQuery();
			
			while(rs.next()){
				
				MemberDTO dto = new MemberDTO();
				
				dto.setUserId(rs.getString("userId"));
				dto.setUserPwd(rs.getString("userPwd"));
				dto.setUserName(rs.getString("userName"));
				dto.setUserSex(rs.getString("userSex"));
				dto.setUserBirth(rs.getString("userBirth"));
				dto.setUserEmail(rs.getString("userEmail"));
				dto.setUserTel(rs.getString("userTel"));
				dto.setUserPoint(rs.getString("userPoint"));
				dto.setUserGrade(rs.getString("userGrade"));
				dto.setUserJoinDate(rs.getString("userJoinDate"));
				dto.setUserAuth(rs.getString(11));
				dto.setAddr1(rs.getString(12));
				dto.setAddr2(rs.getString(13));
				dto.setAddr3(rs.getString(14));
				
				lists.add(dto);
				
				
			}
			pstmt.close();
			rs.close();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		
		return lists;
	}
	
	//--회원정보 관리를 위한 회원리스트 출력문 
	
	
public int delete(String userId){
		
		
		int result = 0;
		
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "delete member1 where userid=?";
			
			pstmt= conn.prepareStatement(sql);
			
			pstmt.setString(1, userId);
			
			result = pstmt.executeUpdate();
			
			pstmt.close();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return result;
	}

//회원정보 페이지을 하기 위한 전체 데이터 갯수 구하기

public int getDataCount(String searchValue){
	
	int totalData = 0;
	
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String sql;
	
	try {
		
		searchValue = "%" + searchValue + "%";
		
		sql = "select nvl(count(*),0) from member1";
		sql += " where userId like ?";
						
		pstmt = conn.prepareStatement(sql);
		
		pstmt.setString(1, searchValue);
		
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

	

	
}





