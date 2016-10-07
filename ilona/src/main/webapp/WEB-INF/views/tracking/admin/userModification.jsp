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
	
	var adminUserModUpdateUserUsernameRestore = $("#adminUserModUsernameTXT").val();
	var adminUserModUpdateUserEmailRestore = $("#adminUserModEmailTXT").val();
	
	var adminUserModUpdateUserDetailsLock = true;
	var adminAccountModResetAccountExpirationLock = true;
	var adminAccountModPasswordAutoResetLock = true;
	var adminAccountModPasswordChangeLock = true;
	var adminAccountModResetPasswordExpirationLock = true;
	
	var adminAccountModDeletedAttempts = [];
	
	function customFormatDate(date) {
		return date.getFullYear()+ "-" + date.getMonth() + "-" + date.getDate()
		+ " " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds()
		+ " " + date.getMilliseconds();
	}
	
	$(document).ready(function() {
		
		var accountDetails = {
			accountExpiration: ${isAccountNonExpired},
			lastLoginDate: ${lastLoginDate},
			passwordExpiration: ${isPasswordNotExpired},
			passwordValidUntil: ${passwordValidUntil}
		};
		
		if(accountDetails.accountExpiration == true) {
			$("#adminUserModUpdateUserDetailsAccountExpirationDIV")
			.html("<p class='text-success'>The account is not expired!</p>");
		} else {
			$("#adminUserModUpdateUserDetailsAccountExpirationDIV")
			.html("<p class='text-danger'>The account is expired!</p>");
		}
	
		var date = new Date(accountDetails.lastLoginDate);
		$("#adminUserModLastLoginDateDiv")
			.html(customFormatDate(date));
		
		date = new Date(accountDetails.passwordValidUntil);
		$("#adminUserManPasswordValidUntilParagh").html(customFormatDate(date));
		
		if(accountDetails.passwordExpiration == true) {
			$("#adminUserModPasswordExpirationDIV")
			.html("<p class='text-success'>The password is not expired!</p>");
		} else {
			$("#adminUserModPasswordExpirationDIV")
			.html("<p class='text-danger'>The password is expired!</p>");
		}
	});
	
	$("#adminUserModUpdateUserDetailsBTN").click(function(event){
		try {
			event.preventDefault();
			if (adminUserModUpdateUserDetailsLock == true) {
				adminUserModUpdateUserDetailsLock = false;
			} else {
				return;
			}
			var inputs = {
				userid: $("#adminUserModUseridHidden").val(),
				username: $("#adminUserModUsernameTXT").val(),
				email: $("#adminUserModEmailTXT").val()
			};
				
			var dependency = {
				validateInputs : checkInputsValidity
			};
				
			var result = dependency.validateInputs(inputs);
			
			if(result.valid == false) {
				adminUserModUpdateUserDetailsLock = true;
				$("#adminUserModUserDetailsResultDIV").html(result.errors);
				$("#adminUserModUsernameTXT").val(adminUserModUpdateUserUsernameRestore);
				$("#adminUserModEmailTXT").val(adminUserModUpdateUserEmailRestore);
				return;
			}
			
			var enabledCB = document.getElementById("adminUserModEnabledCheckbox");
			var adminRoleCB = document.getElementById("adminUserModAdminRoleCheckbox");
			
			var isEnabled = false;
			var hasAdminRole = false;
			
			if(enabledCB.checked) {
				isEnabled = true;
			}
			
			if(adminRoleCB.checked) {
				hasAdminRole = true;
			}
			
			$.ajax({
				type : "POST",
				async : true,
				url : "<c:url value='/tracking/admin/usermodchangeaccountdetails'></c:url>",
				beforeSend : function(xhr) {
					xhr.setRequestHeader($("meta[name='_csrf_header']").attr("content"), 
						$("meta[name='_csrf']").attr("content"));
				},
				data : {
					userid : $("#adminUserModUseridHidden").val(),
					username : $("#adminUserModUsernameTXT").val(),
					email : $("#adminUserModEmailTXT").val(),
					enabled : isEnabled,
					adminRole : hasAdminRole
				},
				success : function(result, status, xhr) {
					try {
						adminUserModUpdateUserDetailsLock = true;
						switch(result.responseState) {
						case 100: {
							$("#adminUserModUserDetailsResultDIV")
								.html("<p class='bg-primary'>User updated!</p>");
							adminUserModUpdateUserUsernameRestore = $("#adminUserModUsernameTXT").val();
							adminUserModUpdateUserEmailRestore = $("#adminUserModEmailTXT").val();					
							return;
						}
						case 300: {
							$("#adminUserModUserDetailsResultDIV")
							.html("<p class='bg-primary'>User data was invalid!</p>");
							break;
						}
						case 400: {
							$("#adminUserModUserDetailsResultDIV")
							.html("<p class='bg-primary'>Service error!</p>");
							break;
						}
						default: {
							$("#adminUserModUserDetailsResultDIV")
							.html("<p class='bg-primary'>Service error!</p>");
							break;
						}
						}
						$("#adminUserModUsernameTXT").val(adminUserModUpdateUserUsernameRestore);
						$("#adminUserModEmailTXT").val(adminUserModUpdateUserEmailRestore);
					} catch(error) {
						console.log(error);
					}
				},
				error : function(xhr, status, error) {
					try {
						adminUserModUpdateUserDetailsLock = true;
						$("#adminUserModUserDetailsResultDIV").html("<p class='bg-primary'>Service error!</p>");
						console.log("" + status + " " + error);
					} catch(error) {
						console.log(error);
					}
				}
			});
		} catch(error) {
			try {
				admiUserModUpdateUserDetailsLock = true;
				$("#adminUserModUserDetailsResultDIV").html("<p class='bg-primary'>Service error!</p>");
				console.log(error);
			} catch(err) {
				console.log(err);
			}
		}
	});
	
	$("#adminUserModManautomaticPasswordResetBTN").click(function(event){
		try {
			event.preventDefault();
			if (adminAccountModPasswordAutoResetLock == true) {
				adminAccountModPasswordAutoResetLock = false;
			} else {
				return;
			}
			$.ajax({
				type : "POST",
				async : true,
				url : "<c:url value='/tracking/admin/automatedpasswordreset'></c:url>",
				beforeSend : function(xhr) {
					xhr.setRequestHeader($("meta[name='_csrf_header']").attr("content"), 
						$("meta[name='_csrf']").attr("content"));
				},
				data : {
					userid : $("#adminUserModUseridHidden").val()
				},
				success : function(result, status, xhr) {
					try {
						adminAccountModPasswordAutoResetLock = true;
						$("#adminUserModPasswordResetResultDIV")
							.html("<p class='bg-primary'>Password has been changed and sended!</p>");
						$("#adminUserManPasswordValidUntilParagh").html(
								customFormatDate(new Date(result)));
						$("#adminUserModPasswordExpirationDIV")
							.html("<p class='text-success'>The password is not expired!</p>");
						$("#adminUserModPassword1TXT").val("");
						$("#adminUserModPassword2TXT").val("");
					} catch(error) {
						console.log(error);
					}
				},
				error : function(xhr, status, error) {
					try {
						adminAccountModPasswordAutoResetLock = true;
						if (xhr.status == 400) {
							$("#adminUserModPasswordResetResultDIV")
							.html("<p class='bg-primary'>Invalid password format!</p>");
						}
						$("#adminUserModPassword1TXT").val("");
						$("#adminUserModPassword2TXT").val("");
					} catch(err) {
						console.log(err);
					}
				}
			});
		} catch(error) {
			try {
				adminAccountModPasswordAutoResetLock = true;
				$("#adminAccountModUpdateUserDetailsResetAccountExpirationResultDIV")
					.html("");
				console.log(error);
			} catch(err) {
				console.log(err);
			}
		}
	});
	
	$("#adminUserModManualPasswordChangeBTN").click(function(event){
		try {
			event.preventDefault();
			if (adminAccountModPasswordChangeLock == true) {
				adminAccountModPasswordChangeLock = false;
			} else {
				return;
			}
			var inputs = {
				password: [$("#adminUserModPassword1TXT").val(),
				           $("#adminUserModPassword2TXT").val()]
			};
					
			var dependency = {
				validateInputs : checkInputsValidity
			};
					
			var result = dependency.validateInputs(inputs);
			
			if(result.valid == false) {
				adminAccountModPasswordChangeLock = true;
				$("#adminUserModPasswordResetResultDIV").html(result.errors);
				return;
			}
			
			$.ajax({
				type :"POST",
				async : true,
				url : "<c:url value='/tracking/admin/usermodmanualpasswordchange'></c:url>",
				beforeSend : function(xhr) {
					xhr.setRequestHeader($("meta[name='_csrf_header']").attr("content"), 
						$("meta[name='_csrf']").attr("content"));
				},
				data : {
					userid : $("#adminUserModUseridHidden").val(),
					password : $("#adminUserModPassword1TXT").val()
				},
				success : function(result, status, xhr) {
					try {
						adminAccountModPasswordChangeLock = true;
						$("#adminUserModPasswordResetResultDIV")
							.html("<p class='bg-primary'>Password has been changed!</p>");
						$("#adminUserManPasswordValidUntilParagh").html(
								customFormatDate(new Date(result)));
						$("#adminUserModPasswordExpirationDIV")
							.html("<p class='text-success'>The password is not expired!</p>");
						$("#adminUserModPassword1TXT").val("");
						$("#adminUserModPassword2TXT").val("");
					} catch(error) {
						console.log(error);
					}
				},
				error : function(xhr, status, error) {
					try {
						adminAccountModPasswordChangeLock = true;
						if(xhr.status == 400) {
							$("#adminUserModPasswordResetResultDIV")
							.html("<p class='bg-primary'>Invalid password format!</p>");
						}
						$("#adminUserModPassword1TXT").val("");
						$("#adminUserModPassword2TXT").val("");
					} catch(err) {
						console.log(err);
					}
				}
			});
		} catch(error) {
			try {
				adminAccountModPasswordChangeLock = true;
				$("#adminUserModPasswordResetResultDIV")
					.hrml("<p class='bg-primary'>Service error!</p>");
				console.log(error);
			} catch(err) {
				console.log(err);
			}
		}
	});
	
	$("#adminUserModResetAccountExpirationBTN").click(function(event){
		try {
			event.preventDefault();
			if (adminAccountModResetAccountExpirationLock == true) {
				adminAccountModResetAccountExpirationLock = false;
			} else {
				return;
			}
			$.ajax({
				type : "POST",
				async : true,
				beforeSend : function(xhr) {
					xhr.setRequestHeader($("meta[name='_csrf_header']").attr("content"), 
						$("meta[name='_csrf']").attr("content"));
				},
				data : {
					userid : $("#adminUserModUseridHidden").val()
				},
				url : "<c:url value='/tracking/admin/usermodresetaccountexpiration'></c:url>",
				success : function(result, status, xhr) {
					try {
						adminAccountModResetAccountExpirationLock = true;
						$("#adminUserModUpdateUserDetailsAccountExpirationDIV")
						.html("<p class='text-success'>The account is not expired!</p>");
						$("#adminUserModLastLoginDateDiv")
						.html(customFormatDate(new Date(result)));
					} catch(error) {
						console.log(error);
					}
				},
				error : function(xhr, status, error) {
					try {
						adminAccountModResetAccountExpirationLock = true;
						$("#adminAccountModUpdateUserDetailsResetAccountExpirationResultDIV")
						 .html("Service error!");
						console.log("" + status + " " + error);
					} catch(err) {
						console.log(err);
					}
				}		 
			});
		} catch(error) {
			try {
				adminAccountModResetAccountExpirationLock = true;
				$("#adminAccountModUpdateUserDetailsResetAccountExpirationResultDIV")
					.hrml("");
				console.log(error);
			} catch(err) {
				console.log(err);
			}
		}
	});
	
	$("#adminUserModResetPasswordExpirationBTN").click(function(event) {
		try {
			event.preventDefault();
			if (adminAccountModResetPasswordExpirationLock == true) {
				adminAccountModResetPasswordExpirationLock = false;
			} else {
				return;
			}
			$.ajax({
				type : "POST",
				async : true,
				beforeSend : function(xhr) {
					xhr.setRequestHeader($("meta[name='_csrf_header']").attr("content"), 
						$("meta[name='_csrf']").attr("content"));
				},
				data : {
					userid : $("#adminUserModUseridHidden").val()
				},
				url : "<c:url value='/tracking/admin/usermodresetpasswordexpiration'></c:url>",
				success : function(result, status, xhr) {
					try {
						adminAccountModResetPasswordExpirationLock = true;
						$("#adminUserModPasswordResetResultDIV")
							.html("<p class='bg-primary'>Pasword expirity updated!</p>");
						$("#adminUserManPasswordValidUntilParagh").html(
								customFormatDate(new Date(result)));
						$("#adminUserModPasswordExpirationDIV")
							.html("<p class='text-success'>The password is not expired!</p>");
						
					} catch(error) {
						console.log(error);
					}					
				},
				error : function(xhr, status, error) {
					try {
						adminAccountModResetPasswordExpirationLock = true;
						$("#adminUserModPasswordResetResultDIV")
							.html("<p class='bg-primary'>Service error!</p>");
						console.log("" + status + " " + error);
					} catch(err) {
						console.log(err);
					}
				}		 
			});
		} catch(error) {
			try {
				adminAccountModResetPasswordExpirationLock = true;
				$("#adminUserModPasswordResetResultDIV")
					.html("<p class='bg-primary'>Service error!</p>");
				console.log(error);
			} catch(err) {
				console.log(err);
			} 
		}
		
	});
	
	$("#adminUserManLoginAttemptsClearBTN").click(function(event){
		event.preventDefault();
		var attempts = document.getElementById("adminUserManLoginAttemptsSelect");
		while(attempts.length != 0) {
			attempts.remove(0);
		}
	});
	
	$("#adminUserManLoginAttemptsDeleteSelectedBTN").click(function(event){
		event.preventDefault();
		var attempts = document.getElementById("adminUserManLoginAttemptsSelect");
		var selected = 1;
		var maxlimit =attempts.length * attempts.length;
		while(selected != 0 || maxlimit == 0) {
			selected = 0;
			for(var i = 0; i < attempts.length; i++) {
				if(attempts[i].selected) {
					attempts.remove(i);
					selected++;
				}
			}
			maxlimit--;
		}
	});
	
	$("#adminUserManLoginAttemptsConfirmChangesBTN").click(function(event){
		event.preventDefault();
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		var attempts = document.getElementById("adminUserManLoginAttemptsSelect");
		var attemptArray = [];
		for (var i = 0; i < attempts.length; i++) {
			attemptArray[i] = attempts[i].value;
		}	

		$.ajax({
			type : "POST",
			async : true,
			beforeSend : function(xhr) {
				xhr.setRequestHeader(header, token);
			},
			data : {
				userid : $("#adminUserModUseridHidden").val(),
				attempts : attemptArray
			},
			url : "<c:url value='/tracking/admin/usermodupdateloginattempts'></c:url>",
			success : function(result, status, xhr) {
				alert("success!");
				
			},
			error : function(xhr, status, error) {
				alert("error!"+status+error);
			}		 
		});
	});
	
