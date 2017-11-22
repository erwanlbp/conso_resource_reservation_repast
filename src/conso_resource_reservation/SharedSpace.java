package conso_resource_reservation;

import java.awt.Point;
import java.awt.geom.Point2D;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;

public class SharedSpace {
	private ResourceDiscovered[][] resourcesPartagees;
	private int nbCol, nbLig;

	private static SharedSpace instance;

	private SharedSpace() {
		nbCol = RunEnvironment.getInstance().getParameters().getInteger("gridWidth");
		nbLig = RunEnvironment.getInstance().getParameters().getInteger("gridHeight");
		this.resourcesPartagees = new ResourceDiscovered[nbCol][nbLig];
		for (int col = 0; col < nbCol; col++) {
			for (int lig = 0; lig < nbLig; lig++) {
				resourcesPartagees[col][lig] = null;
			}
		}
	}

	public static SharedSpace getInstance() {
		if (instance == null) {
			instance = new SharedSpace();
		}
		return instance;
	}

	// Point 4
	// Ajout de resources découvertes dans l'espace partagé
	public boolean add(ResourceDiscovered resource, int col, int lig) {
		if (!validCoordonates(col, lig)) {
			System.out.println("Can't ADD " + resource + " on sharedSpace (" + col + ";" + lig + ")");
			return false;
		}

		// TODO Lock
		if (resourcesPartagees[col][lig] != null) {
			System.out.println("Can't ADD " + resource + " on sharedSpace (" + col + ";" + lig + ") : " + resourcesPartagees[col][lig] + " is already there");
			return false;
		}
		resourcesPartagees[col][lig] = resource;
		// TODO Unlock

		return true;
	}

	// Point
	public boolean bookOne(Agent agent) {
		if (agent.resourceBooked() != null) {
			return false;
		}
		int minCol = nbCol + 1, minLig = nbLig + 1;
		double minD = Point2D.distance(agent.getPosX(), agent.getPosY(), 0, 0);
		// TODO Lock
		for (int lig = 0; lig < nbLig; lig++) {
			for (int col = 0; col < nbCol; col++) {
				if (this.resourcesPartagees[col][lig] == null || this.resourcesPartagees[col][lig].isBooked()) {
					continue;
				}

				double distance = Point2D.distance(agent.getPosX(), agent.getPosY(), col, lig);
				if (distance < minD) {
					minCol = col;
					minLig = lig;
					minD = distance;
				}
			}
		}

		if (validCoordonates(minCol, minLig)) {
			agent.book(resourcesPartagees[minCol][minLig]);
			System.out.println(agent + " booked " + resourcesPartagees[minCol][minLig]);
			return true;
		}
		// TODO Unlock
		return false;
	}

	// Point
	public boolean take(Agent agent, int col, int lig) {
		if (!validCoordonates(col, lig)) {
			System.out.println(agent + " can't TAKE a resource on sharedSpace (" + col + ";" + lig + ")");
			return false;
		}
		// TODO Lock
		if (resourcesPartagees[col][lig] != null && resourcesPartagees[col][lig].getBooker() == agent) {
			agent.consume(resourcesPartagees[col][lig]);
			resourcesPartagees[col][lig] = null;
			System.out.println(agent + " took " + resourcesPartagees[col][lig]);
			return true;
		}
		// TODO Unlock
		return false;
	}

	private boolean validCoordonates(int col, int lig) {
		return col >= 0 && col < this.nbCol && lig >= 0 && lig < this.nbLig;
	}
}
