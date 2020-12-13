package interfaceTest.LStream;

import interfaceTest.MStream.MStream;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * TODO, solo una interface?
 *
 * @param <T>
 */
public interface LStream<T> {

//	public static class ASD{
//
//	}
//
//	//TODO clase abstracta intermedia
//	public static <A> LazyLStream.CollectorHelper<A, List<A>> list(){
//		return new LazyLStream.CollectorHelper<A, List<A>>() {
//			@Override public Collector<A, ?, List<A>> getCollector() {
//				return Collectors.toList();
//			}
//		};
//	};
//
//
//
//	public LazyLStream.CollectorHelper<T, Set<T>> set();

	public static <A> LStream<A> of(Collection<A> collection, boolean lazy) {
		return lazy ? LazyLStream.of(collection) : EagerLStream.of(collection);
	}

	public static <A> LStream<A> of(Collection<A> collection) {
		return of(collection, true);
	}


	Collection<T> get();

	List<T> getList();
	Set<T> getSet();
	//asSet



	static LStream empty() {
		return EagerLStream.emptyList();
	}

	<K> MStream<K, LStream<T>> groupBy(Function<T, K> classifier);

	<K1, K2> MStream<K1, MStream<K2, LStream<T>>> groupBy(Function<T, K1> classifier1, Function<T, K2> classifier2);

	<K1, K2,K3> MStream<K1, MStream<K2, MStream<K3, LStream<T>>>> groupBy(Function<T, K1> classifier1, Function<T, K2> classifier2, Function<T, K3> classifier3);

	LStream<LStream<T>> groupBy(Function<T, ?>... getters);

	LStream<T> getUniqueValuesBy(Function<T, ?>... getters);

	LStream<T> getUniqueValues();

	LStream<T> keepDuplicateValues();

	LStream<T> getDuplicateValueInstances();

	LStream<T> removeDuplicates();

	LStream<Boolean> hasUniqueValuesBy(Function<T, ?>... getters);

	MStream<T, Long> getCountMapBy(Function<T, ?>... getters);

	MStream<T, Long> getCountMap();

	LStream<T> sort(Function<? super T, ? extends Comparable>... comparators);

	LStream<T> sort(Comparator<T>... comparators);

	// Filter devuelve lista o elemento unico
	LStream<T> filter(Predicate<T> predicate);

	<B> LStream<B> map(Function<T, B> mapper);

	LStream<Optional<T>> reduce(BinaryOperator<T> reducer);

	LStream<Boolean> hasUniqueValues();

	LStream<T> addAll(LStream<T> other);

	LStream<T> symmetricDiference(LStream<T> other);

	LStream<T> distinct();

	LStream<Boolean> has(T item);

	LStream<T> first();


}
