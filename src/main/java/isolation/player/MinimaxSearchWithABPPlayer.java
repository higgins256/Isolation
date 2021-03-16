package isolation.player;

import java.util.ArrayList;
import java.util.List;

import gridgames.data.action.Action;
import gridgames.display.Display;
import isolation.action.IsolationMove;
import isolation.action.IsolationMoveAction;
import isolation.grid.IsolationBoard;

public class MinimaxSearchWithABPPlayer extends MinimaxSearchPlayer {

	public MinimaxSearchWithABPPlayer(List<Action> actions, Display display, IsolationBoard board, int playerNumber) {
		super(actions, display, board, playerNumber);
	}

	protected void findMaxMove(int currentDepth, IsolationMove incomingMove) {
		findMaxMove(currentDepth, incomingMove, Integer.MIN_VALUE);
	}

	private void findMaxMove(int currentDepth, IsolationMove incomingMove, int highestAchievableMinimaxValue) {
		List<IsolationMove> minNodeMoves = getPossibleMoves(this);
		IsolationMove hold;
		IsolationMoveAction temp;
		if(currentDepth < getDepthLimit())
		{
			for(int i = 0; i < minNodeMoves.size(); i++)
			{
				movePlayer(this,minNodeMoves.get(i).getMove());
				findMinMove(currentDepth, minNodeMoves.get(i), highestAchievableMinimaxValue);
				undoMovePlayer(this, minNodeMoves.get(i).getMove());
				if(minNodeMoves.get(i).getMinimaxValue() > highestAchievableMinimaxValue)
				{
					highestAchievableMinimaxValue = minNodeMoves.get(i).getMinimaxValue();
				}
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

	protected void findMinMove(int currentDepth, IsolationMove incomingMove, int highestAchievableMinimaxValue) {
		IsolationMoveAction temp;
		IsolationMove hold;
		IsolationPlayer other = getSavedBoard().getOtherPlayer(this);
		List<IsolationMove> maxNodeMoves = getPossibleMoves(other);
		if(currentDepth < getDepthLimit())
		{
			if(maxNodeMoves.size() > 0) {
				int i = 0;
				hold = maxNodeMoves.get(0);
				while (i < maxNodeMoves.size() && hold.getMinimaxValue() >= highestAchievableMinimaxValue) {
					temp = maxNodeMoves.get(i).getMove();
					hold = maxNodeMoves.get(i);
					movePlayer(other, temp);
					findMaxMove(currentDepth + 1, maxNodeMoves.get(i), highestAchievableMinimaxValue);
					undoMovePlayer(other, temp);
					i++;
				}
				if(maxNodeMoves.size() > 0) {
					IsolationMove bestMove = maxNodeMoves.get(0);
					for (IsolationMove maxNodeMove : maxNodeMoves) {
						if (maxNodeMove.getMinimaxValue() > bestMove.getMinimaxValue()) {
							bestMove = maxNodeMove;
						}
					}
					highestAchievableMinimaxValue = bestMove.getMinimaxValue();
				}
			}
			passUpMinMinimaxValue(incomingMove, maxNodeMoves);
		}
	}
}
//if (minNodes.get(i) > highestAchievableMinimaxValue)