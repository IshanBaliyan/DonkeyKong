package assignment;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import simpleIO.Console;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

/**
 * @author Ishan Baliyan
 * 2019-06-22
 * ICS3U
 * Program that plays a game of DONKEY KONG with a main character and the objective of reaching the princess at the top.
 */

public class DonkeyKong extends Application {

	
	//Making the stage, scene and pane values global for using in other classes
	static Stage primaryStage;
	static Pane root;
	Scene scene;
	
	
	@SuppressWarnings("static-access")
	@Override
	public void start(Stage primaryStage) {
		try {
			
				//Printing the basic function and purpose of the program in the console for simplicity since it is only for reference.
				Console.print("Welcome to Donkey Kong");
				Console.print("This games purpose is to save the princess and get to the top of the tower without getting hit by barrels");
				Console.print("Note, this game is pretty difficult and requires perfect exceution for winning");
				Console.print("Press the LEFT arrow key to move left and Press RIGHT arrow key to move right.");
				Console.print("Press SPACE to jump. Press the UP arrow key or the DOWN arrow key to move up or down on a ladder");

				//Creating the pane, stage and scenes for the initial field for the program 
				this.primaryStage = primaryStage;
				root = FXMLLoader.load(getClass().getResource("GUI.fxml"));

				//Creating the scene with 1250x 900 dimensions 
				scene = new Scene(root,1250,900);
				
				//Note that no CSS was manually coded and the application.css class is empty
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

				//Setting the scene and displaying it
				primaryStage.setScene(scene);
				primaryStage.show();
				
				//Making the pane the focus for key pressed and key released functions
	   			root.requestFocus();


		} catch(Exception e) {
			
			//To catch the exception(s) and provide a standard error stream to follow
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		
		//Starting the game
		launch(args);
	}
}