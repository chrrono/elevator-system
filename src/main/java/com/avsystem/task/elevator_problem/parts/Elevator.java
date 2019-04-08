package com.avsystem.task.elevator_problem.parts;

import javax.swing.text.ChangedCharSetException;

public class Elevator {

	private int id;
	private int currentFloor;
	private int destFloor;
	private ElevatorState state;
	private int direction;	//1 - up, -1 -down, 0 - inactive;
	
	
	public Elevator(int id, int currentFloor, int destFloor, ElevatorState state, int direction) {
		super();
		this.id = id;
		this.currentFloor = currentFloor;
		this.destFloor = destFloor;
		this.state = state;
		this.direction = direction;
	}

	public int getcurrentFloor() {
		return currentFloor;
	}

	public void setcurrentFloor(int currentFloor) {
		this.currentFloor = currentFloor;
	}

	public int getDestFloor() {
		return destFloor;
	}

	public void setDestFloor(int destFloor) {
		this.destFloor = destFloor;
	}

	public ElevatorState getState() {
		return state;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	@Override
	public String toString() {
		return "Elevator [id=" + id + ", currentFloor=" + currentFloor + ", destFloor=" + destFloor + ", state=" + state
				+ ", direction=" + direction + "]";
	}
	
	
	public void start() {
		this.state = ElevatorState.INPROGRESS;
	}
	
	public void done() {
		this.state = ElevatorState.DONE;
	}
	
	public void setStateToInactive() {
		this.state = ElevatorState.INACTIVE;
	}
	
	public void moveOneStepUp() {
		this.currentFloor++;
	}
	
	public void moveOneStepDown() {
		this.currentFloor--;
	}
	
	public void setTaskToElevator(Task task) {
		this.destFloor = task.getDestFloor();
		this.direction = this.destFloor - this.currentFloor > 0 ? 1: -1;
//		start();
	}
	
	public void oneStep() {
		if(this.destFloor != this.currentFloor) {
			if((this.destFloor > this.currentFloor) && (this.direction == 1)) {
				moveOneStepUp();
				checkIsDone();
				System.out.println(this);
			}
			else if((this.destFloor < this.currentFloor) && (this.direction == -1)) {
				moveOneStepDown();
				checkIsDone();
				System.out.println(this);
			}
		}
		else if(this.state.equals(ElevatorState.DONE)){
			setStateToInactive();
			System.out.println(this);
		}
		else {
			System.out.println(this);
		}
	}
	
	private void checkIsDone() {
		if(this.destFloor == this.currentFloor) {
			this.direction = 0;
			this.state = ElevatorState.DONE;

		}
	}
}
