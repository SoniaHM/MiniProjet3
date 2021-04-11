// SHA Zamshed and HASSUAN M'CAOURI Sonia 

package student;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


import elevator.Elevator;
import event.DIRECTION;
import model.Person;

public class RescueElevator implements Elevator {
	private DIRECTION direction = DIRECTION.UP;
	private String id;
	private int currentFloor = 1;
	private List<Person> people = new ArrayList<>();
	private final int capacity;
	private LocalTime time;
	private List<Integer> destinations2 = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
	private List<Integer> PeopleDestination = List.of();
	private List<Integer> destinations = List.of();
	private List<Integer> nextDestinations = List.of();
	private List<Integer> mostRequestedFloor = List.of();
	private NeedingHelpElevator another;
	private List<List<Person>> peopleByFloor = List.of();

		
	public RescueElevator(int capacity, String id, NeedingHelpElevator another) {
		this.capacity = capacity;
		this.id = id;
		this.another = another;
	}



	public RescueElevator(int capacity, String id) {
		this.capacity = capacity;
		this.id = id;
	}

	@Override
	public void startsAtFloor(LocalTime time, int initialFloor) {
		this.time = time;
	}

	@Override
	public void peopleWaiting(List<List<Person>> peopleByFloor) {
		this.peopleByFloor = peopleByFloor;
		this.another.peopleWaiting(peopleByFloor);
		}



	@Override
	public List<Integer> chooseNextFloors() {
		
		if (this.time == LocalTime.of(8, 1, 39)) {
			System.out.println();
		}
		
		
		List<Integer> desti = new ArrayList(this.nextDestinations);
		if (!this.nextDestinations.isEmpty()) {

			desti.remove(0);
			if (desti.isEmpty()) {
				
				int floorWithTheMostNumberOfPeople = findFloorWithTheMostNumberOfPeople();		
				int mostNumberOfPeople = peopleByFloor.get(floorWithTheMostNumberOfPeople-1).size();
				
				if (mostNumberOfPeople>this.capacity) {
					this.nextDestinations = another.getNextDestinations();
					return this.nextDestinations;
				}
				
				int secondFloorWithTheMostNumberOfPeople = findSecondFloorWithTheMostNumberOfPeople(findFloorWithTheMostNumberOfPeople());
				if (currentFloor == secondFloorWithTheMostNumberOfPeople) {
					int indexOfCurrentFloor = this.currentFloor - 1;
					List<Person> waitingListForCurrentFloor = this.peopleByFloor.get(indexOfCurrentFloor);

					List<Integer> PeopleDestination = ListToArrayList(findDestinationFloors(waitingListForCurrentFloor));
					if (!PeopleDestination.isEmpty()) {
						this.PeopleDestination = PeopleDestination;
					}
					this.mostRequestedFloor = mostRequestedFloors(ListToArrayList(PeopleDestination),
							ListToArrayList(destinations2));

					this.nextDestinations = this.mostRequestedFloor;
					
					if (this.nextDestinations.isEmpty())
					{
						this.nextDestinations = List.of(1);
					}
					return this.nextDestinations;

				} else {
					this.nextDestinations = List.of(secondFloorWithTheMostNumberOfPeople);
					return this.nextDestinations;
				}
				

			}
			this.nextDestinations = desti;
			return this.nextDestinations;
		}
		
		
		
		
		int floorWithTheMostNumberOfPeople = findFloorWithTheMostNumberOfPeople();		
		int mostNumberOfPeople = peopleByFloor.get(floorWithTheMostNumberOfPeople-1).size();

		if (mostNumberOfPeople>this.capacity) {
			this.nextDestinations = another.getNextDestinations();
			return this.nextDestinations;
		}
		
		
		return List.of(1);

		

//
//		List<Integer> Dest = another.getNextDestinations();
//		Collections.reverse(Dest);		
//
//		if (Dest.equals(another.getNextDestinations())) {
//			
//
//			
//			
//			this.nextDestinations = List.of(1);
//			return this.nextDestinations;
//		}
//		
//		
//	
//		
//		this.nextDestinations = Dest;
//		
//		return this.nextDestinations;
	}










	private List<Integer> findDestinationFloors(List<Person> waitingListForCurrentFloor) {
		return waitingListForCurrentFloor.stream().map(person -> person.getDestinationFloor())
				.collect(Collectors.toList());
	}

	private ArrayList<Integer> ListToArrayList(List<Integer> floorlist) {
		ArrayList<Integer> array = new ArrayList<Integer>();
		for (int i = 0; i < floorlist.size(); i++) {
			array.add(floorlist.get(i));
		}
		return array;
	}
	
	

