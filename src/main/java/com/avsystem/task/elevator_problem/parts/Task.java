package com.avsystem.task.elevator_problem.parts;

public class Task {

	private int destFloor;
	private TaskType type;
	private TaskState state;
	private int selectDirection;

	public Task(int destFloor, TaskType type, TaskState state, int selectDirection) {
		
		this.destFloor = destFloor;
		this.type = type;
		this.state = state;
		this.selectDirection = selectDirection;
	}

	public int getDestFloor() {
		return destFloor;
	}

	public TaskType getType() {
		return type;
	}

	public TaskState getState() {
		return state;
	}
	
	public int getSelectDirection() {
		return selectDirection;
	}

	public void setState(TaskState state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "Task [destFloor=" + destFloor + ", type=" + type + ", state=" + state + ", selectDirection="
				+ selectDirection + "]";
	}
	
	

	
}
