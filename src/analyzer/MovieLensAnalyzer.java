package analyzer;
import data.Movie;
import data.Reviewer;
import graph.Graph;
import util.DataLoader;
import util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
		Graph<Movie> g;
		if (userOption == 1) {
			g = createByRatings(loader);
			showGraphInformation(g);
		} else if (userOption == 2) {

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
		if (userOption > 3) {
			throw new IllegalArgumentException("Input option is greater than option range");
		}

		return userOption;
	}

	private static void showGraphInformation(Graph<Movie> graph) {
		displayGraphOptions();
		int userOption = getUserChoice();

		if(userOption == 1) {
			printGraphStats(graph);
		}
 	}

 	private static void printGraphStats(Graph<Movie> graph) {
		StringBuilder sb = new StringBuilder();

		int numEdges = graph.numEdges();
		int numVertices = graph.numVertices();

		float rhs = numVertices * (numVertices-1);
		float density = numEdges / rhs;

		sb.append("\nThis graph has:\n");
		sb.append("Vertices: " + numVertices + " Edges: " + numEdges + "\n");
		sb.append("A density of : " + density);

		System.out.println(sb.toString());


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
	public static Graph<Movie> createByRatings(DataLoader loader) {
		Set<Integer> reviewerList = new HashSet<>();
		Graph<Movie> ratingsGraph = new Graph<>();
		Map<Integer, Movie> movieMap = loader.getMovies();
		Collection<Movie> values = movieMap.values();
		Map<Integer, Reviewer> reviewerMap = loader.getReviewers();
		int degree = 0;

		System.out.print("\nCreating graph...");
		//add nodes to graph
		for(Movie mov : movieMap.values()) {
			ratingsGraph.addVertex(mov);
		}

		Set<Movie> vertices = new HashSet<>();
		vertices.addAll(ratingsGraph.getVertices());

		Movie vertex = null;
		//iterate through vertices
		for(Iterator<Movie> i = vertices.iterator(); i.hasNext();) {
			reviewerList.clear();
			vertex = i.next(); //copy the vertex data
			i.remove();
			//iterate through all other vertices
			for(Iterator<Movie> j = vertices.iterator(); j.hasNext();) {
				Movie comparator = j.next();

				Map<Integer, Double> ratings = vertex.getRatings();
				Map<Integer, Double> comparatorRatings = comparator.getRatings();


					for(Map.Entry<Integer, Double> entry : ratings.entrySet()) {

						if(vertex.rated(entry.getKey()) && comparator.rated(entry.getKey())) {
							Double vertexRating = ratings.get(entry.getKey());
							Double comparatorRating = comparatorRatings.get(entry.getKey());

							if(vertexRating.equals(comparatorRating)) { //note: per user we compare two separate movies, adding that user to a list if they rate the two movies the same
								reviewerList.add(entry.getKey());
							}

							if(reviewerList.size() == 12) {
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
	public static void createByViews() {

	}

	//[Option 3] u is adjacent to v if at least 33.0% of the users that rated u gave the same rating to v
	public static void createByPercentage() {

	}

}
