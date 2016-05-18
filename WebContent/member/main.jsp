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
  

  
<title>로그인</title>

<script type="text/javascript">


function login(){
	
	var f = document.myForm;
	
	if(!f.userId.value){
		alert("아이디를 입력하세요!");
		f.userId.focus();
		return;
	}
	

	if(!f.userPwd.value){
		alert("패스워드를 입력하세요!");
		f.userPwd.focus();
		return;
	}
	
	f.action = "<%=cp%>/join/login_ok.do";
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


</script>

</head>


<body onload="pass()">

<form action="" method="post" name="myForm">
<center>
<div align="center" style="width: 1000px;">

 	<table border="0" width="1000" height="200" align="center" style="margin-top: 150px;">
		<tr>
			<td align="center" style="vertical-align: bottom;"><img alt="" src="../images/torrentname.png" width="600" height="100"></td>
		</tr>
	</table>
	

	<table align="center" cellpadding="0" cellspacing="0" border="0" style="width: 400px;">
	
	
<tr height="30" style="margin-bottom: 5px;">
	<td colspan="2" align="center" ></td>
</tr>



<tr height="40">
	
	<td width="120" style="padding-left: 5px;">
	  			<input type="text" class="form-control" id="usr" name="userId" placeholder="Id" style="height: 50px; font-size: 20px; font-family: 고딕; width: 400px;">
	</td>
</tr>

<tr height="3"><td colspan="2" bgcolor="#ffffff"></td></tr>


<tr height="40">
	
	<td width="120" style="padding-left: 5px;">
	<input type="password" class="form-control" id="pwd" name="userPwd" placeholder="Password" style="height: 50px; font-size: 20px; font-family: 고딕; width: 400px;">
	</td>
</tr>

<tr height="10"><td colspan="2" bgcolor="#ffffff"></td></tr>


<tr height="30">
	<td colspan="2" align="center">
	<button type="button" style="width: 400px; height: 50px; font-size: 20px; font-family: 고딕; margin-left: 5px;" class="btn btn-warning" onclick="login();">Sign in</button>
	</td>
</tr>	
<tr height="10"><td colspan="2" bgcolor="#ffffff"></td></tr>
<tr>
<td colspan="2" align="center">	
	<button type="button" style="width: 180px;" class="btn btn-default" onclick="javascript:location.href='<%=cp%>/join/join.do';">Register</button>
	<button type="button" style="width: 180px;" class="btn btn-default" onclick="javascript:location.href='<%=cp%>/join/find.do';">Forget</button>
</td>
</tr>



</table>

</div>
</center>
</form>

</body>
</html>