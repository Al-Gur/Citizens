package telran.citizens.dao;

import telran.citizens.model.Person;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CitizensImpl implements Citizens {
    private List<Person> idList;
    private List<Person> lastnameList;
    private List<Person> ageList;

    private static Comparator<Person> lastNameComparator;
    private static Comparator<Person> ageComparator;

    private void Init() {
        lastNameComparator = Comparator.comparing(Person::getLastName);
        ageComparator = (o1, o2) -> Integer.compare(o1.getAge(), o2.getAge());
        idList = new SortedPersonList(null);
        lastnameList = new SortedPersonList(lastNameComparator);
        ageList = new SortedPersonList(ageComparator);
    }

    public CitizensImpl() {
        Init();
    }

    public CitizensImpl(List<Person> citizens) {
        Init();
        ((SortedPersonList) idList).initList(citizens);
        ((SortedPersonList) lastnameList).initList(citizens);
        ((SortedPersonList) ageList).initList(citizens);
    }

    @Override
    public boolean add(Person person) {
        if (!((SortedPersonList) idList).addPerson(person)) {
            return false;
        }
        ((SortedPersonList) lastnameList).addPerson(person);
        ((SortedPersonList) ageList).addPerson(person);
        return true;
    }

    // Returns index of element with id or -1
    private int findId(int id) {
        return ((SortedPersonList) idList).binarySearch(new Person(id, "", "", null));
    }

    @Override
    public boolean remove(int id) {
        int index = findId(id);
        if (index < 0) {
            return false;
        }
        Person person = idList.remove(index);
        lastnameList.remove(((SortedPersonList) lastnameList).binarySearch(person));
        ageList.remove(((SortedPersonList) ageList).binarySearch(person));
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
        int index1 = -((SortedPersonList) ageList).binarySearch(person1) - 1;
        Person person2 = new Person(Integer.MIN_VALUE, "", "", LocalDate.now().minusYears(maxAge + 1));
        int index2 = -((SortedPersonList) ageList).binarySearch(person2) - 1;
        return ageList.subList(index1, index2);
    }

    @Override
    public Iterable<Person> find(String lastName) {
        Person person1 = new Person(Integer.MIN_VALUE, "", lastName, null);
        int index1 = -((SortedPersonList) lastnameList).binarySearch(person1) - 1;
        Person person2 = new Person(Integer.MAX_VALUE, "", lastName, null);
        int index2 = -((SortedPersonList) lastnameList).binarySearch(person2) - 1;
        return lastnameList.subList(index1, index2);
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
