package analyzer;
import data.Movie;
import data.Reviewer;
import graph.Graph;
import graph.GraphAlgorithms;
import sun.util.resources.cldr.zh.CalendarData_zh_Hans_HK;
import util.DataLoader;

import java.util.*;

public class MovieLensAnalyzer {

	public static void main(String[] args){
		// Your program should take two command-line arguments: 
		// 1. A ratings file
		// 2. A movies file with information on each movie e.g. the title and genres		
		if(args.length != 2){
			System.err.println("Usage: java MovieLensAnalyzer [ratings_file] [movie_title_file]");
			System.exit(-1);
		}		
		// FILL IN THE REST OF YOUR PROGRAM
		System.out.println("The files being analyzed are: \n" + "src/ml-latest-small/" + args[0] + "\n" + "src/ml-latest-small/" + args[1]);

		for(int i = 0 ; i < args.length; i++) {
			if("ratings.csv".equals(args[i])) {
				graphByRatings("src/ml-latest-small/" + args[0], "src/ml-latest-small/" + args[1]);
			}
		}
	}

	/**
	 * Private method to prompt program based on ratings.csv (doing it this way to incorporate tags.csv later)
	 * @param s
	 * @param s1
	 */
	public static void graphByRatings(String ratingPath, String moviePath) {
		//initialize
		StringBuilder sb = new StringBuilder();
		//load data from csv
		DataLoader loader = new DataLoader();
		loader.loadData(moviePath, ratingPath);
		//create prompt
		sb.append("\nThere are 3 choices for defining adjacency:\n");
		sb.append("[Option 1] u and v are adjacent if the same 12 users gave the same rating to both movies\n");
		sb.append("[Option 2] u and v are adjacent if the same 12 users watched both movies (regardless of rating)\n");
		sb.append("[Option 3] u is adjacent to v if at least 33.0% of the users that rated u gave the same rating to v\n");
		sb.append("Choose an option to build the graph (1-3): ");
		//print prompt
		System.out.print(sb.toString());
		//scan for input and catch input exceptions
		int userOption = getUserChoice();
		//create graph based on option chosen
		Graph<Integer> g;
		if (userOption == 1) {
			g = createByRatings(loader);
			showGraphInformation(g, loader);
		} else if (userOption == 2) {
			g = createByViews(loader);
			showGraphInformation(g, loader);
		} else {

		}

	}

	private static int getUserChoice() {
		Scanner sc = new Scanner(System.in);
		int userOption;
		try {
			userOption = sc.nextInt();
		} catch (InputMismatchException e) {
			throw new InputMismatchException("Wrong format, please pick a number");
		}
		if (userOption > 4) {
			throw new IllegalArgumentException("Input option is greater than option range");
		}

		return userOption;
	}

	private static void showGraphInformation(Graph<Integer> graph, DataLoader loader) {
		displayGraphOptions();
		int userOption = getUserChoice();


		if(userOption == 1) {
			printGraphStats(graph, loader);
		} else if(userOption == 2) {
			printNodeInfo(graph, loader);
		} else if(userOption == 3) {
			System.out.println("f");
		} else {
			System.exit(0);
		}
 	}

	private static void printNodeInfo(Graph<Integer> graph, DataLoader loader) {
		Scanner sc = new Scanner(System.in);
		Integer movieNum = 0;


		System.out.print("Enter movie ID (1-1000): " );
		try {
			movieNum = sc.nextInt();
		} catch (InputMismatchException e) {
			e.printStackTrace();
		}


		List<Integer> neighbors = graph.getNeighbors(movieNum);
		StringBuilder sb = new StringBuilder();
		sb.append("\n" + loader.getMovies().get(movieNum).toString() + "\n");
		sb.append("Neighbors: \n");

		for(Integer n : neighbors) {
			sb.append("\n	" + loader.getMovies().get(n).getTitle() + "\n" );
		}

		System.out.println(sb.toString());
		showGraphInformation(graph, loader);
	}

	private static void printGraphStats(Graph<Integer> graph, DataLoader loader) {
		StringBuilder sb = new StringBuilder();
		Map<Integer, Movie> movieMap = loader.getMovies();
		int numEdges = graph.numEdges();
		int numVertices = graph.numVertices();

		float rhs = numVertices * (numVertices-1);
		float density = numEdges / rhs;
		Movie max = findMaxDegreeMovie(graph, movieMap);

		sb.append("\nThis graph has:\n");
		sb.append("Vertices: " + numVertices + " Edges: " + numEdges + "\n");
		sb.append("A density of : " + density + "\n");
		sb.append("Highest degree node: " +  max.toString().substring(0,5) + " With degree: " + graph.degree(max.getMovieId()) + "\n");
		sb.append("Diameter is : " + findDiameter(graph));
		System.out.println(sb.toString());

		showGraphInformation(graph, loader);


	}

	//find longest shortest path
	private static String findDiameter(Graph<Integer> graph) {
		int[][] fw = GraphAlgorithms.floydWarshall(graph);

		int maxDiameter = 0;
		int node1 = 0;
		int node2 = 0;
		for(int i = 0; i < fw.length; i++) {

			for(int j = 0; j <  fw.length; j++) {
				if(fw[i][j] != Integer.MAX_VALUE/2) {

					System.out.print("Node: "+ i + " to node " + j + "\n");
					System.out.println("Distance of those nodes is: " + fw[i][j]);

					if(maxDiameter == 0) {
						maxDiameter = fw[i][j];
						node1 = i;
						node2 = j;
					} else if(fw[i][j] > maxDiameter) {
						maxDiameter = fw[i][j];
						node1 = i;
						node2 = j;
					}
				}
			}
		}
		System.out.println(maxDiameter + " from " + node1 + " to " + node2) ;
		return "Bro";
	}

