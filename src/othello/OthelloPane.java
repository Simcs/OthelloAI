package othello;

import static othello.OthelloStone.*;

import java.awt.Point;
import java.util.*;

public class OthelloPane {
	
	public static final int LINE_NUM = 8;
	
	private OthelloStone[][] mPane;
	private boolean isBlackTurn;
	private OthelloPane previous;
	
	public OthelloPane() {
		mPane = new OthelloStone[LINE_NUM][LINE_NUM];
		for(int i=0; i<mPane.length; i++)
			for(int j=0; j<mPane[i].length; j++)
				mPane[i][j] = EMPTY;
		
		mPane[3][3] = WHITE;
		mPane[3][4] = BLACK;
		mPane[4][4] = WHITE;
		mPane[4][3] = BLACK;
		
		isBlackTurn = true;
		previous = null;
		
		findAvailablePoint();
	}
	
	public OthelloPane(OthelloPane othello) {
		mPane = new OthelloStone[LINE_NUM][LINE_NUM];
		for(int i=0; i<mPane.length; i++)
			for(int j=0; j<mPane[i].length; j++)
				mPane[i][j] = othello.mPane[i][j];
		isBlackTurn = othello.isBlackTurn;
		previous = othello.previous;
	}
	
	public boolean isBlackTurn() {
		return isBlackTurn;
	}
	
	public void turnBlackTurn() {
		isBlackTurn = !isBlackTurn;
	}
	
	public void setPrevious(OthelloPane previous) {
		this.previous = new OthelloPane(previous);
	}
	
	public OthelloPane getPrevious() {
		return previous;
	}
	
	public OthelloStone getStone(int r, int c) {
		if(!isValid(r, c))
			throw new IllegalArgumentException("유효하지 않은 위치입니다");
		return mPane[r][c];
	}
	
	public void putStone(Point p) {
		if(p == null) return;
		putStone(p.x, p.y);
	}
	
	public void putStone(int r, int c) {
		if(!isValid(r, c) || mPane[r][c] != AVAILABLE)
			throw new IllegalArgumentException("놓을 수 없는 자리입니다!");
		
		mPane[r][c] = isBlackTurn ? BLACK : WHITE;
		turnStone(r, c);
		turnBlackTurn();
		findAvailablePoint();
	}
	
	public boolean canContinue() {
		for(OthelloStone[] arr : mPane)
			for(OthelloStone stone : arr)
				if(stone == AVAILABLE)
					return true;
		return false;
	}
	
	public void findAvailablePoint() {
		removeAvailable();
		for(int i=0; i<mPane.length; i++)
			for(int j=0; j<mPane[i].length; j++)
				if(isPlaceable(i, j))
					mPane[i][j] = AVAILABLE;
	}
	
	private void removeAvailable() {
		for(int i=0; i<mPane.length; i++) 
			for(int j=0; j<mPane[i].length; j++)
				if(!mPane[i][j].isStone())
					mPane[i][j] = EMPTY;
	}
	
	public Result result() {
		int black=0, white=0;
		for(OthelloStone[] arr : mPane) {
			for(OthelloStone stone : arr) {
				if(stone == BLACK) {
					black++;
				} else if(stone == WHITE) {
					white++;
				}
			}	
		}
		if(black == white) return Result.DRAW;
		return black > white ? Result.WIN : Result.LOSE;
	}
	
	private static boolean isValid(int r, int c) {
		return r<LINE_NUM && r>=0 && c<LINE_NUM && c>=0;
	}
	
	private void turnStone(int r, int c) {
		for(Direction dir : Direction.values()) {
			if(isPlaceable(r, c, dir)) {
				OthelloStone stone = isBlackTurn ? BLACK : WHITE;
				for(Point p : getStonesWithInterest(r, c, dir))
					mPane[p.x][p.y] = stone;
			}
		}
	}
	
	private boolean isPlaceable(int r, int c) {
		if(mPane[r][c].isStone())
			return false;
		for(Direction dir : Direction.values())
			if(isPlaceable(r, c, dir))
				return true;
		return false;
	}
	
	private boolean isPlaceable(int r, int c, Direction dir) {
		List<Point> stonesWithInterest = getStonesWithInterest(r, c, dir);
		if(stonesWithInterest.size() == 0)
			return false;
		
		OthelloStone stone = isBlackTurn ? BLACK : WHITE;
		Point last = stonesWithInterest.get(stonesWithInterest.size()-1);
		return stonesWithInterest.size() >= 2 && mPane[last.x][last.y] == stone;
	}
	
	private List<Point> getStonesWithInterest(int r, int c, Direction dir) {
		OthelloStone stone = isBlackTurn ? BLACK : WHITE;
		List<Point> stonesWithInterest = new ArrayList<>();
		
		switch(dir) {
		case NORTH :
			while(isValid(--r, c) && stone.isOppositeTo(mPane[r][c]))
				stonesWithInterest.add(new Point(r, c));
			break;
		case NORTH_EAST :
			while(isValid(--r, ++c) && stone.isOppositeTo(mPane[r][c]))
				stonesWithInterest.add(new Point(r, c));
			break;
		case EAST :
			while(isValid(r, ++c) && stone.isOppositeTo(mPane[r][c]))
				stonesWithInterest.add(new Point(r, c));
			break;
		case SOUTH_EAST :
			while(isValid(++r, ++c) && stone.isOppositeTo(mPane[r][c]))
				stonesWithInterest.add(new Point(r, c));
			break;
		case SOUTH :
			while(isValid(++r, c) && stone.isOppositeTo(mPane[r][c]))
				stonesWithInterest.add(new Point(r, c));
			break;
		case SOUTH_WEST :
			while(isValid(++r, --c) && stone.isOppositeTo(mPane[r][c]))
				stonesWithInterest.add(new Point(r, c));
			break;
		case WEST :
			while(isValid(r, --c) && stone.isOppositeTo(mPane[r][c]))
				stonesWithInterest.add(new Point(r, c));
			break;
		case NORTH_WEST :
			while(isValid(--r, --c) && stone.isOppositeTo(mPane[r][c]))
				stonesWithInterest.add(new Point(r, c));
			break;
		}
		
		if(isValid(r, c))
			stonesWithInterest.add(new Point(r, c));
		return stonesWithInterest;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append((isBlackTurn ? "흑":"백")+" 차례\n");
		sb.append("    0 1 2 3 4 5 6 7\n");
		for(int i=0; i<mPane.length; i++) {
			sb.append(" "+i);
			for(int j=0; j<mPane[i].length; j++)
				sb.append(" "+mPane[i][j]);
			sb.append("\n");
		}
		return sb.toString();
	}
}
