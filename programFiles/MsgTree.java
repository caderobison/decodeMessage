package programFiles;

/**
 * 
 * @author Cade Robison
 *
 */
public class MsgTree {
	/**
	 * Character this MsgTree node carries
	 */
	public char payloadChar;
	/**
	 * MsgTree to the left of this
	 */
	public MsgTree left;
	/**
	 * MsgTree to the right of this
	 */
	public MsgTree right;
	/**
	 * Current index of encoding string when building a new MsgTree
	 */
	private static int charIndex = 0;
	/**
	 * String representation of this MsgTree
	 */
	private static String treeString = "";
	/**
	 * Used for traverseTreeCodes(). Tells whether the target character has been
	 * found in the tree
	 */
	private static boolean found = false;

	/**
	 * Constructor for a full MsgTree from a string
	 * 
	 * @param encodingString String to build tree from
	 */
	public MsgTree(String encodingString) {
		if (charIndex >= encodingString.length()) {
			charIndex = 0;
			return;
		}
		char c = encodingString.charAt(charIndex);
		payloadChar = c;
		charIndex += 1;
		if (c == '^') {
			left = new MsgTree(encodingString);
			right = new MsgTree(encodingString);
		}
	}

	/**
	 * Constructor for one node of a MsgTree with a payload of 1 character
	 * 
	 * @param payloadChar Character for payload of this node
	 */
	public MsgTree(char payloadChar) {
		this.payloadChar = payloadChar;
		this.left = null;
		this.right = null;
	}

	/**
	 * Converts MsgTree to a string.
	 */
	@Override
	public String toString() {
		treeString = "";
		traversePreorder(this);
		return treeString;
	}

	/**
	 * Copies this MsgTree into a new MsgTree
	 * 
	 * @return New identical MsgTree to this
	 */
	public MsgTree copy() {
		charIndex = 0;
		MsgTree newMsgTree = new MsgTree(this.toString());
		return newMsgTree;
	}

	/**
	 * Prints all characters in MsgTree along with the 1's and 0's in the codes to
	 * get to said characters to console
	 */
	public void printCodes() {
		System.out.println("character	code");
		System.out.println("------------------------");
		toString();
		for (int i = 0; i < treeString.length(); i++) {
			char c = treeString.charAt(i);
			if (c == '^')
				continue;
			else { // c equals a character
				String code = traversePreorderCode(this, "", c);
				if (c != 10 && c != ' ') {
					System.out.println(c + "		" + code);
				} else if (c == 10) {
					System.out.println("\\n" + "		" + code);
				} else {
					System.out.println("space" + "		" + code);
				}
			}
		}
	}

	/**
	 * Helper method to traverse the node while printing node's payload to
	 * treeString. Used in toString() method
	 * 
	 * @param node MsgTree to traverse
	 */
	private void traversePreorder(MsgTree node) {
		if (node == null)
			return;
		treeString += node.payloadChar;
		traversePreorder(node.left);
		traversePreorder(node.right);
	}

	/**
	 * Traverses node (in preorder fashion) looking for target and returns the code
	 * of 1's and 0's to get to target
	 * 
	 * @param node   MsgTree to traverse
	 * @param s      Code of 1's and 0's to get to target. Updated as node is
	 *               traversed
	 * @param target Character that is being looked for
	 * @return String of 1's and 0's to get to target
	 */
	private String traversePreorderCode(MsgTree node, String s, char target) {
		found = false;
		if (node.payloadChar == target) {
			found = true;
			return s;
		}
		if (node.payloadChar == '^' && !found) {
			s = traversePreorderCode(node.left, s + "0", target);
		}
		if (node.payloadChar == '^' && !found) {
			s = traversePreorderCode(node.right, s + "1", target);
		}
		if (!found && node.payloadChar != target) {
			s = s.substring(0, s.length() - 1);
		}
		return s;
	}

	public void traversePreorderCode2(MsgTree node, String s) { // new not submitted
		if (node.payloadChar != '^') {
			if (node.payloadChar == 10)
				System.out.println("\\n" + "		" + s);
			else if (node.payloadChar == ' ')
				System.out.println("Space" + "		" + s);
			else
				System.out.println(node.payloadChar + "		" + s);
			return;
		}
		traversePreorderCode2(node.left, s + "0");
		traversePreorderCode2(node.right, s + "1");
	}

	public void traversePreorderCode3(MsgTree node, String s) { //new not submitted
																//also prints ^ character
			if (node.payloadChar == 10) System.out.println("\\n" + "		" + s);
			else if (node.payloadChar == ' ') System.out.println("Space" + "		" + s);
			else System.out.println(node.payloadChar + "		" + s);
		if (node.left != null) traversePreorderCode3(node.left, s+"0");
		if (node.right != null) traversePreorderCode3(node.right, s+"1");
	}

}
