package application;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;


public class SceneController {

    @FXML private Stage stage;
    @FXML private Scene scene;
    @FXML private Parent root;
    @FXML private TextField searchField;
    @FXML private TextField searchField2;

    @FXML private TableView searchTable;
    @FXML private TableColumn isbnCol;
    @FXML private TableColumn  titleCol;
    @FXML private TableColumn  authorCol;
    @FXML private TableColumn  statusCol;
    @FXML private TableColumn  avCol;

    @FXML private TableView searchTable2;
    @FXML private TableColumn  loanCol;
    @FXML private TableColumn  blisbnCol;
    @FXML private TableColumn  cardCol;
    @FXML private TableColumn  outCol;
    @FXML private TableColumn  dueCol;
    @FXML private TableColumn  inCol;
    @FXML private TableColumn  checkCol;

    @FXML private TableView finesTable;
    @FXML private TableColumn borrowerNameCol;
    @FXML private TableColumn bookTitleCol;
    @FXML private TableColumn daysLateCol;
    @FXML private TableColumn fineAmtCol;
    @FXML private TableColumn finePaidCol;

    @FXML private CheckBox paidFinesCheckbox;

    @FXML private Tab finesTab;
    
    @FXML private TextField FirstName;
    @FXML private TextField LastName;
    @FXML private TextField PhoneNumber;
    @FXML private TextField SSN;
    @FXML private TextField Address;
    @FXML private TextField City;
    @FXML private TextField State;

    static String message = "Testing";
    static Text resultingMessage = new Text();

    private ObservableList<SearchResult> data;
    private ObservableList<Object> allFines;
    private ObservableList<Object> unpaidFines;


