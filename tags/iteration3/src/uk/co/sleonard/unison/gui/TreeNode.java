package uk.co.sleonard.unison.gui;

import javax.swing.tree.DefaultMutableTreeNode;

public class TreeNode extends DefaultMutableTreeNode {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8471464838090837440L;
	private String nodeName;

	public TreeNode(Object childObject, String name) {
		super(childObject);
		nodeName = name;
	}

	@Override
	public String toString() {
		return nodeName;
	}
	
	public void setName(String name) {
		this.nodeName = name;
	}
}
