package isolation.grid;

import gridgames.data.action.Action;
import gridgames.grid.Board;
import gridgames.grid.Cell;
import gridgames.player.Player;
import isolation.action.IsolationMoveAction;
import isolation.player.IsolationPlayer;

public class IsolationBoard extends Board {
	
	private IsolationPlayer isolationPlayer1;
	private IsolationPlayer isolationPlayer2;

	public IsolationBoard(int numRows, int numCols) {
		super(numRows, numCols);
		for(int row=0; row<numRows; row++) {
			for(int col=0; col<numCols; col++) {
				setCell(row, col, new IsolationCell(row, col));
			}
		}
	}
	
	public void placePlayers(IsolationPlayer isolationPlayer1, IsolationPlayer isolationPlayer2) {
		if(isolationPlayer1 != null && isolationPlayer2 != null) {
			this.isolationPlayer1 = isolationPlayer1;
			this.isolationPlayer2 = isolationPlayer2;
			
			Cell player1Cell = getCell(0,3);
			this.isolationPlayer1.setCell(player1Cell);
			player1Cell.add(this.isolationPlayer1);
			player1Cell.setVisited(true);
			
			Cell player2Cell = getCell(6,3);
			this.isolationPlayer2.setCell(player2Cell);
			player2Cell.add(this.isolationPlayer2);
			player2Cell.setVisited(true);
		}
	}
	
	public IsolationPlayer getIsolationPlayer1() {
		return this.isolationPlayer1;
	}
	
	public IsolationPlayer getIsolationPlayer2() {
		return this.isolationPlayer2;
	}
	
	public Cell getPlayer1Cell() {
		return this.isolationPlayer1.getCell();
	}
	
	public Cell getPlayer2Cell() {
		return this.isolationPlayer2.getCell();
	}
	
	public Cell getPlayerCell(IsolationPlayer player) {
		if(player == isolationPlayer1) {
			return getPlayer1Cell();
		} else {
			return getPlayer2Cell();
		}
	}
	
	public IsolationPlayer getOtherPlayer(Player player) {
		if(player == isolationPlayer1) {
			return isolationPlayer2;
		} else {
			return isolationPlayer1;
		}
	}
	
	public boolean isValidAction(IsolationPlayer player, IsolationMoveAction direction) {
		Cell playerStartCell;
		int playerCellRow;
		int playerCellCol;
		
		playerStartCell = player.getCell();
		playerCellRow = playerStartCell.getRow();
		playerCellCol = playerStartCell.getCol();
		
		//check distance
		if(IsolationMoveAction.getNorthDirections().contains(direction)) {
			if(playerCellRow > 0) {
				playerCellRow--;
			} else {
				return false;
			}
		}
		if(IsolationMoveAction.getEastDirections().contains(direction)) {
			if(playerCellCol < 6) {
				playerCellCol++;
			} else {
				return false;
			}
		}
		if(IsolationMoveAction.getSouthDirections().contains(direction)) {
			if(playerCellRow < 6) {
				playerCellRow++;
			} else {
				return false;
			}
		}
		if(IsolationMoveAction.getWestDirections().contains(direction)) {
			if(playerCellCol > 0) {
				playerCellCol--;
			} else {
				return false;
			}
		}
		
		//check for visited cell
		return !getCell(playerCellRow, playerCellCol).wasVisited();
	}
	
	public void movePlayer(IsolationPlayer player, Action direction) {
		Cell playerStartCell;
		Cell playerEndCell;
		int playerCellRow;
		int playerCellCol;
		
		playerStartCell = player.getCell();
		playerCellRow = playerStartCell.getRow();
		playerCellCol = playerStartCell.getCol();
		
		if(IsolationMoveAction.getNorthDirections().contains(direction)) {
			playerCellRow--;
		}
		if(IsolationMoveAction.getEastDirections().contains(direction)) {
			playerCellCol++;
		}
		if(IsolationMoveAction.getSouthDirections().contains(direction)) {
			playerCellRow++;
		}
		if(IsolationMoveAction.getWestDirections().contains(direction)) {
			playerCellCol--;
		}
		
		//update board
		playerStartCell.removeAll();
		playerEndCell = getCell(playerCellRow, playerCellCol);
		playerEndCell.add(player);
		playerEndCell.setVisited(true);
		
		//update player
		player.setCell(playerEndCell);
	}
	
	public boolean isPlayerIsolated(IsolationPlayer player) {
		Cell playerCell;
		int playerCellRow;
		int playerCellCol;
		
		playerCell = player.getCell();
		playerCellRow = playerCell.getRow();
		playerCellCol = playerCell.getCol();
		
		//if N cell is free
		if(playerCellRow > 0 && !getCell(playerCellRow-1, playerCellCol).wasVisited()) {
			return false;
		}
		//if NE cell is free
		if(playerCellRow > 0 && playerCellCol < 6 && !getCell(playerCellRow-1, playerCellCol+1).wasVisited()) {
			return false;
		}
		//if E cell is free
		if(playerCellCol < 6 && !getCell(playerCellRow, playerCellCol+1).wasVisited()) {
			return false;
		}
		//if SE cell is free
		if(playerCellCol < 6 && playerCellRow < 6 && !getCell(playerCellRow+1, playerCellCol+1).wasVisited()) {
			return false;
		}
		//if S cell is free
		if(playerCellRow < 6 && !getCell(playerCellRow+1, playerCellCol).wasVisited()) {
			return false;
		}
		//if SW cell is free
		if(playerCellRow < 6 && playerCellCol > 0 && !getCell(playerCellRow+1, playerCellCol-1).wasVisited()) {
			return false;
		}
		//if W cell is free
		if(playerCellCol > 0 && !getCell(playerCellRow, playerCellCol-1).wasVisited()) {
			return false;
		}
		//if NW cell is free
		if(playerCellCol > 0 && playerCellRow > 0 && !getCell(playerCellRow-1, playerCellCol-1).wasVisited()) {
			return false;
		}
		return true;
	}
	
	@Override
    public Object clone() {
		IsolationBoard b = new IsolationBoard(numRows, numCols);
		b.isolationPlayer1 = this.isolationPlayer1;
        b.isolationPlayer2 = this.isolationPlayer2;
		
        for(int i=0; i<numRows; i++) {
            for(int j=0; j<numCols; j++) {
                b.cells[i][j].addAll(this.cells[i][j].getItems());
                if(this.cells[i][j].wasVisited()) {
                	b.cells[i][j].setVisited(true);
                }
                /*
                if(b.cells[i][j].contains(b.isolationPlayer1)) {
                	b.isolationPlayer1.setCell(b.cells[i][j]);
                } else if(b.cells[i][j].contains(b.isolationPlayer2)) {
                	b.isolationPlayer2.setCell(b.cells[i][j]);
                }
                */
            }
        }
        return b;
    }
}
