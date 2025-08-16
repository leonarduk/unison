package uk.co.sleonard.unison.utils;

import javax.swing.tree.DefaultMutableTreeNode;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a node in the application's GUI hierarchy.
 *
 * @author Stephen <github@leonarduk.com>
 * @since v1.0.0
 */
public class TreeNode extends DefaultMutableTreeNode {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -8471464838090837440L;

    /** The node name. */
    @Getter
    @Setter
    private String nodeName;

    /**
     * Instantiates a new tree node.
     *
     * @param childObject the child object
     * @param name        the name
     */
    public TreeNode(final Object childObject, final String name) {
        super(childObject);
        this.nodeName = name;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.tree.DefaultMutableTreeNode#toString()
     */
    @Override
    public String toString() {
        return this.nodeName;
    }
}
