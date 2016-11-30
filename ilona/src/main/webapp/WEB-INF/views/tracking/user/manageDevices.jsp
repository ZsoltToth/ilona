<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page session="true"%>
<%@ page isELIgnored="false"%>

<!-- CSRF Protection token -->
<meta name="_csrf" content="${_csrf.token}" />
<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}" />

<script src="<c:url value='/js/tracking/deviceValidation.js'></c:url>"></script>
<script src="<c:url value='/js/tracking/tableFunctions.js'></c:url>"></script>



<script type="text/javascript">
	
	$('[data-toggle="tooltip"]').tooltip();
	$('[data-toggle="popover"]').popover();
	
	var userManDevDeviceModificationDeleteDeviceLock = true;
	var userManDevDeviceModificationConfirmModificationLock = true;
	
	var userManDevDeviceidBeforeModification;
	var userManDevDeviceNameBeforeModification;
	var userManDevDeviceTypeBeforeModification;
	var userManDevDeviceTypeNameBeforeModification;
	
	var userManDevDataTableDevices; 
	var userManDevSelectedRow; 
	
	$("#userManDevDeviceModificationDeleteDeviceBTN").click(function(event){
		try {
			event.preventDefault();
			if(userManDevDeviceModificationDeleteDeviceLock == true) {
				userManDevDeviceModificationDeleteDeviceLock = false;
			} else {
				return;
			}
			$.ajax({
				type : "POST",
				async : true,
				url : "<c:url value='/tracking/user/mandevdeletedevice'></c:url>",
				beforeSend : function(xhr) {
					xhr.setRequestHeader($("meta[name='_csrf_header']").attr("content"), 
							$("meta[name='_csrf']").attr("content"));
				},
				data : {
					userid : $("#userManDevOwneridHidden").val(),
					deviceid : $("#userManDevDeviceModificationDeviceid").val(),
					deviceName: $("#userManDevDeviceModificationDeviceName").val(),
					deviceType : $("#userManDevDeviceModificationDeviceType").val(),
					deviceTypeName : $("#userManDevDeviceModificationDeviceTypeName").val()
				},
				success : function(result, status, error) {
					try {
						userManDevDeviceModificationDeleteDeviceLock = true;
						switch(result.responseState) {
						case 100: {
							$("#userManDevResultOrErrorDIV")
								.html("<p class='bg-primary'>Device has been deleted successfully!</p>");						
							userManDevSelectedRow.remove().draw(false);
							break;
						}
						case 200: {
							$("#userManDevResultOrErrorDIV")
							.html("<p class='bg-primary'>Device data was invalid!</p>");
							break;
						}
						case 400: {
							$("#userManDevResultOrErrorDIV")
							.html("<p class='bg-primary'>Service error!</p>");
							break;
						}
						case 600: {
							$("#userManDevResultOrErrorDIV")
							.html("<p class='bg-primary'>Device not found and not deleted with id: "
									+ $("#userManDevDeviceModificationDeviceid").val() + "</p>");
							break;
						}
						default: {
							$("#userManDevResultOrErrorDIV")
							.html("<p class='bg-primary'>Service error!</p>");
							break;
						}
						}
						$("#userManDevModifyDeviceCollapseDIV").addClass("collapse");
						
					} catch(err) {
						console.log(err);
					}
				},
				error : function(xhr, status, error) {
					try {
						userManDevDeviceModificationDeleteDeviceLock = true;
						$("#userManDevModifyDeviceCollapseDIV").addClass("collapse");
						$("#userManDevResultOrErrorDIV").html("<p class='text-danger bg-primary'>Service error!</p>");
					} catch(err) {
						console.log(err);
					}
				}
			});
			
		} catch(err) {
			userManDevDeviceModificationDeleteDeviceLock = true;
			console.log(err);
		}
	});
	
	$("#userManDevDeviceModificationCancelModificationBTN").click(function(event){
		try {
			event.preventDefault();
			$("#userManDevResultOrErrorDIV").html("");
			$("#userManDevModifyDeviceCollapseDIV").addClass("collapse");
			$(userManDevSelectedRow.node()).removeClass("selected");
		} catch(err) {
			console.log(err);
		}
	});
	
	$("#userManDevDeviceModificationConfirmModificationBTN").click(function(event){
		try {
			event.preventDefault();
			
			if(userManDevDeviceModificationConfirmModificationLock == true) {
				userManDevDeviceModificationConfirmModificationLock = false;
			} else {
				return;
			}
			
			$("#userManDevResultOrErrorDIV").html("");

			var inputs = {
				deviceid: document.getElementById("userManDevDeviceModificationDeviceid").value,
				deviceName: document.getElementById("userManDevDeviceModificationDeviceName").value,
				deviceType: document.getElementById("userManDevDeviceModificationDeviceType").value,
				deviceTypeName: document.getElementById("userManDevDeviceModificationDeviceTypeName").value
			};
			
			function restoreDeviceModification() {
				$("#userManDevDeviceModificationDeviceid").val(userManDevDeviceidBeforeModification);
				$("#userManDevDeviceModificationDeviceName").val(userManDevDeviceNameBeforeModification);
				$("#userManDevDeviceModificationDeviceType").val(userManDevDeviceTypeBeforeModification);
				$("#userManDevDeviceModificationDeviceTypeName").val(userManDevDeviceTypeNameBeforeModification);
			}
			
			var dependency = {
				validateInputs : checkDevicesInputsValidity
			};
					
			var result = dependency.validateInputs(inputs);

			if(result.valid == false) {
				userManDevDeviceModificationConfirmModificationLock = true;
				$("#userManDevResultOrErrorDIV").html(result.errors);
				restoreDeviceModification();
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
					userid : $("#userManDevOwneridHidden").val(),
					deviceid : document.getElementById("userManDevDeviceModificationDeviceid").value,
					deviceName : document.getElementById("userManDevDeviceModificationDeviceName").value,
					deviceType : document.getElementById("userManDevDeviceModificationDeviceType").value,
					deviceTypeName : document.getElementById("userManDevDeviceModificationDeviceTypeName").value
				},
				url : "<c:url value='/tracking/user/mandevupdatedevicedetails'></c:url>",
				success : function(result, status, xhr) {
					try {
						userManDevDeviceModificationConfirmModificationLock = true;
						switch(result.responseState) {
						case 100: {
							$("#userManDevResultOrErrorDIV")
								.html("<p class='bg-primary'>The device has been updated successfully!</p>");
							data = [document.getElementById("userManDevDeviceModificationDeviceid").value,
									document.getElementById("userManDevDeviceModificationDeviceName").value,
									document.getElementById("userManDevDeviceModificationDeviceType").value,
									document.getElementById("userManDevDeviceModificationDeviceTypeName").value];
							userManDevSelectedRow.data(data).draw(false);
							$(userManDevSelectedRow.node()).removeClass("selected");
							break;
						}
						case 300: {
							$("#userManDevResultOrErrorDIV")
							.html("<p class='bg-primary'>Device data was invalid!</p>");
							restoreDeviceModification();
							return;
						}
						case 400: {
							$("#userManDevResultOrErrorDIV")
							.html("<p class='bg-primary'>Service error!</p>");
							break;
						}
						case 600: {
							$("#userManDevResultOrErrorDIV")
							.html("<p class='bg-primary'>Device not found with this id: " 
									+ $("#userManDevDeviceModificationDeviceid").val() + "</p>");
							break;
						}
						default: {
							$("#userManDevResultOrErrorDIV")
							.html("<p class='bg-primary'>Service error!</p>");
							break;
						}
						}
						$("#userManDevModifyDeviceCollapseDIV").addClass("collapse");											
					} catch(err) {
						console.log(err);
					}				
				},
				error : function(xhr, status, error) {
					try {
						userManDevDeviceModificationConfirmModificationLock = true;
						$("#userManDevModifyDeviceCollapseDIV").addClass("collapse");
						$("#userManDevResultOrErrorDIV").html("<p class='text-danger bg-primary'>Service error!</p>");
					} catch(err) {
						console.log(err);
					}									
				}			
			});	
			
		} catch(err) {
			userManDevDeviceModificationConfirmModificationLock = true;
			console.log(err);
		}
	});
	
	function userManDevfillInputsWithData(rowData) {
		try {
	
			var actualDeviceid = rowData[0].trim();
			var deviceNameInputValue = rowData[1].trim();
			var deviceTypeInputValue = rowData[2].trim();			
			var deviceTypeNameInputValue =  rowData[3].trim();
					
			document.getElementById("userManDevDeviceModificationDeviceid")
				.value = actualDeviceid;
			document.getElementById("userManDevDeviceModificationDeviceName")
				.value = deviceNameInputValue;
			document.getElementById("userManDevDeviceModificationDeviceType")
				.value = deviceTypeInputValue;
			document.getElementById("userManDevDeviceModificationDeviceTypeName")
				.value = deviceTypeNameInputValue;	
			
			userManDevDeviceidBeforeModification = actualDeviceid;
			userManDevDeviceNameBeforeModification = deviceNameInputValue;
			userManDevDeviceTypeBeforeModification = deviceTypeInputValue;
			userManDevDeviceTypeNameBeforeModification = deviceTypeNameInputValue;
		
		} catch(error) {
			console.log(error);
		}
	}
	
	$(".columnOnOff").click(function(event){
		try {
			event.preventDefault();
			var item = $(this);
			if(item.hasClass("btn-success")) {
				item.removeClass("btn-success");
				item.addClass("btn-danger");
				item.attr("value", "Off");
			} else {
				item.removeClass("btn-danger");
				item.addClass("btn-success");
				item.attr("value", "On");
			}
			var column = userManDevDataTableDevices.column( $(this).attr('data-column') );
	        column.visible( ! column.visible() );
		} catch(error) {
			console.log(error);
		}
	});
	
	$(document).ready(function(){	
		try {

			userManDevDataTableDevices = $("#userManDevDataTable").DataTable({
				responsive: true,
				paging: true,
				ordering: true,
				info: true,
				/*
				 * Column sorting order
				 */
				columnDefs: [{
					targets: [0],
					orderData: [0, 1, 2, 3]
				}, {
					targets: [1],
					orderData: [1, 0, 2, 3]
				}, {
					targets: [2],
					orderData: [2, 0, 1, 3]
				}, {
					targets: [3],
					orderData: [3, 0, 1, 2]
				}]
			});
			
			$('#userManDevDataTable tfoot th').each( function (value, index) {
				if(value < 3) {
					var title = $(this).text();
		        	$(this).html( '<input type="text" placeholder="Search '+title+'" />' );
				}
		    } );
			
			userManDevDataTableDevices.columns().every( function (index) {
				
		        if(index < 3) {
					var that = this;
			 
			        $( 'input', this.footer() ).on( 'keyup change', function () {
			            if ( that.search() !== this.value ) {
			                that
			                    .search( this.value )
			                    .draw();
			            }
			        } );
		        }
		    } );
			
			$('#userManDevDataTable tbody').on( 'click', 'tr', function () {			 
		        if ( $(this).hasClass('selected') ) {
		            $(this).removeClass('selected');
		            $("#userManDevModifyDeviceCollapseDIV").addClass("collapse");
		        }
		        else {
		        	userManDevDataTableDevices.$('tr.selected').removeClass('selected');
		            $(this).addClass('selected');
		            $("#userManDevResultOrErrorDIV").html("");
		            userManDevSelectedRow = userManDevDataTableDevices.row(this);
		            userManDevfillInputsWithData(userManDevSelectedRow.data());
		            resizeInputElements(["userManDevDeviceModificationDeviceid",
		                                 "userManDevDeviceModificationDeviceName",
		                                 "userManDevDeviceModificationDeviceType",
		                                 "userManDevDeviceModificationDeviceTypeName"]);
		            
					$("#userManDevModifyDeviceCollapseDIV").removeClass("collapse");
					document.getElementById("userManDevModifyDeviceCollapseDIV").scrollIntoView();				
		        }
		    } );
		} catch(err) {
			console.log(err);
		}
	});
	
	$("#userManDevTableSettingOnOff").click(function(event){
		try {
			var panel = $("#userManDevTableSettings");	
			if(panel.hasClass("in")) {
				panel.removeClass("in");
			} else {
				panel.addClass("in");
			}
		} catch(error) {
			console.log(error);
		}
	});
