package Final;

import java.util.Arrays;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import Final.UIService;
/*
 * This class extends BorderPane class to easily define and instantiate a MainPane which builds all
 * UI elements needed for the main stage
 */
public class MainPane extends BorderPane {

	public static TextArea textArea = new TextArea();

	//build books lookup UI
	VBox leftVbox = new VBox();
	
	//build user lookup UI
	VBox middleVbox = new VBox();

	//build check in check out UI
	VBox rightVbox = new VBox();
	HBox uidHbox = new HBox();
	HBox bookidHbox = new HBox();
	Label uidLabel = new Label("User ID: ");
	Label bookIdLabel = new Label("Book ID: ");
	TextField uidText = new TextField();
	TextField bookIdText = new TextField();
	
	//create buttons
	Button connect = new Button("Connect to Database");
	Button checkoutBook = new Button("Check out Book");
	Button returnBook = new Button("Return Book");
	Button checkRentals = new Button("Check rentals by user id");
	Button checkStatus = new Button("Check status by book id");
	
	public MainPane(){
		textArea.setPrefRowCount(40);            
		UIService.appendMessage("Welcome to the library database control system");
		leftVbox.getChildren().addAll(0, Arrays.asList(UIService.buildGrid(Final.booksModel)));
		middleVbox.getChildren().addAll(0, Arrays.asList(UIService.buildGrid(Final.userModel)));
		uidHbox.getChildren().addAll(uidLabel, uidText);
		bookidHbox.getChildren().addAll(bookIdLabel, bookIdText);
		rightVbox.getChildren().addAll(uidHbox, bookidHbox);
		
		//add everything to the right side vbox
		rightVbox.getChildren().add(new HBox(checkoutBook));
		rightVbox.getChildren().add(new HBox(returnBook));
		rightVbox.getChildren().add(new HBox(checkRentals));
		rightVbox.getChildren().add(new HBox(checkStatus));

		
		//add vbox nodes to pane in respective places
		this.setLeft(leftVbox);
		this.setCenter(middleVbox);
		this.setRight(rightVbox);
		this.setBottom(textArea);
		
		//set up button actions
		wireButtonActions();

	}
	
	public void wireButtonActions(){
		//set actions for buttons
		connect.setOnAction((e) ->{
			Final.connectStage.show();
		});
		checkoutBook.setOnAction((e) -> {
			String uid = uidText.getText();
			String bookId = bookIdText.getText();
			if(UIService.isValidSearch(uid) && UIService.isValidSearch(bookId)){
				if(Book.isValidId(bookId) && User.isValidId(uid)){
					try {
						String result = BooksService.checkoutBook(uidText.getText(), bookIdText.getText());
						textArea.appendText(result + "\n");
						if(Final.booksModel.bookID == bookIdText.getText()){
						}
					} catch (Exception e1) {
						textArea.appendText("Error checking out book: " + e1.getMessage() +"\n");
						e1.printStackTrace();
					}
					
				}else{
					UIService.appendMessage("User ID or Book ID does not exit");
				}

			}else{
				UIService.appendMessage("Invalid User ID and Or Book ID");
			}
		});
		returnBook.setOnAction((e) -> {
			String uid = uidText.getText();
			String bookId = bookIdText.getText();
			if(UIService.isValidSearch(uid) && UIService.isValidSearch(bookId)){
				if(Book.isValidId(bookId) && User.isValidId(uid)){
					try {
						String result = BooksService.returnBook(uidText.getText(), bookIdText.getText());
						UIService.appendMessage(result);
						if(Final.booksModel.bookID == bookId){
							//here could be an
							//opurtunity to update ui to show book checkout change if correct methods implemented
						}
					} catch (Exception e1) {
						textArea.appendText("Error returning book: " + e1.getMessage() +"\n");
						e1.printStackTrace();
					}
				}else{
					UIService.appendMessage("User ID or Book ID does not exist");
				}

			}else{
				UIService.appendMessage("Invalid User ID and Or Book ID");
			}

		});
		checkRentals.setOnAction((e) -> {
			String id = uidText.getText();
			if(UIService.isValidSearch(id)){
				if(User.isValidId(id)){
					try {
						textArea.appendText(BooksService.checkRentals(id) + "\n");
					} catch (Exception e1) {
						textArea.appendText("Error checking rentals: " + e1.getMessage());
						e1.printStackTrace();
					}
				}else{
					UIService.appendMessage("Invalid User ID: user with this id does not exist");
				}

			}else{
				UIService.appendMessage("Invalid User ID: must be numeric and not empty");
			}

		});
		checkStatus.setOnAction((e) -> {
			String id = bookIdText.getText();
			if(UIService.isValidSearch(id)){
				if(Book.isValidId(id)){
					String[] resultsPair = new String[2];
					try {
						resultsPair = BooksService.getInventoryStatusForBookID(id);
						textArea.appendText("Book record id's for BookID "+id+" checked out: " + resultsPair[0] +"\n");
						textArea.appendText("Book record id's for BookID "+id+" checked in: " + resultsPair[1] +"\n");
					} catch (Exception e1) {
						textArea.appendText("Error getting inventory status of BookID "+id+":  "+ e1.getMessage()  +"\n");
						e1.printStackTrace();
					}
				}else{
					UIService.appendMessage("Invalid Book ID: book with this id does not exist");
				}

			}else{
				UIService.appendMessage("Invalid Book ID: must be numeric and not empty");
			}

		});	
	}
}
