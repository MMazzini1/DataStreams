package interfaceTest.LStream;

import interfaceTest.MStream.LazyMStream;
import interfaceTest.MStream.MStream;
import interfaceTest.Pair;
import interfaceTest.TestModel.Adress;
import interfaceTest.TestModel.Perro;
import interfaceTest.TestModel.Person;
import interfaceTest.TestModel.PersonaMascotasBean;

import javax.management.PersistentMBean;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LazyLStream<T> implements LStream<T> {

	public static void main(String[] args) {

		//TODO support parallel evaluation
		//TODO activar desactivar logging
		//TODO devolver colleciones vacias y no romper con NPES

		//TODO groupBy una mas
		//TODO crear clase base abstracta

		//TODO resaltar el hecho de que permiten varias evaluacion sin romper !!!!! la bifurcacion de la funcion!!

		//TODO reconvertir a lazy??
		//TODO casos de test y tutoriales
		//Explicar que la evaluacion "realiza" las implementaciones con su version eager.

		//TODO hacer un esquema qu emueste que operaciones permiten pasar d un tipo al otro

		//TODO contract: todas las operacioness de lazy, salvo get, no disparan nada

		List<Person> lista = Arrays.asList(new Person(21, "Pablo", 1232332L, new Adress("Cabildo", 1232, 4)),
				new Person(33, "María", 17762322L, new Adress("Amenabar", 1232, 4)),
				new Person(21, "José", 1878732L, new Adress("Libertador", 1232, 4)),
				new Person(21, "Pablo", 124334332L, new Adress("Santa Fé", 1232, 4)),
				new Person(59, "Josef", 124334332L, new Adress("Santa Fé", 1232, 4)),
				new Person(100, "Pablo", 124334332L, new Adress("Córdoba", 1232, 4)),
				new Person(100, "PabloIII", 124334332L, new Adress("Córdoba", 1232, 4)),
				new Person(100, "Pablo3223", 124334332L, new Adress("Córdoba", 1232, 4)),
				new Person(121, "Pablo", 124334332L, new Adress("Córdoba", 1232, 4)),
				new Person(200, "Pablo", 124334332L, new Adress("Santa Fé", 1232, 4)),
				new Person(21, "Pablo", 124333232234332L, new Adress("Santa Fé", 1232, 4)),
				new Person(200, "Pablo", 124334332L, new Adress("ASD", 1232, 4)),
				new Person(121, "Pablo", 124334332L, new Adress("ASD", 1232, 4))

		);

		LStream<Person> persons = LStream.of(lista, true);
		LStream<Person> filtered = persons.filter(person -> person.getName().equals("Pablo"));
		MStream<Integer, LStream<Person>> integerLStreamMStream = filtered.groupBy(person -> person.getAge());
		MStream<Integer, LStream<Person>> filteredMap = integerLStreamMStream.filterKeys(key -> key != 121);

		MStream<Integer, MStream<String, LStream<Person>>> integerMStreamMStream1 = persons.groupBy(p -> p.getAge(), p -> p.getName());
		Map<Integer, MStream<String, LStream<Person>>> integerMStreamMap1 = integerMStreamMStream1.get();

		LStream<LStream<Person>> values = filteredMap.getValues();
		Collection<Collection<Person>> collections = values.map(list -> list.get()).get();

		//		MStream<Integer, LStream<Adress>> integerLStreamMStream1 = filteredMap.remapValues(personss -> personss.map(person -> person.getAdress()));
		//		Map<Integer, LStream<Adress>> integerLStreamMap = integerLStreamMStream1.get();

		MStream<Integer, MStream<String, LStream<Person>>> integerMStreamMStream = persons.groupBy(person -> person.getAge(), person -> person.getAdress().getStreet());

		persons.groupBy(person -> person.getAge(), person -> person.getAdress().getStreet()).get();
		Map<Integer, MStream<String, LStream<Person>>> integerMStreamMap = integerMStreamMStream.get();

		//		LStream<LStream<Person>> buckets = integerMStreamMStream.getBuckets(Person.class);
		LStream<LStream<Number>> bucketss = integerMStreamMStream.getBuckets(Number.class);
		Collection<LStream<Number>> lStreams = integerMStreamMStream.getBuckets(Number.class).get();
		//		Collection<LStream<Person>> lStreams = buckets.get();

		//TODO testeo duplicates
		List<Person> listaa = Arrays.asList(new Person(21, "Pablo", 1232332L, new Adress("Cabildo", 1232, 4)),
				new Person(21, "Pablo", 1232332L, new Adress("Cabildo", 1232, 4)),
				new Person(21, "Josffffé", 1878732L, new Adress("Libertador", 1232, 4)),
				new Person(21, "Pablo", 124334332L, new Adress("Santa Fé", 1232, 4)),
				new Person(21, "Pablo", 124334332L, new Adress("Santa Fé", 1232, 4)),
				new Person(21, "Pablo", 124334332L, new Adress("Santa Fé", 1232, 4)),
				new Person(100, "Pablo3223", 124334332L, new Adress("Córdoba", 1232, 4)),
				new Person(100, "Pablo3223", 124334332L, new Adress("Córdoba", 1232, 4)),
				new Person(100, "Pablo3223", 124334332L, new Adress("Córdoba", 1232, 4)),
				new Person(100, "Pablo3223", 124334332L, new Adress("Córdoba", 1232, 4)),
				new Person(59, "Josef", 124334332L, new Adress("Santa Fé", 1232, 4))

		);

		LStream<Person> of = LStream.of(listaa);

		Collection<Person> people = of.getDuplicateValueInstances().get();
		Collection<Person> people1 = of.getUniqueValues().get();

		MStream<Person, Long> countMap = of.getCountMap();
		Collection<Person> people2 = LStream.of(listaa).getCountMap().filterValues(count -> count > 2).getKeys().get();

		LStream<Person> of1 = LStream.of(listaa);
		MStream<Person, Long> countMap1 = of1.getCountMap();
		MStream<Person, Long> personLongMStream = countMap1.filterValues(count -> count > 2);
		LStream<Person> keys = personLongMStream.getKeys();
		Collection<Person> people3 = keys.get();

		MStream<Person, Long> countMap2 = LStream.of(listaa).getCountMap();
		Map<Person, Long> personLongMap = countMap2.get();

		LStream<Person> distinctPersons = of.distinct();
		//para hacer esto, necesarias crearte un wrapper vos. o agruparpor las dos cosas y quedarte con las keys
		//TODO no es totalmente satisfactorio porque se queda con gente random. Quizas seria mejor que devuelva un tuple o algo asi. IGUAL vos podes mapearlo como hice en el ejemplo de mas abajo
		MStream<Integer, MStream<String, LStream<Person>>> integerMStreamMStream2 = of.groupBy(p -> p.getAge(), p -> p.getName());
		MStream<Integer, LStream<String>> integerLStreamMStream1 = integerMStreamMStream2.remapValues(v -> v.getKeys());
		//TODO aca necesitarias el producto cartesiano de Keys Values commo pares. NO, paja
		//TODO asi se haria

		LStream<Object> of2 = LStream.of(Arrays.asList());
		Collection<Object> objects = of2.first().get();

		LStream<Object> of3 = LStream.of(Arrays.asList(new Person()));
		Collection<Object> of4 = of3.first().get();

		LStream<LStream<Person>> buckets = of.groupBy(p -> p.getAge(), p -> p.getName()).getBuckets(Person.class);
		LStream<LStream<Pair<Integer, String>>> map = buckets.map(list -> list.first().map(person -> Pair.of(person.getAge(), person.getName())));
		Collection<Collection<Pair<Integer, String>>> collections1 = map.map(list -> list.get()).get();
		//TODO otra forma. No, se vuelve pajosos. Por eso getBuckets ayuda.
		//of.groupBy(p -> p.getAge(), p -> p.getName()).getValues().ge

		LazyLStream<Person> asd = (LazyLStream<Person>) LStream.of(listaa);
		//		Collection<LStream<Person>> lStreams1 = asd.groupBy(p -> p.getName(), p -> p.getAge(), p -> p.getName()).get();
		//
		//		Collection<LStream<Person>> lStreams2 = asd.groupBy(p -> p.getName(), p -> p.getAge(), p -> p.getAdress()).get();

		//		Collection<String> strings = names.get();
		//
		//		MStream<Integer, MStream<Integer, LStream<String>>> integerMStreamMStream = names.groupBy(p -> p.length(),
		//				p -> p.hashCode());
		//
		//
		//
		//
		//
		//		LStream<LStream<String>> buckets = integerMStreamMStream.getBuckets(String.class);
		//		Collection<LStream<String>> lStreams = buckets.get();
		//
		//
		//
		//		//Todo pasar un objeto como si fuera un collector
		//		LStream<Integer> keys = integerMStreamMStream.keys();
		//		List<Integer> asList = keys.getAsList();
		//
		//
		//		List<Integer> integers1 = Arrays.asList(1, 2, 3);
		//		integers1.stream().collect(Collectors.toSet());


		//Objetivos de la librería
		// 1) reducir boilerplate al trabajar con streams
		// 2) proveer varias abstracciones utiles out of the box (operaciones comunes al manipular datos)
		// 3) proveer una pseudo-monad
		// 3) poder reusar un mismo stream (ej, bifurcarlo)
		// 4) sinergia entre ambas abstracciones, con operaciones que llevan de un tipo a otro

		//Abstracciones
		//Un LStream representa una coleccion lineal (lista, set, etc)
		//Un MStream representa un mapa

		//Garantias
		// 1) todas las operaciones son lazy por default, salvo get. Incluyendo operaciones que normalmente son terminales (ej, reduce)
		// 2) al ejecutar get, se realiza el cálculo. Si hay anidadas instancias de LStreamo MStream, estas habrán sido evaluadas. Ya no son lazy
		// 3) se trata de hacerlo performante, evitando hacer collects en cada instancia.
		// 4) EN PARTE LA performance depende de la coleccion que haya sido pasads
		//TODO proveer mecanismo para empezar de un eager a stremear

		//Como funciona
		//1) Provee un wrapper de streams. Las distintas operaciones concatenan nuevas operaciones al stream.
		//2) En realidad, es un wrapper sobre supplier de streams.


		//tutorial

		//Inicializaciòn
		LStream<Person> personas = LStream.of(listaa);
		//operaciones básicas
		LStream<Optional<Integer>> edadesAdultos = personas.filter(persona -> persona.getAge() > 18)
				.map(persona -> persona.getAge())
				.reduce(Integer::sum);

		//Ejemplo de bifuracion
		LStream<Integer> mayores18 = personas.filter(persona -> persona.getAge() > 18)
				.map(persona -> persona.getAge());

		LStream<Optional<Integer>> reduce = mayores18.reduce(Integer::sum); //Y en el ejemplo siguiente seguis con esto

		//Sorting, hay muchas mas operaciones. Las vemos despues
		LStream<Person> sorted = personas.sort((persona1, persona2) -> persona1.getName().compareTo(persona2.getName())); //todo en que caso sirve esta api??
		LStream<Person> sort = personas.sort(persona -> persona.getName());





		//Hasta acá no se disparo nada

		//TODO esto no va
		Collection<Optional<Integer>> optionals = edadesAdultos.get();



		//GroupBy
		MStream<Integer, LStream<Person>> personasByAge = personas.groupBy(persona -> persona.getAge());

//
		MStream<Adress, LStream<Person>> personasPorDireccion = LStream.of(listaa).groupBy(person -> person.getAdress());

		//
		Map<Adress, Collection<Optional<Person>>> adressCollectionMap = personasPorDireccion.remapValues(personas5 -> personas5.reduce((p1, p2) -> new Person(p1, p2)).get()).get();

		MStream<Adress, LStream<Perro>> mascotasPorDireccion = null;

		MStream<Adress, PersonaMascotasBean> adressPersonaMascotasBeanMStream = personasPorDireccion.innerJoin(mascotasPorDireccion, (listaPersonas, listaMascotas) -> new PersonaMascotasBean(listaPersonas, listaMascotas));
		LStream<PersonaMascotasBean> values1 = adressPersonaMascotasBeanMStream.getValues();








		//		MStream<Adress, Person> direcciones = null;
//		MStream<Adress, Perro> mascotas = null;
//		MStream<Adress, PersonaMascotasBean> personasYMascotas;

//		MStream<Adress, PersonaMascotasBean> adressPersonaMascotasBeanMStream = direcciones.innerJoin(mascotas, (persona, mascota) -> new PersonaMascotasBean(persona.getName(),
//				mascota.getName(), persona.getAdress()));
//		LStream<PersonaMascotasBean> values1 = adressPersonaMascotasBeanMStream.getValues();

		MStream<Integer, LStream<LStream<Person>>> integerLStreamMStream2 = personasByAge.mergeAsLists(personasByAge);

		//Hasta 3 group By
		MStream<Integer, MStream<String, MStream<Adress, LStream<Person>>>> integerMStreamMStream3 = personas.groupBy(persona -> persona.getAge(), persona -> persona.getName(), persona -> persona.getAdress());
		//getBuckets
		LStream<LStream<Person>> grouped = integerMStreamMStream3.getBuckets(Person.class);
		//también hay un metodo que directamente acepta odos los que le pases

		//TODO cambiarle nombre o no anda?
		LStream<LStream<Person>> lStreamLStream = personas.groupBy(persona -> persona.getAge(), persona -> persona.getName(), persona -> persona.getAdress(),
				persona -> persona.getId()); //todo extraer

		//Familias
		MStream<Adress, LStream<Person>> adressLStreamMStream = personas.groupBy(persona -> persona.getAdress());
		//TODO puede ir ??adressLStreamMStream.remapKeys() ??? que pasa si no cumple con la garania
		MStream<Adress, LStream<String>> adressLStreamMStream1 = adressLStreamMStream.remapValues(personass -> personass.first().map(persona -> persona.getName())); //get apellido
		MStream<Adress, String> adressStringMStream = adressLStreamMStream.remapValues(personass -> personass.first().map(persona -> persona.getName()).get().iterator().next()); //TODO

		MStream<Adress, String> cabildo = adressStringMStream.filterKeys(adress -> adress.equals("Cabildo")); //no queres a los que viven en cabildo
		MStream<Adress, String> perez = adressStringMStream.filterValues(sureName -> sureName.equals("Perez"));

		//TODO operacones que vuelven
		LStream<Adress> direcciones = cabildo.getKeys();




		//TODO merge
		LStream<Person> duplicateValueInstances = personas.getDuplicateValueInstances();
		LStream<Person> personLStream = personas.keepDuplicateValues();
		LStream<Boolean> booleanLStream = personLStream.hasUniqueValues();



		//volviendo a mas operaciones



		//TODO operaciones de combinar








	}

	private Supplier<Stream<T>> upstream;

	public LazyLStream(Supplier<Stream<T>> sourceStream) {
		this.upstream = sourceStream;
	}

	@Override public LStream<T> first() {
		return new LazyLStream<>(logFunction(() -> {
					Optional<T> first = upstream.get().findFirst();
					return first.isPresent() ? Stream.of(first.get()) : Stream.empty();
				}
				, "First"));
	}

//	@Override public CollectorHelper<T, List<T>> list() {
//		return new list<>();
//	}
//
//	@Override public CollectorHelper<T, Set<T>> set() {
//		return new set<>();
//	}

	public static <A> LazyLStream<A> of(Collection<A> coll) {
		return new LazyLStream<A>(() -> (Stream<A>) coll.stream());
	}

	@Override public <K> MStream<K, LStream<T>> groupBy(Function<T, K> classifier) {
		MStream<K, LStream<T>> mStream = new LazyMStream<>(logFunction(() -> {
			Map<K, LStream<T>> collect = upstream.get().collect(Collectors.groupingBy(classifier, new LStreamCollector<>()));
			Stream<Map.Entry<K, LStream<T>>> entryStream = collect.entrySet().stream();
			return entryStream;
		}, "Grouping by"));
		return mStream;
	}

	@Override public <K1, K2> MStream<K1, MStream<K2, LStream<T>>> groupBy(Function<T, K1> classifier1, Function<T, K2> classifier2) {
		MStream<K1, LStream<T>> k1LStreamMStream = groupBy(classifier1);
		MStream<K1, MStream<K2, LStream<T>>> k1MStreamMStream = k1LStreamMStream.remapValues(list -> list.groupBy(classifier2));
		return k1MStreamMStream;
	}

	@Override public <K1, K2,K3> MStream<K1, MStream<K2, MStream<K3, LStream<T>>>> groupBy(Function<T, K1> classifier1, Function<T, K2> classifier2, Function<T, K3> classifier3) {
		MStream<K1, LStream<T>> k1LStreamMStream = groupBy(classifier1);
		MStream<K1, MStream<K2, LStream<T>>> k1MStreamMStream = k1LStreamMStream.remapValues(list -> list.groupBy(classifier2));
		MStream<K1, MStream<K2, MStream<K3, LStream<T>>>> k1MStreamMStream1 = k1MStreamMStream.remapValues(innerMap -> innerMap.remapValues(list -> list.groupBy(classifier3)));
		return k1MStreamMStream1;
	}

	@Override public LStream<LStream<T>> groupBy(Function<T, ?>... getters) {
		LazyMStream<HashWrapper<T>, LStream<T>> groupedMap = (LazyMStream<HashWrapper<T>, LStream<T>>) groupBy(elem -> HashWrapper.of(elem, getters));
		LStream<LStream<T>> buckets = groupedMap.getBuckets();
		return buckets;

	}

	private static class HashWrapper<E> {
		private E object;
		private Function<E, ?>[] getters;

		public static <K> HashWrapper<K> of(K k, Function<K, ?>... getter) {
			HashWrapper<K> kHashWrapper = new HashWrapper<>();
			kHashWrapper.object = k;
			kHashWrapper.getters = getter;
			return kHashWrapper;

		}

		@Override public boolean equals(Object o) {
			for (Function<E, ?> getter : getters) {
				if (!getter.apply(object).equals(getter.apply(((HashWrapper<E>) o).object))) {
					return false;
				}
			}
			return true;
		}

		@Override public int hashCode() {
			int hashCode = 0;
			for (Function<E, ?> getter : getters) {
				int partialHashCode = getter.apply(object).hashCode();
				hashCode = hashCode + partialHashCode;
			}
			return hashCode;
		}

	}

	@Override public LStream<T> getUniqueValuesBy(Function<T, ?>... getters) {
		return new LazyLStream<>(
				logFunction(() -> upstream.get()
								.map(item -> HashWrapper.of(item, getters))
								.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
								.entrySet()
								.stream()
								.filter(p -> p.getValue() == 1)
								.map(Map.Entry::getKey)
								.map(wrapper -> wrapper.object)
						, "Get uniques by"));
	}

	//Devuelve los valores unicos en la lista (una aparición) según equals. Devuelve uns subconjunto de la lista
	@Override public LStream<T> getUniqueValues() {
		return new LazyLStream<>(
				logFunction(() -> upstream.get().
						collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
						.entrySet()
						.stream()
						.filter(p -> p.getValue() == 1)
						.map(Map.Entry::getKey), "Filtering unique values"));
	}

	//Devuelve una lista en la cual se remueven las apariciones repetidas (con 2 o mas apariciones) de elementos, según equals.
	@Override public LStream<T> distinct() {
		return new LazyLStream<>(
				logFunction(() -> upstream.get().
						distinct(), "Getting duplicates"));
	}

	//Devuelve todos los valores duplicados (con 2 o mas apariciones) en la lista, según equals. Devuelve un subconjunto de la lista
	@Override public LStream<T> keepDuplicateValues() {
		return new LazyLStream<>(
				logFunction(() -> upstream.get().
								collect(Collectors.groupingBy(Function.identity()))
								.entrySet()
								.stream()
								.filter(entry -> entry.getValue().size() > 1)
								.flatMap(entry -> entry.getValue().stream())
						, "Getting duplicates"));
	}

	//Devuelve UNA APARICION de cada valor duplicados (con 2 o mas apariciones) en la lista, según equals. Devuelve un subconjunto de la lista
	@Override public LStream<T> getDuplicateValueInstances() {
		return new LazyLStream<>(
				logFunction(() -> upstream.get().
						collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
						.entrySet()
						.stream()
						.filter(p -> p.getValue() > 1)
						.map(Map.Entry::getKey), "Getting duplicates"));
	}

	//Remueve todos los elementos que aparezcan más de una vez en la lista
	@Override public LStream<T> removeDuplicates() {
		return new LazyLStream<>(
				logFunction(() -> upstream.get().
						collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
						.entrySet()
						.stream()
						.filter(p -> p.getValue() == 1)
						.map(Map.Entry::getKey), "Removing duplicates"));
	}

	//FIXME
	//TODO crear contenedor que no acepte streams en el constructor, sino el valor
	@Override public LStream<Boolean> hasUniqueValues() {
		return new LazyLStream<>(logFunction(() ->
				Stream.of(getUniqueValues().get().size() > 0), "Has unique values"));
	}


	@Override public LStream<Boolean> hasUniqueValuesBy(Function<T, ?>... getters) {
		return new LazyLStream<>(logFunction(() ->
				Stream.of(getUniqueValuesBy().get().size() > 0), "Has unique values"));
	}

	@Override public LStream<Boolean> has(T item) {
		return new LazyLStream<>(logFunction(() ->
				Stream.of(get().contains(item)), "Has"));
	}

	//TODO testear esto
	@Override public MStream<T, Long> getCountMapBy(Function<T, ?>... getters) {
		return new LazyMStream<>(
				logFunction(() -> upstream.get().
						collect(Collectors.groupingBy(elem -> HashWrapper.of(elem, getters), Collectors.counting()))
						.entrySet().stream().map(entry -> new Map.Entry<T, Long>() {
							@Override public T getKey() {
								return entry.getKey().object;
							}

							@Override public Long getValue() {
								return entry.getValue();
							}

							@Override public Long setValue(Long value) {
								entry.setValue(value);
								return value;
							}
						}), "Getting count map"));
	}

	@Override public MStream<T, Long> getCountMap() {
		return new LazyMStream<>(
				logFunction(() -> upstream.get().
						collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
						.entrySet().stream(), "Getting count map"));
	}

	@Override public LStream<T> sort(Function<? super T, ? extends Comparable>... comparators) {
		Queue<Comparator<T>> comparatorQueue = new LinkedList<>();
		for (Function<? super T, ? extends Comparable> function : comparators) {
			Comparator<T> comparator = Comparator.comparing(function);
			comparatorQueue.add(comparator);
		}
		Comparator<T> chainedComparator = buildComparator(comparatorQueue, comparatorQueue.poll());
		return new LazyLStream<>(
				logFunction(() -> upstream.get().sorted(chainedComparator), "Sorting"));

	}

	private Comparator<T> buildComparator(Queue<Comparator<T>> comparators, Comparator<T> acum) {
		if (comparators.isEmpty()) {
			return acum;
		} else {
			Comparator comparator = comparators.poll();
			Comparator chained = acum.thenComparing(comparator);
			return buildComparator(comparators, chained);
		}
	}

	@Override public LStream<T> sort(Comparator<T>... comparators) {
		Queue<Comparator<T>> comparatorQueue = new LinkedList<>();
		Collections.addAll(comparatorQueue, comparators);
		Comparator<T> chainedComparator = buildComparator(comparatorQueue, comparatorQueue.poll());
		return new LazyLStream<>(
				logFunction(() -> upstream.get().sorted(chainedComparator), "Sorting"));
	}

	@Override public LStream<T> filter(Predicate<T> predicate) {
		return new LazyLStream<>(
				logFunction(() -> upstream.get().filter(predicate::test), "Filtering"));
	}

	@Override public <B> LStream<B> map(Function<T, B> mapper) {
		return new LazyLStream<>(logFunction(() -> upstream.get().map(mapper::apply), "Mapping"));
	}

	@Override public LStream<Optional<T>> reduce(BinaryOperator<T> reducer) {
		return new LazyLStream<>(logFunction(() ->
				Stream.of(upstream.get().reduce(reducer::apply)), "Reducing"));
	}

	//TODO reusar para combinar el otro
	@Override public LStream<T> addAll(LStream<T> other) {
		if (other instanceof LazyLStream) {
			return combineWithLazy((LazyLStream<T>) other);
		}else {
			return combineWithEager((EagerLStream<T>) other);
		}
	}

	private LStream<T> combineWithEager(EagerLStream<T> other) {
		return new LazyLStream<>(logFunction(() ->
				Stream.concat(upstream.get(),  other.get().stream()), "Add all, adding an eager LStream"));
	}

	private LStream<T> combineWithLazy(LazyLStream<T> other) {
		return new LazyLStream<>(logFunction(() ->
				Stream.concat(upstream.get(),  other.upstream.get()), "Add all, adding a lazy LStream"));
	}

	@Override public LStream<T> symmetricDiference(LStream<T> other) {
		//obtener los dos como set y hacer la operacion
		return null;
	}

	//intersection


//
//
//	protected static abstract class CollectorHelper<T,R>{
//
//		public abstract Collector<T, ?, R> getCollector();
//	}
//
//	public static class list<T> extends CollectorHelper<T, List<T>>{
//		@Override public Collector<T, ?, List<T>> getCollector() {
//			return Collectors.toList();
//		}
//	}
//
//	public static class set<T> extends CollectorHelper<T, Set<T>>{
//
//		@Override public Collector<T, ?, Set<T>> getCollector() {
//			return Collectors.toSet();
//		}
//	}
	@Override public java.util.List<T> get() {
		return upstream.get().collect(Collectors.toList());
	}
	@Override public List<T> getList() {
		return null;
	}
	@Override public Set<T> getSet() {
		return null;
	}


//
//	@Override public <R> R get(CollectorHelper<T, R> collectorHelper) {
//		 return upstream.get().collect(collectorHelper.getCollector());
//	}





	private <B> Supplier<Stream<B>> logFunction(Supplier<Stream<B>> supplier, String toPrint) {
		return () -> {
			Stream<B> stream = supplier.get();
			System.out.println(toPrint);
			return stream;
		};
	}

}
