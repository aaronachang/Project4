/* CRITTERS Critter3.java
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

public class Critter3 extends Critter {

	@Override
	public String toString(){
		return "3";
	}
	
	@Override
	public void doTimeStep() {
		// Critter3 walks around randomly
		walk(Critter.getRandomInt(7));
		Critter3 child = new Critter3();
		reproduce(child, Critter.getRandomInt(7));
	}

	@Override
	public boolean fight(String opponent) {
		// Critter3 doesn't fight odd critters
		char c = opponent.charAt(0); 
		if (c % 2 == 0) { return true; }
		return false;
	}
}
