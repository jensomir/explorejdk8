package jdk8.tutorial.lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by jensomir on 07.11.14.
 */
public class Roster {

    List<Person> personal;

    public Roster(Person... personal) {
        this.personal = Arrays.asList(personal);
    }

    public List<Person> getPersonal() {
        return personal;
    }

    public void setPersonal(List<Person> personal) {
        this.personal = personal;
    }

    public List<Person> getPersonsOlderThan(int age) {
        List<Person> result = new ArrayList<>();
        for (Person p : personal) {
            if (p.getAge() > age) {
                result.add(p);
            }
        }
        return result;
    }

    // Lambda Expressions funktionieren hier, da die aufgerufene Methode das 'funktionale' Interface CheckPerson
    // übergeben bekommt
    public List<Person> getPersonalThatMatchesChecker(CheckPerson checker) {
        List<Person> result = new ArrayList<>();
        for (Person p : personal) {
            if (checker.isValid(p)) {
                result.add(p);
            }
        }
        return result;
    }

    // Statt des statischen CheckPerson Interface, wird das generische Predicate Interface
    // (aus java.util.function) genutzt
    public List<Person> getPersonalThatMatchesPredicate(Predicate<Person> predicate) {
        List<Person> result = new ArrayList<>();
        for (Person p : personal) {
            if (predicate.test(p)) {
                result.add(p);
            }
        }
        return result;
    }

    // Mit dem weiteren funktionalen Interface 'Consumer' wird Einfluss auf die Verarbeitung der Person genommen.
    public List<Person> getPersonalThatMatchesPredicateAndConsumer(Predicate<Person> predicate, Consumer<Person> block) {
        List<Person> result = new ArrayList<>();
        for (Person p : personal) {
            if (predicate.test(p)) {
                result.add(p);
                block.accept(p);
            }
        }
        return result;
    }

    // ... über das Function<T,R> Interface wird die Methode 'apply(T t)' ausgeführt, die R zurückgibt.
    public List<String> getPersonNamesThatMatchesPredicateConsumerAndFunction(Predicate<Person> predicate,
                                                                              Consumer<Person> block,
                                                                              Function<Person, String> myFunction) {
        List<String> result = new ArrayList<>();
        for (Person p : personal) {
            if (predicate.test(p)) {
                block.accept(p);
                result.add(myFunction.apply(p));
            }
        }
        return result;
    }

    // ... und das ganze jetzt noch generischer.
    public <T, R> List<R> getPersonNames(
            Iterable<T> source,
            Predicate<T> predicate,
            Consumer<T> block,
            Function<T, R> myFunction) {
        List<R> result = new ArrayList<>();
        for (T p : source) {
            if (predicate.test(p)) {
                block.accept(p);
                result.add(myFunction.apply(p));
            }
        }
        return result;
    }

    // ... und jetzt das ganze mit der neuen Stream/Filter Funktionalität
    public <T, R> List<R> getPersonAges(
            Collection<T> source,
            Predicate<T> predicate,
            Consumer<R> block,
            Function<T, R> myFunction) {
        List<R> result = new ArrayList<>();
        source
                .stream()
                .filter(predicate)
                .map(myFunction)
                .forEach(block);
        result.addAll(source
                .stream()
                .filter(predicate)
                .map(myFunction)
                .collect(Collectors.toList()));
        return result;
    }


}
