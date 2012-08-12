/*
 * Copyright (c) 2003, the JUNG Project and the Regents of the University of
 * California All rights reserved.
 * 
 * This software is open-source under the BSD license; see either "license.txt"
 * or http://jung.sourceforge.net/license.txt for a description.
 * 
 */
package uk.co.sleonard.unison.gui;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

import uk.co.sleonard.unison.output.Relationship;
import edu.uci.ics.jung.graph.ArchetypeVertex;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.DefaultToolTipFunction;
import edu.uci.ics.jung.graph.decorators.EdgeShape;
import edu.uci.ics.jung.graph.decorators.EllipseVertexShapeFunction;
import edu.uci.ics.jung.graph.decorators.PickableVertexPaintFunction;
import edu.uci.ics.jung.graph.decorators.VertexIconAndShapeFunction;
import edu.uci.ics.jung.graph.decorators.VertexStringer;
import edu.uci.ics.jung.graph.impl.DirectedSparseEdge;
import edu.uci.ics.jung.graph.impl.DirectedSparseGraph;
import edu.uci.ics.jung.graph.impl.DirectedSparseVertex;
import edu.uci.ics.jung.visualization.DefaultGraphLabelRenderer;
import edu.uci.ics.jung.visualization.FRLayout;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.PluggableRenderer;
import edu.uci.ics.jung.visualization.ShapePickSupport;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;

/**
 * Demonstrates the use of <code>GraphZoomScrollPane</code>. This class shows
 * off the <code>VisualizationViewer</code> zooming and panning capabilities,
 * using horizontal and vertical scrollbars.
 * 
 * <p>
 * This demo also shows ToolTips on graph vertices.
 * </p>
 * 
 * @author Tom Nelson - RABA Technologies
 * 
 */
public class GraphPreviewPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7584897717727915747L;

	/**
	 * the graph
	 */
	Graph graph;

	/**
	 * the visual component and renderer for the graph
	 */
	VisualizationViewer vv;

	boolean showLabels;

	/**
	 * create an instance of a simple graph with controls to demo the zoom
	 * features.
	 * 
	 * @param links
	 * @throws UNISoNException
	 * 
	 */
	public GraphPreviewPanel(LinkedList<String> peopleList,
			List<Relationship> links) throws UNISoNException {
		// create a simple graph for the demo
		graph = new DirectedSparseGraph();
		Vertex[] v = createEdges(links, peopleList);

		PluggableRenderer pr = new PluggableRenderer();
		pr.setVertexStringer(new UnicodeVertexStringer(v, peopleList));
		pr.setVertexPaintFunction(new PickableVertexPaintFunction(pr,
				Color.lightGray, Color.white, Color.yellow));
		pr.setGraphLabelRenderer(new DefaultGraphLabelRenderer(Color.cyan,
				Color.cyan));
		VertexIconAndShapeFunction dvisf = new VertexIconAndShapeFunction(
				new EllipseVertexShapeFunction());
		pr.setVertexShapeFunction(dvisf);
		pr.setVertexIconFunction(dvisf);
		vv = new VisualizationViewer(new FRLayout(graph), pr);
		vv.setPickSupport(new ShapePickSupport());
		pr.setEdgeShapeFunction(new EdgeShape.QuadCurve());
		vv.setBackground(Color.white);

		// add my listener for ToolTips
		vv.setToolTipFunction(new DefaultToolTipFunction());

		// create a frome to hold the graph
		final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
		this.add(panel);
		// this.add(vv);
		final ModalGraphMouse gm = new DefaultModalGraphMouse();
		vv.setGraphMouse(gm);

		// showLabels = true;
	}

	class UnicodeVertexStringer implements VertexStringer {

		Map<Vertex, String> map = new HashMap<Vertex, String>();

		public UnicodeVertexStringer(Vertex[] vertices, List<String> labels) {
			for (int i = 0; i < vertices.length; i++) {
				map.put(vertices[i], labels.get(i));
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see edu.uci.ics.jung.graph.decorators.VertexStringer#getLabel(edu.uci.ics.jung.graph.Vertex)
		 */
		public String getLabel(ArchetypeVertex v) {
			if (showLabels) {
				return (String) map.get(v);
			} else {
				return "";
			}
		}
	}

	class UsenetVertex extends DirectedSparseVertex {
		private String label;

		public UsenetVertex(String label) {
			this.label = label;
		}

		@Override
		public String toString() {
			return label;
		}
	}

	/**
	 * create edges for this demo graph
	 * 
	 * @param peopleList
	 * 
	 * @param v
	 *            an array of Vertices to connect
	 * @return
	 * @throws UNISoNException
	 */
	Vertex[] createEdges(List<Relationship> links, LinkedList<String> peopleList)
			throws UNISoNException {
		Vertex[] v = new Vertex[peopleList.size()];
		for (int i = 0; i < peopleList.size(); i++) {
			v[i] = graph.addVertex(new UsenetVertex(peopleList.get(i)));
		}
		for (Relationship link : links) {
			try {
				//subtract one as index starts at 0 for arrays
				graph.addEdge(new DirectedSparseEdge(v[link.getOwner()-1], v[link
						.getTarget()-1]));
			} catch (ArrayIndexOutOfBoundsException e) {
				throw new UNISoNException("Array out of bounds: size "
						+ v.length + " vs " + link.getOwner() + " or "
						+ link.getTarget());

			}
		}
		return v;
	}

	/**
	 * a driver for this demo
	 * 
	 * @throws UNISoNException
	 */
	public static void main(String[] args) throws UNISoNException {
		JFrame frame = new JFrame();
		frame.setVisible(true);
		LinkedList<String> posters = new LinkedList<String>();
		String LUCY = "Lucy";
		posters.add(LUCY);
		String JESS = "Jess";
		posters.add(JESS);
		String MIC = "Mic";
		posters.add(MIC);
		String VIC = "Vic";
		posters.add(VIC);
		String DAN = "Dan";
		posters.add(DAN);
		String STEVE = "Steve";
		posters.add(STEVE);

		Relationship link = new Relationship(posters.indexOf(STEVE), posters
				.indexOf(LUCY));

		List<Relationship> links = new Vector<Relationship>();
		links.add(link);
		link = new Relationship(posters.indexOf(STEVE), posters.indexOf(MIC));
		links.add(link);

		GraphPreviewPanel panel = new GraphPreviewPanel(posters, links);
		panel.setVisible(true);
		frame.add(panel);
		frame.setSize(frame.getPreferredSize());

	}
}
