<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page session="true"%>
<%@ page isELIgnored="false"%>


<!-- CSRF Protection token -->
<meta name="_csrf" content="${_csrf.token}"/>
	<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}"/>

<script src="<c:url value='/js/d3.min.js'></c:url>"></script>
<script src="<c:url value='/js/Donut3D.js'></c:url>"></script>

<script src="<c:url value='/js/tracking/track.js'></c:url>"></script>
<script src="<c:url value='/js/tracking/groundFloorData.js'></c:url>"></script>
<script src="<c:url value='/js/tracking/firstFloorData.js'></c:url>"></script>
<script src="<c:url value='/js/tracking/secondFloorData.js'></c:url>"></script>

<script type="text/javascript">

	var groundFloorImageSource = "<c:url value='/img/groundFloor.jpg'></c:url>";
	var firstFloorImageSource = "<c:url value='/img/firstFloor.jpg'></c:url>";
	var secondFloorImageSource = "<c:url value='/img/secondFloor.jpg'></c:url>";
	var markerImageSource = "<c:url value='/img/marker.png'></c:url>";
	var markerSelectedSource = "<c:url value='/img/markerSelected.png'></c:url>";
	var floorMapSVG;
	
	var graphPontCircleRadius = 3;
	var userTrackingPositionsTable;
	
	var groundFloorMap;
	var firstFloorMap;
	var secondFloorMap;
	
	var positions;
	var positionsImages;
	var pathData = {};
	var selectedPositionImage;
	
	var userTrackingSelectUserLock = true;
	var userTrackingSelectDeviceLock = true;
	var userTrackingGetPathLock = true;
	
	$(document).ready(function(){
		try {
			userTrackingPositionsTable = $("#userTrackingPositionsTable").DataTable({
				responsive: true,
				paging: true,
				ordering: true,
				info: true
			});
			document.getElementById("userTrackingFromDateTime").value = generateDateTimePickerValue();
			document.getElementById("userTrackingToDateTime").value = generateDateTimePickerValue();
			
		} catch(error) {
			console.log(error);
		}
		
	});

	$("#userTrackingUserAndDeviceChooserHeader").click(function(event){
		try {
			var panel =  $("#userTrackingUserAndDeviceBody");
			if(panel.hasClass("in")) {
				panel.removeClass("in");
			} else {
				panel.addClass("in");
			}
		} catch(error) {
			console.log(error);
		}
	});
	
	$("#userTrackingActualDeviceDataHeader").click(function(event){
		try {
			var panel =  $("#userTrackingActualDeviceDataBody");
			if(panel.hasClass("in")) {
				panel.removeClass("in");
			} else {
				panel.addClass("in");
			}
		} catch(error) {
			console.log(error);
		}
	});
	
	function resetFloors() {
		try {
			$("#userTrackingGroundFloorDIV").html("");
			groundFloorMap = d3.select("#userTrackingGroundFloorDIV").append("svg").attr("width", "100%")
				.attr("height", "100%").attr("viewBox", "0 0 1196 705");
			groundFloorMap.append("image").attr("xlink:href", groundFloorImageSource)
				.attr("x", 0).attr("y", 0).attr("width", 1196).attr("height", 705);
			
			$("#userTrackingFirstFloorDIV").html("");
			firstFloorMap = d3.select("#userTrackingFirstFloorDIV").append("svg").attr("width", "100%")
				.attr("height", "100%").attr("viewBox", "0 0 1196 705");
			firstFloorMap.append("image").attr("xlink:href", firstFloorImageSource)
				.attr("x", 0).attr("y", 0).attr("width", 1196).attr("height", 705);
			
			$("#userTrackingSecondFloorDIV").html("");
			secondFloorMap = d3.select("#userTrackingSecondFloorDIV").append("svg").attr("width", "100%")
				.attr("height", "100%").attr("viewBox", "0 0 1196 705");
			secondFloorMap.append("image").attr("xlink:href", secondFloorImageSource)
				.attr("x", 0).attr("y", 0).attr("width", 1196).attr("height", 705);
			
		} catch(error) {
			console.log(error);
		}
	}
	
	$("#userTrackingActualPositionPathOnOffBTN").click(function(event){
		try {
			if(userTrackingGetPathLock == true) {
				userTrackingGetPathLock = false;
			} else {
				return;
			}
			var posId = $(this).attr("data-id");
			var posPathData = pathData[posId];
			console.log(posPathData);
			var positionStart;
			var positionEnd;
			
			var length = positions.length;
			var iteration;
			var i = 0;
			for (i; i < length; i++) {
				if(positions[i].position.uuid == posId) {
					positionStart = positions[i];
					if (i + 1 < length) {
						positionEnd = positions[i + 1];
					}
					iteration = i;
					break;				
				}
			}

			if( !(iteration + 1 < length)) {
				console.log("");
				return;
			}
			
			if(typeof posPathData == "undefined") {
				var url = "<c:url value='/tracking/user/tracking/calculatepath'></c:url>";
				
				var startFloor = calculateFloor(positionStart);
				var endFloor = calculateFloor(positionEnd);
				
				if(startFloor == endFloor) {
					var startNode = calculateNearestNode(positionStart, startFloor);
					var endNode = calculateNearestNode(positionEnd, endFloor);
					generatePathData(startNode.id, endNode.id, url, positionStart, positionEnd, 0);
				} else {
					var startNode = calculateNearestNode(positionStart, startFloor);
					var floorConnectionNode = getFloorConnectionNodeByValue(startFloor);
					generatePathData(startNode.id, floorConnectionNode.id, url, positionStart, positionEnd, 1);
					
					var endNode = calculateNearestNode(positionEnd, endFloor);
					floorConnectionNode = getFloorConnectionNodeByValue(endFloor);
					generatePathData(floorConnectionNode.id, endNode.id, url, positionStart, positionEnd, 2);
				}
				return;
			}
			
			if (posPathData.visible == true) {
				var i = 0;
				var length = posPathData.curveData.length;
				var curves = posPathData.curveData;
				for (i; i < length; i++) {
					curves[i].path.remove();
					curves[i].path = null;
				}
				posPathData.visible = false;
				userTrackingGetPathLock = true;
				return;
			}
			
			if(posPathData.visible == false) {
				posPathData.visible = true;
				var i = 0;
				var length = posPathData.curveData.length;
				var curves = posPathData.curveData;
				for (i; i < length; i++) {
					curves[i].path = drawPath(curves[i].floorMap, curves[i].points);
				}				
			}
			userTrackingGetPathLock = true;
			
		} catch(error) {
			userTrackingGetPathLock = true;
			console.log(error);
		}
	});
	
	function generatePathData(startNode, endNode, url, startPosition, endPosition, mode) {
		try {
			var floorNumber;
			switch(mode) {
			case 0: floorNumber = calculateFloor(startPosition); break;
			case 1: floorNumber = calculateFloor(startPosition); break;
			case 2: floorNumber = calculateFloor(endPosition); break;
			default: throw "invalid mode!";
			}
			$.ajax({
				type : "POST",
				async : true,
				timeout : 10000,
				url : url,
				beforeSend : function(xhr) {
					xhr.setRequestHeader($("meta[name='_csrf_header']")
							.attr("content"), $("meta[name='_csrf']").attr(
							"content"));
				},
				data : {
					start : startNode,
					end : endNode,
					floor : floorNumber 
				},
				success : function(result, status, xhr) {
					try {
						userTrackingGetPathLock = true;
						var points;
						var floorMap;
						var path;
						
						if(mode == 2) {
							
							points = generatePathInputDataEndPosOnly(endPosition, result);
							switch(floorNumber) {
							case 0: floorMap = groundFloorMap; break;
							case 1: floorMap = firstFloorMap; break;
							default: floorMap = secondFloorMap; break;
							}
							path = drawPath(floorMap, points);
							var curveData = {
									path : path,
									points : points,
									floorMap : floorMap
							}
							var id = startPosition.position.uuid;
							var curve = pathData[id];
							if(typeof curve == "undefined") {
								pathData[id] = {
										visible : true,
										curveData : [curveData]
								}
							} else {
								curve.curveData.push(curveData);
							}
							return;
						}
						
						if(mode == 1) {
							points = generatePathInputDataStartPosOnly(startPosition, result);
							switch(floorNumber) {
							case 0: floorMap = groundFloorMap; break;
							case 1: floorMap = firstFloorMap; break;
							default: floorMap = secondFloorMap; break;
							}
							path = drawPath(floorMap, points);
							var curveData = {
									path : path,
									points : points,
									floorMap : floorMap
							}
							var id = startPosition.position.uuid;
							var curve = pathData[id];
							if(typeof curve == "undefined") {
								pathData[id] = {
										visible : true,
										curveData : [curveData]
								}
							} else {
								curve.curveData.push(curveData);
							}
							return;
						}
						
						// both not null
						points = generatePathInputData(startPosition, endPosition, result);
						switch(floorNumber) {
						case 0: floorMap = groundFloorMap; break;
						case 1: floorMap = firstFloorMap; break;
						default: floorMap = secondFloorMap; break;
						}
						path = drawPath(floorMap, points);
						var curveData = {
								path : path,
								points : points,
								floorMap : floorMap
						}
						var id = startPosition.position.uuid;
						var curve = pathData[id];
						if(typeof curve == "undefined") {
							pathData[id] = {
									visible : true,
									curveData : [curveData]
							}
						} else {
							curve.curveData.push(curveData);
						}
						return;
											
					} catch (error) {
						userTrackingGetPathLock = true;
						console.log(error);
					}
				},
				error : function(xhr, status, error) {
					userTrackingGetPathLock = true;
					console.log(error);
				}
			});
		} catch (error) {
			userTrackingGetPathLock = true;
			throw "Function :: generatePathData Error: " + error;
		}
	}
	
	$("#userTrackingActualPositionPreviousPosBTN").click(function(event){
		try {
			var length = positions.length;
			var i = 0;
			var actualId = $(this).attr("data-id");
			for (i; i < length; i++) {
				if(positions[i].position.uuid == actualId) {
					if(i == 0) {
						// LEFT STOP!
					} else {
						var prevActualId = positions[i - 1];
						$("#userTrackingActualPositionId").val(prevActualId.position.uuid);
						$("#userTrackingActualPositionZoneName")
							.val(prevActualId.position.zone.id);
						$("#userTrackingActualPositionTimestamp")	
							.val(new Date(prevActualId.date));
						$("#userTrackingActualPositionPathOnOffBTN")
							.attr("data-id", prevActualId.position.uuid);
						$("#userTrackingActualPositionPreviousPosBTN").attr("data-id", prevActualId.position.uuid);
						$("#userTrackingActualPositionNextPosBTN").attr("data-id", prevActualId.position.uuid);
						var j = 0;
						var lengthImages = positionsImages.length;
						var actualPosImage;
						var prevPosImage;
						for (j; j < lengthImages; j++) {
							if(positionsImages[j].attr("data-id") == positions[i - 1].position.uuid) {
								prevPosImage = positionsImages[j];
							}
							if(positionsImages[j].attr("data-id") == positions[i].position.uuid) {
								actualPosImage = positionsImages[j];
							}
						}
						selectedPositionImage.attr("xlink:href", markerImageSource);
						selectedPositionImage = prevPosImage;
						actualPosImage.attr("xlink:href", markerImageSource);
						prevPosImage.attr("xlink:href", markerSelectedSource);
					}				
				}
			}
		} catch(error) {
			console.log(error);
		}
	});
	
	$("#userTrackingActualPositionNextPosBTN").click(function(event){
		try {
			var length = positions.length;
			var i = 0;
			var actualId = $(this).attr("data-id");
			for (i; i < length; i++) {
				if(positions[i].position.uuid == actualId) {
					if(i == length - 1) {
						// LEFT STOP!
					} else {
						var prevActualId = positions[i + 1];
						$("#userTrackingActualPositionId").val(prevActualId.position.uuid);
						$("#userTrackingActualPositionZoneName")
							.val(prevActualId.position.zone.id);
						$("#userTrackingActualPositionTimestamp")	
							.val(new Date(prevActualId.date));
						$("#userTrackingActualPositionPathOnOffBTN")
							.attr("data-id", prevActualId.position.uuid);
						$("#userTrackingActualPositionPreviousPosBTN").attr("data-id", prevActualId.position.uuid);
						$("#userTrackingActualPositionNextPosBTN").attr("data-id", prevActualId.position.uuid);
						var j = 0;
						var lengthImages = positionsImages.length;
						var actualPosImage;
						var nextPosImage;
						for (j; j < lengthImages; j++) {
							if(positionsImages[j].attr("data-id") == positions[i + 1].position.uuid) {
								nextPosImage = positionsImages[j];
							}
							if(positionsImages[j].attr("data-id") == positions[i].position.uuid) {
								actualPosImage = positionsImages[j];
							}
						}
						selectedPositionImage.attr("xlink:href", markerImageSource);
						selectedPositionImage = nextPosImage;
						actualPosImage.attr("xlink:href", markerImageSource);
						nextPosImage.attr("xlink:href", markerSelectedSource);
					}
					
				}
			}
		} catch(error) {
			console.log(error);
		}
	});
	
	$("#userTrackingSelectDeviceBTN").click(function(event){
		try {
			event.preventDefault();
			
			if(userTrackingSelectDeviceLock == true) {
				userTrackingSelectDeviceLock = false;
			} else {
				return;
			}
			$("#userTrackingSelectionErrorDIV").html("");
			var fromValue = document.getElementById("userTrackingFromDateTime").value;
			var toValue = document.getElementById("userTrackingToDateTime").value;
			if (fromValue.length == 0 || toValue.length == 0) {
				$("#userTrackingSelectionErrorDIV").html(getTrackErrorMessage("Invalid date!"));
				return;
			}
			var devicesSelect = document.getElementById("userTrackingDevicesSelect");
			if(devicesSelect.length == 0) {
				$("#userTrackingSelectionErrorDIV").html(getTrackErrorMessage("Select a device!"));
				return;
			}
			var selectedDeviceId = devicesSelect.value;
			var selected
			var dateFrom = new Date(fromValue);
			var dateTo = new Date(toValue);
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			 
			$.ajax({
				type : "POST",
				async : true,
				url : "<c:url value='/tracking/user/tracking/getpositions'></c:url>",
				beforeSend : function(xhr) {
					xhr.setRequestHeader(header, token);
				},
				data : {
					deviceid : selectedDeviceId,
					from : dateFrom.getTime(),
					to : dateTo.getTime()
				},
				success : function(result, status, xhr) {
					try {
						userTrackingSelectDeviceLock = true;
						if(result.length == 0) {
							//document.getElementById("userTrackingSelectDeviceBTN").disabled = true;
							return;
						}
						positions = result;
						sortPositionsByTime(positions);
						resetFloors();
						userTrackingPositionsTable.clear().draw();
						fillDataTableWithValues(userTrackingPositionsTable, positions);
						positionsImages = drawPositions(positions, [groundFloorMap, firstFloorMap, secondFloorMap], markerImageSource,
								function() {
							try {
								var length = positions.length;
								var i = 0;
								var actualId = d3.select(this).attr("data-id");
								for (i; i < length; i++) {
									if(positions[i].position.uuid == actualId) {
										$("#userTrackingActualPositionId").val(actualId);
										$("#userTrackingActualPositionZoneName")
											.val(positions[i].position.zone.id);
										$("#userTrackingActualPositionTimestamp")	
											.val(new Date(positions[i].date));
										$("#userTrackingActualPositionPathOnOffBTN")
											.attr("data-id", actualId);
										$("#userTrackingActualPositionPreviousPosBTN").attr("data-id", actualId);
										$("#userTrackingActualPositionNextPosBTN").attr("data-id", actualId);
										if(typeof selectedPositionImage == "undefined") {
											selectedPositionImage = d3.select(this).attr("xlink:href", markerSelectedSource);
										} else {
											selectedPositionImage.attr("xlink:href", markerImageSource);
											selectedPositionImage = d3.select(this).attr("xlink:href", markerSelectedSource);
										}
									}
								}
							} catch(error) {
								$("#userTrackingSelectionErrorDIV").html("Service error!");
								console.log(error);
							}
							
						});
						
					} catch(error) {
						userTrackingSelectDeviceLock = true;
						$("#userTrackingSelectionErrorDIV").html("Service error!");
						console.log(error);
					}
				},
				error : function(xhr, status, error) {
					userTrackingSelectDeviceLock = true;
					$("#userTrackingSelectionErrorDIV").html("Service error!");
					console.log(error)
				}
			});
		} catch(error) {
			userTrackingSelectDeviceLock = true;
			console.log(error);
		}
	});
