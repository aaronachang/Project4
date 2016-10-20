/* CRITTERS Critter4.java
 * EE422C Project 4 submission by
 * Aaron Chang
 * AAC3434
 * 16475
 * Siva Manda
 * SM48525
 * 16480
 * Slip days used: <0>
 * Git URL: https://github.com/aaronachang/Project4
 * Fall 2016
 */
package assignment4;

public class Critter4 extends Critter{

	@Override
	public String toString(){
		return "4";
	}
	
	@Override
	public void doTimeStep() {
		// Critter4 only walks in cardinal directions
		walk(Critter.getRandomInt(3) * 2); 
		Critter4 child = new Critter4();
		// Critter4 children don't like cardinal directions
		reproduce(child, Critter.getRandomInt(3) * 2 + 1);
	}

	@Override
	public boolean fight(String opponent) {
		// Critter4 only fights against other Critter4's
		if (opponent == "4") { return true; }
		return false;
	}

}
