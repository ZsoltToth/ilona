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

	$('[data-toggle="tooltip"]').tooltip();
	$('[data-toggle="popover"]').popover();

	var adminAccManSavedUsername = $("#adminAccManUsernameTXT").val();
	var adminAccManSavedEmail = $("#adminAccManEmailTXT").val();
	
	var adminAccManUpdateDetailsLock = true;
	var adminAccManUpdatePasswordLock = true;
	
	$("#accountUpdateDetails").click(function(event){
		try {
			event.preventDefault();	
			if (adminAccManUpdateDetailsLock == true) {
				adminAccManUpdateDetailsLock = false;
			} else {
				return;
			}
			$("#adminAccManAccountDetailsChangeResultDIV").html("");
			
			var inputs = {
				username: $("#adminAccManUsernameTXT").val(),
				email: $("#adminAccManEmailTXT").val()
			};
				
			var dependency = {
				validateInputs : checkInputsValidity
			};
				
			var result = dependency.validateInputs(inputs);

			if (result.valid == false) {
				adminAccManUpdateDetailsLock = true;
				$("#adminAccManUsernameTXT").val(adminAccManSavedUsername);
				$("#adminAccManEmailTXT").val(adminAccManSavedEmail);
				$("#adminAccManAccountDetailsChangeResultDIV").html(result.errors);
			} else {
				$.ajax({
					type : "POST",
					async : true,
					url : "<c:url value='/tracking/admin/accountchangedetails'></c:url>",
					beforeSend : function(xhr) {
						xhr.setRequestHeader($("meta[name='_csrf_header']").attr("content"), 
							$("meta[name='_csrf']").attr("content"));
					},
					data : {
						userid : $("#adminAccManUseridTXT").val(),
						username : $("#adminAccManUsernameTXT").val(),
						email : $("#adminAccManEmailTXT").val()
					},
					success : function(result, status, xhr) {
						try {
							adminAccManUpdateDetailsLock = true;
							switch(result.responseState) {
							case 100: {
								$("#adminAccManAccountDetailsChangeResultDIV")
									.html("<p class='bg-primary'>User has been modified successfully!</p>");
								adminAccManSavedUsername = $("#adminAccManUsernameTXT").val();
								adminAccManSavedEmail = $("#adminAccManEmailTXT").val();
								return;
							}
							case 200: {
								$("#adminAccManAccountDetailsChangeResultDIV")
								.html("<p class='bg-primary'>User data was invalid!</p>");
								break;
							}
							case 300: {
								$("#adminAccManAccountDetailsChangeResultDIV")
								.html("<p class='bg-primary'>User data was invalid!</p>");
								break;
							}
							case 400: {
								$("#adminAccManAccountDetailsChangeResultDIV")
								.html("<p class='bg-primary'>Service error!</p>");
								break;
							}
							default: {
								$("#adminAccManAccountDetailsChangeResultDIV")
								.html("<p class='bg-primary'>Service error!</p>");
								break;
							}
							}
							$("#adminAccManUsernameTXT").val(adminAccManSavedUsername);
							$("#adminAccManEmailTXT").val(adminAccManSavedEmail);
						} catch(error) {
							console.log(error);
						}
					},
					error : function(xhr, status, error) {
						try {
							adminAccManUpdateDetailsLock = true;
							$("#adminAccManAccountDetailsChangeResultDIV")
								.html("<p class='text-danger bg-primary'>An error occured!</p>");
							console.log("" + status + " " + error);
						} catch(err) {
							console.log(err);
						}
					}
				});
			}
		} catch(error) {
			try {
				adminAccManUpdateDetailsLock = true;
				$("#adminAccManAccountDetailsChangeResultDIV")
					.html("<p class='text-danger bg-primary'>An error occured!</p>");
				console.log(error);
			} catch(err) {
				console.log(err);
			}
		}	
	});
	
	$("#adminAccManPasswordChangeBTN").click(function(event){
		try {
			event.preventDefault();
			if (adminAccManUpdatePasswordLock == true) {
				adminAccManUpdatePasswordLock = false;
			} else {
				return;
			}
			
			$("#adminAccManPasswordChangeResultDIV").html("");
			
			var inputs = {
				password : [$("#adminAccManPasswordChange1TXT").val(),
				            $("#adminAccManPasswordChange2TXT").val()]
			};
					
			var dependency = {
				validateInputs : checkInputsValidity
			};
					
			var result = dependency.validateInputs(inputs);
			
			if(result.valid == false) {
				adminAccManUpdatePasswordLock = true;
				$("#adminAccManPasswordChange1TXT").val("");
				$("#adminAccManPasswordChange2TXT").val("");
				$("#adminAccManPasswordChangeResultDIV").html(result.errors);
			} else {
				$.ajax({
					type : "POST",
					async : true,
					url : "<c:url value='/tracking/admin/accountchangepassword'></c:url>",
					beforeSend : function(xhr) {
						xhr.setRequestHeader($("meta[name='_csrf_header']").attr("content"),
							$("meta[name='_csrf']").attr("content"));
					},
					data : {
						userid : $("#adminAccManUseridTXT").val(),
						password : $("#adminAccManPasswordChange1TXT").val()
					},
					success : function(result, status, xhr) {
						try {
							adminAccManUpdatePasswordLock = true;
							$("#adminAccManPasswordChange1TXT").val("");
							$("#adminAccManPasswordChange2TXT").val("");
							switch(result.responseState) {
							case 100: {
								$("#adminAccManPasswordChangeResultDIV")
									.html("<p class='bg-primary'>The password has been modified successfully!</p>");
								break;
							}
							case 200: {
								$("#adminAccManPasswordChangeResultDIV")
								.html("<p class='bg-primary'>Password data was invalid!</p>");
								break;
							}
							case 300: {
								$("#adminAccManPasswordChangeResultDIV")
								.html("<p class='bg-primary'>Password data was invalid!</p>");
								break;
							}
							case 400: {
								$("#adminAccManPasswordChangeResultDIV")
								.html("<p class='bg-primary'>Service error!</p>");
								break;
							}
							default: {
								$("#adminAccManPasswordChangeResultDIV")
								.html("<p class='bg-primary'>Service error!</p>");
								break;
							}
							}														
						} catch(err) {
							console.log(err);
						}
					},
					error : function(xhr, status, error) {
						try {
							adminAccManUpdatePasswordLock = true;
							$("#adminAccManPasswordChangeResultDIV")
								.html("<p class='text-danger bg-primary'>An error occured!</p>");
							
						} catch(error) {
							console.log(error);
						}
					}
				});
			}
		} catch(err) {
			adminAccManUpdatePasswordLock = true;
			console.log(err);
		}
	});
