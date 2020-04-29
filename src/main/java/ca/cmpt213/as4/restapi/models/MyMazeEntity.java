package ca.cmpt213.as4.restapi.models;
/*
    MyMazeEntity represents basic maze entities: WALL AND OPEN
 */
public enum MyMazeEntity implements MazeEntity {
    WALL (1),
    OPEN(2);
    private final int type;
    MyMazeEntity(int type){
        this.type = type;
    }
    public boolean isWall(){
        return this.type == WALL.type;
    }
}
