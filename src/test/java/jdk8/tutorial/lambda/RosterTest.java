package jdk8.tutorial.lambda;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class RosterTest {

    private Roster roster;

    private Person anne, georgeII, victoria, edwardVIII, elisabethII, princeCharles, priceWilliam;

    @Before
    public void setUp() throws Exception {
        anne = createPerson("Anne", 1665, Person.Sex.FEMALE);
        georgeII = createPerson("GeorgeII", 1683, Person.Sex.MALE);
        victoria = createPerson("Victoria", 1819, Person.Sex.FEMALE);
        edwardVIII = createPerson("EdwardVIII", 1894, Person.Sex.MALE);
        elisabethII = createPerson("ElisabethII", 1926, Person.Sex.FEMALE);
        princeCharles = createPerson("Charles", 1948, Person.Sex.MALE);
        priceWilliam = createPerson("William", 1982, Person.Sex.MALE);

        roster = new Roster(anne, georgeII, victoria, edwardVIII, elisabethII, princeCharles, priceWilliam);
    }

    @Test
    public void testSetUp() throws Exception {
        assertThat(roster.getPersonal(), is(not(empty())));
        printList(roster.getPersonal());
    }

    @Test
    public void testGetPersonsOlderThan() throws Exception {
        List<Person> foundPersons = roster.getPersonsOlderThan(66);
        assertThat(foundPersons, contains(anne, georgeII, victoria, edwardVIII, elisabethII));
        printList(foundPersons);
    }

    // ... Lösung mit anonymer Klasse
    @Test
    public void testGetPersonalThatMatchesCheckerAnonymus() throws Exception {
        List<Person> foundPersons = roster.getPersonalThatMatchesChecker(new CheckPerson() {
            @Override
            public boolean isValid(Person p) {
                return p.getAge() <= 200 && p.getAge() > 32;
            }
        });
        assertThat(foundPersons, contains(victoria, edwardVIII, elisabethII, princeCharles));
        printList(foundPersons);
    }

    // ... erste Lambda Expression. Geht, weil die aufgerufene Methode das 'funktionale' Interface CheckPerson übergeben
    // bekommt
    @Test
    public void testGetPersonalThatMatchesCheckerLambda() throws Exception {
        List<Person> foundPersons = roster.getPersonalThatMatchesChecker(
                (Person p) -> p.getGender() == Person.Sex.FEMALE && p.getAge() <= 200 && p.getAge() > 32);
        assertThat(foundPersons, contains(victoria, elisabethII));
        printList(foundPersons);
    }

    // ... zweite Lambda Expression. Statt des statischen CheckPerson Interface, wird das generische Predicate Interface
    // (aus java.util.function) genutzt
    @Test
    public void testGetPersonalThatMatchesPredicateLambda() throws Exception {
        List<Person> foundPersons = roster.getPersonalThatMatchesPredicate(
                (Person p) -> p.getGender() == Person.Sex.MALE && p.getAge() <= 200 && p.getAge() > 32);
        assertThat(foundPersons, contains(edwardVIII, princeCharles));
        printList(foundPersons);
    }

    // ... dritte Lambda Expression als Erweiterung der zweiten. Jetzt wird per Consumer Interface Einfluss auf die
    // Verarbeitung der Person genommen.
    @Test
    public void testGetPersonalThatMatchesPredicateAndConsumerLambda() throws Exception {
        List<Person> foundPersons = roster.getPersonalThatMatchesPredicateAndConsumer(
                (Person p) -> p.getAge() >= 200,
                (Person p) -> {
                    System.out.println("ConsumerPrint: -->");
                    p.printPerson();
                }
        );
        assertThat(foundPersons, contains(anne, georgeII));
    }

    // ... vierte Lambda Expression fügt zusätzliche Funktion hinzu und ermöglicht generische Rückgabewerte.
    @Test
    public void testGetPersonNamesThatMatchesPredicateConsumerAndFunction() throws Exception {
        List<String> foundPersonNames = roster.getPersonNamesThatMatchesPredicateConsumerAndFunction(
                p -> p.getAge() >= 200,
                p -> {
                    System.out.println("ConsumerPrint: -->");
                    p.printPerson();
                },
                p -> p.getName()
        );
        assertThat(foundPersonNames, contains("Anne", "GeorgeII"));
        for(String name : foundPersonNames){
            System.out.println("Found Name: " + name);
        }
    }

    // ... und das ganze noch generischer:
    @Test
    public void testGetPersonNames() throws Exception {
        List<String> foundPersonNames = roster.getPersonNames(
                roster.getPersonal(),
                p -> p.getAge() >= 200,
                p -> {
                    System.out.println("ConsumerPrint: -->");
                    p.printPerson();
                },
                p -> p.getName()
        );
        assertThat(foundPersonNames, contains("Anne", "GeorgeII"));
        for(String name : foundPersonNames){
            System.out.println("Found Name: " + name);
        }
    }

    // ... und mit der neuen Stream/Filter Funktionalität
    @Test
    public void testGetPersonAges() throws Exception {
        List<String> foundPersonAges = roster.getPersonAges(
                roster.getPersonal(),
                p -> p.getAge() >= 200,
                p -> System.out.println("ConsumerPrint: -->" + p.toString()),
                (Person p) -> p.getName() + String.valueOf(p.getAge())
        );
        assertThat(foundPersonAges, contains("Anne349", "GeorgeII331"));
        for(String age : foundPersonAges){
            System.out.println(age);
        }
    }

    // ... und jetzt mit der neuen Method References
    @Test
    public void testGetPersonAgesMethodReference() throws Exception {
        List<String> foundPersonAges = roster.getPersonAges(
                roster.getPersonal(),
                Person::over200YearsOld,
                System.out::println,
                (Person p) -> p.getName() + String.valueOf(p.getAge())
        );
        assertThat(foundPersonAges, contains("Anne349", "GeorgeII331"));
        foundPersonAges.forEach(System.out::println);
    }


    private Person createPerson(String firstName, int yearOfBirth, Person.Sex sex) {
        return new Person(firstName, LocalDate.of(yearOfBirth, 5, 13), sex, String.format("%s@britain.state", firstName));
    }

    private void printList(List<Person> personal) {
        System.out.println("print");
        for (Person p : personal) {
            p.printPerson();
        }
    }
}