    @FXML
    public void switchToScene1(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Scene1.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToScene2(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Scene2.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

@FXML
    public void checkInSearchAction(ActionEvent actionEvent) throws IOException, SQLException {
        scene = (Scene) ((Node) actionEvent.getSource()).getScene();
        // GET THE TEXT THE USER TYPED IN THE SEARCH BOX
        String searchTxt = searchField2.getText();
        System.out.println(searchTxt);
        CheckInSearch checkInSearch = new CheckInSearch(searchTxt);
        ResultSet rs = checkInSearch.searchData();

        loanCol.setCellValueFactory(new PropertyValueFactory<CheckInSearchResult, String>("LoanID"));
        blisbnCol.setCellValueFactory(new PropertyValueFactory<CheckInSearchResult, String>("Isbn"));
        cardCol.setCellValueFactory(new PropertyValueFactory<CheckInSearchResult, String>("CardID"));
        outCol.setCellValueFactory(new PropertyValueFactory<CheckInSearchResult, String>("DateOut"));
        dueCol.setCellValueFactory(new PropertyValueFactory<CheckInSearchResult, String>("DateDue"));
        inCol.setCellValueFactory(new PropertyValueFactory<CheckInSearchResult, String>("DateIn"));

        Callback<TableColumn<CheckInSearchResult, String>, TableCell<CheckInSearchResult,String>> cellFactory = new Callback<TableColumn<CheckInSearchResult, String>, TableCell<CheckInSearchResult, String>>() {
            @Override
            public TableCell<CheckInSearchResult, String> call(TableColumn<CheckInSearchResult, String> checkInSearchResultStringTableColumn) {
                final TableCell<CheckInSearchResult, String> cell = new TableCell<CheckInSearchResult, String>() {

                    final Button btn = new Button("Check In");

                    @Override
                    public void updateItem(String item, boolean empty) {
                        // CHECKS IF THE ROW IS EMPTY IF IT IS THE BUTTON WILL BE MISSING
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.setOnAction(event -> {
                                // GETTING THE CURRENT DATE
                                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Creates the format the date will be in
                                LocalDate myObj = LocalDate.now(); // Create a date object
                                String formattedDate = myObj.format(myFormatObj);
                                String currDate = formattedDate;

                                // GETTING THE ROW NUMBER OF THE BUTTON PRESSED
                                CheckInSearchResult cisr = getTableView().getItems().get(getIndex());
                                String idOfRow = cisr.getLoanID();

                                try {
                                    // THIS WILL CONNECT TO THE DATABASE
                                    Class.forName("com.mysql.cj.jdbc.Driver");
                                    Connection conn = DriverManager.getConnection(
                                            "jdbc:mysql://localhost:3306/library?useSSL=false","root","cs4347libraryproject2001");

                                    // WILL UPDATE THE ROW THE BUTTON WAS PRESSED WITH A DATE IN
                                    String q = "update book_loans set date_in = ? where loan_id = ?";
                                    PreparedStatement bookCheck = conn.prepareStatement(q);
                                    bookCheck.setString(1, currDate);
                                    bookCheck.setInt(2, Integer.parseInt(idOfRow));
                                    int result = bookCheck.executeUpdate();
                                    if (result == 0){
                                        System.out.println("Not Successful");
                                    }
                                    else
                                        System.out.println("Success");

                                    // UPDATES THE TABLE RIGHT AWAY
                                    ResultSet rs = checkInSearch.searchData();
                                    try {
                                        ObservableList<Object> data = FXCollections.observableArrayList();
                                        while (rs.next()) {
                                            String loan = rs.getString("Loan_ID");
                                            String isbn = rs.getString("ISBN");
                                            String card = rs.getString("Card_ID");
                                            String out = rs.getString("Date_Out");
                                            String due = rs.getString("Due_Date");
                                            String in = rs.getString("Date_In");
                                            CheckInSearchResult row = new CheckInSearchResult(loan, isbn, card, out, due, in);

                                            data.add(row);
                                        }
                                        searchTable2.setItems(data);
                                        searchTable2.setVisible(true);

                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                    conn.close();
                                }catch (Exception e) { System.out.println(e);}

                            });
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        };
        checkCol.setCellFactory(cellFactory);

        try {
            ObservableList<Object> data = FXCollections.observableArrayList();
            while (rs.next()) {
                String loan = rs.getString("Loan_ID");
                String isbn = rs.getString("ISBN");
                String card = rs.getString("Card_ID");
                String out = rs.getString("Date_Out");
                String due = rs.getString("Due_Date");
                String in = rs.getString("Date_In");
                CheckInSearchResult row = new CheckInSearchResult(loan, isbn, card, out, due, in);

                data.add(row);
            }
            searchTable2.setItems(data);
            searchTable2.setVisible(true);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void handleSearchAction(ActionEvent actionEvent) throws IOException, SQLException {
        scene = (Scene) ((Node) actionEvent.getSource()).getScene();
        String searchTxt = searchField.getText();
        System.out.println(searchTxt);
        Search search = new Search(searchTxt);
        ResultSet rs = search.searchData();

        isbnCol.setCellValueFactory(new PropertyValueFactory<SearchResult, String>("Isbn"));
        titleCol.setCellValueFactory(new PropertyValueFactory<SearchResult, String>("Title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<SearchResult, String>("Author"));
        statusCol.setCellValueFactory(new PropertyValueFactory<SearchResult, String>("Status"));
        
        // Getting the button row set up
        Callback<TableColumn<SearchResult, String>, TableCell<SearchResult, String>> cellFactory
                = //
                new Callback<TableColumn<SearchResult, String>, TableCell<SearchResult, String>>() {
                    @Override
                    public TableCell call(final TableColumn<SearchResult, String> param) {
                        final TableCell<SearchResult, String> cell = new TableCell<SearchResult, String>() {

                            final Button btn = new Button("Check Out");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        // Gets the ibsn number of the row the button was pressed
                                        SearchResult sr = getTableView().getItems().get(getIndex());
                                        String isbn = sr.getIsbn();
                                        message = null;

                                        // Creates the pop up stage
                                        final Stage dialog = new Stage();
                                        dialog.initModality(Modality.APPLICATION_MODAL);
                                        VBox dialogVbox = new VBox(20);
                                        // Pop up information
                                        dialogVbox.getChildren().add(new Text("Put your Card Number Here:"));
                                        TextField tf = new TextField();
                                        dialogVbox.getChildren().add(tf);

                                        Button btn = new Button();
                                        btn.setText("Go");
                                        btn.setOnAction(new EventHandler<ActionEvent>() {
                                            @Override
                                            public void handle(ActionEvent actionEvent) {
                                                insertIntoDatabase(tf.getText(), isbn);
                                            }
                                        });
                                        dialogVbox.getChildren().add(btn);
                                        dialogVbox.getChildren().add(new Text("Result:"));
                                        dialogVbox.getChildren().add(resultingMessage);
                                        Scene dialogScene = new Scene(dialogVbox, 300, 200);
                                        dialog.setScene(dialogScene);
                                        dialog.show();
                                        //System.out.println(sr.getIsbn());


                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        avCol.setCellFactory(cellFactory);

        try {
            ObservableList<Object> data = FXCollections.observableArrayList();
            while (rs.next()) {
                String isbn = rs.getString("Isbn");
                String title = rs.getString("Title");
                String author = rs.getString("Authors");
                String available = rs.getString("Available");
                SearchResult row = new SearchResult(isbn, title, author, available);

               data.add(row);
            }
            searchTable.setItems(data);
            searchTable.setVisible(true);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void insertIntoDatabase(String CardNo, String isbnNo){
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate myObj = LocalDate.now(); // Create a date object
        String formattedDate = myObj.format(myFormatObj);
        String currDate = formattedDate;

        LocalDate myObj2 = LocalDate.now().plusDays(14);
        String formattedDate2 = myObj2.format(myFormatObj);
        String dueDate = formattedDate2;

        try {
            // THIS WILL CONNECT TO THE DATABASE
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/library?useSSL=false","root","cs4347libraryproject2001");

            // THE INTS WILL BE GIVEN WHEN CONNECTING THIS TO THE GUI
            boolean passing = true;

            // WILL CHECK THAT THE BOOK IS NOT ALREADY CHECKED OUT
            String bookCheckedOutOrNotQuery = "select count(Isbn) from book_loans where Date_in is null and Isbn = ?";
            PreparedStatement bookCheck = conn.prepareStatement(bookCheckedOutOrNotQuery);
            bookCheck.setString(1, isbnNo);
            ResultSet result2 = bookCheck.executeQuery();
            result2.next();
            if (result2.getInt(1) == 1) {
                passing = false;
                System.out.println("This book has already been checked out.");
                resultingMessage.setText("This book has already been checked out.");
            }

            // WILL CHECK IF THE NUMBER OF BOOKS LOANS FOR A CERTAIN CARD HOLDER IS UNDER 3
            String numberOfLoansQuery = "select count(Card_id) from book_loans where Date_in is null and Card_id = ?";
            PreparedStatement number = conn.prepareStatement(numberOfLoansQuery);
            number.setString(1, CardNo);
            ResultSet result = number.executeQuery();
            result.next();
            if (result.getInt(1) >= 3) {
                passing = false;
                System.out.println("You already have the maxiumum numer of book leans which is 3. You can not have more than 3.");
                resultingMessage.setText("You already have the maximum number of book loans.");
            }

            // WILL QUERY TO GET UNIQUE LOAN_ID BUT THERE HAS TO BE AT LEAST 1 IN THERE ALREADY
            Statement stmt1 = conn.createStatement();
            ResultSet rs1 = stmt1.executeQuery("Select Loan_id from book_loans where Loan_id = (select max(Loan_id) from book_loans)");
            rs1.next();
            int max = rs1.getInt(1);
            max++;

            if (passing) {
                // PREPARED STAMENT TO PUT ALL CALULATED VALUES INTO THE TABLE
                String insertQuery = "insert into book_loans values (?, ?, ?, ?, ?, NULL)";
                PreparedStatement insertion = conn.prepareStatement(insertQuery);
                insertion.setInt(1, max);
                insertion.setString(2, isbnNo);
                insertion.setString(3, CardNo);
                insertion.setString(4, currDate);
                insertion.setString(5, dueDate);
                insertion.execute();
                resultingMessage.setText("Success");
            }

            conn.close();
        }catch (Exception e) { System.out.println(e);}


    }
    
    public void handleBorrowerAction(ActionEvent actionEvent)  throws IOException, SQLException {
        scene = (Scene) ((Node) actionEvent.getSource()).getScene();
        Alert emptyAlert = new Alert (AlertType.ERROR, "You have empty fields!", ButtonType.OK);
        boolean passing = true;
        if (FirstName.getText() == "" || LastName.getText() == "" || SSN.getText() == "" || Address.getText() == "" || City.getText() == "" || State.getText() == "")
        {
            emptyAlert.show();
            return;
        }
        
    	String firstN = FirstName.getText();
        String lastN = LastName.getText();
        String phoneNum = PhoneNumber.getText();
        String social = SSN.getText();
        String addr = Address.getText();
        String cit = City.getText();
        String stat = State.getText();
        System.out.println(stat);
        Borrower.connectDatabase();
        Borrower.createNewBorrower(social, firstN + " " + lastN, addr, cit, stat, phoneNum);
    }

    /**
     * This method is the onAction event handler which gets called once the Fines tab is displayed
     * @param event
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    public void displayFines(Event event) throws IOException, SQLException
    {
        // Only display the data if the finesTab is selected
        if (finesTab.isSelected())
        {
            // Bind the value of the column for each row to the variables in the FinesRow class
            borrowerNameCol.setCellValueFactory(new PropertyValueFactory<FinesRow, String>("borrowerNameCol"));
            bookTitleCol.setCellValueFactory(new PropertyValueFactory<FinesRow, String>("bookTitleCol"));
            daysLateCol.setCellValueFactory(new PropertyValueFactory<FinesRow, String>("daysLateCol"));
            fineAmtCol.setCellValueFactory(new PropertyValueFactory<FinesRow, String>("fineAmtCol"));
            finePaidCol.setCellValueFactory(new PropertyValueFactory<FinesRow, String>("finePaidCol"));
            try
            {
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library?useSSL=false", "root", "cs4347libraryproject2001");
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM library.fines");

                allFines = FXCollections.observableArrayList();
                unpaidFines = FXCollections.observableArrayList();
                while (rs.next())
                {
                    int loanID = rs.getInt("Loan_id");
                    double fineAmt = rs.getDouble("Fine_amt");
                    boolean paid = rs.getBoolean("Paid");

                    FinesRow finesTableRow = new FinesRow(loanID, fineAmt, paid);
                    allFines.add(finesTableRow);
                    if (!paid)
                    {
                        unpaidFines.add(finesTableRow);
                    }
                }

                finesTable.setItems(unpaidFines);
                finesTable.setVisible(true);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            paidFinesCheckbox.setSelected(false);
        }
    }

    /**
     * This method is called when the user checks or un-checks the "Show Previously Paid Fines" checkbox
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    public void getPaidFines(ActionEvent actionEvent) throws IOException, SQLException
    {
        scene = (Scene) ((Node) actionEvent.getSource()).getScene();

        if (paidFinesCheckbox.isSelected())
        {
            finesTable.setItems(allFines);
        }
        else
        {
            finesTable.setItems(unpaidFines);
        }
        finesTable.setVisible(true);
    }

    /**
     * This method is called when the user selects on a TableRow and clicks the "Refresh Fines" button
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    public void updateUnpaidFines(ActionEvent actionEvent) throws IOException, SQLException
    {
        scene = (Scene) ((Node) actionEvent.getSource()).getScene();

        try
        {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library?useSSL=false", "root", "cs4347libraryproject2001");
            Statement stmt = conn.createStatement();

            // Update all of the fines in the unpaidFines list and update database
            for (Object o : unpaidFines)
            {
                FinesRow row = (FinesRow) o;
                row.updateFine();
                int loanID = row.getLoanID();
                stmt.execute("UPDATE library.fines SET fines.Fine_amt=" + row.getFineAmount() + " WHERE fines.Loan_id=" + loanID);
            }

            paidFinesCheckbox.setSelected(false);
            displayFines(actionEvent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * This method is called when the user clicks on the "Pay Selected Fine" button.
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    public void payFine(ActionEvent actionEvent) throws IOException, SQLException
    {
        FinesRow finesRow = (FinesRow) finesTable.getSelectionModel().getSelectedItem();

        try
        {
            if (finesRow != null)
            {
                System.out.println("Loan_id: " + finesRow.getLoanID());
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library?useSSL=false", "root", "cs4347libraryproject2001");
                Statement stmt = conn.createStatement();

                // Only pay fines which haven't been payed yet
                if (finesRow.getFinePaidCol().equals("No"))
                {
                    // Only pay the fine if the book has been checked in
                    if (finesRow.getCheckInDate() != null)
                    {
                        stmt.execute("UPDATE library.fines SET fines.Paid=TRUE WHERE fines.Loan_id=" + finesRow.getLoanID());

                        paidFinesCheckbox.setSelected(false);
                        displayFines(actionEvent);
                    }
                    else {
                    	Alert alert = new Alert (AlertType.ERROR, "This book is still checked out!", ButtonType.OK);
                    	alert.show();
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}
