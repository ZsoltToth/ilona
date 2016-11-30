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
<script src="<c:url value='/js/tracking/tableFunctions.js'></c:url>"></script>
<script type="text/javascript">

	var adminAccDevDevicesDataTable;
	var adminAccDevSelectedRow;
	
	var adminAccDevDeviceidBeforeModification;
	var adminAccDevDeviceNameBeforeModification;
	var adminAccDevDeviceTypeBeforeModification;
	var adminAccDevDeviceTypeNameBeforeModification;
	
	$("#adminUserDevAddNewDeviceBTN").click(function(event){
		event.preventDefault();
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		$.ajax({
			type : "POST",
			async : true,
			beforeSend : function(xhr) {
				xhr.setRequestHeader(header, token);
			},
			data : {
				userid : $("#adminUserDevOwneridHidden").val(),			
			},
			url : "<c:url value='/tracking/admin/userdevcreatenewdevicepagerequest'></c:url>",
			success : function(result, status, xhr) {
				$("#page-wrapper").html(result);
			},
			error : function(xhr, status, error) {
				alert("error!");
			}
		});
	});

	$("#adminUserDevDeviceModificationDeleteDeviceBTN").click(function(event){
		try {
			event.preventDefault();			
			$.ajax({
				type : "POST",
				async : true,
				url : "<c:url value='/tracking/admin/deleteuserdevice'></c:url>",
				beforeSend : function(xhr) {
					xhr.setRequestHeader($("meta[name='_csrf_header']").attr("content"), 
							$("meta[name='_csrf']").attr("content"));
				},
				data : {
					userid : $("#adminUserDevOwneridHidden").val(),
					deviceid : $("#adminUserDevDeviceModificationDeviceid").val(),
					deviceName: $("#adminUserDevDeviceModificationDeviceName").val(),
					deviceType : $("#adminUserDevDeviceModificationDeviceType").val(),
					deviceTypeName : $("#adminUserDevDeviceModificationDeviceTypeName").val()
				},
				success : function(result, status, error) {
					try {
						switch(result.responseState) {
						case 100: {
							$("#adminUserDevResultOrErrorDIV")
								.html("<p class='bg-primary'>Device has been deleted successfully!</p>");						
							adminAccDevSelectedRow.remove().draw(false);
							break;
						}
						case 200: {
							$("#adminUserDevResultOrErrorDIV")
							.html("<p class='bg-primary'>Device data was invalid!</p>");
							break;
						}
						case 400: {
							$("#adminUserDevResultOrErrorDIV")
							.html("<p class='bg-primary'>Service error!</p>");
							break;
						}
						case 600: {
							$("#adminUserDevResultOrErrorDIV")
							.html("<p class='bg-primary'>User not found and not deleted with id: "
									+ $("#adminUserDevOwneridHidden").val() + "</p>");
							break;
						}
						case 700: {
							$("#adminUserDevResultOrErrorDIV")
							.html("<p class='bg-primary'>Device not found and not deleted with id: "
									+ $("#adminUserDevDeviceModificationDeviceid").val() + "</p>");
							break;
						}
						default: {
							$("#adminUserDevResultOrErrorDIV")
							.html("<p class='bg-primary'>Service error!</p>");
							break;
						}
						}
						$("#adminUserDevModifyDeviceCollapseDIV").addClass("collapse");
						
					} catch(err) {
						console.log(err);
					}
				},
				error : function(xhr, status, error) {
					try {
						$("#adminUserDevModifyDeviceCollapseDIV").addClass("collapse");
						$("#adminUserDevResultOrErrorDIV").html("<p class='text-danger bg-primary'>Service error!</p>");
					} catch(err) {
						console.log(err);
					}
				}
			});
			
		} catch(err) {
			console.log(err);
		}
	});
	
	$("#adminUserDevDeviceModificationCancelModificationBTN").click(function(event){
		try {
			event.preventDefault();
			$("#adminUserDevResultOrErrorDIV").html("");
			$("#adminUserDevModifyDeviceCollapseDIV").addClass("collapse");
			$(adminAccDevSelectedRow.node()).removeClass("selected");
		} catch(err) {
			console.log(err);
		}
	});
	
	$("#adminUserDevDeviceModificationConfirmModificationBTN").click(function(event){
		try {
			event.preventDefault();
			
			$("#adminUserDevResultOrErrorDIV").html("");

			var inputs = {
				deviceid: document.getElementById("adminUserDevDeviceModificationDeviceid").value,
				deviceName: document.getElementById("adminUserDevDeviceModificationDeviceName").value,
				deviceType: document.getElementById("adminUserDevDeviceModificationDeviceType").value,
				deviceTypeName: document.getElementById("adminUserDevDeviceModificationDeviceTypeName").value
			};
			
			function restoreDeviceModification() {
				$("#adminUserDevDeviceModificationDeviceid").val(adminAccDevDeviceidBeforeModification);
				$("#adminUserDevDeviceModificationDeviceName").val(adminAccDevDeviceNameBeforeModification);
				$("#adminUserDevDeviceModificationDeviceType").val(adminAccDevDeviceTypeBeforeModification);
				$("#adminUserDevDeviceModificationDeviceTypeName").val(adminAccDevDeviceTypeNameBeforeModification);
			}
			
			var dependency = {
				validateInputs : checkDevicesInputsValidity
			};
					
			var result = dependency.validateInputs(inputs);

			if(result.valid == false) {
				$("#adminUserDevResultOrErrorDIV").html(result.errors);
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
					userid : $("#adminUserDevOwneridHidden").val(),
					deviceid : document.getElementById("adminUserDevDeviceModificationDeviceid").value,
					deviceName : document.getElementById("adminUserDevDeviceModificationDeviceName").value,
					deviceType : document.getElementById("adminUserDevDeviceModificationDeviceType").value,
					deviceTypeName : document.getElementById("adminUserDevDeviceModificationDeviceTypeName").value
				},
				url : "<c:url value='/tracking/admin/updatedevicedetails'></c:url>",
				success : function(result, status, xhr) {
					try {
						switch(result.responseState) {
						case 100: {
							$("#adminUserDevResultOrErrorDIV")
								.html("<p class='bg-primary'>The device has been updated successfully!</p>");
							data = [document.getElementById("adminUserDevDeviceModificationDeviceid").value,
									document.getElementById("adminUserDevDeviceModificationDeviceName").value,
									document.getElementById("adminUserDevDeviceModificationDeviceType").value,
									document.getElementById("adminUserDevDeviceModificationDeviceTypeName").value];
							adminAccDevSelectedRow.data(data).draw(false);
							$(adminAccDevSelectedRow.node()).removeClass("selected");
							break;
						}
						case 300: {
							$("#adminUserDevResultOrErrorDIV")
							.html("<p class='bg-primary'>Device data was invalid!</p>");
							restoreDeviceModification();
							return;
						}
						case 400: {
							$("#adminUserDevResultOrErrorDIV")
							.html("<p class='bg-primary'>Service error!</p>");
							break;
						}
						case 600: {
							$("#adminUserDevResultOrErrorDIV")
							.html("<p class='bg-primary'>Device not found with this id: " 
									+ $("#adminUserDevDeviceModificationDeviceid").val() + "</p>");
							break;
						}
						default: {
							$("#adminUserDevResultOrErrorDIV")
							.html("<p class='bg-primary'>Service error!</p>");
							break;
						}
						}
						$("#adminUserDevModifyDeviceCollapseDIV").addClass("collapse");											
					} catch(err) {
						console.log(err);
					}				
				},
				error : function(xhr, status, error) {
					try {
						$("#adminUserDevModifyDeviceCollapseDIV").addClass("collapse");
						$("#adminUserDevResultOrErrorDIV").html("<p class='text-danger bg-primary'>Service error!</p>");
					} catch(err) {
						console.log(err);
					}									
				}			
			});				
		} catch(err) {
			console.log(err);
		}
	});
	
	function userManDevfillInputsWithData(rowData) {
		try {
	
			var actualDeviceid = rowData[0].trim();
			var deviceNameInputValue = rowData[1].trim();
			var deviceTypeInputValue = rowData[2].trim();			
			var deviceTypeNameInputValue =  rowData[3].trim();
					
			document.getElementById("adminUserDevDeviceModificationDeviceid")
				.value = actualDeviceid;
			document.getElementById("adminUserDevDeviceModificationDeviceName")
				.value = deviceNameInputValue;
			document.getElementById("adminUserDevDeviceModificationDeviceType")
				.value = deviceTypeInputValue;
			document.getElementById("adminUserDevDeviceModificationDeviceTypeName")
				.value = deviceTypeNameInputValue;	

			adminAccDevDeviceidBeforeModification = actualDeviceid;
			adminAccDevDeviceNameBeforeModification = deviceNameInputValue;
			adminAccDevDeviceTypeBeforeModification = deviceTypeInputValue;
			adminAccDevDeviceTypeNameBeforeModification = deviceTypeNameInputValue;
		
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
			var column = adminAccDevDevicesDataTable.column( $(this).attr('data-column') );
	        column.visible( ! column.visible() );
		} catch(error) {
			console.log(error);
		}
	});
	
	$(document).ready(function(){
		try {		
			adminAccDevDevicesDataTable = $("#adminUserDevDevicesTable").DataTable({
				responsive: true,
				paging: true,
				ordering: true,
				info: true,
				
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
			
			$('#adminUserDevDevicesTable tfoot th').each( function (value, index) {
				if(value < 3) {
					var title = $(this).text();
		        	$(this).html( '<input type="text" placeholder="Search '+title+'" />' );
				}
		    } );
			
			adminAccDevDevicesDataTable.columns().every( function (index) {
				
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
			
			$('#adminUserDevDevicesTable tbody').on( 'click', 'tr', function () {			 
		        if ( $(this).hasClass('selected') ) {
		            $(this).removeClass('selected');
		            $("#adminUserDevModifyDeviceCollapseDIV").addClass("collapse");
		        }
		        else {
		        	adminAccDevDevicesDataTable.$('tr.selected').removeClass('selected');
		            $(this).addClass('selected');
		            $("#adminUserDevResultOrErrorDIV").html("");
		            $("#adminUserDevModifyDeviceCollapseDIV").removeClass("collapse");
		            adminAccDevSelectedRow = adminAccDevDevicesDataTable.row(this);
		            userManDevfillInputsWithData(adminAccDevSelectedRow.data());
		            resizeInputElements(["adminUserDevDeviceModificationDeviceid",
		                                 "adminUserDevDeviceModificationDeviceName",
		                                 "adminUserDevDeviceModificationDeviceType",
		                                 "adminUserDevDeviceModificationDeviceTypeName"]);				
		        }
		    });
			
		} catch(error) {
			console.log(error);
		}
	});
	
	$("#adminUserDevTableSettingOnOff").click(function(event){
		try {
			var panel = $("#adminUserDevTableSettings");	
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

<jsp:directive.include file="adminNavbar.jsp" />

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">
				<p>Device onwerid: ${deviceOwner} <br/> Device owner name: ${deviceOwnerName}</p>
				<p><input id="adminUserDevAddNewDeviceBTN" class="btn btn-default" value="Add new device"> </p>
			</div>
			<div class="panel-body">			
				<div id="adminUserDevModifyDeviceCollapseDIV" class="collapse">
					<div id="adminUserDevModifyDeviceDataDIV" class="table-responsive" >
						<table class="display">
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
											id="adminUserDevDeviceModificationDeviceid" 
											readonly="readonly"
											pattern="${deviceidPattern}">	
									</td>
									<td>
										<label></label>
										<input type="text" value="" class="form-control"
											id="adminUserDevDeviceModificationDeviceName"
											pattern="${deviceNamePattern}">	
									</td>
									<td>
										<label></label>
										<input type="text" value="" class="form-control"
											id="adminUserDevDeviceModificationDeviceType"
											pattern="${deviceTypePattern}">	
									</td>
									<td>
										<label></label>
										<input type="text" value="" class="form-control"
											id="adminUserDevDeviceModificationDeviceTypeName"
											pattern="${deviceTypeNamePattern}">	
									</td>
								</tr>
								<tr>
									<td>
										<input type="button" value="Confirm modification!"
											id="adminUserDevDeviceModificationConfirmModificationBTN">
									</td>
									<td>
										<input type="button" value="Cancel modification!"
											id="adminUserDevDeviceModificationCancelModificationBTN"> 
									</td>
									<td>
										<input type="button" value="Delete device!"
											id="adminUserDevDeviceModificationDeleteDeviceBTN"> 
									</td>
									<td>
										
									</td>
								</tr>
							</tbody>
							
						</table>
					</div>
				</div>
				<br />
				<div id="adminUserDevResultOrErrorDIV"></div>
				<div>
					<div class="panel-group">
					  <div class="panel panel-default">
					    <div class="panel-heading" id="adminUserDevTableSettingOnOff">
					      <h4 class="panel-title">
					        <a data-toggle="collapse" href="#collapse1">Table settings! (Click!)</a>
					      </h4>
					    </div>
					    <div id="adminUserDevTableSettings" class="panel-collapse collapse" >
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
						<table class="hover" style="width: 100%" id="adminUserDevDevicesTable">
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
									<tr id="adminUserDevDeviceRow${device.deviceid}">
										<td>${device.deviceid}</td>
										<td>${device.deviceName}</td>										
										<td>${device.deviceType}</td>											
										<td>${device.deviceTypeName}</td>																
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
						<input type="hidden" id="adminUserDevOwneridHidden" value="${deviceOwner}">
					</div> <!-- table responsive end -->				
			</div> <!-- panel body end -->
			<div class="panel-body">
				${executionError}
			</div>
		</div> <!-- panel default ending -->

	</div><!-- col-lg-12 ending -->

</div> <!-- container ending -->