package com.avsystem.task.elevator_problem;

import static org.junit.Assert.assertEquals;

import java.util.Deque;
import java.util.List;

import org.junit.Test;

import com.avsystem.task.elevator_problem.parts.ElevatorState;
import com.avsystem.task.elevator_problem.parts.ElevatorSystem;
import com.avsystem.task.elevator_problem.parts.ElevatorSystemImp;
import com.avsystem.task.elevator_problem.parts.Task;

public class Test1 {

	@Test
	public void assignTaskOfInvokeAndMoveElevator() {
		ElevatorSystem elevatorSystem = new ElevatorSystemImp();
		elevatorSystem.pickup(4, -1);
		elevatorSystem.step();
		assertEquals(elevatorSystem.status()[0][1], 1);
		elevatorSystem.step();
		assertEquals(elevatorSystem.status()[0][1], 2);
		elevatorSystem.step();
		assertEquals(elevatorSystem.status()[0][1], 3);
		elevatorSystem.step();
		assertEquals(elevatorSystem.status()[0][1], 4);
		assertEquals(elevatorSystem.getElevatorsList().get(0).getState(), ElevatorState.DONE);
		elevatorSystem.step();
		assertEquals(elevatorSystem.status()[0][1], 4);
		assertEquals(elevatorSystem.getElevatorsList().get(0).getState(), ElevatorState.INACTIVE);
		
	}
	
	@Test
	public void assignTaskOfTransportAndMoveElevator() {
		ElevatorSystem elevatorSystem = new ElevatorSystemImp();
		elevatorSystem.pickup(0, 1);
		elevatorSystem.update(0, 4);
		elevatorSystem.step();
		assertEquals(elevatorSystem.status()[0][1], 1);
		elevatorSystem.step();
		assertEquals(elevatorSystem.status()[0][1], 2);
		elevatorSystem.step();
		assertEquals(elevatorSystem.status()[0][1], 3);
		elevatorSystem.step();
		assertEquals(elevatorSystem.status()[0][1], 4);	
	}
	
	@Test
	public void assignMultipleTaskOfTransportAndMoveElevator() {
		ElevatorSystem elevatorSystem = new ElevatorSystemImp();
		elevatorSystem.pickup(0, 1);
		elevatorSystem.update(0, 4);
		elevatorSystem.update(0, 3);
		elevatorSystem.update(0, 2);
		elevatorSystem.step();
		assertEquals(elevatorSystem.status()[0][1], 1);
		elevatorSystem.step();
		assertEquals(elevatorSystem.status()[0][1], 2);
		elevatorSystem.step();
		assertEquals(elevatorSystem.status()[0][1], 2);
		elevatorSystem.step();
		assertEquals(elevatorSystem.status()[0][1], 3);
		elevatorSystem.step();
		assertEquals(elevatorSystem.status()[0][1], 3);
		elevatorSystem.step();
		assertEquals(elevatorSystem.status()[0][1], 4);	
		elevatorSystem.step();
		assertEquals(elevatorSystem.status()[0][1], 4);
	}
	
	@Test
	public void assignMultipleTaskOfTransport2AndMoveElevator() {
		ElevatorSystem elevatorSystem = new ElevatorSystemImp();
		elevatorSystem.pickup(0, 1);
		elevatorSystem.update(0, 2);
		elevatorSystem.update(0, 3);
		elevatorSystem.update(0, 4);
		elevatorSystem.step();
		assertEquals(elevatorSystem.status()[0][1], 1);
		elevatorSystem.step();
		assertEquals(elevatorSystem.status()[0][1], 2);
		elevatorSystem.step();
		assertEquals(elevatorSystem.status()[0][1], 2);
		elevatorSystem.step();
		assertEquals(elevatorSystem.status()[0][1], 3);
		elevatorSystem.step();
		assertEquals(elevatorSystem.status()[0][1], 3);
		elevatorSystem.step();
		assertEquals(elevatorSystem.status()[0][1], 4);	
		elevatorSystem.step();
		assertEquals(elevatorSystem.status()[0][1], 4);
	}
	
	@Test
	public void whenElevatorInProgressChangeTask() {
		ElevatorSystem elevatorSystem = new ElevatorSystemImp();
		elevatorSystem.pickup(0, 1);
		elevatorSystem.update(0, 4);
		elevatorSystem.step();
		assertEquals(elevatorSystem.status()[0][1], 1);
		elevatorSystem.step();
		assertEquals(elevatorSystem.status()[0][1], 2);
		elevatorSystem.pickup(3, 1);
		elevatorSystem.step();
		elevatorSystem.step();
		elevatorSystem.update(0, 8);
		elevatorSystem.step();	
		elevatorSystem.step();
	}
	
	@Test
	public void testshortestDistance() {
		ElevatorSystem elevatorSystem = new ElevatorSystemImp();
		elevatorSystem.pickup(0, 1);
		elevatorSystem.update(0, 5);
		elevatorSystem.update(0, 6);
		elevatorSystem.step();
		elevatorSystem.step();
		elevatorSystem.step();
		elevatorSystem.step();
		assertEquals(elevatorSystem.status()[0][1], 4);
		elevatorSystem.pickup(7,1);
		elevatorSystem.pickup(7, -1);
		elevatorSystem.step();
		elevatorSystem.step();
		elevatorSystem.step();	
		elevatorSystem.step();
		elevatorSystem.step();
		assertEquals(elevatorSystem.status()[0][1], 7);
		assertEquals(elevatorSystem.status()[1][1], 5);
	}
	
