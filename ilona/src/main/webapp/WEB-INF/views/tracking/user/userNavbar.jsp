<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page session="true"%>
<%@ page isELIgnored="false"%>

<script type="text/javascript">

	var userNavbarNavigationLock = true;

	$(".userNavbarNavigationClass").click(function(event){
		try {
			event.preventDefault();
			if (userNavbarNavigationLock == true) {
				userNavbarNavigationLock = false;
			} else {
				return;
			}
			
			$.ajax({
				async : true,
				type : "POST",
				url : $(this).attr('href'),
				beforeSend : function(xhr) {
					xhr.setRequestHeader($("meta[name='_csrf_header']").attr("content"),
							$("meta[name='_csrf']").attr("content"));
				},
				success : function(result, status, xhr) {
					try {
						userNavbarNavigationLock = true;
						$("#page-wrapper").html(result);
					} catch(error) {
						console.log(error);
					}
				},
				error : function(xhr, status, error) {
					try {
						userNavbarNavigationLock = true;
						$("#userNavbarNavigationErrorDIV").html("<h2>The tracking service is unreachable!<h2>");
					} catch(error) {
						console.log(error);
					}
				},
				timeout : 10000
			});
		} catch(error) {
			try {
				userNavbarNavigationLock = true;
				$("#userNavbarNavigationErrorDIV").html("Service error!<h2>");
			} catch(err) {
				console.log(err);
			}		
		}
	});

</script>

<nav class="navbar navbar-inverse" id="userNavbarDIV">
	<div class="container-fluid">
		<div class="navbar-header">
			<div class="navbar-header">
				<a class="navbar-brand"><b>USER</b></a>
			</div>
		</div>
		<ul class="nav navbar-nav">
			<li>
				<a id="userNavbarHomeMenuitem" class="userNavbarNavigationClass"
					href="<c:url value='/tracking/user/homepage'></c:url>">
					<span class="fa fa-globe"></span> Home
				</a>
			</li>
		</ul>
		<ul class="nav navbar-nav navbar-left">
			<li>
				<a id="userNavbarAccountManagement" class="userNavbarNavigationClass"
					href="<c:url value='/tracking/user/accountmanagement'></c:url>">
					<span class="glyphicon glyphicon-edit "></span> Account Management 
				</a>
			</li>
			
			<li>
				<a id="userNavbarManageDevices" class="userNavbarNavigationClass"
					href="<c:url value='/tracking/user/managedevices'></c:url>">
					<span class="glyphicon glyphicon-user"></span> Manage devices 
				</a>
			</li>
			
			<li>
				<a id="userNavbarAddDevice" class="userNavbarNavigationClass"
					href="<c:url value='/tracking/user/createdevicepage'></c:url>">
					<span class="glyphicon glyphicon-user"></span> Create device 
				</a>
			</li>
					
			<li>
				<a id="userNavbarDeviceTracking" class="userNavbarNavigationClass"
					href="<c:url value='/tracking/user/devicetracking'></c:url>">
					<span class="fa fa-share-alt"></span> Tracking 
				</a>
			</li>
		</ul>
		<ul class="nav navbar-nav navbar-right">
			<li>
				<a id="userNavbarLogout" class="userNavbarNavigationClass"
					href="<c:url value='/tracking/logout'></c:url>">
					<span class="fa fa-power-off"></span> Logut
				</a>
			</li>
		</ul>
	</div>
</nav>

<div>
	<p class="bg-primary text-center" id="userNavbarNavigationErrorDIV"></p>
</div>
