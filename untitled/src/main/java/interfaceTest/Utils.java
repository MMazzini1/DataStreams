package interfaceTest;



import interfaceTest.LStream.LStream;
import interfaceTest.MStream.MStream;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {

	public static <A, B> Map<B, List<A>> groupBy(Collection<A> collection, Function<A, B> classifier, Collector<? super A, Object, List<A>> downstream) {
		return collection.stream().collect(Collectors.groupingBy(classifier::apply, downstream));
	}




	public static <A, B> Map<B, LStream<A>> groupBy(Collection<A> collection, Function<A, B> classifier) {
		return null;
//		groupBy(collection, classifier, Collectors.toList()).var

	}


	public static <A, B, C> Map<B, Map<C, List<A>>> groupBy(List<A> list, Function<A, B> classifier1, Function<A, C> classifier2) {
		return list.stream().collect(Collectors.groupingBy(classifier1::apply,
				Collectors.groupingBy(classifier2)));
	}

	public static <K, T> MStream<K, LStream<T>> groupBy(Supplier<Stream<T>> upstream, Function<T, K> classifier) {
		return null;
	}
}
