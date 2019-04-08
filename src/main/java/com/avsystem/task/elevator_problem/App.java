package com.avsystem.task.elevator_problem;

import java.util.ArrayList;
import java.util.Scanner;

import com.avsystem.task.elevator_problem.parts.ElevatorSystem;
import com.avsystem.task.elevator_problem.parts.ElevatorSystemImp;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	Scanner scanner = new Scanner(System.in);
        System.out.println("Insert numbers of elevators: ");
        int numbersOfElevators = scanner.nextInt();
        System.out.println("Insert numbers of Floors: ");
        int numbersOfFloors = scanner.nextInt();
    	
        ElevatorSystem elevatorSystem = new ElevatorSystemImp(numbersOfElevators,numbersOfFloors);
        System.out.println();
        int numberOfCommand;
        int invokeFloor;
        int direction;
        int destFloor;
        int elevatorId;
        
        while(true) {
        	
        	System.out.println("Insert command number for elevator System: ");
            System.out.println("1 - pickup ");
            System.out.println("2 - update ");
            System.out.println("3 - status");
            System.out.println("4 - step");
            System.out.println();
            
        	numberOfCommand = scanner.nextInt();
	        switch(numberOfCommand) {
	        	
	        	case 1:{
	        		System.out.println("Selected command: pickup");
	        		System.out.println("Insert floor number (0 - "+numbersOfFloors+"):  ");
	        		invokeFloor = scanner.nextInt();
	        		System.out.println("Insert direction (-1 -down, 1 -up):  ");
	        		direction = scanner.nextInt();
	        		System.out.println();
	        		elevatorSystem.pickup(invokeFloor, direction);
	        		break;
	        	}
	        	
	        	case 2:{
	        		System.out.println("Selected command: update");
	        		System.out.println("Insert evelator Id:  ");
	        		elevatorId = scanner.nextInt();
	        		System.out.println("Insert number of destination floor (0 - "+numbersOfFloors+"):  ");
	        		destFloor = scanner.nextInt();
	        		System.out.println();
	        		elevatorSystem.update(elevatorId, destFloor);
	        		break;
	        	}
	        	
	        	case 3:{
	        		System.out.println("Selected command: status");
	        		displayStatus(elevatorSystem.status());
	        		System.out.println();
	        		break;
	        	}
	        	
	        	case 4:{
	        		System.out.println("Selected command: step");
	        		elevatorSystem.step();
	        		System.out.println();
	        		break;
	        	}
	        	
	        	default: {
	        		System.out.println("Insert correct command number: ");
	        		System.out.println();
	        		break;
	        	}
	        	
	        }
        }
        
    }
    
    public static void displayStatus(int [][] elevatorsStatus) {
    	for(int i=0; i< elevatorsStatus.length; i++) {
			System.out.println("Elevator Id: "+elevatorsStatus[i][0]+", current floor: "+elevatorsStatus[i][1]+", destination floor: "+elevatorsStatus[i][2]);
		}
    }
}
