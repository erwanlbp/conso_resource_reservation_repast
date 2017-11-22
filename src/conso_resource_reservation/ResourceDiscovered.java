package conso_resource_reservation;

import repast.simphony.context.Context;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;

public class ResourceDiscovered extends Resource {
	private boolean hasBeenConsumed;
	private Agent booker;

	public ResourceDiscovered(int col, int lig) {
		super(col, lig);
		this.hasBeenConsumed = false;
		this.booker = null;
	}
	
	public void hasBeenConsumed() {
		this.hasBeenConsumed = true;
	}
	
	public boolean isBooked() {
		return booker != null;
	}
	
	public void book(Agent agent) {
		this.booker = agent;
	}
	
	public Agent getBooker() {
		return booker;
	}
	
	@Override
	public void implement() {
		super.implement();
		if(this.hasBeenConsumed) {
			ContextUtils.getContext(this).remove(this);
			return;
		}
	}
}
