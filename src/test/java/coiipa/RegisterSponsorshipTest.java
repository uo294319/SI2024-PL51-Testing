package coiipa;

import org.junit.*;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import util.Database;

/**
 * Testing for business process "Register Sponsorship".
 * Done with {@code JUnit 4}
 */
public class RegisterSponsorshipTest {
	
	private static final String TEST_DB_PATH = "src/test/resources/RegisterSponsorshipTestDB.sql";
	
	private static Database db=new Database();
	
	@Before
	public void setUp()
	{
		db.createDatabase(true);
		db.executeScript(TEST_DB_PATH);
	}
	
	@Test
	public void TCXX_YYY()
	{
		assertNotNull(null);
	}
	
	@AfterClass
	public void tearDown()
	{
		db.createDatabase(true);
		db.loadDatabase();
	}
}
