package assignment;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.management.timer.Timer;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import simpleIO.Console;
public class Controller {
	
	
	//Declaring and linking rectangle values for each floor with the FXML file
	@FXML Rectangle floor1;
	@FXML Rectangle floor2;
	@FXML Rectangle floor3;
	@FXML Rectangle floor4;
	@FXML Rectangle floor5;
	@FXML Rectangle floor6;

	//Declaring and linking all ladders with the FXML file. Note that floor 4 has two ladders.
	@FXML ImageView ladder1;
	@FXML ImageView ladder2;
	@FXML ImageView ladder3;
	@FXML ImageView ladder4;
	@FXML ImageView ladder5;
	@FXML ImageView ladder6;
	
	////Declaring and linking all barrels in the game with the FXML file
	@FXML ImageView barrel1;
	@FXML ImageView barrel2;
	@FXML ImageView barrel3;
	@FXML ImageView barrel4;
	@FXML ImageView barrel5;
	@FXML ImageView barrel6;
	@FXML ImageView barrel7;
	
	//In Donkey Kong, Mario was referred to as "jumpman".
	//Declaring and linking character images with the FXML file
	@FXML ImageView jumpman;
	@FXML ImageView donkeyKong;
	@FXML ImageView princess;
	
	//Declaring and linking title for the name of the game and the "You Win" message at the end of the game.
	@FXML Label title;

	//Declaring variable for the ground Y coordinate since the ground value changes when you are on different floors.
	double groundY;
	
	//Declaring variables for Jumpman's speed.
	final int SPEED = 5;
	final int LADDER_SPEED = 3;
	
	//Declaring variables for the movement of Jumpman's x and y coordinates
	double dx = 0;
	double dy = 0;
	
	//Declaring variables for the movement of the barrels x coordinates since they don't move down each floor.
	double barrel1X = 0, barrel2X = 0, barrel3X = 0, barrel4X = 0, barrel5X = 0, barrel6X = 0, barrel7X = 0;

	//Variables to check if the barrels have hit their required location on the opposite side of the floor before turning back
	boolean hitSideBarrel1 = false, hitSideBarrel2 = false, hitSideBarrel3 = false, hitSideBarrel4 = false, hitSideBarrel5 = false,
			hitSideBarrel6 = false, hitSideBarrel7 = false;
	
	//Declaring variable for the character currently jumping
	boolean jump = false;
	
	//Declaring variable for the first instance of the program
	boolean firstTime = true;
	
	//Declare variable to check if Jumpman is on a ladder
	boolean onLadder = false;
	
	//Declare variable to check if the game is over.
	boolean gameOver = false;
	
	
	//The default ladder that is on Jumpman's floor is ladder 1
	int ladderSelected = 1;
	
	//Declaring variable for Jumpman's jumpheight
	final int JUMP_HEIGHT = 13;
	
	//Declaring variable for the refresh rate of the program in nanoseconds - 10^7 nanoseconds equals a second
	final int THRESHOLD = 30_000_000; 

	
	/**
	 * This method runs the main controller code in a timer to refresh the game
	 */
	public Controller() {
		
		//Declaring a timer and starting the timer for the program
		GameTimer timer = new GameTimer();
		timer.start();
	}
	
	public class GameTimer extends AnimationTimer {
		
		//Declaring variable for the refresh rate;
		long lastTime; 
		
