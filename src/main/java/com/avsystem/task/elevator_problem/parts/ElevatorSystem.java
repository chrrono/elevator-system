package com.avsystem.task.elevator_problem.parts;

import java.util.Deque;
import java.util.List;

public interface ElevatorSystem {

	void pickup(int invokeFloor, int direction);
    void update(int elevatorId, int destFloor);
    void step();
    int[][] status();		//return ([int-elevatorId, int-currentFloor, destFloor],[...],...)
    public List<Elevator> getElevatorsList();
    public int getTaskDestFloorFromQueueOfSepcificElevator(int elevatorId, int indexInTaskQueue);
}