</script>

<jsp:directive.include file="userNavbar.jsp" />

<div class="container-fluid">
	<div class="row">
		<div class="col-lg-12">
			<div id="userTrackingSelectionErrorDIV"></div>
			<div class="panel-group">
			  <div class="panel panel-default">
			    <div class="panel-heading" id="userTrackingUserAndDeviceChooserHeader">
			      <h4 class="panel-title" style="text-align: center">
			        <span class="fa fa-arrow-circle-down"></span>
			        Devices
			        <span class="fa fa-arrow-circle-down"></span>
			      </h4>
			    </div>
			    <div id="userTrackingUserAndDeviceBody" class="panel-collapse collapse">
			      <div class="panel-body">
						<div class="panel panel-default">
							<div class="panel-heading" id="userTrackingDevicesPanelHeader">
								Devices:
							</div>
							<div class="panel-body">		   			
				      			<select  class="form-control" id="userTrackingDevicesSelect">
				      				<c:forEach items="${devices}" var="device">
				      					<option value="${device.deviceid}" title="${device.deviceid}" label="${device.deviceid}">
				      				</c:forEach>
				      			</select>
				      			<br />
				      			<label for="userTrackingFromDateTime">From</label>
				      			<input type="datetime-local" id="userTrackingFromDateTime">
				      			<br />
				      			<label for="userTrackingToDateTime">To</label>
				      			<input type="datetime-local" id="userTrackingToDateTime">  
				      			<br />
				      			<input type="button" value="Select device!" id="userTrackingSelectDeviceBTN">
				      			<input type="hidden" id="userTrackingDeviceOwnerIdHIDDEN">   			
							</div>
						</div>
			      </div>
			    </div>
			  </div>
			</div>
			
			<div class="panel panel-default">
			  	<div class="panel-heading" id="userTrackingActualDeviceDataHeader" style="text-align: center">
			  		<h4 class="panel-title">
				  		<span class="fa fa-arrow-circle-down"></span>
				        Actual position data
				        <span class="fa fa-arrow-circle-down"></span>
			  		</h4>
			  	</div>
			  	<div id="userTrackingActualDeviceDataBody" class="panel-collapse collapse">
				  	<div class="panel-body">
				  		<label for="userTrackingActualPositionId">Position id:</label>
				  		<input class="form-control" type="text" id="userTrackingActualPositionId" readonly="readonly">
				  		<br />
				  		<label for="userTrackingActualPositionZoneName">Zone name:</label>
				  		<input class="form-control" type="text" id="userTrackingActualPositionZoneName" readonly="readonly">
				  		<br />
				  		<label for="userTrackingActualPositionTimestamp">Time:</label>
				  		<input class="form-control" type="text" id="userTrackingActualPositionTimestamp" readonly="readonly">
				  		<br />
				  		<label>Path to the next position!</label>
				  		<input class="btn btn-default" type="button" id="userTrackingActualPositionPathOnOffBTN"
				  			value="Path On / Off">
				  		<br />
				  		<label>Previous position!</label>
				  		<input class="btn btn-default" type="button" id="userTrackingActualPositionPreviousPosBTN" value="Previous position">
				  		<br />
				  		<label>Next position!</label>
				  		<input class="btn btn-default" type="button" id="userTrackingActualPositionNextPosBTN" value="Next position">
				  	</div>
			  	</div>	
			</div>		
		</div>				
	</div>
	
	<div class="row">
		<div class="col-lg-12">
			<div>
				<ul class="nav nav-pills">
				  <li class="active"><a data-toggle="pill" href="#home">Positions table</a></li>
				  <li><a data-toggle="pill" href="#menu1">Ground floor</a></li>
				  <li><a data-toggle="pill" href="#menu2">First floor</a></li>
				  <li><a data-toggle="pill" href="#menu3">Second floor</a></li>
				</ul>
			</div>
			<br />
			<div class="tab-content">
			  <div id="home" class="tab-pane fade in active">
			  	<div class="table-responsive">
					<table class="display nowrap" id="userTrackingPositionsTable" width="100%">
					    <thead>
					    	<tr>
					    		<th>Position id</th>
					    		<th>Zone id</th>
					    		<th>Zone name</th>
					    		<th>Coordinate-X</th>
					    		<th>Coordinate-Y</th>
					    		<th>Coordinate-Z</th>
					    		<th>Timestamp</th>
					    	</tr>
					    </thead>
					    <tbody>
					    </tbody>
					</table>
				</div>
			  </div>
			  <div id="menu1" class="tab-pane fade">
			    <div id="userTrackingGroundFloorDIV"></div>
			  </div>
			  <div id="menu2" class="tab-pane fade">
			    <div id="userTrackingFirstFloorDIV"></div>
			  </div>
			  <div id="menu3" class="tab-pane fade">
			    <div id="userTrackingSecondFloorDIV"></div>
			  </div>
			</div>
		</div>
	</div>
	
</div>