		/**
		 * This method handles the main movements of frame rate and refresh rate of the program
		 * 
		 * @param now
		 * 			The current time for refreshing
		 */
		@Override
		public void handle(long now) { // now in nanoseconds			

			//Essentially looping program every 30,000,000 nanoseconds
			if (now - lastTime > THRESHOLD) {
				
				// One refresh rate has passed
				lastTime = now;
				
				//Declaring variables for the floor bounds
				Bounds floor1Bounds = floor1.getBoundsInParent();
				Bounds floor2Bounds = floor2.getBoundsInParent();
				Bounds floor3Bounds = floor3.getBoundsInParent();
				Bounds floor4Bounds = floor4.getBoundsInParent();
				Bounds floor5Bounds = floor5.getBoundsInParent();
				Bounds floor6Bounds = floor6.getBoundsInParent();
				
				//Declaring variable for Jumpman's bounds
				Bounds characterBounds = jumpman.getBoundsInParent();
				
				//Declaring variables for the ladder bounds
				Bounds ladder1Bounds = ladder1.getBoundsInParent();
				Bounds ladder2Bounds = ladder2.getBoundsInParent();
				Bounds ladder3Bounds = ladder3.getBoundsInParent();
				Bounds ladder4Bounds = ladder4.getBoundsInParent();
				Bounds ladder5Bounds = ladder5.getBoundsInParent();
				Bounds ladder6Bounds = ladder6.getBoundsInParent();
				
				//Declaring variables for the barrel bounds
				Bounds barrel1Bounds = barrel1.getBoundsInParent();
				Bounds barrel2Bounds = barrel2.getBoundsInParent();
				Bounds barrel3Bounds = barrel3.getBoundsInParent();
				Bounds barrel4Bounds = barrel4.getBoundsInParent();
				Bounds barrel5Bounds = barrel5.getBoundsInParent();
				Bounds barrel6Bounds = barrel6.getBoundsInParent();
				Bounds barrel7Bounds = barrel7.getBoundsInParent();
				
				//Declaring variable for princess bounds
				Bounds princessBounds = princess.getBoundsInParent();
				
				//Declaring array for holding the bounds of the ladders, barrels and the floors.
				Bounds[] floorBounds = {floor1Bounds,floor2Bounds,floor3Bounds, floor4Bounds, floor5Bounds, floor6Bounds};
				Bounds[] barrelBounds = {barrel1Bounds, barrel2Bounds, barrel3Bounds, barrel4Bounds, barrel5Bounds, barrel6Bounds, barrel7Bounds};
				Bounds[] ladderBounds = {ladder1Bounds, ladder2Bounds, ladder3Bounds, ladder4Bounds, ladder5Bounds, ladder6Bounds};
				
				//For the initial run, set the ground value to 0;
				if(firstTime == true) {
					groundY = 0.0 - 10;
					firstTime = false;
				}
				
				//If the character is jumping and the character is not on the ladder
				if (jump == true && onLadder == false) {
					// force of gravity
					//Increase the y position by 1 (move down) after every loop of the refresh rate
					dy += 1;
				} 
				//if the character is not jumping and the character is not on the ladder
				else if (jump == false && onLadder == false) {
					
					//Stop moving the character in the air
					dy = 0;
				}

				//If the jump has reached the jump height and the character is not on the ladder and the game is not over
				if (dy >= JUMP_HEIGHT && onLadder == false && jump == true && gameOver == false) {
					
					//Stop jumping
					jump = false;
					
					//Set the y position of the character to the ground of the new floor (top of the ladder)
					jumpman.setY(groundY - 5);
				}
				
				//Declaring variable to flip the direction Jumpman is facing if needed
				flipMario();
				
				//Calling method that detects when character is using ladder and changes character's movement
				usingLadder(floorBounds, characterBounds, ladderBounds); 
				
				//Calling method that rolls all the barrels across each floor back and forth at different speed and directions
				rollBarrels();
				
				//Calling method that check when the character has hit a barrel and ends the game
				hitBarrels(characterBounds, barrelBounds); 

				//Calling method to check if the user has won the game
				checkWin(characterBounds, princessBounds);
				
				//Changing the barrels' movements after each refresh loop to their new given position
				barrel1.setX(barrel1.getY() + barrel1X);
				barrel2.setX(barrel2.getY() + barrel2X);
				barrel3.setX(barrel3.getY() + barrel3X);
				barrel4.setX(barrel4.getY() + barrel4X);
				barrel5.setX(barrel5.getY() + barrel5X);
				barrel6.setX(barrel6.getY() + barrel6X);
				barrel7.setX(barrel7.getY() + barrel7X);
				
				//Moving the character's x and y positions after each loop to their new given position
				jumpman.setX(jumpman.getX() + dx);
				jumpman.setY(jumpman.getY() + dy);
			}
		}

		
		/**
		 * 
		 * This method determines if the user has won the game.
		 * 
		 * @param characterBounds
		 * 					The bounds of the character
		 * @param princessBounds
		 * 					The bounds of the princess
		 */
		public void checkWin(Bounds characterBounds, Bounds princessBounds) {
			
			//If the character has touched the princess
			if(characterBounds.intersects(princessBounds)) {
				//The user has won the game
				gameWin();
			}
		}


