package model;

import java.util.Collections;
import java.util.List;
import DTOs.ActivitiesDTO;
import util.Database;
import util.SemanticValidations;

public class ActivitiesModel {

	public static final String SQL_ACTIVITIES_FILTERED = "SELECT * FROM Activities "
			+ "WHERE name == ? "
			+ "AND edition == ?;";
	
	public static final String SQL_NUM_ACTIVITIES_FILTERED = "SELECT COUNT(id) FROM Activities "
			+ "WHERE name == ? "
			+ "AND edition == ?;";
	
    private Database db = new Database();

	// GETTERS
    public ActivitiesDTO getActivityByInvoice(String idInvoice) {
    	SemanticValidations.validateIdForTable(idInvoice, "Activities", "ERROR. Provided idInvoice for getActivityByInvoice does not exist.");
    	String sql = "SELECT a.* FROM Invoices i "
    			+ "JOIN SponsorshipAgreements sa ON i.idSponsorshipAgreement = sa.id "
    			+ "JOIN Activities a ON a.id = sa.idActivity "
    			+ "WHERE i.id = ?";
    	List<ActivitiesDTO> activities = db.executeQueryPojo(ActivitiesDTO.class, sql, idInvoice);
    	
    	return activities.get(0);
    }

    public List<ActivitiesDTO> getActivitiesFromCurrentYear(String startDate, String endDate) {
		String sql ="SELECT id, name, status, dateStart, dateEnd FROM Activities WHERE dateStart >= ? AND dateEnd <= ?;";
		return db.executeQueryPojo(ActivitiesDTO.class, sql, startDate, endDate);
	}

	public String getActivityId(String name) {
    	String sql = "SELECT a.id FROM Activities a WHERE a.name = ?;";
    	return db.executeQueryArray(sql, name).get(0)[0].toString();
    }

	public List<ActivitiesDTO> getAllActivities() {
		String sql = "SELECT * FROM Activities;";
	    return db.executeQueryPojo(ActivitiesDTO.class, sql);
	}

	// getListActivities()
    public List<Object[]> getActiveActivityListArray() {
    	String sql = "SELECT name || '-' || edition FROM Activities WHERE status IN ('planned', 'done');";
		return db.executeQueryArray(sql);
	}
    
    public List<ActivitiesDTO> getFilteredActivities(String startDate, String endDate, String status) {
    	SemanticValidations.validateStatus(status);
        String sql = "SELECT id, name, status, dateStart, dateEnd FROM Activities WHERE dateStart >= ? AND dateEnd <= ?";
        if (status == "all") {
        	sql += ";";
        	return db.executeQueryPojo(ActivitiesDTO.class, sql, startDate, endDate);
        }
        else {
        	sql += " AND status = ?;";
        	return db.executeQueryPojo(ActivitiesDTO.class, sql, startDate, endDate, status);
        }
    }

    public ActivitiesDTO getActivityByName(String nameActivity) {
		SemanticValidations.validateName(nameActivity);
		String sql = "SELECT * FROM Activities WHERE name == ?;";
		List<ActivitiesDTO> activities = db.executeQueryPojo(ActivitiesDTO.class, sql, nameActivity);
		return activities.get(0);
	}
    
    public ActivitiesDTO getActivityById(String idActivity) {
    	SemanticValidations.validateIdForTable(idActivity, "Activities", "ERROR. Provided idActivity for getActivityById does not exist.");
		String sql = "SELECT * FROM Activities WHERE id == ?;";
		List<ActivitiesDTO> activities = db.executeQueryPojo(ActivitiesDTO.class, sql, idActivity);
		return activities.get(0);
	}

    public List<ActivitiesDTO> getActivitiesbyStatus(String... status) {
	    String placeholders = String.join(",", Collections.nCopies(status.length, "?"));
	    String sql = "SELECT * FROM Activities WHERE status IN (" + placeholders + ")";
	    return db.executeQueryPojo(ActivitiesDTO.class, sql, (Object[]) status);
	}
    
    public List<ActivitiesDTO> getClosableActivities() {
	    String sql = "SELECT * FROM Activities"
	    		+ " WHERE status IN ('registered', 'planned', 'done')"
	    		+ " AND dateStart != '' AND dateEnd != '' AND place != ''";
	    return db.executeQueryPojo(ActivitiesDTO.class, sql);
    }
  
    public ActivitiesDTO getActivityByFilters(String name, String edition) {
    	SemanticValidations.validateName(name);
		SemanticValidations.validatePositiveNumberOrZero(edition, "It is not a valid number");
    	List<ActivitiesDTO> sol = db.executeQueryPojo(ActivitiesDTO.class, SQL_ACTIVITIES_FILTERED, name, edition);
		return sol.get(0);
	}
    