</script>

<jsp:directive.include file="adminNavbar.jsp" />

<div class="row">
	<div class="col-lg-6">
		<div class="panel panel-default">
			<div class="panel-heading">
				User base details:
			</div>
			<div class="panel-body">
				<div id="adminUserModUserDetailsResultDIV"></div>
				<label for="adminUserModUseridTXT">Userid:
				</label>
					
				<input type="text" 
					class="form-control"
					id="adminUserModUseridTXT"
					required="required"
					name="userid"
					readonly="readonly"
					value="${Userid}"><br />
					
				<input type="hidden" id="adminUserModUseridHidden" value="${Userid}">
					
				<label for="adminUserModUsernameTXT">Username: <br />
					<a href="#adminUserModUsernameDropdown" 
						data-toggle="collapse">Click for more information:
					</a>
					<span data-toggle="popover"
						data-html="true"
						data-trigger="hover"
						data-content="${usernameRestriction}"
						title="The username pattern can contain the following elements:"
						class="fa  fa-info-circle">
					</span>
				</label>
				<p id="adminUserModUsernameDropdown" class="collapse">
					${usernameRestriction}
				</p>
					
				<input type="text" 
					class="form-control"
					id="adminUserModUsernameTXT"
					required="required"
					placeholder="Please type in the new username!"
					name="username"
					value="${Username}"><br />
					
				<label for="adminUserModEmailTXT">Email address: <br />
					<a href="#adminUserModEmailDropdown" 
						data-toggle="collapse">Click for more information:
					</a>
					<span data-toggle="popover"
						data-html="true"
						data-trigger="hover"
						data-content="${emailRestriction}"
						title="The email pattern can contain the following elements:"
						class="fa  fa-info-circle">
					</span>
				</label>
				<p id="adminUserModEmailDropdown" class="collapse">
					${emailRestriction}
				</p>	
				
				<input type="email" 
					class="form-control"
					id="adminUserModEmailTXT"
					required="required"
					placeholder="Please type in the new email!"
					name="email"
					value="${Email}"><br />
				
				<label for="adminUserModEnabledCheckbox">Enabled: <br />
					<a href="#adminUserModEnabledDropdown" 
						data-toggle="collapse">Click for more information:
					</a>
					<span data-toggle="popover"
						data-html="true"
						data-trigger="hover"				
						data-content="${enabledRestriction}"
						class="fa  fa-info-circle">
					</span>
				</label> <br />
				<p id="adminUserModEnabledDropdown" class="collapse">
					${enabledRestriction}
				</p>
					
				<input type="checkbox" 
					id="adminUserModEnabledCheckbox"
					required="required"
					${Enabled}
					name="isEnabled"><br />
				
				<label for="adminUserModAdminRoleCheckbox">Admin role: <br />
					<a href="#adminUserModAdminRoleDropdown" 
						data-toggle="collapse">Click for more information:
					</a>
					<span data-toggle="popover"
						data-html="true"
						data-trigger="hover"
						${Enabled}
						data-content="${adminRestriction}"
						class="fa  fa-info-circle">
					</span>
				</label> <br />
				<p id="adminUserModAdminRoleDropdown" class="collapse">
					${adminRestriction}
				</p>
				
				<input type="checkbox" 
					id="adminUserModAdminRoleCheckbox"
					required="required"
					name="isAdmin"
					${IsAdmin}><br /><br />
					
				<input type="button" class="btn btn-default" value="Update account details!" id="adminUserModUpdateUserDetailsBTN">
				<br /><br /><br />
				
				<div id="adminUserModUpdateUserDetailsAccountExpirationDIV"></div>
				
				<p>Account valid until:</p>					
				<div id ="adminUserModLastLoginDateDiv"></div><br />
				
				<input type="button" class="btn btn-default" value="Reset account expiration!" id="adminUserModResetAccountExpirationBTN"><br />
				<div id="adminAccountModUpdateUserDetailsResetAccountExpirationResultDIV"></div>					
			</div>
		</div>
	</div>
	
	<div class="col-lg-6">
		<div class="panel panel-default">
			<div class="panel-heading">
				User password change:
			</div>
			<div class="panel-body">
				<label for="creationUserid">Password: <br />
					<a href="#adminUserModPasswordDropdown" 
						data-toggle="collapse">Click for more information:
					</a>
					<span data-toggle="popover"
						data-html="true"
						data-trigger="hover"
						data-content="${passwordRestriction}"
						title="The userid pattern can contain the following elements:"
						class="fa  fa-info-circle">
					</span>
				</label>
				<p id="adminUserModPasswordDropdown" class="collapse">
					${passwordRestriction}
				</p>
					
				<input type="password" 
					class="form-control"
					id="adminUserModPassword1TXT"
					required="required"
					placeholder="Please type in the new password"
					pattern = "${useridPattern}"
					name="password1"><br />			
					
				<input type="password" 
					class="form-control"
					id="adminUserModPassword2TXT"
					required="required"
					placeholder="Please type in the new password again!"
					pattern = "${useridPattern}"
					name="password2"><br />
					
				<input type="button" value="Change password!" id="adminUserModManualPasswordChangeBTN">
				<input type="button" value="Generate and send password!" id="adminUserModManautomaticPasswordResetBTN">
				<br /><br />
				
				<div id="adminUserModPasswordResetResultDIV"></div>
				
				<p><b>Account expiration details:</b></p>
				<div id="adminUserModPasswordExpirationDIV"></div>
				
				Account valid until:
				<p id="adminUserManPasswordValidUntilParagh"></p>
				<br />
				<input type="button" value="Reset password expiration!" id="adminUserModResetPasswordExpirationBTN">			
			</div>
		</div>
	</div>
	
	<div class="col-lg-6">
		<div class="panel panel-default">
			<div class="panel-heading">
				Account locked details:
			</div>
			<div class="panel-body">
				<p>Bad login attempts: (Hold ctrl to select more!)</p>
				<select multiple class="form-control" id="adminUserManLoginAttemptsSelect" size="15">
       				<c:forEach items="${loginAttempts}" var="attempt">
       					<option value="${attempt.formatMilliseconds}">${attempt.formatDate}</option>
       				</c:forEach>
      			</select> <br />		
      			<input type="button" value="Clear all!" id="adminUserManLoginAttemptsClearBTN"> 
      			<input type="button" value="Clear selected!" id="adminUserManLoginAttemptsDeleteSelectedBTN"> 
      			<input type="button" value="Confirm changes!" id="adminUserManLoginAttemptsConfirmChangesBTN">		
			</div>
		</div>
	</div>
</div>
