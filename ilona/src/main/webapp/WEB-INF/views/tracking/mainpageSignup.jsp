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

<script src="<c:url value='/js/trackingGeneralFunctions.js'></c:url>"></script>
<script src="<c:url value='/js/tracking/validation.js'></c:url>"></script>
<script type="text/javascript">
	/*
	 * Popover and tooltip initialization.
	 * This two inicializations must be here, because the architect of the page.
	 * If I put this in the main page, that initialization will not initialize the newest elements.
	 */
	$('[data-toggle="tooltip"]').tooltip();
	$('[data-toggle="popover"]').popover();
	
	var mainpageSignupSignupLock = true;
	
	$("#mainpageSignupRegistrationBTN").click(function(event){
		try {
			event.preventDefault();
			if(mainpageSignupSignupLock == true) {
				mainpageSignupSignupLock = false;
			} else {
				return;
			}
			
			/*
			 * Clear the error div.
			 */
			$("#mainpageSignupErrorDIV").html("");
			
			/*
			 * Validity checking.
			 */
			var userid = document.getElementById("mainpageSignupUseridTXT");
			var username = document.getElementById("mainpageSignupUsernameTXT");
			var password1 = document.getElementById("mainpageSignupPassword1TXT");
			var password2 = document.getElementById("mainpageSignupPassword2TXT");
			var email = document.getElementById("mainpageSignupEmailTXT");
	
			var inputs = {
				userid: userid.value,
				username: username.value,
				password: [password1.value,
				           password2.value],
				email: email.value
			};
			
			var dependency = {
				validateInputs : checkInputsValidity
			};
			
			var result = dependency.validateInputs(inputs);
						
			function eraseInputFields() {
				$("#mainpageSignupUseridTXT").val("");
				$("#mainpageSignupUsernameTXT").val("");
				$("#mainpageSignupPassword1TXT").val("");
				$("#mainpageSignupPassword2TXT").val("");
				$("#mainpageSignupEmailTXT").val("");
			}
			/*
			 * Bad validity.
			 */
			if (result.valid == false) {
				$("#mainpageSignupErrorDIV").html(result.errors);
				mainpageSignupSignupLock = true;
				/*
				 * After good validity.
				 */
			} else{
				$.ajax({
					type : "POST",
					async : true,
					url : "<c:url value='/tracking/registeruser'></c:url>",
					beforeSend : function(xhr) {
						xhr.setRequestHeader($("meta[name='_csrf_header']").attr("content"),
								$("meta[name='_csrf']").attr("content"));
					},
					data : {
						userid : userid.value,
						username : username.value,
						password : password1.value,
						email : email.value,
					},
					success : function(result, status, xhr) {
						try {
						mainpageSignupSignupLock = true;								
						switch(result.responseState) {
						case 100: {
							$("#mainpageSignupErrorDIV")
								.html("<p class='bg-primary'>User has been created successfully!</p>");
							break;
						}
						case 200: {
							$("#mainpageSignupErrorDIV")
							.html("<p class='bg-primary'>User data was invalid!</p>");
							break;
						}
						case 300: {
							$("#mainpageSignupErrorDIV")
							.html("<p class='bg-primary'>User data was invalid!</p>");
							break;
						}
						case 400: {
							$("#mainpageSignupErrorDIV")
							.html("<p class='bg-primary'>Service error!</p>");
							break;
						}
						case 600: {
							$("#mainpageSignupErrorDIV")
							.html("<p class='bg-primary'>A user is already exists with id: " + userid.value + "</p>");
							break;
						}
						default: {
							$("#mainpageSignupErrorDIV")
							.html("<p class='bg-primary'>Service error!</p>");
							break;
						}
						}
						eraseInputFields();
						} catch(error) {
							$("#mainpageSignupErrorDIV")
							.html("<p class='bg-primary'>Service error!</p>");
							console.log(error);
						}
					},
					error : function(xhr, status, error) {
						mainpageSignupSignupLock = true;
						eraseInputFields();
						$("#mainpageSignupErrorDIV").
						html("<p class='bg-primary'>There has been an error with the service!</p>");
					}					
				});
			}
		} catch(error) {
			try {
				mainpageSignupSignupLock = true;
				$("#mainpageSignupErrorDIV").
				html("<p class='bg-primary'>There has been an error with the service!</p>");
				console.log(error);
			} catch(e) {
				console.log(e);
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
					<h3 class="panel-title">ILONA - Tracking module user sign up!</h3>
				</div>
				<div class="panel-body">
						<div id="mainpageSignupErrorDIV">
							
						</div>
							
						<label for="mainpageSignupUseridTXT">Userid: <br />
							<a href="#mainpageSignupUseridDropdown" 
								data-toggle="collapse">Click for more information:
							</a>
							<span data-toggle="popover"
								data-html="true"
								data-trigger="hover"
								data-content="${useridRestriction}"
								title="The userid can contain the following elements:"
								class="fa  fa-info-circle">
							</span>
								
						</label>
						<p id="mainpageSignupUseridDropdown" class="collapse">
							${useridRestriction}
						</p>
							
						<input type="text"
							class="form-control"
							id="mainpageSignupUseridTXT"
							required="required"
							placeholder="Please type in your userid!"
							name="userid"> <br />
							
							
						<label for="mainpageSignupUsernameTXT">Username: <br />
							<a href="#mainpageSignupUsernameDropdown" 
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
						<p id="mainpageSignupUsernameDropdown" class="collapse">
							${usernameRestriction}
						</p>
							
						<input type="text"
							class="form-control"
							id="mainpageSignupUsernameTXT"
							required="required"
							placeholder="Please type in your username!"
							name="username" > <br />
							
						<label for="mainpageSignupPassword1TXT">Password: <br />
							<a href="#mainpageSignupPasswordDropdown" 
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
						<p id="mainpageSignupPasswordDropdown" class="collapse">
							${passwordRestriction}
						</p>
						
						<input type="password"
							class="form-control"
							id="mainpageSignupPassword1TXT"
							required="required"
							placeholder="Please type in your password!"
							name="password"> <br />
							
						<input type="password"
							class="form-control"
							id="mainpageSignupPassword2TXT"
							required="required"
							placeholder="Please type in your password!"
							pattern="[a-zA-Z0-9,.-_?]{6,30}"
							name="password"> <br />
							
						<label for="mainpageSignupEmailTXT">Email address: <br />
							<a href="#mainpageSignupEmailDropdown" 
								data-toggle="collapse">Click for more information:
							</a>
							<span data-toggle="popover"
								data-html="true"
								data-trigger="hover"
								data-content="${emailRestriction}"
								class="fa  fa-info-circle">
							</span>
						</label>
						<p id="mainpageSignupEmailDropdown" class="collapse">
							${emailRestriction}
						</p>
							
						<input type="email"
							class="form-control"
							id="mainpageSignupEmailTXT"
							required="required"
							placeholder="Please type in your email address!"
							name="userid"><br />
		
					<button id="mainpageSignupRegistrationBTN" type="button" class="btn">Register</button>
					<input type="hidden" value = "<c:url value='/tracking/registeruser'></c:url>" 
						id="mainpageSignupRegistrationUrlHidden">		
				</div>	<!-- panel body end -->							
			</div> <!-- panel default end -->
		</div> <!-- col-12 end -->
	</div> <!-- Row end -->
</div> <!-- container fluid end -->