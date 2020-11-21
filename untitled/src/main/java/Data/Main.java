package Data;

import TestModel.Adress;
import TestModel.Person;

import java.util.Arrays;
import java.util.List;

public class Main {

	public static void main(String[] args) {

		//ejemplos de uso
		System.out.println("Casos de prueba");

		List<Person> people = Arrays.asList(new Person(21, "Pablo", 1232332L, new Adress("Cabildo", 1232, 4)),
				new Person(33, "María", 17762322L, new Adress("Amenabar", 1232, 4)),
				new Person(21, "José", 1878732L, new Adress("Libertador", 1232, 4)),
				new Person(21, "Pablo", 124334332L, new Adress("Santa Fé", 1232, 4))
		);

		ListData<Person> peopleList = ListData.of(people);

		//Hay personas repetidas? utilizando el equals de la clase
		boolean repeatedPersons = peopleList.unique();

		//Hay personas con el mismo nombre?
		boolean uniqueNames = peopleList.uniqueBy(person -> person.getName());


		//Hay personas con el mismo nombre y edad?
		boolean uniqueCombinationOfNameAge = peopleList.uniqueBy(person -> person.getName(), person -> person.getAge());

		//todo intersection/symetricDifference/deleteDuplicates




	}

}
