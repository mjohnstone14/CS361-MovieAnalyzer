package graph;
import java.util.List;


    public class trial {
        private static final int INFINITY = Integer.MAX_VALUE/2;

        public static int[][] floydWarshall(Graph g, int INFINITY){
            int numberOfVerts = g.numVertices();
            int[][] dist = new int[numberOfVerts+1][numberOfVerts+1];

            //Initialize the dist matrix
            for(int src=1; src<=numberOfVerts; src++){
                for(int dest=1; dest<=numberOfVerts; dest++){
                    //Get all of the source nodes neighbors
                    List<Integer> neighbors = g.getNeighbors(src);
                    //if the source node is connected to the destination node
                    if(neighbors.contains(dest)){

                        dist[src][dest] = 1;
                    }
                    //The nodes aren't connected so you can't currently get from one to the other
                    else{
                        dist[src][dest] = INFINITY;
                    }
                }
            }

            //Compute the all pairs shortest paths
            for(int intermediate =1; intermediate<=numberOfVerts; intermediate++){
                for(int src=1; src<=numberOfVerts; src++){
                    for(int dest=1; dest<=numberOfVerts; dest++){
                        //If it is less costly to go through this intermediate node
                        if(dist[src][intermediate] + dist[intermediate][dest] < dist[src][dest]){
                            //Then update the matrix with this new lower cost path
                            dist[src][dest] = dist[src][intermediate] + dist[intermediate][dest];
                        }
                    }
                }
            }
            return dist;
        }


        public static void main (String [] args) {
            Graph<Integer> g = new Graph<>();
            g.addVertex(1);
            g.addVertex(2);
            g.addVertex(3);
            g.addVertex(4);
            g.addVertex(5);
            g.addEdge(1, 2);
            g.addEdge(2, 3);
            g.addEdge(3, 4);
            g.addEdge(4, 5);
            g.addEdge(1, 3);
            g.addEdge(1, 4);
            g.addEdge(3, 2);
            g.addEdge(2, 4);
            int[][] fw = floydWarshall(g,INFINITY);

            for (int i = 1; i < fw.length - 1; i++) {
                for (int j = 1; j < fw.length - 1; j++) {
                    System.out.println("i: " + i + " j: " + j + " distance between: " + fw[i][j]);
                }
            }
        }

    }