<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page session="true"%>
<%@ page isELIgnored="false"%>

<!-- CSRF Protection token -->
<meta name="_csrf" content="${_csrf.token}" />
<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}" />

<script src="<c:url value='/js/tracking/validation.js'></c:url>"></script>
<script type="text/javascript">

	var userAccManSavedUsername = $("#userAccManUsernameTXT").val();
	var userAccManSavedEmail = $("#userAccManEmailTXT").val();

	var userAccManUpdateDetailsLock = true;
	var userAccManChangePasswordLock = true;
	
	$('[data-toggle="tooltip"]').tooltip();
	$('[data-toggle="popover"]').popover();

	$("#userAccManUpdateDetailsBTN").click(function(event){
		try {
			event.preventDefault();
			if(userAccManUpdateDetailsLock == true) {
				userAccManUpdateDetailsLock = false;
			} else {
				return;
			}
			function restoreAccountDetails() {
				$("#userAccManUsernameTXT").val(userAccManSavedUsername);
				$("#userAccManEmailTXT").val(userAccManSavedEmail);
			}
						
			/*
			 * Validity 
			 */
			$("#userAccManUpdateDetailsErrorDIV").html("");
			
			var inputs = {
				username: $("#userAccManUsernameTXT").val(),
				email: $("#userAccManEmailTXT").val()
			};
			
			var dependency = {
				validateInputs : checkInputsValidity
			};
			
			var result = dependency.validateInputs(inputs);
			
			if (result.valid == false) {
				restoreAccountDetails();
				$("#userAccManUpdateDetailsErrorDIV").html(result.errors);
				userAccManUpdateDetailsLock = true;
			} else {
				$.ajax({
					type : "POST",
					async : true,
					url : "<c:url value='/tracking/user/accountmanagement/changeuserdetails'></c:url>",
					beforeSend : function(xhr) {
						xhr.setRequestHeader($("meta[name='_csrf_header']").attr("content"),
								$("meta[name='_csrf']").attr("content"));
					},
					data : {
						userid : $("#userAccManUseridTXT").val(),
						username : $("#userAccManUsernameTXT").val(),
						email : $("#userAccManEmailTXT").val()
					},
					success : function(result, status, xhr) {
						try {
							userAccManUpdateDetailsLock = true;
							switch(result.responseState) {
							case 100: {
								$("#userAccManUpdateDetailsErrorDIV")
									.html("<p class='bg-primary'>The account has been modified!</p>");
								userAccManSavedUsername = $("#userAccManUsernameTXT").val();
								userAccManSavedEmail = $("#userAccManEmailTXT").val();
								break;
							}
							case 200: {
								$("#userAccManUpdateDetailsErrorDIV")
								.html("<p class='bg-primary'>Invalid parameter!</p>");
								break;
							}
							case 300: {
								$("#userAccManUpdateDetailsErrorDIV")
								.html("<p class='bg-primary'>Invalid data!</p>");						
								break;
							}
							case 400: {
								$("#userAccManUpdateDetailsErrorDIV")
								.html("<p class='bg-primary'>Service error!</p>");
								break;
							}						
							default: {
								$("#userAccManUpdateDetailsErrorDIV")
								.html("<p class='bg-primary'>Service error!</p>");
								break;
							}
							}
							if(result.responseState != 100) {
								restoreAccountDetails();
							}
						} catch(err) {
							console.log(err);
						}				
					},
					error : function(xhr, status, error) {
						try {
							userAccManUpdateDetailsLock = true;
							restoreAccountDetails();				
							$("#userAccManUpdateDetailsErrorDIV").html("<p class='text-danger bg-primary'>Tracking service is offline!</p>");
						} catch(err) {
							console.log(err);
						}					
					}
				});
			}
		} catch(error) {
			try {
				userAccManUpdateDetailsLock = true;
				$("#userAccManUpdateDetailsErrorDIV").html("<p class='text-danger bg-primary'>Service error!</p>");
				restoreAccountDetails();
				console.log(error);
			} catch(err) {
				console.log(err);
			}
		}
	});
	
	$("#userAccManChangePasswordBTN").click(function(event){
		try {
			event.preventDefault();
			if (userAccManChangePasswordLock == true) {
				userAccManChangePasswordLock = false;
			} else {
				return;
			}
			$("#userAccManChangePasswordErrorsDIV").html("");
			
			function clearPasswordFields() {
				$("#userAccManPassword1TXT").val("");
				$("#userAccManPassword2TXT").val("");
			}
			
			var inputs = {
				password: [$("#userAccManPassword1TXT").val(),
				           $("#userAccManPassword2TXT").val()]
			};
				
			var dependency = {
				validateInputs : checkInputsValidity
			};
				
			var result = dependency.validateInputs(inputs);
	
			if(result.valid == false) {
				$("#userAccManChangePasswordErrorsDIV").html(result.errors);
				userAccManChangePasswordLock = true;
			} else {
				$.ajax({
					type : "POST",
					async : true,
					url : "<c:url value='/tracking/user/accountmanagement/changepassword'></c:url>",
					beforeSend : function(xhr) {
						xhr.setRequestHeader($("meta[name='_csrf_header']").attr("content"),
								$("meta[name='_csrf']").attr("content"));
					},
					data : {
						userid : $("#userAccManUseridTXT").val(),
						password : $("#userAccManPassword1TXT").val()
					},
					success : function(result, status, xhr) {
						try {
							userAccManChangePasswordLock = true;
							clearPasswordFields();
							switch(result.responseState) {
							case 100: {
								$("#userAccManChangePasswordErrorsDIV")
									.html("<p class='bg-primary'>The password has been modified!</p>");
								break;
							}
							case 200: {
								$("#userAccManChangePasswordErrorsDIV")
								.html("<p class='bg-primary'>Invalid parameter!</p>");
								break;
							}
							case 300: {
								$("#userAccManChangePasswordErrorsDIV")
								.html("<p class='bg-primary'>Invalid data!</p>");						
								break;
							}
							case 400: {
								$("#userAccManChangePasswordErrorsDIV")
								.html("<p class='bg-primary'>Service error!</p>");
								break;
							}						
							default: {
								$("#userAccManChangePasswordErrorsDIV")
								.html("<p class='bg-primary'>Service error!</p>");
								break;
							}
							}						
						} catch(err) {
							console.log(err);
						}			
					},
					error : function(xhr, status, error) {
						userAccManChangePasswordLock = true;
						clearPasswordFields();
						$("#userAccManChangePasswordErrorsDIV").html("<p class='text-danger bg-primary'>Tracking service error!</p>"+error);
					}
				});
			}
		} catch(err) {
			try {
				userAccManChangePasswordLock = true;
				clearPasswordFields();
				$("#userAccManChangePasswordErrorsDIV").html("<p class='text-danger bg-primary'>Tracking service error!</p>"+error);
			} catch(error) {
				console.log(error);
			}
		}
	});
