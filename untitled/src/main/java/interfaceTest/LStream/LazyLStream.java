package interfaceTest.LStream;

import TestModel.Adress;
import TestModel.Person;
import interfaceTest.MStream.LazyMStream;
import interfaceTest.MStream.MStream;
import interfaceTest.Pair;
import jdk.nashorn.internal.ir.CallNode;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
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

		Collection<Person> people = of.getDuplicateValues().get();
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
		LStream<Person> distinctAgeNames = of.distinctBy(p -> p.getAge(), p -> p.getName()); //para hacer esto, necesarias crearte un wrapper vos. o agruparpor las dos cosas y quedarte con las keys
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
		Collection<LStream<Person>> lStreams1 = asd.groupBy(p -> p.getName(), p -> p.getAge(), p -> p.getName()).get();

		Collection<LStream<Person>> lStreams2 = asd.groupBy(p -> p.getName(), p -> p.getAge(), p -> p.getAdress()).get();

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

	}

	private Supplier<Stream<T>> upstream;

	public LazyLStream(Supplier<Stream<T>> sourceStream) {
		this.upstream = sourceStream;
	}

	//TODO eliminar, lo estoy usando para el empty pero prob deberia ser privado
	public LazyLStream() {
	}

	@Override public LStream<T> first() {
		return new LazyLStream<>(logFunction(() -> {
					Optional<T> first = upstream.get().findFirst();
					Stream<T> empty = Stream.empty();
					return first.isPresent() ? Stream.of(first.get()) : empty;
				}
				, "First"));
	}

	public static <A> LazyLStream<A> of(Collection<A> coll) {
		return new LazyLStream<A>(() -> (Stream<A>) coll.stream());
	}

	protected LazyLStream<T> addAll(LazyLStream<T> other) {

		return null;
	}

	@Override public <K> MStream<K, LStream<T>> groupBy(Function<T, K> classifier) {
		//todo evitar este paso de collect
		MStream<K, LStream<T>> mStream = new LazyMStream<>(logFunction(() -> {
			Map<K, LStream<T>> collect = upstream.get().collect(Collectors.groupingBy(classifier, new LStreamCollector<>()));
			Stream<Map.Entry<K, LStream<T>>> entryStream = collect.entrySet().stream();
			return entryStream;
		}, "grouping by"));
		return mStream;

	}

	//todo se puede hacer sin pasar x mapas intermedios?
	@Override public <K1, K2> MStream<K1, MStream<K2, LStream<T>>> groupBy(Function<T, K1> classifier1, Function<T, K2> classifier2) {
		MStream<K1, LStream<T>> k1LStreamMStream = groupBy(classifier1);
		MStream<K1, MStream<K2, LStream<T>>> k1MStreamMStream = k1LStreamMStream.remapValues(list -> list.groupBy(classifier2));
		return k1MStreamMStream;

		//		LazyMStream<K1, Map<K2, LStream<T>>> mStream = new LazyMStream<>(logFunction(() -> {
		//			MStream<K1, LStream<T>> k1LStreamMStream = groupBy(classifier1);
		//			MStream<K1, MStream<K2, LStream<T>>> k1MStreamMStream = k1LStreamMStream.remapValues(list -> list.groupBy(classifier2));
		//			Stream<Map.Entry<K1, Map<K2, LStream<T>>>> entryStream = k1MStreamMStream.entrySet().stream();
		//			return entryStream;
		//		}, "grouping by x 2"));

		//		LazyMStream<K1, Map<K2, LStream<T>>> mStream = new LazyMStream<>(logFunction(() -> {
		//			Map<K1, Map<K2, LStream<T>>> collect = upstream.get().collect(Collectors.groupingBy(classifier1,
		//					() -> new LazyMStream<>(), Collectors.groupingBy(classifier2,
		//					new LStreamCollector<>())));
		//			Stream<Map.Entry<K1, Map<K2, LStream<T>>>> entryStream = collect.entrySet().stream();
		//			return entryStream;
		//		}, "grouping by x 2"));
		//		return mStream;
	}

	//	@Override public LStream<T> filterUniques() {
	//		return new LazyLStream<>(
	//				logFunction(() -> upstream.get().
	//						collect(Collectors.groupingBy( Function.identity(), Collectors.counting() ))
	//						.entrySet()
	//						.stream()
	//						.filter( p -> p.getValue() == 1 )
	//						.map( Map.Entry::getKey ), "Filtering duplicates"));
	//	}

	//
	//	@Override public LStream<T> filterDuplicatesBy() {
	//		return new LazyLStream<>(
	//				logFunction(() -> upstream.get().
	//						collect(Collectors.groupingBy( Function.identity(), Collectors.counting() ))
	//						.entrySet()
	//						.stream()
	//						.filter( p -> p.getValue() == 1 )
	//						.map( Map.Entry::getKey ), "Filtering duplicates"));
	//	}

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

	//TODO no está implementaod, esto lo copie del otro. Este si tiene sentido.
	public LStream<T> getUniqueValuesBy() {
		return new LazyLStream<>(
				logFunction(() -> upstream.get().
						collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
						.entrySet()
						.stream()
						.filter(p -> p.getValue() == 1)
						.map(Map.Entry::getKey), "Filtering duplicates"));
	}

	//Devuelve los valores unicos en la lista (una aparición) según equals. Devuelve uns subconjunto de la lista
	@Override public LStream<T> getUniqueValues() {
		return new LazyLStream<>(
				logFunction(() -> upstream.get().
						collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
						.entrySet()
						.stream()
						.filter(p -> p.getValue() == 1)
						.map(Map.Entry::getKey), "Filtering duplicates"));
	}

	//Devuelve una lista en la cual se remueven las apariciones repetidas (con 2 o mas apariciones) de elementos, según
	//getters cuya combinacion define la igualdad a considerar. El elemento que "sobrevive" es elegido al azar.
	@Override public LStream<T> distinctBy(Function<T, ?>... getters) {
		return new LazyLStream<>(
				logFunction(() -> upstream.get()
								.map(item -> HashWrapper.of(item, getters))
								.collect(Collectors.toSet())
								.stream()
								.map(wrapper -> wrapper.object)
						, "Get uniques by"));
	}

	//Devuelve una lista en la cual se remueven las apariciones repetidas (con 2 o mas apariciones) de elementos, según equals.
	@Override public LStream<T> distinct() {
		return new LazyLStream<>(
				logFunction(() -> upstream.get().
						distinct(), "Getting duplicates"));
	}

	//Devuelve una aparicion de cada valor duplicados (con 2 o mas apariciones) en la lista, según equals. Devuelve un subconjunto de la lista
	@Override public LStream<T> getDuplicateValues() {
		return new LazyLStream<>(
				logFunction(() -> upstream.get().
						collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
						.entrySet()
						.stream()
						.filter(p -> p.getValue() > 1)
						.map(Map.Entry::getKey), "Getting duplicates"));
	}

	//TODO ex duplicates by
	//Devuelve todas las apariciones de valores duplicados (con 2 o mas apariciones) en la lista, según el equals definidio por la combinacion de getters
	public LStream<LStream<T>> groupBy(Function<T, ?>... getters) {
		LazyMStream<HashWrapper<T>, LStream<T>> hashWrapperLStreamMStream = (LazyMStream<HashWrapper<T>, LStream<T>>) groupBy(elem -> HashWrapper.of(elem, getters));
		LStream<LStream<T>> buckets = hashWrapperLStreamMStream.getBuckets();
		return buckets;

	}

