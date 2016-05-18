<%@page import="java.sql.Connection"%>
<%@page import="com.util.DBCPConn"%>
<%@page import="com.member.MemberDAO"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	request.setCharacterEncoding("UTF-8"); 
	String cp = request.getContextPath();	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>회원가입</title>


<link rel="stylesheet" href="/study/member/css/style.css" type="text/css"/>
<link rel="stylesheet" href="/study/member/css/created.css" type="text/css"/>

<link href='http://fonts.googleapis.com/css?family=Mouse+Memoirs' rel='stylesheet' type='text/css'>
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

<script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>
<script>
    function sample6_execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                var fullAddr = ''; // 최종 주소 변수
                var extraAddr = ''; // 조합형 주소 변수

                // 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                    fullAddr = data.roadAddress;

                } else { // 사용자가 지번 주소를 선택했을 경우(J)
                    fullAddr = data.jibunAddress;
                }

                // 사용자가 선택한 주소가 도로명 타입일때 조합한다.
                if(data.userSelectedType === 'R'){
                    //법정동명이 있을 경우 추가한다.
                    if(data.bname !== ''){
                        extraAddr += data.bname;
                    }
                    // 건물명이 있을 경우 추가한다.
                    if(data.buildingName !== ''){
                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }
                    // 조합형주소의 유무에 따라 양쪽에 괄호를 추가하여 최종 주소를 만든다.
                    fullAddr += (extraAddr !== '' ? ' ('+ extraAddr +')' : '');
                }

                // 우편번호와 주소 정보를 해당 필드에 넣는다.
                document.getElementById('sample6_postcode').value = data.zonecode; //5자리 새우편번호 사용
                document.getElementById('sample6_address').value = fullAddr;

                // 커서를 상세주소 필드로 이동한다.
                document.getElementById('sample6_address2').focus();
            }
        }).open();
    }
</script>

<script type="text/javascript" src="<%=cp %>/member/js/util.js"></script>

