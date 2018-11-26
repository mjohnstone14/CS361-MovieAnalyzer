package graph;
import util.Pair;

import java.util.List;
import java.util.Arrays;
import java.util.PriorityQueue;



public class GraphAlgorithms {

    public static int[] dijkstrasAlgorithm(Graph<Integer> graph, int source){
      int[] distance = new int[graph.numVertices()];
      int[] prev = new int[graph.numVertices()];
      PriorityQueue<Pair> queue = new PriorityQueue<>();

      Arrays.fill(distance, Integer.MAX_VALUE/2);
      distance[source] = 0;
      queue.offer(new Pair(source, 0));

      while (!queue.isEmpty()) {
          Pair current = queue.poll();
          for (Integer n :  graph.getNeighbors((Integer)current.priority)){
              int weight = 1;
              if (distance[(Integer) current.priority] != Integer.MAX_VALUE/2 && weight < distance[n]) {
                  distance[n] = weight;
                  prev[n] = (Integer) current.priority;
                  Pair newDistance = new Pair(n, weight);
                  if (queue.contains(newDistance)) {
                      queue.remove(newDistance);
                  }
                  queue.offer(newDistance);
              }
          }
      }

      return distance;

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



	// FILL IN
}
