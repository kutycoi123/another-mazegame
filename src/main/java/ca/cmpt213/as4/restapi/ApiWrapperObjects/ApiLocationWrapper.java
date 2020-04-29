package ca.cmpt213.as4.restapi.ApiWrapperObjects;

import ca.cmpt213.as4.restapi.models.EntityPosition;

import java.util.ArrayList;
import java.util.List;

public class ApiLocationWrapper {
    public int x;
    public int y;

    // MAY NEED TO CHANGE PARAMETERS HERE TO SUITE YOUR PROJECT
    public static ApiLocationWrapper makeFromCellLocation(EntityPosition cell) {
        ApiLocationWrapper location = new ApiLocationWrapper();
        // Populate this object!
        location.x = cell.getXCoordinate();
        location.y = cell.getYCoordinate();

        return location;
    }

    // MAY NEED TO CHANGE PARAMETERS HERE TO SUITE YOUR PROJECT
    public static List<ApiLocationWrapper> makeFromCellLocations(Iterable<EntityPosition> cells) {
        List<ApiLocationWrapper> locations = new ArrayList<>();
        // Populate this object!

        for (EntityPosition pos : cells) {
            locations.add(ApiLocationWrapper.makeFromCellLocation(pos));
        }
        return locations;
    }
}
