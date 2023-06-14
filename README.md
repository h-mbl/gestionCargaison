# CargoManagement
CargoManagement is a program designed to manage the cargo truck process in a warehouse. It facilitates the transportation of boxes from various storage buildings to the truck. 
The program determines the optimal service points based on box availability and proximity to the truck, ensuring efficient cargo management.

## Description

A large warehouse consists of multiple storage buildings. The CargoManagement program takes the following inputs:
Total number of boxes to be transported
Maximum capacity of the truck
Positions of storage buildings involved in the current cargo
Number of boxes available at each storage building
The program starts by identifying the service point with the largest number of boxes.
 The coordinates of this building become the initial position of the truck. 
For each shipment, the program displays the positions of the storage buildings closest to the truck's current position and the number of boxes remaining at each service point.
 When multiple service points have the same distance, the program prioritizes the point with the lower latitude. If latitudes are also the same, the point with the smaller longitude is chosen. 
It is assumed that all available boxes can be transported from each building to the truck, and the number of loaded boxes must not exceed the truck's maximum capacity. 
The program calculates distances using the haversine formula.

## How to Run the Project

To run the CargoManagement project, follow these steps:
Make sure you have Java installed on your system.
Download the CargoManagement.java file to your local machine.
Prepare an input file named inputFile.txt, adhering to the format specified below:
The first line should contain the total number of boxes to load on the truck and the truck's capacity in terms of boxes, separated by a space.
Each subsequent line should represent the available boxes at a service point, along with the point coordinates.
Open a command prompt or terminal window.
Navigate to the directory where you saved the CargoManagement.java file.
Compile the Java source code by executing the following command:

```javac CargoManagement.java```
Run the program using the following command:

```java CargoManagement inputFile.txt outputfile.txt```

The program will process the input file, perform the necessary calculations, and save the results to the specified output file.
### Input File Format
The input file (inputFile.txt) should follow this format:

```<total_boxes> <truck_capacity>
<boxes_building1> <latitude_building1> <longitude_building1>
<boxes_building2> <latitude_building2> <longitude_building2>
```
```<total_boxes>: The total number of boxes to load on the truck.
<truck_capacity>: The maximum capacity of the truck in terms of boxes.
<boxes_buildingX>: The number of boxes available at storage building X.
<latitude_buildingX>: The latitude coordinate of storage building X.
<longitude_buildingX>: The longitude coordinate of storage building X.
...
```
Each line represents a different storage building.

### Output Format
The output file (outputfile.txt) will contain the following information:

```
Truck position: <latitude_truck> <longitude_truck>
Distance: <calculated_distance> Number of boxes: <remaining_boxes> Position: <latitude_building> <longitude_building>
...
```
```
<latitude_truck>: Latitude coordinate of the truck's position.
<longitude_truck>: Longitude coordinate of the truck's position.
<calculated_distance>: The distance between the truck and a particular storage building,
 calculated using the haversine formula.
<remaining_boxes>: The number of boxes remaining at the considered service point.
<latitude_building>: Latitude coordinate of a storage building.
<longitude_building>: Longitude coordinate of a storage building.
```
Each line represents the details of a storage building involved in the cargo, ordered by their proximity to the truck.

## Worst Case Theoretical Complexity
The total theoretical complexity of the code in the worst case is O(n^2).

