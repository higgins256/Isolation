package isolation.player;

import java.util.ArrayList;
import java.util.List;

import gridgames.data.action.Action;
import gridgames.display.Display;
import isolation.action.IsolationMove;
import isolation.grid.IsolationBoard;

public class MinimaxSearchWithABPPlayer extends MinimaxSearchPlayer {

	public MinimaxSearchWithABPPlayer(List<Action> actions, Display display, IsolationBoard board, int playerNumber) {
		super(actions, display, board, playerNumber);
	}

	protected void findMaxMove(int currentDepth, IsolationMove incomingMove) {
		findMaxMove(currentDepth, incomingMove, Integer.MIN_VALUE);
	}

	private void findMaxMove(int currentDepth, IsolationMove incomingMove, int highestAchievableMinimaxValue) {
		if(currentDepth < getDepthLimit())
		{

		}
	}

	protected void findMinMove(int currentDepth, IsolationMove incomingMove, int highestAchievableMinimaxValue) {
		//TODO: implement if you want
	}
}
