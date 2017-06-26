<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page session="true"%>
<%@ page isELIgnored="false"%>

<script type="text/javascript">

	var adminNavbarNavigationLock = true;

	$(".adminNavbarNavigationClass").click(function(event){
		try {
			event.preventDefault();
			if (adminNavbarNavigationLock == true) {
				adminNavbarNavigationLock = false;
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
						adminNavbarNavigationLock = true;
						$("#page-wrapper").html(result);
					} catch(error) {
						console.log(error);
					}
				},
				error : function(xhr, status, error) {
					try {
						adminNavbarNavigationLock = true;
						$("#adminNavbarNavigationErrorDIV")
							.html("<p class='text-danger bg-primary'>Service error!</p>");
					} catch(err) {
						console.log(err);
					}
				},
				timeout : 10000
			});
		} catch(error) {
			try {
				adminNavbarNavigationLock = true;
				$("#adminNavbarNavigationErrorDIV")
					.html("<p class='text-danger bg-primary'>Service error!</p>");
				console.log(error);
			} catch(err) {
				console.log(err);
			}
		}
	});

</script>

<nav class="navbar navbar-inverse" id="manipageNavbar">
	<div class="container-fluid">
		<div class="navbar-header">
			<div class="navbar-header">
				<a class="navbar-brand"><b>ADMIN</b></a>
			</div>
		</div>
		<ul class="nav navbar-nav">
			<li><a class="adminNavbarNavigationClass"
				href="<c:url value='/tracking/admin/homepage'></c:url>"><span
					class="fa fa-globe"></span> Home</a></li>
		</ul>
		<ul class="nav navbar-nav navbar-left">
			<li><a class="adminNavbarNavigationClass"
				href="<c:url value='/tracking/admin/accountmanagement'></c:url>"><span
					class="glyphicon glyphicon-edit "></span> Account Management </a></li>
			<li><a class="adminNavbarNavigationClass"
				href="<c:url value='/tracking/admin/createuser'></c:url>"><span
					class="glyphicon glyphicon-user"></span> Create User </a></li>
			<li><a class="adminNavbarNavigationClass"
				href="<c:url value='/tracking/admin/listallusers'></c:url>"><span
					class="fa  fa-users"></span> List Users</a></li>
			<li><a class="adminNavbarNavigationClass"
				href="<c:url value='/tracking/admin/centralmanagement'></c:url>"><span
					class="fa fa-gear"></span> Central management</a></li>
			<li><a class="adminNavbarNavigationClass"
				href="<c:url value='/tracking/admin/trackingmainpage'></c:url>"><span
					class="fa fa-share-alt"></span> Track</a></li>
			<li><a class="adminNavbarNavigationClass"
				href="<c:url value='/tracking/admin/statistics'></c:url>"><span
					class="glyphicon glyphicon-tasks"></span> Statistics</a></li>
		</ul>
		<ul class="nav navbar-nav navbar-right">
			<li><a class="adminNavbarNavigationClass"
				href="<c:url value='/tracking/logout'></c:url>"><span
					class="fa fa-power-off"></span> Logut</a></li>
		</ul>
	</div>
</nav>
<div id="adminNavbarNavigationErrorDIV"></div>