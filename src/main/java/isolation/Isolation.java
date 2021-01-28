package isolation;

import java.util.Random;

import gridgames.display.Display;
import gridgames.game.TwoPlayerGame;
import gridgames.player.EV3Player;
import gridgames.player.Player;
import lejos.hardware.Button;
import isolation.action.IsolationMoveAction;
import isolation.grid.IsolationBoard;
import isolation.player.IsolationPlayer;

public class Isolation extends TwoPlayerGame {
	
	private IsolationBoard isolationBoard;
	
	public Isolation(Display display, IsolationBoard board) {
		this.display = display;
		this.isolationBoard = board;
		initializeBoard();
	}

	@Override
	public void play(Player player1, Player player2) {
		IsolationPlayer isolationPlayer1 = (IsolationPlayer) player1.getGamePlayer();
		IsolationPlayer isolationPlayer2 = (IsolationPlayer) player2.getGamePlayer();
		boolean isPlayer1EV3Player = false;
		boolean isPlayer2EV3Player = false;
		Random r = new Random();
		IsolationPlayer winner;
		Player currentPlayer;
		IsolationMoveAction action;
		String turnMessage;
		
    	if(player1 instanceof EV3Player) {
        	isPlayer1EV3Player = true;
        }
		if(player2 instanceof EV3Player) {
        	isPlayer2EV3Player = true;
        }
		//randomly select first player
		currentPlayer = (r.nextInt(2)==0)?player1:player2;
		
		do {
			turnMessage = "Player " + ((currentPlayer==player1)?"1":"2") + "'s turn";
			display.addMessage(turnMessage);
            display.printState(true);
            action = (IsolationMoveAction) currentPlayer.getAction();
            while(!isolationBoard.isValidAction((IsolationPlayer) currentPlayer.getGamePlayer(), action)) {
            	action = (IsolationMoveAction) currentPlayer.getAction();
            }
            isolationBoard.movePlayer((IsolationPlayer) currentPlayer.getGamePlayer(), action);
            currentPlayer.incrementNumActionsExecuted();

        	//next player's turn
        	currentPlayer = (currentPlayer == player1)?player2:player1;
        } while (!isGameOver(isolationPlayer1, isolationPlayer2));
		
		winner = getWinner(isolationPlayer1, isolationPlayer2);
        display.addMessage("Player " + winner.toString() + " won in " + winner.getNumActionsExecuted() + " moves.");
        display.printState(true);
        
        if(isPlayer1EV3Player || isPlayer2EV3Player) {
        	Button.waitForAnyPress();
        }
	}

	@Override
	public void initializeBoard() {
		this.display.setBoard(this.isolationBoard);
	}
	
	private boolean isGameOver(IsolationPlayer player1, IsolationPlayer player2) {
		return isolationBoard.isPlayerIsolated(player1) || isolationBoard.isPlayerIsolated(player2);
    }
	
	private IsolationPlayer getWinner(IsolationPlayer player1, IsolationPlayer player2) {
		if(isolationBoard.isPlayerIsolated(player1)) {
			return player2;
		} else {
			return player1;
		}
	}
}
