<%@page import="com.board.BoardDTO"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<%  //JSP POST방식 알파벳을 제외한 언어 깨지지 않게
	request.setCharacterEncoding("UTF-8");
	String cp = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>게시판</title>

<link rel="stylesheet" href="/torrent/board/css/style.css" type="text/css"/>
<link rel="stylesheet" href="/torrent/board/css/join.css" type="text/css"/>
  <meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script> /* daum API */
    function sample6_execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                var fullAddr = ''; 
                var extraAddr = '';              
                if (data.userSelectedType === 'R') { 
                    fullAddr = data.roadAddress;
                } else { 
                    fullAddr = data.jibunAddress;
                }            
                if(data.userSelectedType === 'R'){              
                    if(data.bname !== ''){
                        extraAddr += data.bname;
                    }              
                    if(data.buildingName !== ''){
                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }                
                    fullAddr += (extraAddr !== '' ? ' ('+ extraAddr +')' : '');
                } 
                document.getElementById('sample6_postcode').value = data.zonecode; 
                document.getElementById('sample6_address').value = fullAddr;        
                document.getElementById('sample6_address2').focus();
            }
        }).open();
    }
</script>

<script type="text/javascript">

	function sendIt() {
    
		var f = document.searchForm;
		
		str = f.searchValue.value;
		str = str.trim();
	    if(!str) {
	        alert("\n검색값을 입력하세요. ");
	        f.searchValue.focus();
	        return;
	    }
		f.searchValue.value = str;
		
	    f.action = "<%=cp%>/sboard/board.do?board=${board}";
	    f.submit();
	}


	function pass(){
		
		var pass = "${pass}";
		if(pass!="")
			alert("${pass}");
		
		var nopass = "${message}";
		if(nopass!="")
			alert("${message}");

	}
	

	function send(){
			
		var f = document.myForm1;
		
 		str = f.userId.value;
		str = str.trim();
		if(!str){
			alert("아이디를 입력하세요!");
			f.userId.focus();
			return;	
		}
		f.userId.value = str;
		
 		str = f.userPwd.value;
		str = str.trim();
		if(!str){
			alert("비밀번호를 입력하세요!");
			f.userPwd.focus();
			return;	
		}
		f.userPwd.value = str;
				
		if(f.userPwd.value!=f.userPwd2.value){
			alert("비밀번호를 정확하게 입력하세요!");
			f.userPwd2.focus();
			return;	
		} 
				        
 		str = f.userName.value;
		str = str.trim();
		if(!str){
			alert("이름을 입력하세요!");
			f.userName.focus();
			return;	
		}
		f.userName.value = str;
		
        if(!isValidKorean(str))  {
            alert("이름을 정확하게 입력하세요!");
            f.userName.focus();
            return;
        } 
        
		str = f.userTel.value;
		str = str.trim();
		if(!str){
			alert("전화번호를 입력하세요!");
			f.userTel.focus();
			return;	
		}
		f.userTel.value = str;
	        
	    year = f.year.value;
        month= f.month.value;
        day	 = f.day.value;
        if(!isValidDate(year,month,day)){
            alert("생년월일을 정확하게 입력하세요!");
            f.year.focus();
            return;
        }
        str = year+"-"+month+"-"+day;
        f.userBirth.value = str;    
        
        
		f.action = "<%=cp%>/join/memupdate_ok.do";
		f.submit();
	}
	
	function payIt(){
		
		var p = document.payForm;
		
		phone1 = p.phone1.value;
		phone2 = p.phone2.value;
		phone3 = p.phone3.value;
		
		str = phone1+phone2+phone3;
		p.userTel.value = str;
		
		if(p.userTel.value!=p.userTel1.value){
			alert("전화번호가 일치하지 않습니다!!");
			p.userPwd.focus();
			return;	
		} 
			
		
		if(p.userPwd.value!=p.userPwd2.value){
			alert("비밀번호가 일치하지 않습니다!!");
			p.userPwd.focus();
			return;	
		} 
					
		p.action = "<%=cp%>/join/pay_ok.do";
		p.submit();
		alert("포인트 충전이 완료 되었습니다!!");
	}
	
function pass(){
		
		var pass = "${pass}";
		if(pass!="")
			alert("${pass}");
		
		var nopass = "${message}";
		if(nopass!="")
			alert("${message}");
	} 
	
	

