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
<script src="<c:url value='/js/tracking/deviceValidation.js'></c:url>"></script>
<script type="text/javascript">

	$('[data-toggle="tooltip"]').tooltip();
	$('[data-toggle="popover"]').popover();

	var userCreateDeviceCreateDeviceLock = true;
	
	$("#userCreateDeviceCreateDeviceBTN").click(function(event){
		try {
			event.preventDefault();
			
			if(userCreateDeviceCreateDeviceLock == true) {
				userCreateDeviceCreateDeviceLock = false;
			} else {
				return;
			}
			
			$("#userCreateDeviceCreationErrorDIV").html("");
			
			var deviceIdElement = document.getElementById("userCreateDeviceDeviceidTXT");
			var deviceNameElement = document.getElementById("userCreateDeviceDeviceNameTXT");
			var deviceTypeElement = document.getElementById("userCreateDeviceDeviceTypeTXT");
			var deviceTypeNameElement = document.getElementById("userCreateDeviceDeviceTypeNameTXT");
			
			function clearDeviceInputs() {
				$("#userCreateDeviceDeviceidTXT").val("");
				$("#userCreateDeviceDeviceNameTXT").val("");
				$("#userCreateDeviceDeviceTypeTXT").val("");
				$("#userCreateDeviceDeviceTypeNameTXT").val("");
			}
			
			var inputs = {
				deviceid: deviceIdElement.value,
				deviceName: deviceNameElement.value,
				deviceType: deviceTypeElement.value,
				deviceTypeName: deviceTypeNameElement.value
			};
				
			var dependency = {
				validateInputs : checkDevicesInputsValidity
			};
				
			var result = dependency.validateInputs(inputs);
		
			if(result.valid == false) {
				$("#userCreateDeviceCreationErrorDIV").html(result.errors);
				userCreateDeviceCreateDeviceLock = true;
				return;
			}
			
			$.ajax({
				type : "POST",
				async : true,
				beforeSend : function(xhr) {
					xhr.setRequestHeader($("meta[name='_csrf_header']").attr("content"), 
							$("meta[name='_csrf']").attr("content"));
				},
				url : "<c:url value='/tracking/user/createnewdevice'></c:url>",
				data : {
					userid : $("#userCreateDeviceOwneridHidden").val(),
					deviceid : $("#userCreateDeviceDeviceidTXT").val(),
					deviceName : $("#userCreateDeviceDeviceNameTXT").val(),
					deviceType : $("#userCreateDeviceDeviceTypeTXT").val(),
					deviceTypeName : $("#userCreateDeviceDeviceTypeNameTXT").val()
				},
				success : function(result, status, xhr) {
					try {
						userCreateDeviceCreateDeviceLock = true;
						switch(result.responseState) {
						case 100: {
							$("#userCreateDeviceCreationErrorDIV")
								.html("<p class='bg-primary'>The device has been added successfully!</p>");
							break;
						}
						case 200: {
							$("#userCreateDeviceCreationErrorDIV")
							.html("<p class='bg-primary'>Device data was invalid!</p>");
							break;
						}
						case 300: {
							$("#userCreateDeviceCreationErrorDIV")
							.html("<p class='bg-primary'>Device data was invalid!</p>");
							break;
						}
						case 400: {
							$("#userCreateDeviceCreationErrorDIV")
							.html("<p class='bg-primary'>Service error!</p>");
							break;
						}
						case 600: {
							$("#userCreateDeviceCreationErrorDIV")
							.html("<p class='bg-primary'>A device is already exists with id: "
									+ $("#userCreateDeviceDeviceidTXT").val() + "</p>");
							break;
						}
						default: {
							$("#userCreateDeviceCreationErrorDIV")
							.html("<p class='bg-primary'>Service error!</p>");
							break;
						}
						}
						clearDeviceInputs();
					} catch(error) {
						console.log(error);
					}
				},
				error : function(xhr, status, error) {
					try {
						userCreateDeviceCreateDeviceLock = true;
						clearDeviceInputs();				
						$("#userCreateDeviceCreationErrorDIV").html("<p class='bg-primary'>Service error!</p>");
					} catch(error) {
						console.log(error);
					}
				}
			});
		} catch(error) {
			try {
				userCreateDeviceCreateDeviceLock = true;
				$("#userCreateDeviceCreationErrorDIV").html("<p class='bg-primary'>Service error!</p>");
				console.log(error)
			} catch(err) {
				console.log(err);
			}
		}
	});	
