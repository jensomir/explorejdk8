package jdk8.tutorial.lambda;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class PersonTest {

    @Test
    public void testGetAge() throws Exception {
        Person p1 = createPerson();
        int age = p1.getAge();
        assertTrue(age == 58);
    }

    @Test
    public void testPrintPerson() throws Exception {
        Person p = createPerson();
        String pPrinted = p.toString();
        assertTrue(true);
        p.printPerson();
    }

    private Person createPerson(){
        return new Person("Henry V.", LocalDate.of(1956, 5, 13), Person.Sex.MALE, "henry@britain.com");
    }


}