</script>

</head>
<body onload="pass()" bgcolor="" style="font-style: 나눔고딕체;">
	
<table style="width: 1200px; " cellspacing="0" align="center">
	<tr height="80px;" style="background-color: #242424;">
		<td style="border-top-left-radius:10px; padding-left: 0px;" colspan="3" >
		<img alt="" src="../images/torrentname.png" height="80px;" width="500px;" 
		style="margin-left: 20px;" onclick="javascripte:location.href='<%=cp%>/sboard/main.do'"></td>
		<td style=" width: 300px; border-top-right-radius:10px;">
		<form action="" method="post" name="myForm1">
			<table style="width: 280px;">
				<tr style="height: 40px;">
					<td align="left" valign="bottom" colspan="2" style="padding-left: 10px;">
						<font style="font-size: 20px;font-weight: bold; color: #ffffff"> ${dto1.userName}</font>
						<font style="font-size: 15px;font-weight: bold; color: #ffffff">님 기다렸어요! ^_^</font></td>
					<td></td></tr>
				<tr style="height: 40px;">
					<td align="left" style="width: 120px; padding-left: 10px">
						<font style="font-size: 15px;font-weight: bold; color: #ffffff">
						<span class="glyphicon glyphicon-star"></span>:
						<span style="font-size: 20px;font-weight: bold; color: #FFBB00">${dto1.userPoint}</span>점</font></td>
					<td align="right" style="width: 160px;">
					<button type="button" class="btn btn-success" style="padding: 5px;" 
						data-target="#pay" data-toggle="modal"><span class="glyphicon glyphicon-usd"></span></button>
					<button type="button" class="btn btn-warning" style="padding: 5px;" 
						data-target="#layerpop" data-toggle="modal"><span class="glyphicon glyphicon-edit">Edit</span></button>
					<button type="button" class="btn btn-danger" style="padding: 5px;" 
						onclick="javascript:location.href='<%=cp%>/sboard/logout.do'">
						<span class="glyphicon glyphicon-log-out">Out</span></button></td></tr>
			</table></form></td></tr>
	<tr>
		<td colspan="4" height="70px;" style="margin-bottom: 0px;" >
			<table width="100%">
				<tr>
					<td height="70px" style="background-color: #242424;border-bottom-right-radius:10px;border-bottom-left-radius:10px;">
	 					<div class="navbar navbar-inverse" style="border-top-left-radius:0px;
	 					border-top-right-radius:0px;margin:auto; border: 0px;">
	  						
						  <div class="container-fluid" style="padding: 0px; padding-left: 20px; background-color: #242424;">
						    <div>
						      <ul class="nav navbar-nav">
						        <li style="width: 100px;padding-left:8px;"><a href="/torrent/sboard/board.do?board=notice"">
						        	<font style="font-size: 18px; margin-left: 0px;"><b>Notice</b></font></a></li>
						        <li style="width: 100px;padding-left:0px;"><a href="/torrent/sboard/board.do?board=TOP100">
									<font style="font-size: 18px; margin-left: 0px;"><b>TOP100</b></font></a></li>
						        <li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#">
									<font style="font-size: 18px; margin-left: 0px;"><b>Movie</b></font><span class="caret"></span></a>
							          <ul class="dropdown-menu">
										<li><a href="/torrent/sboard/board.do?board=newMovie">최신외국영화</a></li>
										<li><a href="/torrent/sboard/board.do?board=oldMovie">지난외국영화</a></li>
										<li><a href="/torrent/sboard/board.do?board=korMovie">한국 영화</a></li>
										<li><a href="/torrent/sboard/board.do?board=hdMovie">DVD 고화질 영화</a></li>
										<li><a href="/torrent/sboard/board.do?board=threeDMovie">3D 영화</a></li>
							          </ul>
						        </li>
						        <li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#">
									<font style="font-size: 18px; margin-left: 0px;"><b>TV</b></font><span class="caret"></span></a>
							          <ul class="dropdown-menu">
										<li><a href="/torrent/sboard/board.do?board=korTV">한국 TV</a></li>
										<li><a href="/torrent/sboard/board.do?board=forTV">외국 TV</a></li>
							          </ul>
						        </li>
						        <li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#">
									<font style="font-size: 18px; margin-left: 0px;"><b>Ani</b></font><span class="caret"></span></a>
							          <ul class="dropdown-menu">
										<li><a href="/torrent/sboard/board.do?board=aniOver">종방</a></li>
										<li><a href="/torrent/sboard/board.do?board=aniOn">방영중</a></li>
							          </ul>
						        </li>
						        <li style="width: 80px;padding-left:0px;"><a href="/torrent/sboard/board.do?board=game">
									<font style="font-size: 18px; margin-left: 0px;"><b>Game</b></font></a></li>
						        <li style="width: 140px;padding-left:0px;"><a href="/torrent/sboard/board.do?board=comnov">
									<font style="font-size: 18px; margin-left: 0px;"><b>Comic/Novel</b></font></a></li>
						        <li style="width: 60px;padding-left:0px;"><a href="/torrent/sboard/board.do?board=util">
									<font style="font-size: 18px; margin-left: 0px;"><b>Util</b></font></a></li>
						        <li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#">
									<font style="font-size: 18px; margin-left: 0px;"><b>Music</b></font><span class="caret"></span></a>
							          <ul class="dropdown-menu">
										<li><a href="/torrent/sboard/board.do?board=korMusic">한국 음악</a></li>
										<li><a href="/torrent/sboard/board.do?board=forMusic">외국 음악</a></li>
							          </ul>
						        </li>
						        <li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" href="#">
									<font style="font-size: 18px; margin-left: 0px;"><b>Forum</b></font><span class="caret"></span></a>
							          <ul class="dropdown-menu">
										<li><a href="/torrent/sboard/board.do?board=reviewForum">감상 후기</a></li>
										<li><a href="/torrent/sboard/board.do?board=requestForum">요청 게시판</a></li>
										<li><a href="/torrent/sboard/board.do?board=freeForum">자유 게시판</a></li>
							          </ul>
						        </li>
						      </ul>
						      <ul class="nav navbar-nav navbar-right">
  			<c:if test="${dto1.userGrade==5}">
        			<li style="padding-right: 15px;"><a href="javascript:location.href='<%=cp%>/sboard/control.do';"><font style="font-size: 15px; font-family: 돋움;"><b>관리자모드</b></font></a></li>
 			</c:if>
      </ul>
						    </div>
						  </div>
  						</div>
					</td>
				</tr>
			</table></td></tr></table>