</script>

<jsp:directive.include file="userNavbar.jsp" />
<div class="container-fluid">
	<div class="row">
		<div class="col-lg-12">
			<div class="panel panel-default">
				<div class="panel-heading">
					<p>Userid: ${deviceOwnerid}</p>
				</div>
				<div class="panel-body">
					<div id="userCreateDeviceCreationErrorDIV"></div>
										
					<label for="userCreateDeviceDeviceidTXT">Deviceid: <br />
					 	<a href="#userCreateDeviceDeviceidDropdown" 
							data-toggle="collapse">Click for more information:
						</a>
						<span data-toggle="popover"
							data-html="true"
							data-trigger="hover"
							data-content="${deviceidRestriction}"
							title="The deviceid can contain the following elements:"
							class="fa  fa-info-circle">
						</span>
					</label>
					<p id="userCreateDeviceDeviceidDropdown" class="collapse">
						${deviceidRestriction}
					</p>
						
					<input type="text"
						class="form-control"
						id="userCreateDeviceDeviceidTXT"
						required="required"
						placeholder="Please type in your userid!"
						pattern="${deviceidPattern}"
						name="deviceid"> <br />
						
					<label for="userCreateDeviceDeviceNameTXT">Device name: <br />
						<a href="#userCreateDeviceDeviceNameDropdown" 
							data-toggle="collapse">Click for more information:
						</a>
						<span data-toggle="popover"
							data-html="true"
							data-trigger="hover"
							data-content="${deviceNameRestriction}"
							title="The device name can contain the following elements:"
							class="fa  fa-info-circle">
						</span>
					</label>
					<p id="userCreateDeviceDeviceNameDropdown" class="collapse">
						${deviceNameRestriction}
					</p>
						
					<input type="text"
						class="form-control"
						id="userCreateDeviceDeviceNameTXT"
						required="required"
						placeholder="Please type in your userid!"
						pattern="${deviceNamePattern}"
						name="devicename"> <br />
						
					<label for="userCreateDeviceDeviceTypeTXT">Device type: <br />
						<a href="#userCreateDeviceDeviceTypeDropdown" 
							data-toggle="collapse">Click for more information:
						</a>
						<span data-toggle="popover"
							data-html="true"
							data-trigger="hover"
							data-content="${deviceTypeRestriction}"
							title="The device type can contain the following elements:"
							class="fa  fa-info-circle">
						</span>
					</label>
					<p id="userCreateDeviceDeviceTypeDropdown" class="collapse">
						${deviceTypeRestriction}
					</p>
						
					<input type="text"
						class="form-control"
						id="userCreateDeviceDeviceTypeTXT"
						required="required"
						placeholder="Please type in your userid!"
						pattern="${deviceTypePattern}"
						name="devicetype"> <br />
						
					<label for="userCreateDeviceDeviceTypeNameTXT">Device type name: <br />
						<a href="#userCreateDeviceDeviceTypeNameDropdown" 
							data-toggle="collapse">Click for more information:
						</a>
						<span data-toggle="popover"
							data-html="true"
							data-trigger="hover"
							data-content="${deviceTypeNameRestriction}"
							title="The device type name can contain the following elements:"
							class="fa  fa-info-circle">
						</span>
					</label>
					<p id="userCreateDeviceDeviceTypeNameDropdown" class="collapse">
						${deviceTypeNameRestriction}
					</p>
						
					<input type="text"
						class="form-control"
						id="userCreateDeviceDeviceTypeNameTXT"
						required="required"
						placeholder="Please type in your userid!"
						pattern="${deviceTypeNamePattern}"
						name="devicetypename"> <br />
					
					<input type="button" id="userCreateDeviceCreateDeviceBTN" value="Add Device!" class="btn btn-primary">
					<input type="hidden" id="userCreateDeviceOwneridHidden" value="${deviceOwnerid}">
				</div>
				<div>
					<c:if test="${executionError != null}">
						<p class="bg-primary">Service error device cannot be loaded!</p>
					</c:if>			
				</div>
			</div>
	
		</div>
	</div>
</div>