<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@page session="true"%>
<%@ page isELIgnored="false"%>

<!-- CSRF Protection token -->
<meta name="_csrf" content="${_csrf.token}"/>
	<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}"/>

<script type="text/javascript">
	
	var mainpageLoginPasswordResetLock = true;
	var mainpageLoginPasswordTokenResetLock = true;
	
	$("#mainpageLoginLoginBTN").click(function() {
		try {
			$("#errorContent").html("");
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			
			var userid = $("#mainpageLoginUseridTXT").val();
			if(userid == null || userid == "") {
				$("#mainpageLoginErrorContent").html("Userid is invalid!");
				return;
			}
			
			var password = $("#mainpageLoginPasswordTXT").val();
			if(password == null || password == "") {
				$("#mainpageLoginErrorContent").html("Password is invalid!");
				return;
			}
			
			var rememberMe = document.getElementById("mainpageLoginRemeberMeCHB");
			var rememberMevalue = false;
			if(rememberMe.checked) {
				rememberMevalue = true;
			}
			$.ajax({
				async : true,
				type : "POST",
				beforeSend : function(xhr) {
					xhr.setRequestHeader(header, token);
				},
				data : {
					username : userid,
					password : password,
					rememberme : rememberMevalue
				},
				url : "<c:url value='/tracking/processlogin'></c:url>",
				timeout : 10000,
				error : function(xhr, status, error) {
					$("#mainpageLoginUseridTXT").val("");
					$("#mainpageLoginPasswordTXT").val("");
					$("#mainpageLoginErrorContent").html("Service error!");
					console.log(xhr.responseText);
				},
				success : function(result, status, xhr) {
					$("#page-wrapper").html(result);
				}
			});
		} catch(err) {
			try {
				$("#mainpageLoginErrorContent").html("Service is unavailable!");
			} catch(finalError) {
				console.log(finalError);
			}
		}
	});

	$("#mainpageLoginPasswordResetBTN").click(function() {
		try {
			if(mainpageLoginPasswordResetLock == true) {
				mainpageLoginPasswordResetLock = false;
			} else {
				return;
			}						
			$("#errorContent").html("");
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			var userid = $("#mainpageLoginResetPasswordUseridTXT").val();
			
			if(userid == null || userid == "") {
				$("#mainpageLoginPasswordResetErrorParagh").html("Password value error!");
				mainpageLoginPasswordResetLock = true;
				return;
			}
			$.ajax({
				async : true,
				type : "POST",
				beforeSend : function(xhr) {
					xhr.setRequestHeader(header, token);
				},
				url : "<c:url value='/tracking/resetpassword'></c:url>",
				data : {
					userid : userid
				},
				timeout : 10000,
				success : function(result, status, xhr) {
					mainpageLoginPasswordResetLock = true;
					$("#mainpageLoginResetPasswordUseridTXT").val("");
					switch(result.responseState) {
					case 100: {
						$("#mainpageLoginPasswordResetErrorParagh")
							.html("The revocery token has been sent to the email address!");
						break;
					}
					case 200: {
						$("#mainpageLoginPasswordResetErrorParagh")
						.html("Invalid parameter!");
						break;
					}
					case 400: {
						$("#mainpageLoginPasswordResetErrorParagh")
						.html("Service error!");
						break;
					}
					case 600: {
						$("#mainpageLoginPasswordResetErrorParagh")
						.html("There is no user with this id in the system!");
						break;
					}
					default: {
						$("#mainpageLoginPasswordResetErrorParagh")
						.html("Service error!");
						break;
					}
					}
				},
				error : function(xhr, status, error) {
					mainpageLoginPasswordResetLock = true;
					$("#mainpageLoginResetPasswordUseridTXT").val("");
					$("#mainpageLoginPasswordResetErrorParagh").html("Service error!");
				}
				
			});
		} catch(err) {
			mainpageLoginPasswordResetLock = true;
			try {
				$("#mainpageLoginPasswordResetErrorParagh").html("Service is unavailable!");
			} catch(err) {
				
			}	
		}
	});
	
	$("#mainpageLoginResetPassworkWithTokenBTN").click(function(event) {
		try {
			if(mainpageLoginPasswordTokenResetLock == true) {
				mainpageLoginPasswordTokenResetLock = false;
			} else {
				return;
			}
			event.preventDefault();
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			
			var userid = $("#mainpageLoginRestorePasswordWithTokenUseridTXT").val();			
			var passwordToken = $("#mainpageLoginRestorePasswordWithTokenTokenTXT").val();			
			$("#mainpageLoginRestorePasswordWithTokenUseridTXT").val("");
			$("#mainpageLoginRestorePasswordWithTokenTokenTXT").val("");
			if(userid == null || userid == "") {
				$("#mainpageLoginPasswordResetWithTokenErrorParagh").html("Userid is invalid!");
				mainpageLoginPasswordTokenResetLock = true;
				return;
			}
			
			if(passwordToken == null || passwordToken == "") {
				$("#mainpageLoginPasswordResetWithTokenErrorParagh").html("Token is invalid!");
				mainpageLoginPasswordTokenResetLock = true;
				return;
			}
			
			$.ajax({
				type : "POST",
				async : true,
				beforeSend : function(xhr) {
					xhr.setRequestHeader(header, token);
				},
				url : "<c:url value='/tracking/passwordrequestwithtoken'></c:url>",
				data : {
					userid : userid,
					token : passwordToken
				},
				timeout : 10000,
				success : function(result, status, xhr) {
					mainpageLoginPasswordTokenResetLock = true;
					switch(result.responseState) {
					case 100: {
						$("#mainpageLoginPasswordResetWithTokenErrorParagh")
							.html("The new password has been sent to the email address!");
						break;
					}
					case 200: {
						$("#mainpageLoginPasswordResetWithTokenErrorParagh")
						.html("Invalid parameter!");
						break;
					}
					case 300: {
						$("#mainpageLoginPasswordResetWithTokenErrorParagh")
						.html("The token has expired!");
						break;
					}
					case 400: {
						$("#mainpageLoginPasswordResetWithTokenErrorParagh")
						.html("Service error!");
						break;
					}
					case 600: {
						$("#mainpageLoginPasswordResetWithTokenErrorParagh")
						.html("There is no user with this id in the system!");
						break;
					}
					default: {
						$("#mainpageLoginPasswordResetWithTokenErrorParagh")
						.html("Service error!");
						break;
					}
					}
				},
				error : function(xhr, status, error) {
					mainpageLoginPasswordTokenResetLock = true;
					$("#mainpageLoginPasswordResetWithTokenErrorParagh").html("Service error!");
				}
				
			});
			
		} catch(err) {
			try {
				mainpageLoginPasswordTokenResetLock = true;
				alert(err);
				$("#mainpageLoginPasswordResetWithTokenErrorParagh").html("Service is unavailable!");
			} catch(err) {
				
			}	
		}
	});
	
