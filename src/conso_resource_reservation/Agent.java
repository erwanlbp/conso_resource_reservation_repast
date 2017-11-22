package conso_resource_reservation;

import java.awt.Point;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

public class Agent extends SimpleAgent {

	private Point resourceToGo;
	private boolean hasConsume;

	public Agent() {
		super();
		this.resourceToGo = null;
		this.hasConsume = false;
	}

	protected void move() {
		int width = RunEnvironment.getInstance().getParameters().getInteger("gridWidth");
		int height = RunEnvironment.getInstance().getParameters().getInteger("gridHeight");

		if (resourceToGo == null) {
			// RANDOM MOVE
			int rX = ContextCreator.rand.nextInt(3) - 1;
			int rY = ContextCreator.rand.nextInt(3) - 1;
			this.setPosX((this.getPosX() + rX) % width);
			this.setPosY((this.getPosY() + rY) % height);
		} else {
			// MOVE TO RESOURCE
			int[] newPos = getNextLinePoint(getPosX(), getPosY(), (int) resourceToGo.getX(), (int) resourceToGo.getY());
			System.out.println(this+" wants "+resourceToGo+" => move to ("+newPos[0]+";"+newPos[1]+")");
			this.setPosX(newPos[0]);
			this.setPosY(newPos[1]);
		}
	}

	public void consume(ResourceDiscovered resource) {
		resource.hasBeenConsumed();
		this.hasConsume = true;
	}

	public Point resourceBooked() {
		return this.resourceToGo;
	}

	public void book(ResourceDiscovered rd) {
		rd.book(this);
		this.resourceToGo = new Point(rd.getPosX(), rd.getPosY());
	}
	
	@Override
	public void compute() {
		Iterable<SimpleAgent> agents = ((Grid<SimpleAgent>) ContextUtils.getContext(this).getProjection("grid")).getObjectsAt(this.getPosX(), this.getPosY());
		for (SimpleAgent a : agents) {
			if (a instanceof ResourceHidden) {
				System.out.println(this+" discovered "+a);
				((ResourceHidden) a).hasBeenDiscovered();
			} else if (a instanceof ResourceDiscovered && this.resourceToGo != null	&& this.getPosX() == (int) resourceToGo.getX() && this.getPosY() == (int) resourceToGo.getY()) {
				System.out.println(this+ " consumed "+a);
				this.consume((ResourceDiscovered) a);
			}
		}
	}

	@Override
	public void implement() {
		// Randomly add a resource
		ContextCreator.addRandomResource(this);

		if (resourceToGo != null) {
			if(SharedSpace.getInstance().take(this, getPosX(), getPosY())) {
				this.resourceToGo = null;
			}
		} else {
			SharedSpace.getInstance().bookOne(this);
		}
		// Point 7, 8
		this.move();
	}

	private int[] getNextLinePoint(int x, int y, int x2, int y2) {
		int w = x2 - x;
		int h = y2 - y;
		int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0;
		if (w < 0)
			dx1 = -1;
		else if (w > 0)
			dx1 = 1;
		if (h < 0)
			dy1 = -1;
		else if (h > 0)
			dy1 = 1;
		if (w < 0)
			dx2 = -1;
		else if (w > 0)
			dx2 = 1;
		int longest = Math.abs(w);
		int shortest = Math.abs(h);
		if (!(longest > shortest)) {
			longest = Math.abs(h);
			shortest = Math.abs(w);
			if (h < 0)
				dy2 = -1;
			else if (h > 0)
				dy2 = 1;
			dx2 = 0;
		}
		int numerator = longest >> 1;
		numerator += shortest;
		if (!(numerator < longest)) {
			numerator -= longest;
			x += dx1;
			y += dy1;
		} else {
			x += dx2;
			y += dy2;
		}
		int[] res = { x, y };
		return res;
	}

}
