package graph;
import util.Pair;

import java.util.*;


public class GraphAlgorithms {


    public static int[] dijkstrasAlgorithm(Graph<Integer> graph, int source) {
        int[] distance = new int[graph.numVertices()];
        int[] prev = new int [graph.numVertices()];
        boolean[] beenHere = new boolean[graph.numVertices()];
        for (int i = 0; i < distance.length; i++) {
            distance[i] = Integer.MAX_VALUE;
        }
        distance[source] = 0;

        for (int i = 0; i < distance.length; i++) {
            final int next = shortestVertPath(distance, beenHere);
            beenHere[next] = true;
            List<Integer> x = graph.getNeighbors(next);
            Integer[] n = x.toArray(new Integer[x.size()]);
            for (int j = 0; j < n.length; j++) {
                final int v = n[j];
                final int d = distance[next + 1];
                if (distance[v] > d) {
                    distance[v] = d;
                }
                prev[v] = next;
            }
        }
        return prev;
    }

    private static int shortestVertPath (int [] distance, boolean [] v) {
        int x = Integer.MAX_VALUE;
        int y = -1;   // graph not connected, or no unvisited vertices
        for (int i=0; i<distance.length; i++) {
            if (!v[i] && distance[i]<x) {y=i; x=distance[i];}
            }
        return y;
        }


  public static int[][] floydWarshall(Graph<Integer> graph){
    int vertNum = graph.numVertices();
        int[][] distance = new int[vertNum+1][vertNum+1];

        //make matrix
        for(int search=1; search<=vertNum; search++){
            for(int fin=1; fin<=vertNum; fin++){ //get start node neighbors
                List<Integer> neighbors = graph.getNeighbors(search);
                //start node is connected to finish node
                if(neighbors.contains(fin)){
                    distance[search][fin] = 1;
                }
                else{
                    distance[search][fin] = Integer.MAX_VALUE/2; //node not connected to finish
                }
            }
        }

        //get shortest path for all
        for(int mid =1; mid<=vertNum; mid++){
            for(int search=1; search<=vertNum; search++){
                for(int fin=1; fin<=vertNum; fin++){
                    //If most efficient
                    if(distance[search][mid] + distance[mid][fin] < distance[search][fin]){
                        distance[search][fin] = distance[search][mid] + distance[mid][fin]; //updates matrix
                    }
                }
            }
        }

        return distance;
    }

    public static void main (String [] args){
        Graph <Integer> g = new Graph<>();
        g.addVertex(1);
        g.addVertex(2);
        g.addVertex(3);
        g.addVertex(4);
        g.addVertex(5);
        g.addEdge(1, 2);
        g.addEdge(2,3);
        g.addEdge(3,5);
        g.addEdge(5,4);
        int[][] fw = floydWarshall(g);


//        for(int i = 1; i < fw.length; i++) {
//
//            for (int j = 1; j < fw.length; j++) {
//                if (fw[i][j] != Integer.MAX_VALUE / 2) {
//
//                    System.out.print("Node: " + i + " to node " + j + "\n");
//                    System.out.println("Distance of those nodes is: " + fw[i][j]);
//                }
//            }
//        }

        for(int i = 1 ; i <= 5 ; i++) {
            int[] ints = dijkstrasAlgorithm(g, i);
            System.out.println(Arrays.toString(ints));
        }


    }






    // FILL IN
}
