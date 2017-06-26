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

	var adminCreateNewDeviceLock = true;
	
	$("#adminCreateNewDeviceCreateNewDeviceBTN").click(function(event){
		try {
			event.preventDefault();
			if (adminCreateNewDeviceLock == true) {
				adminCreateNewDeviceLock = false;
			} else {
				return;
			}
			var deviceIdElement = document.getElementById("adminCreateNewDeviceDeviceidTXT");
			var deviceNameElement = document.getElementById("adminCreateNewDeviceDeviceNameTXT");
			var deviceTypeElement = document.getElementById("adminCreateNewDeviceDeviceTypeTXT");
			var deviceTypeNameElement = document.getElementById("adminCreateNewDeviceDeviceTypeNameTXT");
			
			function clearDeviceInputs() {
				$("#adminCreateNewDeviceDeviceidTXT").val("");
				$("#adminCreateNewDeviceDeviceNameTXT").val("");
				$("#adminCreateNewDeviceDeviceTypeTXT").val("");
				$("#adminCreateNewDeviceDeviceTypeNameTXT").val("");
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
				adminCreateNewDeviceLock = true;
				$("#adminCreateNewDeviceResultDIV").html(result.errors);
				return;
			}
			
			$.ajax({
				type : "POST",
				async : true,
				beforeSend : function(xhr) {
					xhr.setRequestHeader($("meta[name='_csrf_header']").attr("content"), 
						$("meta[name='_csrf']").attr("content"));
				},
				url : "<c:url value='/tracking/admin/createnewdeviceforuser'></c:url>",
				data : {
					userid : $("#adminCreateNewDeviceOwnerIDHIDDEN").val(),
					deviceid : deviceIdElement.value,
					deviceName : deviceNameElement.value,
					deviceType : deviceTypeElement.value,
					deviceTypeName : deviceTypeNameElement.value
				},
				success : function(result, status, xhr) {
					try {
						adminCreateNewDeviceLock = true;
						switch(result.responseState) {
						case 100: {
							$("#adminCreateNewDeviceResultDIV")
								.html("<p class='bg-primary'>The device has been added successfully!</p>");
							break;
						}
						case 200: {
							$("#adminCreateNewDeviceResultDIV")
							.html("<p class='bg-primary'>Device data was invalid!</p>");
							break;
						}
						case 300: {
							$("#adminCreateNewDeviceResultDIV")
							.html("<p class='bg-primary'>Device data was invalid!</p>");
							break;
						}
						case 400: {
							$("#adminCreateNewDeviceResultDIV")
							.html("<p class='bg-primary'>Service error!</p>");
							break;
						}
						case 600: {
							$("#adminCreateNewDeviceResultDIV")
							.html("<p class='bg-primary'>No user has found with id: "
									+ $("#adminCreateNewDeviceDeviceidTXT").val() + "</p>");
							break;
						}
						case 700: {
							$("#adminCreateNewDeviceResultDIV")
							.html("<p class='bg-primary'>Duplicated device with id: "
									+ $("#adminCreateNewDeviceDeviceidTXT").val() + "</p>");
							break;
						}
						default: {
							$("#adminCreateNewDeviceResultDIV")
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
						adminCreateNewDeviceLock = true;
						clearDeviceInputs();
						$("#adminCreateNewDeviceResultDIV")
						.html("<p class='bg-primary'>Service error!</p>");
						console.log("" + status + " " + error);
					} catch(error) {
						console.log(error);
					}
				}
			});
		} catch(err) {
			try {
				adminCreateNewDeviceLock = true;
				clearDeviceInputs();
				$("#adminCreateNewDeviceResultDIV")
				.html("<p class='bg-primary'>Service error!</p>");
				console.log("" + status + " " + error);
			} catch(error) {
				console.log(error);
			}
		}
	});	
</script>


<jsp:directive.include file="adminNavbar.jsp" />

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">
				<p>Userid: ${deviceOwnerid}</p>
			</div>
			<div class="panel-body">
				<div id="adminCreateNewDeviceResultDIV"></div>
				
				<label for="adminCreateNewDeviceDeviceidTXT">Deviceid: <br /> 
					<a href="#adminCreateNewDeviceDeviceidDropdown" 
						data-toggle="collapse">Click for more information:
					</a>
					<span data-toggle="popover"
						data-html="true"
						data-trigger="hover"
						data-content="${deviceidRestriction}"
						title="The userid can contain the following elements:"
						class="fa  fa-info-circle">
					</span>
				</label>
				<p id="adminCreateNewDeviceDeviceidDropdown" class="collapse">
					${deviceidRestriction}
				</p>
				
				<input type="text"
					class="form-control"
					id="adminCreateNewDeviceDeviceidTXT"
					required="required"
					placeholder="Please type in your userid!"
					pattern="${deviceidPattern}"
					name="userid"> <br />
					
				<label for="adminCreateNewDeviceDeviceNameTXT">Device name: <br />
					<a href="#adminCreateNewDeviceDeviceNameDropdown" 
						data-toggle="collapse">Click for more information:
					</a>
					<span data-toggle="popover"
						data-html="true"
						data-trigger="hover"
						data-content="${deviceNameRestriction}"
						title="The userid can contain the following elements:"
						class="fa  fa-info-circle">
					</span>
				</label>
				<p id="adminCreateNewDeviceDeviceNameDropdown" class="collapse">
					${deviceNameRestriction}
				</p>
					
				<input type="text"
					class="form-control"
					id="adminCreateNewDeviceDeviceNameTXT"
					required="required"
					placeholder="Please type in your userid!"
					pattern="${deviceNamePattern}"
					name="userid"> <br />
					
				<label for="adminCreateNewDeviceDeviceTypeTXT">Device type: <br />
					<a href="#adminCreateNewDeviceDeviceTypeDropdown" 
						data-toggle="collapse">Click for more information:
					</a>
					<span data-toggle="popover"
						data-html="true"
						data-trigger="hover"
						data-content="${deviceTypeRestriction}"
						title="The userid can contain the following elements:"
						class="fa  fa-info-circle">
					</span>
				</label>
				<p id="adminCreateNewDeviceDeviceTypeDropdown" class="collapse">
					${deviceTypeRestriction}
				</p>
					
				<input type="text"
					class="form-control"
					id="adminCreateNewDeviceDeviceTypeTXT"
					required="required"
					placeholder="Please type in your userid!"
					pattern="${deviceTypePattern}"
					name="userid"> <br />
					
				<label for="adminCreateNewDeviceDeviceTypeNameTXT">Device type name: <br />
					<a href="#adminCreateNewDeviceDeviceTypeNameDropdown" 
						data-toggle="collapse">Click for more information:
					</a>
					<span data-toggle="popover"
						data-html="true"
						data-trigger="hover"
						data-content="${deviceTypeNameRestriction}"
						title="The userid can contain the following elements:"
						class="fa  fa-info-circle">
					</span>
				</label>
				<p id="adminCreateNewDeviceDeviceTypeNameDropdown" class="collapse">
					${deviceTypeNameRestriction}
				</p>
					
				<input type="text"
					class="form-control"
					id="adminCreateNewDeviceDeviceTypeNameTXT"
					required="required"
					placeholder="Please type in your userid!"
					pattern="${deviceTypeNamePattern}"
					name="userid"> <br />
				
				<input type="button" id="adminCreateNewDeviceCreateNewDeviceBTN" value="Add Device!" class="btn btn-primary">
				<input type="hidden" id="adminCreateNewDeviceOwnerIDHIDDEN" value="${deviceOwnerid}">
			</div>		
		</div>
	</div>
</div>