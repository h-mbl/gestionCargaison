public class Truck {
    private int maxTruckCapacity;
    private double latitude;
    private double longitude;
    private int availableSpaceTruck;
    private int numberBoxesTruck;
    public Truck(int maxTruckCapacity, double longitude, double latitude){
        this.maxTruckCapacity = maxTruckCapacity;
        this.latitude = latitude;
        this.longitude = longitude;
        this.numberBoxesTruck = numberBoxesTruck;
        availableSpaceTruck = maxTruckCapacity - numberBoxesTruck;
    }
    public int getNumberBoxesTruck() {
        return numberBoxesTruck;
    }

    public void setNumberBoxesTruck(int numberBoxesTruck) {
        this.numberBoxesTruck = numberBoxesTruck;
    }

    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double Latitude){
        this.latitude=latitude;
    }

    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude){
        this.longitude=longitude;
    }
    public int getAvailableSpaceTruck(){return availableSpaceTruck;}
    public int getMaxTruckCapacity(){return maxTruckCapacity;}
}
