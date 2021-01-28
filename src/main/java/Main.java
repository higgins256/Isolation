import gridgames.display.ConsoleDisplay;
import gridgames.display.Display;
import gridgames.display.EV3Display;
import gridgames.data.action.Action;
import gridgames.player.Player;
import isolation.Isolation;
import isolation.action.IsolationMoveAction;
import isolation.grid.IsolationBoard;
import isolation.player.IsolationPlayer;
import isolation.player.MinimaxSearchPlayer;
import isolation.player.MinimaxSearchWithABPPlayer;
import gridgames.player.HumanPlayer;
import gridgames.player.EV3Player;

import java.util.List;
import java.util.Scanner;

public class Main {
	
	public static int DEPTH_LIMIT = 5;
	
    public static void main(String[] args) {
    	List<Action> allActions = IsolationMoveAction.getAllMoveActions();
    	if(args.length > 0 && "-console".equals(args[0])) {
    		runOnConsole(allActions);
    	} else {
    		runOnRobot(allActions);
    	}
    }
    
    public static void runOnConsole(List<Action> allActions) {
    	Scanner scanner = new Scanner(System.in);
    	Display display = new ConsoleDisplay();
        String choice;
        Isolation game = null;
        Player player1 = null;
        Player player2 = null;
        IsolationPlayer isolationPlayer1;
        IsolationPlayer isolationPlayer2;
        IsolationBoard isolationBoard;
        
        do {
        	isolationBoard = new IsolationBoard(7, 7);
        	player1 = getPlayer1(scanner, display, isolationBoard);
        	player2 = getPlayer2(scanner, display, isolationBoard);
        	isolationPlayer1 = (IsolationPlayer) player1.getGamePlayer();
        	isolationPlayer2 = (IsolationPlayer) player2.getGamePlayer();
        	
        	isolationBoard.placePlayers(isolationPlayer1, isolationPlayer2);
        	game = new Isolation(display, isolationBoard);
        	
            do {
            	game.play(player1, player2);
                System.out.print("Play again? [YES, NO]: ");
                choice = scanner.next().toLowerCase();
            } while(!choice.equals("yes") && !choice.equals("no"));
        } while(choice.equals("yes"));
        scanner.close();
    }
    
    public static void runOnRobot(List<Action> allActions) {
    	IsolationBoard isolationBoard = new IsolationBoard(7, 7);
    	
    	EV3Display display = new EV3Display();
    	IsolationPlayer player = new MinimaxSearchWithABPPlayer(IsolationMoveAction.getAllMoveActions(), display, isolationBoard, 1);
        player.setDepthLimit(DEPTH_LIMIT);
        EV3Player robot1 = new EV3Player(player);
        display.setEv3Display(robot1.getEv3Display());
        robot1.displayInstructions();
        
        //TODO: FIGURE THIS OUT
        display = new EV3Display();
    	player = new MinimaxSearchWithABPPlayer(IsolationMoveAction.getAllMoveActions(), display, isolationBoard, 2);
        player.setDepthLimit(DEPTH_LIMIT);
        EV3Player robot2 = new EV3Player(player);        
        display.setEv3Display(robot2.getEv3Display());
        robot2.displayInstructions();
        ////////////////////////////////////////////////
       
        isolationBoard.placePlayers((IsolationPlayer)robot1.getGamePlayer(), (IsolationPlayer)robot2.getGamePlayer());
    	Isolation game = new Isolation(display, isolationBoard);
    	
        game.play(robot1, robot2);
    }
    
    private static Player getPlayer1(Scanner scanner, Display display, IsolationBoard board) {
    	return getPlayer(scanner, display, board, 1);
    }
    
    private static Player getPlayer2(Scanner scanner, Display display, IsolationBoard board) {
    	return getPlayer(scanner, display, board, 2);
    }
    
    private static Player getPlayer(Scanner scanner, Display display, IsolationBoard board, int playerNumber) {
    	List<Action> actions = IsolationMoveAction.getAllMoveActions();
    	Player player = null;
    	String choice;
    	 do {
             System.out.print("Player " + playerNumber + ": human or computer? [HUMAN, COMPUTER]: ");
             choice = scanner.next().toLowerCase();
         } while(!choice.equals("human") && !choice.equals("computer"));
    	 
    	 if(choice.equals("human")) {
    		 IsolationPlayer isolationPlayer = new IsolationPlayer(actions, display, board, playerNumber);
    		 player = new HumanPlayer(isolationPlayer, scanner);
         } else {
             do {
                 System.out.print("Minimax only or minimax with alpha-beta pruning? [MINIMAX, ALPHA-BETA]: ");
                 choice = scanner.next().toLowerCase();
             } while(!choice.equals("minimax") && !choice.equals("alpha-beta"));
             if(choice.equals("minimax")) {
                 player = new MinimaxSearchPlayer(actions, display, board, playerNumber);
                 ((IsolationPlayer) player).setDepthLimit(DEPTH_LIMIT);
             } else {
                player = new MinimaxSearchWithABPPlayer(actions, display, board, playerNumber);
                ((IsolationPlayer) player).setDepthLimit(DEPTH_LIMIT);
             }
         }
    	return player;
    }
}