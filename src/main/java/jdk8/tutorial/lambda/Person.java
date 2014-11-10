package jdk8.tutorial.lambda;

import java.time.LocalDate;
import java.time.Period;

/**
 * Created by jensomir on 06.11.14.
 */
public class Person {

    String name;
    LocalDate birthday;
    Sex gender;
    String emailAddress;

    public Person(String name, LocalDate birthday, Sex gender, String emailAddress) {
        this.name = name;
        this.birthday = birthday;
        this.gender = gender;
        this.emailAddress = emailAddress;
    }

    public enum Sex {
        MALE, FEMALE
    }

    public int getAge() {
        return Period.between(birthday, LocalDate.now()).getYears();
    }

    public Sex getGender(){
        return gender;
    }

    public void printPerson() {
        System.out.println(toString());
    }

    public String getName() {
        return name;
    }

    public boolean over200YearsOld(){
        return getAge() > 200;
    }

    @Override
    public String toString() {
        return name + " age=" + getAge() + ", gender=" + getGender() + '\'';
    }
}
