import java.io.*;
import java.util.*;

public class GestionCargaison {
    private static List<BatimentEntreposage> batiments = new LinkedList<>();
    private static int capaciteMaxCamion;
    private static int position;
    private static int boitesRestantes=0;
    private static int NombreBoiteDansCamion;
    private static Comparator<BatimentEntreposage> comparator = Comparator
            .comparingDouble(BatimentEntreposage::getDistanceBatiments)
            .thenComparingDouble(BatimentEntreposage::getLongitude)
            .thenComparingDouble(BatimentEntreposage::getLatitude);
    public static void main(String[] args) {
        try {
            //pour lire les fichiers en args
            readFilesArgs("src/fichier1.txt");
            //trier selon le premier l'element ayant + de boites
            triSelonNbreBoite();
            //initialise la position du camion (longitude et latitude)
            camion camion = new camion(capaciteMaxCamion,batiments.get(0).getLongitude(),batiments.get(0).getLatitude());
            System.out.println("Truck position: " + camion.getLatitude() + "," + camion.getLongitude() );
            //ecris la position du camion
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/fichier2.txt"))) {
                writer.write("Truck position: " + camion.getLongitude() + "," + camion.getLatitude());
            }
            //set les distances des entrepots par rapport a la distance du camion
            for (int j = 1; j < batiments.size()-1; j++) {
                batiments.get(j).setDistanceBatiments( calculerDistance(camion.getLatitude(), camion.getLongitude(),
                        batiments.get(j).getLatitude(), batiments.get(j).getLongitude()));
                System.out.println(batiments.get(j).getDistanceBatiments());
                System.out.println("-----DISTANCE-----");

            }
            //tri les elements a partir de l'element 2 de la liste jusqu'a la fin.
            //pour trouver la liste croissante des batiments proches du camions
            triQuicksort(batiments,comparator,1, batiments.size()-1);
          /*  for (BatimentEntreposage batiment : batiments) {
                System.out.println("Nom: " + batiment.getNombreBoitesDisponibles());
                System.out.println("Longitude: " + batiment.getLongitude());
                System.out.println("Latitude: " + batiment.getLatitude());
                System.out.println("Distance: " + batiment.getDistanceBatiments());
                // Ajoutez d'autres attributs selon les besoins
                System.out.println("--------------------");
            }*/

            for (int i = 0; i < batiments.size();i++) {
                if (boitesRestantes == 0 && !batiments.isEmpty()) {
                    //condition pour verifier l'espace disponible dans camion >= nombre de boite à chargée
                    if(NombreBoiteDansCamion+batiments.get(i).getNombreBoitesDisponibles()
                            <= camion.getCapaciteMaxCamion()){
                        NombreBoiteDansCamion = camion.getNombreBoiteDansCamion() + batiments.get(i).getNombreBoitesDisponibles();
                        boitesRestantes=0;
                    }
                    else{
                        //nombre de boite restante
                        boitesRestantes= NombreBoiteDansCamion+batiments.get(i).getNombreBoitesDisponibles()-camion.getCapaciteMaxCamion();
                        //nombre de boite que l'on peut charger dans camion
                        NombreBoiteDansCamion= camion.getCapaciteMaxCamion();

                    }

                    //set le nombre de boite deja dans le camion
                    camion.setNombreBoiteDansCamion(NombreBoiteDansCamion);
                    //imprime et ecris la position du camion dans le fichier
                    System.out.println("Distance: " + batiments.get(i).getDistanceBatiments() + " Number of boxes: " +
                            boitesRestantes + " Position: " + batiments.get(i).getLatitude() + ","
                                + batiments.get(i).getLongitude());
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/fichier2.txt"))) {
                            writer.write("Distance: " + batiments.get(i).getDistanceBatiments() + "\tNumber of boxes: " + boitesRestantes
                                    + "\tPosition: " + batiments.get(i).getLatitude() + "," + batiments.get(i).getLongitude());
                            writer.newLine();
                    }

                }else{
                    break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    private static void triSelonNbreBoite() {
        //recherche la position du batiment ayant plus de boite

        int tempElement=batiments.get(0).getNombreBoitesDisponibles();
        position=0;
        //cherche lùele,ent le plus grqnd de la liste en le sauvegardant dans une variable temporaire
        for (int i = 0; i < batiments.size(); i++) {
                if (batiments.get(position).getNombreBoitesDisponibles() < batiments.get(i).getNombreBoitesDisponibles()) {
                    tempElement=batiments.get(i).getNombreBoitesDisponibles();
                    position=i;
                  //si les batiments ont le meme nombre de boite
                } else if (batiments.get(position).getNombreBoitesDisponibles() == batiments.get(i).getNombreBoitesDisponibles()) {
                    //recupere la position ayant la latitude la plus basse
                    if (batiments.get(position).getLatitude()  < batiments.get(i).getLatitude() ){
                        tempElement=batiments.get(position).getNombreBoitesDisponibles();
                    }
                    //si le batiment ont la meme latitude prendre le batiment avec la longitude la plus basse
                    else if (batiments.get(position).getLatitude() == batiments.get(i).getLatitude()){
                        if (batiments.get(position).getLongitude()< batiments.get(i).getLongitude()){
                            tempElement=batiments.get(position).getNombreBoitesDisponibles();
                        }
                        else{
                            tempElement=batiments.get(i).getNombreBoitesDisponibles();
                            position=i;
                        }
                    }
                } else {
                    tempElement=batiments.get(position).getNombreBoitesDisponibles();
                }
            }


        //echanger les elements de la liste pour mettre l'element avec le plus grand nombre de boite au debut de la liste
        echanger(batiments,position, 0);
        }


   public static void triQuicksort(List<BatimentEntreposage> batiments, Comparator<BatimentEntreposage> comparator, int debut, int fin) {
       if (debut < fin) {
           int pivotIndex = partitionner(batiments, comparator, debut, fin);
           triQuicksort(batiments, comparator, debut, pivotIndex - 1);
           triQuicksort(batiments, comparator, pivotIndex + 1, fin);
       }
   }

    public static int partitionner(List<BatimentEntreposage> batiments, Comparator<BatimentEntreposage> comparator, int debut, int fin) {
        BatimentEntreposage pivot = batiments.get(fin);
        int i = debut - 1;

        for (int j = debut; j < fin; j++) {
            if (comparator.compare(batiments.get(j), pivot) <= 0) {
                i++;
                echanger(batiments, i, j);
            }
        }

        echanger(batiments, i + 1, fin);
        return i + 1;
    }
    //echange les elements de la liste
    public static void echanger(List<BatimentEntreposage> batiments, int i, int j) {
        BatimentEntreposage temp = batiments.get(i);
        batiments.set(i, batiments.get(j));
        batiments.set(j, temp);
    }
    //calcule la distance grace a la formule d'Harvesine
    public static double calculerDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000;

        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        double distanceLat = lat2 - lat1;
        double distanceLon = lon2 - lon1;
        double a = Math.sin(distanceLat / 2) * Math.sin(distanceLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) * Math.sin(distanceLon / 2) * Math.sin(distanceLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));

        double distance = R * c;

        return distance;
    }


    //la methode qui lit le fichier en Args
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