<table width="1000" height="15" align="center">
	<tr height="20"></tr>
	<tr>
		<td align="center" style="font-size: 25px;"><b>${boardTitle}</b></td></tr>		
</table>

	<table border="0" align="center" width="1000px;" >
		<tr style="height: 25px;">
			<td align="left">
				<form action="" name="searchForm" method="post">
					<select name="searchKey" class="selectFiled">
						<c:choose>
							<c:when test="${searchKey == null}">
								<option value="subject">제목</option>
								<option value="userId">글쓴이</option>
								<option value="content">내용</option>
							</c:when>
							<c:otherwise>
								<c:if test="${searchKey.equals('subject')}">
									<option value="subject" selected="selected">제목</option>
									<option value="userId">글쓴이</option>
									<option value="content">내용</option></c:if>
								<c:if test="${searchKey=='userId'}">
									<option value="subject">제목</option>
									<option value="userId" selected="selected">글쓴이</option>
									<option value="content">내용</option></c:if>
								<c:if test="${searchKey.equals('content')}">
									<option value="subject">제목</option>
									<option value="userId">글쓴이</option>
									<option value="content" selected="selected">내용</option></c:if>
							</c:otherwise>
						</c:choose>
					</select>
					
					<input type="text" value="${searchValue }" name="searchValue" class="textFiled" style=" height: 18px;"/>
					<button type="button" class="btn btn-default" style="height: 25px; width: 50px;"onclick="sendIt();" >
				<span style="padding: 0px;" class="glyphicon glyphicon-search"></span></button>
				</form></td>
			<c:if test=""></c:if>	
			
				
			<c:if test="${!board.equals('TOP100')&&!(board.equals('notice')&&dto1.userGrade!=5)}">
			<td align="right">
				<button type="button" class="btn btn-default" style="height: 25px; width: 50px;" 
				onclick="javascript:location.href='<%=cp%>/sboard/write.do${params}';">
				<span style="padding: 0px;" class="glyphicon glyphicon-pencil"></span></button></td></c:if></tr>					
	</table>
	

