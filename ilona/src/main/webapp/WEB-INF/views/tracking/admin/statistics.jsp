<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="true"%>
<%@ page isELIgnored="false"%>

<!-- CSRF Protection token -->
<meta name="_csrf" content="${_csrf.token}" />
<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}" />

<script src="https://d3js.org/d3.v4.min.js"></script>
<script src="<c:url value='/js/Donut3D.js'></c:url>"></script>

<script type="text/javascript">
	
	$("#adminStatisticsUserCountRefreshBTN").click(function(event){
		try {
			event.preventDefault();
			$("#adminStatisticsUserCountDountGraphDIV").html("");
			var userData = [
				{label: "Users", value: 156, color: "#3366CC"},
				{label: "Admins", value: 23, color: "#FF9900"}
			];
			
			var userDataBase = [
			    			{label: "Users", value: 0, color: "#3366CC"},
			    			{label: "Admins", value: 100, color: "#FF9900"}
			    		];
			var svg = d3.select("#adminStatisticsUserCountDountGraphDIV").append("svg").attr("width",300).attr("height",250);
			svg.append("g").attr("id","salesDonut");
			//Donut3D.draw("salesDonut", userDataBase, 150, 100, 130, 100, 30, 0.4);
			
			//Donut3D.transition("salesDonut", userData , 150, 100, 30, 0.4);
		} catch(error) {
			console.log(error);
		}
	});
	
</script>

<style>
/*
body {
  font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
  width: 960px;
  height: 500px;
  position: relative;
}
*/
path.slice{
	stroke-width:2px;
}
polyline{
	opacity: .3;
	stroke: black;
	stroke-width: 2px;
	fill: none;

} 
svg text.percent{
	fill:white;
	text-anchor:middle;
	font-size:12px;
}

.bar {
  fill: steelblue;
}

.bar:hover {
  fill: brown;
}

.axis {
  font: 10px sans-serif;
}

.axis path,
.axis line {
  fill: none;
  stroke: #000;
  shape-rendering: crispEdges;
}

.x.axis path {
  display: none;
}

</style>

<script>

$("#drawonoff").click(function(){
	
$("#adminStatisticsOnlineUsersDIV").html("");
var myData = [{letter: "All users", frequency: 120},
			  {letter: "Normal users", frequency: 110},
			  {letter: "Admins", frequency: 10}];

var margin = {top: 20, right: 20, bottom: 30, left: 40},
	width = 500 - margin.left - margin.right,
	height = 300 - margin.top - margin.bottom;

var x = d3.scaleOrdinal()
	.domain([0, width/2]);

var y = d3.scaleLinear()
	.range([height, 0]);

var xAxis = d3.axisBottom(x);

var yAxis = d3.axisLeft(y);

var svg = d3.select("#adminStatisticsOnlineUsersDIV").append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
  .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

//d3.tsv("data.tsv", type, function(error, data) {
  //if (error) throw error;

  //x.domain(myData.map(function(d) { return d.letter; }));
  	x.domain(["All users","Normal users","Admins"]);
  //y.domain([0, d3.max(myData, function(d) { return d.frequency; })]);
	y.domain([0, 150]);
  svg.append("g")
      .attr("class", "x axis")
      .attr("transform", "translate(0," + height + ")")
      .call(xAxis);

  svg.append("g")
      .attr("class", "y axis")
      .call(yAxis)
      .append("text")
      .attr("transform", "rotate(-90)")
      .attr("y", 6)
      .attr("dy", ".71em")
      .style("text-anchor", "end")
      .text("User count");

  svg.selectAll(".bar")
      .data(myData)
    .enter().append("rect")
      .attr("class", "bar")
      .attr("x", function(d) { return x(d.letter); })
      .attr("width", x.rangeBand())
      .attr("y", function(d) { return y(d.frequency); })
      .attr("height", function(d) { return height - y(d.frequency); });
//});

function type(d) {
  d.frequency = +d.frequency;
  return d;
}
});
</script>


<jsp:directive.include file="adminNavbar.jsp" />

<div class="row" id="adminMainpageContent">
	<div class="col-lg-3">
		<div class="panel panel-default">
			<div class="panel-heading">
				User number statistics?! ez a név?
			</div>
			<div class="panel-body" id="adminStatisticsUserCountDIVBody">
				<p>Total users: 120</p>
				<p>Total users: 120</p>
				<p>Total users: 120</p>
				<div id="adminStatisticsUserCountDountGraphDIV"></div>
				<p class="text-align:center">
					<input type="button" class="btn" value="Refresh" id="adminStatisticsUserCountRefreshBTN">
				</p>
			</div>
		</div>
	</div>
	
	<div class="col-lg-9">
		<div class="panel panel-default">
			<div class="panel-heading">
				Users availability:  userek melyik zónában tartózkodtak piechart, legyen hozzá dao	
			</div>
			<div class="panel-body" id="adminStatisticsOnlineUsersDIV">
				<input type="button" value="Draw online/offline" id="drawonoff">
			</div>
		</div>
	</div>
	
	
</div>
