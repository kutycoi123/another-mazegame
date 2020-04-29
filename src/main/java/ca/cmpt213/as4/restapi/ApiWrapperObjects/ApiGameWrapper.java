package ca.cmpt213.as4.restapi.ApiWrapperObjects;

import ca.cmpt213.as4.restapi.models.GameState;
import ca.cmpt213.as4.restapi.models.MazeGame;

public class ApiGameWrapper {
    public int gameNumber;
    public boolean isGameWon;
    public boolean isGameLost;
    public int numCheeseFound;
    public int numCheeseGoal;

    // MAY NEED TO CHANGE PARAMETERS HERE TO SUITE YOUR PROJECT
    public static ApiGameWrapper makeFromGame(MazeGame game, int id) {
        ApiGameWrapper wrapper = new ApiGameWrapper();
        wrapper.gameNumber = id;
        // Populate this object!
        wrapper.numCheeseFound = game.getCheeseCollected();
        wrapper.numCheeseGoal = game.getCheeseToWin();
        GameState state = game.getState();
        if (state == GameState.WINNING) {
            wrapper.isGameWon = true;
            wrapper.isGameLost = false;
        } else if (state == GameState.LOSING) {
            wrapper.isGameLost = true;
            wrapper.isGameWon = false;
        }
        return wrapper;
    }
}
