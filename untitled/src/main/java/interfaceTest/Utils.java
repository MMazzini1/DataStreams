package interfaceTest;



import interfaceTest.LStream.LStream;
import interfaceTest.LStream.LStreamCollector;
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
		return collection.stream().collect(Collectors.groupingBy(classifier::apply, new LStreamCollector<>()) );
	}

	public static <A, B,C> Map<B, Map< C,LStream<A>>> groupBy(Collection<A> collection, Function<A, B> classifier1, Function<A, C> classifier2) {
		return collection.stream().collect(Collectors.groupingBy(classifier1::apply,
				Collectors.groupingBy(classifier2::apply, new LStreamCollector<>())));
	}


	public static <A, B, C> Map<B, Map<C, List<A>>> groupBy(List<A> list, Function<A, B> classifier1, Function<A, C> classifier2) {
		return list.stream().collect(Collectors.groupingBy(classifier1::apply,
				Collectors.groupingBy(classifier2)));
	}

	public static <K, T> MStream<K, LStream<T>> groupBy(Supplier<Stream<T>> upstream, Function<T, K> classifier) {
		return null;
	}


	public static <B> Supplier<Stream<B>> logFunction(Supplier<Stream<B>> supplier, String toPrint) {
		return () -> {
			Stream<B> stream = supplier.get();
			System.out.println(toPrint);
			return stream;
		};
	}
}
