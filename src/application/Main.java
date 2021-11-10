package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class Main extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Scene1.fxml"));
			Scene scene = new Scene(root);
			String css = this.getClass().getResource("application.css").toExternalForm();
			scene.getStylesheets().add(css);
			
			/* INITIALIZING STAGE PROPERTIES */
			
			Image icon = new Image("bookshelf-icon.png");
			stage.getIcons().add(icon);
			stage.setTitle("Richardson Library App");
			stage.setResizable(false);
			
			/* DATABASE CALL */
			
			try{  
				Connection con = DriverManager.getConnection(  
				"jdbc:mysql://localhost:3306/library","root","cs4347libraryproject2001"); 
				Statement stmt = con.createStatement();
				
				ResultSet rs = stmt.executeQuery(""
						+ "SELECT b.Isbn, b.Title, ba.Author_id, a.Name "
						+ "FROM book b "
						+ "LEFT JOIN book_authors ba on b.Isbn = ba.Isbn "
						+ "LEFT JOIN authors a on ba.Author_id = a.Author_id "
						+ "ORDER BY Author_id");
				ResultSetMetaData rsmd = rs.getMetaData();
				System.out.println(rsmd.getColumnName(1) + "  " + rsmd.getColumnName(2) + "  " + rsmd.getColumnName(3) + "  " + rsmd.getColumnName(4));
				while(rs.next()) {
					System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3) + "  " + rs.getString(4));  
				}
				con.close();  
			} catch(Exception e) { System.out.println(e); }  
			
			/* SETTING/SHOWING SCENE */
			
			stage.setScene(scene);
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}