<table border="0" align="center" style="width: 1000px;" class="table table-condensed">
	<tr>
	<c:choose>
		<c:when test="${board!='TOP100'}">
		<td style="border-width: 0;" colspan="7"></c:when>
			<c:otherwise>
		<td style="border-width: 0;" colspan="8"></c:otherwise></c:choose>
	<c:choose>
		<c:when test="${board.indexOf('Movie') != -1}">
			<a href="/torrent/sboard/board.do?board=${board}&category=[액션/전쟁]">[액션/전쟁]&nbsp;</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[스릴러/범죄]">[스릴러/범죄]&nbsp;</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[로맨스/멜로]">[로맨스/멜로]&nbsp;</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[드라마/가족]">[드라마/가족]&nbsp;</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[애니]">[애니]&nbsp;</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[코미디]">[코미디]&nbsp;</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[다큐멘터리]">[다큐멘터리]</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[SF/판타지]">[SF/판타지]</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[공포/호러]">[공포/호러]</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[스포츠]">[스포츠]</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[음악/뮤지컬]">[음악/뮤지컬]</a></c:when>
		<c:when test="${board.indexOf('TV') != -1}">
			<a href="/torrent/sboard/board.do?board=${board}&category=[드라마(방영중)]">[드라마(방영중)]&nbsp;</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[드라마(종방)]">[드라마(종방)]&nbsp;</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[쇼/오락/스포츠]">[쇼/오락/스포츠]&nbsp;</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[시사/교양/다큐]">[시사/교양/다큐]&nbsp;</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[분할/편집영상]">[분할/편집 영상]&nbsp;</a></c:when>
		<c:when test="${board=='game'}">
			<a href="/torrent/sboard/board.do?board=${board}&category=[액션/아케이드]">[액션/아케이드]&nbsp;</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[RPG]">[RPG]&nbsp;</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[시뮬]">[시뮬]&nbsp;</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[전략]">[전략]&nbsp;</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[어드벤처]">[어드벤처]&nbsp;</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[스포츠]">[스포츠]&nbsp;</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[기타]">[기타]</a></c:when>
		<c:when test="${board=='comnov'}">
			<a href="/torrent/sboard/board.do?board=${board}&category=[연재만화]">[연재만화]&nbsp;</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[완결만화]">[완결만화]&nbsp;</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[소설]">[소설]&nbsp;</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[기타]">[기타]&nbsp;</a></c:when>
		<c:when test="${board=='util'}">
			<a href="/torrent/sboard/board.do?board=${board}&category=[멀티미디어]">[멀티미디어]&nbsp;</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[보안/백신]">[보안/백신]&nbsp;</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[소프트웨어]">[소프트웨어]&nbsp;</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[기타]">[기타]&nbsp;</a></c:when>
		<c:when test="${board.indexOf('Music') != -1}">
			<a href="/torrent/sboard/board.do?board=${board}&category=[힙합/R&B]">[힙합/R&B]&nbsp;</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[Rock]">[Rock]&nbsp;</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[OST]">[OST]&nbsp;</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[클래식]">[클래식]&nbsp;</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[Jazz]">[Jazz]&nbsp;</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[뉴에이지]">[뉴에이지]&nbsp;</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[인디]">[인디]</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[트로트]">[트로트]</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[동요]">[동요]</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[국악]">[국악]</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[태교]">[태교]</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[CCM]">[CCM]</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[벨소리]">[벨소리]</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[기타]">[기타]</a></c:when>
		<c:when test="${board.indexOf('Forum') != -1 && !board.equals('freeForum')}">
			<a href="/torrent/sboard/board.do?board=${board}&category=[Movie]">[Movie]&nbsp;</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[TV]">[TV]&nbsp;</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[Ani]">[Ani]&nbsp;</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[Game]">[Game]&nbsp;</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[Comic/Novel]">[Comic/Novel]&nbsp;</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[Util]">[Util]&nbsp;</a>
			<a href="/torrent/sboard/board.do?board=${board}&category=[Music]">[Music]&nbsp;</a></c:when>
	</c:choose>
		</td></tr>
	<tr bgcolor="#242424" style="color: #B7B7B7; height: 40px; font-size: 16px;">
	<c:choose>
		<c:when test="${!board.equals('TOP100')}">
			<td style="font-size: 16px;" class="num" width="60" align="center">번호</td></c:when>
		<c:otherwise>
			<td style="font-size: 16px;" class="num" width="60" align="center">순위</td></c:otherwise></c:choose>
	<c:if test="${board.equals('TOP100')}">
		<td style="font-size: 16px;" class="num" width="210" align="center">게시판</td></c:if>
	<c:if test="${!board.equals('notice') && board.indexOf('ani') == -1 && !board.equals('freeForum')}">
		<td style="font-size: 16px;" class="num" width="160" align="center">카테고리</td></c:if>
		<td style="font-size: 16px;" class="subject" width="600" align="center">제목</td>
		<td style="font-size: 16px;" class="name" width="100" align="center">글쓴이</td>
		<td style="font-size: 16px;" class="created" width="100" align="center">작성일</td>
		<td style="font-size: 16px;" class="hitCount" width="60" align="center">조회</td>
		<td style="font-size: 16px;" class="hitCount" width="80" align="center">추천수</td></tr>

