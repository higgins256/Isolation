package isolation.player;

import java.util.ArrayList;
import java.util.List;

import gridgames.data.action.Action;
import gridgames.display.Display;
import gridgames.grid.Cell;
import isolation.action.IsolationMove;
import isolation.action.IsolationMoveAction;
import isolation.grid.IsolationBoard;

public class MinimaxSearchPlayer extends IsolationPlayer {
	
	private IsolationBoard savedBoard;

	public MinimaxSearchPlayer(List<Action> actions, Display display, IsolationBoard board, int playerNumber) {
		super(actions, display, board, playerNumber);
	}
	
	@Override
	public Action getAction() {
    	IsolationMove isolationMove = new IsolationMove();
    	
    	saveState();
		findMaxMove(0, isolationMove);
    	restoreState();
    	incrementNumActionsExecuted();
    	
    	return isolationMove.getMove();
	}
	
	protected void findMaxMove(int currentDepth, IsolationMove incomingMove) {
		List<IsolationMove>minNodeMoves = getPossibleMoves(this);
		if(currentDepth < getDepthLimit())
		{
			for(int i = 0; i < minNodeMoves.size(); i++)
			{
				movePlayer(this,minNodeMoves.get(i).getMove());
				findMinMove(currentDepth, minNodeMoves.get(i));
				undoMovePlayer(this, minNodeMoves.get(i).getMove());
			}
			passUpMaxMinimaxValue(incomingMove,minNodeMoves);
		}
		else
		{
			incomingMove.setMinimaxValue(getMinimaxValue());
		}
		if(minNodeMoves.size() > 0) {
			IsolationMove bestMove = minNodeMoves.get(0);
			for (IsolationMove minNodeMove : minNodeMoves) {
				if (minNodeMove.getMinimaxValue() > bestMove.getMinimaxValue()) {
					bestMove = minNodeMove;
				}
			}
			incomingMove.setMove(bestMove.getMove());
		}
	}
	
	protected void findMinMove(int currentDepth, IsolationMove incomingMove) {
		IsolationMoveAction temp;
		IsolationPlayer other = savedBoard.getOtherPlayer(this);
		List<IsolationMove> maxNodeMoves = getPossibleMoves(other);
		if(currentDepth < getDepthLimit())
		{
			for (IsolationMove maxNodeMove : maxNodeMoves) {
				temp = maxNodeMove.getMove();
				movePlayer(other, temp);
				findMaxMove(currentDepth + 1, maxNodeMove);
				undoMovePlayer(other, temp);
			}
			passUpMinMinimaxValue(incomingMove, maxNodeMoves);
		}
	}
	
	protected void passUpMaxMinimaxValue(IsolationMove incomingMove, List<IsolationMove> possibleMoves) {
		if(possibleMoves == null || possibleMoves.isEmpty())
		{
			incomingMove.setMinimaxValue(Integer.MIN_VALUE);
		}
		else
		{
			IsolationMove bestMove = possibleMoves.get(0);
			for(int i = 0; i < possibleMoves.size(); i++)
			{
				if(possibleMoves.get(i).getMinimaxValue() > bestMove.getMinimaxValue())
				{
					bestMove = possibleMoves.get(i);
				}
			}
			incomingMove.setMinimaxValue(bestMove.getMinimaxValue());
		}
	}
	
	protected void passUpMinMinimaxValue(IsolationMove incomingMove, List<IsolationMove> possibleMoves) {
		if(possibleMoves == null || possibleMoves.isEmpty())
		{
			incomingMove.setMinimaxValue(Integer.MAX_VALUE);
		}
		else
		{
			IsolationMove bestMove = possibleMoves.get(0);
			for(int i = 0; i < possibleMoves.size(); i++)
			{
				if(possibleMoves.get(i).getMinimaxValue() < bestMove.getMinimaxValue())
				{
					bestMove = possibleMoves.get(i);
				}
			}
			incomingMove.setMinimaxValue(bestMove.getMinimaxValue());
		}
	}
	
	protected void saveState() {
		savedBoard = (IsolationBoard)getBoard().clone();
	}

	protected void restoreState() {
		IsolationPlayer player1 = savedBoard.getIsolationPlayer1();
		IsolationPlayer player2 = savedBoard.getIsolationPlayer2();
		IsolationBoard board = (IsolationBoard) getBoard();
		Cell c;
		
		for(int row=0; row<7; row++) {
			for(int col=0; col<7; col++) {
				c = board.getCell(row, col);
				if(savedBoard.getCell(row, col).wasVisited()) {
					c.setVisited(true);
				} else {
					c.setVisited(false);
				}
				if(c.contains(player1)) {
					player1.setCell(c);
				} else if(c.contains(player2)) {
					player2.setCell(c);
				}
			}
		}
	}

	protected void movePlayer(IsolationPlayer player, Action move) {
		IsolationBoard board = (IsolationBoard) getBoard();
		board.movePlayer(player, move);
	}

	protected void undoMovePlayer(IsolationPlayer player, Action move) {
		Action undoMove = IsolationMoveAction.getOppositeAction(move);
		IsolationBoard board = (IsolationBoard) getBoard();
		player.getCell().setVisited(false);
		board.movePlayer(player, undoMove);
	}
}
