package ca.cmpt213.as4.restapi.models;

/*
 * MazeEntity defines a generic interface for any kind of maze entity
 * This allows us to have a very generic maze game with multiple different entities.
 */
public interface MazeEntity {
    public boolean isWall();
}
