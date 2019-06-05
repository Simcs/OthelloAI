package othello;

import java.awt.Point;
import java.util.*;

enum Direction {
	NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST;
}

public class OthelloModel implements OthelloModelInterface {
	
	public static final int LINE_NUM = 8;
	
	private OthelloPane othello;
	private Set<PaneOberserver> paneObservers;
	private Set<StatusObserver> statusObservers;
	
	public OthelloModel() {
		othello = new OthelloPane();
		paneObservers = new HashSet<>();
		statusObservers = new HashSet<>();
	}
	
	@Override
	public boolean isBlackTurn() {
		return othello.isBlackTurn();
	}

	@Override
	public void turnBlackTurn() {
		othello.turnBlackTurn();
	}

	@Override
	public void setPrevious(OthelloPane othello) {
		this.othello.setPrevious(othello);
	}

	@Override
	public void backToPrevious() {
		othello = othello.getPrevious();
		notifyAllObservers();
	}

	@Override
	public boolean hasPrevious() {
		return othello.getPrevious() != null;
	}

	@Override
	public OthelloPane getOthelloPane() {
		return new OthelloPane(othello);
	}

	@Override
	public OthelloStone getStone(int r, int c) throws IllegalArgumentException {
		return othello.getStone(r, c);
	}

	@Override
	public void putStone(Point p) throws IllegalArgumentException {
		othello.putStone(p);
		notifyAllObservers();
	}

	@Override
	public void putStone(int r, int c) throws IllegalArgumentException {
		othello.putStone(r, c);
		notifyAllObservers();
	}

	@Override
	public boolean checkCanContinue() {
		if(!othello.canContinue()) {
			othello.turnBlackTurn();
			othello.findAvailablePoint();
			notifyStatusObservers("놓을 곳이 없습니다");
			notifyPaneObservers();
			
			try {
				Thread.sleep(1000);
			} catch(InterruptedException ie) {}
			
			if(!othello.canContinue()) {
				notifyStatusObservers("게임 오버! "+result());
				return false;
			}
		}
		return true;
	}

	@Override
	public void reset() {
		othello = new OthelloPane();
		notifyAllObservers();
	}

	@Override
	public Result result() {
		return othello.result();
	}

	@Override
	public void registerPaneObserver(PaneOberserver o) {
		paneObservers.add(o);
	}

	@Override
	public void removePaneObserver(PaneOberserver o) {
		if(paneObservers.contains(o))
			paneObservers.remove(o);
	}

	@Override
	public void registerStatusObserver(StatusObserver o) {
		statusObservers.add(o);
	}

	@Override
	public void removeStatusObserver(StatusObserver o) {
		if(statusObservers.contains(o))
			statusObservers.remove(o);
	}
	
	private void notifyPaneObservers() {
		for(PaneOberserver paneObserver : paneObservers)
			paneObserver.updatePane();
	}
	
	private void notifyStatusObservers(String text) {
		for(StatusObserver statusObserver : statusObservers)
			statusObserver.updateStatus(text);
	}
	
	private void notifyAllObservers() {
		notifyPaneObservers();
		notifyStatusObservers(currentState());
	}
	
	private String currentState() {
		return othello.isBlackTurn() ? "흑 차례" : "백 차례";
	}
	
}