    public List<ActivitiesDTO> getActivitiesInDateRange(String startDate, String endDate) {
        // Validate date range
        SemanticValidations.validateDateAfterTo(endDate, startDate, false, "End Date must be after Start Date");

        String sql = "SELECT * FROM Activities " +
                     "WHERE ((dateStart BETWEEN ? AND ?) " +  // Starts in range
                     "OR (dateEnd BETWEEN ? AND ?) " +        // Ends in range
                     "OR (dateStart <= ? AND dateEnd >= ?)) " + // Spans entire range
                     "AND lower(status) = 'planned' " +       // Only planned activities
                     "ORDER BY dateStart";

        return db.executeQueryPojo(ActivitiesDTO.class, sql, startDate, endDate, startDate, endDate, startDate, startDate);
    }
    
    public int getNumberActivitiesByFilters(String name, String edition) {
    	SemanticValidations.validateName(name);
		SemanticValidations.validatePositiveNumberOrZero(edition, "It is not a valid number");
		List<Object[]> result = db.executeQueryArray(SQL_NUM_ACTIVITIES_FILTERED, name, edition);
		if (result == null || result.isEmpty()) {
			return 0;
		}
		return (int) result.get(0)[0];
	}

	// INSERTIONS
    
    public void insertNewActivity(String name, String edition, String dateStart, String dateEnd, String place) {
		SemanticValidations.validateName(name);
		SemanticValidations.validatePositiveNumberOrZero(edition, "It is not a valid number");
		if (!dateStart.isBlank()) {
			SemanticValidations.validateDateBeforeTo(dateStart, dateEnd, true, "Incompatible dates");
		}
		if (!dateEnd.isBlank()) {
			SemanticValidations.validateDateAfterTo(dateEnd, dateStart, true, "Incompatible dates");
		}
		if (!place.isBlank()) {
			SemanticValidations.validateName(place);
		}
		
		String sql = "INSERT INTO Activities"
				+ "(name, edition, status, dateStart, dateEnd, place) VALUES "
				+ "(?, ?, 'planned', ?, ?, ?)";
		db.executeUpdate(sql, name, edition, dateStart, dateEnd, place);
	}
    
    
    /**
     * Closes the specified activity.
     * 
     * @param idActivity the identifier of the activity to be closed; must exist in the {@code Activities} table
     *
     * @throws ApplicationException if the {@code idActivity} does not exist
     */
    public void closeActivityById(String idActivity) {
		SemanticValidations.validateIdForTable(idActivity, "Activities", "ERROR. Provided idActivity for closeActivityById does not exist.");
		
		String sql;
		
		sql = "UPDATE Activities SET status = 'closed' WHERE id = ?;";
		db.executeUpdate(sql, idActivity);
		
		//sql = "UPDATE Movements SET status = 'cancelled' WHERE idActivity = ? AND status = 'estimated';";
		//db.executeUpdate(sql, idActivity);
		
		sql = "UPDATE SponsorshipAgreements SET status = 'cancelled' WHERE idActivity = ? AND status = 'signed';";
		db.executeUpdate(sql, idActivity);
		
		sql = "UPDATE Invoices SET status = 'cancelled'"
				+ "WHERE idSponsorshipAgreement IN ("
				+ "    SELECT id "
				+ "    FROM SponsorshipAgreements "
				+ "    WHERE idActivity = ?"
				+ ") AND status = 'signed';";
		db.executeUpdate(sql, idActivity);
	}
    
    public void updateActivity(String id, String name, String edition, String status, String dateStart, String dateEnd, String place) {
    	SemanticValidations.validateName(name);
		SemanticValidations.validatePositiveNumberOrZero(edition, "It is not a valid number");
		if (!dateStart.isBlank()) {
			SemanticValidations.validateDateBeforeTo(dateStart, dateEnd, true, "Incompatible dates");
		}
		if (!dateEnd.isBlank()) {
			SemanticValidations.validateDateAfterTo(dateEnd, dateStart, true, "Incompatible dates");
		}
		if (!place.isBlank()) {
			SemanticValidations.validateName(place);
		}
				
		String sql = "UPDATE Activities SET name = ?, edition = ?, status = ?, dateStart = ?, dateEnd = ?, place = ? WHERE id = ?;";
		db.executeUpdate(sql, name, edition, status, dateStart, dateEnd, place, id);
	}
}
