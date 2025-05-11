package coiipa;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import util.Database;


/**
 * Testing for business process "Consult Activity Status".
 * Done with {@code JUnit 5}
 */
public class CloseActivityTest {
	
	private static final String TEST_DB_PATH = "src/test/resources/CloseActivityTestDB.sql";
	
	private static Database db=new Database();
	
	@BeforeEach
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
	
	@AfterAll
	public void tearDown()
	{
		db.createDatabase(true);
		db.loadDatabase();
	}
}
