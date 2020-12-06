package interfaceTest.MStream;

import interfaceTest.LStream.EagerLStream;
import interfaceTest.LStream.LStream;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

public class EagerMStream<K,V> implements MStream<K, V> {

	private HashMap<K,V> map;

	public static <A, B> MStream<A, B> of(Map<A, B> grouped) {
		return null;
	}

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

	@Override public <A> MStream remapValues(Function<V, A> remapper) {
//		Map<K, V> data = this.data;
//		Map<K, A> map = data.entrySet().stream().map(entry -> Pair.of(entry.getKey(), remapper.apply(entry.getValue())))
//				.collect(Collectors.toMap(pair -> pair.getLeft(), pair -> pair.getRight()));
//
//		return MapData.of(map);
		return null;

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




	@Override public EagerLStream<V> values(){
		return EagerLStream.of(map.values());
	}

	@Override public <ValueType> LStream<LStream<ValueType>> getBuckets(Class<ValueType> clazz){
		return null;
	}

	@Override public EagerLStream<K> keys(){
		return  EagerLStream.of(map.keySet());
	}

	 public HashMap<K, V> get(){
		return map;
	}


}
