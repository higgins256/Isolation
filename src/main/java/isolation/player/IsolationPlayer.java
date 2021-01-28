package isolation.player;

import java.util.ArrayList;
import java.util.List;

import gridgames.data.action.Action;
import gridgames.data.item.Item;
import gridgames.display.Display;
import gridgames.grid.Board;
import gridgames.grid.Cell;
import gridgames.player.Player;
import isolation.action.IsolationMove;
import isolation.action.IsolationMoveAction;
import isolation.grid.IsolationBoard;

public class IsolationPlayer extends Player implements Item {
	
	private IsolationBoard board;
	private int playerNumber;
	private int depthLimit;
	private int numStatesChecked;

	public IsolationPlayer(List<Action> actions, Display display, IsolationBoard board, int playerNumber) {
		super(actions, display);
		this.board = board;
		display.setBoard(this.board);
		this.playerNumber = playerNumber;
		this.depthLimit = 1;
	}
	
	public final void setDepthLimit(int depthLimit) {
		this.depthLimit = depthLimit;
	}
	
	public int getDepthLimit() {
		return this.depthLimit;
	}
	
	public Board getBoard() {
		return this.board;
	}
	
	public void setBoard(IsolationBoard board) {
		this.board = board;
	}
	
	public int getNumStatesChecked() {
		return this.numStatesChecked;
	}
	
	public void incrementNumStatesChecked() {
		this.numStatesChecked++;
	}
	
	protected final int getMinimaxValue() {
		IsolationBoard board = (IsolationBoard) getBoard();
		IsolationPlayer otherPlayer = board.getOtherPlayer(this);
		int numPossiblePlayerMoves = getNumPossibleMoves(this);
		int numPossibleOpponentMoves = getNumPossibleMoves(otherPlayer);
		this.incrementNumStatesChecked();
		return numPossiblePlayerMoves - 3*numPossibleOpponentMoves;
	}
	
	protected int getNumPossibleMoves(IsolationPlayer player) {
		return getEmptyCellNeighbors(player).size();
	}
	
	protected List<Cell> getEmptyCellNeighbors(IsolationPlayer player) {
		ArrayList<Cell> neighbors = new ArrayList<Cell>();
		IsolationBoard board = (IsolationBoard) getBoard();
		Cell currentCell = board.getPlayerCell(player);
		int currentRow = currentCell.getRow();
		int currentCol = currentCell.getCol();
		Cell neighborCell;
		
		//add N cell
		if(currentRow > 0) {
			neighborCell = board.getCell(currentRow-1, currentCol);
			if(!neighborCell.wasVisited()) {
				neighbors.add(neighborCell);
			}
		}
		//add NE cell
		if(currentRow > 0 && currentCol < board.getNumCols()-1) {
			neighborCell = board.getCell(currentRow-1, currentCol+1);
			if(!neighborCell.wasVisited()) {
				neighbors.add(neighborCell);
			}
		}
		//add E cell
		if(currentCol < board.getNumCols()-1) {
			neighborCell =board.getCell(currentRow, currentCol+1);
			if(!neighborCell.wasVisited()) {
				neighbors.add(neighborCell);
			}
		}
		//add SE cell
		if(currentCol < board.getNumCols()-1 && currentRow < board.getNumRows()-1) {
			neighborCell = board.getCell(currentRow+1, currentCol+1);
			if(!neighborCell.wasVisited()) {
				neighbors.add(neighborCell);
			}
		}
		//add S cell
		if(currentRow < board.getNumRows()-1) {
			neighborCell = board.getCell(currentRow+1, currentCol);
			if(!neighborCell.wasVisited()) {
				neighbors.add(neighborCell);
			}
		}
		//add SW cell
		if(currentRow < board.getNumRows()-1 && currentCol > 0) {
			neighborCell = board.getCell(currentRow+1, currentCol-1);
			if(!neighborCell.wasVisited()) {
				neighbors.add(neighborCell);
			}
		}
		//add W cell
		if(currentCol > 0) {
			neighborCell = board.getCell(currentRow, currentCol-1);
			if(!neighborCell.wasVisited()) {
				neighbors.add(neighborCell);
			}
		}
		//add NW cell
		if(currentCol > 0 && currentRow > 0) {
			neighborCell = board.getCell(currentRow-1, currentCol-1);
			if(!neighborCell.wasVisited()) {
				neighbors.add(neighborCell);
			}
		}
		return neighbors;
	}
	
	protected ArrayList<IsolationMove> getPossibleMoves(IsolationPlayer player) {
		ArrayList<IsolationMove> possibleMoves = new ArrayList<IsolationMove>();
		List<Cell> unvisitedNeighbors = getEmptyCellNeighbors(player);
    	for(Cell neighbor: unvisitedNeighbors) {
    		possibleMoves.add(new IsolationMove(getEmptyCellNeighborMoveAction(player, neighbor)));
    	}
    	return possibleMoves;
	}
	
	protected IsolationMoveAction getEmptyCellNeighborMoveAction(IsolationPlayer player, Cell neighbor) {
		IsolationBoard board = (IsolationBoard) getBoard();
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

	//this class will either be extended or wrapped by a HumanPlayer instance 
	@Override
	public Action getAction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isHiddenItem() {
		return false;
	}
	
	@Override
	public String toString() {
		return ""+playerNumber;
	}

}
