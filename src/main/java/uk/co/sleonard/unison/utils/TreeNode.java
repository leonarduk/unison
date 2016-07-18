package uk.co.sleonard.unison.utils;

import javafx.scene.control.TreeItem;

/**
 * The Class TreeNode.
 * 
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 *
 */
public class TreeNode extends TreeItem<Object> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8471464838090837440L;

	/** The node name. */
	private String nodeName;

	/**
	 * Instantiates a new tree node.
	 *
	 * @param childObject
	 *            the child object
	 * @param name
	 *            the name
	 */
	public TreeNode(final Object childObject, final String name) {
		super(childObject);
		this.nodeName = name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the new name
	 */
	public void setName(final String name) {
		this.setValue(name);
		this.nodeName = name;
	}

	@Override
	public String toString() {
		return this.nodeName;
	}
}
