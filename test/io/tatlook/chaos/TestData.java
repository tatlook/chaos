/**
 * 
 */
package io.tatlook.chaos;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * @author Administrator
 *
 */
class TestData {

	@Test
	void testEmptyData() {
		ChaosData data = new ChaosData();
		assertNotEquals(null, data.getDistVector());
		assertNotEquals(null, data.getCXVector());
		assertNotEquals(null, data.getCYVector());
		assertFalse(data.getDistVector().isEmpty());
		assertFalse(data.getCXVector().isEmpty());
		assertFalse(data.getCYVector().isEmpty());
		
		assertNotEquals(ChaosData.current, data);
	}

	@Test
	void testFullEmptyData() {
		ChaosData data = new ChaosData(new double[0], new double[0][0], new double[0][0]);
		assertNotEquals(null, data.getDistVector());
		assertNotEquals(null, data.getCXVector());
		assertNotEquals(null, data.getCYVector());
		assertTrue(data.getDistVector().isEmpty());
		assertTrue(data.getCXVector().isEmpty());
		assertTrue(data.getCYVector().isEmpty());
		
		assertNotEquals(ChaosData.current, data);
	}

	@Test
	void testErrorConstruct() {
		try {
			new ChaosData(new double[0], new double[3][0], new double[9][2]);
			fail("no error");
		} catch (AssertionError e) {
		}
	}

}
