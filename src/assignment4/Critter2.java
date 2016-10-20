/* CRITTERS Critter2.java
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

public class Critter2 extends Critter {
	
	@Override
	public String toString(){
		return "2";
	}
	
	@Override
	public void doTimeStep(){
		//Critter 2 will run
		run(Critter.getRandomInt(7));
		Critter2 child = new Critter2();
		reproduce(child, getRandomInt(7));
	}
	
	@Override
	public boolean fight (String opponent){
		//Critter 2 will only fight with Critter 1 and Critter 3, else it doesn't
		if (opponent == "1" || opponent == "3"){
			return true;
		}
		return false;
	}
}
