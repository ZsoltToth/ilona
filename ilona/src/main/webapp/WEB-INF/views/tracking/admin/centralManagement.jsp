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

<script type="text/javascript" src="<c:url value='/js/tracking/centralManagementUtil.js'></c:url>"/>
<script>
	
	var states = {
			enabledCheck : ${enabledCheck},
			enabledCheckLock: {value: true},
			accountExpCheck: ${accExpCheck},
			accountExpCheckLock: {value: true},
			accountExpTime: ${accExpTime},
			accountExpTimeLock: {value: true},
			accountExpTimeMin: 2678400000, // one month
			accountExpTimeMax: 3 * 31536000000, // 3 years
			credValidityPeriod: ${credValPeriod},
			credValidityPeriodLock: {value: true},
			credValidityPeriodMin: 2678400000,
			credValidityPeriodMax: 3 * 31536000000,
			badLoginsUpperBound: ${badLoginUpperBound},
			badLoginsUpperBoundLock: {value: true},
			badLoginsUpperBoundMin: 3,
			badLoginsUpperBoundMax: 20,
			lockedCheck: ${lockedCheck},
			lockedCheckLock: {value: true},
			lockedTimeAfterBadLogins: ${lockedTimeAfterBadLogin},
			lockedTimeAfterBadLoginsLock: {value: true},
			lockedTimeAfterBadLoginsMin: 60000, // one minute
			lockedTimeAfterBadLoginsMax: 2678400000, // 
			passwordRecoveryTokenTime: ${passwordRecoveryTokenTime},
			passwordRecoveryTokenTimeLock: {value: true},
			passwordRecoveryTokenTimeMin: 10 * 60000, // 10 minutes
			passwordRecoveryTokenTimeMax: 7 * 86400000 // one week
	};
	
	$(document).ready(function(){
		//generateText();
		
		if(states.enabledCheck == true) {
			$("#adminCentManEnableCheckOnOff").attr("value", "ON");
			$("#adminCentManEnableCheckOnOff").addClass("btn-success");
		} else {
			$("#adminCentManEnableCheckOnOff").attr("value", "OFF");
			$("#adminCentManEnableCheckOnOff").addClass("btn-danger");
		}
		
		if(states.accountExpCheck == true) {
			$("#adminCentManAccExpirationCheckOnOff").attr("value", "ON");
			$("#adminCentManAccExpirationCheckOnOff").addClass("btn-success");
		} else {
			$("#adminCentManAccExpirationCheckOnOff").attr("value", "OFF");
			$("#adminCentManAccExpirationCheckOnOff").addClass("btn-danger");
		}
		
		if(states.lockedCheck == true) {
			$("#adminCentManAccLockCheckOnOff").attr("value", "ON");
			$("#adminCentManAccLockCheckOnOff").addClass("btn-success");
		} else {
			$("#adminCentManAccLockCheckOnOff").attr("value", "OFF");
			$("#adminCentManAccLockCheckOnOff").addClass("btn-danger");
		}
		
		var data = calculateYearMonthDayHour(states.accountExpTime);
		$("#adminCentManAccountExpirationOutputDIV").html(generateText(data));
		
		data = calculateYearMonthDayHour(states.credValidityPeriod);
		$("#adminCentManCredentialsExpirationOutputDIV").html(generateText(data));
		
		data = calculateDayHourMinute(states.lockedTimeAfterBadLogins);
		$("#adminCentManLockTimeAfterBadLoginOutputDIV").html(generateDaysHoursMinutesText(data));
		
		data = calculateDayHourMinute(states.passwordRecoveryTokenTime);
		$("#adminCentManPassRecTokenTimeOutputDIV").html(generateDaysHoursMinutesText(data));
		
		$("#adminCentManMaximumBadLoginsResultDIV").html(states.badLoginsUpperBound);		
		
	});
	
	
</script>

<jsp:directive.include file="adminNavbar.jsp" />

