/*
*la classe pour creer l'objet Batiment d'entreposage
*/
public class BatimentEntreposage {
    private int nombreBoitesDisponibles;
    private double latitude;
    private double longitude;
    public static final BatimentEntreposage VALEUR_ABSENTE = new BatimentEntreposage(0, 0.0, 0.0);

    public BatimentEntreposage(int nombreBoitesDisponibles, double latitude, double longitude) {
        this.nombreBoitesDisponibles = nombreBoitesDisponibles;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getNombreBoitesDisponibles() {
        return nombreBoitesDisponibles;
    }

    public void setNombreBoitesDisponibles(int nombreBoitesDisponibles) {
        this.nombreBoitesDisponibles = nombreBoitesDisponibles;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}