		/**
		 * This method displays a message of winning the game.
		 */
		public void gameWin() {
			
			//Set the font of the title to David Libre and the size increased to 80
			title.setFont(new Font("David Libre", 80));
			
			//Change the title text to "You win"
			title.setText("You win!");
			
			//Align the title to the center of the label
			title.setAlignment(Pos.CENTER);
			
			//Set the game over value to true to end the game
			gameOver = true;
		}


		/**
		 * This method checks if the user hit the barrels and then ends the game if necessary.
		 * 
		 * @param characterBounds
		 * 						The bounds of the character
		 * @param barrelBounds
		 * 					The bounds of the barrel in an array
		 */
		public void hitBarrels(Bounds characterBounds, Bounds[] barrelBounds) {
			
			//Using try catch statements due to the use of a new pane for the losing message
			try {
				
				//If the character has intersected the bounds of any of the barrels, end the game.
				if(characterBounds.intersects(barrelBounds[0])){
					gameOver();
				} else if(characterBounds.intersects(barrelBounds[1])){
					gameOver();
				} else if(characterBounds.intersects(barrelBounds[2])){
					gameOver();
				} else if(characterBounds.intersects(barrelBounds[3])){
					gameOver();
				} else if(characterBounds.intersects(barrelBounds[4])){
					gameOver();
				} else if(characterBounds.intersects(barrelBounds[5])){
					gameOver();
				} else if(characterBounds.intersects(barrelBounds[6])){
					gameOver();
				}}
			catch (IOException e) {
				
				//Helps trace the problem through the standard error stream
				e.printStackTrace();
			}
		}


