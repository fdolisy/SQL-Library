package application;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.Stage;

import javax.swing.*;


public class SceneController {

    @FXML private Stage stage;
    @FXML private Scene scene;
    @FXML private Parent root;
    @FXML private TextField searchField;

    @FXML private TableView searchTable;
    @FXML private TableColumn isbnCol;
    @FXML private TableColumn  titleCol;
    @FXML private TableColumn  authorCol;
    @FXML private TableColumn  avCol;

    @FXML private TableView finesTable;
    @FXML private TableColumn borrowerNameCol;
    @FXML private TableColumn bookTitleCol;
    @FXML private TableColumn daysLateCol;
    @FXML private TableColumn fineAmtCol;
    @FXML private TableColumn finePaidCol;

    @FXML private CheckBox paidFinesCheckbox;

    @FXML private Tab finesTab;

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
    public void handleSearchAction(ActionEvent actionEvent) throws IOException, SQLException {
        scene = (Scene) ((Node) actionEvent.getSource()).getScene();
        String searchTxt = searchField.getText();
        System.out.println(searchTxt);
        Search search = new Search(searchTxt);
        ResultSet rs = search.searchData();

        isbnCol.setCellValueFactory(new PropertyValueFactory<SearchResult, String>("Isbn"));
        titleCol.setCellValueFactory(new PropertyValueFactory<SearchResult, String>("Title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<SearchResult, String>("Author"));
        avCol.setCellValueFactory(new PropertyValueFactory<SearchResult, String>("Status"));

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
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
