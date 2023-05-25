import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GestionCargaison {
    private static List<BatimentEntreposage> batiments = new ArrayList<>();
    private static int capaciteMaxCamion;
    private static int nombreTotalBoites;
    private static int boitesChargees;

    public static void main(String[] args) {
        //gestion des arguments
        if (args.length < 2) {
            System.out.println("Veuillez fournir les noms de fichiers d'entrée et de sortie.");
            return;
        }

        String fichierEntree = args[0];
        String fichierSortie = args[1];


        try {
            /*
             *lire le fichier passé en argument
             * créer des objets
             * ajouter les objets dans une liste des batiments
             */
            batiments = lireDonneesDepuisFichier("src/fichier1.txt");

            //BatimentEntreposage batimentInitial = Collections.max(batiments, Comparator.comparingInt
            // (BatimentEntreposage::getNombreBoitesDisponibles));

            // Recherche du bâtiment avec le plus grand nombre de boîtes
            if (!batiments.isEmpty()) {
                //utilise le comparator pour comparer les objets batiments
                BatimentEntreposage batimentInitial = Collections.max(batiments,
                        Comparator.comparingInt(BatimentEntreposage::getNombreBoitesDisponibles));
                //initialise la position du camion
                double camionLatitude = batimentInitial.getLatitude();
                double camionLongitude = batimentInitial.getLongitude();
                //ecris la position initial du camion dans le fichier
                System.out.println("Truck position: " + camionLatitude + "," + camionLongitude);
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/fichier2.txt"))) {
                    writer.write("Truck position: " + camionLatitude + "," + camionLongitude);
                    writer.newLine();

                    // Boucle de chargement des boîtes dans le camion
                    boitesChargees = 0;
                    while (boitesChargees < capaciteMaxCamion && nombreTotalBoites > 0) {
                        // Affichage des bâtiments proches et des boîtes restantes
                        BatimentEntreposage batimentProche = rechercherBatimentProche(camionLatitude,
                                camionLongitude, batiments);
                        double distance = calculerDistance(camionLatitude, camionLongitude,
                                batimentProche.getLatitude(), batimentProche.getLongitude());
                        int boitesRestantes = batimentProche.getNombreBoitesDisponibles();
                        System.out.println("Distance: " + distance + " Number of boxes: " +
                                boitesRestantes + " Position: " + batimentProche.getLatitude() + ","
                                + batimentProche.getLongitude());

                        writer.write("Distance: " + distance + "\tNumber of boxes: " + boitesRestantes
                                + "\tPosition: " + batimentProche.getLatitude() + "," + batimentProche.getLongitude());
                        writer.newLine();

                        // Chargement des boîtes dans le camion
                        int boitesdjacharger = (int) Math.min(capaciteMaxCamion - boitesChargees, boitesRestantes);
                        //incremente le boite a chag
                        boitesChargees += boitesdjacharger;
                        System.out.println(boitesChargees);
                        //decremente le nombre de total de boite
                        nombreTotalBoites -= boitesdjacharger;
                        //  batimentProche.setNombreBoitesDisponibles(boitesRestantes - boitesdjacharger);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                // System.out.println(nombreTotalBoites);

                System.out.println("Total boxes loaded: " + boitesChargees);
            }else {
                System.out.println("Aucun Batiment d'entreposage trouvé");
            }
            // Utilisez l'objet batimentInitial comme nécessaire
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<BatimentEntreposage> lireDonneesDepuisFichier(String fichierEntree) throws IOException {
        List<BatimentEntreposage> batiments = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fichierEntree))) {
            String ligne;
            //pour ignorer la première ligne
            boolean isFirstLine = true;

            while ((ligne = reader.readLine()) != null) {
                if (isFirstLine) {
                    String[] informationsPremiereLigne = ligne.trim().split("\\s+");
                    nombreTotalBoites = Integer.parseInt(informationsPremiereLigne[0]);
                    capaciteMaxCamion = Integer.parseInt(informationsPremiereLigne[1]);
                    isFirstLine = false;
                    continue; // Ignorer la première ligne
                }

                String[] elements = ligne.trim().split("\\(");
                int nombreBoites = Integer.parseInt(elements[0].trim());

                // Extraire les coordonnées entre parenthèses
                String coordonneesStr = elements[1];
                String[] coordonnees = coordonneesStr.substring(0, coordonneesStr.length() - 1).split(",");
                double latitude = Double.parseDouble(coordonnees[0].trim());
                double longitude = Double.parseDouble(coordonnees[1].trim());

                BatimentEntreposage batiment = new BatimentEntreposage(nombreBoites, latitude, longitude);
                batiments.add(batiment);
            }
        }

        return batiments;
    }

    public static double calculerDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000;
        //source : staroverflow
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    public static BatimentEntreposage rechercherBatimentProche(double camionLatitude, double camionLongitude,
                                                               List<BatimentEntreposage> batiments) {
        return batiments.stream()
                .min(Comparator.comparingDouble(b -> calculerDistance(camionLatitude, camionLongitude,
                        b.getLatitude(), b.getLongitude())))
                .orElse(null);

    }

}
