package programFiles;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * 
 * @author Cade Robison
 *
 */
public class MessageDecoder {

	/**
	 * Index of the line which starts the bits containing the message to decode
	 * (first line in the file has index 0)
	 */
	private static int bitsLineIndex;

	/**
	 * Number of characters in the message. Used for printStats()
	 */
	private static int numChars;
	/**
	 * Number of bits in the message. Used for printStats()
	 */
	private static int numBits;
	/**
	 * Average number of bits per character in the message. Used for printStats()
	 */
	private static double avgBitsPerChar;

	public static void main(String[] args) {
		boolean goodFile = false;
		String bits = "";
		Scanner consoleScnr = new Scanner(System.in);
		String treeString = "";
		while (!goodFile) {
			bitsLineIndex = -1;
			treeString = "";
			System.out.println("Please enter filename to decode:");
			String fileName = consoleScnr.next();
			File f = new File(fileName);
			try {
				bits = findBits(f);
				treeString = findTree(f, 0);
				for (int i = 1; i < bitsLineIndex; i++) {
					treeString = treeString + (char) 10 + findTree(f, i);
				}
				if (treeString.length() > 1 && !treeString.contains((CharSequence) "^"))
					throw new IOException(); // If treeString has more than on character, then it must have an internal
												// node somewhere. If not, the file is messed up
				goodFile = true;

			} catch (FileNotFoundException e) {
				System.err.println("The file you want to decode doesn't exist. Try again");
			} catch (IOException e) {
				System.err.println("The file you want to decode is not in the right format. Try again");
			}
		}
		MsgTree tree = new MsgTree(treeString);
		System.out.println();
		tree.printCodes();
//		System.out.println("character	code"); this is new code not submitted
//		System.out.println("------------------------");
//		tree.traversePreorderCode2(tree, "");
		try {
			if (decode(tree, bits) != "") {
				System.out.println();
				System.out.println("MESSAGE:");
				System.out.println();
				System.out.println(decode(tree, bits));
				System.out.println();
				printStats();
			}
		} catch (IndexOutOfBoundsException e) {
			System.err.println("Illegal file message. Cannot decode message.");
		}

		consoleScnr.close();

	}

	/**
	 * Method to decode string of 1's and 0's in order to get message
	 * 
	 * @param tree MsgTree to use to decode bits
	 * @param bits String of 1's and 0's to decode
	 * @return Message bits was encoding
	 */
	public static String decode(MsgTree tree, String bits) {
		String returnString = "";
		avgBitsPerChar = 0;
		numChars = 0;
		int i = 0;
		char c;
		while (i < bits.length()) {
			MsgTree newTree = tree;
			while (newTree.left != null && newTree.right != null) {
				c = bits.charAt(i);
				if (c == '0') {
					newTree = newTree.left;
				} else { // c == '1'
					newTree = newTree.right;
				}
				i++;
				numBits++;
			}
			if (newTree.payloadChar != '^') {
				returnString += newTree.payloadChar;
				numChars++;
			}
			avgBitsPerChar = ((double) numBits) / numChars;
		}
		return returnString;
	}

	/**
	 * Helper method that finds the bits containing the message to decode in the
	 * file
	 * 
	 * @param f File to find bits in
	 * @return String of bits that the message is encoded in
	 * @throws IOException           Exception if the bits are not found
	 * @throws FileNotFoundException Exception if the file given is not found
	 */
	private static String findBits(File f) throws IOException, FileNotFoundException {
		String bits = "";
		Scanner scnr = new Scanner(f);
		while (scnr.hasNextLine()) {
			bits = scnr.nextLine();
			bitsLineIndex++;
		}
		scnr.close();
		if (bitsLineIndex <= 0 || !bits.matches("^[0-1]*$")) // The bits line is always the last one so if the index is
																// less than or equal to 0, it either doesn't exist or
																// is before the string. if the bits line doesn't have
																// only 1's and 0's in it the file format is also messed
																// up
			throw new IOException();
		return bits;
	}

	/**
	 * Helper method to find the string encoding the tree in the file
	 * 
	 * @param f     File to analyze
	 * @param index Index of the bits of the message to decode. Will not analyze
	 *              past this line
	 * @return String containing information to make the tree
	 * @throws FileNotFoundException Exception if file given is not found
	 */
	private static String findTree(File f, int index) throws FileNotFoundException {
		Scanner scnr = new Scanner(f);
		String tree = "";
		for (int i = 0; i <= index; i++) {
			tree = scnr.nextLine();
		}
		scnr.close();
		return tree;
	}

	/**
	 * Prints the statistics of the message in the file. Is reliant on the message
	 * and not just the tree.
	 */
	private static void printStats() {
		System.out.println("STATISTICS:");
		System.out.printf("%s%.1f%n", "Avg bits/char:		", avgBitsPerChar);
		System.out.println("Total characters:	" + numChars);
		double spaceSave = (1 - avgBitsPerChar / 16) * 100;
		System.out.printf("%s%.1f%n", "Space savings:		", spaceSave);
	}
}
