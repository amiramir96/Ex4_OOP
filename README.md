# Ex2-OOP
Object Oriented Programming exercise 2<br>
## Program Overview
in this assigment we were required to implement Directed Weighted Garph and some choosen algorithms via our teacher Interfaces : https://github.com/amiramir96/Ex2-OOP/tree/main/src/api <br> 
the directed weigthed graph/algorithms objects shall implemented within the best time run as possible since its can hold alot of vertex and edges. <br>
in addition, we shall create a GUI programme that support every algorithm that implemented on the graph (for ex, load graph, isConnected, tsp etc..) <br>

Our implementation works in O(1) for all basic operations on the graph (get/add/remove node/edge) and is very modular- all data transfer between classes is done through the api.

## structre of the project code
the project splits to packges: api (interfaces of our teacher, as explained above), FileWorkout, impGraph, graphAlgo, graphics, tests (will be explained below, not in this topic)

|**package name:**|                                                     **Description**                                                                                      |
|-----------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|
| **api**         |    interfaces of graph, node, edge, point(geolocation) and graph algorithms.                                                                             | 
| **FileWorkout** |     implement DFS algorithm, for more details on DFS <br> please look at "Review of the Literature" at the bottom of the readme                          |   
|  **impGraph**   |     implmentation of all interfaces from api package, also, hold some inner class to manage threads, iterators                                           |   | **graphAlgo**   |    hold implementation for the known algorithms DFS, Dijkstra to support the graphAlgorithms methods                                                     |
|  **graphics**   |    represent all the GUI classes, with constructing the Window class, the GUI gets open for the user usage                                               |
| **Tests**       |   2 categories - 1) Currectness: test each of the public class methods to return currect answer, 2) RunningTime - testing only algorithms for big graphs | 
| **JsonFiles**   |   Concentrate all the json files at the same package                                                                                                     |

## Tests
The "PerformanceTest.jar" file is used totest the performaence of the algorithm on graphs with different sizes.  
It can be called from the cmd or be given the number of nodes for the graph.  
`java -jar PerformanceTest.jar` or `java -jar PerformanceTest.jar 200` for example.  
The program will create a random graph with the specified number of nodes in the folder and test the elapsed time of multiple implemented methods on that graph.
<br>

## graphics - GUI 
via the GUI, the use can do every action he could from the code via using DirectedWeightedGraphAlgorithms interface commands <br>
supports loading, saving graph from json files <br>
supports add, delete commands to node, edges and create new Directed Weigthed Graph <br>

### logic system
each class hold a role: <br>
1. window - hold the Frame that we draw on <br>
2. drawGarph (a.k.a drawer) - responsible to paint and update himself via outside inputs (menubar - user methods commands, mouse moveing) <br>
3. menu - get the user orders via clicking on toolbar features, and transform the command click on the bar to: <br>
     a. actions on the graph and the graph algo objects. <br>
     b. execute the relevant proccess with the drawer (init the currect details/params and use repaint command)


### how to use / tutorial
MenuBar - the user can execute all the features of the algorithms, editting the graph nodes/edges and load/save graph <br>
For any INVALID input, a popup msg will appear and will explain what the currect format that require to be sent next time <br>
the gui support mouse inputs as moving screen and zooming in/out the picture<br>
defualt colors is BLACK for node, BLUE for edge, for example: <img src="https://user-images.githubusercontent.com/89981387/145404360-8810e39f-1229-4eb7-905a-157ee689cb6c.jpg" width="75" height="75">
 <br>
any other kind of color to the graph will represent occure of one of the algorithms command! <br>
<br>File category <br>
load graph - choose json file from directory and load it while creating new graph. <br>
save graph - choose folder at directory to extract to the graph to json file. <br>
<br>
Algo_Command category<br>
isConnected - true = all nodes drawn to GREEN, false - RED <br> 
<img src="https://user-images.githubusercontent.com/89981387/145404367-044e39c2-d637-47bb-a5be-b0048b3cdb26.jpg" width="200" height="200">
<img src="https://user-images.githubusercontent.com/89981387/145405687-61ce5384-af8e-4df9-b4fb-f6c472093beb.png" width="200" height="200">

<br>
center - the center node drawn to bright yellow, if garph is not connected - popup a msg to the user. <br> 
<img src="https://user-images.githubusercontent.com/89981387/145404369-5aed4b73-202c-47dd-bcb6-f65bd2297d18.jpg" width="200" height="200">
 <br>

