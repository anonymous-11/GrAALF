/**
 * 
 */
package querying;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Scanner;
import javax.swing.JFrame;
import classes.AccessCall;
import classes.ResourceItem;
import controlClasses.RuntimeVariables;
import dataBaseStuff.GraphDBDal;
import edu.uci.ics.jung.graph.Graph;
import exceptions.VariableNoitFoundException;
import helpers.ColorHelpers;
import helpers.DescribeFactory;
import mainPackage.GraphPanel;
import querying.QueryInterpreter;
import querying.adapters.BaseAdapter;
import querying.adapters.memory.InMemoryAdapter;
import querying.adapters.simpleNeo4J.SimpleNeo4JAdapter;
import querying.adapters.simplePG.SimplePGAdapter;
import querying.parsing.ParsedQuery;
import java.util.*;

/**
 * @author author
 *
 */
public class QueryProcessor implements Runnable {

	private boolean MemQuery, SimplePGQuery, SimpleNeo4JQuery;
	public Long num_edges, num_vertex;
	Graph<ResourceItem, AccessCall> theGraph;
	boolean ShowGraph;
	boolean ShowVerbose;
	String fileAdr;
	GraphDBDal GraphActionFactory;

	/**
	 * 
	 */
	public QueryProcessor(boolean MemQuery, boolean SimplePGQuery, boolean SimpleNeo4JQuery, Long num_edges,
			Long num_vertex, Graph<ResourceItem, AccessCall> theGraph, boolean ShowGraph, boolean ShowVerbose,
			String fileAdr, GraphDBDal GraphActionFactory) {
		// TODO Auto-generated constructor stub
		this.MemQuery = MemQuery;
		this.SimplePGQuery = SimplePGQuery;
		this.SimpleNeo4JQuery = SimpleNeo4JQuery;
		this.num_edges = num_edges;
		this.num_vertex = num_vertex;
		this.theGraph = theGraph;
		this.ShowGraph = ShowGraph;
		this.ShowVerbose = ShowVerbose;
		this.fileAdr = fileAdr;
		this.GraphActionFactory = GraphActionFactory;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		BaseAdapter queryMachine = null;

		/// set the query adapter
		if (MemQuery)
			queryMachine = InMemoryAdapter.getSignleton();
		else if (SimplePGQuery)
			queryMachine = SimplePGAdapter.getSignleton();
		else if (SimpleNeo4JQuery)
			queryMachine = SimpleNeo4JAdapter.getSignleton();

		Graph<ResourceItem, AccessCall> theLocalGraph = null;

		Map<Integer, Thread> theThreadMap = new HashMap<Integer, Thread>();

		/// setup GUI window
		GraphPanel theGraphWindow = null;
		JFrame frame1 = new JFrame();

		Scanner reader = new Scanner(System.in);
		long num_vertices = 0;
		Integer asyncID = 0;
		while (true) {

			try {
				ColorHelpers.PrintBlue("$$>>");
				String command = reader.nextLine();
				if (command.equalsIgnoreCase("exit") || command.equalsIgnoreCase("quit"))
					break;

				else if (command.trim().equalsIgnoreCase("info")) {
					ColorHelpers.PrintGreen(
							String.format("Total Edges : %d \n Total Vertices : %d \r\n", num_edges, num_vertex));
					continue;
				} else if (command.trim().toLowerCase().startsWith("set ")) { // process
																				// runtime
																				// variablse
																				// settings

					RuntimeVariables.getInstance().setValue(command.trim().split(" ")[1], command.trim().split(" ")[2]);
					continue;
				} else if (command.trim().toLowerCase().startsWith("get ")) {// process
																				// runtime
																				// variablse
																				// settings
					ColorHelpers
							.PrintGreen(RuntimeVariables.getInstance().getValue(command.trim().split(" ")[1]) + "\r\n");
					continue;
				} else if (command.trim().toLowerCase().startsWith("describe")) {

					boolean isAggregated = !(command.contains(" verbose"));
					boolean hasPath = command.indexOf("path=") > 0;
					boolean hasSort = command.indexOf("orderby=") > 0;
					String thePath = hasPath ? command.substring(command.indexOf("path=") + "path=".length()) : null;
					String SortBy = hasSort ? command.substring(command.indexOf("orderby=") + "orderby=".length(),
							command.indexOf(" ", command.indexOf("orderby=")) > 0
									? command.indexOf(" ", command.indexOf("orderby="))
									: command.length())
							: null;

					DescribeFactory.doDescribe(thePath, isAggregated, SortBy);
					continue;
				}

				Instant start = Instant.now();

				try {

					if (command.startsWith("+")) {

						command = command.substring(1);

						
						AsyncQueryRunner asqr = new AsyncQueryRunner(asyncID, command, true);

						Thread T = new Thread(asqr);
						theThreadMap.put(asyncID, T);
						System.out.println("Async query started with id :  " + asyncID);
						asyncID += 1;

						T.start();
						continue;
					} else if (command.startsWith("-")) {
						Integer theID = Integer.parseInt(command.split(" ")[1]);
						theThreadMap.get(theID).stop();
						continue;
					}
					ParsedQuery query = null;
					try {
						query = QueryInterpreter.interpret(command, theLocalGraph);
					} catch (Exception ex) {
						ex.printStackTrace();
					}

					System.out.println(theGraph.getEdgeCount() + "|" + theGraph.getVertexCount());
//					if (theGraph.getVertexCount() != num_vertices) {
//						if (MemQuery) {
//							InMemoryAdapter mem = InMemoryAdapter.getSignleton();
//							mem.ClearAll();
//							System.gc();
//							Object [] tempArr =theGraph.getVertices().toArray().clone(); 
//							for (int i = 0; i < tempArr.length  ; i++) {
//								ResourceItem pick =  (ResourceItem) tempArr[i] ;
//								mem.addResourceItem(pick);
//							}
//							tempArr = theGraph.getEdges().toArray().clone();
//							for (int i = 0; i< tempArr.length ; i++) {
//								AccessCall pick = (AccessCall) tempArr[i];
//								mem.addAccessCall(pick);
//							}
//							tempArr= null;
//							System.gc();
//							
////							for (AccessCall pick : theGraph.getEdges()) {
////								mem.addAccessCall(pick);
////							}
//						}
//						num_vertices = theGraph.getVertexCount();
//					}

					theLocalGraph = queryMachine.runQuery(query);
				} catch (Exception ex) {

					System.out.println(ex.getMessage());
					ex.printStackTrace();

					ColorHelpers.PrintRed("Error evaluating the query! please check the query and run again.\n");
					continue;
				}

				Instant end = Instant.now();

				ColorHelpers.PrintBlue("in : " + Duration.between(start, end).toMillis() + "  Milli Seconds \n");

				theGraphWindow = new GraphPanel(theLocalGraph);
				if (frame1.isVisible()) {
					frame1.setVisible(false);
					frame1.dispose();
				}

				frame1 = new JFrame();
				frame1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame1.setSize(400, 400);
				if (ShowGraph || ShowVerbose) {
					theGraphWindow.vv.repaint();
					frame1.add(theGraphWindow);
					frame1.setVisible(true);
					frame1.setExtendedState(JFrame.MAXIMIZED_BOTH);
					frame1.setTitle(fileAdr);
				}

				System.out.flush();
				System.gc();

			} catch (VariableNoitFoundException ex) {
				ColorHelpers.PrintRed(ex.getMessage());
			} catch (Exception ex) {
				throw (ex);
				// ColorHelpers.PrintRed("query Problem!please try agin...
				// \r\n");
			}
		}
		GraphActionFactory.closeConnections();
		// System.out.print("\033[H\033[2J");
		ColorHelpers.PrintGreen("\nGood Luck from XXXX Lab @SomeUniversity Team!\r\n");
	}

}
