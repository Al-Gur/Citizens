package telran.citizens.dao;

import telran.citizens.model.Person;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class SortedPersonList extends ArrayList<Person> {
    private Comparator<Person> comparator;

    // List is first sorted by firstComparator, then by unique id
    public SortedPersonList(Comparator<Person> firstComparator) {
        this.comparator = (o1, o2) -> {
            int res1 = firstComparator != null ? firstComparator.compare(o1, o2) : 0;
            return res1 != 0 ? res1 : Integer.compare(o1.getId(), o2.getId());
        };
    }

    public void initList(List<Person> list) {
        this.addAll(list);
        this.sort(comparator);
    }

    public int binarySearch(Person person) {
        int left = 0;
        int right = size() - 1;
        if (right < 0 || comparator.compare(person, get(right)) > 0) {
            return -right - 2; // -size-1
        }
        while (left < right) {
            int middle = (left + right) / 2;
            if (comparator.compare(person, get(middle)) > 0) {
                left = middle + 1;
            } else {
                right = middle;
            }
        }
        return get(left).equals(person) ? left : -left - 1;
    }

    // if the list is sorted first not by id,
    // then absence of an element with the same id in the list should be checked before
    public boolean addPerson(Person person) {
        if (person == null) {
            return false;
        }
        int index = binarySearch(person);
        if (index < 0) {
            add(-index - 1, person);
            return true;
        } else {
            return false;
        }
    }
}
