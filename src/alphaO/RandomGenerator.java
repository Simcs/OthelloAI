package alphaO;

import java.awt.Point;
import java.util.*;

import othello.OthelloPane;

import static othello.OthelloStone.*;
import static othello.OthelloPane.*;


public class RandomGenerator implements AlphaO {

	@Override
	public Point getMove(OthelloPane othello) {
		try {
			Thread.sleep(1000);
		} catch(InterruptedException e) {}
		
		List<Point> availablePoint = new ArrayList<>();
		for(int i=0; i<LINE_NUM; i++)
			for(int j=0; j<LINE_NUM; j++)
				if(othello.getStone(i, j) == AVAILABLE)
					availablePoint.add(new Point(i, j));
		
		return availablePoint.size() == 0 ? null :
			availablePoint.get((int)(Math.random()*availablePoint.size()));
	}
}
