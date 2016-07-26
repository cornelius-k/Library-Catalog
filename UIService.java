package Final;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import Final.Final;
/*
 * this class contains only static methods intended to be used for building and working with UI components
 * 
 */
public class UIService {

	//appends message to main TextArea
	public static void appendMessage(String message){
		Final.mainPane.textArea.appendText(message + '\n');
	}
	
	//standard validation used application-wide for a user-entered value that will be used in a query
	public static boolean isValidSearch(String id){
		boolean isValid = true;
		String regex = "[0-9]+"; //allow only numbers
		if(id == null || id.equals("") || !id.matches(regex)){isValid = false;} 
		return isValid;
	}
	
	
	//generically designed method for iterating over a map of properties in a TableModel instance, 
	//this function will return a new grid pane containing each property's key and a text field object
	//which is automatically bound to the data model object's observable property value
	public static GridPane buildGrid(TableModel model){
		GridPane grid = new GridPane();
		int i = 0;
		for(String propName : model.properties.keySet()){
			grid.add(new Label(propName), 0, i);
			TextField textField = new TextField();
			textField.textProperty().bindBidirectional(model.properties.get(propName));
			grid.add(textField, 1, i);
			i++;
		}
		Button search = new Button("Search by " + model.searchKey);
		search.setOnAction((e) -> {
			try {
				String resultText = model.search();
				appendMessage(resultText); 
			} catch (Exception e1) {
				model.clear();
				e1.printStackTrace();
			}
		});
		grid.add(search, 0, i);
		return grid;
	}
	
}
