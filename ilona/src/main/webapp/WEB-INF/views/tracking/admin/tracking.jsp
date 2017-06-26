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
	
	var floorMapSVG;
	
	var graphPontCircleRadius = 3;
	var adminTrackingPositionsTable;
	
	var groundFloorMap;
	var firstFloorMap;
	var secondFloorMap;
	
	var positions;
	
	$(document).ready(function(){
		adminTrackingPositionsTable = $("#adminTrackingPositionsTable").DataTable({
			responsive: true,
			paging: true,
			ordering: true,
			info: true
		});
	});
	
		$("#draw1").click(function(){
			var startLocation = [108,85];
			var endLocation=[580,93];

			var startPoint;
			var endPoint;
			// calculated start point
			startPoint = points[0];
			var distance = Math.sqrt(Math.pow(points[0].pointx - startLocation[0],2)+
					Math.pow(points[0].pointy - startLocation[1],2));
			for(var i = 1; i < points.length; i++) {
				var distancePoints = Math.sqrt(Math.pow(points[i].pointx - startLocation[0],2)+
						Math.pow(points[i].pointy - startLocation[1],2));
				if(distance > distancePoints) {
					distance = distancePoints;
					startPoint = points[i];
				}
			}
			endPoint = points[0];
			distance = Math.sqrt(Math.pow(points[0].pointx - endLocation[0],2)+
					Math.pow(points[0].pointy - endLocation[1],2));
			
			for(var i = 1; i < points.length; i++) {
				var distancePoints = Math.sqrt(Math.pow(points[i].pointx - endLocation[0],2)+
						Math.pow(points[i].pointy - endLocation[1],2));
				if(distance > distancePoints) {
					distance = distancePoints;
					endPoint = points[i];
				}
			}
			//alert(startPoint.pointid);
			//alert(endPoint.pointid);
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			var actualPoint;
			$.ajax({
				type : "POST",
				async : false,
				url : "<c:url value='/tracking/admin/tracking/calculatedpath' ></c:url>",
				beforeSend : function(xhr) {
					xhr.setRequestHeader(header, token);
				},
				data : {
					start : startPoint.pointid,
					end : endPoint.pointid
				},
				success : function(result, status, xhr) {
					actualPoint = result;
				},
				error : function(xhr, status, error) {
					alert("Error" + status + error);
				}
				
			});
			
			var drawPoints = [];
			for(var i = 0; i < actualPoint.length; i++) {
				for(var j = 0; j < points.length; j++) {
					if(points[j].pointid == actualPoint[i]) {
						drawPoints.push(points[j]);
						continue;
					}
				}
			}
			
			
			//alert(drawPoints);
			//svgMain.selectAll("*").remove("*");
			var startPointData =[];
			startPointData.push({"x" : startLocation[0], "y": startLocation[1]});
			for(var i = 0; i < drawPoints.length; i++) {
				startPointData.push({"x": drawPoints[i].pointx, "y": drawPoints[i].pointy});
			}
			startPointData.push({"x": endLocation[0], "y": endLocation[1]});
			/*
			
			var dataSet1 = [{ "x": 300,   "y": 150},  { "x": 300,  "y": 250},
			               { "x": 300,   "y": 400},  { "x": 350,  "y": 500},
			               { "x": 400,   "y": 600},  { "x": 700,  "y": 700}];
			*/
			//var lineGrapha = d3.svg.line().x( function(d) { return d.x; }).y( function(d) {return d.y; }).interpolate("linear"); // v3
			var lineGrapha = d3.line().x( function(d) { return d.x; }).y( function(d) {return d.y; }).curve(d3.curveBasis);	// v4
			svgMain.append("path").attr("d", lineGrapha(startPointData)).attr("stroke", "blue").attr("stroke-width",2).attr("fill", "none");
			var urlMarker = "<c:url value='/img/marker.png'></c:url>"
			svgMain
	    	.append("image")
	        .attr("xlink:href", urlMarker)
	        .attr("x", startLocation[0] - 7)
	        .attr("y", startLocation[0] - 40)
	        .attr("width", 14)
	        .attr("height", 20);
			
			svgMain
	    	.append("image")
	        .attr("xlink:href", urlMarker)
	        .attr("x", endLocation[0] - 30)
	        .attr("y", endLocation[1] - 30)
	        .attr("width", 30)
	        .attr("height", 30)
	        .attr("id","vege");
	        //$("#circleJo").hide();
		});
		/*
		var list = document.getElementById("sel2");
		var szov = "";
		while(list.length != 0) {
			list.remove(0);
		}
		
		for (var i = 0; i < list.length; i++) {
			list.remove(i);
		}
		
		for (var i = 0; i < list.length; i++) {
			szov += " " + list[i].text;
		}
		for (var i = 0; i < 10; i++) {
			var option = document.createElement("option");
			option.text = "text" + i;
			list.add(option);
		}		
		$("#szovki").html(szov);
		*/
		var svgMain = d3.select("#adminTrackingDrawingContent").append("svg").attr("width",1500).attr("height",1000);

		$("#adminTrackingUserAndDeviceChooserHeader").click(function(event){
			try {
				var panel =  $("#adminTrackingUserAndDeviceBody");
				if(panel.hasClass("in")) {
					panel.removeClass("in");
				} else {
					panel.addClass("in");
				}
			} catch(error) {
				console.log(error);
			}
		});
	
		
	$("#adminTrackingSelectUsersBTN").click(function(event){
		try {
			event.preventDefault();
			
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			
			var value = document.getElementById("adminTrackingUsersSelect").value.trim();
 
			$.ajax({
				type : "POST",
				async : true,
				url : "<c:url value='/tracking/admin/tracking/getdevicelist'></c:url>",
				beforeSend : function(xhr) {
					xhr.setRequestHeader(header, token);
				},
				data : {
					userid : value
				},
				success : function(result, status, xhr) {
					try {
						clearAndFillSelectElement("adminTrackingDevicesSelect", result);
						$("#adminTrackingDevicesPanelHeader").html("Devices: " + value);
					} catch(error) {
						console.log(error);
					}
				},
				error : function(xhr, status, error) {
					alert("ERROR!");
				}
			});
		} catch(error) {
			console.log(error);
		}	
	});
	
	
	
	function resetFloors() {
		try {
			$("#adminTrackingGroundFloorDIV").html("");
			groundFloorMap = d3.select("#adminTrackingGroundFloorDIV").append("svg").attr("width", "100%")
				.attr("height", "100%").attr("viewBox", "0 0 1196 705");
			groundFloorMap.append("image").attr("xlink:href", groundFloorImageSource)
				.attr("x", 0).attr("y", 0).attr("width", 1196).attr("height", 705);
			
			$("#adminTrackingFirstFloorDIV").html("");
			firstFloorMap = d3.select("#adminTrackingFirstFloorDIV").append("svg").attr("width", "100%")
				.attr("height", "100%").attr("viewBox", "0 0 1196 705");
			firstFloorMap.append("image").attr("xlink:href", firstFloorImageSource)
				.attr("x", 0).attr("y", 0).attr("width", 1196).attr("height", 705);
			
			$("#adminTrackingSecondFloorDIV").html("");
			secondFloorMap = d3.select("#adminTrackingSecondFloorDIV").append("svg").attr("width", "100%")
				.attr("height", "100%").attr("viewBox", "0 0 1196 705");
			secondFloorMap.append("image").attr("xlink:href", secondFloorImageSource)
				.attr("x", 0).attr("y", 0).attr("width", 1196).attr("height", 705);
			
		} catch(error) {
			console.log(error);
		}
	}
	
	$("#adminTrackingSelectDeviceBTN").click(function(event){
		try {
			event.preventDefault();
			
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			 
			$.ajax({
				type : "POST",
				async : true,
				url : "<c:url value='/tracking/admin/tracking/getpositions'></c:url>",
				beforeSend : function(xhr) {
					xhr.setRequestHeader(header, token);
				},
				data : {
					
				},
				success : function(result, status, xhr) {
					try {
						positions = result;
						resetFloors();
						adminTrackingPositionsTable.clear().draw();
						fillDataTableWithValues(adminTrackingPositionsTable, positions);
						drawPositions(positions, [groundFloorMap, firstFloorMap, secondFloorMap], markerImageSource);
						//createMap("adminTrackingFirstFloorDIV", firstFloorImageSource);
						drawGraphPoints(groundFloorMap, graphNodesGroundFloor);
						drawGraphPoints(firstFloorMap, graphNodesFirstFloor);
						drawGraphPoints(secondFloorMap, graphNodesSecondFloor);
						drawArea(groundFloorMap, ZonesGroundFloor);
						drawArea(firstFloorMap, ZonesFirstFloor);
						drawArea(secondFloorMap, ZonesSecondFloor);
						var url = "<c:url value='/tracking/admin/tracking/calculatepath'></c:url>";
						generatePath(positions, url);
					} catch(error) {
						console.log(error);
					}
				},
				error : function(xhr, status, error) {
					alert("ERROR!");
				}
			});
		} catch(error) {
			console.log(error);
		}
	});
