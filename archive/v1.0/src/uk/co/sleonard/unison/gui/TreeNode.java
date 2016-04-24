package uk.co.sleonard.unison.gui;

import javax.swing.tree.DefaultMutableTreeNode;

public class TreeNode extends DefaultMutableTreeNode {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8471464838090837440L;
	private String nodeName;

	public TreeNode(final Object childObject, final String name) {
		super(childObject);
		this.nodeName = name;
	}

	public void setName(final String name) {
		this.nodeName = name;
	}

	@Override
	public String toString() {
		return this.nodeName;
	}
}
