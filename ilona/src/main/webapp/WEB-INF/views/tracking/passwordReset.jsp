<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@page session="true"%>
<%@ page isELIgnored="false"%>

<script src="<c:url value='/js/tracking/validation.js'></c:url>"></script>
<script type="text/javascript">

	var passwordResetNavigationLock = true;
	var  passwordResetChangePasswordLock = true;
	
	$('[data-toggle="tooltip"]').tooltip();
	$('[data-toggle="popover"]').popover();
	
	$(".passwordResetNavigationClass").click(function(event){
		try {
			event.preventDefault();
			if (passwordResetNavigationLock == true) {
				passwordResetNavigationLock = false;
			} else {
				return;
			}
			
			$.ajax({
				async : true,
				type : "POST",
				url : $(this).attr('href'),
				beforeSend : function(xhr) {
					xhr.setRequestHeader($("meta[name='_csrf_header']").attr("content"),
							$("meta[name='_csrf']").attr("content"));
				},
				success : function(result, status, xhr) {
					try {
						passwordResetNavigationLock = true;
						$("#page-wrapper").html(result);
					} catch(error) {
						console.log(error);
					}
				},
				error : function(xhr, status, error) {
					try {
						passwordResetNavigationLock = true;
						$("#userNavbarNavigationErrorDIV").html("<h2>The tracking service is unreachable!<h2>");
					} catch(error) {
						console.log(error);
					}
				},
				timeout : 10000
			});
		} catch(error) {
			try {
				passwordResetNavigationLock = true;
				$("#userNavbarNavigationErrorDIV").html("Service error!<h2>");
			} catch(err) {
				console.log(err);
			}		
		}
	});
	
	$("#passwordResetPasswordBTN").click(function(event){
		try {
			event.preventDefault();
			if (passwordResetChangePasswordLock == true) {
				passwordResetChangePasswordLock = false;
			} else {
				return;
			}
			$("#passwordResetChangePasswordErrorsDIV").html("");
			
			function clearPasswordFields() {
				$("#passwordResetPassword1TXT").val("");
				$("#passwordResetPassword2TXT").val("");
			}
			
			var inputs = {
				password: [$("#passwordResetPassword1TXT").val(),
				           $("#passwordResetPassword2TXT").val()]
			};
				
			var dependency = {
				validateInputs : checkInputsValidity
			};
				
			var result = dependency.validateInputs(inputs);
	
			if(false) {//(result.valid == false) {
				$("#passwordResetChangePasswordErrorsDIV").html(result.errors);
				passwordResetChangePasswordLock = true;
			} else {
				$.ajax({
					type : "POST",
					async : true,
					url : "<c:url value='/tracking/expiredpasswordchange'></c:url>",
					beforeSend : function(xhr) {
						xhr.setRequestHeader($("meta[name='_csrf_header']").attr("content"),
								$("meta[name='_csrf']").attr("content"));
					},
					data : {
						oldPassword: $("#passwordResetOldPasswordTXT").val(),
						newPassword1: $("#passwordResetPassword1TXT").val(),
						newPassword2: $("#passwordResetPassword2TXT").val()
					},
					success : function(result, status, xhr) {
						try {
							passwordResetChangePasswordLock = true;
							$("#page-wrapper").html(result);
						} catch(err) {
							console.log(err);
						}			
					},
					error : function(xhr, status, error) {
						passwordResetChangePasswordLock = true;
						
						var result;
						try {
							result = JSON.parse(xhr.responseText);
						} catch(err) {
							
							$("#passwordResetChangePasswordErrorsDIV")
							.html("<p class='bg-primary'>Error!</p>");
						}
						console.log(xhr);
						if(xhr.status == 400) {
							switch(result.responseState) {						
							case 200: {
								$("#passwordResetChangePasswordErrorsDIV")
								.html("<p class='bg-primary'>The old password doesnt match with the current password!</p>");
								break;
							}
							case 300: {
								$("#passwordResetChangePasswordErrorsDIV")
								.html("<p class='bg-primary'>Password format is invalid! Please see the description!</p>");						
								break;
							}
							case 400: {
								$("#passwordResetChangePasswordErrorsDIV")
								.html("<p class='bg-primary'>Service error!</p>");
								break;
							}
							case 600: {
								$("#passwordResetChangePasswordErrorsDIV")
								.html("<p class='bg-primary'>The new two password fields is not equal!</p>");
								break;
							}		
							default: {
								$("#passwordResetChangePasswordErrorsDIV")
								.html("<p class='bg-primary'>Service error!</p>");
								break;
							}
							}
						} else {
							$("#passwordResetChangePasswordErrorsDIV")
							.html("<p class='bg-primary'>Service error!</p>");
						}
					}
				});
			}
		} catch(err) {
			try {
				passwordResetChangePasswordLock = true;
				clearPasswordFields();
				$("#passwordResetChangePasswordErrorsDIV").html("<p class='text-danger bg-primary'>Tracking service error!</p>"+error);
			} catch(error) {
				console.log(error);
			}
		}
	});

</script>

<!-- CSRF Protection token -->
<meta name="_csrf" content="${_csrf.token}" />
<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}" />

<nav class="navbar navbar-inverse" id="passresetPageNavigationDIV">
	<div class="container-fluid">
		<div class="navbar-header">			
			<a class="navbar-brand">ILONA - Password reset</a>			
		</div>	
		<ul class="nav navbar-nav navbar-left">			
			<li>
				<a  class="passwordResetNavigationClass"
					href="<c:url value='/tracking/logout'></c:url>">
					<span class="glyphicon glyphicon-log-in"></span> Logout
				</a>
			</li>
		</ul>
	</div>		
</nav>

<div class="container-fluid">
	<div class="row">
		<div class="col-lg-12">
			<div>
				<div class="panel panel-danger">
				<div class="panel-heading">
					<h4><b>The password is expired! You need to change the password!</b></h4>				
				</div>				
				<div class="panel-body">
				<label for="passwordResetOldPasswordTXT">Old password:</label>
					<input type="password"
						id="passwordResetOldPasswordTXT"
						class="form-control"
						required="required"
						pattern="${passwordPattern}"
						value="">
					<br />
				
					<label for="passwordResetPassword1TXT" id="passwordResetPassword1TXTLabel">
						New password: <br />
						<a href="#passwordResetPasswordDropdown" 
								data-toggle="collapse">Click for more information:
						</a>
						<span data-toggle="popover"
							data-html="true"
							data-trigger="hover"
							data-content="${passwordRestriction}"
							title="The password can contain the following elements:"
							class="fa  fa-info-circle">
						</span>
					</label>
					<p id="passwordResetPasswordDropdown" class="collapse">
						${passwordRestriction}
					</p>
					
					<input type="password"
						id="passwordResetPassword1TXT"
						class="form-control"
						required="required"
						pattern="${passwordPattern}"
						value="">
					<br />
					
					<input type="password"
						id="passwordResetPassword2TXT"
						required="required"
						class="form-control"
						value="">
					<br />
					
					<input type="button" 
						id="passwordResetPasswordBTN" 
						class="btn btn-danger" 
						value="Change password!">
				</div>
				
				<div class="panel-body" id="passwordResetChangePasswordErrorsDIV">
					
				</div>
			</div> <!-- panel danger account password end -->
			</div>
		</div>
	</div>
</div>