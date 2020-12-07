package interfaceTest.LStream;

import interfaceTest.LStream.EagerLStream;
import interfaceTest.LStream.LStream;
import interfaceTest.LStream.LazyLStream;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

//TODO para eagerLsTREAM implements Collector<T, LazyLStream<T>, LazyLStream<T>>
public class LStreamCollector<T>  implements Collector<T, LStream<T>, LStream<T>> {


	@Override public Supplier<LStream<T>> supplier() {
		return () -> LStream.empty();
	}

	@Override public BiConsumer<LStream<T>, T> accumulator() {
		return (list, val) -> {
			((EagerLStream<T>) list).add(val);
		};
	}

	//todo
	@Override public BinaryOperator<LStream<T>> combiner() {
		return null;
	}

	@Override public Function<LStream<T>, LStream<T>> finisher() {
		return Function.identity();
	}

	@Override public Set<Characteristics> characteristics() {
		return new HashSet<>();
	}
}
