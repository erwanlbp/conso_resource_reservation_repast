package conso_resource_reservation;

public class Resource extends SimpleAgent{
	
	protected int posX;
	protected int posY;

	protected Resource(int x, int y) {
		super();
		this.posX = x;
		this.posY = y;
	}
	
	@Override
	public int getPosX() {
		return this.posX;
	}

	@Override
	public void setPosX(int posX) {
		super.setPosX(posX);
		this.posX = posX;
	}

	@Override
	public int getPosY() {
		return this.posY;
	}

	@Override
	public void setPosY(int posY) {
		super.setPosY(posY);
		this.posY = posY;
	}

	@Override
	public void compute() {
		
	}

	@Override
	public void implement() {
	}
}
