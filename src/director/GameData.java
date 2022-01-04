package director;

import Graph.api.DirectedWeightedGraph;
import Graph.api.DirectedWeightedGraphAlgorithms;
import game_client.Client;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * this object holds all the game info
 * and details for the game extracted from the server
 * extracted from client->getInfo
 */
public class GameData{

    Client curr_client; // most important - our game client
    // fields we get from the client ^^
    int pokemons_size;
    boolean is_logged_in;
    int moves;
    int grade;
    int game_level;
    String timeLeft; // string in seconds
    int max_user_level;
    int id;
    String graph_directory;
    int agents_size;

    // fields that holds all the game data structres
    // this way the consumer of this code can point everywhere in the programme via the gameData obj
    List<Pokemon> pokemons;
    List<Agent> agents;
    DirectedWeightedGraph curr_graph;
    DirectedWeightedGraphAlgorithms curr_algo;
    Loader load;

    /**
     * wont be able to construct gamedata without a working client
     * @param c - client that works with server game
     */
    public GameData(Client c) {
        this.agents = null;
        this.curr_client = c;
    }

    /**
     * @return only the unengaged pokemons (a.k.a free)
     */
    public List<Pokemon> getFreePokemons(){
        LinkedList<Pokemon> output = new LinkedList<>();
        // just loop over whole pokemon list and choose if unegaged
        for (Pokemon poki : this.pokemons){
            if (!poki.engaged){
                output.addLast(poki);
            }
        }
        return output;
    }

    /**
     * update its own fields via loader methods
     */
    public void self_update(boolean agentFlag, boolean pokFlag){
        if (agentFlag || pokFlag){
            load.updateGameData(this, agentFlag, pokFlag);
        }
        Collections.sort(this.pokemons); // sort pokemons list from the highest value to the lowest one
    }

    /** --------------------------------- GETTERS AND SETTERS ---------------------------------------*/
    public int getPokemons_size() {
        return pokemons_size;
    }

    public void setPokemons_size(int pokemons_size) {
        this.pokemons_size = pokemons_size;
    }

    public boolean isIs_logged_in() {
        return is_logged_in;
    }

    public void setIs_logged_in(boolean is_logged_in) {
        this.is_logged_in = is_logged_in;
    }

    public int getMoves() {
        return moves;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getGame_level() {
        return game_level;
    }

    public void setGame_level(int game_level) {
        this.game_level = game_level;
    }

    public int getMax_user_level() {
        return max_user_level;
    }

    public void setMax_user_level(int max_user_level) {
        this.max_user_level = max_user_level;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGraph_directory() {
        return graph_directory;
    }

    public void setGraph_directory(String graph_directory) {
        this.graph_directory = graph_directory;
    }

    public int getAgents_size() {
        return agents_size;
    }

    public void setAgents_size(int agents_size) {
        this.agents_size = agents_size;
    }

    public List<Pokemon> getPokemons() {
        return pokemons;
    }

    public void setPokemons(List<Pokemon> pokemons) {
        this.pokemons = pokemons;
    }
    public void setAgents(List<Agent> agents) {
        this.agents = agents;
    }
    public List<Agent> getAgents() {
        return agents;
    }

    public Client getCurr_client() {
        return curr_client;
    }

    public void setCurr_client(Client curr_client) {
        this.curr_client = curr_client;
    }

    public DirectedWeightedGraph getCurr_graph() {
        return curr_graph;
    }

    public void setCurr_graph(DirectedWeightedGraph curr_graph) {
        this.curr_graph = curr_graph;
    }

    public DirectedWeightedGraphAlgorithms getCurr_algo() {
        return curr_algo;
    }

    public void setCurr_algo(DirectedWeightedGraphAlgorithms curr_algo) {
        this.curr_algo = curr_algo;
    }

    public Loader getLoad() {
        return load;
    }

    public void setLoad(Loader load) {
        this.load = load;
    }

    public String getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(String timeLeft) {
        this.timeLeft = timeLeft;
    }
}