	@Test
	public void testPickUpCumulationOnOneElevator() {
		ElevatorSystem elevatorSystem = new ElevatorSystemImp();
		elevatorSystem.pickup(4, 1);
		elevatorSystem.step();
		elevatorSystem.step();
		elevatorSystem.step();
		elevatorSystem.pickup(8, 1);
		elevatorSystem.pickup(6, 1);
		elevatorSystem.pickup(11, 1);
		elevatorSystem.step();
		assertEquals(elevatorSystem.status()[0][1], 4);
		assertEquals(elevatorSystem.status()[1][1], 1);
		elevatorSystem.step();
		assertEquals(elevatorSystem.status()[0][1], 4);
		assertEquals(elevatorSystem.status()[1][1], 2);
		assertEquals(elevatorSystem.status()[0][1], 4);
	}
	
	
	@Test
	public void testLast() {
		ElevatorSystem elevatorSystem = new ElevatorSystemImp();
		elevatorSystem.pickup(3, 1);
		elevatorSystem.step();
		elevatorSystem.step();
		elevatorSystem.pickup(10, 1);
		elevatorSystem.step();
		elevatorSystem.step();
		elevatorSystem.update(0, 6);
		elevatorSystem.pickup(4, -1);
		elevatorSystem.step();
		elevatorSystem.step();
		elevatorSystem.step();
		assertEquals(elevatorSystem.status()[0][1], 6);
		assertEquals(elevatorSystem.status()[1][1], 3);
		elevatorSystem.step();
		elevatorSystem.step();
		assertEquals(elevatorSystem.status()[0][1], 7);
		assertEquals(elevatorSystem.status()[1][1], 4);
		elevatorSystem.update(1, 2);
		elevatorSystem.step();
		assertEquals(elevatorSystem.status()[1][1], 3);
	}
	
	@Test
	public void testPickUpCumulation() {
		ElevatorSystem elevatorSystem = new ElevatorSystemImp(4,20);
		elevatorSystem.pickup(3, 1);
		elevatorSystem.pickup(10, -1);
		elevatorSystem.pickup(9, -1);
		elevatorSystem.pickup(12, 1);
		elevatorSystem.pickup(14, -1);
		elevatorSystem.pickup(11, -1);
		elevatorSystem.pickup(6, 1);
		elevatorSystem.step();
		int [][] status = elevatorSystem.status();
		for(int i=0; i<4; i++) {
			assertEquals(1,status[i][1]);
		}
		assertEquals(3,status[0][2]);
		assertEquals(10,status[1][2]);
		assertEquals(9,status[2][2]);
		assertEquals(6,status[3][2]);
		elevatorSystem.step();
		elevatorSystem.step();
		elevatorSystem.step();
		elevatorSystem.step();
		elevatorSystem.step();
		elevatorSystem.step();
		elevatorSystem.step();
		elevatorSystem.step();
		elevatorSystem.step();
		elevatorSystem.pickup(13, -1);
		elevatorSystem.step();
		elevatorSystem.step();
		elevatorSystem.step();
	}
	
	@Test
	public void testPickUpCumulation2() {
		ElevatorSystem elevatorSystem = new ElevatorSystemImp(3,20);
		elevatorSystem.pickup(4, 1);
		elevatorSystem.pickup(7, 1);
		elevatorSystem.pickup(5, 1);
		assertEquals(4, elevatorSystem.getTaskDestFloorFromQueueOfSepcificElevator(0, 0));
		assertEquals(5, elevatorSystem.getTaskDestFloorFromQueueOfSepcificElevator(1, 0));
		assertEquals(7, elevatorSystem.getTaskDestFloorFromQueueOfSepcificElevator(1, 1));
		elevatorSystem.step();
		elevatorSystem.step();
		elevatorSystem.step();
		elevatorSystem.pickup(12, -1);
		elevatorSystem.pickup(11, -1);
		assertEquals(12, elevatorSystem.getTaskDestFloorFromQueueOfSepcificElevator(2, 0));
		assertEquals(11, elevatorSystem.getTaskDestFloorFromQueueOfSepcificElevator(2, 1));
		elevatorSystem.step();
		elevatorSystem.step();
		elevatorSystem.step();
		elevatorSystem.step();
		elevatorSystem.step();
		elevatorSystem.step();
		elevatorSystem.step();
		elevatorSystem.step();
		elevatorSystem.step();
		elevatorSystem.step();
		elevatorSystem.pickup(13, -1);
		assertEquals(13, elevatorSystem.getTaskDestFloorFromQueueOfSepcificElevator(1, 0));
	}
	
	@Test
	public void testALotOfUpdate() {
		ElevatorSystem elevatorSystem = new ElevatorSystemImp(3,20);
		elevatorSystem.pickup(3, 1);
		elevatorSystem.pickup(6, -1);
		elevatorSystem.step();
		elevatorSystem.step();
		elevatorSystem.step();
		elevatorSystem.step();
		elevatorSystem.update(0, 10);
		elevatorSystem.update(0, 4);
		elevatorSystem.pickup(8, 1);
		assertEquals(4, elevatorSystem.getTaskDestFloorFromQueueOfSepcificElevator(0, 0));
		assertEquals(8, elevatorSystem.getTaskDestFloorFromQueueOfSepcificElevator(0, 1));
		assertEquals(10, elevatorSystem.getTaskDestFloorFromQueueOfSepcificElevator(0, 2));
		assertEquals(6, elevatorSystem.getTaskDestFloorFromQueueOfSepcificElevator(1, 0));
		elevatorSystem.step();
		
		
	}
	
	@Test
	public void testOfmistakenInvoke() {
		ElevatorSystem elevatorSystem = new ElevatorSystemImp(3,20);
		elevatorSystem.pickup(3, 1);
		elevatorSystem.step();
		elevatorSystem.step();
		elevatorSystem.step();
		elevatorSystem.pickup(3, 1);
		elevatorSystem.step();
		
	}
}
