package interfaceTest.LStream;

import TestModel.Adress;
import TestModel.Person;
import interfaceTest.MStream.EagerMStream;
import interfaceTest.MStream.MStream;
import interfaceTest.StreamUtils;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

public class EagerLStream<T> implements LStream<T> {

	public static void main(String[] args) {

		//TODO support parallel evaluation


		List<Person> lista = Arrays.asList(new Person(21, "Pablo", 1232332L, new Adress("Cabildo", 1232, 4)),
				new Person(33, "María", 17762322L, new Adress("Amenabar", 1232, 4)),
				new Person(21, "José", 1878732L, new Adress("Libertador", 1232, 4)),
				new Person(21, "Pablo", 124334332L, new Adress("Santa Fé", 1232, 4)),
				new Person(59, "Josef", 124334332L, new Adress("Santa Fé", 1232, 4)),
				new Person(100, "Jeronimo", 124334332L, new Adress("Santa Fé", 1232, 4)),
				new Person(121, "Pablo", 124334332L, new Adress("Santa Fé", 1232, 4)),
				new Person(200, "Pablo", 124334332L, new Adress("Santa Fé", 1232, 4))

		);

		LStream<Person> persons = LStream.of(lista, false);
		LStream<Person> filtered = persons.filter(person -> person.getName().equals("Pablo"));
		LStream<String> names = filtered.map(a -> a.getName());

		Collection<String> strings = names.get();
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




	private Collection<T> collection;

	public EagerLStream(Collection<T> collection) {
		this.collection = collection;
	}




	public static <A> EagerLStream<A> of(Collection<A> coll){
		return new EagerLStream<>(coll);

	}


	@Override public <K> MStream<K, LStream<T>> groupBy(Function<T, K> classifier) {
		//hacer clasificacion
		Map<K, LStream<T>> klStreamMap = StreamUtils.groupBy(collection, classifier);
		MStream<K, LStream<T>> of = EagerMStream.of(klStreamMap);
		return of;
	}

	@Override public <K1,K2> MStream<K1, MStream<K2, LStream<T>>> groupBy(Function<T, K1> classifier1, Function<T, K2> classifier2) {
		//hacer clasificacion
		Map<K1, MStream<K2, LStream<T>>> klStreamMap = null;
		MStream<K1, MStream<K2, LStream<T>>> of = EagerMStream.of(klStreamMap);
		return of;
	}


	@Override public EagerLStream<T> findDuplicates() {
		HashSet<T> uniqueValues = new HashSet<>();
		List<T> duplicates = new ArrayList<>();
		for (T item: collection){
			boolean added = uniqueValues.add(item);
			if (!added){
				duplicates.add(item);
			}
		}
		return EagerLStream.of(duplicates);

//		List<Integer> duplicates = IntStream.of( 1, 2, 3, 2, 1, 2, 3, 4, 2, 2, 2 )
//				.boxed()
//				.collect( Collectors.groupingBy( Function.identity(), Collectors.counting() ) )
//				.entrySet()
//				.stream()
//				.filter( p -> p.getValue() > 1 )
//				.map( Map.Entry::getKey )
//				.collect( Collectors.toList() );

	}

	@Override public EagerLStream<T> sort(Function<? super T, ? extends Comparable>... comparators) {

		Queue<Comparator<T>> comparatorQueue = new LinkedList<>();
		for (Function<? super T, ? extends Comparable> function : comparators) {
			Comparator<T> comparator = Comparator.comparing(function);
			comparatorQueue.add(comparator);
		}
		Comparator<T> chainedComparator = buildComparator(comparatorQueue, comparatorQueue.poll());
		List<T> sortedcollection = collection.stream().sorted(chainedComparator).collect(Collectors.toList());
		return of(sortedcollection);

	}

	@Override public EagerLStream<T> sort(Comparator<T>... comparators) {

		Queue<Comparator<T>> comparatorQueue = new LinkedList<>();
		Collections.addAll(comparatorQueue, comparators);
		Comparator<T> chainedComparator = buildComparator(comparatorQueue, comparatorQueue.poll());
		List<T> sortedcollection = collection.stream().sorted(chainedComparator).collect(Collectors.toList());
		return of(sortedcollection);

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


	@Override public EagerLStream<T> filter(Predicate<T> predicate) {
		List<T> collect = collection.stream().filter(predicate::test).collect(Collectors.toList());
		return new EagerLStream<>(collect);
	}


	@Override public Collection<T> get() {
		return collection;
	}

	@Override public List<T> getAsList() {
		return null;
	}

	@Override public <B> EagerLStream<B> map(Function<T, B> mapper) {
		return new EagerLStream(collection.stream().map(mapper::apply).collect(Collectors.toList()));
	}


	@Override public Optional<T> reduce(BinaryOperator<T> reducer) {
		return collection.stream().reduce(reducer::apply);
	}


	@Override public boolean unique() {
		HashSet<T> ts = new HashSet<>(collection);
		return ts.size() == collection.size();
	}

	@Override public ArrayList<T> uniqueBy(BiPredicate<T, T> matcher) {
//		ArrayList<T> repeated = new ArrayList<>();
//		for (int i = 0; i < collection.size(); i++) {
//			for (int j = 0; j < collection.size(); j++) {
//				if (j != i) {
//					T thiz = collection.get(i);
//					T that = collection.get(j);
//					if (matcher.test(thiz, that)) {
//						repeated.add(thiz);
//					}
//				}
//			}
//		}
//		boolean unique = repeated.isEmpty();
//
//		return repeated;

		return null;
	}

	@Override public void forEach(Consumer<T> consumer) {
		collection.stream().forEach(consumer::accept);
		return;
	}


	@Override public HashSet<T> symmetricDiference(LStream<T> other) {
		return null;
	}

	public HashSet<T> symmetricDiference(EagerLStream<T> other) {
		HashSet<T> difference = new HashSet<>();
		difference.addAll(collection);
		difference.addAll(other.collection);
		difference.removeAll(intersection(other).get());
		return difference;
	}

	 public EagerLStream<T> intersection(EagerLStream<T> other) {
		Set<T> intersection = new HashSet<T>(collection);
		intersection.retainAll(other.collection);
		return EagerLStream.of(new ArrayList<>(intersection));
	}


	@Override public EagerLStream<T> removeDuplicates() {
		HashSet<T> ts = new HashSet<>();
		ts.addAll(collection);
		return EagerLStream.of(new ArrayList<>(ts));
	}

	@Override public boolean has(T item) {
		return collection.contains(item);
	}



}
