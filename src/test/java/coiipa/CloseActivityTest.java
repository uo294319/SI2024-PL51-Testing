package coiipa;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.jupiter.api.*;

import DTOs.ActivitiesDTO;
import model.ActivitiesModel;
import util.ApplicationException;
import util.Database;
import util.SwingMain;


/**
 * Testing for business process "Consult Activity Status".
 * Done with {@code JUnit 5}
 */
public class CloseActivityTest {
	
	private static final String TEST_DB_PATH = "src/test/resources/CloseActivityTestDB.sql";
	
	private static Database db = new Database();
	private static ActivitiesModel model = new ActivitiesModel();
	
	/**
	 * Set today's date to a specific date.
	 * @param date to set as a string with "yyyy-MM-dd" format.
	 */
	private static void setDate(String date)
	{
		try {
			SwingMain.setTodayDate(new SimpleDateFormat("yyyy-MM-dd").parse(date));
		} catch (ParseException e) {
			throw new IllegalStateException("Error previous to testing when parsing date TEST_TODAY_DATE");
		}
	}
	
	@BeforeEach
	public void setUp()
	{
		// Initialize the database and load the test script
		db.createDatabase(false);
		db.executeScript(TEST_DB_PATH);
	}
	
	@Test
	public void TC10_Valid_1()
	{
		// Test Case 10: Valid activity closure with a future date
		String id = "2";
		CloseActivityTest.setDate("2024-01-15");
		
		// Check that it can be closed without errors.
		try {
			model.closeActivityById(id);
		} catch ( Exception e ) {
			fail("Exception during closeActivityById. Msg = " + e.getMessage());
		}
		
		// Check that it is actually closed.
		String sql = "SELECT * FROM Activities WHERE id = ?";
		ActivitiesDTO result = db.executeQueryPojo(ActivitiesDTO.class, sql, id).get(0);
		assertEquals("closed", result.getStatus());
	}
	
	@Test
	public void TC11_Valid_2()
	{
		// Test Case 11: Valid activity closure with today's date
		String id = "2";
		CloseActivityTest.setDate("2024-01-10");
		
		// Check that it can be closed without errors.
		try {
			model.closeActivityById(id);
		} catch ( Exception e ) {
			fail("Exception during closeActivityById. Msg = " + e.getMessage());
		}
		
		// Check that it is actually closed.
		String sql = "SELECT * FROM Activities WHERE id = ?";
		ActivitiesDTO result = db.executeQueryPojo(ActivitiesDTO.class, sql, id).get(0);
		assertEquals("closed", result.getStatus());
	}
	
	@Test
	public void TC12_NonExistingActivity()
	{
		// Test Case 12: Non-existing activity
		String id = "100";
		CloseActivityTest.setDate("2024-01-10");
		
        // Expect ApplicationException due to non-existing activity
		assertThrows(
			ApplicationException.class,
			() -> model.closeActivityById(id)
		);
	}
	
	@Test
	public void TC13_AlreadyClosedActivity()
	{
		// Test Case 13: Already closed activity
		String id = "1";
		CloseActivityTest.setDate("2024-01-10");
		
		// Expect ApplicationException due to already closed activity
		assertThrows(
			ApplicationException.class,
			() -> model.closeActivityById(id)
		);
	}
	
	@Test
	public void TC14_NonCelebratedActivity()
	{
		// Test Case 14: Non-celebrated activity
		String id = "2";
		CloseActivityTest.setDate("2023-01-15");
		
		// Expect ApplicationException due to non-celebrated activity
		assertThrows(
			ApplicationException.class,
			() -> model.closeActivityById(id)
		);
	}
	
	@Test
	public void TC15_NonPaidSponsorship()
	{
		// Test Case 15: Non-paid sponsorship
		String id = "3";
		CloseActivityTest.setDate("2023-01-15");
		
		// Expect ApplicationException due to non-paid sponsorship
		assertThrows(
			ApplicationException.class,
			() -> model.closeActivityById(id)
		);
	}
	
	@Test
	public void TC16_NonPaidOtherIncome()
	{
		// Test Case 16: Non-paid other income
		String id = "4";
		CloseActivityTest.setDate("2024-01-15");
		
		// Expect ApplicationException due to non-paid other income
		assertThrows(
			ApplicationException.class,
			() -> model.closeActivityById(id)
		);
	}
	
	@Test
	public void TC16_NonPaidExpenses()
	{
		// Test Case 16: Non-paid expenses
		String id = "5";
		CloseActivityTest.setDate("2025-01-15");
		
		// Expect ApplicationException due to non-paid expenses
		assertThrows(
			ApplicationException.class,
			() -> model.closeActivityById(id)
		);
	}
	
	@AfterAll
	public static void tearDownAfterAll()
	{
		// Clean up the database after all tests
		db.createDatabase(false);
		db.loadDatabase();
	}
}