shortestPathDist - ask from user input via format "node_id,node_id" (int,int) and draw green for nodes, pink for edges(if there is no path, nothing will happen)  and popup msg with the distance of the path <br>
shortestPath - same as above, diff colors (pink for nodes, red for edges).<br>
<img src="https://user-images.githubusercontent.com/89981387/145404370-394acdf5-5b63-4040-8c51-d7f92470a070.jpg" width="200" height="200">
<br> 


tsp - ask from user input via format "node_id,node_id,....,node_id" (int,int,...,int), if there is solution -> draw nodes in green and add string to the node_id with 
the station idx along the road, edges with pink \ if there is no solution - draw all nodes of the input to red. <br> <img src="https://user-images.githubusercontent.com/89981387/145404376-369f8dc4-c9af-4691-b4d4-47bc835d6d11.jpg" width="200" height="200">
<img src="https://user-images.githubusercontent.com/89981387/145405679-e9442608-f8d6-4a7e-83ba-525e38cedc3f.png" width="200" height="200">
<br>

Graph_Management category <br>
add node - ask for user input via "node_id,x,y" (int,float,float) and add node to the currect graph <br>
add edge - same as above, valid input is: "node_id_src,node_id_dest,weight" ("int,int,float") <br>
del node - input is "node_id" (int), the node will be deleted from graph and screen <br>
del edge - as above, valid input is: "node_id_src,node_id_dest" (int,int)
create new graph - remove the currect graph and start a clean new graph (zero nodes and edges)
<br>


## Running The Simulation
Running the simulation is performed as instracted, by calling `java -jar Ex2.jar Graph_Name.json` for a graph in the folder (or in nested 'data' folder).
The program will open a window with visual representation of the graph and menu bar with different algorithms that can be triggered.
<br>


## Running Time Results
<br>

|**Node_size**|**Edge_size**|**construct graph**|**isConnected**   |**shortestPath**  | **shortestPathDist** |  **center**         | **tsp for 20 nodes** |
|-------------|-------------|-------------------|------------------|------------------|----------------------|---------------------|----------------------|
|    100      |    2000     |30 ms              |13 ms             |107 ms            | 5 ms                 | 259 ms              | 12 ms                |
|   1,000     |  20,000     | 31ms              | 43 ms            | 150 ms           | 15 ms                | 1 second            | 62 ms                | 
| 10,000      | 200,000     |121 ms             |less than 1 second| 532 ms           | 140 ms               | 337 seconds         |     2 seconds        |
| 100,000     | 2,000,000   |1 seconds          | 1 second         | 2 seconds        |       2 seconds      |timeout(apx 84 hours)| 34 seconds           |
| 1,000,000   |  20,000,000 |18 seconds         |   23 seconds     | 28 seconds       |  28 seconds          |timeout(apx 10 years)| 577 seconds          |



<br>

## Assigment instructions
The instructions for this assignment can be found here (Hebrew):  
https://docs.google.com/document/d/17h5VGIHtqWHrzgoRjH05_PjHgCn8-EDcecrkR9sVChQ/edit
<br>

## Review of the Literature
DFS - https://en.wikipedia.org/wiki/Depth-first_search , https://www.youtube.com/watch?v=7fujbpJ0LB4&t=10s <br>
Dijksta - https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm , https://www.youtube.com/watch?v=pSqmAO-m7Lk&t=776s <br>
GUI via Swing - https://www.youtube.com/watch?v=Kmgo00avvEw&t=7097s <br>
tsp algorithms (which helped to get ideas) - https://www.youtube.com/watch?v=M5UggIrAOME , https://en.wikipedia.org/wiki/Travelling_salesman_problem <br>
mathematical way to draw arrow - https://stackoverflow.com/questions/2027613/how-to-draw-a-directed-arrow-line-in-java comment 19 <br>
thread usage - https://www.youtube.com/watch?v=r_MbozD32eo&t=517s <br>
iterator usage - https://stackoverflow.com/questions/9200080/join-multiple-iterators-in-java?lq=1 comment 0 <br>
special material - https://github.com/ShaiAharon/OOP_19 credit to our teacher Shai.Aharon, helped alot with GUI and threads as well <br>
