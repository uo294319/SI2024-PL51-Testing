package coiipa;

import org.junit.*;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.Database;
import util.SwingMain;
import model.SponsorshipAgreementsModel; 
import util.ApplicationException;

/**
 * Testing for business process "Register Sponsorship".
 * Done with {@code JUnit 4}
 */
public class RegisterSponsorshipTest
{
	
	private static final String TEST_DB_PATH = "src/test/resources/RegisterSponsorshipTestDB.sql";
	private static final String TEST_TODAY_DATE = "2024-01-10";
	
	private static Database db=new Database();
	private static SponsorshipAgreementsModel model = new SponsorshipAgreementsModel();
	Map<String, String> data;
	
	@BeforeClass
	public static void setUpBeforeClass()
	{
		try {
			// Set the test today's date for all tests.
			SwingMain.setTodayDate(new SimpleDateFormat("yyyy-MM-dd").parse(TEST_TODAY_DATE));
		} catch (ParseException e) {
			throw new IllegalStateException("Error previous to testing when parsing date TEST_TODAY_DATE");
		}
	}
	
	@Before
	public void setUp() 
	{
		// Initialize the database and load the test script.
		db.createDatabase(false);
		db.executeScript(TEST_DB_PATH);
		
		// Reset the dictionary (map) with data for each test case.
		data = new HashMap<>();
	}
	
	@Test
	public void TC01_Valid_1()
	{
		// Test Case TC01 data: Valid sponsorship agreement with future date
		data.put("idActivity", "2");
		data.put("idSponsorContact", "1");
		data.put("idGBMember", "1");
		data.put("date", "2024-01-01");
		data.put("amount", "3000.00");
				
		// Check that it can be inserted in the DB without errors.
		try {
			model.insertNewSponsorshipAgreement(
				data.get("idSponsorContact"),
				data.get("idGBMember"),
				data.get("idActivity"),
				data.get("amount"),
				data.get("date")
			);
		} catch ( Exception e ) {
			fail("Exception during insertNewSponsorshipAgreement. Msg = " + e.getMessage());
		}
		
		// Verify the data is correctly inserted into the database
		String sql = "SELECT idSponsorContact, idGBMember, idActivity, amount, date "
				+ "FROM SponsorshipAgreements WHERE idSponsorContact = ? AND idGBMember = ? "
				+ "AND idActivity = ? AND amount = ? AND date = ?";
		
		List<Object[]> result = db.executeQueryArray(
			sql,
			data.get("idSponsorContact"),
			data.get("idGBMember"),
			data.get("idActivity"),
			data.get("amount"),
			data.get("date")
		);
		
		if (result == null) throw new IllegalStateException("Error obtaining data from the DB in TC01.");
	    assertEquals("Should return exactly one row", 1, result.size());
	    
	    // Check the values match the expected
	    assertEquals("idSponsorContact should match", data.get("idSponsorContact"), result.get(0)[0].toString());
	    assertEquals("idGBMember should match", data.get("idGBMember"), result.get(0)[1].toString());
	    assertEquals("idActivity should match", data.get("idActivity"), result.get(0)[2].toString());
	    assertEquals("amount should match", Double.parseDouble(data.get("amount")), result.get(0)[3]);
	    assertEquals("date should match", data.get("date"), result.get(0)[4].toString());
	}
	
	@Test
	public void TC02_Valid_2()
	{
		// Test Case TC02 data: Valid sponsorship agreement with today's date
		data.put("idActivity", "2");
		data.put("idSponsorContact", "1");
		data.put("idGBMember", "1");
		data.put("date", TEST_TODAY_DATE);
		data.put("amount", "4000.00");
				
		// Check that it can be inserted in the DB without errors.
		try {
			model.insertNewSponsorshipAgreement(
				data.get("idSponsorContact"),
				data.get("idGBMember"),
				data.get("idActivity"),
				data.get("amount"),
				data.get("date")
			);
		} catch ( Exception e ) {
			fail("Exception during insertNewSponsorshipAgreement. Msg = " + e.getMessage());
		}
		
		// Verify the data is correctly inserted into the database
		String sql = "SELECT idSponsorContact, idGBMember, idActivity, amount, date "
				+ "FROM SponsorshipAgreements WHERE idSponsorContact = ? AND idGBMember = ? "
				+ "AND idActivity = ? AND amount = ? AND date = ?";
		
		List<Object[]> result = db.executeQueryArray(
			sql,
			data.get("idSponsorContact"),
			data.get("idGBMember"),
			data.get("idActivity"),
			data.get("amount"),
			data.get("date")
		);
		
		if (result == null) throw new IllegalStateException("Error obtaining data from the DB in TC02.");
	    assertEquals("Should return exactly one row", 1, result.size());
	    
	    // Check the values match the expected
	    assertEquals("idSponsorContact should match", data.get("idSponsorContact"), result.get(0)[0].toString());
	    assertEquals("idGBMember should match", data.get("idGBMember"), result.get(0)[1].toString());
	    assertEquals("idActivity should match", data.get("idActivity"), result.get(0)[2].toString());
	    assertEquals("amount should match", Double.parseDouble(data.get("amount")), result.get(0)[3]);
	    assertEquals("date should match", data.get("date"), result.get(0)[4].toString());
	}
	
