package interfaceTest.LStream;

import TestModel.Adress;
import TestModel.Person;
import interfaceTest.MStream.EagerMStream;
import interfaceTest.MStream.MStream;
import interfaceTest.StreamUtils;
import interfaceTest.Utils;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

public class EagerLStream<T> implements LStream<T> {





	private Collection<T> collection;

	public EagerLStream(Collection<T> collection) {
		this.collection = collection;
	}

	public EagerLStream() {
	}

	public static <A> EagerLStream<A> of(Collection<A> coll){
		return new EagerLStream<>(coll);

	}

	public static <A> LStream<A> emptyList() {
		return new EagerLStream(new ArrayList());
	}

	@Override public <K> MStream<K, LStream<T>> groupBy(Function<T, K> classifier) {
		Map<K, LStream<T>> klStreamMap = Utils.groupBy(collection, classifier);
		//todo pq este no anda con el constr? y si con el metodo statico
		MStream<K, LStream<T>> of = EagerMStream.of(klStreamMap);
		return of;
	}

	@Override public <K1,K2> MStream<K1, MStream<K2, LStream<T>>> groupBy(Function<T, K1> classifier1, Function<T, K2> classifier2) {
		//hacer clasificacion
//		Map<K1, Map<K2, LStream<T>>> k1MapMap = Utils.groupBy(collection, classifier1, classifier2);
		//todo pq este no anda con el constr? y si con el metodo statico
		MStream<K1, LStream<T>> k1LStreamMStream = groupBy(classifier1);
		MStream<K1, MStream<K2, LStream<T>>> result = k1LStreamMStream.remapValues(list -> list.groupBy(classifier2));
		return result;
	}

	@Override public LStream<T> distinctBy(Function<T, ?>... getters) {
		return null;
	}

	@Override public LStream<T> getUniqueValues() {
		return null;
	}

	@Override public EagerLStream<T> getDuplicateValues() {
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

	@Override public MStream<T, Long> getCountMap() {
		return null;
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


	@Override public EagerLStream<T> distinct() {
		HashSet<T> ts = new HashSet<>();
		ts.addAll(collection);
		return EagerLStream.of(new ArrayList<>(ts));
	}

	@Override public boolean has(T item) {
		return collection.contains(item);
	}

	@Override public LStream<T> first() {
		Iterator<T> iterator = collection.iterator();
		return iterator.hasNext() ? new EagerLStream<T>(wrapAsList(iterator.next())) : emptyList();
	}

	private Collection<T> wrapAsList(T next) {
		ArrayList<T> ts = new ArrayList<>();
		ts.add(next);
		return ts;
	}

	//TODO protected
	public void add(T val){
		collection.add(val);
	}

//	@Override public String toString() {
//		return "[" +  " EagerLStream = " + collection.toString() +   " ]";
//	}
}
