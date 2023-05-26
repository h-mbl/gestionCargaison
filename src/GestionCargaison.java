import java.io.*;
import java.util.*;

public class GestionCargaison {

    private static List<BatimentEntreposage> batiments = new LinkedList<>();
    private static int capaciteMaxCamion;
    public static void main(String[] args) {
        try {
            //pour lire les fichiers en args
            readFilesArgs("src/fichier1.txt");
            //trier selon le premier l'element ayant + de boites
            triSelonNbreBoite();
            //initialise la position du camion (longitude et latitude)
            camion camion = new camion(capaciteMaxCamion,batiments.get(0).getLongitude(),batiments.get(0).getLatitude());
            System.out.println("Truck position: " + camion.getLongitude() + "," + camion.getLatitude());
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/fichier2.txt"))) {
                writer.write("Truck position: " + camion.getLongitude() + "," + camion.getLatitude());
            }
            for (int i = 0; i < batiments.size();) {
                int boitesRestantes = 0;
                if (boitesRestantes == 0 && !batiments.isEmpty()) {
                    new camion(capaciteMaxCamion, batiments.get(i).getLongitude(), batiments.get(i).getLatitude());
                    //
                    //
                    for (int j = 0; j < batiments.size(); j++) {
                        int dernieredistance = 0;
                        double distance = batiments.get(i).getLongitude() - batiments.get(j).getLongitude();

                        if (distance == dernieredistance) {
                            double distanceSecond = batiments.get(i).getLatitude() - batiments.get(j).getLatitude();
                            batiments.get(j).setDistanceBatiments(distanceSecond);
                        } else {
                            batiments.get(j).setDistanceBatiments(distance);
                        }
                    }
                    boitesRestantes = camion.getNombreBoiteDansCamion() - batiments.get(i).getNombreBoitesDisponibles();
                    int NombreBoiteDansCamion = camion.getNombreBoiteDansCamion() + batiments.get(i).getNombreBoitesDisponibles();
                    camion.setNombreBoiteDansCamion(NombreBoiteDansCamion);
                    batiments.remove(i);
                    triSimple();
                    //imprime et ecris la position du camion dans le fichier
                    if (!batiments.isEmpty()) {
                        System.out.println("Distance: " + batiments.get(i).getDistanceBatiments() + " Number of boxes: " +
                                boitesRestantes + " Position: " + batiments.get(i).getLatitude() + ","
                                + batiments.get(i).getLongitude());
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/fichier2.txt"))) {
                            writer.write("Distance: " + batiments.get(i).getDistanceBatiments() + "\tNumber of boxes: " + boitesRestantes
                                    + "\tPosition: " + batiments.get(i).getLatitude() + "," + batiments.get(i).getLongitude());
                            writer.newLine();
                        }
                        //
                        //
                        i = 0;
                    }else{
                        break;
                    }

                }else{
                    break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    //les 2 methodes sont presque pareille : (

    private static void triSelonNbreBoite() {
        // Il compare les latitudes des bâtiments. Les bâtiments seront ensuite triés par ordre croissant de leur latitude.
        Comparator<BatimentEntreposage> comparator = Comparator
                //puis-je utiliser reversed()
                .comparingInt(BatimentEntreposage::getNombreBoitesDisponibles).reversed()
                .thenComparingDouble(BatimentEntreposage::getLongitude)
                .thenComparingDouble(BatimentEntreposage::getLatitude);

        triSort(comparator);


    }
    private static void triSimple() {
        // Il compare les latitudes des bâtiments. Les bâtiments seront ensuite triés par ordre croissant de leur latitude.
        Comparator<BatimentEntreposage> comparator = Comparator
                .comparingDouble(BatimentEntreposage::getDistanceBatiments).reversed()
                .thenComparingDouble(BatimentEntreposage::getLongitude)
                .thenComparingDouble(BatimentEntreposage::getLatitude);

        triSort(comparator);


    }
    public static void triSort( Comparator<BatimentEntreposage> comparator){
        for (int i = 1; i < batiments.size(); i++) {
            BatimentEntreposage current = batiments.get(i);
            int j = i - 1;

            while (j >= 0 && comparator.compare(batiments.get(j), current) > 0) {
                batiments.set(j + 1, batiments.get(j));
                j--;
            }
            batiments.set(j + 1, current);
        }
    }
    //a revoir
    public static double calculerDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }


    //la methode qui lit le fichier en Args
    //a revoir
    private static void readFilesArgs(String fichierEntree) throws IOException {

        try (BufferedReader reader = new BufferedReader(new FileReader(fichierEntree))) {
            String ligne;
            boolean isFirstLine = true;

            while ((ligne = reader.readLine()) != null) {
                //pour ignorer la première ligne
                //et recuperer les donnees de la premiere ligne
                if (isFirstLine) {
                    String[] informationsPremiereLigne = ligne.trim().split("\\s+");
                    int nombreTotalBoites = Integer.parseInt(informationsPremiereLigne[0]);
                    //cree l'objet camion
                    capaciteMaxCamion = Integer.parseInt(informationsPremiereLigne[1]);
                  //  camion camion = new camion(capaciteMaxCamion, 0, 0);
                    //passe a la ligne suivante
                    isFirstLine = false;
                    continue;
                }

                String[] elements = ligne.trim().split("\\(");
                int nombreBoites = Integer.parseInt(elements[0].trim());

                // Extraire les coordonnées entre parenthèses
                String coordonneesStr = elements[1];
                String[] coordonnees = coordonneesStr.substring(0, coordonneesStr.length() - 1).split(",");
                double latitude = Double.parseDouble(coordonnees[0].trim());
                double longitude = Double.parseDouble(coordonnees[1].trim());

                // Créer un batiment
                //l'ajoute a la liste
                BatimentEntreposage batiment = new BatimentEntreposage(nombreBoites, latitude, longitude);
                batiments.add(batiment);
            }
        }
    }
}
