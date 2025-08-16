/*
 * Copyright (c) 2003, the JUNG Project and the Regents of the University of California All rights
 * reserved.
 *
 * This software is open-source under the BSD license; see either "license.txt" or
 * http://jung.sourceforge.net/license.txt for a description.
 *
 */
package uk.co.sleonard.unison.gui;

import edu.uci.ics.jung.graph.ArchetypeVertex;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.*;
import edu.uci.ics.jung.graph.impl.DirectedSparseEdge;
import edu.uci.ics.jung.graph.impl.DirectedSparseGraph;
import edu.uci.ics.jung.graph.impl.DirectedSparseVertex;
import edu.uci.ics.jung.visualization.*;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import uk.co.sleonard.unison.UNISoNException;
import uk.co.sleonard.unison.output.Relationship;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Demonstrates the use of <code>GraphZoomScrollPane</code>. This class shows off the
 * <code>VisualizationViewer</code> zooming and panning capabilities, using horizontal and vertical
 * scrollbars.
 *
 * <p>
 * This demo also shows ToolTips on graph vertices.
 * </p>
 *
 * @author Tom Nelson - RABA Technologies
 * @since v1.0.0
 *
 */
public class GraphPreviewPanel extends JPanel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7584897717727915747L;

	/** the graph. */
	private final Graph graph;

	/** the visual component and renderer for the graph. */
	private final VisualizationViewer vv;

	/** The show labels. */
	boolean showLabels;

	/**
	 * a driver for this demo.
	 *
	 * @param args
	 *            the arguments
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	public static void main(final String[] args) throws UNISoNException {
		final JFrame frame = new JFrame();
		frame.setVisible(true);
		final LinkedList<String> posters = new LinkedList<>();
		final String LUCY = "Lucy";
		posters.add(LUCY);
		final String JESS = "Jess";
		posters.add(JESS);
		final String MIC = "Mic";
		posters.add(MIC);
		final String VIC = "Vic";
		posters.add(VIC);
		final String DAN = "Dan";
		posters.add(DAN);
		final String STEVE = "Steve";
		posters.add(STEVE);

		Relationship link = new Relationship(posters.indexOf(STEVE), posters.indexOf(LUCY));

		final List<Relationship> links = new Vector<>();
		links.add(link);
		link = new Relationship(posters.indexOf(STEVE), posters.indexOf(MIC));
		links.add(link);

		final GraphPreviewPanel panel = new GraphPreviewPanel(posters, links);
		panel.setVisible(true);
		frame.add(panel);
		frame.setSize(frame.getPreferredSize());

	}

	/**
	 * create an instance of a simple graph with controls to demo the zoom features.
	 *
	 * @param peopleList
	 *            the people list
	 * @param links
	 *            the links
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	public GraphPreviewPanel(final LinkedList<String> peopleList, final List<Relationship> links)
	        throws UNISoNException {
		// create a simple graph for the demo
		this.graph = new DirectedSparseGraph();
		final Vertex[] v = this.createEdges(links, peopleList);

		final PluggableRenderer pr = new PluggableRenderer();
		pr.setVertexStringer(new UnicodeVertexStringer(v, peopleList));
		pr.setVertexPaintFunction(
		        new PickableVertexPaintFunction(pr, Color.lightGray, Color.white, Color.yellow));
		pr.setGraphLabelRenderer(new DefaultGraphLabelRenderer(Color.cyan, Color.cyan));
		final VertexIconAndShapeFunction dvisf = new VertexIconAndShapeFunction(
		        new EllipseVertexShapeFunction());
		pr.setVertexShapeFunction(dvisf);
		pr.setVertexIconFunction(dvisf);
		this.vv = new VisualizationViewer(new FRLayout(this.graph), pr);
		this.vv.setPickSupport(new ShapePickSupport());
		pr.setEdgeShapeFunction(new EdgeShape.QuadCurve());
		this.vv.setBackground(Color.white);

		// add my listener for ToolTips
		this.vv.setToolTipFunction(new DefaultToolTipFunction());

		// create a frome to hold the graph
		final GraphZoomScrollPane panel = new GraphZoomScrollPane(this.vv);
		this.add(panel);
		// this.add(vv);
		final ModalGraphMouse gm = new DefaultModalGraphMouse();
		this.vv.setGraphMouse(gm);

		// showLabels = true;
	}

	/**
	 * create edges for this demo graph.
	 *
	 * @param links
	 *            the links
	 * @param peopleList
	 *            the people list
	 * @return the vertex[]
	 * @throws UNISoNException
	 *             the UNI so n exception
	 */
	Vertex[] createEdges(final List<Relationship> links, final LinkedList<String> peopleList)
	        throws UNISoNException {
		final Vertex[] v = new Vertex[peopleList.size()];
		for (int i = 0; i < peopleList.size(); i++) {
			v[i] = this.graph.addVertex(new UsenetVertex(peopleList.get(i)));
		}
		for (final Relationship link : links) {
			try {
				// subtract one as index starts at 0 for arrays
				this.graph.addEdge(
				        new DirectedSparseEdge(v[link.getOwner() - 1], v[link.getTarget() - 1]));
			}
			catch (final ArrayIndexOutOfBoundsException e) {
				throw new UNISoNException("Array out of bounds: size " + v.length + " vs "
				        + link.getOwner() + " or " + link.getTarget(), e);

			}
		}
		return v;
	}

	/**
	 * The Class UnicodeVertexStringer.
	 */
	private class UnicodeVertexStringer implements VertexStringer {

		/** The map. */
		Map<Vertex, String> map = new HashMap<>();

		/**
		 * Instantiates a new unicode vertex stringer.
		 *
		 * @param vertices
		 *            the vertices
		 * @param labels
		 *            the labels
		 */
		public UnicodeVertexStringer(final Vertex[] vertices, final List<String> labels) {
			for (int i = 0; i < vertices.length; i++) {
				this.map.put(vertices[i], labels.get(i));
			}
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * edu.uci.ics.jung.graph.decorators.VertexStringer#getLabel(edu.uci.ics.jung.graph.Vertex)
		 */
		@Override
		public String getLabel(final ArchetypeVertex v) {
			if (GraphPreviewPanel.this.showLabels) {
				return this.map.get(v);
			}
			return "";
		}
	}

	/**
	 * The Class UsenetVertex.
	 */
	class UsenetVertex extends DirectedSparseVertex {

		/** The label. */
		private final String label;

		/**
		 * Instantiates a new usenet vertex.
		 *
		 * @param label
		 *            the label
		 */
		public UsenetVertex(final String label) {
			this.label = label;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see edu.uci.ics.jung.graph.impl.AbstractSparseVertex#toString()
		 */
		@Override
		public String toString() {
			return this.label;
		}
	}
}
