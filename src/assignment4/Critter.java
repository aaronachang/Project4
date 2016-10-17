/* CRITTERS Main.java
 * EE422C Project 4 submission by
 * Aaron Chang
 * AAC3434
 * 16475
 * Siva Manda
 * SM48525
 * 16480
 * Slip days used: <0>
 * Fall 2016
 */
package assignment4;

import java.awt.Point;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* see the PDF for descriptions of the methods and fields in this class
 * you may add fields, methods or inner classes to Critter ONLY if you make your additions private
 * no new public, protected or default-package code or data can be added to Critter
 */


public abstract class Critter {
	private static String myPackage;
	private	static List<Critter> population = new java.util.ArrayList<Critter>();
	private static List<Critter> babies = new java.util.ArrayList<Critter>();
	private static Map<Point, ArrayList<Critter>> world = new HashMap<Point, ArrayList<Critter>>();
	
	private static final Point[] dir = {
			new Point(1, 0),
			new Point(1, 1),
			new Point(0, 1),
			new Point(-1, 1),
			new Point(-1, 0),
			new Point(-1, -1),
			new Point(0, -1),
			new Point(1, -1)
		};

	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}
	
	private static java.util.Random rand = new java.util.Random();
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}
	
	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}
	
	
	/* a one-character long string that visually depicts your critter in the ASCII interface */
	public String toString() { return "*"; }
	
	private int energy = Params.start_energy;
	protected int getEnergy() { return energy; }
	
	private boolean hasMoved;
	private int x_coord;
	private int y_coord;
	
    private void move(int direction) {
    	x_coord = (x_coord + dir[direction].x) % Params.world_width;
    	y_coord = (y_coord + dir[direction].y) % Params.world_height;
    	hasMoved = true;
    }
	
	protected final void walk(int direction) {
		energy -= Params.walk_energy_cost;
		if (energy >= 0)
			move(direction);
	}
	
	protected final void run(int direction) {
		energy -= Params.run_energy_cost;
		if (energy >= 0) {
			move(direction);
			move(direction);
		}
	}
	
	protected final void reproduce(Critter offspring, int direction) {
		if (energy < Params.min_reproduce_energy) { return; }
		
		offspring.energy = energy / 2;
		energy = energy % 2 == 0 ? energy / 2 : energy / 2 + 1;
		
		offspring.x_coord = (x_coord + dir[direction].x) % Params.world_width;
		offspring.y_coord = (x_coord + dir[direction].x) % Params.world_width;
		babies.add(offspring);
	}

	public abstract void doTimeStep();
	public abstract boolean fight(String oponent);
	
	/**
	 * create and initialize a Critter subclass.
	 * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
	 * an InvalidCritterException must be thrown.
	 * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
	 * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
	 * an Exception.)
	 * @param critter_class_name
	 * @throws InvalidCritterException
	 */
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {
		Class<?> myCritter = null;
		Constructor<?> constructor = null;
		Object instanceOfMyCritter = null;

	    try {
			myCritter = Class.forName(critter_class_name);
		    constructor = myCritter.getConstructor(); // get null parameter constructor
		    instanceOfMyCritter = constructor.newInstance(); // create instance
		    Critter me = (Critter) instanceOfMyCritter; // cast to Critter
		    me.x_coord = getRandomInt(Params.world_width);
		    me.y_coord = getRandomInt(Params.world_height);
		    
		    population.add(me);
		    Point pos = new Point(me.x_coord, me.y_coord);
		    
		    ArrayList<Critter> bucket = world.get(pos);
		    if (bucket == null)
		    	bucket = new ArrayList<Critter>();
		    
		    bucket.add(me);
		    
		    world.put(pos, bucket);
		    
		} catch (Exception e) {
			if (e instanceof ClassNotFoundException)
				throw new InvalidCritterException(critter_class_name);
			else
				e.printStackTrace();
		}
	}
	
	/**
	 * Gets a list of critters of a specific type.
	 * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
	 * @return List of Critters.
	 * @throws InvalidCritterException
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		List<Critter> result = new java.util.ArrayList<Critter>();
		
		for (ArrayList<Critter> bugs : world.values()) {
			for (Critter bug : bugs) {
				if (bug.getClass().getTypeName().equals(critter_class_name))
					result.add(bug);
			}
		}
		return result;
	}
	
	/**
	 * Prints out how many Critters of each type there are on the board.
	 * @param critters List of Critters.
	 */
	public static void runStats(List<Critter> critters) {
		System.out.print("" + critters.size() + " critters as follows -- ");
		java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
		for (Critter crit : critters) {
			String crit_string = crit.toString();
			Integer old_count = critter_count.get(crit_string);
			if (old_count == null) {
				critter_count.put(crit_string,  1);
			} else {
				critter_count.put(crit_string, old_count.intValue() + 1);
			}
		}
		String prefix = "";
		for (String s : critter_count.keySet()) {
			System.out.print(prefix + s + ":" + critter_count.get(s));
			prefix = ", ";
		}
		System.out.println();		
	}
	
	/* the TestCritter class allows some critters to "cheat". If you want to 
	 * create tests of your Critter model, you can create subclasses of this class
	 * and then use the setter functions contained here. 
	 * 
	 * NOTE: you must make sure that the setter functions work with your implementation
	 * of Critter. That means, if you're recording the positions of your critters
	 * using some sort of external grid or some other data structure in addition
	 * to the x_coord and y_coord functions, then you MUST update these setter functions
	 * so that they correctly update your grid/data structure.
	 */
	static abstract class TestCritter extends Critter {
		protected void setEnergy(int new_energy_value) {
			super.energy = new_energy_value;
		}
		
		protected void setX_coord(int new_x_coord) {
			super.x_coord = new_x_coord;
		}
		
		protected void setY_coord(int new_y_coord) {
			super.y_coord = new_y_coord;
		}
		
		protected int getX_coord() {
			return super.x_coord;
		}
		
		protected int getY_coord() {
			return super.y_coord;
		}
		

		/*
		 * This method getPopulation has to be modified by you if you are not using the population
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.
		 */
		protected static List<Critter> getPopulation() {
			return population;
		}
		
		/*
		 * This method getBabies has to be modified by you if you are not using the babies
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.  Babies should be added to the general population 
		 * at either the beginning OR the end of every timestep.
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}

	/**
	 * Clear the world of all critters, dead and alive
	 */
	public static void clearWorld() {
		for (ArrayList<Critter> spot : world.values())
			spot.clear();
	}
	
	/*
	 * return 0-7 for direction of immediate open space
	 * return 8-15 for direction of open space two spaces away
	 */
	private static int nextAdjacentPoint(Point p){
		for (int direction = 0; direction < 8; direction++) { // walk
			int temp_x = (p.x + dir[direction].x) % Params.world_width;
			int temp_y = (p.y + dir[direction].y) % Params.world_height;
			Point temp_p = new Point(temp_x, temp_y);
			if (!world.containsKey(temp_p) || world.get(temp_p).size() == 0) { return direction; }
		}
		
		for (int direction = 0; direction < 8; direction++) { // run if walk isn't an option
			int temp_x = (p.x + 2 * dir[direction].x) % Params.world_width;
			int temp_y = (p.y + 2 * dir[direction].y) % Params.world_height;
			Point temp_p = new Point(temp_x, temp_y);
			if (!world.containsKey(temp_p) || world.get(temp_p).size() == 0) { return direction + 8; }
		}
		
		return -1; // cannot walk or run
	}
	
	private void tryToEscape() {
		int escapeDir = nextAdjacentPoint(new Point(x_coord, y_coord));
		if (hasMoved || escapeDir == -1) { energy -= Params.walk_energy_cost; } 
		else if (escapeDir < 8) { walk(escapeDir); }
		else { run(escapeDir - 8); }
	}
	
	private static void resolveEncounters() {
		for (ArrayList<Critter> spot : world.values()) {
			// if spot occupied by more than one critter
			while (spot.size() > 1) {
				Critter critA = spot.get(0);
				Critter critB = spot.get(1);
				Point fightPoint = new Point(critA.x_coord, critA.y_coord); 
				boolean fightA = critA.fight(critB.toString());
				boolean fightB = critB.fight(critA.toString());
				
				if (!fightA) { critA.tryToEscape(); }
				if (!fightB) { critB.tryToEscape(); }
				if (critA.energy <= 0 || critB.energy <= 0) {
					if (critB.energy <= 0) {
						spot.remove(critB);
						population.remove(critB);
					}
					if (critA.energy <= 0) { 
						spot.remove(critA); 
						population.remove(critA); 
					}
				} else if (critA.x_coord == fightPoint.x && critA.y_coord == fightPoint.y && 
						   critB.x_coord == fightPoint.x && critB.y_coord == fightPoint.y) {
					//fight
					int rollA = getRandomInt(critA.energy);
					int rollB = getRandomInt(critB.energy);
					
					if (rollA > rollB) { 
						critA.energy += .5 * critB.energy;
						spot.remove(critB);
						population.remove(critB);
					} else {
						critB.energy += .5 * critA.energy;
						spot.remove(critA);
						population.remove(critA);
					}
				} else { // remove from spot if critter moved
					if (!(critB.x_coord == fightPoint.x && critB.y_coord == fightPoint.y)) {
						spot.remove(critB);
					}
					if (!(critA.x_coord == fightPoint.x && critA.y_coord == fightPoint.y)) {
						spot.remove(critA);
					}
				}
			}
		}
	}
	
	private static int timestep = 0;
	public static void worldTimeStep() {
		timestep++;
		for (ArrayList<Critter> spot : world.values()) {
			for (Critter bug : spot) {
				bug.hasMoved = false;
				bug.doTimeStep();
			}
		}
		resolveEncounters();
		
		// Update rest energy
		for (ArrayList<Critter> spot : world.values())
			for (Critter bug : spot)
				bug.energy -= Params.rest_energy_cost;
		
		// Add algae
		try {
			for (int i = 0; i < Params.refresh_algae_count; i++)
				makeCritter("assignment4.Algae");
		} catch (InvalidCritterException e) {
			e.printStackTrace();
		}
		
		// Remove dead critters
		Iterator<Critter> iter = population.iterator();
		while (iter.hasNext()) {
			Critter bug = iter.next();
			if (bug.energy <= 0) {
				iter.remove();
			}
		}
				
		// Add babies to population
		population.addAll(babies);
		
		// Clear and update critter map
		for (ArrayList<Critter> spot : world.values())
			spot.clear();
		
		for (Critter bug : population) {
			Point p = new Point(bug.x_coord, bug.y_coord);
			if (!world.containsKey(p)) { world.put(p, new ArrayList<Critter>()); }
			world.get(p).add(bug);
		}
	}
	
	public static void displayWorld() {
		for (int i = -1; i <= Params.world_height; i++) {
			for (int j = -1; j <= Params.world_width; j++) {
				if (i == -1 || i == Params.world_height)
					System.out.print((j == -1 || j == Params.world_width) ? '+' : '-');
				else {
					Point currentPosition = new Point(j, i);
					if (j == -1 || j == Params.world_width)
						System.out.print('|');
					else if (world.containsKey(currentPosition) && world.get(currentPosition).size() > 0)
						System.out.print(world.get(currentPosition).get(0));
					else
						System.out.print(' ');
				}
			}
			System.out.println();
		}
	}
}
