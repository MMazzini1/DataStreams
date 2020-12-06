package interfaceTest.MStream;

import interfaceTest.LStream.EagerLStream;
import interfaceTest.LStream.LStream;

import java.util.HashMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

public class LazyMStream<K,V> implements MStream<K,V> {

	@Override public <A> MStream remapValues(Function<V, A> remapper) {
		return null;
	}

	@Override public MStream<K, V> merge(MStream<K, V> toMerge, BinaryOperator<V> remapping) {
		return null;
	}

	@Override public MStream<K, V> filterKeys(Predicate<K> predicate) {
		return null;
	}

	@Override public MStream<K, V> filterValues(Predicate<V> predicate) {
		return null;
	}

	@Override public LStream<V> values() {
		return null;
	}

	@Override public <ValueType> LStream<LStream<ValueType>> getBuckets(Class<ValueType> clazz) {
		return null;
	}

	@Override public LStream<K> keys() {
		return null;
	}
}
