# Advanced Java Final Poject — Library Catalog System
Written for Java 1.8.0 and Java FX UI Library, mysql database also required to be installed and configured. Check `Final.java` for hardcoded creds.

(Required UML, Algorithm and User Guide)

### UML Diagram

![Alt text](screenshots/UML_Diagram.png?raw=true "UML")




### Algorithm:

- Create Interface and Super class TableModel which implements methods and variables that will be shared between it’s subclasses

- Create subclasses of TabelModel Book and User which each contain a map of property names and bindable properties for UI interaction, these classes serve to store data from similarly structured tables in the DB and do some interaction with the database
- Implement Search method uniquely in both Book and User data model
- Create BookService class which is not meant to be instantiated, and defines several static methods to preform database operations given parameters related to books Such as checking in and out a book, checking the status of a book, translating a book’s record id into it’s BookID for use with the Book database table, etc..

- Create a UI service class which is also not meant to be instantiated which defines several static methods that simplify working with UI elements

- Create a MainPane and ConnectPane class which extend Pane classes and define all UI elements for the respective panes, using UIService and BookService when needed such as for button actions

- Create static instances of model classes and panes, as well as a static connection object for database operations in “Final” class which extends Application and implements a start method for running a JavaFX application. Because these panes and models are statically defined in the Final class they and their methods and properties are accessible throughout the application.  Although the model and pane objects are globally available, their data is typically controlled via instance methods which provide a degree of encapsulation, which makes the code easier to work with in several ways including writing tests.

- Create separate sql files which are loaded and executed by functions in the Final class to build and populate database with required schema and test data. 

- Set both the MainPane and ConnectPane into scenes and stages, and show both stages, with the connect pane shown last so that the user sees it first

### Usage Guide including Screenshots
Launch the application via terminal or an IDE
![Alt text](screenshots/1.png?raw=true "1")

- Select a database driver to use, only one is available however and it is prepopulated
- Select a database URL, again only one is available and it is prepopulated
- Use given username and password, or change them if you have another valid user which will have access to the tables
- Click Connect to connect to the database, text box indicating status will update to say “Connected” or any errors should they occur
  ![Alt text](screenshots/2.png?raw=true "2")

- Populate Database with schema and data by clicking “Create Tables”, status text box will update to indicate tables have been created, or any errors should they occur. Note: clicking create tables before connecting will result in an error, as well as creating tables more than once.
  ![Alt text](screenshots/3.png?raw=true "3")
- Click `close` to exit the Connection window.
- Enter a user id into the User ID text box and click “Search by user ID” to search for a user.  User IDs are integers, entering text that contains characters other than an integer, or entering a User ID that does not exist will result in an error response explaining this to the user, (not a mysql error propagated to the front end)
  ![Alt text](screenshots/4.png?raw=true "4")
- Find some books that have been checked out by checking user id 1’s rentals. Enter 1 into the User ID text box on the right side of the window and choose “Check rentals by user id”. This will show you information about the books which the User of that ID has checked out. The same validation rules apply to this search. (I have searched twice in this screenshot)
  ![Alt text](screenshots/5.png?raw=true "5")
- Copy an ISBN Number from the results to look up a book’s info and see what the library’s inventor is. Paste this number into the ISBN box and click search by isbn.
- Now we can see information about this particular book, and the inventory listing the book record Id’s in checked out and checked in lists.
  ![Alt text](screenshots/6.png?raw=true "6")
- Notice that all of the copies of this book have been checked out.  Lets try to check out book with record id number 1 by User number 2 by entering “2” into User id on the right, and “1” into book ID on the right and clicking check Check out book.
  ![Alt text](screenshots/7.png?raw=true "7")
- User number 2 is unable to check out this book because it is already checked out, by user number 1.  Lets return user number 1’s copy of book record id #1 by entering 1 into user id and 1 into book id on the right hand side panel and clicking “Return Book”
  ![Alt text](screenshots/8.png?raw=true "8")
- We see a message saying that the book has been returned, now lets search by the same ISBN to check the inventory. We see that book with record ID #1 is now checked in and available to be checked out by another user. Checking out book #1 will now be available to any user.
   ![Alt text](screenshots/9.png?raw=true "7")
- We can also check the status of a book ID record by choosing check status by book id.
  ![Alt text](screenshots/9.png?raw=true "9")
- Checking out a book that does not exist, or using a user that does not exist will not proceed and will respond stating so. Also a user who has not checked out a particular book, cannot return another user’s book.  The application will display a message stating so. 