	private List<Integer> mostRequestedFloors(ArrayList<Integer> PeopleDestination, ArrayList<Integer> destinations) {

		ArrayList<Integer> copyPeopleDestination = new ArrayList<Integer>(PeopleDestination);
		ArrayList<Integer> copyPeopleDestination2 = new ArrayList<Integer>(PeopleDestination);

		List<Integer> mostPopularFloors = new ArrayList<Integer>();
		while (!copyPeopleDestination2.isEmpty() && mostPopularFloors.size() < 11) {
			final int currentMax = mostPopularFloor(copyPeopleDestination, destinations);
			while (copyPeopleDestination2.stream().anyMatch(i -> i == currentMax)) {
				copyPeopleDestination2.remove(copyPeopleDestination2.indexOf(currentMax));
			}
			mostPopularFloors.add(currentMax);
			copyPeopleDestination = new ArrayList<Integer>(copyPeopleDestination2);
		}

		return mostPopularFloors;
	}

	private int mostPopularFloor(ArrayList<Integer> copyPeopleDestination, ArrayList<Integer> destinations) {
		int max = 0;
		int maxFloor = 0;

		for (int i = 0; i <= destinations.size(); i++) {
			int countPeopleWithSameDestination = countPeopleWithSameDestination(i, copyPeopleDestination);
			if (max < countPeopleWithSameDestination) {
				max = countPeopleWithSameDestination;

				maxFloor = i;
			}
		}
		return maxFloor;
	}

	private int countPeopleWithSameDestination(int floor, ArrayList<Integer> copyPeopleDestination) {
		int c = 0;
		for (int j = 0; j < copyPeopleDestination.size(); j++) {
			if (copyPeopleDestination.stream().anyMatch(i -> i == floor)) {
				c++;
				copyPeopleDestination.remove(copyPeopleDestination.indexOf(floor));
			}

		}
		return c;

	}

	private int findFloorWithTheMostNumberOfPeople() {
		int floorWithTheMostNumberOfPeople = 0;
		int MostNumberOfPeople = 0;
		for (int floorIndex = 0; floorIndex < this.peopleByFloor.size(); floorIndex++) {
			int numberOFPeopleWaiting = this.peopleByFloor.get(floorIndex).size();
			if (numberOFPeopleWaiting > MostNumberOfPeople) {
				MostNumberOfPeople = numberOFPeopleWaiting;
				floorWithTheMostNumberOfPeople = floorIndex + 1;
			}
		}
		if (floorWithTheMostNumberOfPeople==0)
		{
			floorWithTheMostNumberOfPeople=1;
		}
		
		return floorWithTheMostNumberOfPeople;
	}
	
	private int findSecondFloorWithTheMostNumberOfPeople(int MostCrowdedFloor) {
		int secondFloorWithTheMostNumberOfPeople = 0;
		int MostNumberOfPeople = 0;
		
		if (MostCrowdedFloor > 0)
		{
			int MostCrowdedFloorIndex = MostCrowdedFloor - 1;
			List<List<Person>> peopleByFloorCopy = new ArrayList(peopleByFloor);
			peopleByFloorCopy.set(MostCrowdedFloorIndex, List.of());
			for (int floorIndex = 0; floorIndex < peopleByFloorCopy.size(); floorIndex++) {
				int numberOFPeopleWaiting = peopleByFloorCopy.get(floorIndex).size();
				if (numberOFPeopleWaiting > MostNumberOfPeople) {
					MostNumberOfPeople = numberOFPeopleWaiting;
					secondFloorWithTheMostNumberOfPeople = floorIndex + 1;
				}
			}
		}
		if (secondFloorWithTheMostNumberOfPeople==0)
		{
			secondFloorWithTheMostNumberOfPeople=1;
		}
		
		return secondFloorWithTheMostNumberOfPeople;
	}
	

	private int countWaitingPeople() {
		return peopleByFloor.stream().mapToInt(List<Person>::size).sum();
	}

	@Override
	public void arriveAtFloor(int floor) {
		if (!this.destinations.isEmpty()) {
			this.destinations.remove(0);
		}
		this.currentFloor = floor;
	}

	@Override
	public void loadPeople(List<Person> people) {
		this.peopleByFloor = another.getPeopleByFloor();
		this.people.addAll(people);
		int indexFloor = this.currentFloor - 1;
		this.peopleByFloor.get(indexFloor).removeAll(people);
		this.another.getPeopleByFloor().get(indexFloor).removeAll(people);
	}

	@Override
	public void unload(List<Person> people) {
		this.people.removeAll(people);
	}

	@Override
	public void newPersonWaitingAtFloor(int floor, Person person) {
		this.peopleByFloor = another.getPeopleByFloor();
		int indexFloor = floor - 1;
	//	this.peopleByFloor.get(indexFloor).add(person);
	//	this.another.getPeopleByFloor().get(indexFloor).add(person);

	}

	@Override
	public void lastPersonArrived() {
	}

	@Override
	public void timeIs(LocalTime time) {
		this.time = time;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public void standByAtFloor(int currentFloor) {
	}
}