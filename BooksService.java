package Final;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * This is a service style class that implements only static methods which work with Book model data to achieve book
 * related tasks
 * 
 */

public class BooksService {

	//returns an array with two strings, checked in and checked out book ids comma seperated
	public static String[] getInventoryStatusForBookID(String bookId) throws SQLException{
		Statement search = Final.connection.createStatement();
		//could be handled better with a key value pair / hash map
		String[] results = new String[2];
		results[0] = "";
		results[1] = "";
		//get inventory
		ResultSet rs = search.executeQuery("select id, CheckedOut from Books where BookID='"+ bookId +"'");
		//iterate over results, building strings
		while(rs.next()){
			if (rs.getString("CheckedOut") == null){
				//when value is null the book is not checked out, so add it's books table record id to a string
				results[0] = results[0] + rs.getString("id") + ", ";
			}else{
				//when value exists book is checked out, so add it's books table record id to a string
				results[1] = results[1] + rs.getString("id")  + ", ";
			}
		}
		return results;
	}
	
	//check out a book for a given user id and book record id
	public static String checkoutBook(String uid, String recordId) throws SQLException{
		String result;
		Statement checkInventory = Final.connection.createStatement();
		ResultSet rs = checkInventory.executeQuery("SELECT CheckedOut FROM bookslibrary.Books where id="+recordId);
		//move cursor to first position
		rs.next();
		if(rs.getString(1) == null){ //this book is not checked out by any user, so check out the book
			Statement update = Final.connection.createStatement();
			update.executeUpdate("UPDATE `bookslibrary`.`Books` SET `CheckedOut`='"+uid+"' WHERE `id`='"+recordId+"';");
			result = "Book with id " + recordId + " checked out by user with id " + uid;
		}
		else{
			//the book was checked out by a user, so it cannot be checked out
			result = "Book with id " + recordId + " is already checked out by this or another user";
		}
		
		return result;
		
	}
	
	//return a book for a given user and book id
	public static String returnBook(String uid, String recordId) throws SQLException{
		Statement checkInventory = Final.connection.createStatement();
		ResultSet rs = checkInventory.executeQuery("SELECT CheckedOut FROM bookslibrary.Books where id="+recordId);
		//move cursor to first position
		rs.next();
		String status;
		//ensure this book is checked out by the user who is trying to return it, before executing sql update
		if(rs.getString(1) != null && rs.getString(1).equals(uid)){ 
			Statement update = Final.connection.createStatement();
			update.executeUpdate("UPDATE `bookslibrary`.`Books` SET `CheckedOut`=null WHERE `id`='"+recordId+"';");
			status = "Book with record id " + recordId + " returned by user with id " + uid;
		}
		else{
			status = "Book with record id " + recordId + " does not exist or is not checked out by user with id " + uid + " so therefore it cannot be returned";
		}
		
		return status;

	}
	
	//look up checked-out information for a given user and print it out
	public static String checkRentals(String uid) throws SQLException{
		Statement checkRentals = Final.connection.createStatement();
		ResultSet rs = checkRentals.executeQuery("SELECT BookID, id FROM bookslibrary.Books where CheckedOut='"+uid + "'");
		String status = "User with id "+ uid +" has the following books checked out:\n" ;
		while(rs.next()){
			String bookID = rs.getString("BookID");
			String recordId = rs.getString("id");
			//get more information about this book, and add to status string to be returned
			ResultSet bookInfo = getBookInfo(bookID);
			status = status + "Title: " + bookInfo.getString("title") + "\n"
					+"ISBN: " + bookInfo.getString("ISBN") + "\n"
					+"BookID: " + bookInfo.getString("BookID") + "\n"
					+"Book record ID: " + recordId + "\n"
					+"-------------\n";
		}
		return status;
	}
	
	//get Book model information for a given book record id in the books table
	public static ResultSet getBookInfo(String bookID){
		ResultSet rs = null;
		try {
			Statement getName = Final.connection.createStatement();
			rs = getName.executeQuery("SELECT ISBN, title, BookID FROM bookslibrary.Book where BookID='"+bookID + "'");
			rs.next();
		} catch (SQLException e) {
			UIService.appendMessage("Error retrieving book name");
			e.printStackTrace();
		}
		return rs;
	}
}
