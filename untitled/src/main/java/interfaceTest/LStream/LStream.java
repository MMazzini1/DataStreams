package interfaceTest.LStream;

import interfaceTest.MStream.EagerMStream;
import interfaceTest.MStream.MStream;
import sun.applet.resources.MsgAppletViewer;

import java.util.*;
import java.util.function.*;

/**
 * TODO, solo una interface?
 *
 *
 * @param <T>
 */
public interface LStream<T> {

	public static <A> LStream<A> of(Collection<A> collection, boolean lazy){
		return lazy ? LazyLStream.of(collection) : EagerLStream.of(collection);
	}

	 static LStream empty(){
		 return new LazyLStream();
};

	<K> MStream<K, LStream<T>> groupBy(Function<T, K> classifier);

	<K1,K2> MStream<K1, MStream<K2, LStream<T>>> groupBy(Function<T, K1> classifier1, Function<T, K2> classifier2);

	LStream<T> findDuplicates();

	LStream<T> sort(Function<? super T, ? extends Comparable>... comparators);

	LStream<T> sort(Comparator<T>... comparators);

	// Filter devuelve lista o elemento unico
	LStream<T> filter(Predicate<T> predicate);

	Collection<T> get();


	List<T> getAsList();

//	public static class CollectorsHelper<R,T>{
//
//		private R r;
//		private T t;
//
//		public CollectorsHelper(R r, T t) {
//			this.r = r;
//			this.t = t;
//		}
//
//		public static toList(){
//
//
//		}
//
//	}



//	public default <R> List<T> getAs(CollectorsHelper<R,T> coll){
//		CollectorsHelper<ArrayList<T>, CollectorsHelper<R, T>> arrayListCollectorsHelperCollectorsHelper = new CollectorsHelper<>(new
//				ArrayList<T>(), coll);
//
//	}


	<B> LStream<B> map(Function<T, B> mapper);

	Optional<T> reduce(BinaryOperator<T> reducer);

	boolean unique();

	ArrayList<T> uniqueBy(BiPredicate<T, T> matcher);

	void forEach(Consumer<T> consumer);

	HashSet<T> symmetricDiference(LStream<T> other);
//
//	AbstractListStream<T, ListType> intersection(ListType other);

	LStream<T> removeDuplicates();

	boolean has(T item);


}
