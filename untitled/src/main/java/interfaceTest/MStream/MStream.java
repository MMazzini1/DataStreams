package interfaceTest.MStream;

import interfaceTest.LStream.EagerLStream;
import interfaceTest.LStream.LStream;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

public interface MStream<K, V> {

	Map<K,V> get();

	<A> MStream<K,A> remapValues(Function<V, A> remapper);

	MStream<K,V> merge(MStream<K,V> toMerge, BinaryOperator<V> remapping);

	MStream<K,V> filterKeys(Predicate<K> predicate);

	MStream<K,V> filterValues(Predicate<V> predicate);

	LStream<V> getValues();

	<ValueType> LStream<LStream<ValueType>> getBuckets(Class<ValueType> clazz);

	LStream<K> getKeys();


}