<c:forEach var="dto" items="${lists }">
<tr>
	<c:choose>
		<c:when test="${!board.equals('TOP100')}">
			<td align="center" width="60">${dto.num }</td></c:when>
		<c:otherwise>
			<td align="center" width="60">${dto.rnum }</td></c:otherwise></c:choose>
	<c:if test="${board.equals('TOP100')}">
		<td align="center" width="210">
			<c:choose>
				<c:when test="${dto.board == 'newMovie'}">최신외국영화</c:when>
				<c:when test="${dto.board == 'oldMovie'}">지난외국영화</c:when>
				<c:when test="${dto.board == 'korMovie'}">한국 영화</c:when>
				<c:when test="${dto.board == 'hdMovie'}">DVD 고화질 영화</c:when>
				<c:when test="${dto.board == 'threeDMovie'}">3D 영화</c:when>
				<c:when test="${dto.board == 'korTV'}">한국 TV</c:when>
				<c:when test="${dto.board == 'forTV'}">외국 TV</c:when>
				<c:when test="${dto.board == 'aniOver'}">애니메이션 (종방)</c:when>
				<c:when test="${dto.board == 'aniOn'}">애니메이션 (방영중)</c:when>
				<c:when test="${dto.board == 'game'}">게임</c:when>
				<c:when test="${dto.board == 'comnov'}">만화/소설</c:when>
				<c:when test="${dto.board == 'util'}">유틸</c:when>
				<c:when test="${dto.board == 'korMusic'}">한국 음악</c:when>
				<c:when test="${dto.board == 'forMusic'}">외국 음악</c:when>
			</c:choose></td></c:if>
	<c:if test="${!board.equals('notice') && board.indexOf('ani') == -1 && !board.equals('freeForum')}">
		<td align="center" width="160">${dto.category }</td></c:if>
	<td align="center" width="600">
		<c:choose>
			<c:when test="${board=='TOP100'}"> 
				<a href="/torrent/sboard/view.do?board=${dto.board}&category=${category}
					&pageNum=${pageNum}&searchKey=${searchKey}&searchValue=${searchValue}
					&num=${dto.num}&virtualBoard=${board}">${dto.subject }</a></c:when>
					
			<c:when test="${dto.reportCount==2 || (board=='TOP100'&& dto.reportCount==2)}">
			블라인드 처리된 게시물 입니다.
			</c:when>		
			
			<c:otherwise>
				<a href="/torrent/sboard/view.do?board=${dto.board}&category=${category}
				&pageNum=${pageNum}&searchKey=${searchKey}&searchValue=${searchValue}
				&num=${dto.num}">${dto.subject }</a></c:otherwise></c:choose></td>
	<td align="center" width="100">${dto.userId }</td>
	<td align="center" width="100">${dto.created }</td>
	<td align="center" width="60">${dto.hitCount }</td>
	<td align="center" width="80">${dto.recommendCount }</td>
	<%-- <td align="center" width="250">
	<a href="${downloadPath }?num=${dto.num}">
	${dto.originalFileName }</a>
	
	<img src="${imagePath }/${dto.saveFileName }" width="180" height="180"/>
	
	</td> --%>
</tr>
</c:forEach>
</table>

