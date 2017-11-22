package conso_resource_reservation;

import repast.simphony.context.Context;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;

public class ResourceHidden extends Resource{
	private boolean hasBeenDiscovered;

	public ResourceHidden(int col, int lig) {
		super(col, lig);
		this.hasBeenDiscovered = false;
	}
		
	public void hasBeenDiscovered() {
		this.hasBeenDiscovered = true;
	}
	
	@Override
	public void implement() {
		super.implement();
		if(this.hasBeenDiscovered) {
			Context<Object> context = ContextUtils.getContext(this);
			ResourceDiscovered rd = new ResourceDiscovered(this.getPosX(), this.getPosY());
			context.remove(this);
			SharedSpace.getInstance().add(rd, rd.getPosX(), rd.getPosY());
			context.add(rd);
			((Grid<SimpleAgent>)context.getProjection("grid")).moveTo(rd, this.getPosX(), this.getPosY());
			return;
		}
	}
}
