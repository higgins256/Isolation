package isolation.grid;

import gridgames.grid.Cell;

public class IsolationCell extends Cell {

	public IsolationCell(int row, int col) {
		super(row, col);
	}
	
	@Override
	public String toString() {
		if(getItems().isEmpty() && this.wasVisited()) {
			return "*";
		} else {
			return super.toString();
		}
	}

}
