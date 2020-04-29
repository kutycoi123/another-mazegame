package ca.cmpt213.as4.restapi.models;
/*
 * EntityPosition is responsible for representing a specific cell/position in maze
 * It has two main attributes: row and col
 */
public class EntityPosition {
    private int y, x;

    public EntityPosition(int y, int x){
        this.y = y;
        this.x = x;
    }

    public int getYCoordinate(){
        return y;
    }

    public int getXCoordinate(){
        return x;
    }

    public void setYCoordinate(int y) {
        this.y = y;
    }

    public void setXCoordinate(int x) {
        this.x = x;
    }


    public boolean equals(EntityPosition entity){
        if (y == entity.getYCoordinate() && x == entity.getXCoordinate())
            return true;
        else
            return false;
    }

    public void updatePos(EntityPosition newPos){
        this.setYCoordinate(newPos.getYCoordinate());
        this.setXCoordinate(newPos.getXCoordinate());
    }

}