		/**
		 * 
		 * This method checks if the character is using the ladder and changes his location.
		 * @param floorBounds
		 * 					The bounds of all the floors in an array.
		 * @param characterBounds
		 * 					The bounds of the character.
		 * @param ladderBounds
		 * 					The bounds of the ladder
		 */
		public void usingLadder(Bounds[] floorBounds, Bounds characterBounds, Bounds[] ladderBounds) {

		//If the character is in the same x position as the ladder and is intersecting the ladder
		//Ladder 1
		if(characterBounds.getMaxX() > ladderBounds[0].getMinX()) {
			
			//If the character is intersecting the ladder in both x and y positions
			if (characterBounds.intersects(ladderBounds[0])) {
				
				//The character is on the ladder
				onLadder = true;
				
				//Set the current ladder selected to ladder 1
				ladderSelected = 1;
			} 
			
			//Otherwise, if the character is on the ladder and is not intersecting the ladder anymore and last used ladder one.
			else if (onLadder == true && !characterBounds.intersects(ladderBounds[0]) && ladderSelected == 1){
				
				//State the new coordinates of the floor
				groundY = floorBounds[1].getMinY() - floorBounds[0].getMinY() - 7;
				
				//The character is no longer on the ladder
				onLadder = false;
			}
		} 

		//If the character is in the same x position as the ladder and is intersecting the ladder
		//Ladder 2
		if(characterBounds.getMaxX() > ladderBounds[1].getMinX()) {
			
			//If the character is intersecting the ladder in both x and y positions
			if (characterBounds.intersects(ladderBounds[1])) {
				
				//The character is on the ladder
				onLadder = true;
				
				//Set the current ladder selected to ladder 1
				ladderSelected = 2;
			} 
			
			//Otherwise, if the character is on the ladder and is not intersecting the ladder anymore and last used ladder two.
			else if (onLadder == true && !characterBounds.intersects(ladderBounds[1]) && ladderSelected == 2){
				
				//State the new coordinates of the floor
				groundY = floorBounds[2].getMinY() - floorBounds[0].getMinY() - 7;
				
				//The character is no longer on the ladder
				onLadder = false;
			}
		} 

		//If the character is in the same x position as the ladder and is intersecting the ladder
		//Ladder 3
		if(characterBounds.getMaxX() > ladderBounds[2].getMinX()) {
			
			//If the character is intersecting the ladder in both x and y positions
			if (characterBounds.intersects(ladderBounds[2])) {
				
				//The character is on the ladder
				onLadder = true;
				
				//Set the current ladder selected to ladder 1
				ladderSelected = 3;
			} 
			//Otherwise, if the character is on the ladder and is not intersecting the ladder anymore and last used ladder three.
			else if (onLadder == true && !characterBounds.intersects(ladderBounds[2]) && ladderSelected == 3){
				
				//State the new coordinates of the floor
				groundY = floorBounds[3].getMinY() - floorBounds[0].getMinY() - 7;
				
				//The character is no longer on the ladder
				onLadder = false;
			}
		} 

		//If the character is in the same x position as the ladder and is intersecting the ladder
		//Ladder 4
		if(characterBounds.getMaxX() > ladderBounds[3].getMinX()) {
			
			//If the character is intersecting the ladder in both x and y positions
			if (characterBounds.intersects(ladderBounds[3])) {
				
				//The character is on the ladder
				onLadder = true;
				
				//Set the current ladder selected to ladder 1
				ladderSelected = 4;
			} 
			//Otherwise, if the character is on the ladder and is not intersecting the ladder anymore and last used ladder four.
			else if (onLadder == true && !characterBounds.intersects(ladderBounds[2]) && ladderSelected == 4){
				
				//State the new coordinates of the floor
				groundY = floorBounds[3].getMinY() - floorBounds[0].getMinY() - 7; 
				
				//The character is no longer on the ladder
				onLadder = false;
			}
		} 

		//If the character is in the same x position as the ladder and is intersecting the ladder
		//Ladder 5
		if(characterBounds.getMaxX() > ladderBounds[4].getMinX()) {
			
			//If the character is intersecting the ladder in both x and y positions
			if (characterBounds.intersects(ladderBounds[4])) {
				
				//The character is on the ladder
				onLadder = true;
				
				//Set the current ladder selected to ladder 1
				ladderSelected = 5;
			} 
			//Otherwise, if the character is on the ladder and is not intersecting the ladder anymore and last used ladder five.
			else if (onLadder == true && !characterBounds.intersects(ladderBounds[4])  && ladderSelected == 5){
				
				//State the new coordinates of the floor
				groundY = floorBounds[4].getMinY() - floorBounds[0].getMinY() - 7;
				
				//The character is no longer on the ladder
				onLadder = false;
				
			}
		} 

		//If the character is in the same x position as the ladder and is intersecting the ladder
		//Ladder 6
		if(characterBounds.getMaxX() > ladderBounds[5].getMinX()) {
			
			//If the character is intersecting the ladder in both x and y positions
			if (characterBounds.intersects(ladderBounds[5])) {
				
				//The character is on the ladder
				onLadder = true;
				
				//Set the current ladder selected to ladder 1
				ladderSelected = 6;
			} 
			//Otherwise, if the character is on the ladder and is not intersecting the ladder anymore and last used ladder six.
			else if (onLadder == true && !characterBounds.intersects(ladderBounds[0])  && ladderSelected == 6){
				
				//State the new coordinates of the floor
				groundY = floorBounds[5].getMinY() - floorBounds[0].getMinY() - 7;
				
				//The character is no longer on the ladder
				onLadder = false;
			}
		}
		}

		/**
		 * Method that flips the direction Jumpman is facing depending on the direction he is moving
		 */
		public void flipMario() {
			
			//If the character is facing left
			if(dx < 0) {
				//Flip the character to face left
				jumpman.setScaleX(-1);	
			} 

			//If the character is facing right
			else if (dx > 0) {
				
				//Flip the character to face right
				jumpman.setScaleX(1);
			}
		}

