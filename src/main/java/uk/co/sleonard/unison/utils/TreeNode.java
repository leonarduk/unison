package uk.co.sleonard.unison.utils;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Objects;

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
    public TreeNode(final Object childObject, final @NotNull String name) {
        super(childObject);
        this.nodeName = Objects.requireNonNull(name, "name");
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.tree.DefaultMutableTreeNode#toString()
     */
    @Override
    public @NotNull String toString() {
        return this.nodeName;
    }
}