	@Test
	public void TC03_ClosedActivity()
	{
		// Test Case TC03 data: Attempt to register sponsorship for a closed activity
		data.put("idActivity", "1");
		data.put("idSponsorContact", "1");
		data.put("idGBMember", "1");
		data.put("date", "2024-01-01");
		data.put("amount", "3000.00");
		
		
		// Expect ApplicationException due to closed activity
		assertThrows(
			ApplicationException.class,
			() -> model.insertNewSponsorshipAgreement(
				data.get("idSponsorContact"),
				data.get("idGBMember"),
				data.get("idActivity"),
				data.get("amount"),
				data.get("date")
			)
		);
	}
	
	@Test
	public void TC04_UnexistantActivity()
	{
		// Test Case TC04 data: Attempt to register sponsorship for an unexisting activity
		data.put("idActivity", "3");
		data.put("idSponsorContact", "1");
		data.put("idGBMember", "1");
		data.put("date", "2024-01-01");
		data.put("amount", "3000.00");
		
		// Expect ApplicationException due to unexisting activity
		assertThrows(
			ApplicationException.class,
			() -> model.insertNewSponsorshipAgreement(
				data.get("idSponsorContact"),
				data.get("idGBMember"),
				data.get("idActivity"),
				data.get("amount"),
				data.get("date")
			)
		);
	}
	
	@Test
	public void TC05_AlreadySponsoringContact()
	{
		// Test Case TC05 data: Attempt to register sponsorship for an already sponsoring contact
		data.put("idActivity", "2");
		data.put("idSponsorContact", "2");
		data.put("idGBMember", "1");
		data.put("date", "2024-01-01");
		data.put("amount", "3000.00");
		
		// Expect ApplicationException due to already sponsoring contact
		assertThrows(
			ApplicationException.class,
			() -> model.insertNewSponsorshipAgreement(
				data.get("idSponsorContact"),
				data.get("idGBMember"),
				data.get("idActivity"),
				data.get("amount"),
				data.get("date")
			)
		);
	}
	
	@Test
	public void TC06_UnexistantContact()
	{
		// Test Case TC06 data: Attempt to register sponsorship for an unexisting contact
		data.put("idActivity", "2");
		data.put("idSponsorContact", "3");
		data.put("idGBMember", "1");
		data.put("date", "2024-01-01");
		data.put("amount", "3000.00");
		
		// Expect ApplicationException due to unexisting contact
		assertThrows(
			ApplicationException.class,
			() -> model.insertNewSponsorshipAgreement(
				data.get("idSponsorContact"),
				data.get("idGBMember"),
				data.get("idActivity"),
				data.get("amount"),
				data.get("date")
			)
		);
	}
	
	@Test
	public void TC07_UnexistantGBMember()
	{
		// Test Case TC07 data: Attempt to register sponsorship for an unexisting GB member
		data.put("idActivity", "2");
		data.put("idSponsorContact", "1");
		data.put("idGBMember", "2");
		data.put("date", "2024-01-01");
		data.put("amount", "3000.00");
		
		// Expect ApplicationException due to unexisting GB member
		assertThrows(
			ApplicationException.class,
			() -> model.insertNewSponsorshipAgreement(
				data.get("idSponsorContact"),
				data.get("idGBMember"),
				data.get("idActivity"),
				data.get("amount"),
				data.get("date")
			)
		);
	}
	
	@Test
	public void TC08_FutureAgreementDate()
	{
		// Test Case TC08 data: Attempt to register sponsorship with a future date
		data.put("idActivity", "2");
		data.put("idSponsorContact", "1");
		data.put("idGBMember", "1");
		data.put("date", "2024-01-11");
		data.put("amount", "3000.00");
		
		// Expect ApplicationException due to future date
		assertThrows(
			ApplicationException.class,
			() -> model.insertNewSponsorshipAgreement(
				data.get("idSponsorContact"),
				data.get("idGBMember"),
				data.get("idActivity"),
				data.get("amount"),
				data.get("date")
			)
		);
	}
	
	@Test
	public void TC09_AmountBelowLowestLevel()
	{
		// Test Case TC09 data: Attempt to register sponsorship with an amount below the lowest level
		data.put("idActivity", "2");
		data.put("idSponsorContact", "1");
		data.put("idGBMember", "1");
		data.put("date", "2024-01-01");
		data.put("amount", "2000.00");
		
		// Expect ApplicationException due to amount below the lowest level
		assertThrows(
			ApplicationException.class,
			() -> model.insertNewSponsorshipAgreement(
				data.get("idSponsorContact"),
				data.get("idGBMember"),
				data.get("idActivity"),
				data.get("amount"),
				data.get("date")
			)
		);
	}
	
	@AfterClass
	public static void tearDownAfterClass()
	{
		// Reset the database after all tests
		db.createDatabase(false);
		db.loadDatabase();
	}
}
