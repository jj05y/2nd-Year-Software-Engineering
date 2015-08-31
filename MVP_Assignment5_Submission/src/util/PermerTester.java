package util;

/**
 * 
 * @author MVP
 * 	This class permutes and prints all permutations of a string, either a passed command line argument or "ABCD"
 *  It then prints a second list of the select few permutations that are in the dictionary
 *
 */
public class PermerTester {
	public static void main(String[] args) {
		Permer permer = new Permer();

		System.out.println("All permutaions:");
		for (String s : permer.getAllPerms((args.length == 0 ? "ABCD" : args[0]))) {
			System.out.println(s);
		}

		System.out.println("\nPermutations that are in the dictionary:");
		for (String s : permer.getLegitPerms((args.length == 0 ? "ABCD" : args[0]))) {
			System.out.println(s);
		}
	}
}
