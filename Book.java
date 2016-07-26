package Final;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Final.BooksService;

import Final.TableModel;
import javafx.beans.property.SimpleStringProperty;
/*
 * This class is a data model that works mostly with the Book table
 * and holds some data from the Books table, such as checked in and checked out books
 * 
 * it implements a search method as required by the TableInterface which it's abstract super 
 * class TableModel implements
 */
public class Book extends TableModel {
	
	public String bookID = "";

	//constructor
	public Book(){
		//reference superclass TableModel for documentation on these instance vars
		this.properties.put("ISBN", new SimpleStringProperty(""));
		this.properties.put("author", new SimpleStringProperty(""));
		this.properties.put("title", new SimpleStringProperty(""));
		this.properties.put("checkedOut", new SimpleStringProperty(""));
		this.properties.put("checkedIn", new SimpleStringProperty(""));
		this.searchKey = "ISBN";
		this.primaryKey = "BookID";
		this.modelName = "Book";
	
	}
	
	//update the observable properties which the UI reflects with new data
	public void updateInventoryProperties(String[] resultPair){
		this.properties.get("checkedIn").set(resultPair[0]);
		this.properties.get("checkedOut").set(resultPair[1]);
	}
	
	
	//implementation required by interface, search by ISBN
	public String search() throws SQLException{
		String resultText;
		String id = getId();

		//validate ISBN field before attempting query
		if(searchValid()){

			//create a new statement using global connection object and query for book information
			Statement search = Final.connection.createStatement();
			ResultSet rs = search.executeQuery("select BookID, ISBN, author, title from Book where ISBN='" + id +"'");

			//only one row expected in result set, use rs.first to move cursor to first row simultaneusly checking that result set is not empty
			if(rs.first()){
				
				//parse result set into properties which will automatically update UI due to binding
				this.properties.get("ISBN").set(rs.getString("ISBN"));
				this.properties.get("author").set(rs.getString("author"));
				this.properties.get("title").set(rs.getString("title"));
				
				this.bookID = rs.getString("BookID");

				//look up inventory
				String[] resultPair = BooksService.getInventoryStatusForBookID(this.bookID);
				
				//update ui
				updateInventoryProperties(resultPair);
				resultText = "Searched by ISBN";
				
			}else{
				//result set was empty
				resultText = "There are no books with that ISBN";
			}

		}else{
			//string validation failed
			resultText = "Invalid or blank ISBN";
		}
		return resultText;
	}
	


}
