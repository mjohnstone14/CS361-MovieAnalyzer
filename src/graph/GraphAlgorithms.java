package graph;
import java.util.*;
import util.PriorityQueue;

public class GraphAlgorithms {


    public static int[] dijkstrasAlgorithm(Graph<Integer> graph, int source) {
        Integer[] vArray = graph.getVertices().toArray(new Integer[graph.getVertices().size()]);
        ArrayList<Integer> verts = new ArrayList<Integer>(Arrays.asList(vArray));
        PriorityQueue pQueue = new PriorityQueue();
        int[] distance= new int[verts.size()];
        int[] parents= new int[verts.size()];

        for(int i=0;i<verts.size();i++) {
            if(verts.get(i)== source) {
                distance[i]= 0;
                parents[i]= -1;
            }
            else {
                distance[i]= Integer.MAX_VALUE;
                parents[i]= -1;
            }
        }

        for(int i=0;i<verts.size();i++) {
            pQueue.push(distance[i],i);
        }

        while(!pQueue.isEmpty()) {

            int currentVertex = verts.get(pQueue.topElement());
            int currentDistance = pQueue.topPriority();
            pQueue.pop();
            
            List<Integer> adjacencyList = graph.getNeighbors(currentVertex);
            for(Integer node: adjacencyList) {
                int alt;
                if(currentDistance != Integer.MAX_VALUE) {
                    alt = currentDistance + 1;
                }
                else {
                    alt = currentDistance;
                }
                if(alt<distance[node-1]) {
                    distance[node-1]= alt;
                    parents[node-1]= currentVertex;
                    pQueue.changePriority(node-1, alt);
                }
            }
        }
        return parents;
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

        for(int i = 1 ; i <= 2; i++) {
            int[] ints = dijkstrasAlgorithm(g, i);
            System.out.println(Arrays.toString(ints));
        }


    }






    // FILL IN
}
