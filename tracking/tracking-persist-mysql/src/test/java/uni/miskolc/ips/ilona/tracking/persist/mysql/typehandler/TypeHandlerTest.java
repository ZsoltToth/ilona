package uni.miskolc.ips.ilona.tracking.persist.mysql.typehandler;

import java.sql.ResultSet;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import uni.miskolc.ips.ilona.tracking.persist.mysql.typehandlers.BooleanToFromTinyintTypeHandler;

public class TypeHandlerTest {

	private BooleanToFromTinyintTypeHandler handler = new BooleanToFromTinyintTypeHandler();
	private final String COLUMN_NAME = "column1";
	private final int COLUMN_INDEX = 1;

	@Test
	public void testGetNullableResultTrue() throws Exception {
		ResultSet result = EasyMock.createMock(ResultSet.class);
		EasyMock.expect(result.getInt(COLUMN_NAME)).andReturn(1).anyTimes();
		EasyMock.expect(result.getInt(COLUMN_INDEX)).andReturn(1).anyTimes();
		EasyMock.expect(result.wasNull()).andReturn(false).anyTimes();
		EasyMock.replay(result);
		if (!handler.getNullableResult(result, COLUMN_NAME)) {
			Assert.fail("The return value us not true!");
		}
	}

	@Test
	public void testGetNullableResultFalse() throws Exception {
		ResultSet result = EasyMock.createMock(ResultSet.class);
		EasyMock.expect(result.getInt(COLUMN_NAME)).andReturn(0).anyTimes();
		EasyMock.expect(result.getInt(COLUMN_INDEX)).andReturn(0).anyTimes();
		EasyMock.expect(result.wasNull()).andReturn(false).anyTimes();
		EasyMock.replay(result);
		if (handler.getNullableResult(result, COLUMN_NAME)) {
			Assert.fail("The return value us not true!");
		}
	}

	@Test
	public void testGetNullableResultNullValue() throws Exception {
		ResultSet result = EasyMock.createMock(ResultSet.class);
		EasyMock.expect(result.getInt(COLUMN_NAME)).andReturn(0).anyTimes();
		EasyMock.expect(result.getInt(COLUMN_INDEX)).andReturn(0).anyTimes();
		EasyMock.expect(result.wasNull()).andReturn(true).anyTimes();
		EasyMock.replay(result);
		if (handler.getNullableResult(result, COLUMN_NAME)) {
			Assert.fail("The return value us not true!");
		}
	}

	@Test
	public void testGetNullableResultInt() throws Exception {
		ResultSet result = EasyMock.createMock(ResultSet.class);
		EasyMock.expect(result.getInt(COLUMN_NAME)).andReturn(1).anyTimes();
		EasyMock.expect(result.getInt(COLUMN_INDEX)).andReturn(1).anyTimes();
		EasyMock.expect(result.wasNull()).andReturn(false).anyTimes();
		EasyMock.replay(result);
		if (!handler.getNullableResult(result, COLUMN_INDEX)) {
			Assert.fail("The return value us not true!");
		}
	}

	@Test
	public void testGetNullableResultIntFalse() throws Exception {
		ResultSet result = EasyMock.createMock(ResultSet.class);
		EasyMock.expect(result.getInt(COLUMN_NAME)).andReturn(0).anyTimes();
		EasyMock.expect(result.getInt(COLUMN_INDEX)).andReturn(0).anyTimes();
		EasyMock.expect(result.wasNull()).andReturn(false).anyTimes();
		EasyMock.replay(result);
		if (handler.getNullableResult(result, COLUMN_INDEX)) {
			Assert.fail("The return value us not true!");
		}
	}

	@Test
	public void testGetNullableResultIntNullValue() throws Exception {
		ResultSet result = EasyMock.createMock(ResultSet.class);
		EasyMock.expect(result.getInt(COLUMN_NAME)).andReturn(0).anyTimes();
		EasyMock.expect(result.getInt(COLUMN_INDEX)).andReturn(0).anyTimes();
		EasyMock.expect(result.wasNull()).andReturn(true).anyTimes();
		EasyMock.replay(result);
		if (handler.getNullableResult(result, COLUMN_INDEX)) {
			Assert.fail("The return value us not true!");
		}
	}
}
