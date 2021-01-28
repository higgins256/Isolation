package isolation.action;

import java.util.Objects;

public class IsolationMove {
	
	private IsolationMoveAction move;
	private int minimaxValue;
	
	public IsolationMove() {}
	
	public IsolationMove(IsolationMoveAction move) {
		this.move = move;
	}
	
	public IsolationMoveAction getMove() {
		return this.move;
	}
	
	public void setMove(IsolationMoveAction move) {
		this.move = move;
	}
	
	public int getMinimaxValue() {
		return this.minimaxValue;
	}
	
	public void setMinimaxValue(int minimaxValue) {
		this.minimaxValue = minimaxValue;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if(move != null) {
			sb.append("move: " + move.getDescription() + ", ");
		}
		sb.append("minimaxValue: " + minimaxValue);
		return sb.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof IsolationMove)) return false;
		IsolationMove that = (IsolationMove) o;
		return minimaxValue == that.minimaxValue && move == that.move;
	}
}
