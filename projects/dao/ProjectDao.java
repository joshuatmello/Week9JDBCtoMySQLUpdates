package projects.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import projects.entity.Project;
import projects.exception.DbException;
import provided.util.DaoBase;

public class ProjectDao extends DaoBase {
	
	//This is my DAO data layer. Reads and writes to the MySQL database. 
	
	static final private String CATEGORY_TABLE = "category";
	static final private String MATERIAL_TABLE = "material";
	static final private String PROJECT_TABLE = "project";
	static final private String PROJECT_CATEGORY_TABLE = "project_category";
	static final private String STEP_TABLE = "step";
	
	
	/**
	 * This method will save the project details. First a SQL statement is made.
	 * Then a Connection is obtained and transaction started. A PreparedStatement
	 * is needed since we used user input, and it sets the parameter values for
	 * the Project object. Data is saved, transaction committed. 
	 */
	public Project insertProject(Project project) {
	
	//writes the SQL statement that takes and inserts the values from project
	//from the insertProject() method. Uses ?'s as place holders for values. 
		
	// @formatter:off
	String sql = ""
			+ "INSERT INTO " + PROJECT_TABLE + " "
			+ "(project_name, estimated_hours, actual_hours, difficulty, notes) "
			+ "VALUES "
			+ "(?, ?, ?, ?, ?)";
	// @formatter:on
	
	//obtains connection, uses a try-with-resource statement
	try(Connection conn = DbConnection.getConnection()){
		
		//starts transaction, startTransaction() if from DaoBase class
		startTransaction(conn);
		
		//creates PreparedStatement, using method on Connection class 
		//called prepareStatement(). Uses a try-with-resource. 
		//Passes the SQL statement in as the parameter.
		try(PreparedStatement stmt = conn.prepareStatement(sql)){
			
			//sets Parameters, uses convenience method setParameter() from DaoBase
			setParameter(stmt, 1, project.getProjectName(), String.class);
			setParameter(stmt, 2, project.getEstimatedHours(), BigDecimal.class);
			setParameter(stmt, 3, project.getActualHours(), BigDecimal.class);
			setParameter(stmt, 4, project.getDifficulty(), Integer.class);
			setParameter(stmt, 5, project.getNotes(), String.class);
			
			//performs the insert. Do NOT pass in parameters to executeUpdate()
			//or it resets all parameters and gives an obscure error
			stmt.executeUpdate();
			
			//gets the project ID using convenience method from DaoBase
			//from passing in conn and PROJECT_TABLE. 
			Integer projectId= getLastInsertId(conn, PROJECT_TABLE);
			
			//commits the transaction, using DaoBase method 
			commitTransaction(conn);
			
			//saves the data
			project.setProjectId(projectId);	
			
			return project;
		}
		//catches the inner try block, rolls back transaction if error.
		catch(Exception e) {
			rollbackTransaction(conn);
			throw new DbException(e);			
		}
		}
	//Exception that exception from the try-with-resource 
	catch(SQLException e) {
		throw new DbException(e);
		}
		
	}

}
