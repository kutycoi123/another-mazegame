package ca.cmpt213.as4.restapi.models;
/*
 * GameState represents state of a game
 * RUNNING: game is still in progress
 * WINNING: game is won
 * LOSING: game is lost
 * CHEESE_COLLECTING: some cheeses are just collected
 */
public enum GameState {
    RUNNING,
    WINNING,
    LOSING,
    CHEESE_COLLECTING
}