<table border="1" align="center" bgcolor="#E1E1E1">
	<tr>
		<td style="width: 1000px;"></td>
		
	</tr>
</table>

<table width="1200" cellpadding="0" cellspacing="1" align="center" bgcolor="#E1E1E1">
	<tr>
		<td align="center">
			<c:if test="${dataCount !=0 }">
				${pageIndexList }
			</c:if>
			<c:if test="${dataCount ==0 }">
				등록된 게시물이 없습니다.
			</c:if>
		</td>
	</tr>
</table>

<table style="width: 1200px; height: 100px;" cellspacing="0" align="center">
	<tr height="20"></tr>
	<tr style="height: 100px;">
		<td colspan="3" style="background-color:#242424; border-radius:10px;" align="center">
			<font style="font-size: 13px; font-family: 고딕;color: #cccccc">Copyright (c) 2015 
			<span style="font-size: 15px;color: #00D8FF; font-weight: bold;">PUNCH TORRENT</span> 
			All rights reserved .  <span style="font-size: 15px;color: #00D8FF;">DMCA</span><br/>
		 	<span style="font-size: 15px;color: #00D8FF;">광고제휴문의</span> 
		 	<span style="font-size: 13px;color: #00D8FF;"> contact us</span> 
		 	dung8758@naver.com</font></td></tr>
</table>
<!-- 회원정보수정 ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------>

