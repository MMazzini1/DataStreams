package Data;


import java.math.BigDecimal;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

//Todo para nested lists, probablemente MapData deberia ser un tipo anidado
public class ListData<T> {



	private List<T> data;

	public ListData(List<T> data) {
		this.data = data;
	}

	public static <T> ListData<T> of(List<T> list) {
		return new ListData<T>(list);
	}

	public ListData<T> join(ListData<T> two) {
		return ListData.of(this.concat(two));
	}

	public static <K> ListData<K> empty(Class<K> clazz) {
		return new ListData<K>(new ArrayList<K>());
	}

	public ListData<T> sort(Function<? super T, ? extends Comparable>... comparators) {

		Queue<Comparator<T>> comparatorQueue = new LinkedList<>();
		for (Function<? super T, ? extends Comparable> function : comparators) {
			Comparator<T> comparator = Comparator.comparing(function);
			comparatorQueue.add(comparator);
		}
		Comparator<T> chainedComparator = buildComparator(comparatorQueue, comparatorQueue.poll());
		List<T> sortedData = data.stream().sorted(chainedComparator).collect(Collectors.toList());
		return of(sortedData);

	}

	public ListData<T> sort(Comparator<T>... comparators) {

		Queue<Comparator<T>> comparatorQueue = new LinkedList<>();
		Collections.addAll(comparatorQueue, comparators);
		Comparator<T> chainedComparator = buildComparator(comparatorQueue, comparatorQueue.poll());
		List<T> sortedData = data.stream().sorted(chainedComparator).collect(Collectors.toList());
		return of(sortedData);

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

	/**
	 * OUTER JOINS: define outer join como la operacion de consolidar dos listas, obteniendo resultados si matchean + los que no matchean del lado izquierdo + los que no matchean del lado derecho
	*/
	public ListData<T> outerJoin(ListData<T> toMerge, BiFunction<T, T, T> merger) {
		List<T> mergedResult = new ArrayList<T>();
		HashSet<T> notProcessedThis = new HashSet<>(data);
		HashSet<T> notProcessedOther = new HashSet<>(toMerge.data);
		for (T thisItem : data) {
			for (T otherItem : toMerge.data) {
				if (thisItem.equals(otherItem)) {
					T merged = merger.apply(thisItem, otherItem);
					mergedResult.add(merged);
					notProcessedThis.remove(thisItem);
					notProcessedOther.remove(otherItem);
				}
			}
		}
		mergedResult.addAll(notProcessedThis);
		mergedResult.addAll(notProcessedOther);
		return ListData.of(mergedResult);
	}

	/**  LEFT JOINS se define left join como la operacion de consolidar dos listas, obteniendo resultados si matchean + los que no matchean del lado izquierdo */

	/**  LEFT JOIN MISMO TIPO*/
	//	public ListData<T> leftJoin(ListData<T> toMerge, BiFunction<T, T, T> merger) {
	//		List<T> mergedResult = new ArrayList<>();
	//		HashSet<T> added = new HashSet<T>(this.data);
	//		for (T otherItem : toMerge.data) {
	//			for (T thisItem: this.data) {
	//				if (thisItem.equals(otherItem)) {
	//					T merged = merger.apply(thisItem, otherItem);
	//					mergedResult.add(merged);
	//				}
	//			}
	//		}
	//		mergedResult.addAll(this.removeAux(added));
	//		return ListData.of(mergedResult);
	//	}

	/**
	 * LEFT JOIN DISTINTO TIPO CON FUNCION DEFAULTER  Y THROWS SI HAY MÁS DE UN MATCH PARA UN ELEMENTO DADO DE CUALQUIERA DE LAS DOS LISTAS
	 */
	public <X, K> ListData<K> leftJoinThrowsAtMostOneMatch(ListData<X> toMerge, BiFunction<T, X, K> merger, Function<T, K> defaulter) {
		List<K> mergedResult = new ArrayList<>();
		HashSet<T> notAdded = new HashSet<T>(data);
		for (T thisItem : data) {
			boolean matched = false;
			for (X otherItem : toMerge.data) {
				if (thisItem.equals(otherItem)) {
					if (matched) {
						throw new RuntimeException("Doble match para un mismo item: " + otherItem.toString());
					}
					K merged = merger.apply(thisItem, otherItem);
					mergedResult.add(merged);
					notAdded.remove(thisItem);
					matched = true;
				}
			}
		}
		mergedResult.addAll(notAdded.stream().map(defaulter::apply).collect(Collectors.toList()));
		return ListData.of(mergedResult);
	}

	public ListData<T> leftJoinThrowsAtMostOneMatch(ListData<T> toMerge, BiFunction<T, T, T> merger){
		ListData<T> result = leftJoinThrowsAtMostOneMatch(toMerge, merger, a -> a);
		return result;
	}

	/**  INNER JOINS: se define inner join como la operacion de consolidar dos listas, obteniendo resultado solos en los casos que matchean */

	/**  INNER JOIN CON MERGER  */
	//	public <X, K> ListData<K> innerJoin(ListData<X> toMerge, BiFunction<T, X, K> merger) {
	//		List<K> mergedResult = new ArrayList<>();
	//		for (X otherItem : toMerge.data) {
	//			for (T thisItem: data) {
	//					if (thisItem.equals(otherItem)) {
	//						K merged = merger.apply(thisItem, otherItem);
	//						mergedResult.add(merged);
	//					}
	//			}
	//		}
	//		return ListData.of(mergedResult);
	//	}

	/**  INNER JOIN CON MERGER Y MATCHER  */
	//	public <X, K> ListData<K> innerJoin(ListData<X> toMerge, BiFunction<T, X, K> merger, BiPredicate<T, X> matcher) {
	//		List<K> mergedResult = new ArrayList<>();
	//		for (X otherItem : toMerge.data) {
	//			for (T thisItem : data) {
	//				if (matcher.test(thisItem, otherItem)) {
	//					K merged = merger.apply(thisItem, otherItem);
	//					mergedResult.add(merged);
	//				}
	//			}
	//		}
	//		return ListData.of(mergedResult);
	//	}

	/**
	 * INNER JOIN CON MERGER Y THROWS SI AL MENSO ALGUN ELEMENTO DEL LEFT SIDE NO JOINEA CON NADA. SE ADMITE MAS DE UN MATCH
	 */
	public <X, K> ListData<K> innerJoinThrowsLeftAtLeastOneMatch(ListData<X> toMerge, BiFunction<T, X, K> merger, BiPredicate<T, X> matcher) {
		List<K> mergedResult = new ArrayList<>();
		for (T thisItem : data) {
			boolean matched = false;
			for (X otherItem : toMerge.data) {
				if (matcher.test(thisItem, otherItem)) {
					K merged = merger.apply(thisItem, otherItem);
					mergedResult.add(merged);
					matched = true;
				}
			}
			if (!matched) {
				throw new RuntimeException("Ningún match para el item: " + thisItem.toString());
			}
		}
		return ListData.of(mergedResult);
	}

	/**
	 * INNER JOIN CON MERGER Y MATCHER Y THROWS SI HAY NINGÚN O MÁS DE UN MATCH PARA UN ELEMENTO DADO DE CUALQUIERA DE LAS DOS LISTAS
	 */
	public <X, K> ListData<K> innerJoinThrowsAlwaysOneMatch(ListData<X> toMerge, BiFunction<T, X, K> merger, BiPredicate<T, X> matcher) {
		List<K> mergedResult = new ArrayList<>();
		for (X otherItem : toMerge.data) {
			boolean matched = false;
			for (T thisItem : data) {
				if (matcher.test(thisItem, otherItem)) {
					if (matched) {
						throw new RuntimeException("Doble match para un mismo item: " + otherItem.toString());
					}
					K merged = merger.apply(thisItem, otherItem);
					mergedResult.add(merged);
					matched = true;
				}
			}
			if (!matched) {
				throw new RuntimeException("Ningún match para el item: " + otherItem.toString());
			}
		}
		return ListData.of(mergedResult);
	}

	public <X, K> ListData<K> innerJoinThrowsAlwaysOneMatch(ListData<X> toMerge, BiFunction<T, X, K> merger) {
		List<K> mergedResult = new ArrayList<>();
		for (X otherItem : toMerge.data) {
			boolean matched = false;
			for (T thisItem : data) {
				if (thisItem.equals(otherItem)) {
					if (matched) {
						throw new RuntimeException("Doble match para un mismo item: " + otherItem.toString());
					}
					K merged = merger.apply(thisItem, otherItem);
					mergedResult.add(merged);
					matched = true;
				}
			}
			if (!matched) {
				throw new RuntimeException("Ningún match para el item: " + otherItem.toString());
			}
		}
		return ListData.of(mergedResult);
	}


	/**
	 * INNER JOIN CON MERGER Y MATCHER Y THROWS SI HAY NINGÚN O MÁS DE UN MATCH PARA UN ELEMENTO DADO DE CUALQUIERA DE LAS DOS LISTAS
	 */
	public <X, K> ListData<K> innerJoin(ListData<X> toMerge, BiFunction<T, X, K> merger, BiPredicate<T, X> matcher) {
		List<K> mergedResult = new ArrayList<>();
		for (X otherItem : toMerge.data) {
			boolean matched = false;
			for (T thisItem : data) {
				if (matcher.test(thisItem, otherItem)) {
					K merged = merger.apply(thisItem, otherItem);
					mergedResult.add(merged);
					matched = true;
				}
			}
		}
		return ListData.of(mergedResult);
	}


	// Filter devuelve lista o elemento unico
	public <K> ListData<K> partitionAndMap(Predicate<T> predicate, Function<T,K> trueMapper, Function<T,K> falseMapper) {
		List<K> trueList = this.data.stream().filter(predicate).map(trueMapper).collect(Collectors.toList());
		List<K> falseList = this.data.stream().filter(predicate.negate()).map(falseMapper).collect(Collectors.toList());
		trueList.addAll(falseList);
		return ListData.of(trueList);
	}


	// Filter devuelve lista o elemento unico
	public ListData<T> filter(Predicate<T> predicate) {
		List<T> collect = data.stream().filter(predicate::test).collect(Collectors.toList());
		return ListData.of(collect);
	}

	public List<T> get() {
		if (data == null){
			throw new NullPointerException("Get de listData devuelve null");
		}
		return data;
	}

	private ListData<T> remove(HashSet<T> added) {
		return ListData.of(data.stream().filter(item -> !added.contains(item)).collect(Collectors.toList()));
	}

	private Collection<T> removeAux(HashSet<T> added) {
		return data.stream().filter(item -> !added.contains(item)).collect(Collectors.toList());
	}

	public ListData<T> mapAndJoin(Function<T, T> mapper) {
		ListData<T> of = ListData.of(data.stream().map(mapper::apply).collect(Collectors.toList()));
		ListData<T> join = this.join(of);
		return join;
	}

	public <B> ListData<B> map(Function<T, B> mapper) {
		return ListData.of(data.stream().map(mapper::apply).collect(Collectors.toList()));
	}

	public <V> MapData<V, List<T>> groupBy(Function<T, V> classifier) {
		Map<V, List<T>> grouped = StreamUtils.groupBy(data, classifier::apply);
		return MapData.of(grouped);
	}



	public <V> MapData<V, ListData<T>> groupByWrapped(Function<T, V> classifier) {
		MapData<V, List<T>> vListMapData = groupBy(classifier);
		MapData<V, ListData<T>> vListDataMapData = vListMapData.remapValues(list -> ListData.of(list));
		return vListDataMapData;
	}





	public <V, R> MapData<V, Map<R, List<T>>> groupBy(Function<T, V> classifier1, Function<T, R> classifier2) {
		Map<V, Map<R, List<T>>> vMapMap = StreamUtils.groupBy(data, classifier1::apply, classifier2::apply);
		return MapData.of(vMapMap);
	}

	public Optional<T> reduce(BinaryOperator<T> reducer) {
		return data.stream().reduce(reducer::apply);
	}

	private List<T> concat(ListData<T> two) {
		List<T> result = new ArrayList<>();
		result.addAll(this.data);
		result.addAll(two.data);
		return result;
	}

	//TODO validate type list. K representa el subtipo interno de la lista. validate K
	public <K> ListData<K> reduceForList(BinaryOperator<K> reducer) {
		List<K> collect = data.stream().map(list -> ((List<K>) list).stream().reduce(reducer::apply).get()).collect(Collectors.toList());
		return ListData.of(collect);
	}

	//TODO validate type list. K representa el subtipo interno de la lista. validate K
	public boolean unique() {
		HashSet<T> ts = new HashSet<>(data);
		return ts.size() == data.size();
	}

	public HashSet<T> unique(String message) {
		HashSet<T> ts = new HashSet<>(data);
		boolean unique = ts.size() == data.size();
		System.out.println(message + " is unique: " + unique);
		return ts;
	}

	public ArrayList<T> uniqueBy(String message, BiPredicate<T, T> matcher) {
		ArrayList<T> repeated = new ArrayList<>();
		for (int i = 0; i < data.size(); i++) {
			for (int j = 0; j < data.size(); j++) {
				if (j != i) {
					T thiz = data.get(i);
					T that = data.get(j);
					if (matcher.test(thiz, that)) {
						repeated.add(thiz);
					}
				}
			}
		}
		boolean unique = repeated.isEmpty();
		System.out.println(message + " is unique: " + unique);
		return repeated;
	}

	public void forEach(Consumer<T> consumer) {
		data.stream().forEach(consumer::accept);
		return;
	}

	public HashSet<T> symmetricDiference(ListData<T> other) {
		HashSet<T> difference = new HashSet<>();
		difference.addAll(data);
		difference.addAll(other.data);
		difference.removeAll(intersection(other).get());
		return difference;
	}

	public ListData<T> intersection(ListData<T> other) {
		Set<T> intersection = new HashSet<T>(data);
		intersection.retainAll(other.data);
		return ListData.of(new ArrayList<>(intersection));
	}


	public ListData<T> removeDuplicates() {
		HashSet<T> ts = new HashSet<>();
		ts.addAll(data);
		return ListData.of(new ArrayList<>(ts));
	}

	public boolean has(T item) {
		return data.contains(item);
	}

	public BigDecimal sumProperty(Function<T, BigDecimal> mapper) {
		return data.stream().map( mapper::apply).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
	}

	public ListData<T> findDuplicates() {
		HashSet<T> uniqueValues = new HashSet<>();
		List<T> duplicates = new ArrayList<>();
		for (T item: data){
			boolean added = uniqueValues.add(item);
			if (!added){
				duplicates.add(item);
			}
		}
		return ListData.of(duplicates);
	}
}


