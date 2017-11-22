package conso_resource_reservation;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

public abstract class SimpleAgent {

	private int energy;

	protected SimpleAgent() {
		
	}

	@ScheduledMethod(start = 1, interval = 1, priority = 1)
	public abstract void compute();

	@ScheduledMethod(start = 1, interval = 1, priority = 2)
	public abstract void implement();

	protected void move() {
		int width = RunEnvironment.getInstance().getParameters().getInteger("gridWidth");
		int height = RunEnvironment.getInstance().getParameters().getInteger("gridHeight");
		int rX = ContextCreator.rand.nextInt(3) - 1;
		int rY = ContextCreator.rand.nextInt(3) - 1;
		this.setPosX((this.getPosX() + rX) % width);
		this.setPosY((this.getPosY() + rY) % height);
	}

	public int getPosX() {
		Grid<SimpleAgent> grid = (Grid<SimpleAgent>) ContextUtils.getContext(this).getProjection("grid");
		GridPoint gp = grid.getLocation(this);
		return gp.getX();
	}

	public void setPosX(int posX) {
		Grid<SimpleAgent> grid = (Grid<SimpleAgent>) ContextUtils.getContext(this).getProjection("grid");
		grid.moveTo(this, posX, this.getPosY());
	}

	public int getPosY() {
		Grid<SimpleAgent> grid = (Grid<SimpleAgent>) ContextUtils.getContext(this).getProjection("grid");
		GridPoint gp = grid.getLocation(this);
		return gp.getY();
	}

	public void setPosY(int posY) {
		Grid<SimpleAgent> grid = (Grid<SimpleAgent>) ContextUtils.getContext(this).getProjection("grid");
		grid.moveTo(this, this.getPosX(), posY);
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName()+"("+getPosX()+";"+getPosY()+")";
	}
}