</script>

<jsp:directive.include file="userNavbar.jsp" />

<div class="container-fluid">
	<div class="row">
		<div class="col-lg-12">
			<div class="panel panel-primary">
				<div class="panel-heading">
					<h4><b>Account details change:</b></h4>
				</div>
				<div class="panel-body">
					<label for="userAccManUseridTXT"id="userAccManUseridTXTLabel">
						 Userid: READONLY
						 <span class="glyphicon glyphicon-exclamation-sign">
						</span>
					</label>
					<input id="userAccManUseridTXT"
						type="text" 
						readonly="readonly" 
						required="required"
						class="form-control"
						value="${useridValue}"><br/>
					
					<label for="userAccManUsernameTXT" id="userAccManUsernameTXTLabel">Username: <br />
						<a href="#userAccManUsernameDropdown" 
								data-toggle="collapse">Click for more information:
						</a>
						<span data-toggle="popover"
							data-html="true"
							data-trigger="hover"
							data-content="${usernameRestriction}"
							title="The username can contain the following elements:"
							class="fa  fa-info-circle">
						</span>
					</label>
					<p id="userAccManUsernameDropdown" class="collapse">
						${usernameRestriction}
					</p>
					
					<input type="text" 
						id="userAccManUsernameTXT" 
						class="form-control"
						required="required"
						pattern="${usernamePattern}"
						value="${usernameValue}"><br />
		
					<label for="userAccManEmailTXT" id="userAccManEmailTXTLabel">
						Email address: <br />
						<a href="#userAccManEmailDropdown" 
								data-toggle="collapse">Click for more information:
						</a>
						<span data-toggle="popover"
							data-html="true"
							data-trigger="hover"
							data-content="${emailRestriction}"
							title="The email address can contain the following elements:"
							class="fa  fa-info-circle">
						</span>
					</label>
					<p id="userAccManEmailDropdown" class="collapse">
						${emailRestriction}
					</p>
					
					<input type="email"
						id="userAccManEmailTXT" 
						class="form-control" 
						required="required"
						value="${emailValue}"><br />
				
					<input type="button" 
						id="userAccManUpdateDetailsBTN" 
						class="btn btn-primary" 
						value="Update account!"><br/>
							
				</div>
				
				<div class="panel-body" id="userAccManUpdateDetailsErrorDIV">
					
				</div>
			</div> <!-- panel primary basic details end -->
				
			<div class="panel panel-danger">
				<div class="panel-heading">
					<h4><b>Account password change:</b></h4>
					
				</div>
				
				<div class="panel-body">
					<label for="userAccManPassword1TXT" id="userAccManPassword1TXTLabel">
						Password: <br />
						<a href="#userAccManPasswordDropdown" 
								data-toggle="collapse">Click for more information:
						</a>
						<span data-toggle="popover"
							data-html="true"
							data-trigger="hover"
							data-content="${passwordRestriction}"
							title="The email address can contain the following elements:"
							class="fa  fa-info-circle">
						</span>
					</label>
					<p id="userAccManPasswordDropdown" class="collapse">
						${passwordRestriction}
					</p>
					
					<input type="password"
						id="userAccManPassword1TXT"
						class="form-control"
						required="required"
						pattern="${passwordPattern}"
						name="userAccManPassword1TXTName"
						value="">
					<br />
					
					<input type="password"
						id="userAccManPassword2TXT"
						required="required"
						class="form-control"
						name="userAccManPassword2TXTName"
						value="">
					<br />
					
					<input type="button" 
						id="userAccManChangePasswordBTN" 
						class="btn btn-danger" 
						value="Change password!">
				</div>
				
				<div class="panel-body" id="userAccManChangePasswordErrorsDIV">
					
				</div>
			</div> <!-- panel danger account password end -->
		</div> <!-- colg 12 end -->
	</div> <!-- row end -->
</div><!-- container end -->