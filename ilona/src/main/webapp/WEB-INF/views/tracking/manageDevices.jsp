<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page session="true"%>
<%@ page isELIgnored="false"%>

<script>

	var manageDevicesDeviceidBeforeModification;
	var manageDevicesDeviceNameBeforeModification;
	var manageDevicesDeviceTypeBeforeModification;
	var manageDevicesDeviceTypeNameBeforeModification;

	function userManDevfillInputsWithData(rowData) {
		try {
	
			var actualDeviceid = rowData[0].trim();
			var deviceNameInputValue = rowData[1].trim();
			var deviceTypeInputValue = rowData[2].trim();			
			var deviceTypeNameInputValue =  rowData[3].trim();
					
			document.getElementById("manageDevicesDeviceModificationDeviceid")
				.value = actualDeviceid;
			document.getElementById("manageDevicesDeviceModificationDeviceName")
				.value = deviceNameInputValue;
			document.getElementById("manageDevicesDeviceModificationDeviceType")
				.value = deviceTypeInputValue;
			document.getElementById("manageDevicesDeviceModificationDeviceTypeName")
				.value = deviceTypeNameInputValue;	
			
			manageDevicesDeviceidBeforeModification = actualDeviceid;
			manageDevicesDeviceNameBeforeModification = deviceNameInputValue;
			manageDevicesDeviceTypeBeforeModification = deviceTypeInputValue;
			manageDevicesDeviceTypeNameBeforeModification = deviceTypeNameInputValue;
			$("#manageDevicesModifyDeviceCollapseDIV").removeClass("collapse");
			document.getElementById("manageDevicesModifyDeviceCollapseDIV").scrollIntoView();
		} catch(error) {
			console.log(error);
		}
	}

</script>

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">
				<p>Device onwerid: ${deviceOwnerid} <br/> Device owner name: ${deviceOwnerName}</p>
			</div>
			<div class="panel-body">			
				<div id="manageDevicesModifyDeviceCollapseDIV" class="collapse">
					<div id="manageDevicesModifyDeviceDataDIV" class="table-responsive" >
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
										<input type="text" value="" class="form-control"
											id="manageDevicesDeviceModificationDeviceid" 
											readonly="readonly"
											pattern="${deviceidPattern}">	
									</td>
									<td>
										<input type="text" value="" class="form-control"
											id="manageDevicesDeviceModificationDeviceName"
											pattern="${deviceNamePattern}">	
									</td>
									<td>									
										<input type="text" value="" class="form-control"
											id="manageDevicesDeviceModificationDeviceType"
											pattern="${deviceTypePattern}">	
									</td>
									<td>
										<input type="text" value="" class="form-control"
											id="manageDevicesDeviceModificationDeviceTypeName"
											pattern="${deviceTypeNamePattern}">	
									</td>
								</tr>
								<tr>
									<td>
										<input type="button" value="Confirm modification!"
											id="manageDevicesDeviceModificationConfirmModificationBTN">
									</td>
									<td>
										<input type="button" value="Cancel modification!"
											id="manageDevicesDeviceModificationCancelModificationBTN"> 
									</td>
									<td>
										<input type="button" value="Delete device!"
											id="manageDevicesDeviceModificationDeleteDeviceBTN"> 
									</td>
									<td></td>
								</tr>
							</tbody>
							
						</table>
					</div>
				</div>
				<br />
				<div id="manageDevicesDevResultOrErrorDIV"></div>
				<div>
					<div class="panel-group">
					  <div class="panel panel-default">
					    <div class="panel-heading" id="manageDevicesTableSettingOnOff">
					      <h4 class="panel-title">
					        <a data-toggle="collapse" href="#collapse1">Table settings! (Click!)</a>
					      </h4>
					    </div>
					    <div id="manageDevicesTableSettingsDIV" class="panel-collapse collapse" >
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
						<table class="hover" style="width: 100%" id="manageDeviceDevicesTable">
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
									<tr>
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
						<input type="hidden" id="manageDevicesDevOwneridHidden" value="${deviceOwnerid}">
					</div> <!-- table responsive end -->
				
			</div> <!-- panel body end -->
			<div class="panel-body">
				${executionError}
			</div>
		</div> <!-- panel default ending -->
	</div><!-- col-lg-12 ending -->
</div> <!-- container ending -->