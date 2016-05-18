package com.member;

import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.util.DBCPConn;


public class MemberServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	protected void forward(HttpServletRequest req, HttpServletResponse resp, String url)
			throws ServletException, IOException {
		RequestDispatcher rd = req.getRequestDispatcher(url);
		rd.forward(req, resp);
		
	}
		
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");
		
		Connection conn = DBCPConn.getConnection();
		MemberDAO dao = new MemberDAO(conn);
				
		String cp = req.getContextPath();
		String uri = req.getRequestURI();
		
		String url;
		
		if(uri.indexOf("join.do")!=-1){
			
			System.out.println("join.do");
			
			url = "/member/join.jsp";
			forward(req, resp, url);
			
		}else if(uri.indexOf("join_ok.do")!=-1) {
			
			System.out.println("join_ok.do");
					
			MemberDTO dto = new MemberDTO();
						
			String email = req.getParameter("userEmail");
			String id = req.getParameter("userId");
			
			//회원가입전 이메일 확인================================================
			
			if (dao.getEmail(email)!=null){
				
				req.setAttribute("userId",req.getParameter("userId"));
				req.setAttribute("userPwd",req.getParameter("userPwd"));
				req.setAttribute("userPwd2",req.getParameter("userPwd2"));
				req.setAttribute("userEmail",req.getParameter("userEmail"));
				req.setAttribute("userName",req.getParameter("userName"));
				req.setAttribute("userTel",req.getParameter("userTel"));
				req.setAttribute("userEmail",req.getParameter("userEmail"));
				req.setAttribute("year",req.getParameter("year"));
				req.setAttribute("month",req.getParameter("month"));
				req.setAttribute("day",req.getParameter("day"));
				req.setAttribute("day1",req.getParameter("addr1"));
				req.setAttribute("day2",req.getParameter("addr2"));
				req.setAttribute("day3",req.getParameter("addr3"));
								
				req.setAttribute("message", "이메일 중복!!");
				url = "/member/join.jsp";
				forward(req, resp, url);
				return;
				
			} else if (dao.getReadData(id)!=null){
				
				req.setAttribute("userId",req.getParameter("userId"));
				req.setAttribute("userPwd",req.getParameter("userPwd"));
				req.setAttribute("userPwd2",req.getParameter("userPwd2"));
				req.setAttribute("userEmail",req.getParameter("userEmail"));
				req.setAttribute("userName",req.getParameter("userName"));
				req.setAttribute("userTel",req.getParameter("userTel"));
				req.setAttribute("userEmail",req.getParameter("userEmail"));
				req.setAttribute("year",req.getParameter("year"));
				req.setAttribute("month",req.getParameter("month"));
				req.setAttribute("day",req.getParameter("day"));
				req.setAttribute("addr1",req.getParameter("addr1"));
				req.setAttribute("addr2",req.getParameter("addr2"));
				req.setAttribute("addr3",req.getParameter("addr3"));
								
				req.setAttribute("message", "아이디 중복!!");
				url = "/member/join.jsp";
				forward(req, resp, url);
				return;
				
			}
						
			dto.setUserId(req.getParameter("userId"));
			dto.setUserEmail(req.getParameter("userEmail"));
			dto.setUserPwd(req.getParameter("userPwd"));
			dto.setUserName(req.getParameter("userName"));
			dto.setUserSex(req.getParameter("userSex"));
			dto.setUserBirth(req.getParameter("userBirth"));
			dto.setUserTel(req.getParameter("userTel"));
			dto.setAddr1(req.getParameter("addr1"));
			dto.setAddr2(req.getParameter("addr2"));
			dto.setAddr3(req.getParameter("addr3"));
				
			dao.insertData(dto);
									
			String senderName = "펀치토렌트 관리자";
			String senderEmail = "admin@punch.com";
			String receiverEmail = req.getParameter("userEmail");
			String subject = "펀치 토렌트 회원가입 이메일 인증.";
			
			String content = "<table align=\"center\" width=\"500\">"
							+"<tr height=\"50\"><td><a href=\"http://localhost:8080/torrent/project/login.do\"><img src =\"http://dcimg2.dcinside.com/viewimage.php?id=25b4c02eeac2&no=29bcc427b38177a16fb3dab004c86b6f1a1232ae65b3ad26368095e9597b0f1dea61bb2a5863b1e7d53750937305f43359d2c65dfe72ed1878022ccb\" width=\"230\" height=\"50\"></a></td></tr>"
							+"<tr height=\"3\" bgcolor=\"#B2CCFF\" ><td></td></tr>"
							+"<tr height=\"15\" bgcolor=\"#ffffff\" ><td></td></tr>"	
							+"<tr height=\"20\"><td><font style=\"font-size: 20pt; font-weight: bold; color: #5D5D5D\">펀치토렌트 회원가입 인증 하기</font></td></tr>"
							+"<tr height=\"15\" bgcolor=\"#ffffff\" ><td></td></tr>"
							+"<tr height=\"20\"><td><font style=\"font-size: 8pt; color: #BDBDBD\">안녕하세요, 펀치토렌트 회원 가입을 위한 이메일 인증을 진행합니다.<br/>"
							+"아래 </font> 이메일 인증 <font style=\"font-size: 8pt; color: #BDBDBD\">버튼을 클릭해 회원 가입 인증을 완료 하여 주시기 바랍니다.</font></td></tr>"
							+"<tr height=\"40\" bgcolor=\"#ffffff\" ><td></td></tr>"
							+"<tr height=\"20\"><td align=\"center\" valign=\"middle\"><a href=\"http://192.168.16.15:8080/torrent/join/auth.do?id="+id
							+ "\"><button  class=\"btn btn-default\" style=\"width: 120px;\""
							+ "\">이메일 인증</button></a></td></tr>"
							+"<tr height=\"40\" bgcolor=\"#ffffff\" ><td></td></tr>"
							+"<tr height=\"20\"><td><font style=\"font-size: 9pt;\">본 메일은 발신전용 메일입니다. 궁금하신 사항은 펀치토렌트로 문의하시기 바랍니다.<br/>"
							+"타인이 고객님의 이메일을 입력했을 경우 메일이 발송될 수 있습니다.</font></td></tr>"
							+"<tr height=\"25\" bgcolor=\"#ffffff\" ><td></td></tr>"
							+"<tr height=\"3\" bgcolor=\"#B2CCFF\" ><td></td></tr>"
							+"<tr height=\"50\"><td valign=\"middle\"><center><a href=\"http://192.168.16.15:8080/torrent/project/login.do\"><img src =\"http://dcimg2.dcinside.com/viewimage.php?id=25b4c02eeac2&no=29bcc427b38177a16fb3dab004c86b6f1a1232ae65b3ad26368095e9597b0f1dea61bb2a5863b1e7d53750937305f43359d2c65dfe72ed1878022ccb\" width=\"150\" height=\"35\"></a></td></center></tr>"
							+"<tr height=\"20\" bgcolor=\"#ffffff\" ><td>㈜펀치토렌트, 서울시 강남구 역심동 648-23번지 여삼빌딩<br/>"
							+"Copyright © PunchTorrent Korea Corporation. All Rights Reserved.</td></tr>"
							+"</table>";
					
			String host = "localhost";
			
			Properties props = System.getProperties();
			Session ssn = Session.getInstance(props,null);
			
			try{
				
				MimeMessage message = new MimeMessage(ssn);
				
				message.addRecipient(Message.RecipientType.TO,
						new InternetAddress(receiverEmail));
				message.setFrom(new InternetAddress(senderEmail,senderName,"UTF-8"));
				message.setSubject(subject, "UTF-8");
				message.setContent(content, "text/html;charset=UTF-8");
				
				Transport tp = ssn.getTransport("smtp");
				tp.connect(host, "", ""); 
				tp.sendMessage(message, message.getAllRecipients());
				tp.close();
										
			}catch (Exception e){
				System.out.println(e.toString());
			}	
			
			req.setAttribute("message", "이메일 인증 후 회원가입이 완료됩니다!!");
			
			url ="/member/main.jsp";
			forward(req, resp, url);
			return;
					
		}else if(uri.indexOf("main.do")!=-1) {
			
			System.out.println("main.do");
			
			url = "/member/main.jsp";
			forward(req, resp, url);
			return;
			
		}else if(uri.indexOf("login_ok.do")!=-1){
			
			System.out.println("login_ok.do");
						
			String userId = req.getParameter("userId");
			String userPwd = req.getParameter("userPwd");
								
			MemberDTO dto = dao.getReadData(userId);
			
			if(dto==null || (!dto.getUserPwd().equals(userPwd))){
				
				System.out.println("1");
				req.setAttribute("message", "아이디 또는 패스워드를 정확히 입력하세요!!");
				req.setAttribute("find", "비밀번호 찾기");
				
				url = "/member/main.jsp";
				forward(req, resp, url);
				return;
							
			}else if(dto.getUserAuth()==null) {
				System.out.println("2");
				req.setAttribute("message", "이메일 인증 후 로그인가능!!");
				
				url = "/member/main.jsp";
				forward(req, resp, url);
				return;
			}

			HttpSession session = req.getSession(true);
			session.setMaxInactiveInterval(3600);
			CustomInfo info = new CustomInfo();
			
			info.setUserId(dto.getUserId());
			info.setUserName(dto.getUserName());
			
			session.setAttribute("customInfo", info);
			req.setAttribute("Id", dto.getUserId());
			
			url = "/sboard/main.do";
			forward(req, resp, url);
			return;
						
		}else if(uri.indexOf("find.do")!=-1){//비밀번호찾기
			
			url = "/member/find.jsp";
			forward(req, resp, url);
			
			
		}else if(uri.indexOf("find_ok.do")!=-1){
			
			String userId = req.getParameter("userId");
			String userEmail = req.getParameter("userEmail");
			
			MemberDTO dto = dao.getReadData(userId);
			
			if(dto==null || (!dto.getUserId().equals(userId)) || (!dto.getUserEmail().equals(userEmail))){
				
				req.setAttribute("message", "회원정보가 존재하지않습니다");
				req.setAttribute("userId",userId);
				req.setAttribute("userEmail",userEmail);
				url = "/member/find.jsp";
				forward(req, resp, url);
				
				
			}else if((dto.getUserId().equals(userId)) && (dto.getUserEmail().equals(userEmail))){
				
				//===================================================================================================
				String senderName = "펀치토렌트 관리자";
				String senderEmail = "admin@punch.com";
				String receiverEmail = req.getParameter("userEmail");
				String subject = "펀치 토렌트 비밀번호 찾기 서비스";
				
				String content = "<table align=\"center\" width=\"500\">"
								+"<tr height=\"50\"><td><a href=\"http://localhost:8080/torrent/project/login.do\"><img src =\"http://dcimg2.dcinside.com/viewimage.php?id=25b4c02eeac2&no=29bcc427b38177a16fb3dab004c86b6f1a1232ae65b3ad26368095e9597b0f1dea61bb2a5863b1e7d53750937305f43359d2c65dfe72ed1878022ccb\" width=\"230\" height=\"50\"></a></td></tr>"
								+"<tr height=\"3\" bgcolor=\"#B2CCFF\" ><td></td></tr>"
								+"<tr height=\"15\" bgcolor=\"#ffffff\" ><td></td></tr>"	
								+"<tr height=\"20\"><td><font style=\"font-size: 20pt; font-weight: bold; color: #5D5D5D\">펀치토렌트 비밀번호 찾기</font></td></tr>"
								+"<tr height=\"15\" bgcolor=\"#ffffff\" ><td></td></tr>"
								+"<tr height=\"20\"><td><font style=\"font-size: 8pt; color: #BDBDBD\">안녕하세요, 펀치토렌트 비밀번호 찾기 서비스 입니다.<br/></font></td></tr>"
								+"<tr height=\"40\" bgcolor=\"#ffffff\" ><td></td></tr>"
								+"<tr height=\"20\"><td align=\"center\" valign=\"middle\"><font style=\"font-size: 15pt; font-weight: bold; color: red\">"+dto.getUserPwd()+"</font></td></tr>"
								+"<tr height=\"40\" bgcolor=\"#ffffff\" ><td></td></tr>"
								+"<tr height=\"20\"><td><font style=\"font-size: 9pt;\">본 메일은 발신전용 메일입니다. 궁금하신 사항은 펀치토렌트로 문의하시기 바랍니다.<br/>"
								+"타인이 고객님의 이메일을 입력했을 경우 메일이 발송될 수 있습니다.</font></td></tr>"
								+"<tr height=\"25\" bgcolor=\"#ffffff\" ><td></td></tr>"
								+"<tr height=\"3\" bgcolor=\"#B2CCFF\" ><td></td></tr>"
								+"<tr height=\"50\"><td valign=\"middle\"><center><a href=\"http://192.168.16.15:8080/torrent/project/login.do\"><img src =\"http://dcimg2.dcinside.com/viewimage.php?id=25b4c02eeac2&no=29bcc427b38177a16fb3dab004c86b6f1a1232ae65b3ad26368095e9597b0f1dea61bb2a5863b1e7d53750937305f43359d2c65dfe72ed1878022ccb\" width=\"150\" height=\"35\"></a></td></center></tr>"
								+"<tr height=\"20\" bgcolor=\"#ffffff\" ><td>㈜펀치토렌트, 서울시 강남구 역심동 648-23번지 여삼빌딩<br/>"
								+"Copyright © PunchTorrent Korea Corporation. All Rights Reserved.</td></tr>"
								+"</table>";
				
				
				String host = "localhost";
				
				Properties props = System.getProperties();
				Session ssn = Session.getInstance(props,null);
				
				try{
					
					MimeMessage message = new MimeMessage(ssn);
					
					message.addRecipient(Message.RecipientType.TO,
							new InternetAddress(receiverEmail));
					message.setFrom(new InternetAddress(senderEmail,senderName,"UTF-8"));
					message.setSubject(subject, "UTF-8");
					message.setContent(content, "text/html;charset=UTF-8");
					
					Transport tp = ssn.getTransport("smtp");
					tp.connect(host, "", ""); 
					tp.sendMessage(message, message.getAllRecipients());
					tp.close();
											
				}catch (Exception e){
					System.out.println(e.toString());
				}
				
				//===================================================================================================
								
				req.setAttribute("pass","회원님의 이메일로 비밀번호가 발송되었습니다.");
				url ="/member/main.jsp";
				forward(req, resp, url);
				
			}
			
		}else if(uri.indexOf("auth.do")!=-1){
		
			String id = req.getParameter("id");
			MemberDTO dto = dao.getReadData(id);
			
			dao.emailAuth(dto);
			
			req.setAttribute("message", "이메일 인증이 완료되었습니다!!");
			
			url = "/member/main.jsp";
			forward(req, resp, url);
			return;
			
		}else if(uri.indexOf("Idchk.do")!=-1){
			
			String id = req.getParameter("userId");
						
			if (dao.getReadData(id)!=null){
				
				req.setAttribute("userId",req.getParameter("userId"));
				req.setAttribute("userPwd",req.getParameter("userPwd"));
				req.setAttribute("userPwd2",req.getParameter("userPwd2"));
				req.setAttribute("userEmail",req.getParameter("userEmail"));
				req.setAttribute("userName",req.getParameter("userName"));
				req.setAttribute("userTel",req.getParameter("userTel"));
				req.setAttribute("userEmail",req.getParameter("userEmail"));
				req.setAttribute("year",req.getParameter("year"));
				req.setAttribute("month",req.getParameter("month"));
				req.setAttribute("day",req.getParameter("day"));
				req.setAttribute("addr1",req.getParameter("addr1"));
				req.setAttribute("addr2",req.getParameter("addr2"));
				req.setAttribute("addr3",req.getParameter("addr3"));
				
				req.setAttribute("message", "아이디 중복!!");
				url = "/member/join.jsp";
				forward(req, resp, url);
				return;
				
			} else if (dao.getReadData(id)==null) {
				
				req.setAttribute("userId",req.getParameter("userId"));
				req.setAttribute("userPwd",req.getParameter("userPwd"));
				req.setAttribute("userPwd2",req.getParameter("userPwd2"));
				req.setAttribute("userEmail",req.getParameter("userEmail"));
				req.setAttribute("userName",req.getParameter("userName"));
				req.setAttribute("userTel",req.getParameter("userTel"));
				req.setAttribute("userEmail",req.getParameter("userEmail"));
				req.setAttribute("year",req.getParameter("year"));
				req.setAttribute("month",req.getParameter("month"));
				req.setAttribute("day",req.getParameter("day"));
				req.setAttribute("addr1",req.getParameter("addr1"));
				req.setAttribute("addr2",req.getParameter("addr2"));
				req.setAttribute("addr3",req.getParameter("addr3"));
								
				req.setAttribute("userId",id);
				req.setAttribute("message", "아이디 사용가능!!");
				url = "/member/join.jsp";
				forward(req, resp, url);
				return;
			}
								
		}else if(uri.indexOf("Emailchk.do")!=-1){
					
			String email = req.getParameter("userEmail");
			
			if (dao.getEmail(email)!=null){
				
				req.setAttribute("userId",req.getParameter("userId"));
				req.setAttribute("userPwd",req.getParameter("userPwd"));
				req.setAttribute("userPwd2",req.getParameter("userPwd2"));
				req.setAttribute("userEmail",req.getParameter("userEmail"));
				req.setAttribute("userName",req.getParameter("userName"));
				req.setAttribute("userTel",req.getParameter("userTel"));
				req.setAttribute("userEmail",req.getParameter("userEmail"));
				req.setAttribute("year",req.getParameter("year"));
				req.setAttribute("month",req.getParameter("month"));
				req.setAttribute("day",req.getParameter("day"));
				req.setAttribute("addr1",req.getParameter("addr1"));
				req.setAttribute("addr2",req.getParameter("addr2"));
				req.setAttribute("addr3",req.getParameter("addr3"));
				
				req.setAttribute("message", "이메일 중복!!");
				url = "/member/join.jsp";
				forward(req, resp, url);
				return;
				
			} else if (dao.getEmail(email)==null) {
				
				req.setAttribute("userId",req.getParameter("userId"));
				req.setAttribute("userPwd",req.getParameter("userPwd"));
				req.setAttribute("userPwd2",req.getParameter("userPwd2"));
				req.setAttribute("userEmail",req.getParameter("userEmail"));
				req.setAttribute("userName",req.getParameter("userName"));
				req.setAttribute("userTel",req.getParameter("userTel"));
				req.setAttribute("userEmail",req.getParameter("userEmail"));
				req.setAttribute("year",req.getParameter("year"));
				req.setAttribute("month",req.getParameter("month"));
				req.setAttribute("day",req.getParameter("day"));
				req.setAttribute("addr1",req.getParameter("addr1"));
				req.setAttribute("addr2",req.getParameter("addr2"));
				req.setAttribute("addr3",req.getParameter("addr3"));
				
				req.setAttribute("message", "이메일 사용가능!!");
				url = "/member/join.jsp";
				forward(req, resp, url);
				return;
				
			}							
		}
		
		else if(uri.indexOf("change.do")!=-1) {
			
			System.out.println("여긴오니");
			String id = req.getParameter("userId");
			String pageNum = req.getParameter("pageNum");
			
			String point = req.getParameter("userPoint");
			System.out.println("수정전");
			System.out.println("point : " +point);
			dao.updatePoint(id, point);
			System.out.println("수정후");
			System.out.println("id : "+id);
			url = cp +"/sboard/control.do?pageNum="+pageNum;
			resp.sendRedirect(url);
			
		}else if(uri.indexOf("delete.do")!=-1) {
			
			System.out.println("");
			String id = req.getParameter("userId");
			dao.delete(id);
			url = cp +"/sboard/control.do";
			resp.sendRedirect(url);
		}else if(uri.indexOf("memupdate_ok.do")!=-1){
			
			System.out.println("memupdate_ok.do");
			
			MemberDTO dto = new MemberDTO();
													
			dto.setUserId(req.getParameter("userId"));
			dto.setUserPwd(req.getParameter("userPwd"));
			dto.setUserName(req.getParameter("userName"));
			dto.setUserBirth(req.getParameter("userBirth"));
			dto.setUserTel(req.getParameter("userTel"));
			dto.setAddr1(req.getParameter("addr1"));
			dto.setAddr2(req.getParameter("addr2"));
			dto.setAddr3(req.getParameter("addr3"));
				
			dao.updateData(dto);
			HttpSession session = req.getSession();
			session.removeAttribute("customInfo");
			req.setAttribute("message", "다시 로그인 해주세요!!");
			url = "/member/main.jsp";
			forward(req, resp, url);
			return;
			
		}else if(uri.indexOf("pay_ok.do")!=-1) {
	                	        	        
	        String pay = req.getParameter("pay");
	        
	        req.getParameter("money1");
	        req.getParameter("money2");
	                	        
	        if(pay.equals("money1")){
	        	pay=req.getParameter("money1");
	        }else if(pay.equals("money2")){
	        	pay=req.getParameter("money2");
	        }
      
	         String id = req.getParameter("userId");
	         MemberDTO dto = dao.getReadData(id);
	         int num1 = Integer.parseInt(dto.getUserPoint()) ;
	         int num2 = Integer.parseInt(pay);
	         String num = Integer.toString(num1 + num2);
	         System.out.println("id :"  + id);
	         System.out.println("num : " + num);
	         dao.updatePoint(id, num);
	         	         
	         HttpSession session = req.getSession();
	         CustomInfo info = new CustomInfo();
	         
	         info.setUserId(dto.getUserId());
	      	         
	         session.setAttribute("customInfo", info);
	                  
	         url = cp+"/sboard/main.do";
	         resp.sendRedirect(url);
	         
	         return;
	    }
		
		
		
					
	}
	
}