</script>

<style>
	input.form-control {
  width: auto;
}
</style>

<jsp:directive.include file="userNavbar.jsp" />

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">
				<p>Device onwerid: ${deviceOwnerid} <br/> Device owner name: ${deviceOwnerName}</p>
			</div>
			<div class="panel-body">			
				<div id="userManDevModifyDeviceCollapseDIV" class="collapse">
					<div id="userManDevModifyDeviceDataDIV" class="table-responsive" >
						<table class="table">
							<thead>
								<tr>
									<th>Deviceid</th>
									<th>Device name</th>
									<th>Device type</th>
									<th>Delete type name</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>
										<label></label>
										<input type="text" value="" class="form-control"
											id="userManDevDeviceModificationDeviceid" 
											readonly="readonly"
											pattern="${deviceidPattern}">	
									</td>
									<td>
										<label></label>
										<input type="text" value="" class="form-control"
											id="userManDevDeviceModificationDeviceName"
											pattern="${deviceNamePattern}">	
									</td>
									<td>
										<label></label>
										<input type="text" value="" class="form-control"
											id="userManDevDeviceModificationDeviceType"
											pattern="${deviceTypePattern}">	
									</td>
									<td>
										<label></label>
										<input type="text" value="" class="form-control"
											id="userManDevDeviceModificationDeviceTypeName"
											pattern="${deviceTypeNamePattern}">	
									</td>
								</tr>
								<tr>
									<td>
										<input type="button" value="Confirm modification!"
											id="userManDevDeviceModificationConfirmModificationBTN">
									</td>
									<td>
										<input type="button" value="Cancel modification!"
											id="userManDevDeviceModificationCancelModificationBTN"> 
									</td>
									<td>
										<input type="button" value="Delete device!"
											id="userManDevDeviceModificationDeleteDeviceBTN"> 
									</td>
									<td>
										
									</td>
								</tr>
							</tbody>
							
						</table>
					</div>
				</div>
				<br />
				<div id="userManDevResultOrErrorDIV"></div>
				<div>
					<div class="panel-group">
					  <div class="panel panel-default">
					    <div class="panel-heading" id="userManDevTableSettingOnOff">
					      <h4 class="panel-title">
					        <a data-toggle="collapse" href="#collapse1">Table settings! (Click!)</a>
					      </h4>
					    </div>
					    <div id="userManDevTableSettings" class="panel-collapse collapse" >
					    	<div class="panel-body">
								<div class="table-responsive">
									<table class="table">
										<thead>
											<tr>
												<th>Deviceid</th>
												<th>Device name</th>
												<th>Device type</th>
												<th>Device type name</th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td>
													<input type="button" class="btn btn-success columnOnOff"
														data-column="0" value="On">
												</td>
												<td>
													<input type="button" class="btn btn-success columnOnOff"  
														data-column="1" value="On">
												</td>
												<td>
													<input type="button" class="btn btn-success columnOnOff"  
														data-column="2" value="On">
												</td>
												<td>
													<input type="button" class="btn btn-success columnOnOff"
														data-column="3" value="On">
												</td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
					    </div>
					  </div>
					</div>
				</div>
					<div class="table-responsive">
						<table class="hover" style="width: 100%" id="userManDevDataTable">
							<thead>
								<tr>
									<th>Deviceid</th>
									<th>Device name</th>
									<th>Device type</th>
									<th>Device type name</th>							
								</tr>
							</thead>
							<tbody>							
								<c:forEach var="device" items="${devices}" >
									<tr id="userManDevDeviceRow${device.deviceid}">
										<td>${device.deviceid}</td>
										
										<td id="userManDevDeviceName${device.deviceid}">${device.deviceName}</td>
											
										<td id="userManDevDeviceType${device.deviceid}">${device.deviceType}</td>
											
										<td id="userManDevDeviceTypeName${device.deviceid}">${device.deviceTypeName}</td>																
									</tr>
								</c:forEach>						
							</tbody>
							<tfoot>
								<tr>
									<th>Deviceid</th>
									<th>Device name</th>
									<th>Device type</th>
									<th>Device type name</th>
								</tr>
							</tfoot>
						</table>
						<input type="hidden" id="userManDevOwneridHidden" value="${deviceOwnerid}">
					</div> <!-- table responsive end -->				
			</div> <!-- panel body end -->
			<div class="panel-body">
				${executionError}
			</div>
		</div> <!-- panel default ending -->

	</div><!-- col-lg-12 ending -->

</div> <!-- container ending -->