<div class="container-fluid">
	<div class="row">
		<div class="col-lg-4">
			<div class="panel panel-default">
				<div class="panel-heading">					
						<b>Account expiration duration</b>
				</div>
				<div class="panel-body">
					<div>
						<table class="table table-condensed">
							<thead>
								<tr>
									<td>Years</td>
									<td>Months</td>
									<td>Days</td>
									<td>Hours</td>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>
										<input type="button" value="+" class="col-sm adminCentManAccountChangeValueInMillisClass" 
											data-expirationtime="yearplus" data-element="accountExpTime"
											data-result="adminCentManAccountExpirationOutputDIV">
										<input type="button" value="-" class="col-sm adminCentManAccountChangeValueInMillisClass"
											data-expirationtime="yearminus" data-element="accountExpTime"
											data-result="adminCentManAccountExpirationOutputDIV">
									</td>
									<td>
										<input type="button" value="+" class="col-sm adminCentManAccountChangeValueInMillisClass"
											data-expirationtime="monthplus" data-element="accountExpTime"
											data-result="adminCentManAccountExpirationOutputDIV">
										<input type="button" value="-" class="col-sm adminCentManAccountChangeValueInMillisClass" 
											data-expirationtime="monthminus" data-element="accountExpTime"
											data-result="adminCentManAccountExpirationOutputDIV">
									</td>
									<td>
										<input type="button" value="+" class="col-sm adminCentManAccountChangeValueInMillisClass" 
											data-expirationtime="dayplus" data-element="accountExpTime"
											data-result="adminCentManAccountExpirationOutputDIV">
										<input type="button" value="-" class="col-sm adminCentManAccountChangeValueInMillisClass" 
											data-expirationtime="dayminus" data-element="accountExpTime"
											data-result="adminCentManAccountExpirationOutputDIV">
									</td>
									<td>
										<input type="button" value="+" class="col-sm adminCentManAccountChangeValueInMillisClass" 
											data-expirationtime="hourplus" data-element="accountExpTime"
											data-result="adminCentManAccountExpirationOutputDIV">
										<input type="button" value="-" class="col-sm adminCentManAccountChangeValueInMillisClass" 
											data-expirationtime="hourminus" data-element="accountExpTime"
											data-result="adminCentManAccountExpirationOutputDIV">
									</td>
								</tr>
							</tbody>
						</table>
						<label>Last login validity time:</label>
						<div id="adminCentManAccountExpirationOutputDIV"></div>
						<input type="button" value="UPDATE" data-result="adminCentManAccountExpirationUpdateResultDIV"
							data-element="accountExpTime" class="centManUpdateLongValue"
							data-url="<c:url value='/tracking/admin/centman/updateaccexptime'/>">
						<div id="adminCentManAccountExpirationUpdateResultDIV"></div>
					</div>
				</div> <!-- panel body ending -->
			</div> <!-- panel ending -->
		</div> <!-- COL-LG-4 ending -->
		
		<div class="col-lg-4">
			<div class="panel panel-default">
				<div class="panel-heading">					
						<b>Credentials validity time</b>
				</div>
				<div class="panel-body">
					<div>
						<table class="table table-condensed">
							<thead>
								<tr>
									<td>Years</td>
									<td>Months</td>
									<td>Days</td>
									<td>Hours</td>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>
										<input type="button" value="+" class="col-sm adminCentManAccountChangeValueInMillisClass" 
											data-expirationtime="yearplus" data-element="credValidityPeriod"
											data-result="adminCentManCredentialsExpirationOutputDIV">
										<input type="button" value="-" class="col-sm adminCentManAccountChangeValueInMillisClass"
											data-expirationtime="yearminus" data-element="credValidityPeriod"
											data-result="adminCentManCredentialsExpirationOutputDIV">
									</td>
									<td>
										<input type="button" value="+" class="col-sm adminCentManAccountChangeValueInMillisClass"
											data-expirationtime="monthplus" data-element="credValidityPeriod"
											data-result="adminCentManCredentialsExpirationOutputDIV">
										<input type="button" value="-" class="col-sm adminCentManAccountChangeValueInMillisClass" 
											data-expirationtime="monthminus" data-element="credValidityPeriod"
											data-result="adminCentManCredentialsExpirationOutputDIV">
									</td>
									<td>
										<input type="button" value="+" class="col-sm adminCentManAccountChangeValueInMillisClass" 
											data-expirationtime="dayplus" data-element="credValidityPeriod"
											data-result="adminCentManCredentialsExpirationOutputDIV">
										<input type="button" value="-" class="col-sm adminCentManAccountChangeValueInMillisClass" 
											data-expirationtime="dayminus" data-element="credValidityPeriod"
											data-result="adminCentManCredentialsExpirationOutputDIV">
									</td>
									<td>
										<input type="button" value="+" class="col-sm adminCentManAccountChangeValueInMillisClass" 
											data-expirationtime="hourplus" data-element="credValidityPeriod"
											data-result="adminCentManCredentialsExpirationOutputDIV">
										<input type="button" value="-" class="col-sm adminCentManAccountChangeValueInMillisClass" 
											data-expirationtime="hourminus" data-element="credValidityPeriod"
											data-result="adminCentManCredentialsExpirationOutputDIV">
									</td>
								</tr>
							</tbody>
						</table>
						<label>Credentials validity time:</label>
						<div id="adminCentManCredentialsExpirationOutputDIV"></div>
						<input type="button" value="UPDATE" data-result="adminCentManCredentialsExpirationUpdateResultDIV"
							data-element="credValidityPeriod" class="centManUpdateLongValue"
							data-url="<c:url value='/tracking/admin/centman/updatecredvalidtime'/>">
						<div id="adminCentManCredentialsExpirationUpdateResultDIV"></div>
					</div>
				</div> <!-- panel body ending -->
			</div> <!-- panel ending -->
		</div> <!-- COL-LG-4 ending -->
		
		<div class="col-lg-4">
			<div class="panel panel-default">
				<div class="panel-heading">					
						<b>Lock time after bad logins</b>
				</div>
				<div class="panel-body">
					<div>
						<table class="table table-condensed">
							<thead>
								<tr>
									<td>Days</td>
									<td>Hours</td>
									<td>Minutes</td>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>
										<input type="button" value="+" class="col-sm adminCentManAccountChangeValueInMillisClass" 
											data-expirationtime="dayplus" data-element="lockedTimeAfterBadLogins" data-execute="DayHourMinute"
											data-result="adminCentManLockTimeAfterBadLoginOutputDIV">
										<input type="button" value="-" class="col-sm adminCentManAccountChangeValueInMillisClass"
											data-expirationtime="dayminus" data-element="lockedTimeAfterBadLogins" data-execute="DayHourMinute"
											data-result="adminCentManLockTimeAfterBadLoginOutputDIV">
									</td>
									<td>
										<input type="button" value="+" class="col-sm adminCentManAccountChangeValueInMillisClass"
											data-expirationtime="hourplus" data-element="lockedTimeAfterBadLogins" data-execute="DayHourMinute"
											data-result="adminCentManLockTimeAfterBadLoginOutputDIV">
										<input type="button" value="-" class="col-sm adminCentManAccountChangeValueInMillisClass" 
											data-expirationtime="hourminus" data-element="lockedTimeAfterBadLogins" data-execute="DayHourMinute"
											data-result="adminCentManLockTimeAfterBadLoginOutputDIV">
									</td>
									<td>
										<input type="button" value="+" class="col-sm adminCentManAccountChangeValueInMillisClass" 
											data-expirationtime="minuteplus" data-element="lockedTimeAfterBadLogins" data-execute="DayHourMinute"
											data-result="adminCentManLockTimeAfterBadLoginOutputDIV">
										<input type="button" value="-" class="col-sm adminCentManAccountChangeValueInMillisClass" 
											data-expirationtime="minuteminus" data-element="lockedTimeAfterBadLogins" data-execute="DayHourMinute"
											data-result="adminCentManLockTimeAfterBadLoginOutputDIV">
									</td>								
								</tr>
							</tbody>
						</table>
						<label>Lock time after bad logins:</label>
						<div id="adminCentManLockTimeAfterBadLoginOutputDIV"></div>
						<input type="button" value="UPDATE" data-result="adminCentManLockTimeAfterBadLoginUpdateResultDIV"
							data-element="lockedTimeAfterBadLogins" class="centManUpdateLongValue"
							data-url="<c:url value='/tracking/admin/centman/updatebantimeafterbadlogins'/>">
						<div id="adminCentManLockTimeAfterBadLoginUpdateResultDIV"></div>
					</div>
				</div> <!-- panel body ending -->
			</div> <!-- panel ending -->
		</div> <!-- COL-LG-4 ending -->
		
	</div>
	
	<!-- ================================ FIRST ROW END ===================================================== -->
			
	<div class="row">
		<div class="col-lg-3">
			<div class="panel panel-default">
				<div class="panel-heading">					
						<b>Account enabled check </b>
				</div>
				<div class="panel-body">
					<div class="table-responsive">
						<table class="table">
							<tbody>
								<tr>
									<td>
										<input type="button" value="" class="btn onOffButton"
											id="adminCentManEnableCheckOnOff" >									
									</td>
									<td>
										<input type="button" value="Update" class="btn btn-primary centManUpdateState"
											data-url="<c:url value='/tracking/admin/centman/updateenabledcheck'/>" 
											data-result="adminCentManAccountEnabledResultDIV"
											data-value="adminCentManEnableCheckOnOff"
											data-lock="enabledCheckLock">
									</td>
								</tr>
							</tbody>
						</table>
						<div id="adminCentManAccountEnabledResultDIV"></div>			
					</div>
				</div> <!-- panel body ending -->
			</div> <!-- panel ending -->
		</div>
		
		<div class="col-lg-3">
			<div class="panel panel-default">
				<div class="panel-heading">					
						<b>Account expiration check </b>
				</div>
				<div class="panel-body">
					<div class="table-responsive">
						<table class="table">
							<tbody>
								<tr>
									<td>
										<input type="button" value="" class="btn onOffButton"
											id="adminCentManAccExpirationCheckOnOff" >									
									</td>
									<td>
										<input type="button" value="Update" class="btn btn-primary centManUpdateState"
											data-url="<c:url value='/tracking/admin/centman/updateaccexpcheck'/>" 
											data-result="adminCentManAccExpirationCheckResultDIV"
											data-value="adminCentManAccExpirationCheckOnOff"
											data-lock=accountExpCheckLock>
									</td>
								</tr>
							</tbody>
						</table>
						<div id="adminCentManAccExpirationCheckResultDIV"></div>			
					</div>
				</div> <!-- panel body ending -->
			</div> <!-- panel ending -->
		</div>
		
		<div class="col-lg-3">
			<div class="panel panel-default">
				<div class="panel-heading">					
						<b>Account locked check </b>
				</div>
				<div class="panel-body">
					<div class="table-responsive">
						<table class="table">
							<tbody>
								<tr>
									<td>
										<input type="button" value="" class="btn onOffButton"
											id="adminCentManAccLockCheckOnOff" >									
									</td>
									<td>
										<input type="button" value="Update" class="btn btn-primary centManUpdateState"
											data-url="<c:url value='/tracking/admin/centman/updatelockcheckenabled'/>" 
											data-result="adminCentManAccLockCheckResultDIV"
											data-value="adminCentManAccLockCheckOnOff"
											data-lock="lockedCheckLock">
									</td>
								</tr>
							</tbody>
						</table>
						<div id="adminCentManAccLockCheckResultDIV"></div>			
					</div>
				</div> <!-- panel body ending -->
			</div> <!-- panel ending -->
		</div>
	
		<div class="col-lg-3">
			<div class="panel panel-default">
				<div class="panel-heading">					
						<b>Maximum bad logins </b>
				</div>
				<div class="panel-body">
					<div class="table-responsive">
						<table class="table">
							<thead>
								<tr>
									<td>Value</td>
									<td>Update</td>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>
										<input type="button" value="+" class="col-sm adminCentManAccountChangeValueInMillisClass" 
											data-expirationtime="valueplus" data-element="badLoginsUpperBound" data-execute="value"
											data-result="adminCentManMaximumBadLoginsResultDIV">
										<input type="button" value="-" class="col-sm adminCentManAccountChangeValueInMillisClass" 
											data-expirationtime="valueminus" data-element="badLoginsUpperBound" data-execute="value"
											data-result="adminCentManMaximumBadLoginsResultDIV">								
									</td>
									<td>
										<input type="button" value="UPDATE" data-result="adminCentManMaximumBadLoginsUpdateResultDIV"
											data-element="badLoginsUpperBound" class="centManUpdateLongValue"
											data-url="<c:url value='/tracking/admin/centman/updatebadloginuppervalue'/>">
									</td>
								</tr>
							</tbody>
						</table>
						<label>Value:</label>
						<div id="adminCentManMaximumBadLoginsResultDIV"></div>	
						
						<div id="adminCentManMaximumBadLoginsUpdateResultDIV"></div>		
					</div>
				</div> <!-- panel body ending -->
			</div> <!-- panel ending -->
		</div>
	
	</div>
	
	<div class="row">
		<div class="col-lg-4">
			<div class="panel panel-default">
				<div class="panel-heading">					
						<b>Password recovery token validity time</b>
				</div>
				<div class="panel-body">
					<div>
						<table class="table table-condensed">
							<thead>
								<tr>
									<td>Days</td>
									<td>Hours</td>
									<td>Minutes</td>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>
										<input type="button" value="+" class="col-sm adminCentManAccountChangeValueInMillisClass" 
											data-expirationtime="dayplus" data-element="passwordRecoveryTokenTime" data-execute="DayHourMinute"
											data-result="adminCentManPassRecTokenTimeOutputDIV">
										<input type="button" value="-" class="col-sm adminCentManAccountChangeValueInMillisClass"
											data-expirationtime="dayminus" data-element="passwordRecoveryTokenTime" data-execute="DayHourMinute"
											data-result="adminCentManPassRecTokenTimeOutputDIV">
									</td>
									<td>
										<input type="button" value="+" class="col-sm adminCentManAccountChangeValueInMillisClass"
											data-expirationtime="hourplus" data-element="passwordRecoveryTokenTime" data-execute="DayHourMinute"
											data-result="adminCentManPassRecTokenTimeOutputDIV">
										<input type="button" value="-" class="col-sm adminCentManAccountChangeValueInMillisClass" 
											data-expirationtime="hourminus" data-element="passwordRecoveryTokenTime" data-execute="DayHourMinute"
											data-result="adminCentManPassRecTokenTimeOutputDIV">
									</td>
									<td>
										<input type="button" value="+" class="col-sm adminCentManAccountChangeValueInMillisClass" 
											data-expirationtime="minuteplus" data-element="passwordRecoveryTokenTime" data-execute="DayHourMinute"
											data-result="adminCentManPassRecTokenTimeOutputDIV">
										<input type="button" value="-" class="col-sm adminCentManAccountChangeValueInMillisClass" 
											data-expirationtime="minuteminus" data-element="passwordRecoveryTokenTime" data-execute="DayHourMinute"
											data-result="adminCentManPassRecTokenTimeOutputDIV">
									</td>								
								</tr>
							</tbody>
						</table>
						<label>Password recovery token validity time:</label>
						<div id="adminCentManPassRecTokenTimeOutputDIV"></div>
						<input type="button" value="UPDATE" data-result="adminCentManPassRecTokenTimeUpdateResultDIV"
							data-element="passwordRecoveryTokenTime" class="centManUpdateLongValue"
							data-url="<c:url value='/tracking/admin/centman/updatepasstokvaliditytime'/>">
						<div id="adminCentManPassRecTokenTimeUpdateResultDIV"></div>
					</div>
				</div> <!-- panel body ending -->
			</div> <!-- panel ending -->
		</div> <!-- COL-LG-4 ending -->
		

	</div>
</div>