	private static Movie findMaxDegreeMovie(Graph<Integer> graph, Map<Integer, Movie> movieMap) {
		Integer maxMovie = null;
		int numNode = 0;
		int ourNode = 0;
		for(Integer i : graph.getVertices()) {
			if(maxMovie == null) {
				maxMovie = i;
			} else if(graph.degree(i) > graph.degree(maxMovie)) {
				maxMovie = i;
			}

		}

		return movieMap.get(maxMovie);
	}

	private static void displayGraphOptions() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n[Option 1] Print out statistics about the graph.\n");
		sb.append("[Option 2] Print node information.\n");
		sb.append("[Option 3] Display shortest path between two nodes.\n");
		sb.append("[Option 4] Quit\n");
		sb.append("Choose an option 1-4: ");
		//print prompt
		System.out.print(sb.toString());
	}

	//[Option 1] u and v are adjacent if the same 12 users gave the same rating to both movies
	public static Graph<Integer> createByRatings(DataLoader loader) {
		Set<Integer> reviewerList = new HashSet<>();
		Graph<Integer> ratingsGraph = new Graph<>();
		Map<Integer, Movie> movieMap = loader.getMovies();
		Collection<Movie> values = movieMap.values();
		Map<Integer, Reviewer> reviewerMap = loader.getReviewers();


		System.out.print("\nCreating graph...");
		//add nodes to graph
		for(Integer movieNum : movieMap.keySet()) {
			ratingsGraph.addVertex(movieNum);
		}

		Set<Integer> vertices = new HashSet<>();
		vertices.addAll(ratingsGraph.getVertices());

		Integer vertex = null;
		//iterate through vertices
		for(Iterator<Integer> i = vertices.iterator(); i.hasNext();) {
			reviewerList.clear();
			vertex = i.next(); //copy the vertex data
			i.remove();
			//iterate through all other vertices
			for(Iterator<Integer> j = vertices.iterator(); j.hasNext();) {
				Integer comparator = j.next();

				Map<Integer, Double> ratings = movieMap.get(vertex).getRatings();
				Map<Integer, Double> comparatorRatings = movieMap.get(comparator).getRatings();


					for(Map.Entry<Integer, Double> entry : ratings.entrySet()) {
						//System.out.println("I am " + entry.getKey() + " and " + entry.toString());
						if(movieMap.get(vertex).rated(entry.getKey()) && movieMap.get(comparator).rated(entry.getKey())) {
							Double vertexRating = ratings.get(entry.getKey());
							Double comparatorRating = comparatorRatings.get(entry.getKey());

							if(vertexRating.equals(comparatorRating)) { //note: per user we compare two separate movies, adding that user to a list if they rate the two movies the same
								reviewerList.add(entry.getKey());
							}

							if(reviewerList.size() == 12 && !ratingsGraph.edgeExists(vertex, comparator)) {
								ratingsGraph.addEdge(vertex, comparator);
								ratingsGraph.addEdge(comparator, vertex); //undirected graph
							}
						}

					}
			}

		}

		System.out.print("The graph has been created!\n");

		return ratingsGraph;
	}

	//[Option 2] u and v are adjacent if the same 12 users watched both movies (regardless of rating)
	public static Graph<Integer> createByViews(DataLoader loader) {
		Graph<Integer> watchedMovies = new Graph<>();
		Map<Integer, Movie> movieMap;
		loader.printMovieList();
		movieMap = loader.getMovies();

		System.out.print("Creating graph...");
		//add nodes to graph
		for(Integer movieNum : movieMap.keySet()) {
			watchedMovies.addVertex(movieNum);
		}

		Set<Integer> vertices = new HashSet<>();
		vertices.addAll(watchedMovies.getVertices());

		Integer vertex = null;
		//iterate through vertices
		for(Iterator<Integer> i = vertices.iterator(); i.hasNext();) {
			vertex = i.next(); //copy the vertex data
			i.remove();

			//iterate through all other vertices
			for(Iterator<Integer> j = vertices.iterator(); j.hasNext();) {
				Integer comparator = j.next();

				//get the list of users and their ratings
				Map<Integer, Double> ratings = movieMap.get(vertex).getRatings();
				Map<Integer, Double> comparatorRatings = movieMap.get(comparator).getRatings();

				//find the intersect between both sets of users
				Set<Integer> intersect = new HashSet<>();
				intersect.addAll(ratings.keySet());
				Set<Integer> comparatorSet = comparatorRatings.keySet();
				intersect.retainAll(comparatorSet);

				//verify that the set is of size 12 and add edges
				if(intersect.size() == 12 && !watchedMovies.edgeExists(vertex, comparator)) {
					watchedMovies.addEdge(vertex, comparator);
					watchedMovies.addEdge(comparator, vertex);

				}

			}


		}
		System.out.println("Created");
		return watchedMovies;

	}

	//[Option 3] u is adjacent to v if at least 33.0% of the users that rated u gave the same rating to v
	public static void createByPercentage() {

	}

}
