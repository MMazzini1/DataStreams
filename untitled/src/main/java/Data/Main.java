package Data;

import TestModel.Adress;
import TestModel.Person;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.*;

public class Main {

	//TODO no se puede separar de la fuente de datos?
	// https://dzone.com/articles/how-to-replay-java-streams
	// https://dzone.com/articles/how-to-replay-java-streams

	//https://basicsstrong.com/creating-your-own-streams-using-custom-spliterator-and-how-streams-works-internally-in-java/
	public static void main(String[] args) {

//		//ejemplos de uso
//		System.out.println("Casos de prueba");
//		BigDecimal nn = null;
//		BigDecimal other = BigDecimal.ZERO;
//
		List<Integer> integers1 = Arrays.asList(1, 2, 3);
//
//		Stream<Integer> stream3 = StreamSupport.stream(integers1.spliterator(), false);

		//INTEGER->String->BigDecima
		Stream<Integer> stream2 = integers1.stream();

		Stream<String> stringStream2 = stream2.map(a -> "a");

		Stream<BigDecimal> stringStream3 = stringStream2.map(c -> BigDecimal.ZERO);
		List<BigDecimal> collect1 = stringStream3.collect(Collectors.toList());
		//
//		List<Person> people = Arrays.asList(new Person(21, "Pablo", 1232332L, new Adress("Cabildo", 1232, 4)),
//				new Person(33, "María", 17762322L, new Adress("Amenabar", 1232, 4)),
//				new Person(21, "José", 1878732L, new Adress("Libertador", 1232, 4)),
//				new Person(21, "Pablo", 124334332L, new Adress("Santa Fé", 1232, 4)),
//				new Person(59, "Pablo", 124334332L, new Adress("Santa Fé", 1232, 4)),
//				new Person(100, "Pablo", 124334332L, new Adress("Santa Fé", 1232, 4)),
//				new Person(121, "Pablo", 124334332L, new Adress("Santa Fé", 1232, 4)),
//				new Person(200, "Pablo", 124334332L, new Adress("Santa Fé", 1232, 4))
//
//		);
//
//
//		ListData<Person> peopleList = ListData.of(people );
//
//		//describir principios de la api
//		//inmutabilidad
//		//devolver el mismo tipo
//		//comodidad (reduce boilerplate)
		//permite reusar streams (ejecutar el mismo stream dos veces)
		//permite crear streams sin tener acceso a los datos
//
//
//		//Usos básicos
//		ListData<Person> olderThan21 = peopleList.filter(person -> person.getAge() > 21);
//		ListData<Person> olderThan99 = olderThan21.filter(je -> je.getAge() > 99);
//
//
//		List<Person> people1 = olderThan21.get();
//		List<Person> people2 = olderThan99.get();
//
//		ListData<String> olderThan21Names = olderThan21.map(person -> person.getName());
//		ListData<Long> olderThan99Ids = olderThan99.map(a -> a.getId());
//
//		List<String> strings = olderThan21Names.get();
//		List<Long> longs = olderThan99Ids.get();


		List<Person> lista = Arrays.asList(new Person(21, "Pablo", 1232332L, new Adress("Cabildo", 1232, 4)),
				new Person(33, "María", 17762322L, new Adress("Amenabar", 1232, 4)),
				new Person(21, "José", 1878732L, new Adress("Libertador", 1232, 4)),
				new Person(21, "Pablo", 124334332L, new Adress("Santa Fé", 1232, 4)),
				new Person(59, "Josef", 124334332L, new Adress("Santa Fé", 1232, 4)),
				new Person(100, "Jeronimo", 124334332L, new Adress("Santa Fé", 1232, 4)),
				new Person(121, "Pablo", 124334332L, new Adress("Santa Fé", 1232, 4)),
				new Person(200, "Pablo", 124334332L, new Adress("Santa Fé", 1232, 4))

		);

		List<Person> lista1 = Arrays.asList(new Person(21, "Pablo", 1232332L, new Adress("Cabildo", 1232, 4)),
				new Person(33, "María", 17762322L, new Adress("Amenabar", 1232, 4)),
				new Person(21, "José", 1878732L, new Adress("Santa Fé", 1232, 4))

		);

		List<Integer> collect = lista.stream().map(p -> p.getAge()).collect(Collectors.toList());

		List<Integer> map = StreamUtils.map(lista, (p) -> p.getAge());












		Function<List<Person>, Supplier<Stream<Person>>> funcionQueDevuelveUnSuppliearDeStreamParaLaListaQueLePases = (listaa) -> () -> listaa.stream().filter(person -> person.getAge() > 26);
		Stream<String> stringStream = funcionQueDevuelveUnSuppliearDeStreamParaLaListaQueLePases.apply(lista).get().map(p -> p.getName());
		Stream<Long> longStream = funcionQueDevuelveUnSuppliearDeStreamParaLaListaQueLePases.apply(lista1).get().map(p -> p.getId());

		/**
		 * Sourceless and with source.
		 *
		 *	Hacer solo con source por ahora.
		 * Los dos son lazy, los dos admiten re-usar los streams.
		 *
		 * Al final pasas el collector para ver como juntas todo.
		 *
		 * Definir operaciones terminales y operaciones que no lo son.
		 *
		 *
		 * Interface eager 
		 *
		 */


		ListData<Person, Person> root = ListData.of(Person.class);

		ListData<Person, Person> AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA = root.filter(p -> p.getAge() > 26);
		ListData<Integer, Person> edadViejos = AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA.map(persona -> persona.getAge());
		ListData<Long, Person> dniViejos = AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA.map(persona -> persona.getId());

		//WTF
		ListData<ListDataEager<Integer>, Person> map1 = AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA.map(persona -> ListDataEager.of(new
				ArrayList<Integer>()));
		List<ListDataEager<Integer>> evaluate8 = map1.evaluate(new ArrayList<Person>());

		//WTF
		ListData<ListData<Integer, Integer>, Person> map2 = AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA.map(persona -> ListData.of(Integer.class));

		//WTF
		List<ListData<Integer, Integer>> evaluate7 = map2.evaluate(new ArrayList<Person>());


		List<Integer> evaluate1 = edadViejos.evaluate(lista);
		List<Integer> evaluate3 = edadViejos.evaluate(lista1);

		List<Long> evaluate4 = dniViejos.evaluate(lista);
		List<Long> evaluate5 = dniViejos.evaluate(lista1);

		//API LAZY

		ListData<Person, Person> stream = ListData.of(Person.class);
//		ListData.of(Person.class).sort( a -> a.getAge()).var
		//		ListData<Person, Person> age = stream.filter(person -> person.getAge() > 100);
//		ListData<String, Person> nombresViejos = age.map(person -> person.getName());
//		ListData<Long, Person> idsViejos = age.map(person -> person.getId());

		/** Mi FORMA */
		ListData<Person, Person> santaFePeople = stream.filter(p -> p.getAdress().getStreet().equals("Santa Fé"));

		ReducedData<Integer, Person> test = santaFePeople.map(person -> person.getAge())
				.reduce(Integer::sum);

		ListData<Person, Person> sorted = santaFePeople.sort(p -> p.getName(), p -> p.getAge());

		sorted.evaluate(lista);

		Optional<Integer> evaluate6 = test.evaluate(lista);









		Optional<Integer> evaluate = test.evaluate(lista);
		Optional<Integer> evaluate2 = test.evaluate(lista1);


		/** Forma Normal */
		Function<List<Person>, Supplier<Stream<Person>>> reduced = (list) -> () -> list.stream()
				.filter(p -> p.getAdress().getStreet().equals("Santa Fé"));

		Supplier<Stream<Person>> apply = reduced.apply(lista1);




		//		Optional<Integer> evaluate = reducer.evaluate(Arrays.asList(new Person(21, "Pablo", 1232332L, new Adress("Cabildo", 1232, 4)),
//				new Person(33, "María", 17762322L, new Adress("Amenabar", 1232, 4)),
//				new Person(21, "José", 1878732L, new Adress("Libertador", 1232, 4)),
//				new Person(21, "Pablo", 124334332L, new Adress("Santa Fé", 1232, 4)),
//				new Person(59, "Pablo", 124334332L, new Adress("Santa Fé", 1232, 4)),
//				new Person(100, "Pablo", 124334332L, new Adress("Santa Fé", 1232, 4)),
//				new Person(121, "Pablo", 124334332L, new Adress("Santa Fé", 1232, 4)),
//				new Person(200, "Pablo", 124334332L, new Adress("Santa Fé", 1232, 4))
//
//		));

//		ListData<Person, Person> root = ListData.of(Person.class);


//		test.evaluate()


//		List<Long> evaluate1 = idsViejos.evaluate(Arrays.asList(new Person(21, "Pablo", 1232332L, new Adress("Cabildo", 1232, 4)),
//				new Person(33, "María", 17762322L, new Adress("Amenabar", 1232, 4))
//				));

		//		ListData<Integer> olderThan21Ages = olderThan21.map(person -> person.getAge());
//		Optional<Integer> sumOfAgesGreaterThan21 = olderThan21Ages.reduce(Integer::sum);
//		Integer result = sumOfAgesGreaterThan21.get();
//
//		//Comparación con streams
//		List<Person> olderThan21List = people.stream().filter(person -> person.getAge() > 21).collect(Collectors.toList());
//		olderThan21List.stream().map( person -> person.getAge())
//
//		//Hay personas repetidas? utilizando el equals de la clase
//		boolean repeatedPersons = peopleList.unique();
//
//		//Hay personas con el mismo nombre?
//		boolean uniqueNames = peopleList.uniqueBy(person -> person.getName());
//
//
//		//Hay personas con el mismo nombre y edad?
//		boolean uniqueCombinationOfNameAge = peopleList.uniqueBy(person -> person.getName(), person -> person.getAge());
//
//		//todo intersection/symetricDifference/deleteDuplicates

		List<Integer> integers = Arrays.asList(1, 2, 3);

		Stream<Integer> stream1 = integers.stream();
		Stream<String> stringStream1 = stream1.map(a -> a.toString());
		stringStream1.findFirst();


	}

}
