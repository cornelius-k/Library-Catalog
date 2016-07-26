package Final;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

import javafx.beans.property.SimpleStringProperty;

/*
 * This is an abstract super class from which the data model classes inherit, it implements TableInterface
 * 
 * static and instance level variables and methods that are needed by TableModel subclasses are declared and defined here
 * 
 */

public abstract class TableModel implements TableInterface {
	//a map used for defining property names and observable SimpleStringProperties
	//this allows a single function to iterate over the collection and create automatically bound labels and text boxes
	//for the ui
	Map<String, SimpleStringProperty> properties = new LinkedHashMap<String, SimpleStringProperty>();
	
	//a string that can be used to determine which key to execute search queries with
	String searchKey;
	
	//strings useful for building sql queries
	public static String modelName;
	public static String primaryKey;
	
	//a method to reset all values inside the properties map to an empty string
	public void clear(){
		for(String k : this.properties.keySet()){
			this.properties.get(k).set("");
		}
	}
	
	//a shortcut to check if search key meets validation requirements for database queries
	public boolean searchValid(){
		return UIService.isValidSearch(getId());
	}
	
	//gets search key value from
	public String getId(){
		return this.properties.get(this.searchKey).getValue();

	}
	
	//checks database for a record with a given id, designed generically so that subclass data models can make use of it
	//without changing or overriding implementation
	public static boolean isValidId(String id){
		boolean isValid = true;
		Statement search;
		try {
			//use global connection object to execute statement using instance var values when needed for sql query
			search = Final.connection.createStatement();
			ResultSet rs = search.executeQuery("select * from " + modelName + " where "+primaryKey+"='"+id+"'");
			if(!rs.next()){
				//there are no rows in the result set therefore this id is not valid
				isValid = false;
			}
		} catch (SQLException e) {
			//print error
			UIService.appendMessage("Error checking " + modelName + " " + primaryKey);
			e.printStackTrace();
		}
		return isValid;
	}
}
