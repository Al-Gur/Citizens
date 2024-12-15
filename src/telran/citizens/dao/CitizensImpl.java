package telran.citizens.dao;

import telran.citizens.model.Person;

import java.time.LocalDate;
import java.util.*;

public class CitizensImpl implements Citizens {
    private TreeSet<Person> idList;
    private TreeSet<Person> lastnameList;
    private TreeSet<Person> ageList;

    private final static Comparator<Person> lastNameComparator =
            addComparator((o1, o2) -> o1.getLastName().compareTo(o2.getLastName()));

    private final static Comparator<Person> ageComparator =
            addComparator((o1, o2) -> Integer.compare(o1.getAge(), o2.getAge()));

    private static Comparator<Person> addComparator(Comparator<Person> comparator) {
        return (o1, o2) -> {
            int res1 = comparator != null ? comparator.compare(o1, o2) : 0;
            return res1 != 0 ? res1 : o1.compareTo(o2);
        };
    }

    public CitizensImpl() {
        idList = new TreeSet<>();
        lastnameList = new TreeSet<>(lastNameComparator);
        ageList = new TreeSet<>(ageComparator);
    }

    // O(n*n) -> O(n * log n)
    public CitizensImpl(List<Person> citizens) {
        this();
        citizens.forEach(person -> add(person));
    }

    // O(n) -> O(log n)
    @Override
    public boolean add(Person person) {
        if (person == null || Objects.equals(idList.floor(person), person)){
            return false;
        }
        idList.add(person);
        lastnameList.add(person);
        ageList.add(person);
        return true;
    }

    // O(n) -> O(log n)
    @Override
    public boolean remove(int id) {
        Person person = find(id);
        if (person == null) {
            return false;
        }
        idList.remove(person);
        lastnameList.remove(person);
        ageList.remove(person);
        return true;
    }

    // O(log n)
    @Override
    public Person find(int id) {
        Person p = new Person(id, "", "", null);
        Person res = idList.floor(p);
        return Objects.equals(res, p)? res : null;
    }

    // O(log n)
    @Override
    public Iterable<Person> find(int minAge, int maxAge) {
        Person person1 = new Person(Integer.MIN_VALUE, "", "", LocalDate.now().minusYears(minAge));
        Person person2 = new Person(Integer.MAX_VALUE, "", "", LocalDate.now().minusYears(maxAge));
        return ageList.subSet(person1, true, person2, true);
    }

    // O(log n)
    @Override
    public Iterable<Person> find(String lastName) {
        Person person1 = new Person(Integer.MIN_VALUE, "", lastName, null);
        Person person2 = new Person(Integer.MAX_VALUE, "", lastName, null);
        return lastnameList.subSet(person1, true, person2, true);
    }

    // O(1)
    @Override
    public Iterable<Person> getAllPersonSortedById() {
        return idList;
    }

    // O(1)
    @Override
    public Iterable<Person> getAllPersonSortedByLastName() {
        return lastnameList;
    }

    // O(1)
    @Override
    public Iterable<Person> getAllPersonSortedByAge() {
        return ageList;
    }

    // O(1)
    @Override
    public int size() {
        return idList.size();
    }
}