</script>

<jsp:directive.include file="mainpageNavbar.jsp" />
<div class="container-fluid">
	<div class="row">
		<div class="col-lg-12">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">ILONA - Tracking module login page!</h3>
				</div>
				<div class="panel-body">
											
						<p class="bg-primary" id="mainpageLoginErrorContent">
							<c:if test="${error != null}">
								Authentication failed, bad login data!
							</c:if>
						</p>
					
						<label for="mainpageLoginUseridTXT">Userid:</label>
							
						<input type="text"
							class="form-control"
							id="mainpageLoginUseridTXT"
							required="required"
							placeholder="Please type in your userid!"
							name="userid">  <br/>
							
						<label for="mainpageLoginPasswordTXT">Password:
							
						</label>
						
						<input type="password"
							class="form-control"
							id="mainpageLoginPasswordTXT"
							required="required"
							placeholder="Please type in your password!"
							name="password">  <br/>
							
					<label for="mainpageLoginRemeberMeCHB">Remember me:</label>
					 <input type="checkbox" class="form-control"
						id="mainpageLoginRemeberMeCHB">
						
					<br />
					
					<input id="mainpageLoginLoginBTN"
						type="button"  
						value="Login">
							
				</div>
			</div>
			
			
		</div>
	</div> <!-- login content row div end -->
	
	<div class="row">
		<div class="col-lg-6">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">Password reset request!</h3>
				</div>
				<div class="panel-body">
	
					<label for="mainpageLoginResetPasswordUseridTXT">Userid:						
					</label>
						
					<input type="text"
						class="form-control"
						id="mainpageLoginResetPasswordUseridTXT"
						required="required"
						placeholder="Please type in your userid!"
						name="password">  <br/>
		
					<input type="button" 
						value="Password reset" 
						id="mainpageLoginPasswordResetBTN">
					<br /><br />	
					<p class="bg-primary" id="mainpageLoginPasswordResetErrorParagh"></p>		
				</div>
			</div>		
		</div>
		
		<div class="col-lg-6">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">Password restore with token!</h3>
				</div>
				<div class="panel-body">
	
					<label for="mainpageLoginRestorePasswordWithTokenUseridTXT">Userid:						
					</label>
						
					<input type="text"
						class="form-control"
						id="mainpageLoginRestorePasswordWithTokenUseridTXT"
						required="required"
						placeholder="Please type in the userid!"
						name="userid">  <br/>
						
					<label for="mainpageLoginRestorePasswordWithTokenTokenTXT">Token:						
					</label>
						
					<input type="text"
						class="form-control"
						id="mainpageLoginRestorePasswordWithTokenTokenTXT"
						required="required"
						placeholder="Please type in the token!"
						name="token">  <br/>
		
					<input type="button" 
						value="Password reset" 
						id="mainpageLoginResetPassworkWithTokenBTN">
					<br /><br />	
					<p class="bg-primary" id="mainpageLoginPasswordResetWithTokenErrorParagh"></p>		
				</div>
			</div>		
		</div>
	</div> <!-- Password reset row end -->
	
</div> <!-- container fluid  end -->


