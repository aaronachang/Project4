/* CRITTERS Critter1.java
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

public class Critter1 extends Critter {
	
	@Override
	public String toString(){
		return "1";
	}

	@Override
	public void doTimeStep() {
		//Angry walks around looking to fight
		walk(Critter.getRandomInt(10));
		Angry child = new Angry();
		reproduce(child, getRandomInt(5));		
	}

	@Override
	public boolean fight(String oponent) {
		//Angry likes to fight all the time
		return true;	
	}
}
