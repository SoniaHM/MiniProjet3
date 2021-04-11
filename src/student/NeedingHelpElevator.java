// SHA Zamshed and HASSUAN M'CAOURI Sonia 

package student;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import elevator.Elevator;
import event.DIRECTION;
import model.Person;

public class NeedingHelpElevator implements Elevator {
	private DIRECTION direction = DIRECTION.UP;
	private String id;
	private int currentFloor = 1;
	private List<List<Person>> peopleByFloor = List.of();
	private List<Person> people = new ArrayList<>();
	private final int capacity;
	private LocalTime time;
	private List<Integer> destinations2 = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
	private List<Integer> PeopleDestination = List.of();
	private List<Integer> destinations = List.of();
	private List<Integer> nextDestinations = List.of();
	private List<Integer> mostRequestedFloor = List.of();
	private Elevator another;

	
	public List<List<Person>> getPeopleByFloor() {
		return peopleByFloor;
	}



	public NeedingHelpElevator(int capacity, String id) {
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
	}

	@Override
	public List<Integer> chooseNextFloors() {

		if (!this.mostRequestedFloor.isEmpty()) {
			this.mostRequestedFloor.remove(0);
			if (this.mostRequestedFloor.isEmpty()) {
				this.nextDestinations = List.of(1);
				return List.of(1);
			}
			else {
				this.nextDestinations = this.mostRequestedFloor;
				return this.mostRequestedFloor;
			}
		}



		int numberOfPeopleWaiting = countWaitingPeople();
		if (numberOfPeopleWaiting > 0) {

			int floorWithTheMostNumberOfPeople = findFloorWithTheMostNumberOfPeople();
			if (currentFloor == floorWithTheMostNumberOfPeople) {
				int indexOfCurrentFloor = this.currentFloor - 1;
				List<Person> waitingListForCurrentFloor = this.peopleByFloor.get(indexOfCurrentFloor);

				List<Integer> PeopleDestination = ListToArrayList(findDestinationFloors(waitingListForCurrentFloor));
				if (!PeopleDestination.isEmpty()) {
					this.PeopleDestination = PeopleDestination;
				}
				this.mostRequestedFloor = mostRequestedFloors(ListToArrayList(PeopleDestination),
						ListToArrayList(destinations2));

				this.nextDestinations = this.mostRequestedFloor;
				return this.mostRequestedFloor;

			} else {
				this.nextDestinations = List.of(floorWithTheMostNumberOfPeople);
				return List.of(floorWithTheMostNumberOfPeople);
			}
		}
		this.nextDestinations = List.of(1); 
		return List.of(1);

	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NeedingHelpElevator other = (NeedingHelpElevator) obj;
		if (nextDestinations == null) {
			if (other.nextDestinations != null)
				return false;
		} else if (!nextDestinations.equals(other.nextDestinations))
			return false;
		return true;
	}


	public List<Integer> getNextDestinations() {
		return nextDestinations;
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
			int numberOfPeopleWaiting = this.peopleByFloor.get(floorIndex).size();
			if (numberOfPeopleWaiting > MostNumberOfPeople) {
				MostNumberOfPeople = numberOfPeopleWaiting;
				floorWithTheMostNumberOfPeople = floorIndex + 1;
			}
		}
		
		if (floorWithTheMostNumberOfPeople==0)
		{
			floorWithTheMostNumberOfPeople=1;
		}
		
		return floorWithTheMostNumberOfPeople;
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
		this.people.addAll(people);
		int indexFloor = this.currentFloor - 1;
		this.peopleByFloor.get(indexFloor).removeAll(people);
	}

	@Override
	public void unload(List<Person> people) {
		this.people.removeAll(people);
		
	}

	@Override
	public void newPersonWaitingAtFloor(int floor, Person person) {
		int indexFloor = floor - 1;
		this.peopleByFloor.get(indexFloor).add(person);
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