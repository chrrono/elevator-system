package com.avsystem.task.elevator_problem.parts;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class ElevatorSystemImp implements ElevatorSystem {

	private List<Elevator> elevatorsList;
	private List<Deque<Task>> taskQueuesList;
	public static int ELEVATORS_NUMBER;
	public static int HIGHEST_FLOOR;
	public static int LOWEST_FLOOR;
	private Scheduler scheduler;
	
	
	public ElevatorSystemImp() {
		elevatorsList = new ArrayList<Elevator>();
		taskQueuesList = new ArrayList<Deque<Task>>();
		scheduler = new Scheduler();
		ELEVATORS_NUMBER = 16;
		HIGHEST_FLOOR = 20;
		LOWEST_FLOOR = 0;
		initializerTaskQueueAndElevators();
	}
	
	public ElevatorSystemImp(int elevatorsNumber, int highestFloor) {
		elevatorsList = new ArrayList<Elevator>();
		taskQueuesList = new ArrayList<Deque<Task>>();
		scheduler = new Scheduler();
		ELEVATORS_NUMBER = elevatorsNumber;
		HIGHEST_FLOOR = highestFloor;
		LOWEST_FLOOR = 0;
		initializerTaskQueueAndElevators();
	}
	
	private void initializerTaskQueueAndElevators() {
		for(int i = 0; i < ELEVATORS_NUMBER; i++) 
			taskQueuesList.add(new ArrayDeque<Task>());
		for(int i = 0; i < ELEVATORS_NUMBER; i++) 
			elevatorsList.add(new Elevator(i, 0, 0, ElevatorState.INACTIVE, 0));
		
	}

	public void pickup(int invokeFloor, int direction) {
		scheduler.selectElevatorWithTheShortestDistanceAndAssignInvokeTask(invokeFloor,direction);
	}
	
	public void update(int elevatorId, int destFloor) {
		scheduler.assignTaskToSpecificTaskQueueBecauseOfTransportTask(elevatorId, destFloor);
	}
	
	public void step() {
		scheduler.getNextTaskIfPossibleForAllElevators();
		for(int i = 0; i < ELEVATORS_NUMBER; i++) {
			elevatorsList.get(i).oneStep();
		}
//		scheduler.checkTaskIsDoneAndSwitchElevatorsToInactive();
	}
	
	public int[][] status() {
		int[][] elevatorsLocation = new int[ELEVATORS_NUMBER][];
		for(int i = 0; i < ELEVATORS_NUMBER; i++) {
			elevatorsLocation[i] = new int[3];
		}
		for(int i = 0; i < ELEVATORS_NUMBER; i++) {
			elevatorsLocation[i][0] = i;
			elevatorsLocation[i][1] = elevatorsList.get(i).getcurrentFloor();
			elevatorsLocation[i][2] = elevatorsList.get(i).getDestFloor();
		}
		return elevatorsLocation;
	}
	
	public List<Elevator> getElevatorsList() {
		return elevatorsList;
	}

	public int getTaskDestFloorFromQueueOfSepcificElevator(int elevatorId, int indexInTaskQueue){
		
		int i = 0;
		if(elevatorId > taskQueuesList.size() - 1) return -100;
		Deque<Task> taskQueueForSpecificElevator =  taskQueuesList.get(elevatorId);
		if(indexInTaskQueue > taskQueueForSpecificElevator.size() - 1) return -100;
		if(indexInTaskQueue == 0) return taskQueueForSpecificElevator.getFirst().getDestFloor();
		
		for(Task task: taskQueueForSpecificElevator) {
			if(indexInTaskQueue == i) return task.getDestFloor();
			i++;
		}
		return -100;
	}
	
	




	class Scheduler {
		
		public void getNextTaskIfPossibleForAllElevators() {
			Task nextTask;
			System.out.println();
			for(int i=0; i<elevatorsList.size(); i++) {
				Elevator elevator = elevatorsList.get(i);
				Deque<Task> taskQueueForElevator = taskQueuesList.get(i);
					
				if(elevator.getState().equals(ElevatorState.INACTIVE)) {
					if(!taskQueueForElevator.isEmpty()) {
						nextTask = taskQueueForElevator.getFirst();	
						elevator.setTaskToElevator(nextTask);
						nextTask.setState(TaskState.RUNNING);
						elevator.start();
					}
				}
				else if(elevator.getState().equals(ElevatorState.DONE)) {
					taskQueueForElevator.poll();
				}
				else if(elevator.getState().equals(ElevatorState.INPROGRESS)) {
					if(elevator.getDestFloor() != taskQueueForElevator.getFirst().getDestFloor()) {
						nextTask = taskQueueForElevator.getFirst();	
						elevator.setTaskToElevator(nextTask);
						nextTask.setState(TaskState.RUNNING);
					}	
					
				}
				System.out.println("ElevatorId: "+i+", QueueTask: " +taskQueueForElevator);
			}
			System.out.println();
		}

		
		public void selectElevatorWithTheShortestDistanceAndAssignInvokeTask(int invokeFloor, int direction) {
			
			int minDistance = Integer.MAX_VALUE;
			int elevatorIndexWithMinDistance = -1;
			int distance;
			int indexToInsertInQueue = 0;
			boolean isFindIndex = false;
			boolean aboveMinDistance = false;
			
			Deque<Task> taskQueueForSpecificElevator;
			Elevator specificElevator;
			
			Iterator<Task> queueIterator;
			Task firstNextTask;
			Task secondNextTask;

			
			int i = 0;
			int j = 0;
			while(i < ElevatorSystemImp.ELEVATORS_NUMBER) {
				taskQueueForSpecificElevator = taskQueuesList.get(i);
				specificElevator = elevatorsList.get(i);
				distance = 0;
				isFindIndex = false;
				aboveMinDistance = false;
				j = 0;
				
				if(taskQueueForSpecificElevator.isEmpty()) {
					distance = countDistanceBetweenTwoFloors(invokeFloor, specificElevator.getcurrentFloor());
					if(distance < minDistance) {
						minDistance = distance;
						elevatorIndexWithMinDistance = i;
						indexToInsertInQueue = 0;
					}
				}
				else {
					firstNextTask = new Task(specificElevator.getcurrentFloor(), TaskType.TRANSPORT, TaskState.RUNNING, 0);
					queueIterator = taskQueueForSpecificElevator.iterator();
					secondNextTask = queueIterator.next();
					if(isFindIndex = compareTwoTaskAndCheckIfInsertNewTaskBeetweenThemIsPosible(firstNextTask, secondNextTask, invokeFloor, direction)) {
						distance += countDistanceFromFirstTaskDestFloorToInvokeFloor(firstNextTask, invokeFloor, direction);
					}
					else {
						distance += countDistanceFromFirstNextTaskDestFloorToSecondNextTaskDestFloor(firstNextTask, secondNextTask);
						if(distance > minDistance)
							aboveMinDistance = true;
					}
					
					
					if(!isFindIndex) j++;
					while(queueIterator.hasNext() && !isFindIndex && !aboveMinDistance) {
						firstNextTask = secondNextTask;
						secondNextTask = queueIterator.next();
						if(isFindIndex = compareTwoTaskAndCheckIfInsertNewTaskBeetweenThemIsPosible(firstNextTask, secondNextTask, invokeFloor, direction)) {
							distance += countDistanceFromFirstTaskDestFloorToInvokeFloor(firstNextTask, invokeFloor, direction);
						}
						else {
							distance += countDistanceFromFirstNextTaskDestFloorToSecondNextTaskDestFloor(firstNextTask, secondNextTask);
							if(distance > minDistance) {
								aboveMinDistance = true;
								break;
							}
							j++;
						}
					}
					
					if(!isFindIndex && !aboveMinDistance) {
						firstNextTask = secondNextTask;
						secondNextTask = new Task(invokeFloor, TaskType.INVOKE, TaskState.WAIT, direction);
						distance += countDistanceFromFirstNextTaskDestFloorToSecondNextTaskDestFloor(firstNextTask, secondNextTask);
					}
				
					if(distance < minDistance && !aboveMinDistance) {
						minDistance = distance;
						elevatorIndexWithMinDistance = i;
						indexToInsertInQueue = j;
					}
				}
				i++;
			}

			assignTaskToSpecificTaskQueueBecauseOfInvokeTask(elevatorIndexWithMinDistance,invokeFloor,direction,indexToInsertInQueue);
		}
		
		
		private boolean compareTwoTaskAndCheckIfInsertNewTaskBeetweenThemIsPosible(Task firstNextTask,Task secondNextTask, int invokeFloorOfNewTask, int directionOfNewTask) {
			
			if(!isInvokeFloorIsBeetwenFirstTaskFloorAndSecondTaskFloor(firstNextTask.getDestFloor(),invokeFloorOfNewTask,secondNextTask.getDestFloor()))
				return false;
			
			int firstTaskDest = firstNextTask.getDestFloor();
			int firstTaskDirection = firstNextTask.getSelectDirection();
			TaskType firstTaskType = firstNextTask.getType();
			
			int secondTaskDest = secondNextTask.getDestFloor();
			int secondTaskDirection = secondNextTask.getSelectDirection();
			TaskType secondTaskType = secondNextTask.getType();
			
			int directionBeetweenTasks = secondTaskDest - firstTaskDest > 0 ? 1 : -1;
			
			if(firstTaskType.equals(TaskType.TRANSPORT) && secondTaskType.equals(TaskType.TRANSPORT) && directionBeetweenTasks == directionOfNewTask)
				return true;
			if(firstTaskType.equals(TaskType.TRANSPORT) && secondTaskType.equals(TaskType.INVOKE) 
					&& secondTaskDirection == directionOfNewTask && directionOfNewTask == directionBeetweenTasks && directionBeetweenTasks == secondTaskDirection)
				return true;
			if(firstTaskType.equals(TaskType.INVOKE) && secondTaskType.equals(TaskType.INVOKE) 
					&& firstTaskDirection == directionBeetweenTasks && secondTaskDirection == directionOfNewTask && directionBeetweenTasks == secondTaskDirection)
				return true;
					
			return false;
		}
		
		
		private boolean isInvokeFloorIsBeetwenFirstTaskFloorAndSecondTaskFloor(int firstFloor, int secondFloor, int thirdFloor) {
			if(firstFloor < thirdFloor) {
				if(firstFloor <= secondFloor && secondFloor <= thirdFloor)
					return true;
			}
			else if(firstFloor > thirdFloor){
				if(firstFloor >= secondFloor && secondFloor >= thirdFloor)
					return true;
			}
			return false;
				
		}
		
		
		private int countDistanceBetweenTwoFloors(int firstFloor, int secondFloor) {
			return Math.abs(secondFloor - firstFloor);
		}
		
		
		private int countDistanceFromFirstTaskDestFloorToInvokeFloor(Task firstNextTask, int invokeFloor, int direction) {
			
			int firstTaskDest = firstNextTask.getDestFloor();
			return Math.abs(firstTaskDest - invokeFloor);
		}
		
		
		private int countDistanceFromFirstNextTaskDestFloorToSecondNextTaskDestFloor(Task firstNextTask, Task secondNextTask) {
			
			int firstTaskDest = firstNextTask.getDestFloor();
			int firstTaskDirection = firstNextTask.getSelectDirection();
			TaskType firstTaskType = firstNextTask.getType();
			
			int secondTaskDest = secondNextTask.getDestFloor();
			int secondTaskDirection = secondNextTask.getSelectDirection();
			TaskType secondTaskType = secondNextTask.getType();
			
			int distanceBeetwen = 0;
			
			if(firstTaskType.equals(TaskType.TRANSPORT))
				distanceBeetwen = Math.abs(secondTaskDest - firstTaskDest) + 1; // one step for stop elevator on floor
			if(firstTaskType.equals(TaskType.INVOKE) && firstTaskDirection == -1) {
				if(firstTaskDest < secondTaskDest)
					distanceBeetwen = Math.abs(LOWEST_FLOOR - firstTaskDest) + 1 + Math.abs(LOWEST_FLOOR - secondTaskDest) + 1;
				else
					if(secondTaskType.equals(TaskType.INVOKE) && secondTaskDirection == 1)
						distanceBeetwen = Math.abs(LOWEST_FLOOR - firstTaskDest) + 1 + Math.abs(LOWEST_FLOOR - secondTaskDest) + 1;
					else
						distanceBeetwen = Math.abs(secondTaskDest - firstTaskDest) + 1;
			}
			else if(firstTaskType.equals(TaskType.INVOKE) && firstTaskDirection == 1) {
				if(firstTaskDest > secondTaskDest)
					distanceBeetwen = Math.abs(HIGHEST_FLOOR - firstTaskDest) + 1 + Math.abs(HIGHEST_FLOOR - secondTaskDest) + 1;
				else
					if(secondTaskType.equals(TaskType.INVOKE) && secondTaskDirection == -1)
						distanceBeetwen = Math.abs(HIGHEST_FLOOR - firstTaskDest) + 1 + Math.abs(HIGHEST_FLOOR - secondTaskDest) + 1;
					else
						distanceBeetwen = Math.abs(secondTaskDest - firstTaskDest) + 1;
			}
				
			return distanceBeetwen;
		}
		
		
		
		private void assignTaskToSpecificTaskQueueBecauseOfInvokeTask(int elevatorId, int invokeFloor, int direction, int indexToInsertInQueueTask) {
			
			Elevator selectedElevator = elevatorsList.get(elevatorId);
			
			TaskType type = TaskType.INVOKE;
			int destFloor = invokeFloor;
			TaskState state = TaskState.WAIT;
			int selectDirection = direction;
			
			Task newTask = new Task(destFloor, type, state, selectDirection);
			if(checkIfInvokeTaskAndIsTheSameLevelAsElevator(newTask,selectedElevator,elevatorId)) {
				selectedElevator.setDirection(newTask.getSelectDirection());
				System.out.println("Skip task: "+ newTask+" to elevator with Id: "+ elevatorId+", because elevator is in the same floor");
				return;
			}
			
			Task task;
			Stack<Task> temporaryStack = new Stack<Task>();
			Deque<Task> taskQueueForElevator = taskQueuesList.get(elevatorId);
			
			for(int i=0; i<indexToInsertInQueueTask; i++) {
				task = taskQueueForElevator.poll();
				temporaryStack.push(task);
			}
			
			taskQueueForElevator.addFirst(newTask);
			while(!temporaryStack.isEmpty()) {
				taskQueueForElevator.addFirst(temporaryStack.pop());
			}

			System.out.println("Assign task: "+ newTask+" to elevator with Id: "+ elevatorId);
		}
		
		
		private boolean checkIfInvokeTaskAndIsTheSameLevelAsElevator(Task nextTask,Elevator elevator, int elevatorId) {
			if(nextTask.getType().equals(TaskType.INVOKE) && nextTask.getDestFloor() == elevator.getcurrentFloor() 
					&& taskQueuesList.get(elevatorId).isEmpty())
				return true;
			return false;
		}
		
		
		
		public void assignTaskToSpecificTaskQueueBecauseOfTransportTask(int elevatorId, int destFloor) {
			
			if(checkIfElevatorIsInDoneState(elevatorId))
				return;
			
			TaskType type = TaskType.TRANSPORT;
			TaskState state = TaskState.WAIT;
			int selectDirection = 0;
			
			Task newTask = new Task(destFloor, type, state, selectDirection);
			Deque<Task> taskQueueForElevator = taskQueuesList.get(elevatorId);
			Elevator elevator = elevatorsList.get(elevatorId);
			insertTransportTaskToSpecificTaskQueueToSpecificPlace(taskQueueForElevator, elevator, newTask);
			System.out.println("Assign task: "+newTask);

		}
		
		
		private boolean checkIfElevatorIsInDoneState(int elevatorId) {
			if(elevatorsList.get(elevatorId).getState().equals(ElevatorState.DONE)) {
				System.out.println("If you want use update, you must invoke step() function !!!");
				System.out.println();
				return true;
			}
			return false;
		}
		
		
		private void insertTransportTaskToSpecificTaskQueueToSpecificPlace(Deque<Task> taskQueueForElevator,Elevator elevator, Task newTask) {
			
			int insertIndex = 0;
			boolean isfindIndex = false;
			int elevatorDirection = elevator.getDirection();
			Stack<Task> temporaryStack = new Stack<Task>();
			Iterator<Task> iterator = taskQueueForElevator.iterator();
			
			if(taskQueueForElevator.isEmpty()) {
				taskQueueForElevator.add(newTask);	
				return;
			}
			
			while(iterator.hasNext() && !isfindIndex) {
				Task firstTaskInQueue = iterator.next();
				if(elevatorDirection == 0) elevatorDirection = firstTaskInQueue.getDestFloor() - elevator.getcurrentFloor() > 0 ? 1 : -1;
				
				if(firstTaskInQueue.getType().equals(TaskType.TRANSPORT)) {
					int result = compareDestFloorWithDirection(newTask.getDestFloor(), firstTaskInQueue.getDestFloor(),elevatorDirection);
					if(result == -1) {
						insertIndex++;
					}
					else {
						isfindIndex = true;
					}
				}
				else if(firstTaskInQueue.getType().equals(TaskType.INVOKE) && firstTaskInQueue.getSelectDirection() == elevatorDirection) {
					int result = compareDestFloorWithDirection(newTask.getDestFloor(), firstTaskInQueue.getDestFloor(),elevatorDirection);
					if(result == -1) {
						insertIndex++;
					}
					else {
						isfindIndex = true;
					}
				}
			
			}
			
			Task task;
			
			for(int i=0; i<insertIndex; i++) {
				task = taskQueueForElevator.poll();
				temporaryStack.push(task);
			}
			
			taskQueueForElevator.addFirst(newTask);
			while(!temporaryStack.isEmpty()) {
				taskQueueForElevator.addFirst(temporaryStack.pop());
			}

		}
		
		
		private int compareDestFloorWithDirection(int destNew, int destInQueu, int direction) {
			if(direction == 1) {
				if(destNew < destInQueu) return 1;
				else return -1;	// 1 to idziemy dalej
			}
			else if(direction == -1) {
				if(destNew > destInQueu) return 1; //-1 to wstawiamy
				else return -1;
			}
			return 0;
		}
		
		
		private int selectElevatorWithShortestQueueOfTasks() {
			
			boolean selected = false;
			int i = 0;
			int minSize = Integer.MAX_VALUE;
			int sizeOfQueue;
			int elevatorIndexWithShortestQueue = -1;
			
			while(!selected && i < ElevatorSystemImp.ELEVATORS_NUMBER) {
				if(taskQueuesList.get(i).isEmpty()) {
					selected = true;
					elevatorIndexWithShortestQueue = i;
				}
				else {
					sizeOfQueue = taskQueuesList.get(i).size();
					if(sizeOfQueue < minSize) {
						minSize = sizeOfQueue;
						elevatorIndexWithShortestQueue = i;
					}
				}
				i++;
			}
			System.out.println("Select elevator with id: "+ elevatorIndexWithShortestQueue);
			return elevatorIndexWithShortestQueue;
		}
		
	}
	
}
