package util;

public class PermerTester {
	public static void main(String[] args) {
		Permer permer = new Permer();

		System.out.println("All permutaions:");
		for (String s : permer.getAllPerms((args.length == 0 ? "ABCD" : args[1]))) {
			System.out.println(s);
		}

		System.out.println("\nPermutations that are in the dictionary:");
		for (String s : permer.getLegitPerms((args.length == 0 ? "ABCD" : args[1]))) {
			System.out.println(s);
		}
	}
}
