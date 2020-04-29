package ca.cmpt213.as4.restapi.ApiWrapperObjects;

import ca.cmpt213.as4.restapi.models.MazeGame;

import java.util.List;

public class ApiBoardWrapper {
    public int boardWidth;
    public int boardHeight;
    public ApiLocationWrapper mouseLocation;
    public ApiLocationWrapper cheeseLocation;
    public List<ApiLocationWrapper> catLocations;
    public boolean[][] hasWalls;
    public boolean[][] isVisible;

    // MAY NEED TO CHANGE PARAMETERS HERE TO SUITE YOUR PROJECT
    public static ApiBoardWrapper makeFromGame(MazeGame game) {
        ApiBoardWrapper wrapper = new ApiBoardWrapper();

        // Populate this object!
        wrapper.boardHeight = game.getMazeHeight();
        wrapper.boardWidth = game.getMazeWidth();
        wrapper.mouseLocation = ApiLocationWrapper.makeFromCellLocation(game.getMousePos());
        wrapper.cheeseLocation = ApiLocationWrapper.makeFromCellLocation(game.getCheesePos());
        wrapper.catLocations = ApiLocationWrapper.makeFromCellLocations(game.getCatsPos());
        wrapper.hasWalls = game.getBooleanMaze();
        wrapper.isVisible = game.getExplored();
        return wrapper;
    }
}