</script>

<jsp:directive.include file="adminNavbar.jsp" />

<div class="container-fluid">
	<div class="row">
		<div class="col-lg-12">
		
			<div class="panel-group">
			  <div class="panel panel-default">
			    <div class="panel-heading" id="adminTrackingUserAndDeviceChooserHeader">
			      <h4 class="panel-title" style="text-align: center">
			        <span class="fa fa-arrow-circle-down"></span>
			        Users and devices
			        <span class="fa fa-arrow-circle-down"></span>
			      </h4>
			    </div>
			    <div id="adminTrackingUserAndDeviceBody" class="panel-collapse collapse">
			      <div class="panel-body">
			      		<div class="panel panel-default">
							<div class="panel-heading">
								Users:
							</div>
							<div class="panel-body">
								<select  class="form-control" id="adminTrackingUsersSelect">
				       				<c:forEach items="${userids}" var="userid">
				       					<option>${userid}</option>
				       				</c:forEach>
				      			</select>
				      			<br />
				      			<input type="button" value="Select user!" id="adminTrackingSelectUsersBTN" >    			
				      			<br />
							</div>
						</div>
						
						<div class="panel panel-default">
							<div class="panel-heading" id="adminTrackingDevicesPanelHeader">
								Devices:
							</div>
							<div class="panel-body">
			   			
				      			<select  class="form-control" id="adminTrackingDevicesSelect">
				      			</select>
				      			<br />
				      			<input type="button" value="Select device!" id="adminTrackingSelectDeviceBTN">   			
							</div>
						</div>
			      </div>
			    </div>
			  </div>
			</div>		
		</div>	
			
		<div class="col-lg-6">
			<div class="panel panel-default">
				<div class="panel-heading">			
					Date picker from - to !	      			
	      			<input type="button" value="DRAW111!" id="draw111">
	      			<input type="button" value="DRAW1!" id="draw1"><br />
	      			<input type="button" value="DRAW2!" id="draw2">
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
					<table class="display nowrap" id="adminTrackingPositionsTable" width="100%">
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
			    <div id="adminTrackingGroundFloorDIV"></div>
			  </div>
			  <div id="menu2" class="tab-pane fade">
			    <div id="adminTrackingFirstFloorDIV"></div>
			  </div>
			  <div id="menu3" class="tab-pane fade">
			    <div id="adminTrackingSecondFloorDIV"></div>
			  </div>
			</div>
		</div>
	</div>
	
	<div class="row">
		<div class="col-lg-12">
			<div class="panel panel-default">
				<div class="panel-heading">
					<ul class="breadcrumb">
					  <li><a id="adminTrackingPositionsTableDrawMenuItem" href="#">Positions table</a></li>
					  <li><a id="adminTrackingGroundFloorMapDrawMenuItem" href="#">Ground floor</a></li>
					  <li><a id="adminTrackingFirstFloorMapDrawMenuItem" href="#">First floor</a></li>
					  <li><a id="adminTrackingSecondFloorMapDrawMenuItem">Second floor</a></li> 
					</ul>
				</div>
				<div class="panel-body" id="adminTrackingDrawingContent">
					
				</div>
			</div>
		</div>
	</div>
</div>