package application;

import java.io.IOException;
import java.sql.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


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

    private ObservableList<SearchResult> data;


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
}
