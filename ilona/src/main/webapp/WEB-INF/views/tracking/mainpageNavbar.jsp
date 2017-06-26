<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@page session="true"%>
<%@ page isELIgnored="false"%>

<script src="<c:url value='/js/trackingGeneralFunctions.js'></c:url>"></script>
<script type="text/javascript">
	
	var mainpageNavbarNavigationLock = true;

	$(".mainpageNavbarNavigationClass").click(function(event){
		try {							
			event.preventDefault();
			if(mainpageNavbarNavigationLock == true) {
				mainpageNavbarNavigationLock == false;
			} else {
				return;
			}
			$.ajax({
				type : "POST",
				async : true,
				url : $(this).attr("href"),
				timeout : 10000,
				beforeSend : function(xhr) {				
					xhr.setRequestHeader($("meta[name='_csrf_header']").attr("content"),
						$("meta[name='_csrf']").attr("content"));
				},
				success : function(result, status, xhr) {
					try {
						mainpageNavbarNavigationLock = true;
						$("#page-wrapper").html(result);
					} catch(error) {
						$("#mainpageNavbarErrorOutputParagh")
							.html("<h2>The tracking service is unreachable!</h2>");
						console.log(error);
					}
				},
				error : function(xhr, status, error) {
					try {
						mainpageNavbarNavigationLock = true;
						$("#mainpageNavbarErrorOutputParagh").html("<h2>The tracking service is unreachable!</h2>");
						console.log("Cause: Status: " + status + " Error: " + error);
					} catch (err) {
						console.log(err);
					}
				}
			});	
		} catch(error) {
			try {
				mainpageNavbarNavigationLock = true;
				$("#mainpageNavbarErrorOutputParagh").html("<h2>The tracking service is unreachable!</h2>");
				console.log(error);
			} catch(finalError) {
				console.log(finalError);
			}
		}			
	});

</script>

<nav class="navbar navbar-inverse" id="mainpageNavbarNavigationDIV">
	<div class="container-fluid">
		<div class="navbar-header">			
			<a class="navbar-brand">ILONA - Main page</a>			
		</div>
		<ul class="nav navbar-nav">
			<li class="active">
				<a class="mainpageNavbarNavigationClass"
					href="<c:url value='/tracking/getmainpagehome'></c:url>">
					<span class="fa fa-th-large"></span> Home			
				</a>
			</li>
		</ul>
		<ul class="nav navbar-nav navbar-left">
			<li>
				<a class="mainpageNavbarNavigationClass"
					href="<c:url value='/tracking/getmainpagesignup'></c:url>">
					<span class="glyphicon glyphicon-user"></span> Sign Up
				</a>
			</li>
			
			<li>
				<a  class="mainpageNavbarNavigationClass"
					href="<c:url value='/tracking/getloginpage'></c:url>">
					<span class="glyphicon glyphicon-log-in"></span> Login
				</a>
			</li>
		</ul>
	</div>		
</nav>

<div>
	<p class="bg-primary text-center" id="mainpageNavbarErrorOutputParagh"></p>
</div>