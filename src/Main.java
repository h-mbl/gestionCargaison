import java.io.*;
import java.util.*;

public class Main {
    private static List<BatimentEntreposage> batiments = new LinkedList<>();
    private static int capaciteMaxCamion;
    private static int nombreTotalBoites;
    private static int boitesChargees;
    private static double distance;

    public static void main(String[] args) {
        //gestion des arguments
        if (3 < 2) {
            System.out.println("Veuillez fournir les noms de fichiers d'entrée et de sortie.");
            return;
        }


        try {
            /*
             *lire le fichier passé en argument
             * créer des objets
             * ajouter les objets dans une liste des batiments
             */
            batiments = lireDonneesDepuisFichier("src/fichier1.txt");

           
            //utilise le comparator pour comparer les objets batiments
            //compare les longitudes des bâtiments. Les bâtiments seront triés par ordre croissant de leur longitude.
            //Si deux bâtiments ont la même longitude, ce deuxième critère de tri sera utilisé.
            // Il compare les latitudes des bâtiments. Les bâtiments seront ensuite triés par ordre croissant de leur latitude.
            Comparator<BatimentEntreposage> comparator = Comparator
                    .comparingDouble(BatimentEntreposage::getLongitude)
                    .thenComparingDouble(BatimentEntreposage::getLatitude);

            //sort :(

            for (int i = 1; i < batiments.size(); i++) {
                BatimentEntreposage current = batiments.get(i);
                int j = i - 1;

                while (j >= 0 && comparator.compare(batiments.get(j), current) > 0) {
                    batiments.set(j + 1, batiments.get(j));
                    j--;
                }

                batiments.set(j + 1, current);
            }

            // fin de tri
            //la liste tries des batiments
            Queue<BatimentEntreposage> fileBatiments = new LinkedList<>(batiments);
            for (BatimentEntreposage batiments : fileBatiments) {
                System.out.println("Nombre de boites : " + batiments.getNombreBoitesDisponibles());
                System.out.println("Latitude : " + batiments.getLatitude());
                System.out.println("Longitude : " + batiments.getLongitude());
                System.out.println("--------------------");
            }
            BatimentEntreposage batimentPlusGrandNombreBoites = null;
            int nombreBoitesTemp = 0;
            int position = -1;
            for (int i = 0; i < fileBatiments.size(); i++) {
                BatimentEntreposage batiments = ((LinkedList<BatimentEntreposage>) fileBatiments).get(i);
                //vérifie si le nombre de boîtes disponibles pour le bâtiment actuel  est supérieur à la valeur maximale (nombreBoitesTemp)
                // actuellement enregistrée.
                if (batiments.getNombreBoitesDisponibles() > nombreBoitesTemp) {
                    batimentPlusGrandNombreBoites = batiments;
                    nombreBoitesTemp = batiments.getNombreBoitesDisponibles();
                    position = i;
                }
            }

            if (!batiments.isEmpty()) {
                double camionLatitude = batimentPlusGrandNombreBoites.getLatitude();
                double camionLongitude = batimentPlusGrandNombreBoites.getLongitude();

                //X
                System.out.println("Longitude : " + camionLatitude);
                System.out.println("Latitude : " + camionLongitude);
                System.out.println("Nombre de boites : " + nombreBoitesTemp);
                System.out.println("Position dans la file : " + position);

                //ecris et imprime la position initial du camion dans le fichier
                System.out.println("Truck position: " + camionLatitude + "," + camionLongitude);
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/fichier2.txt"))) {
                    writer.write("Truck position: " + camionLatitude + "," + camionLongitude);
                    writer.newLine();
                    //iterateur
                    Iterator<BatimentEntreposage> it = batiments.iterator();

                    // Boucle de chargement des boîtes dans le camion
                    boitesChargees = 0;
                    while (boitesChargees < capaciteMaxCamion && nombreTotalBoites > 0 ) {
                        // Affichage des bâtiments proches et des boîtes restantes
                        BatimentEntreposage batimentProche = rechercherBatimentProche(camionLatitude,
                                camionLongitude, position, batiments);
                        //double distance = calculerDistance(camionLatitude, camionLongitude,
                        //          batimentProche.getLatitude(), batimentProche.getLongitude());
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
                        //supprimer l'entrepot dans la base de donnee
                        if (position >= 0 && position < batiments.size()) {
                            batiments.remove(position);
                        } else {
                            // La position est invalide, gérer l'erreur ou afficher un message approprié
                            break;
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                // System.out.println(nombreTotalBoites);

                System.out.println("Total boxes loaded: " + boitesChargees);
            } else {
                System.out.println("Aucun Batiment d'entreposage trouvé");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //lis le fichier .txt selon le modele 
    //nombredeboite capacitemaxdecamion
    //coordonnees entrepot
    private static List<BatimentEntreposage> lireDonneesDepuisFichier(String fichierEntree) throws IOException {
        List<BatimentEntreposage> batiments = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fichierEntree))) {
            String ligne;
            boolean isFirstLine = true;

            while ((ligne = reader.readLine()) != null) {
                //pour ignorer la première ligne
                //et recuperer les donnees de la premiere ligne
                if (isFirstLine) {
                    String[] informationsPremiereLigne = ligne.trim().split("\\s+");
                    nombreTotalBoites = Integer.parseInt(informationsPremiereLigne[0]);
                    capaciteMaxCamion = Integer.parseInt(informationsPremiereLigne[1]);
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

        return batiments;
    }

    //calcule la distance grace a la formule d'harveiseme
    //source : GPT
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

    //recherche le batiment proche du point initial
    public static BatimentEntreposage rechercherBatimentProche(double camionLatitude, double camionLongitude, int position,
                                                               List<BatimentEntreposage> batiments) {
        
        // La liste n'est pas vide                                                      
        if (!batiments.isEmpty()) {
            if (batiments.size() < 2) {
                // La liste ne contient pas suffisamment d'éléments pour comparer les distances
                distance = 0;
                return batiments.get(position);
            }

            BatimentEntreposage elementInitial = batiments.get(position);

            if (position == 0 || position == batiments.size() - 1) {
                // L'élément initial est le premier ou le dernier de la liste, pas de voisins à comparer
                BatimentEntreposage elementEnDessous1 = batiments.get(position + 1);
                double distanceEnDessous = calculerDistance(elementInitial.getLatitude(), elementInitial.getLongitude(),
                        elementEnDessous1.getLatitude(), elementEnDessous1.getLongitude());
                distance = distanceEnDessous;
                return elementEnDessous1;
            }

            //la liste etant deja trie en croissance selon la longitude
            //on cherche la distance de l'element initial avec l'element en dessous et au dessus de l'element initial afin 
            //afin de choisir ce qui est le plus proche
            BatimentEntreposage elementAuDessus = batiments.get(position - 1);
            BatimentEntreposage elementEnDessous = batiments.get(position + 1);

            double distanceAuDessus = calculerDistance(elementInitial.getLatitude(), elementInitial.getLongitude(),
                    elementAuDessus.getLatitude(), elementAuDessus.getLongitude());

            double distanceEnDessous = calculerDistance(elementInitial.getLatitude(), elementInitial.getLongitude(),
                    elementEnDessous.getLatitude(), elementEnDessous.getLongitude());
            
            //comparaison de ces deux distances        
            if (distanceAuDessus > distanceEnDessous) {
                distance = distanceAuDessus;
                return elementAuDessus;
            } else if (distanceEnDessous > distanceAuDessus) {
                distance = distanceEnDessous;
                return elementEnDessous;
            } else {
                // si Les distances sont identiques, retourne celui avec la latitude la plus petite
                if (elementAuDessus.getLatitude() < elementEnDessous.getLatitude()) {
                    distance = distanceAuDessus;
                    return elementAuDessus;
                } else {
                    distance = distanceEnDessous;
                    return elementEnDessous;
                }
            }
        } else {
             // retourne une constant vide si la liste est vide 
            return BatimentEntreposage.VALEUR_ABSENTE;
        }
    }
}