<script type="text/javascript">

	function sendIt(){
			
		var f = document.myForm;
		
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
				
 		str = f.userEmail.value;
		str = str.trim();
		if(!str){
			alert("이메일을 입력하세요!");
			f.userEmail.focus();
			return;	
		}
		f.userEmail.value = str;
		
        if(!isValidEmail(str))  {
            alert("이메일을 정확하게 입력하세요!");
            f.userEmail.focus();
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
        
        
		f.action = "<%=cp%>/join/join_ok.do";
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
	
	function Idchk(){
		
		var f = document.myForm;
		
		str = f.userId.value;
		str = str.trim();
		if(!str){
			alert("아이디를 입력하세요!");
			f.userId.focus();
			return;	
		}
		f.userId.value = str;
				
		f.action = "<%=cp%>/join/Idchk.do";
		f.submit();
	}
	
	function Emailchk(){
		
		var f = document.myForm;
		
		str = f.userEmail.value;
		str = str.trim();
		f.userEmail.value = str;
		
		if(!str){
			alert("이메일을 입력하세요!");
			f.userId.focus();
			return;	
		}
        if(!isValidEmail(str))  {
            alert("이메일을 정확하게 입력하세요!");
            f.userEmail.focus();
            return;
        }
	
		f.action = "<%=cp%>/join/Emailchk.do";
		f.submit();
	}
	
	
</script>

</head>
<body onload="pass()">

<form action="" method="post" name="myForm">

	<table align="center" cellpadding="0" cellspacing="0" border="0" style="width: 700px;">

		<tr height="30" >
			<td colspan="2" align="center" >
			<img src ="<%=cp %>/images/torrentname.png" width="700" height="200">
			</td>
		</tr>
		
	</table>

	<table align="center" cellpadding="0" cellspacing="0" border="0" style="width: 400px;">
				
		<tr height="3"><td colspan="2" bgcolor="#ffffff"></td></tr>
		
		<tr height="40">
			<td width="110" style="padding-left: 5px;">
				<input type="text" class="form-control" value="${userId }" name="userId" placeholder="※아이디" style="height: 50px; font-size: 20px; font-family: 고딕; width: 250px;">
			</td>
			<td>	
				<input type="button" value="ID Check&nbsp&nbsp&nbsp&nbsp&nbsp" style="width: 150px; height: 50px; font-size: 15px; font-family: 고딕;  font-weight: bold; " class="btn btn-default" onclick="Idchk();">
			</td>			
		</tr>
		
		<tr height="3"><td colspan="2" bgcolor="#ffffff"></td></tr>
		
		<tr height="40">
			<td width="110" style="padding-left: 5px;">
			<input type="text" class="form-control"  value="${userEmail }" name="userEmail" placeholder="※이메일" style="height: 50px; font-size: 20px; font-family: 고딕; width: 250px;">
			</td>
			<td>	
				<input type="button" value="Email Check" style="width: 150px; height: 50px; font-size: 15px; font-family: 고딕; font-weight: bold;" class="btn btn-default" onclick="Emailchk();">
			</td>
		</tr>
		
		<tr height="3"><td colspan="2" bgcolor="#ffffff"></td></tr>
				
		<tr height="40">
			<td width="140" style="padding-left: 5px;" colspan="2">
			<input type="password" class="form-control" value="${userPwd}" name="userPwd" placeholder="※비밀번호" style="height: 50px; font-size: 20px; font-family: 고딕; width: 400px;">
			</td>
		</tr>
		
		<tr height="3"><td colspan="2" bgcolor="#ffffff"></td></tr>
				
		<tr height="40">
			<td width="140" style="padding-left: 5px;" colspan="2">
			<input type="password" class="form-control" value="${userPwd2 }" name="userPwd2" placeholder="※비밀번호 확인" style="height: 50px; font-size: 20px; font-family: 고딕; width: 400px;">
			</td>
		</tr>
		
		<tr height="3"><td colspan="2" bgcolor="#ffffff"></td></tr>
						
		<tr height="40">
			<td width="140" style="padding-left: 5px;" colspan="2">
			<input type="text" class="form-control" value="${userName}" name="userName" placeholder="※이름" style="height: 50px; font-size: 20px; font-family: 고딕; width: 400px;">
			</td>
		</tr>		
				
		<tr height="3"><td colspan="2" bgcolor="#ffffff"></td></tr>
			
		<tr height="40">
			<td width="140" style="padding-left: 5px;" colspan="2">
			<input type="text" class="form-control" value="${userTel}" name="userTel" placeholder="※전화" style="height: 50px; font-size: 20px; font-family: 고딕; width: 400px;">
			</td>
		</tr>
				
		<tr height="3"><td colspan="2" bgcolor="#ffffff"></td></tr>
		
		<tr height="40">
			<td width="70" style="padding-left: 5px;" colspan="2">
			<font style="height: 50px; font-size: 20px; font-family: 고딕;">&nbsp;&nbsp;※성별&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font>
			<input type="radio" name="userSex" checked="checked" value="man"><font style="height: 50px; font-size: 20px; font-family: 고딕;">&nbsp;남</font>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="radio" name="userSex" value="girl"><font style="height: 50px; font-size: 20px; font-family: 고딕;">&nbsp;여</font>
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
				<input type="text" readonly class="form-control" id="sample6_postcode" name="addr1" value="${addr1}" placeholder="※우편번호" style="height: 50px; font-size: 20px; font-family: 고딕; width: 250px;">
			</td>
			<td>	
				<input type="button" class="form-control" onclick="sample6_execDaumPostcode()" value="우편번호 찾기" style="width: 150px; height: 50px; font-size: 15px; font-family: 고딕; font-weight: bold; background-color: #FCFCFC;" class="btn btn-default">
			</td>			
		</tr>
		
		<tr height="3"><td colspan="2" bgcolor="#ffffff"></td></tr>
		
		<tr height="40">
			<td width="110" style="padding-left: 5px;">
				<input type="text" readonly class="form-control" id="sample6_address" name="addr2" value="${addr2}" placeholder="※주소" style="height: 50px; font-size: 20px; font-family: 고딕; width: 250px;">
			</td>
			<td>	
				<input type="text" class="form-control" id="sample6_address2" name="addr3" value="${addr3}" placeholder="※상세주소" style="width: 150px; height: 50px; font-size: 20px; font-family: 고딕;">
			</td>			
		</tr>

		
		<tr height="10"><td colspan="2" bgcolor="#ffffff"></td></tr>
		<tr>
		<td colspan="2" align="center">	
			<button type="button" style="width: 120px;" class="btn btn-default" onclick="sendIt();">회원가입</button>
			<button type="reset" style="width: 120px;" class="btn btn-default" onclick="document.myForm.userId.focus();">다시입력</button>
			<button type="button" style="width: 120px;" class="btn btn-default" onclick="javascript:location.href='<%=cp%>/join/main.do';">가입취소</button>
		</td>
		</tr>
		

	</table>

</form>

</body>
</html>