<%@ page contentType="text/html; charset=UTF-8"%>
<%@	taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<% 
	request.setCharacterEncoding("UTF-8");
	String cp = request.getContextPath();

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href='http://fonts.googleapis.com/css?family=Mouse+Memoirs' rel='stylesheet' type='text/css'>
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<title>비밀번호 찾기</title>

<link rel="stylesheet" href="<%=cp%>/member/css/style.css" type="text/css">


<script type="text/javascript">

 	function find(){
 		
 		var f = document.myForm;
 		
 		if(!f.userId.value){
			alert("아이디를 입력하세요!");
			f.userId.focus();
			return;
		}
		

		if(!f.userEmail.value){
			alert("이메일을 입력하세요!");
			f.userEmail.focus();
			return;
		}
		
		f.action = "<%=cp%>/join/find_ok.do";
		f.submit();
		 		
 	}
 	
 	function pass(){
 		 		
 		var nopass = "${message}";
 		if(nopass!="")
 			alert("${message}");

 	}

</script>

</head>
<body>

<br/><br/>

<form action="" method="post" name="myForm">

<table align="center" cellpadding="0" cellspacing="0" border="0" style="margin-top: 200px;">

	
	<tr height="2"><td colspan="2" bgcolor="#ffffff"></td></tr>
	
<tr height="30">
	<td colspan="2" align="center" ><font style="font-size: 30px; font-family:고딕;"><b>Can't access your account?</b></font></td>
</tr>


<tr height="20"><td colspan="2" bgcolor="#ffffff"></td></tr>

<tr height="25">
		<td width="120" style="padding-left: 5px;"colspan="2">
	<input type="text" class="form-control" name="userId" placeholder="※Id" style="height: 50px; font-size: 20px; font-family: 고딕; width: 400px;">
	</td>
</tr>

<tr height="2"><td colspan="2" bgcolor="#ffffff"></td></tr>


<tr height="25">
	
	<td width="120" style="padding-left: 5px;">
	<input type="text" class="form-control" name="userEmail" placeholder="※Email" style="height: 50px; font-size: 20px; font-family: 고딕; width: 400px;">
	</td>
</tr>

<tr height="2"><td colspan="2" bgcolor="#ffffff"></td></tr>

<tr height="30">
	<td colspan="2" align="center">
	<button type="button" style="width: 180px;" class="btn btn-warning" onclick="find();"><span class="glyphicon glyphicon-ok"></span></button>
	<button type="button" style="width: 180px;" class="btn btn-danger" onclick="javascript:location.href='<%=cp%>/join/main.do';"><span class="glyphicon glyphicon-remove"></span></button>
	</td>
</tr>	

<tr height="30">
	<td colspan="2" align="center">
	<font color="red" ><b>${message}</b></font>
	</td>	
</tr>

</table>

</form>




































</body>
</html>