package Data;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//TODO leer https://stackoverflow.com/questions/2704652/monad-in-plain-english-for-the-oop-programmer-with-no-fp-background?rq=1

//Todo para nested lists, probablemente MapData deberia ser un tipo anidado
public class ListData<T, Q> implements ListDataBase<T> {

	//TODO revisar https://github.com/jOOQ/jOOL

	//TODO lazy y no lazy

	private boolean debug;
	private Supplier<Stream<T>> dataStream;
	//	private List<T> data;

	//	public List<T> evaluate(List<Q>  toEval){
	//		return evaluate(new SourceHolder<>(toEval));
	//	}
	//	public ListData(Supplier<Stream<T>> dataStream) {
	//		this.dataStream = dataStream;
	//	}

	//todo separar en dos clases?

	//API SOURCE LESS
	private SourceHolder<Q> source = new SourceHolder<Q>();




	public static <Q> ListData<Q, Q> of(Class<Q> clazz) {
		return new ListData<Q,Q>();
	}

	public ListData() {
		this.dataStream = () -> (Stream<T>) source.getStreamSource().stream();
	}


	public List<T> evaluate(List<Q> toEval) {
		source.setStreamSource(toEval);
		List<T> collect = dataStream.get().collect(Collectors.toList());
		return collect;
	}



	public ListData(Supplier<Stream<T>> dataStream, SourceHolder<Q> holder) {
		this.dataStream = dataStream;
		this.source = holder;
	}



	private <B> Supplier<Stream<B>> logFunction(Supplier<Stream<B>> supplier, String toPrint) {
		return () -> {
			System.out.println(toPrint);
			return supplier.get();
		};
	}

	private <B> ListData<B, Q> appendStream(Supplier<Stream<B>> stream) {
		return new ListData(stream, source);
	}

	@Override public ListData<T, Q> filter(Predicate<T> predicate) {
		return appendStream(
				logFunction(() -> dataStream.get().filter(predicate::test), "Logging"));
	}

	@Override public <B> ListData<B, Q> map(Function<T, B> mapper) {
		return appendStream(logFunction(() -> dataStream.get().map(mapper::apply), "Mapping"));
	}



	@Override public ReducedData<T, Q> reduce(BinaryOperator<T> reducer) {
		return new ReducedData(dataStream, source, reducer);
	}

