package interfaceTest.LStream;

import interfaceTest.LStream.EagerLStream;
import interfaceTest.LStream.LStream;
import interfaceTest.LStream.LazyLStream;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

//TODO para eagerLsTREAM implements Collector<T, LazyLStream<T>, LazyLStream<T>>
public class LStreamCollector<T>  {

//
//	@Override public Supplier<LazyLStream<T>> supplier() {
//		return () -> new LazyLStream<>();
//	}
//
//	@Override public BiConsumer<LazyLStream<T>, T> accumulator() {
//		return (list, val) -> {
//			list.add(val);
//		};
//	}
//
//	@Override public BinaryOperator<LazyLStream<T>> combiner() {
//		return (list, list) -> {
//
//		}
//	}
//
//	@Override public Function<LazyLStream<T>, LazyLStream<T>> finisher() {
//		return Function.identity();
//	}
//
//	@Override public Set<Characteristics> characteristics() {
//		return null;
//	}
}
