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
package assignment4; // cannot be in default package
import java.util.Scanner;
import java.util.List;
import java.io.*;


/*
 * Usage: java <pkgname>.Main <input file> test
 * input file is optional.  If input file is specified, the word 'test' is optional.
 * May not use 'test' argument without specifying input file.
 */
public class Main {

    static Scanner kb;	// scanner connected to keyboard input, or input file
    private static String inputFile;	// input file, used instead of keyboard input if specified
    static ByteArrayOutputStream testOutputString;	// if test specified, holds all console output
    private static String myPackage;	// package of Critter file.  Critter cannot be in default pkg.
    private static boolean DEBUG = false; // Use it or not, as you wish!
    static PrintStream old = System.out;	// if you want to restore output to console


    // Gets the package name.  The usage assumes that Critter and its subclasses are all in the same package.
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    /**
     * Main method.
     * @param args args can be empty.  If not empty, provide two parameters -- the first is a file name, 
     * and the second is test (for test output, where all output to be directed to a String), or nothing.
     */
    public static void main(String[] args) { 
        if (args.length != 0) {
            try {
                inputFile = args[0];
                kb = new Scanner(new File(inputFile));			
            } catch (FileNotFoundException e) {
                System.out.println("USAGE: java Main OR java Main <input file> <test output>");
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("USAGE: java Main OR java Main <input file>  <test output>");
            }
            if (args.length >= 2) {
                if (args[1].equals("test")) { // if the word "test" is the second argument to java
                    // Create a stream to hold the output
                    testOutputString = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(testOutputString);
                    // Save the old System.out.
                    old = System.out;
                    // Tell Java to use the special stream; all console output will be redirected here from now
                    System.setOut(ps);
                }
            }
        } else { // if no arguments to main
            kb = new Scanner(System.in); // use keyboard and console
        }

        /* Do not alter the code above for your submission. */
        /* Write your code below. */
        
        System.out.println("critter> ");
        
        while (kb.hasNextLine()) {
            String input = kb.nextLine();
            boolean exit = false;
            
            try {
            	// Split the command into words
            	String[] user_args = input.split("\\s+");
            	
            	switch (user_args[0].toLowerCase()) {
            		case "quit": 
            			if (user_args.length > 1) throw new Exception();
            			exit = true;
            			break;
            						
            		case "show": 
            			if (user_args.length > 1) throw new Exception();
            			Critter.displayWorld();
            			break;
				
            		case "step": 
            			if (user_args.length > 2) throw new Exception();
            			int steps;
            						
            			if (user_args.length > 1){
            				steps = Integer.parseInt(user_args[1]);
            			}
            			else{
            				steps = 1;
            			}
            						
            			for (int i = 0; i < steps; i++){
            				Critter.worldTimeStep();
            			}
            			break;
            						
            		case "seed": 
            			if (user_args.length > 2) throw new Exception();
            			Critter.setSeed(Long.parseLong(user_args[1]));
            			break;
            						
            		case "make": 
            			if (user_args.length > 3) throw new Exception();
            			int amount;
            						
            			if (user_args.length > 2){
            				amount = Integer.parseInt(user_args[2]);
            			}
            			else{
            				amount = 1;
            			}
            						
            			for (int i = 0; i < amount; i++){
            				Critter.makeCritter(user_args[1]);
            			}
            			break;
            						
            		case "stats": 
            			if (user_args.length > 2) throw new Exception();
            			List<Critter> crts = Critter.getInstances(user_args[1]);
            			Class<?> classes = Class.forName(user_args[1]);
            			classes.getMethod("runStats", java.util.List.class).invoke(null, crts);
            			break;
            						
            		default: 	 
            			System.out.println("Not a valid command: " + input);
            	}
            } catch (Exception e){
                // Code for exception
            	System.out.println("Error in user input: " + input);
            }
            
            if (exit) {
            	break;
            }
            System.out.print("critter> ");
        }
        kb.close();
        
        /* Write your code above */
        System.out.flush();
    }
}
