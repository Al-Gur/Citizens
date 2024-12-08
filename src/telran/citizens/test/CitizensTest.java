package telran.citizens.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import telran.citizens.dao.Citizens;
import telran.citizens.dao.CitizensImpl;
import telran.citizens.model.Person;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CitizensTest {
    private Citizens citizens;
    private static final LocalDate now = LocalDate.now();
    private static final List<Person> list0 = Arrays.asList(
            new Person(1, "Peter", "Jackson", now.minusYears(23)),
            new Person(2, "John", "Smith", now.minusYears(20)),
            new Person(3, "Mary", "Jackson", now.minusYears(20)),
            new Person(4, "Rabindranate", "Anand", now.minusYears(25)));

    @BeforeEach
    void setUp() {
        citizens = new CitizensImpl(list0);
    }

    @Test
    void testAdd() {
        assertFalse(citizens.add(null));
        assertFalse(citizens.add(new Person(2, "John", "Smith", now.minusYears(20))));
        assertEquals(4, citizens.size());
        assertTrue(citizens.add(new Person(5, "John", "Smith", now.minusYears(20))));
        assertEquals(5, citizens.size());
    }

    @Test
    void testRemove() {
        assertFalse(citizens.remove(5));
        assertEquals(4, citizens.size());
        assertTrue(citizens.remove(2));
        assertEquals(3, citizens.size());
    }

    @Test
    void testFindById() {
        Person person = citizens.find(1);
        assertEquals(1, person.getId());
        assertEquals("Peter", person.getFirstName());
        assertEquals("Jackson", person.getLastName());
        assertEquals(23, person.getAge());
        assertNull(citizens.find(5));
    }

    @Test
    void testFindByAges() {
        Iterable<Person> res = citizens.find(20, 23);
        List<Person> list = new ArrayList<Person>(list0);
        list.sort((o1,o2)-> Integer.compare(o1.getAge(), o2.getAge()));
        assertIterableEquals(res, list.subList(0,3));
        //Iterable<Person> res0 = list0.subList(0,2);
        //assertIterableEquals(res0, res); // failed if the order differs
//        System.out.println("============= Age test =============");
//        res.forEach(person -> System.out.println(person));
    }

    @Test
    void testFindByLastName() {
        Iterable<Person> res = citizens.find("Jackson");
        List<Person> list = new ArrayList<Person>(list0);
        list.sort((o1,o2)-> o1.getLastName().compareTo(o2.getLastName()));
        assertIterableEquals(res, list.subList(1,3));
//        System.out.println("============= Name test =============");
//        for (Person person : res) {
//            System.out.println(person);
//        }
    }

    @Test
    void testGetAllPersonSortedById() {
        Iterable<Person> res = citizens.getAllPersonSortedById();
        List<Person> list = new ArrayList<Person>(list0);
        list.sort((o1,o2)-> o1.compareTo(o2));
        assertIterableEquals(res, list);
//        System.out.println("============= Sorted by id =============");
//        for (Person person : res) {
//            System.out.println(person);
//        }
    }

    @Test
    void testGetAllPersonSortedByLastName() {
        Iterable<Person> res = citizens.getAllPersonSortedByLastName();
        List<Person> list = new ArrayList<Person>(list0);
        list.sort((o1,o2)-> o1.getLastName().compareTo(o2.getLastName()));
        assertIterableEquals(res, list);
//        System.out.println("============= Sorted by LastName =============");
//        for (Person person : res) {
//            System.out.println(person);
//        }
    }

    @Test
    void testGetAllPersonSortedByAge() {
        Iterable<Person> res = citizens.getAllPersonSortedByAge();
        List<Person> list = new ArrayList<Person>(list0);
        list.sort((o1,o2)-> Integer.compare(o1.getAge(), o2.getAge()));
        assertIterableEquals(res, list);
//        System.out.println("============= Sorted by age =============");
//        for (Person person : res) {
//            System.out.println(person);
//        }
    }

    @Test
    void testSize() {
        assertEquals(4, citizens.size());
    }
}