		/**
		 * This method moves the barrels automatically across the map
		 */
		public void rollBarrels() {
			
			//Barrel 1 movement
			
			//If the barrel has not hit the other side
			if(barrel1.getX() >= -800 && hitSideBarrel1 == false) {
				
				//Make the barrel move to the opposite direction of the floor
				barrel1X += -6;
				
				//Rotate the barrel 10 degrees 
				barrel1.setRotate(barrel1.getRotate() - 10);
			} 
			//Otherwise, the barrel must have reached the other side
			else if(barrel1.getX() <= 0){
				
				//The barrel reached the other side of the floor
				hitSideBarrel1 = true;
				
				//Move the barrel to the opposite direction
				barrel1X += 6;
				
				//Rotate the barrel in the opposite direction
				barrel1.setRotate(barrel1.getRotate() + 10);
			}
			//If the barrel reaches its starting point
			if(barrel1.getX()==0) {
				
				//The barrel has arrived to its origin side
				hitSideBarrel1 = false;
			}
			
			
			//Barrel 2 movement
			
			//If the barrel has not hit the other side
			if(barrel2.getX() >= -920 && hitSideBarrel2 == false) {
				
				//Make the barrel move to the opposite direction of the floor
				barrel2X += -6;
				
				//Rotate the barrel 10 degrees 
				barrel2.setRotate(barrel2.getRotate() - 10);
			} else if(barrel2.getX() <= 0){
				
				//The barrel reached the other side of the floor
				hitSideBarrel2 = true;
				
				//Move the barrel to the opposite direction
				barrel2X += 6;
				
				//Rotate the barrel in the opposite direction
				barrel2.setRotate(barrel2.getRotate() + 10);
			}
			if(barrel2.getX()==0) {
				
				//The barrel has arrived to its origin side
				hitSideBarrel2 = false;
			}
			
			//Barrel 3 movement
			
			//If the barrel has not hit the other side
			if(barrel3.getX() <= 800 && hitSideBarrel3 == false) {
				
				//Make the barrel move to the opposite direction of the floor
				barrel3X += 6;
				
				//Rotate the barrel 10 degrees 
				barrel3.setRotate(barrel3.getRotate() + 10);
			} else if(barrel3.getX() >= 0){
				
				//The barrel reached the other side of the floor
				hitSideBarrel3 = true;
				
				//Move the barrel to the opposite direction
				barrel3X += -6;
				
				//Rotate the barrel in the opposite direction
				barrel3.setRotate(barrel3.getRotate() - 10);
			}
			if(barrel3.getX()==0) {
				
				//The barrel has arrived to its origin side
				hitSideBarrel3 = false;
			}
			
			//Barrel 4 movement
			
			//If the barrel has not hit the other side
			if(barrel4.getX() >= -920 && hitSideBarrel4 == false) {
				
				//Make the barrel move to the opposite direction of the floor
				barrel4X += -6;
				
				//Rotate the barrel 10 degrees 
				barrel4.setRotate(barrel4.getRotate() - 10);
			} else if(barrel4.getX() <= 0){
				
				//The barrel reached the other side of the floor
				hitSideBarrel4 = true;
				
				//Move the barrel to the opposite direction
				barrel4X += 6;
				
				//Rotate the barrel in the opposite direction
				barrel4.setRotate(barrel4.getRotate() + 10);
			}
			if(barrel4.getX()==0) {
				
				//The barrel has arrived to its origin side
				hitSideBarrel4 = false;
			}
			
			//Barrel 5

			//If the barrel has not hit the other side
			if(barrel5.getX() <= 800 && hitSideBarrel5 == false) {
				
				//Make the barrel move to the opposite direction of the floor
				barrel5X += 7;
				
				//Rotate the barrel 10 degrees 
				barrel5.setRotate(barrel5.getRotate() + 10);
			} else if(barrel5.getX() >= 0){
				
				//The barrel reached the other side of the floor
				hitSideBarrel5 = true;
				
				//Move the barrel to the opposite direction
				barrel5X += -7;
				
				//Rotate the barrel in the opposite direction
				barrel5.setRotate(barrel5.getRotate() - 10);
			}
			if(barrel5.getX()==0) {
				
				//The barrel has arrived to its origin side
				hitSideBarrel5 = false;
			}
			
			//Barrel 6
			
			//If the barrel has not hit the other side
			if(barrel6.getX() >= -800 && hitSideBarrel6 == false) {
				
				//Make the barrel move to the opposite direction of the floor
				barrel6X += -9;
				
				//Rotate the barrel 10 degrees 
				barrel6.setRotate(barrel6.getRotate() - 10);
			} else if(barrel6.getX() <= 0){
				
				//The barrel reached the other side of the floor
				hitSideBarrel6 = true;
				
				//Move the barrel to the opposite direction
				barrel6X += 9;
				
				//Rotate the barrel in the opposite direction
				barrel6.setRotate(barrel6.getRotate() + 10);
			}
			if(barrel1.getX()==0) {
				
				//The barrel has arrived to its origin side
				hitSideBarrel6 = false;
			}
			
			//Barrel 7
			
			//If the barrel has not hit the other side
			if(barrel7.getX() <= 800 && hitSideBarrel7 == false) {
				
				//Make the barrel move to the opposite direction of the floor
				barrel7X += 8;
				
				//Rotate the barrel 10 degrees 
				barrel7.setRotate(barrel7.getRotate() + 10);
			} else if(barrel7.getX() >= 0){
				
				//The barrel reached the other side of the floor
				hitSideBarrel7 = true;
				
				//Move the barrel to the opposite direction
				barrel7X += -8;
				
				//Rotate the barrel in the opposite direction
				barrel7.setRotate(barrel7.getRotate() - 10);
			}
			if(barrel7.getX()==0) {
				
				//The barrel has arrived to its origin side
				hitSideBarrel7 = false;
			}
		}
	}
	/**
	 * This method makes the character jump
	 * 
	 * @throws InterruptedException
	 */
	@FXML
	public void jump() throws InterruptedException {
		
		//Setting jump value to true
		jump = true;
		
		//Making the character move back to the ground
		dy = -JUMP_HEIGHT;
		
		//Don't let the character move left or right since it is NOT a quadratic jump
		dx = 0;

	}
	
