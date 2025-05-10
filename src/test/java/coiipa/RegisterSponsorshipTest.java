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
public class RegisterSponsorshipTest {
	
	private static final String TEST_DB_PATH = "src/test/resources/RegisterSponsorshipTestDB.sql";
	private static final String TEST_TODAY_DATE = "2024-01-10";
	
	private static Database db=new Database();
	private static SponsorshipAgreementsModel model = new SponsorshipAgreementsModel();
	
	@Before
	public void setUp() 
	{
		db.createDatabase(false);
		db.executeScript(TEST_DB_PATH);
		
		try {
			SwingMain.setTodayDate(new SimpleDateFormat("yyyy-MM-dd").parse(TEST_TODAY_DATE));
		} catch (ParseException e) {
			throw new IllegalStateException("Error previous to testing when parsing date TEST_TODAY_DATE");
		}
	}
	
	@Test
	public void TC01_Valid_1()
	{
		// Preparing data
		Map<String, String> data = new HashMap<>();
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
		
		// Check that it is actually inserted in the DB.
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
		// Preparing data
		Map<String, String> data = new HashMap<>();
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
		
		// Check that it is actually inserted in the DB.
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
	public void TC03_ClosedActivity()
	{
		// Preparing data
		Map<String, String> data = new HashMap<>();
		data.put("idActivity", "1");
		data.put("idSponsorContact", "1");
		data.put("idGBMember", "1");
		data.put("date", "2024-01-01");
		data.put("amount", "3000.00");
		
		
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
		// Preparing data
		Map<String, String> data = new HashMap<>();
		data.put("idActivity", "3");
		data.put("idSponsorContact", "1");
		data.put("idGBMember", "1");
		data.put("date", "2024-01-01");
		data.put("amount", "3000.00");
		
		
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
		// Preparing data
		Map<String, String> data = new HashMap<>();
		data.put("idActivity", "2");
		data.put("idSponsorContact", "2");
		data.put("idGBMember", "1");
		data.put("date", "2024-01-01");
		data.put("amount", "3000.00");
		
		
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
		// Preparing data
		Map<String, String> data = new HashMap<>();
		data.put("idActivity", "2");
		data.put("idSponsorContact", "3");
		data.put("idGBMember", "1");
		data.put("date", "2024-01-01");
		data.put("amount", "3000.00");
		
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
		// Preparing data
		Map<String, String> data = new HashMap<>();
		data.put("idActivity", "2");
		data.put("idSponsorContact", "1");
		data.put("idGBMember", "2");
		data.put("date", "2024-01-01");
		data.put("amount", "3000.00");
		
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
		// Preparing data
		Map<String, String> data = new HashMap<>();
		data.put("idActivity", "2");
		data.put("idSponsorContact", "1");
		data.put("idGBMember", "1");
		data.put("date", "2024-01-11");
		data.put("amount", "3000.00");
		
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
		// Preparing data
		Map<String, String> data = new HashMap<>();
		data.put("idActivity", "2");
		data.put("idSponsorContact", "1");
		data.put("idGBMember", "1");
		data.put("date", "2024-01-01");
		data.put("amount", "2000.00");
		
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
}
