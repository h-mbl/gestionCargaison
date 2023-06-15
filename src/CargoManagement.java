import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class CargoManagement {
    //list of warehouses
    private static ArrayList<WareHouse> buildings = new ArrayList<>();
    private static int position;
    private static int maxTruckCapacity;
    private static int totalNumberBoxes;
    //number of boxes remaining at a warehouse
    private static int remainingBoxes =0;
    //number of boxes already in the truck
    private static int numberBoxesTruck;

    public static void main(String[] args) {
       if (args.length != 2) {
            System.out.println("provide input or output file names.");
            return;
        }
        //main examples
        String inputFile = "src/" +args[0];
        String outputFile = "src/"+args[1];

        /* String inputFile = "othersInputfilesExamles/"+args[0];
        String outputFile = "othersInputfilesExamles/"+args[1];*/
        try {
            //read the file passed as an argument and create adds the buildings to the building list
            readFile(inputFile);
            //sort buildings by number of boxes
            sortByNbrBoxes();
            //initializes the truck's position (longitude and latitude) with the first building in the list
            Truck truck = new Truck(maxTruckCapacity,buildings.get(0).getLongitude(),buildings.get(0).getLatitude());
            //write the truck's position in the file 
            FileWriter writer = new FileWriter(outputFile, false);
            writer.write("Truck position: " +"("+ truck.getLongitude() + "," + truck.getLatitude()+")\n");
            //set building distances against truck distances using the harveisne formula
            for (int j = 1; j < buildings.size(); j++) {
                buildings.get(j).setDistanceBuildingsTruck( calculerDistance(truck.getLatitude(), truck.getLongitude(),
                        buildings.get(j).getLatitude(), buildings.get(j).getLongitude()));
            }

            //sort elements from element 2 of the list to the end.
            //to find the growing list of buildings near the truck position
            quickSortBuilding(buildings,1, buildings.size()-1);
            //loading Boxes
            for (int i = 0; i < buildings.size();i++) {
                if (remainingBoxes == 0 && !buildings.isEmpty()) {
                    //condition to check available space in truck >= number of loaded boxes
                    if(numberBoxesTruck +buildings.get(i).getNumberBoxesAvailables()
                            <= truck.getMaxTruckCapacity() && numberBoxesTruck +
                            buildings.get(i).getNumberBoxesAvailables() <= totalNumberBoxes ){
                        numberBoxesTruck = truck.getNumberBoxesTruck() +
                                buildings.get(i).getNumberBoxesAvailables();
                        remainingBoxes =0;
                    }
                    else{
                        //number of boxes remaining
                        remainingBoxes = numberBoxesTruck +buildings.get(i).getNumberBoxesAvailables()
                                  - totalNumberBoxes;
                        //number of boxes that can be loaded into truck
                        numberBoxesTruck = truck.getMaxTruckCapacity();

                    }
                    //set the number of boxes already in the truck
                    truck.setNumberBoxesTruck(numberBoxesTruck);
                    //space management in print
                    String space1 =" ".repeat(10-String.valueOf(Math.round(buildings.get(i)
                            .getDistanceBuildingsTruck() * 10.0) / 10.0).length());
                    String space2 =" ".repeat(6-String.valueOf( remainingBoxes ).length());

                    //write truck position to outputfile
                    writer.write("Distance: " + Math.round(buildings.get(i).getDistanceBuildingsTruck()*10.0)/10.0
                            +space1+  "Number of boxes: " + remainingBoxes + space2 +  "Position: "
                            + "("+buildings.get(i).getLatitude()  + ","  + buildings.get(i).getLongitude()+")\n");
                }else{
                    break;
                }
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("unable to read files");
            throw new RuntimeException(e);
        }

    }


    private static void readFile(String inputFile) throws FileNotFoundException {
        File file = new File(inputFile);
        Scanner scanner = new Scanner(file);

        totalNumberBoxes = scanner.nextInt();
        maxTruckCapacity = scanner.nextInt();
        scanner.nextLine();

        parseFichier(scanner, buildings);
        scanner.close();
    }

    private static void sortByNbrBoxes() {
        //find the position of the building with the most boxes

        int tempElement=buildings.get(0).getNumberBoxesAvailables();
        position=0;
        //searches for the largest element in the list, saving it in a temporary variable
        for (int i = 0; i < buildings.size(); i++) {
                if (buildings.get(position).getNumberBoxesAvailables() <
                        buildings.get(i).getNumberBoxesAvailables()) {
                    tempElement=buildings.get(i).getNumberBoxesAvailables();
                    position=i;
                  //if the buildings have the same number of boxes
                } else if (buildings.get(position).getNumberBoxesAvailables() ==
                        buildings.get(i).getNumberBoxesAvailables()) {
                    //recovers the position with the lowest latitude
                    if (buildings.get(position).getLatitude()  < buildings.get(i).getLatitude() ){
                        tempElement=buildings.get(position).getNumberBoxesAvailables();
                    }
                    //if the buildings have the same latitude take the building with the lowest longitude
                    else if (buildings.get(position).getLatitude() == buildings.get(i).getLatitude()){
                        if (buildings.get(position).getLongitude()< buildings.get(i).getLongitude()){
                            tempElement=buildings.get(position).getNumberBoxesAvailables();
                        }
                        else{
                            tempElement=buildings.get(i).getNumberBoxesAvailables();
                            position=i;
                        }
                    }
                } else {
                    tempElement=buildings.get(position).getNumberBoxesAvailables();
                }
            }


        //exchange list elements to put the element with the highest number of boxes at the top of the list
        exchange(buildings,position, 0);
        }


   public static void quickSortBuilding(List<WareHouse> batiments, int debut, int fin) {
       if (debut < fin) {
           int pivotIndex = partition(batiments,debut, fin);
           quickSortBuilding(batiments, debut, pivotIndex - 1);
           quickSortBuilding(batiments,  pivotIndex + 1, fin);
       }
   }
    public static int partition(List<WareHouse> batiments, int debut, int fin) {
        int i = debut - 1;

        for (int j = debut; j < fin; j++) {
            if (batiments.get(j).getDistanceBuildingsTruck() < batiments.get(fin).getDistanceBuildingsTruck()) {
                i++;
                exchange(batiments, i, j);
            }
            else if(batiments.get(j).getDistanceBuildingsTruck() ==  batiments.get(fin).getDistanceBuildingsTruck()){
                if (batiments.get(j).getLatitude()< batiments.get(fin).getLatitude()) {
                    i++;
                    exchange(batiments, i, j);
                }
                else if (batiments.get(j).getLatitude() == batiments.get(fin).getLatitude()) {
                    if (batiments.get(j).getLongitude()< batiments.get(fin).getLongitude()) {
                        i++;
                        exchange(batiments, i, j);
                    }
            }}
        }

        exchange(batiments, i + 1, fin);
        return i + 1;
    }
    //exchange list items
    public static void exchange(List<WareHouse> batiments, int i, int j) {
        WareHouse temp = batiments.get(i);
        batiments.set(i, batiments.get(j));
        batiments.set(j, temp);
    }
    //calculates distance using Harvesine's formula
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

    public static void parseFichier(Scanner scanner, ArrayList<WareHouse> batiments){
        while (scanner.hasNextLine()) {
            String ligne = scanner.nextLine();
            ligne = ligne.replace(",", " ").replace("(", "")
                    .replace(")", "");
            String[] donnees = ligne.split("\\s+");
            int compteur = 0;
            int nombreBoites = 0;
            double latitude = 0;
            double longitude = 0;
            for (String donnee : donnees) {
                if (compteur == 0) {
                    nombreBoites = Integer.parseInt(donnee);
                    compteur++;
                } else if (compteur == 1) {
                    latitude = Double.parseDouble(donnee);
                    compteur++;
                } else {
                    longitude = Double.parseDouble(donnee);
                    batiments.add(new WareHouse(nombreBoites, latitude, longitude));
                    compteur = 0;
                }
            }
        }
    }
}
