package isolation.player;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import isolation.action.IsolationMove;
import org.junit.jupiter.api.Test;

import gridgames.data.action.Action;
import gridgames.display.ConsoleDisplay;
import gridgames.grid.Cell;
import isolation.action.IsolationMoveAction;
import isolation.grid.IsolationBoard;

public class MinimaxSearchPlayersTest {
	
	private IsolationPlayer player1;
	private IsolationPlayer player2;
	private IsolationBoard board;
	private ConsoleDisplay display;
	private Method passUpMaxMinimaxValue;
	private Method passUpMinMinimaxValue;

	//MINIMAXSEARCHPLAYER TESTS/////////////////////////////////////////////////////////////////////////////////////////

	private void setUpNonAbpPlayer() {
		board = new IsolationBoard(7, 7);
		display = new ConsoleDisplay();
		player1 = new MinimaxSearchPlayer(IsolationMoveAction.getAllMoveActions(), display, board, 1);
		player2 = new MinimaxSearchPlayer(IsolationMoveAction.getAllMoveActions(), display, board, 2);
		board.placePlayers(player1, player2);

		try {
			passUpMaxMinimaxValue = MinimaxSearchPlayer.class.getDeclaredMethod("passUpMaxMinimaxValue", IsolationMove.class, List.class);
			passUpMaxMinimaxValue.setAccessible(true);
			passUpMinMinimaxValue = MinimaxSearchPlayer.class.getDeclaredMethod("passUpMinMinimaxValue", IsolationMove.class, List.class);
			passUpMinMinimaxValue.setAccessible(true);
		} catch(NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testPassUpMaxMinimaxValue() {
		Random r = new Random();
		int numMoves;
		IsolationMove isolationMove;
		ArrayList<IsolationMove> possibleMoves;
		ArrayList<Action> allIsolationMoveActions;
		int minimaxValue;
		int maxMinimaxValue;
		IsolationMove bestMaxMove = null;
		try {
			for(int i=0; i<5; i++) {
				setUpNonAbpPlayer();
				numMoves = r.nextInt(8) + 1;
				possibleMoves = new ArrayList<>(numMoves);
				allIsolationMoveActions = IsolationMoveAction.getAllMoveActions();
				maxMinimaxValue = Integer.MIN_VALUE;
				while(!allIsolationMoveActions.isEmpty()) {
					isolationMove = new IsolationMove((IsolationMoveAction) allIsolationMoveActions.remove(r.nextInt(allIsolationMoveActions.size())));
					minimaxValue = r.nextInt(100);
					isolationMove.setMinimaxValue(minimaxValue);
					possibleMoves.add(isolationMove);
					if(minimaxValue > maxMinimaxValue) {
						maxMinimaxValue = minimaxValue;
						bestMaxMove = isolationMove;
					}
				}
				isolationMove = new IsolationMove();
				passUpMaxMinimaxValue.invoke(player1, isolationMove, possibleMoves);
				assertEquals(isolationMove.getMinimaxValue(), bestMaxMove.getMinimaxValue(), "setMaxIsolationMove is not choosing the first move found with the largest minimax value");
			}
		} catch(Exception e) {
			e.printStackTrace();
			fail("check the console for the exception stack trace");
		}
	}

	@Test
	public void testPassUpMinMinimaxValue() {
		Random r = new Random();
		int numMoves;
		IsolationMove isolationMove;
		ArrayList<IsolationMove> possibleMoves;
		ArrayList<Action> allIsolationMoveActions;
		int minimaxValue;
		int minMinimaxValue;
		IsolationMove bestMinMove = null;
		try {
			for(int i=0; i<5; i++) {
				setUpNonAbpPlayer();
				numMoves = r.nextInt(8) + 1;
				possibleMoves = new ArrayList<>(numMoves);
				allIsolationMoveActions = IsolationMoveAction.getAllMoveActions();
				minMinimaxValue = Integer.MAX_VALUE;
				while(!allIsolationMoveActions.isEmpty()) {
					isolationMove = new IsolationMove((IsolationMoveAction) allIsolationMoveActions.remove(r.nextInt(allIsolationMoveActions.size())));
					minimaxValue = r.nextInt(100);
					isolationMove.setMinimaxValue(minimaxValue);
					possibleMoves.add(isolationMove);
					if(minimaxValue < minMinimaxValue) {
						minMinimaxValue = minimaxValue;
						bestMinMove = isolationMove;
					}
				}
				isolationMove = new IsolationMove();
				passUpMinMinimaxValue.invoke(player1, isolationMove, possibleMoves);
				assertEquals(isolationMove.getMinimaxValue(), bestMinMove.getMinimaxValue(), "setMinIsolationMove is not choosing the first move found with the smallest minimax value");
			}
		} catch(Exception e) {
			e.printStackTrace();
			fail("check the console for the exception stack trace");
		}
	}

	@Test
	public void testOnePossibleMove() {
		List<Cell> emptyCells;
		Action action;
		Action expectedAction;
		try {
			setUpNonAbpPlayer();
			//test from inner cell
			for(int i=0; i<10; i++) {
				emptyCells = placePlayerInInnerCell(player1, 1);
				action = player1.getAction();
				expectedAction = getEmptyCellNeighborMoveAction(player1, emptyCells.get(0));
				assertEquals(expectedAction, action, "not correctly returning move to only unvisited neighbor cell from inner cell");
			}
			
			//test from outer cell
			for(int i=0; i<10; i++) {
				emptyCells = placePlayerInOuterCell(player1, 1);
				action = player1.getAction();
				expectedAction = getEmptyCellNeighborMoveAction(player1, emptyCells.get(0));
				assertEquals(expectedAction, action, "not correctly returning move to only unvisited neighbor cell from outer (non-corner) cell");
			}
			
			//test from corner cell
			for(int i=0; i<10; i++) {
				emptyCells = placePlayerInCornerCell(player1, 1);
				action = player1.getAction();
				expectedAction = getEmptyCellNeighborMoveAction(player1, emptyCells.get(0));
				assertEquals(expectedAction, action, "not correctly returning move to only unvisited neighbor cell from corner cell");
			}			
		} catch(Exception e) {
			e.printStackTrace();
			fail("check the console for the exception stack trace");
		}
	}
	
	@Test
	public void testBestMoveOneLevelDeep() {
		Action action;
		try {
			setUpNonAbpPlayer();
			setupDepthLevelTest(player1, player2);
			action = player1.getAction();
			assertNotNull(action, "not returning a move after 1 search level");
			assertEquals(IsolationMoveAction.SW, action, "not correctly returning move to cell allowing greater number of moves identifiable after 1 search level");
			assertEquals(14, player1.getNumStatesChecked(), "not computing the minimax value for the correct number of states");
		} catch(Exception e) {
			e.printStackTrace();
			fail("check the console for the exception stack trace");
		}
	}
	
	@Test
	public void testBestMoveTwoLevelsDeep() {
		Action action;
		try {
			setUpNonAbpPlayer();
			player1.setDepthLimit(2);
			setupDepthLevelTest(player1, player2);
			action = player1.getAction();
			assertNotNull(action, "not returning a move after 2 search levels");
			assertEquals(IsolationMoveAction.SW, action, "not correctly returning move to cell allowing greater number of moves identifiable after 2 search levels");
			assertEquals(33, player1.getNumStatesChecked(), "not computing the minimax value for the correct number of states");
		} catch(Exception e) {
			e.printStackTrace();
			fail("check the console for the exception stack trace");
		}
	}
	
	@Test
	public void testBestMoveThreeLevelsDeep() {
		Action action;
		try {
			setUpNonAbpPlayer();
			player1.setDepthLimit(3);
			setupDepthLevelTest(player1, player2);
			action = player1.getAction();
			assertNotNull(action, "not returning a move after 3 search levels");
			assertEquals(IsolationMoveAction.SW, action, "not correctly returning move to cell allowing greater number of moves identifiable after 3 search levels");
			assertEquals(18, player1.getNumStatesChecked(), "not computing the minimax value for the correct number of states");
		} catch(Exception e) {
			e.printStackTrace();
			fail("check the console for the exception stack trace");
		}
	}

	//MINIMAXSEARCHWITHABPPLAYER TESTS//////////////////////////////////////////////////////////////////////////////////

	private void setUpAbpPlayer() {
		board = new IsolationBoard(7, 7);
		display = new ConsoleDisplay();
		player1 = new MinimaxSearchWithABPPlayer(IsolationMoveAction.getAllMoveActions(), display, board, 1);
		player2 = new MinimaxSearchWithABPPlayer(IsolationMoveAction.getAllMoveActions(), display, board, 2);
		board.placePlayers(player1, player2);
	}

	@Test
	public void testOnePossibleMoveForAbpPlayer() {
		List<Cell> emptyCells;
		Action action;
		Action expectedAction;
		try {
			setUpAbpPlayer();
			//test from inner cell
			for(int i=0; i<10; i++) {
				emptyCells = placePlayerInInnerCell(player1, 1);
				action = player1.getAction();
				expectedAction = getEmptyCellNeighborMoveAction(player1, emptyCells.get(0));
				assertEquals(expectedAction, action, "not correctly returning move to only unvisited neighbor cell from inner cell");
			}

			//test from outer cell
			for(int i=0; i<10; i++) {
				emptyCells = placePlayerInOuterCell(player1, 1);
				action = player1.getAction();
				expectedAction = getEmptyCellNeighborMoveAction(player1, emptyCells.get(0));
				assertEquals(expectedAction, action, "not correctly returning move to only unvisited neighbor cell from outer (non-corner) cell");
			}

			//test from corner cell
			for(int i=0; i<10; i++) {
				emptyCells = placePlayerInCornerCell(player1, 1);
				action = player1.getAction();
				expectedAction = getEmptyCellNeighborMoveAction(player1, emptyCells.get(0));
				assertEquals(expectedAction, action, "not correctly returning move to only unvisited neighbor cell from corner cell");
			}
		} catch(Exception e) {
			e.printStackTrace();
			fail("check the console for the exception stack trace");
		}
	}

	@Test
	public void testBestMoveOneAndTwoLevelsDeepForAbpPlayer() {
		Action action;
		try {
			setUpAbpPlayer();
			setupDepthLevelTest(player1, player2);
			action = player1.getAction();
			assertNotNull(action, "not returning a move after 1 search level");
			assertEquals(IsolationMoveAction.SW, action, "not correctly returning move to cell allowing greater number of moves identifiable after 1 search level");
			assertEquals(10, player1.getNumStatesChecked(), "not computing the minimax value for the correct number of states");

			setUpAbpPlayer();
			player1.setDepthLimit(2);
			setupDepthLevelTest(player1, player2);
			action = player1.getAction();
			assertNotNull(action, "not returning a move after 2 search levels");
			assertEquals(IsolationMoveAction.SW, action, "not correctly returning move to cell allowing greater number of moves identifiable after 2 search levels");
			assertEquals(28, player1.getNumStatesChecked(), "not computing the minimax value for the correct number of states");
		} catch(Exception e) {
			e.printStackTrace();
			fail("check the console for the exception stack trace");
		}
	}
	
	private List<Cell> placePlayerInInnerCell(IsolationPlayer player, int numEmptyNeighbors) {
		ArrayList<Cell> emptyCells = new ArrayList<Cell>();
		Random r = new Random();
		int row = r.nextInt(5)+1;
		int col = r.nextInt(5)+1;
		Cell randomInnerCell = board.getCell(row, col);
		int numNeighborsEmptied = 0;
		int rRow;
		int rCol;
		Cell c;
		
		//move player
		removePlayer(player);
		randomInnerCell.add(player);
		player.setCell(randomInnerCell);
		
		//set all neighbors and occupied cell as visited
		for(int i=row-1; i<=row+1; i++) {
			for(int j=col-1; j<=col+1; j++) {
				board.getCell(i,j).setVisited(true);
			}
		}
		
		//unvisit desired number of neighbors
		while(numNeighborsEmptied < numEmptyNeighbors) {
			rRow = r.nextInt(3)+(row-1);
			rCol = r.nextInt(3)+(col-1);
			c = board.getCell(rRow, rCol);
			if(c.wasVisited() && !c.contains(player)) {
				c.setVisited(false);
				emptyCells.add(c);
				numNeighborsEmptied++;
			}
		}
		return emptyCells;
	}
	
	private List<Cell> placePlayerInOuterCell(IsolationPlayer player, int numEmptyNeighbors) {
		ArrayList<Cell> emptyCells = new ArrayList<Cell>();
		Random r = new Random();
		int row = r.nextInt(2)*6;
		int col = r.nextInt(5)+1;
		Cell randomOuterCell = board.getCell(row, col);
		int numNeighborsEmptied = 0;
		int rRow;
		int rCol;
		Cell c;
		
		//move player
		removePlayer(player);
		randomOuterCell.add(player);
		player.setCell(randomOuterCell);

		//set all neighbors and occupied cell as visited
		for(int i=row-1; i<=row+1; i++) {
			for(int j=col-1; j<=col+1; j++) {
				if(i>=0 && i<=6 && j>=0 && j<=6) {
					board.getCell(i,j).setVisited(true);
				}
			}
		}
		
		//unvisit desired number of neighbors
		while(numNeighborsEmptied < numEmptyNeighbors) {
			rRow = (row==0)?r.nextInt(2):(r.nextInt(2)+5);
			rCol = r.nextInt(3)+(col-1);
			c = board.getCell(rRow, rCol);
			if(c.wasVisited() && !c.contains(player)) {
				c.setVisited(false);
				emptyCells.add(c);
				numNeighborsEmptied++;
			}
		}
		return emptyCells;
	}
	
	private List<Cell> placePlayerInCornerCell(IsolationPlayer player, int numEmptyNeighbors) {
		ArrayList<Cell> emptyCells = new ArrayList<Cell>();
		Random r = new Random();
		int row = r.nextInt(2)*6;
		int col = r.nextInt(2)*6;
		Cell randomCornerCell = board.getCell(row, col);
		int numNeighborsEmptied = 0;
		int rRow;
		int rCol;
		Cell c;
		
		//move player
		removePlayer(player);
		randomCornerCell.add(player);
		player.setCell(randomCornerCell);
		
		//set all neighbors and occupied cell as visited
		for(int i=row-1; i<=row+1; i++) {
			for(int j=col-1; j<=col+1; j++) {
				if(i>=0 && i<=6 && j>=0 && j<=6) {
					board.getCell(i,j).setVisited(true);
				}
			}
		}
		
		//unvisit desired number of neighbors
		while(numNeighborsEmptied < numEmptyNeighbors) {
			rRow = (row==0)?r.nextInt(2):(r.nextInt(2)+5);
			rCol = (col==0)?r.nextInt(2):(r.nextInt(2)+5);
			c = board.getCell(rRow, rCol);
			if(c.wasVisited() && !c.contains(player)) {
				c.setVisited(false);
				emptyCells.add(c);
				numNeighborsEmptied++;
			}
		}
		return emptyCells;
	}
	
	private void setupDepthLevelTest(IsolationPlayer player, IsolationPlayer otherPlayer) {
		Cell playerCell = board.getCell(2, 3);
		Cell otherPlayerCell = board.getCell(4, 3);
		ArrayList<Cell> unvisitedSquares = new ArrayList<Cell>();
		Cell c;
		
		//identify unvisited cells
		unvisitedSquares.add(board.getCell(1,2));
		unvisitedSquares.add(board.getCell(2,2));
		unvisitedSquares.add(board.getCell(2,6));
		unvisitedSquares.add(board.getCell(3,2));
		unvisitedSquares.add(board.getCell(3,4));
		unvisitedSquares.add(board.getCell(3,6));
		unvisitedSquares.add(board.getCell(4,1));
		unvisitedSquares.add(board.getCell(4,2));
		unvisitedSquares.add(board.getCell(4,4));
		unvisitedSquares.add(board.getCell(4,6));
		unvisitedSquares.add(board.getCell(5,5));
		
		//move players
		removePlayer(player);
		playerCell.add(player);
		player.setCell(playerCell);
		removePlayer(otherPlayer);
		otherPlayerCell.add(otherPlayer);
		otherPlayer.setCell(otherPlayerCell);
		
		//set desired squares as visited
		for(int i=0; i<7; i++) {
			for(int j=0; j<7; j++) {
				c = board.getCell(i,j);
				if(!unvisitedSquares.contains(c)) {
					board.getCell(i,j).setVisited(true);
				}
			}
		}
	}
	
	private void setupPruningTest(IsolationPlayer player, IsolationPlayer otherPlayer) {
		Cell playerCell = board.getCell(3, 3);
		Cell otherPlayerCell = board.getCell(3, 6);
		ArrayList<Cell> unvisitedSquares = new ArrayList<Cell>();
		Cell c;
		
		//identify unvisited cells
		unvisitedSquares.add(board.getCell(1,2));
		unvisitedSquares.add(board.getCell(1,3));
		unvisitedSquares.add(board.getCell(2,3));
		unvisitedSquares.add(board.getCell(3,1));
		unvisitedSquares.add(board.getCell(3,4));
		unvisitedSquares.add(board.getCell(3,5));
		unvisitedSquares.add(board.getCell(4,2));
		unvisitedSquares.add(board.getCell(4,4));
		unvisitedSquares.add(board.getCell(4,5));
		
		//move players
		removePlayer(player);
		playerCell.add(player);
		player.setCell(playerCell);
		removePlayer(otherPlayer);
		otherPlayerCell.add(otherPlayer);
		otherPlayer.setCell(otherPlayerCell);
		
		//set desired squares as visited
		for(int i=0; i<7; i++) {
			for(int j=0; j<7; j++) {
				c = board.getCell(i,j);
				if(!unvisitedSquares.contains(c)) {
					board.getCell(i,j).setVisited(true);
				}
			}
		}
	}
	
	private void removePlayer(IsolationPlayer player) {
		Cell playerCell = player.getCell();
		playerCell.remove(player);
	}
	
	private Action getEmptyCellNeighborMoveAction(IsolationPlayer player, Cell neighbor) {
		Cell currentCell = board.getPlayerCell(player);
		int currentRow = currentCell.getRow();
		int currentCol = currentCell.getCol();
		int neighborRow = neighbor.getRow();
		int neighborCol = neighbor.getCol();
		
		if(neighborRow < currentRow && neighborCol == currentCol) {
			return IsolationMoveAction.N;
		} else if(neighborRow < currentRow && neighborCol > currentCol) {
			return IsolationMoveAction.NE;
		} else if(neighborCol > currentCol && neighborRow== currentRow) {
			return IsolationMoveAction.E;
		}  else if(neighborCol > currentCol && neighborRow > currentRow) {
			return IsolationMoveAction.SE;
		}  else if(neighborRow > currentRow && neighborCol == currentCol) {
			return IsolationMoveAction.S;
		} else if(neighborRow > currentRow && neighborCol < currentCol) {
			return IsolationMoveAction.SW;
		} else if(neighborCol < currentCol && neighborRow == currentRow) {
			return IsolationMoveAction.W;
		} else if(neighborCol < currentCol && neighborRow < currentRow) {
			return IsolationMoveAction.NW;
		} else {
			return null;
		}
	}
}
