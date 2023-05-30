public class camion {
    private int capaciteMaxCamion ;
    private double latitude;
    private double longitude;
    private int espaceDisponibleCamion;
    private int nombreBoiteDansCamion;
    public camion(int capaciteMaxCamion,double longitude,double latitude){
        this.capaciteMaxCamion = capaciteMaxCamion;
        this.latitude = latitude;
        this.longitude = longitude;
        this.nombreBoiteDansCamion= nombreBoiteDansCamion;
        espaceDisponibleCamion= capaciteMaxCamion-nombreBoiteDansCamion;
    }
    public int getNombreBoiteDansCamion() {
        return nombreBoiteDansCamion;
    }

    public void setNombreBoiteDansCamion(int nombreBoiteDansCamion) {
        this.nombreBoiteDansCamion = nombreBoiteDansCamion;
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
    public int getEspaceDisponibleCamion(){return espaceDisponibleCamion;}
    public int getCapaciteMaxCamion(){return capaciteMaxCamion;}
}
