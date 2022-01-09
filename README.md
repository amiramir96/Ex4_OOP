# Ex4-OOP
Object Oriented Programming exercise 4<br>
## Program Overview
in this assigment we were required to implemant the user side of Pokemon Game that will work sync with: <br>
the client provided [here](https://github.com/benmoshe/OOP_2021/tree/main/Assignments/Ex4/src/ex4_java_client) <br>
and the server side [here](https://github.com/benmoshe/OOP_2021/tree/main/Assignments/Ex4) <br> 
the pokemon world game is a directed weighted graph, while each pokemon is located on a point of the graph nodes and edges, all of that have to be represented within GUI.
and the Agents (the pokemon masters) will move around the world (the graph) and catch as many pokemons as possible in order to maximize the total agents score a.k.a grade. <br>
<br>
The restrictions is simple - dont cross a treshold of 10 moving commands per second (avergly), and dont change anything at the client class.<br>
Our principles to this project is to create:<br>
1. consistent algorithm. <br>
2. 100% sync within the server. <br>
3. gui flow is smooth as much as possible and simple for the user to understand. <br>
4. use our DirectedWeightedGraph and algorithms classes from previous assigment and stands with the above bullets. [link for prev assigment](https://github.com/amiramir96/Ex2-OOP) <br>

in the end, thats how the game looks like: <br>
![ezgif com-gif-maker](https://user-images.githubusercontent.com/89981387/148676984-2f35d222-a2e5-4d87-94cd-ab9308a7cfd4.gif)

<br>

## Architecture of the project
the project splits to the next main packges:

|**package name:**|                                                     **Description**                                                                                      |
|-----------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|
| **graph**       |    holds all the graph and algo files, worked and been test in the last assigment                                                                        | 
| **game_client** |     holds the given client of the assigment and the MainProccess file which holds the main function for the game                                      |   
|  **director**   |   holds all the game classes (e.g Agent, Pokemon) and the algorithm "brain" itself                                                                     |   
|  **graphics**   |    represent all the GUI classes and responsible to repaint the image window as long as the game is running                                               |
| **Tests**       |  holds the tests for the Graph from the prev assigment and tests for the director - the classes which we built for this assigment                          | 
| **data && json_graphs**   |   data package - json files for graphs of the GAME (sync with server jar), json_graphs for tests of the graph packeg classes                   |

###  design patterns
There are several design patterns implemented in this project, the most prominent one is "MVC".  
There are 4 packages in the project, `graph` and `game client` are the "Model" part, and they control the data flow in the program.  
`graphics` is the "View", it shows and updates the graph, agents and pokimones to the user.  
and `director` is the "Control", that decides the next action according to the data from the "Model".  

### multi threading workout
look here for more details: [multi threading wiki page](https://github.com/amiramir96/Ex4_OOP/wiki/multi-thread-workout) <br>


## algorithm overview
The algorithm implemented in this project is a greedy algorithm.  
for every new pokimone, every agent will calculate it's fastest route to the pokimone from the last node of it's current path (say if the agent's path is 1-4-2-3 it will calculate the distance from 3 to the new pokimone)  
The agent that will get to the pokimone the fastest (overall path/ agent speed) will engage the pokimone.  
we find the shortest path to the pokimone using the dijkstra algorithm.  
for more details click here:  [Algorithm overview](https://github.com/amiramir96/Ex4_OOP/wiki/Algorithm#algorithm-overview)
<br>

## graphics - GUI 
this time, the GUI opens, runs and stops automatically from the moment the game starts till its ends. <br>
the user doesnt have a control over anythin in the window beside the option to stop the game via exiting the game window:<br>
     stop the game meanwhile running, can happen by clieck: Menu (at the menu toolbar) -> exit. <br>
<br>
for more details about the GUI (symbol explanation, and full scenario clip) [click here](https://github.com/amiramir96/Ex4_OOP/wiki/GUI)
<br>


## Running The Game
Running the game is performed as instracted, by calling `java -jar Ex4_Server_v0.0.jar <stage_num [0,15]>` for activating the server within scenario number (from 0 to 15 include). <br>
to connect with our program as client, please run `java -jar director_client_V1.2.jar` to activate the program and the gui as well. <br>
both of the jar files shall be at the same folder and be running from thats folder. <br>

## Release 
if u would like to just play without handling any code, u can get only the server game and client via downloading the Release V1.2, which holds both of the named jar files <br>
<br>

## Scenario Results
<br>
Results on the provided server are recorded here: https://github.com/amiramir96/Ex4_OOP/wiki/Results#scenario-results
<br>

## Assigment instructions
The instructions for this assignment can be found here (Hebrew):  
https://docs.google.com/document/d/1LrXIX2pLvRIVHdSqVIimCCxL7UBMaogAcLKfr2dOjHk/edit
<br>

## Review of the Literature
tsp algorithms (which helped to get ideas) - https://www.youtube.com/watch?v=M5UggIrAOME , https://en.wikipedia.org/wiki/Travelling_salesman_problem <br>
mathematical way to draw arrow - https://stackoverflow.com/questions/2027613/how-to-draw-a-directed-arrow-line-in-java comment 19 <br>
thread usage - https://www.youtube.com/watch?v=r_MbozD32eo&t=517s <br>
about threads sync - https://www.studytonight.com/java/synchronization.php <br>
GUI via Swing (little course in youtube) - https://www.youtube.com/watch?v=Kmgo00avvEw&t=7097s <br>

