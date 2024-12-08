package telran.citizens.dao;

import telran.citizens.model.Person;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CitizensImpl implements Citizens {
    private List<Person> idList;
    private List<Person> lastnameList;
    private List<Person> ageList;

    private final static Comparator<Person> lastNameComparator =
            addComparator((o1, o2) -> o1.getLastName().compareTo(o2.getLastName()));
    ;
    private final static Comparator<Person> ageComparator =
            addComparator((o1, o2) -> Integer.compare(o1.getAge(), o2.getAge()));

    private static Comparator<Person> addComparator(Comparator<Person> comparator) {
        return (o1, o2) -> {
            int res1 = comparator != null ? comparator.compare(o1, o2) : 0;
            return res1 != 0 ? res1 : o1.compareTo(o2);
        };
    }

    public CitizensImpl() {
        idList = new ArrayList<>();
        lastnameList = new ArrayList<>();
        ageList = new ArrayList<>();
    }

    public CitizensImpl(List<Person> citizens) {
        idList = new ArrayList<>(citizens);
        Collections.sort(idList);
        lastnameList = new ArrayList<>(citizens);
        Collections.sort(lastnameList, lastNameComparator);
        ageList = new ArrayList<>(citizens);
        Collections.sort(ageList, ageComparator);
    }

    @Override
    public boolean add(Person person) {
        if (person == null) {
            return false;
        }
        int index = Collections.binarySearch(idList, person);
        if (index >= 0) {
            return false;
        }
        idList.add(-index - 1, person);
        lastnameList.add(-Collections.binarySearch(lastnameList, person, lastNameComparator) - 1, person);
        ageList.add(-Collections.binarySearch(ageList, person, ageComparator) - 1, person);
        return true;
    }

    // Returns index of element with id or -1
    private int findId(int id) {
        return Collections.binarySearch(idList, new Person(id, "", "", null));
    }

    private int absIndex(int index) {
        return index >= 0 ? index : -index - 1;
    }

    private int absIndexAfter(int index) {
        return index >= 0 ? index + 1 : -index - 1;
    }

    @Override
    public boolean remove(int id) {
        int index = findId(id);
        if (index < 0) {
            return false;
        }
        Person person = idList.remove(index);
        lastnameList.remove(Collections.binarySearch(lastnameList, person, lastNameComparator));
        ageList.remove(Collections.binarySearch(ageList, person, ageComparator));
        return true;
    }

    @Override
    public Person find(int id) {
        int index = findId(id);
        return index >= 0 ? idList.get(index) : null;
    }

    @Override
    public Iterable<Person> find(int minAge, int maxAge) {
        Person person1 = new Person(Integer.MIN_VALUE, "", "", LocalDate.now().minusYears(minAge));
        int index1 = Collections.binarySearch(ageList, person1, ageComparator);
        Person person2 = new Person(Integer.MAX_VALUE, "", "", LocalDate.now().minusYears(maxAge));
        int index2 = Collections.binarySearch(ageList, person2, ageComparator);
        return ageList.subList(absIndex(index1), absIndexAfter(index2));
    }

    @Override
    public Iterable<Person> find(String lastName) {
        Person person1 = new Person(Integer.MIN_VALUE, "", lastName, null);
        int index1 = Collections.binarySearch(lastnameList, person1, lastNameComparator);
        Person person2 = new Person(Integer.MAX_VALUE, "", lastName, null);
        int index2 = Collections.binarySearch(lastnameList, person2, lastNameComparator);
        return lastnameList.subList(absIndex(index1), absIndexAfter(index2));
    }

    @Override
    public Iterable<Person> getAllPersonSortedById() {
        return idList;
    }

    @Override
    public Iterable<Person> getAllPersonSortedByLastName() {
        return lastnameList;
    }

    @Override
    public Iterable<Person> getAllPersonSortedByAge() {
        return ageList;
    }

    @Override
    public int size() {
        return idList.size();
    }
}
