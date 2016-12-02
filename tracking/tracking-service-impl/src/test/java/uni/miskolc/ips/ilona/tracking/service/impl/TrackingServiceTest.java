package uni.miskolc.ips.ilona.tracking.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import uni.miskolc.ips.ilona.measurement.model.measurement.Measurement;
import uni.miskolc.ips.ilona.measurement.model.position.Position;
import uni.miskolc.ips.ilona.positioning.service.PositioningService;
import uni.miskolc.ips.ilona.tracking.model.DeviceData;
import uni.miskolc.ips.ilona.tracking.model.TrackPosition;
import uni.miskolc.ips.ilona.tracking.persist.TrackingDAO;
import uni.miskolc.ips.ilona.tracking.persist.exception.DeviceNotFoundException;
import uni.miskolc.ips.ilona.tracking.persist.exception.OperationExecutionErrorException;
import uni.miskolc.ips.ilona.tracking.persist.exception.PositionAlreadyExistsException;
import uni.miskolc.ips.ilona.tracking.service.TrackingService;
import uni.miskolc.ips.ilona.tracking.service.exceptions.DuplicatedPositionException;
import uni.miskolc.ips.ilona.tracking.service.exceptions.ServiceGeneralErrorException;

public class TrackingServiceTest {

	@Test
	public void testCalculatePosition() throws Exception {
		TrackingService trs = new TrackingServiceImpl();
		// Measurement mes = EasyMock.createMock(Measurement.class);
		UUID uuid = UUID.randomUUID();
		Measurement mes = new Measurement();
		mes.setId(uuid);
		Position pos = EasyMock.createMock(Position.class);
		EasyMock.expect(pos.getUUID()).andReturn(uuid).anyTimes();
		EasyMock.replay(pos);
		PositioningService posser = EasyMock.createMock(PositioningService.class);
		EasyMock.expect(posser.determinePosition(mes)).andReturn(pos).anyTimes();
		EasyMock.replay(posser);
		((TrackingServiceImpl) trs).setPositionService(posser);
		System.out.println(" " + posser);
		trs.calculatePosition(mes);
	}

	@Test
	public void testStorePosition() throws Exception {
		TrackingService trs = new TrackingServiceImpl();
		Position pos = new Position();
		UUID uuid = UUID.randomUUID();
		pos.setUUID(uuid);
		DeviceData devdata = EasyMock.createMock(DeviceData.class);
		EasyMock.expect(devdata.getDeviceid()).andReturn("devid");
		EasyMock.replay(devdata);
		TrackingDAO trd = EasyMock.createMock(TrackingDAO.class);
		trd.storePosition(devdata, pos);
		EasyMock.expectLastCall().andVoid();
		EasyMock.replay(trd);
		((TrackingServiceImpl) trs).setTrackingDAO(trd);

		trs.storePosition(devdata, pos);
	}

	@Test
	public void testStorePositionPositionAlreadyExists() throws Exception {
		try {
			TrackingService trs = new TrackingServiceImpl();
			Position pos = new Position();
			UUID uuid = UUID.randomUUID();
			pos.setUUID(uuid);
			DeviceData devdata = EasyMock.createMock(DeviceData.class);
			EasyMock.expect(devdata.getDeviceid()).andReturn("devid");
			EasyMock.replay(devdata);
			TrackingDAO trd = EasyMock.createMock(TrackingDAO.class);
			trd.storePosition(devdata, pos);
			EasyMock.expectLastCall().andThrow(new PositionAlreadyExistsException());
			EasyMock.replay(trd);
			((TrackingServiceImpl) trs).setTrackingDAO(trd);

			trs.storePosition(devdata, pos);
		} catch (DuplicatedPositionException e) {
			return;
		}
		Assert.fail();
	}

	@Test
	public void testStorePositionDeviceNotFound() throws Exception {
		try {
			TrackingService trs = new TrackingServiceImpl();
			Position pos = new Position();
			UUID uuid = UUID.randomUUID();
			pos.setUUID(uuid);
			DeviceData devdata = EasyMock.createMock(DeviceData.class);
			EasyMock.expect(devdata.getDeviceid()).andReturn("devid");
			EasyMock.replay(devdata);
			TrackingDAO trd = EasyMock.createMock(TrackingDAO.class);
			trd.storePosition(devdata, pos);
			EasyMock.expectLastCall().andThrow(new DeviceNotFoundException());
			EasyMock.replay(trd);
			((TrackingServiceImpl) trs).setTrackingDAO(trd);

			trs.storePosition(devdata, pos);
		} catch (uni.miskolc.ips.ilona.tracking.service.exceptions.DeviceNotFoundException e) {
			return;
		}
		Assert.fail();
	}

	@Test
	public void testGetPositions() {
		try {
			TrackingService trs = new TrackingServiceImpl();
			DeviceData devdata = EasyMock.createMock(DeviceData.class);
			EasyMock.expect(devdata.getDeviceid()).andReturn("devid");
			EasyMock.replay(devdata);
			Date date = new Date();
			TrackingDAO trd = EasyMock.createMock(TrackingDAO.class);
			EasyMock.expect(trd.restorePositionsInterval(devdata, date, date))
					.andReturn(new ArrayList<TrackPosition>());
			EasyMock.replay(trd);
			((TrackingServiceImpl) trs).setTrackingDAO(trd);
			trs.getPositions(devdata, date, date);
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test
	public void testGetPositionsDeviceNotFound() {
		try {
			TrackingService trs = new TrackingServiceImpl();
			DeviceData devdata = EasyMock.createMock(DeviceData.class);
			EasyMock.expect(devdata.getDeviceid()).andReturn("devid");
			EasyMock.replay(devdata);
			Date date = new Date();
			TrackingDAO trd = EasyMock.createMock(TrackingDAO.class);
			EasyMock.expect(trd.restorePositionsInterval(devdata, date, date)).andThrow(new DeviceNotFoundException());
			EasyMock.replay(trd);
			((TrackingServiceImpl) trs).setTrackingDAO(trd);
			trs.getPositions(devdata, date, date);
		} catch (uni.miskolc.ips.ilona.tracking.service.exceptions.DeviceNotFoundException e) {
			return;
		} catch (Exception e) {
			Assert.fail();
		}
		Assert.fail();
	}
	

	@Test
	public void testGetPositionsGeneralError() {
		try {
			TrackingService trs = new TrackingServiceImpl();
			DeviceData devdata = EasyMock.createMock(DeviceData.class);
			EasyMock.expect(devdata.getDeviceid()).andReturn("devid");
			EasyMock.replay(devdata);
			Date date = new Date();
			TrackingDAO trd = EasyMock.createMock(TrackingDAO.class);
			EasyMock.expect(trd.restorePositionsInterval(devdata, date, date)).andThrow(new OperationExecutionErrorException());
			EasyMock.replay(trd);
			((TrackingServiceImpl) trs).setTrackingDAO(trd);
			trs.getPositions(devdata, date, date);
		} catch (ServiceGeneralErrorException e) {
			return;
		} catch (Exception e) {
			Assert.fail();
		}
		Assert.fail();
	}
}
