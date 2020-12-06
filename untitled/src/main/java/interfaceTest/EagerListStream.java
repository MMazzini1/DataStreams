package interfaceTest;

import TestModel.Person;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

public class EagerListStream<T> implements AbstractListStream<T, EagerListStream<T>> {

	public static void main(String[] args) {

		EagerListStream<Person> of = of(new ArrayList<Person>());

		MapStream<Integer, MapStream<String, EagerListStream<Person>>> integerMapStreamMapStream = of.groupBy(person -> person.getAge(),
				person -> person.toString());

		EagerListStream<Person> filter = of.filter(p -> p.getAge() > 3);

		EagerListStream<Boolean> aa = filter.map(p -> p.getName().equals("aa"));


		

		//		AbstractListStream<AbstractListStream<Person>> buckets = grouped.getBuckets(Person.class);






	}




	private Collection<T> collection;

	public EagerListStream(Collection<T> collection) {
		this.collection = collection;
	}




	public static <A> EagerListStream<A> of(Collection<A> coll){
		return new EagerListStream<>(coll);

	}


	@Override public <K> MapStream<K, EagerListStream<T>> groupBy(Function<T, K> classifier) {
		//hacer clasificacion
		HashMap<K, EagerListStream<T>> map = new HashMap<>();
		return MapStream.of(map);
	}

	@Override public <K1,K2> MapStream<K1,MapStream<K2, EagerListStream<T>>> groupBy(Function<T, K1> classifier1, Function<T, K2> classifier2) {
		//hacer clasificacion
		HashMap<K1, Map<K2, EagerListStream<T>>> map = new HashMap<>();
		return MapStream.of(map);
	}


	@Override public EagerListStream<T> findDuplicates() {
		HashSet<T> uniqueValues = new HashSet<>();
		List<T> duplicates = new ArrayList<>();
		for (T item: collection){
			boolean added = uniqueValues.add(item);
			if (!added){
				duplicates.add(item);
			}
		}
		return EagerListStream.of(duplicates);

//		List<Integer> duplicates = IntStream.of( 1, 2, 3, 2, 1, 2, 3, 4, 2, 2, 2 )
//				.boxed()
//				.collect( Collectors.groupingBy( Function.identity(), Collectors.counting() ) )
//				.entrySet()
//				.stream()
//				.filter( p -> p.getValue() > 1 )
//				.map( Map.Entry::getKey )
//				.collect( Collectors.toList() );

	}

	@Override public EagerListStream<T> sort(Function<? super T, ? extends Comparable>... comparators) {

		Queue<Comparator<T>> comparatorQueue = new LinkedList<>();
		for (Function<? super T, ? extends Comparable> function : comparators) {
			Comparator<T> comparator = Comparator.comparing(function);
			comparatorQueue.add(comparator);
		}
		Comparator<T> chainedComparator = buildComparator(comparatorQueue, comparatorQueue.poll());
		List<T> sortedcollection = collection.stream().sorted(chainedComparator).collect(Collectors.toList());
		return of(sortedcollection);

	}

	@Override public EagerListStream<T> sort(Comparator<T>... comparators) {

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

	// Filter devuelve lista o elemento unico
	@Override public EagerListStream<T> filter(Predicate<T> predicate) {
		List<T> collect = collection.stream().filter(predicate::test).collect(Collectors.toList());
		return EagerListStream.of(collect);
	}


	@Override public Collection<T> get() {
		return collection;
	}

	@Override public <B> EagerListStream<B> map(Function<T, B> mapper) {
		return EagerListStream.of(collection.stream().map(mapper::apply).collect(Collectors.toList()));
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

	@Override public HashSet<T> symmetricDiference(EagerListStream<T> other) {
		HashSet<T> difference = new HashSet<>();
		difference.addAll(collection);
		difference.addAll(other.collection);
		difference.removeAll(intersection(other).get());
		return difference;
	}

	@Override public EagerListStream<T> intersection(EagerListStream<T> other) {
		Set<T> intersection = new HashSet<T>(collection);
		intersection.retainAll(other.collection);
		return EagerListStream.of(new ArrayList<>(intersection));
	}


	@Override public EagerListStream<T> removeDuplicates() {
		HashSet<T> ts = new HashSet<>();
		ts.addAll(collection);
		return EagerListStream.of(new ArrayList<>(ts));
	}

	@Override public boolean has(T item) {
		return collection.contains(item);
	}



}
