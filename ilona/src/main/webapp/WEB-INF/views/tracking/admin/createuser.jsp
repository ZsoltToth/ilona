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
	
	var adminCreateUserLock = true;
	
	$("#createUserButton").click(function(event){
		try {
			event.preventDefault();
			if (adminCreateUserLock == true) {
				adminCreateUserLock = false;
			} else {
				return;
			}
			$("#adminUserCreationError").html("");
			
			function clearInputs() {
				$("#adminCreateUserUseridTXT").val("");
				$("#adminCreateUserUsernameTXT").val("");
				$("#adminCreateUserEmailTXT").val("");
				$("#adminCreateUserPassword1TXT").val("");
				$("#adminCreateUserPassword2TXT").val("");
			}
			
			var inputs = {
				userid: $("#adminCreateUserUseridTXT").val(),
				username: $("#adminCreateUserUsernameTXT").val(),
				password: [$("#adminCreateUserPassword1TXT").val(),
				           $("#adminCreateUserPassword2TXT").val()],
				email: $("#adminCreateUserEmailTXT").val()
			};
			
			var dependency = {
				validateInputs : checkInputsValidity
			};
				
			var result = dependency.validateInputs(inputs);
			
			if (result.valid == false) {
				adminCreateUserLock = true;
				$("#adminUserCreationError").html(result.errors);
			} else {
				
				var enabled = false;
				var admin = false;
				var UserEnabled = document.getElementById('creationIsUserEnabled');
				var AdminRole = document.getElementById('creationIsAdmin');
				if(UserEnabled.checked) {
					enabled = true;
				}
				if(AdminRole.checked) {
					admin = true;
				}
	
				$.ajax({
					async : true,
					type : "POST",
					beforeSend : function(xhr) {
						xhr.setRequestHeader($("meta[name='_csrf_header']").attr("content"),
								$("meta[name='_csrf']").attr("content"));
					},
					url : "<c:url value='tracking/admin/registeruser'></c:url>",
					data : {
						userid : $("#adminCreateUserUseridTXT").val(),
						username : $("#adminCreateUserUsernameTXT").val(),
						email : $("#adminCreateUserEmailTXT").val(),
						password : $("#adminCreateUserPassword1TXT").val(),
						enabled : enabled,
						adminRole : admin
					},
					success : function(result, status, xhr) {
						try {
							adminCreateUserLock = true;
							switch(result.responseState) {
							case 100: {
								$("#adminUserCreationError")
									.html("<p class='bg-primary'>User has been created successfully!</p>");
								break;
							}
							case 200: {
								$("#adminUserCreationError")
								.html("<p class='bg-primary'>User data was invalid!</p>");
								break;
							}
							case 300: {
								$("#adminUserCreationError")
								.html("<p class='bg-primary'>User data was invalid!</p>");
								break;
							}
							case 400: {
								$("#adminUserCreationError")
								.html("<p class='bg-primary'>Service error!</p>");
								break;
							}
							case 600: {
								$("#adminUserCreationError")
								.html("<p class='bg-primary'>A user is already exists with id: " 
										+ $("#adminCreateUserUseridTXT").val() + "</p>");
								break;
							}
							default: {
								$("#mainpageSignupErrorDIV")
								.html("<p class='bg-primary'>Service error!</p>");
								break;
							}
							}
							clearInputs();
						} catch(err) {
							console.log(err);
						}
						
						
					},
					error : function(xhr, status, error) {
						try {
							adminCreateUserLock = true;
							clearInputs();
							$("#adminUserCreationError")
								.html("<p class='text-danger bg-primary'>An error occured!</p>");
							console.log("" + status + " " + error); 
						} catch(err) {
							console.log(err);
						}
					},
					timeout : 10000
				});
			}	
		} catch(error) {
			try {
				adminCreateUserLock = true;
				$("#adminUserCreationError")
					.html("<p class='text-danger bg-primary'>An error occured!</p>");
				console.log(error); 
			} catch(err) {
				console.log(err);
			}
		}
		
	});
</script>

<jsp:directive.include file="adminNavbar.jsp" />

