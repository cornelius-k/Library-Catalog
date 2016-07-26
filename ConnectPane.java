package Final;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
/*
 * This class extends Grid Pane class to easily define and instantiate a ConnectPane which builds all
 * UI elements needed for the connect to database stage
 */
public class ConnectPane extends GridPane{

	ComboBox jdbcDrop = new ComboBox();
	ComboBox dbDrop = new ComboBox();
	TextField statusText = new TextField("Not Connected");
	TextField userText = new TextField(Final.DEFAULT_USER);
	TextField passText = new TextField(Final.DEFAULT_PASS);
	Button close = new Button("close");
	Button connect = new Button("Connect");
	Button createTables = new Button("Create Tables");
	

	ConnectPane(){

		//create grid pane
		this.setPadding(new Insets(5));
		this.setVgap(5);
		this.setHgap(5);

		//driver option
		this.add(new Label("JDBC Driver: "), 0, 0);
		jdbcDrop.getItems().addAll(Final.JDBC_DRIVER);
		jdbcDrop.setValue(Final.JDBC_DRIVER);
		this.add(jdbcDrop, 1, 0);

		//database option
		this.add(new Label("Database URL: "), 0, 1);
		dbDrop.getItems().addAll(Final.DB_URL);
		dbDrop.setValue(Final.DB_URL);
		this.add(dbDrop, 1, 1);

		//username textfield
		this.add(new Label("Username: "), 0, 2);
		this.add(userText, 1, 2);

		//password textfield
		this.add(new Label("Password: "), 0, 3);
		this.add(passText, 1, 3);

		//add the status text
		this.add(statusText, 0, 5);

		//add buttons
		this.add(connect, 0, 6);
		this.add(createTables, 1, 6);
		this.add(close, 2, 6);

		wireButtonActions();

	}
	
	public void wireButtonActions(){
		//buttons and actions
		close.setOnAction(e -> Final.connectStage.close());

		connect.setOnAction((e) -> {
			System.out.println(jdbcDrop.getValue().toString() + dbDrop.getValue().toString());
			//attempt to connect to db using given credentials, and update the status text with a string that describes the result
			statusText.setText(Final.getConnected(jdbcDrop.getValue().toString(), dbDrop.getValue().toString(), userText.getText(), passText.getText()));
			UIService.appendMessage("Connected");
		});
		createTables.setOnAction((e) -> {
			try {
				Final.createTables();
				statusText.setText("Created Tables");
			} catch (Exception e1) {
				statusText.setText("Error creating tables: " + e1.getMessage());
				e1.printStackTrace();
			}
		});
	}

}