//	@Override public MStream<T, Long> getCountMapBy(Function<T, ?>... getters) {
//		return new LazyMStream<>(
//				logFunction(() -> upstream.get().
//						collect(Collectors.groupingBy(elem -> HashWrapper.of(elem, getters), Collectors.counting()))
//						.entrySet().
//								stream()
//						.map(entry)
//				), "Getting count map"));
//	}

	@Override public MStream<T, Long> getCountMap() {
		return new LazyMStream<>(
				logFunction(() -> upstream.get().
						collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
						.entrySet().stream(), "Getting count map"));
	}

	@Override public LStream<T> sort(Function<? super T, ? extends Comparable>... comparators) {
		return null;
	}

	@Override public LStream<T> sort(Comparator<T>... comparators) {
		return null;
	}

	@Override public LStream<T> filter(Predicate<T> predicate) {
		return new LazyLStream<>(
				logFunction(() -> upstream.get().filter(predicate::test), "Filtering"));
	}

	//TODO se debe pasar el colector
	@Override public Collection<T> get() {
		return upstream.get().collect(Collectors.toList());
	}

	@Override public List<T> getAsList() {
		return null;
	}

	@Override public <B> LStream<B> map(Function<T, B> mapper) {
		return new LazyLStream<>(logFunction(() -> upstream.get().map(mapper::apply), "Mapping"));
	}

	@Override public Optional<T> reduce(BinaryOperator<T> reducer) {
		return Optional.empty();
	}

	@Override public boolean unique() {
		return false;
	}

	@Override public ArrayList<T> uniqueBy(BiPredicate<T, T> matcher) {
		return null;
	}

	@Override public void forEach(Consumer<T> consumer) {

	}

	@Override public HashSet<T> symmetricDiference(LStream<T> other) {
		return null;
	}

	@Override public boolean has(T item) {
		return false;
	}

	//TODO el orden queda al reves
	private <B> Supplier<Stream<B>> logFunction(Supplier<Stream<B>> supplier, String toPrint) {
		return () -> {
			Stream<B> stream = supplier.get();
			System.out.println(toPrint);
			return stream;
		};
	}

}