	@Override public ListData<T, Q> sort(Function<? super T, ? extends Comparable>... comparators) {
		Queue<Comparator<T>> comparatorQueue = new LinkedList<>();
		for (Function<? super T, ? extends Comparable> function : comparators) {
			Comparator<T> comparator = Comparator.comparing(function);
			comparatorQueue.add(comparator);
		}
		Comparator<T> chainedComparator = buildComparator(comparatorQueue, comparatorQueue.poll());

		return appendStream(logFunction(() -> dataStream.get().sorted(chainedComparator), "Sorting"));
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





//	//TODO esto lo hice en wellprod y está piola
//	public ListData<T> keepUniquesBy(Function<T, ?>... getter) {
//		if (getter.length == 0) {
//			throw new IllegalArgumentException("UniqueBy debe recibir al menos un getter. Llamar a unique si se desea utilizar el hash/equals"
//					+ "default del objeto");
//		}
//		Set<HashEqualsWrapper<T>> wrappedList = data.stream().map(item -> HashEqualsWrapper.of(item, getter)).collect(Collectors.toSet());
//		List<T> uniques = wrappedList.stream().map(wrapped -> wrapped.object).collect(Collectors.toList());
//		return ListData.of(uniques);
//	}

	//	public ListData<T,Q> sort(Comparator<T>... comparators) {
	//
	//		Queue<Comparator<T>> comparatorQueue = new LinkedList<>();
	//		Collections.addAll(comparatorQueue, comparators);
	//		Comparator<T> chainedComparator = buildComparator(comparatorQueue, comparatorQueue.poll());
	//		List<T> sortedData = data.stream().sorted(chainedComparator).collect(Collectors.toList());
	//		return of(sortedData);
	//
	//	}



	//	public List<T> getAndSave() {
	//
	//		data = dataStream.get().collect(Collectors.toList());
	//		dataStream = () -> data.stream();
	//		return data;
	//	}
	//
	//	public List<T> get() {
	//		if (data == null) {
	//			throw new NullPointerException("Get de listData devuelve null");
	//		}
	//		List<T> collect = dataStream.get().collect(Collectors.toList());
	//		return collect;
	//	}

	//	public ListData<T> join(ListData<T> two) {
	//		return ListData.of(this.concat(two));
	//	}
	//
	//	public static <K> ListData<K> empty(Class<K> clazz) {
	//		return new ListData<K>(new ArrayList<K>());
	//	}

	// Filter devuelve lista o elemento unico
	//	public <K> ListData<K> partitionAndMap(Predicate<T> predicate, Function<T,K> trueMapper, Function<T,K> falseMapper) {
	//		List<K> trueList = this.data.stream().filter(predicate).map(trueMapper).collect(Collectors.toList());
	//		List<K> falseList = this.data.stream().filter(predicate.negate()).map(falseMapper).collect(Collectors.toList());
	//		trueList.addAll(falseList);
	//		return ListData.of(trueList);
	//	}

	//	private ListData<T> remove(HashSet<T> added) {
	//		return ListData.of(data.stream().filter(item -> !added.contains(item)).collect(Collectors.toList()));
	//	}
	//
	//	private Collection<T> removeAux(HashSet<T> added) {
	//		return data.stream().filter(item -> !added.contains(item)).collect(Collectors.toList());
	//	}
	//
	//	public ListData<T> mapAndJoin(Function<T, T> mapper) {
	//		ListData<T> of = ListData.of(data.stream().map(mapper::apply).collect(Collectors.toList()));
	//		ListData<T> join = this.join(of);
	//		return join;
	//	}

	//	public <V> MapData<V, List<T>> groupBy(Function<T, V> classifier) {
	//		Map<V, List<T>> grouped = StreamUtils.groupBy(data, classifier::apply);
	//		return MapData.of(grouped);
	//	}
	//
	//
	//	public <V> MapData<V, ListData<T>> groupByWrapped(Function<T, V> classifier) {
	//		MapData<V, List<T>> vListMapData = groupBy(classifier);
	//		MapData<V, ListData<T>> vListDataMapData = vListMapData.remapValues(list -> ListData.of(list));
	//		return vListDataMapData;
	//	}
	//
	//
	//
	//	public <V, R> MapData<V, Map<R, List<T>>> groupBy(Function<T, V> classifier1, Function<T, R> classifier2) {
	//		Map<V, Map<R, List<T>>> vMapMap = StreamUtils.groupBy(data, classifier1::apply, classifier2::apply);
	//		return MapData.of(vMapMap);
	//	}
	//
	//	public Optional<T> reduce(BinaryOperator<T> reducer) {
	//		return data.stream().reduce(reducer::apply);
	//	}
	//
	//	private List<T> concat(ListData<T> two) {
	//		List<T> result = new ArrayList<>();
	//		result.addAll(this.data);
	//		result.addAll(two.data);
	//		return result;
	//	}
	//
	//	//TODO validate type list. K representa el subtipo interno de la lista. validate K
	//	public <K> ListData<K> reduceForList(BinaryOperator<K> reducer) {
	//		List<K> collect = data.stream().map(list -> ((List<K>) list).stream().reduce(reducer::apply).get()).collect(Collectors.toList());
	//		return ListData.of(collect);
	//	}
	//
	//	//TODO validate type list. K representa el subtipo interno de la lista. validate K
	//	public boolean unique() {
	//		HashSet<T> ts = new HashSet<>(data);
	//		return ts.size() == data.size();
	//	}
	//
	//	public HashSet<T> unique(String message) {
	//		HashSet<T> ts = new HashSet<>(data);
	//		boolean unique = ts.size() == data.size();
	//		System.out.println(message + " is unique: " + unique);
	//		return ts;
	//	}
	//
	//
	//
	//
	//	private static class HashEqualsWrapper<E>{
	//		private E object;
	//		private Function<E, ?>[] getters;
	//
	//		public static <K> HashEqualsWrapper<K> of(K k, Function<K, ?>... getter) {
	//			HashEqualsWrapper<K> kHashEqualsWrapper = new HashEqualsWrapper<>();
	//			kHashEqualsWrapper.object = k;
	//			kHashEqualsWrapper.getters = getter;
	//			return kHashEqualsWrapper;
	//
	//		}
	//
	//		@Override public boolean equals(Object o) {
	//			for (Function<E, ?> getter: getters){
	//				if (!getter.apply(object).equals(getter.apply(((HashEqualsWrapper<E>) o).object))){
	//					return false;
	//				}
	//			}
	//			return true;
	//		}
	//
	//		@Override public int hashCode() {
	//			int hashCode = 0;
	//			for (Function<E, ?> getter: getters){
	//				int partialHashCode = getter.apply(object).hashCode();
	//				hashCode = hashCode + partialHashCode;
	//			}
	//			return hashCode;
	//		}
	//
	//	}
	//
	//
	//
	//	//TODO deleteDuplicatesBy
	//
	//	//TODO que acepte infinitas funciones
	//	//TODO hashset, deberia tener complejidad lineal
	//	//https://stackoverflow.com/questions/5453226/java-need-a-hash-map-where-one-supplies-a-function-to-do-the-hashing
	////	public boolean uniqueBy(Function<T, ?> getter) {
	////		HashSet<HashEqualsWrapper<T>> uniques = new HashSet<>();
	////		List<HashEqualsWrapper<T>> wrappedList = data.stream().map(item -> HashEqualsWrapper.of(item, getter)).collect(Collectors.toList());
	////		uniques.addAll(wrappedList);
	////		return uniques.size() == data.size();
	////	}
	//
	//
	//	/**  O(n) */
	//	public boolean uniqueBy(Function<T, ?>... getter) {
	//		if (getter.length == 0){
	//			throw new IllegalArgumentException("UniqueBy debe recibir al menos un getter. Llamar a unique si se desea utilizar el hash/equals"
	//					+ "default del objeto");
	//		}
	//		HashSet<HashEqualsWrapper<T>> uniques = new HashSet<>();
	//		List<HashEqualsWrapper<T>> wrappedList = data.stream().map(item -> HashEqualsWrapper.of(item, getter)).collect(Collectors.toList());
	//		uniques.addAll(wrappedList);
	//		return uniques.size() == data.size();
	//	}
	//
	//
	//	//TODO hashset, deberia tener complejidad lineal
	//	//https://stackoverflow.com/questions/5453226/java-need-a-hash-map-where-one-supplies-a-function-to-do-the-hashing
	//	public ArrayList<T> uniqueBy(BiPredicate<T, T> matcher) {
	//		ArrayList<T> repeated = new ArrayList<>();
	//		for (int i = 0; i < data.size(); i++) {
	//			for (int j = 0; j < data.size(); j++) {
	//				if (j != i) {
	//					T thiz = data.get(i);
	//					T that = data.get(j);
	//					if (matcher.test(thiz, that)) {
	//						repeated.add(thiz);
	//					}
	//				}
	//			}
	//		}
	//		boolean unique = repeated.isEmpty();
	//		return repeated;
	//	}
	//
	//
	//
	//	public void forEach(Consumer<T> consumer) {
	//		data.stream().forEach(consumer::accept);
	//		return;
	//	}
	//
	//	public HashSet<T> symmetricDiference(ListData<T> other) {
	//		HashSet<T> difference = new HashSet<>();
	//		difference.addAll(data);
	//		difference.addAll(other.data);
	//		difference.removeAll(intersection(other).get());
	//		return difference;
	//	}
	//
	//	public ListData<T> intersection(ListData<T> other) {
	//		Set<T> intersection = new HashSet<T>(data);
	//		intersection.retainAll(other.data);
	//		return ListData.of(new ArrayList<>(intersection));
	//	}
	//
	//
	//	public ListData<T> deleteDuplicates() {
	//		HashSet<T> ts = new HashSet<>();
	//		ts.addAll(data);
	//		return ListData.of(new ArrayList<>(ts));
	//	}
	//
	//	public boolean has(T item) {
	//		return data.contains(item);
	//	}
	//
	//	public BigDecimal sumProperty(Function<T, BigDecimal> mapper) {
	//		return data.stream().map( mapper::apply).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
	//	}
	//
	//	public ListData<T> findDuplicates() {
	//		HashSet<T> uniqueValues = new HashSet<>();
	//		List<T> duplicates = new ArrayList<>();
	//		for (T item: data){
	//			boolean added = uniqueValues.add(item);
	//			if (!added){
	//				duplicates.add(item);
	//			}
	//		}
	//		return ListData.of(duplicates);
	//	}
}


