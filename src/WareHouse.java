
public class WareHouse {
    private int numberBoxesAvailables;
    private double latitude;
    private double longitude;
    private double distanceBuildingsTruck =0;

    public WareHouse(int numberBoxesAvailables, double latitude, double longitude) {
        this.numberBoxesAvailables = numberBoxesAvailables;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getNumberBoxesAvailables() {
        return numberBoxesAvailables;
    }

    public void setNumberBoxesAvailables(int numberBoxesAvailables) {
        this.numberBoxesAvailables = numberBoxesAvailables;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
    public double getDistanceBuildingsTruck() {
        return distanceBuildingsTruck;
    }

    public void setDistanceBuildingsTruck(double distanceBuildingsTruck) {
        this.distanceBuildingsTruck = distanceBuildingsTruck;
    }
}