<div class="modal fade" id="layerpop" style="width: 200;">
  <div class="modal-dialog">
    <div class="modal-content">
      <!-- header -->
      <div class="modal-header">
        <!-- 닫기(x) 버튼 -->
        <button type="button" class="close" data-dismiss="modal">×</button>
        <!-- header title -->
        <h4 class="modal-title" align="center" style="font-weight: bold; font-size: 25pt;">회원정보 수정</h4>
      	</div>
      	<!-- body -->
      	<div class="modal-body">
            <form action="" method="post" name="myForm1">
		
			<table align="center" cellpadding="0" cellspacing="0" border="0" style="width: 400px;">
						
				<tr height="3"><td colspan="2" bgcolor="#ffffff"></td></tr>
				
				<tr height="40">
					<td width="110" style="padding-left: 5px;" colspan="2">
						<input type="text" readonly class="form-control" value="${dto1.userId }" name="userId" placeholder="※아이디" style="height: 50px; font-size: 20px; font-family: 고딕; width: 400px;">
					</td>
				</tr>
				
				<tr height="3"><td colspan="2" bgcolor="#ffffff"></td></tr>
				
				<tr height="40">
					<td width="110" style="padding-left: 5px;" colspan="2">
					<input type="text" readonly class="form-control"  value="${dto1.userEmail }" name="userEmail" placeholder="※이메일" style="height: 50px; font-size: 20px; font-family: 고딕; width: 400px;">
					</td>
				</tr>
				
				<tr height="3"><td colspan="2" bgcolor="#ffffff"></td></tr>
						
				<tr height="40">
					<td width="140" style="padding-left: 5px;" colspan="2">
					<input type="password" class="form-control" value="${userPwd}" name="userPwd" placeholder="※변경할 비밀번호" style="height: 50px; font-size: 20px; font-family: 고딕; width: 400px;">
					</td>
				</tr>
				
				<tr height="3"><td colspan="2" bgcolor="#ffffff"></td></tr>
						
				<tr height="40">
					<td width="140" style="padding-left: 5px;" colspan="2">
					<input type="password" class="form-control" value="${userPwd2 }" name="userPwd2" placeholder="※변경할 비밀번호 확인" style="height: 50px; font-size: 20px; font-family: 고딕; width: 400px;">
					</td>
				</tr>
				
				<tr height="3"><td colspan="2" bgcolor="#ffffff"></td></tr>
								
				<tr height="40">
					<td width="140" style="padding-left: 5px;" colspan="2">
					<input type="text" class="form-control" value="${dto1.userName}" name="userName" placeholder="※이름" style="height: 50px; font-size: 20px; font-family: 고딕; width: 400px;">
					</td>
				</tr>		
						
				<tr height="3"><td colspan="2" bgcolor="#ffffff"></td></tr>
					
				<tr height="40">
					<td width="140" style="padding-left: 5px;" colspan="2">
					<input type="text" class="form-control" value="${dto1.userTel}" name="userTel" placeholder="※전화" style="height: 50px; font-size: 20px; font-family: 고딕; width: 400px;">
					</td>
				</tr>
						
				<tr height="3"><td colspan="2" bgcolor="#ffffff"></td></tr>
				
				<tr height="40">
					<td width="70" style="padding-left: 5px;" colspan="2">
					<font style="height: 50px; font-size: 20px; font-family: 고딕;">&nbsp;&nbsp;※성별&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font>
					
					<c:if test="${dto1.userSex=='man'}">
						<input type="radio" name="userSex" checked="checked" value="man"><font style="height: 50px; font-size: 20px; font-family: 고딕;">&nbsp;남</font>
					</c:if>
					<c:if test="${dto1.userSex=='girl'}">
						<input type="radio" name="userSex" value="girl" checked="checked"><font style="height: 50px; font-size: 20px; font-family: 고딕;">&nbsp;여</font>
					</c:if>

					</td>
				</tr>
				
				<tr height="3"><td colspan="2" bgcolor="#ffffff"></td></tr>
			
				<tr height="40">
					<td width="140" style="padding-left: 5px;" colspan="2">
						<font style="height: 50px; font-size: 20px; font-family: 고딕;">&nbsp;&nbsp;※생년월일&nbsp;&nbsp;&nbsp;&nbsp;
							<select name="year" title="1992">
								<%for(int i=80; i>=1; i--) {%>
									<option value="<%=i+1935%>"><%=i+1935%></option>
								<%} %>
							</select>년
							<select name="month">
								<%for(int i=1; i<=12; i++) {%>
									<option value="<%=i%>"><%=i%></option>
								<%} %>
							</select>월
							<select name="day">
								<%for(int i=1; i<=31; i++) {%>
									<option value="<%=i%>"><%=i%></option>
								<%} %>
						</select>일</font>
						
						<input type="hidden" name="userBirth" value="userBirth">
					</td>
				</tr>
					
				<!-- font-weight : normal or bold or 100 ~ 900 (폰트 굵기 설정) -->
				
				<tr height="3"><td colspan="2" bgcolor="#ffffff"></td></tr>
				
				<tr height="40">
					<td width="110" style="padding-left: 5px;">
						<input type="text" readonly class="form-control" id="sample6_postcode" name="addr1" value="${dto1.addr1}" placeholder="※우편번호" style="height: 50px; font-size: 20px; font-family: 고딕; width: 250px;">
					</td>
					<td>	
						<input type="button" class="form-control" onclick="sample6_execDaumPostcode()" value="우편번호 찾기" style="width: 150px; height: 50px; font-size: 15px; font-family: 고딕; font-weight: bold; background-color: #FCFCFC;" class="btn btn-default">
					</td>			
				</tr>
				
				<tr height="3"><td colspan="2" bgcolor="#ffffff"></td></tr>
				
				<tr height="40">
					<td width="110" style="padding-left: 5px;">
						<input type="text" readonly class="form-control" id="sample6_address" name="addr2" value="${dto1.addr2}" placeholder="※주소" style="height: 50px; font-size: 20px; font-family: 고딕; width: 250px;">
					</td>
					<td>	
						<input type="text" class="form-control" id="sample6_address2" name="addr3" value="${dto1.addr3}" placeholder="※상세주소" style="width: 150px; height: 50px; font-size: 20px; font-family: 고딕;">
					</td>			
				</tr>
						
				<tr height="10"><td colspan="2" bgcolor="#ffffff"></td></tr>		
		
			</table>
		
		</form>                            
      </div>
      <!-- Footer -->
      <div class="modal-footer" align="center">
        <button type="button" style="width: 120px;" class="btn btn-default" onclick="send();">정보수정</button>
		<button type="reset" style="width: 120px;" class="btn btn-default" onclick="document.myForm1.userPwd.focus();">다시입력</button>
		<button type="button" class="btn btn-default" data-dismiss="modal">취소</button>
      </div>
    </div>
  </div>
</div>


<!-- 회원정보수정 ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------>

