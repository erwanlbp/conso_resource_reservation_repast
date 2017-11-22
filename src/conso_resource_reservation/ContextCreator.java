package conso_resource_reservation;

import java.util.Random;

import repast.simphony.context.Context;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;
import repast.simphony.util.ContextUtils;

public class ContextCreator implements ContextBuilder<SimpleAgent> {

	public static Random rand = new Random(System.currentTimeMillis());

	@Override
	public Context<SimpleAgent> build(Context<SimpleAgent> context) {
		context.setId("conso_resource_reservation");

		int width = RunEnvironment.getInstance().getParameters().getInteger("gridWidth");
		int height = RunEnvironment.getInstance().getParameters().getInteger("gridHeight");
		int nbAgent = RunEnvironment.getInstance().getParameters().getInteger("nbAgent");
		int nbResource = RunEnvironment.getInstance().getParameters().getInteger("nbResource");

		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<SimpleAgent> grid = gridFactory.createGrid("grid", context, new GridBuilderParameters<SimpleAgent>(
				new WrapAroundBorders(), new SimpleGridAdder<SimpleAgent>(), true, width, height));

		while (nbAgent > 0) {
			int col = rand.nextInt(width);
			int lig = rand.nextInt(height);

			SimpleAgent agt = grid.getRandomObjectAt(col, lig);

			if (agt == null) {
				SimpleAgent newAgt = new Agent();
				context.add(newAgt);
				grid.moveTo(newAgt, col, lig);
				nbAgent--;
			}
		}

		while (nbResource > 0) {
			int col = rand.nextInt(width);
			int lig = rand.nextInt(height);

			SimpleAgent res = grid.getRandomObjectAt(col, lig);

			if (res == null) {
				SimpleAgent newRes = new ResourceHidden(col, lig);
				context.add(newRes);
				grid.moveTo(newRes, col, lig);
				nbResource--;
			}
		}

		return context;
	}
	
	public static void addRandomResource(SimpleAgent a) {
		double lim = RunEnvironment.getInstance().getParameters().getDouble("chancePoping");
		if (Math.random() < lim) {
			SimpleAgent newRes = new ResourceHidden(ContextCreator.rand.nextInt(RunEnvironment.getInstance().getParameters().getInteger("gridWidth")),	ContextCreator.rand.nextInt(RunEnvironment.getInstance().getParameters().getInteger("gridHeight")));
			Context<SimpleAgent> context = ContextUtils.getContext(a);
			context.add(newRes);
			((Grid<SimpleAgent>) context.getProjection("grid")).moveTo(newRes, newRes.getPosX(), newRes.getPosY());
			System.out.println("Popped "+newRes);
		}
	}


}