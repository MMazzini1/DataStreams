package interfaceTest.LStream;

import interfaceTest.MStream.EagerMStream;
import interfaceTest.MStream.MStream;
import interfaceTest.Utils;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class EagerLStream<T> implements LStream<T> {



	private Collection<T> collection;

	public EagerLStream(Collection<T> collection) {
		this.collection = collection;
	}

	public EagerLStream() {
	}

//	@Override public LazyLStream.CollectorHelper<T, List<T>> list() {
//		return null;
//	}
//
//	@Override public LazyLStream.CollectorHelper<T, Set<T>> set() {
//		return null;
//	}

	public static <A> LStream<A> of(Collection<A> coll){
		return new EagerLStream<>(coll);

	}

	public static <A> LStream<A> of(A elem){
		return new EagerLStream<>(wrapAsList(elem));
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

	@Override public <K1, K2, K3> MStream<K1, MStream<K2, MStream<K3, LStream<T>>>> groupBy(Function<T, K1> classifier1, Function<T, K2> classifier2, Function<T, K3> classifier3) {
		return null;
	}



	@Override public LStream<LStream<T>> groupBy(Function<T, ?>... getters) {
		return null;
	}

	@Override public LStream<T> getUniqueValuesBy(Function<T, ?>... getters) {
		return null;
	}



	@Override public LStream<T> getUniqueValues() {
		return null;
	}

	@Override public LStream<T> keepDuplicateValues() {
		return null;
	}

	@Override public LStream<T> getDuplicateValueInstances() {
		HashSet<T> uniqueValues = new HashSet<>();
		List<T> duplicates = new ArrayList<>();
		for (T item: collection){
			boolean added = uniqueValues.add(item);
			if (!added){
				duplicates.add(item);
			}
		}
		return LStream.of(duplicates);

//		List<Integer> duplicates = IntStream.of( 1, 2, 3, 2, 1, 2, 3, 4, 2, 2, 2 )
//				.boxed()
//				.collect( Collectors.groupingBy( Function.identity(), Collectors.counting() ) )
//				.entrySet()
//				.stream()
//				.filter( p -> p.getValue() > 1 )
//				.map( Map.Entry::getKey )
//				.collect( Collectors.toList() );

	}

	@Override public LStream<T> removeDuplicates() {
		return null;
	}

	@Override public LStream<Boolean> hasUniqueValuesBy(Function<T, ?>... getters) {
		return null;
	}

	@Override public MStream<T, Long> getCountMapBy(Function<T, ?>... getters) {
		return null;
	}

	@Override public MStream<T, Long> getCountMap() {
		return null;
	}

	@Override public LStream<T> sort(Function<? super T, ? extends Comparable>... comparators) {

		Queue<Comparator<T>> comparatorQueue = new LinkedList<>();
		for (Function<? super T, ? extends Comparable> function : comparators) {
			Comparator<T> comparator = Comparator.comparing(function);
			comparatorQueue.add(comparator);
		}
		Comparator<T> chainedComparator = buildComparator(comparatorQueue, comparatorQueue.poll());
		List<T> sortedcollection = collection.stream().sorted(chainedComparator).collect(Collectors.toList());
		return of(sortedcollection);

	}

	@Override public LStream<T> sort(Comparator<T>... comparators) {

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


	@Override public LStream<T> filter(Predicate<T> predicate) {
		List<T> collect = collection.stream().filter(predicate::test).collect(Collectors.toList());
		return new EagerLStream<>(collect);
	}


	@Override public Collection<T> get() {
		return collection;
	}

	@Override public List<T> getList() {
		return null;
	}

	@Override public Set<T> getSet() {
		return null;
	}



	@Override public <B> EagerLStream<B> map(Function<T, B> mapper) {
		return new EagerLStream(collection.stream().map(mapper::apply).collect(Collectors.toList()));
	}


	@Override public LStream<Optional<T>> reduce(BinaryOperator<T> reducer) {
		return new EagerLStream<>(wrapAsList(collection.stream().reduce(reducer::apply)));
	}


	@Override public LStream<Boolean> hasUniqueValues() {
		HashSet<T> ts = new HashSet<>(collection);
		return new EagerLStream<>(wrapAsList(ts.size() == collection.size()));
	}

	@Override public LStream<T> addAll(LStream<T> other) {
		return null;
	}




	@Override public LStream<T> symmetricDiference(LStream<T> other) {
		return null;
	}

	public HashSet<T> symmetricDiference(EagerLStream<T> other) {
		HashSet<T> difference = new HashSet<>();
		difference.addAll(collection);
		difference.addAll(other.collection);
		difference.removeAll(intersection(other).get());
		return difference;
	}

	 public LStream<T> intersection(EagerLStream<T> other) {
		Set<T> intersection = new HashSet<T>(collection);
		intersection.retainAll(other.collection);
		return EagerLStream.of(new ArrayList<>(intersection));
	}


	@Override public LStream<T> distinct() {
		HashSet<T> ts = new HashSet<>();
		ts.addAll(collection);
		return EagerLStream.of(new ArrayList<>(ts));
	}

	@Override public LStream<Boolean> has(T item) {
		return new EagerLStream<Boolean>(wrapAsList(collection.contains(item)));
	}

	@Override public LStream<T> first() {
		Iterator<T> iterator = collection.iterator();
		return iterator.hasNext() ? new EagerLStream<T>(wrapAsList(iterator.next())) : emptyList();
	}


	private static <A> Collection<A> wrapAsList(A next) {
		ArrayList<A> ts = new ArrayList<>();
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
