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


public class Main extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
			
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			/* INITIALIZING STAGE PROPERTIES */
			
			Image icon = new Image("bookshelf-icon.png");
			stage.getIcons().add(icon);
			stage.setTitle("Stage Demo Program");
			stage.setResizable(false);
			//stage.setFullScreen(true);
			
			/* ADDING ELEMENTS/NODES
			
			Text text = new Text();
			text.setText("WHOA!!!");
			text.setX(50);
			text.setY(50);
			text.setFont(Font.font("Verdana", 50));
			text.setFill(Color.LIMEGREEN);
			root.getChildren().add(text);
			
			Line line = new Line();
			line.setStartX(200);
			line.setStartY(200);
			line.setEndX(500);
			line.setEndY(200);
			line.setStrokeWidth(5);
			line.setStroke(Color.RED);
			line.setOpacity(0.5);
			root.getChildren().add(line);
			
			ImageView imageView = new ImageView(icon);
			imageView.setX(400);
			imageView.setY(400);
			imageView.setPreserveRatio(true);
			imageView.setFitHeight(100);
			root.getChildren().add(imageView);
			
			*/
			
			/* SETTING/SHOWING SCENE */
			
			stage.setScene(scene);
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