<div class="modal fade" id="pay" style="width: 200;">
  <div class="modal-dialog">
    <div class="modal-content">
      <!-- header -->
      <div class="modal-header" >
        <!-- 닫기(x) 버튼 -->
        <button type="button" class="close" data-dismiss="modal">×</button>
        <!-- header title -->
        <h4 class="modal-title" align="center" style="font-weight: bold; font-size: 25pt;">펀치토렌트 포인트 결제</h4>
      	</div>
      	<!-- body -->
      	<div class="modal-body" >
        	        	 
        	
        	<c:if test="${dto1.userPoint!=null}" >
        		<font style="font-size: 20pt;">&nbsp;&nbsp;&nbsp;&nbsp;${dto1.userName} 님 보유중인 포인트 </font> <font style="font-weight: bold; font-size: 20pt; color: red;"> ${dto1.userPoint} </font> <font style="font-size: 20pt;">point</font>
        	</c:if>
        	
        	<form action="" method="post" name="payForm">
		
			<table align="center" cellpadding="0" cellspacing="0" border="0" style="width: 100%;">
						
				<tr height="3"><td colspan="4" bgcolor="#8C8C8C"></td></tr>
				
				<tr height="40">
					<td style="border-right: 1px solid;" width="40" bgcolor="#EAEAEA">&nbsp;&nbsp;&nbsp;결제금액</td>
					<td width="70">&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="radio" name="pay" checked="checked" value="money1">
					<input type="text" size="6" name="money1" placeholder="직접입력">원
					
					<input type="radio" name="pay" value="money2">
					<select name="money2">
						<option value="150000">150,000</option>
						<option value="100000">100,000</option>
						<option value="50000">50,000</option>
						<option value="30000">30,000</option>
						<option value="10000">10,000</option>
						<option value="5000">5,000</option>
						<option value="1000">1,000</option>
					</select>원
					
					<input type="hidden" name="userId" value="${dto1.userId}">
					</td>
				</tr>
								
				<tr height="1"><td colspan="4" bgcolor="#8C8C8C"></td></tr>
	
				<tr height="40">
					<td style="border-right: 1px solid;" width="40" bgcolor="#EAEAEA">&nbsp;&nbsp;&nbsp;결제수단</td>
					<td width="70">&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="radio" name="payPhone" checked="checked">
					휴대폰결제
					</td>
					<td width="70"></td>
				</tr>
														
				<tr height="1"><td colspan="4" bgcolor="#8C8C8C"></td></tr>

				<tr height="30">
					
					<td colspan="4" align="center">
						<font style="color: #8C8C8C; font-size: 9pt" >휴대폰 결제는 휴대폰 인증절차 없이 비밀번호만으로 쉽게 사용 가능합니다.</font>
					</td>
					
				</tr>		
				
				<tr height="10"><td colspan="4" bgcolor="#ffffff"></td></tr>
				
				<tr height="30">
					<td></td>
					<td	align="center" width="120" >
						<font style="font-size: 10pt" >휴대폰번호&nbsp;&nbsp;</font>
						<select name="phone1" title="010">
							<option value="010">010</option>
							<option value="011">011</option>
							<option value="016">016</option>
							<option value="017">017</option>
							<option value="018">018</option>
							<option value="070">070</option>
						</select>-
						<input type="text" name="phone2" size="1" maxlength="4">-
						<input type="text" name="phone3" size="1" maxlength="4">
						<input type="hidden" name="userTel">
						<input type="hidden" name="userTel1" value="${dto1.userTel}">
					</td>
					
				</tr>
				
				<tr height="5"><td colspan="4" bgcolor="#ffffff"></td></tr>
				
				<tr height="30">
					<td></td>
					<td	align="center" width="120" >
						<font style="font-size: 10pt" >&nbsp;&nbsp;비밀번호&nbsp;&nbsp;</font>
						<input type="password" name="userPwd" size="18">
						<input type="hidden" name="userPwd2" value="${dto1.userPwd }">
					</td>
					<td></td>
				</tr>		
																
				<tr height="15"><td colspan="4" bgcolor="#ffffff"></td></tr>
																										
				<tr height="3"><td colspan="4" bgcolor="#8C8C8C"></td></tr>
				
			</table>
			</form>
      	</div>
      	<!-- Footer -->
      	<div class="modal-footer" align="center">
        	<button type="button" style="width: 120px; background-color: #8C8C8C;" class="btn btn-default" onclick="payIt();">충전하기!</button>
			<button type="button" class="btn btn-default" data-dismiss="modal">취소</button>
      	</div>
    </div>
  </div>
</div>


</body>
</html>