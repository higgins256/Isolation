package isolation.action;

import java.util.ArrayList;
import java.util.Arrays;

import gridgames.data.action.Action;

public enum IsolationMoveAction implements Action {
	N,
    NE,
    E,
    SE,
    S,
    SW,
    W,
    NW;
	
	public static final Action[] ISOLATION_MOVE_ACTIONS = {IsolationMoveAction.N, IsolationMoveAction.NE, IsolationMoveAction.E, IsolationMoveAction.SE, IsolationMoveAction.S, IsolationMoveAction.SW, IsolationMoveAction.W, IsolationMoveAction.NW};
	public static final IsolationMoveAction[] N_DIRECTIONS = {IsolationMoveAction.N, IsolationMoveAction.NE, IsolationMoveAction.NW};
	public static final IsolationMoveAction[] E_DIRECTIONS = {IsolationMoveAction.NE, IsolationMoveAction.E, IsolationMoveAction.SE};
	public static final IsolationMoveAction[] S_DIRECTIONS = {IsolationMoveAction.SE, IsolationMoveAction.S, IsolationMoveAction.SW};
	public static final IsolationMoveAction[] W_DIRECTIONS = {IsolationMoveAction.SW, IsolationMoveAction.W, IsolationMoveAction.NW};


	
	@Override
	public String getDescription() {
		if(IsolationMoveAction.N.equals(this)) {
			return "N";
		} else if(IsolationMoveAction.NE.equals(this)) {
			return "NE";
		} else if(IsolationMoveAction.E.equals(this)) {
			return "E";
		} else if(IsolationMoveAction.SE.equals(this)) {
			return "SE";
		} else if(IsolationMoveAction.S.equals(this)) {
			return "S";
		} else if(IsolationMoveAction.SW.equals(this)) {
			return "SW";
		} else if(IsolationMoveAction.W.equals(this)) {
			return "W";
		} else if(IsolationMoveAction.NW.equals(this)) {
			return "NW";
		} else {
			return "";
		}
	}

	@Override
	public Action getActionFromDescription(String description) {
		if("N".equals(description)) {
			return IsolationMoveAction.N;
		} else if("NE".equals(description)) {
			return IsolationMoveAction.NE;
		} else if("E".equals(description)) {
			return IsolationMoveAction.E;
		} else if("SE".equals(description)) {
			return IsolationMoveAction.SE;
		} else if("S".equals(description)) {
			return IsolationMoveAction.S;
		} else if("SW".equals(description)) {
			return IsolationMoveAction.SW;
		} else if("W".equals(description)) {
			return IsolationMoveAction.W;
		} else if("NW".equals(description)) {
			return IsolationMoveAction.NW;
		} else {
			return null;
		}
	}
	
	public static Action getOppositeAction(Action otherAction) {
		if(IsolationMoveAction.N.equals(otherAction)) {
			return IsolationMoveAction.S;
		} else if(IsolationMoveAction.NE.equals(otherAction)) {
			return IsolationMoveAction.SW;
		} else if(IsolationMoveAction.E.equals(otherAction)) {
			return IsolationMoveAction.W;
		} else if(IsolationMoveAction.SE.equals(otherAction)) {
			return IsolationMoveAction.NW;
		} else if(IsolationMoveAction.S.equals(otherAction)) {
			return IsolationMoveAction.N;
		} else if(IsolationMoveAction.SW.equals(otherAction)) {
			return IsolationMoveAction.NE;
		} else if(IsolationMoveAction.W.equals(otherAction)) {
			return IsolationMoveAction.E;
		} else if(IsolationMoveAction.NW.equals(otherAction)) {
			return IsolationMoveAction.SE;
		} else {
			return null;
		}
	}
	
	public static ArrayList<Action> getAllMoveActions() {
		return new ArrayList<Action>(Arrays.asList(ISOLATION_MOVE_ACTIONS));
	}
	
	public static ArrayList<IsolationMoveAction> getNorthDirections() {
		return new ArrayList<IsolationMoveAction>(Arrays.asList(N_DIRECTIONS));
	}
	
	public static ArrayList<IsolationMoveAction> getEastDirections() {
		return new ArrayList<IsolationMoveAction>(Arrays.asList(E_DIRECTIONS));
	}
	
	public static ArrayList<IsolationMoveAction> getSouthDirections() {
		return new ArrayList<IsolationMoveAction>(Arrays.asList(S_DIRECTIONS));
	}
	
	public static ArrayList<IsolationMoveAction> getWestDirections() {
		return new ArrayList<IsolationMoveAction>(Arrays.asList(W_DIRECTIONS));
	}

}
