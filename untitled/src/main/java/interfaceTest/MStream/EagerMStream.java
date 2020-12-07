package interfaceTest.MStream;

import interfaceTest.LStream.EagerLStream;
import interfaceTest.LStream.LStream;
import interfaceTest.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EagerMStream<K,V> implements MStream<K, V> {

	private Map<K,V> map;

	public static <A, B> MStream<A, B> of(Map<A, B> grouped) {
		EagerMStream<A, B> kvEagerMStream =  new EagerMStream<A,B>(grouped);
		return kvEagerMStream;
	}

	public EagerMStream(Map<K, V> map) {
		this.map = map;
	}

//	public static <A,B> EagerMStream<A,B> of(Map<A,B> map){
//		EagerMStream<A,B> kvEagerMStream =  new EagerMStream<A,B>(map);
//		return kvEagerMStream;
//	}
	//
//	private final Helper<O> helper = new Helper(){
//
//	};
//
//	 public <O> void getBucketsss(O o) {
//		helper.doSomething(o).var
//	}

//	public Object  getBucketsss() {
//		Collection<V> values = this.get().values();
//		return values instanceof MapStream ? getBuckets()
//	}



//	private static class Helper<T> {
//
//		public T doSomething(T t) {
//			return null;
//		}
//
//		static <T> Helper<T> create(Class<T> foo) {
//			return new Helper<T>();
//		}
//
//	}

//	private Asd<K,V,O> asd;
//
//	public static class Asd<K,V,O> extends MapStream<K,V,O>
//		implements MapHelper<K,V> {
//
//
//	}
//
//	public <Z extends O> void getBucketss(Z o){
//
//	}

	@Override public <A> MStream<K,A> remapValues(Function<V, A> remapper) {
		Map<K, A> result = map.entrySet().stream().map(entry -> Pair.of(entry.getKey(), remapper.apply(entry.getValue())))
				.collect(Collectors.toMap(pair -> pair.getLeft(), pair -> pair.getRight()));

		EagerMStream eagerMStream = new EagerMStream(result);
		return eagerMStream;

	}

	@Override public MStream<K,V> merge(MStream<K,V> toMerge, BinaryOperator<V> remapping){
//		Stream<Map.Entry<K, V>> combinedStream = Stream.concat(data.entrySet().stream(), toMerge.data.entrySet().stream());
//		Map<K, V> collect = combinedStream.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, remapping));
//		return MapData.of(collect);
		return null;

	}


	@Override public MStream filterKeys(Predicate<K> predicate){
		return null;
	}

	@Override public MStream filterValues(Predicate<V> predicate){
		return null;
	}




	@Override public EagerLStream<V> getValues(){
		return EagerLStream.of(map.values());
	}

	@Override public <ValueType> LStream<LStream<ValueType>> getBuckets(Class<ValueType> clazz){
		return null;
	}

	@Override public EagerLStream<K> getKeys(){
		return  EagerLStream.of(map.keySet());
	}

	 public Map<K, V> get(){
		return map;
	}

//	@Override public String toString() {
//
//			return "[" +  " EagerMStream = " + map.toString() +   " ]";
//
//	}
}