</script>

<jsp:directive.include file="adminNavbar.jsp" />

<div class="container-fluid">
	<div class="row">
		<div class="col-lg-12">
			<div class="panel panel-primary">
				<div class="panel-heading">
					<h4><b>Account details change:</b></h4>
				</div>
				<div class="panel-body">
					<label for="adminAccManUseridTXT">Userid: READONLY<span class="glyphicon glyphicon-exclamation-sign">
					</span></label>
					<input id="adminAccManUseridTXT"
						type="text" 
						readonly="readonly" 
						required="required"
						class="form-control"
						value="${useridValue}"><br/>
					
					<label for="adminAccManUsernameTXT">Username: <br />
						<a href="#adminAccManUsernameDropdown" 
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
					<p id="adminAccManUsernameDropdown" class="collapse">
						${usernameRestriction}
					</p>
					
					<input type="text" 
						id="adminAccManUsernameTXT" 
						class="form-control"
						required="required"
						pattern="${usernamePattern}"
						value="${usernameValue}"><br />
		
					<label for="adminAccManEmailTXT">Email address:<br />
						<a href="#adminAccManEmailDropdown" 
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
					<p id="adminAccManEmailDropdown" class="collapse">
						${emailRestriction}
					</p>
					
					<input type="email"
						id="adminAccManEmailTXT" 
						class="form-control" 
						required="required"
						value="${emailValue}"><br />
				
					<input type="button" id="accountUpdateDetails" class="btn btn-primary" value="Update account!"><br/>
							
				</div>
				
				<div class="panel-body" id="adminAccManAccountDetailsChangeResultDIV">
					
				</div>
			</div>
				
			<div class="panel panel-danger">
				<div class="panel-heading">
					<h4><b>Account password change:</b></h4>					
				</div>
				
				<div class="panel-body">
					<label for="adminAccManPasswordChange1TXT">Password: <br/>
						<a href="#adminAccManPasswordDropdown" 
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
					<p id="adminAccManPasswordDropdown" class="collapse">
						${passwordRestriction}
					</p>
					
					<input type="password"
						id="adminAccManPasswordChange1TXT"
						class="form-control"
						required="required"
						pattern="${passwordPattern}"
						name="password1"
						placeholder="Please type in the new password!"
						value=""><br />
					
					<input type="password"
						id="adminAccManPasswordChange2TXT"
						required="required"
						class="form-control"
						name="password2"
						placeholder="Please type in the new password again!"
						value=""><br />
					
					<input type="button" id="adminAccManPasswordChangeBTN" class="btn btn-danger" value="Change password!">
				</div>
				
				<div class="panel-body" id="adminAccManPasswordChangeResultDIV">
				
				</div>
			</div>
		</div> <!-- col-lg-12 end -->
	</div> <!-- row end -->
</div> <!-- container fluid end -->
