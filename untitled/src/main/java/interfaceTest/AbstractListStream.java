package interfaceTest;

import java.util.*;
import java.util.function.*;

/**
 * TODO, solo una interface?
 *
 *
 * @param <T>
 */
public interface AbstractListStream<T, ListType> {



	<K> MapStream<K, ? extends AbstractListStream<T, ListType>> groupBy(Function<T, K> classifier);

	<K1,K2> MapStream<K1, ? extends MapStream<K2, ? extends AbstractListStream<T, ListType>>> groupBy(Function<T, K1> classifier1, Function<T, K2> classifier2);

	AbstractListStream<T, ListType> findDuplicates();

	AbstractListStream<T, ListType> sort(Function<? super T, ? extends Comparable>... comparators);

	AbstractListStream<T, ListType> sort(Comparator<T>... comparators);

	// Filter devuelve lista o elemento unico
	AbstractListStream<T, ListType> filter(Predicate<T> predicate);

	Collection<T> get();

	<B> AbstractListStream<B, ?> map(Function<T, B> mapper);

	Optional<T> reduce(BinaryOperator<T> reducer);

	boolean unique();

	ArrayList<T> uniqueBy(BiPredicate<T, T> matcher);

	void forEach(Consumer<T> consumer);

	HashSet<T> symmetricDiference(ListType other);

	AbstractListStream<T, ListType> intersection(ListType other);

	AbstractListStream<T, ListType> removeDuplicates();

	boolean has(T item);
}