	/**
	 * This method handles the keys that are pressed fro the user and makes the required actions
	 * 
	 * @param event
	 * 			The events from the user.
	 * @throws InterruptedException
	 */
	@FXML
	public void keyPressed(KeyEvent event) throws InterruptedException {
		
		Bounds characterBounds = jumpman.getBoundsInParent();
		
		//If the game is still running
		if(gameOver == false) {
			
			//If the user presses space and the character is not on the ladder or jumping already
			if (event.getCode() == KeyCode.SPACE && onLadder == false && jump == false) {
				
				//Jump
				jump();
			} 
			
			//If the user presses up and the character is on the ladder
			else if(event.getCode() == KeyCode.UP && onLadder == true){
				
				//Move the character up at the ladder speed
				dy = -LADDER_SPEED;
				
				//Don't let the character move left or right on the ladder
				dx = 0;

			} 
			
			//If the user wants to move down, and is on the ladder
			else if (event.getCode() == KeyCode.DOWN && onLadder == true && characterBounds.getMinY() > groundY - 12) {

				//Move down at ladder speed
				dy = LADDER_SPEED;
				
				//Do not move left or right
				dx = 0;

			}

			//If the user presses the left key and the character is not on the ladder
			if (event.getCode() == KeyCode.LEFT && onLadder == false) {
				
				//Move left at the Jumpman's speed
				dx = -SPEED;
				
				//Do not move any Y positions
				dy = 0;
			} 
			
			//If the user presses the right key and the character is not on the ladder
			else if (event.getCode() == KeyCode.RIGHT && onLadder == false) {
				
				//Move right at Jumpman's speed
				dx = SPEED;
				
				//Do not move up or down
				dy = 0;
			}
		}

	}
	/**
	 * This method checks if certain keys were released and does the required actions
	 * 
	 * @param event
	 * 			The events from the user
	 * @throws InterruptedException
	 */
	@FXML
	public void keyReleased(KeyEvent event) throws InterruptedException {
		
		//Declaring variable for the event;
		KeyCode code = event.getCode();
		
		//If the user is not pressing up or down and is not on the ladder
		if ((code == KeyCode.UP || code == KeyCode.DOWN ) && onLadder == true) {
			
			//Do not move up or down
			dy = 0;
		} 
		
		//If the user is not pressing the left or right keys
		if (code == KeyCode.LEFT || code == KeyCode.RIGHT) {
			
			//Do not move the character left or right
			dx = 0;
		}

	}
	/**
	 * This method ends the game and changes the display page
	 * @throws IOException
	 */
	public void gameOver() throws IOException {
		
		//Stop moving the character
		dx = 0;
		dy = 0;
		jump = false;
		
		//Create a new pane for the game over page. 
		Pane gameOverPage = FXMLLoader.load(getClass().getResource("GameOver.fxml"));
		Scene endPage = new Scene(gameOverPage,1250,900);
		endPage.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		DonkeyKong.primaryStage.setScene(endPage);
		DonkeyKong.primaryStage.show();
		
		//Set the game over value to true
		gameOver = true;
		
	}
}