<div class="container-fluid">
	<div class="row">
		<div class="col-lg-12">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">ILONA CREATE USER</h3>
				</div>	
				<div class="panel-body">
						<div id="adminUserCreationError">					
						</div>	
											
						<label for="adminCreateUserUseridTXT">Userid: <br />
							<a href="#adminCreateUserUseridDropdown" 
								data-toggle="collapse">Click for more information:
							</a>
							<span data-toggle="popover"
								data-html="true"
								data-trigger="hover"
								data-content="${useridRestriction}"
								title="The userid pattern can contain the following elements:"
								class="fa  fa-info-circle">
							</span>
						</label>
						<p id="adminCreateUserUseridDropdown" class="collapse">
							${useridRestriction}
						</p>
							
						<input type="text" 
							class="form-control"
							id="adminCreateUserUseridTXT"
							required="required"
							placeholder="Please type in the userid!"
							pattern = "${useridPattern}"
							name="userid"><br />
							
						<label for="adminCreateUserUsernameTXT">Username: <br />
							<a href="#adminCreateUserUsernameDropdown" 
								data-toggle="collapse">Click for more information:
							</a>
							<span
								data-toggle="popover"
								data-html="true"
								data-trigger="hover"
								data-content="${usernameRestriction}"
								title="The userid pattern can contain the following elements:"
								class="fa  fa-info-circle">
							</span>
						</label>
						<p id="adminCreateUserUsernameDropdown" class="collapse">
							${usernameRestriction}
						</p>
						
						<input type="text" 
							class="form-control"
							id="adminCreateUserUsernameTXT"
							required="required"
							placeholder="Please type in the username!"
							pattern="${usernamePattern}"
							name="username"><br />	
							
						<label for="adminCreateUserEmailTXT">Email address: <br />
							<a href="#adminCreateUserEmailDropdown" 
								data-toggle="collapse">Click for more information:
							</a>
							<span
								data-toggle="popover"
								data-html="true"
								data-trigger="hover"
								data-content="${emailRestriction}"
								class="fa  fa-info-circle">
							</span>
						</label>
						<p id="adminCreateUserEmailDropdown" class="collapse">
							${emailRestriction}
						</p>		
						
						<input type="email" 
							class="form-control"
							id="adminCreateUserEmailTXT"
							required="required"
							placeholder="Please type in the email address!"
							name="email"><br />		
							
						<label for="adminCreateUserPassword1TXT">Password: <br />
							<a href="#adminCreateUserPasswordDropdown" 
								data-toggle="collapse">Click for more information:
							</a>
							<span
								data-toggle="popover"
								data-html="true"
								data-trigger="hover"
								data-content="${passwordRestriction}"
								class="fa  fa-info-circle">
							</span>
						</label>
						<p id="adminCreateUserPasswordDropdown" class="collapse">
							${passwordRestriction}
						</p>
							
						<input type="password" 
							class="form-control"
							id="adminCreateUserPassword1TXT"
							required="required"
							placeholder="Please type in the password!"
							pattern="${passwordPattern}"
							name="password"> <br />
							
						<input type="password" 
							class="form-control"
							id="adminCreateUserPassword2TXT"
							required="required"
							placeholder="Please type in the password again!"
							pattern="${passwordPattern}"
							name="password"> <br />
							
						<label for="creationIsUserEnabled">Enabled: <br />
							<a href="#adminCreateUserEnabledDropdown" 
								data-toggle="collapse">Click for more information:
							</a>
							<span
								data-toggle="popover"
								data-html="true"
								data-trigger="hover"
								data-content="${enabledRestriction}"
								class="fa  fa-info-circle">
							</span>
						</label>
						<p id="adminCreateUserEnabledDropdown" class="collapse">
							${enabledRestriction}
						</p>
						<br>
							
						<input type="checkbox" 
							class="form-control-left"
							id="creationIsUserEnabled"
							name="enabled" checked="checked"><br /><br />
							
						<label for="creationIsAdmin">Admin role: <br />
							<a href="#adminCreateUserAdminRoleDropdown" 
								data-toggle="collapse">Click for more information:
							</a>
							<span
								data-toggle="popover"
								data-html="true"
								data-trigger="hover"
								data-content="${userRoleRestriction}"
								class="fa  fa-info-circle">
							</span>
						</label>
						<p id="adminCreateUserAdminRoleDropdown" class="collapse">
							${userRoleRestriction}
						</p>
						<br>
							
						<input type="checkbox" 
							class="form-control-left"
							id="creationIsAdmin"
							name="adminRole"><br/><br/>
		
					<input type="button" id="createUserButton" class="btn btn-primary" value="CreateUser">
				</div>			
			</div>
		</div>
	</div>
</div>