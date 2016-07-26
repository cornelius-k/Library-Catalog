/** This is a class for the Final Exam Project Application
 * 
 * It sets global variables and constants, and instantiates
 * static references to data model objects that work directly with a database
 * and through services
 * 
 * This class also creates instances of pane objects that will be used for the
 * two windows this application uses, as well as one utility method of connecting to the database.
 * 
 * @author Neil Kempin 
 * Course: CMSC-214 
 * Project Final
 * Written in Eclipse on Mac OS for Java 1.8.0
 *
 */
package Final;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.LinkedList;

public class Final extends Application{
	
	//global db connection object
	public static Connection connection;
	
	//constants
	public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	public static final String DB_URL = "jdbc:mysql://localhost/bookslibrary";
	public static final String DEFAULT_PASS = "tiger";
	public static final String DEFAULT_USER = "scott";
	
	//data model objects
	public static Book booksModel = new Book();
	public static User userModel = new User();
	
	//create static references to new instances of custom pane classes
	public static MainPane mainPane = new MainPane();
	public static ConnectPane connectPane = new ConnectPane();
	public static Stage connectStage = new Stage();

	//start javafx application
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		//set up main stage
		Scene mainScene = new Scene(mainPane, 900, 600);
		primaryStage.setScene(mainScene);

		//set up connect stage
		Scene connectScene = new Scene(connectPane);
		connectStage.setScene(connectScene);
		
		//show both stages
		primaryStage.show();
		connectStage.show();
		
	}
	

	//attempts to connect to db and returns status
	public static String getConnected(String driver, String db, String user, String pass){
		String status;
		//application only supports one driver
		if(!driver.equals(JDBC_DRIVER)){
			status = "Error, driver " + driver + " not supported";
		}else{
			//attempt connection and update status
			try {
				connection = DriverManager.getConnection(db, user, pass);
				status = "Connected";
			} catch (SQLException e) {
				status = "Connection failed: " + e.getMessage();
				e.printStackTrace();
			}
		}
		return status;
	}
	
	//reads data from file and returns contents as string
	public static String getTextInFile(String fileName) throws IOException{
		byte[] encoded = Files.readAllBytes(Paths.get(fileName));
		return new String(encoded, "UTF-8");
	}
	
	//populate database by creating tables and inserting seed data
	public static void createTables() throws SQLException{
		//get a sequence of file names into a linked list for order preserved iteration
		String strQueries = "createbook,createbooks,createuser,populatebooks,populatebook,populateuser";
		LinkedList<String> queries = new LinkedList<String>();
		queries.addAll(Arrays.asList(strQueries.split(",")));
		
		//iterate over each string creating a new Statement object and executing sql
		for(String s : queries){
			Statement create = Final.connection.createStatement();		
			try {
				String sqlString = getTextInFile(s+".sql");
				create.executeUpdate(sqlString);
				UIService.appendMessage("Ran query for " + s);
			} catch (IOException e) {
				UIService.appendMessage("Error loading sql file for " + s );
				e.printStackTrace();
			}	
		}
	}
	
	//required by IDE
	public static void main(String[] args){
		Application.launch(args);
	}
}
