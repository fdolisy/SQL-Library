package application;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.sql.*;

public class Main extends Application {
	
	private static String url;
	private static String user;
	private static String password;

    public static void main(String[] args) {
    	System.out.println("url = " + args[0]);
    	System.out.println("user = " + args[1]);
    	System.out.println("password = " + args[2]);
    	url = args[0];
    	user = args[1];
    	password = args[2];
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
        	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Scene1.fxml"));
        	
        	Parent root = (Parent)fxmlLoader.load();
        	SceneController controller = fxmlLoader.<SceneController>getController();
        	controller.createConnection(url, user, password);
            Scene scene = new Scene(root);
            String css = this.getClass().getResource("application.css").toExternalForm();
            scene.getStylesheets().add(css);

            /* INITIALIZING STAGE PROPERTIES */

            //Image icon = new Image("bookshelf-icon.png");
            //stage.getIcons().add(icon);
            stage.setTitle("Richardson Library App");
            stage.setResizable(false);

            /* DATABASE CALL */

            try{
            	/*
            	Connection con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/library?useSSL=false","root","cs4347libraryproject2001");
                
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
                    System.out.println(rs.getString(1) + "  " + rs.getString(2) + "  " + rs.getString(3) + "  " + rs.getString(4));
                }
                con.close();
                */
            } catch(Exception e) { System.out.println("failed to connect to database"+ e); }


            /* SETTING/SHOWING SCENE */

            stage.setScene(scene);
            stage.show();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
