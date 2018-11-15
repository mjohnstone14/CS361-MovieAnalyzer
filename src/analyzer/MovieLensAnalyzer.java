package analyzer;
import graph.Graph;
import util.DataLoader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

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
		Scanner sc = new Scanner(System.in);
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
		int userOption = 0;
		try {
			userOption = sc.nextInt();
		} catch (InputMismatchException e) {
			throw new InputMismatchException("Wrong format, please pick a number");
		}
		if (userOption > 3) {
			throw new IllegalArgumentException("Input option is greater than option range");
		}
		sc.close();

		Graph<Integer> g = new Graph<>();
		if (userOption == 1) {
			g = createByRatings();
			loader.printMovieList();
			loader.printReviewerList();
		} else if (userOption == 2) {

		} else {

		}

	}



	//[Option 1] u and v are adjacent if the same 12 users gave the same rating to both movies
	public static Graph<Integer> createByRatings() {
		Graph<Integer> ratingsGraph = new Graph<>();


		return null;
	}

	//[Option 2] u and v are adjacent if the same 12 users watched both movies (regardless of rating)
	public static void createByViews() {

	}

	//[Option 3] u is adjacent to v if at least 33.0% of the users that rated u gave the same rating to v
	public static void createByPercentage() {

	}

}
