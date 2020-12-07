package interfaceTest.MStream;

import TestModel.Adress;
import TestModel.Person;
import interfaceTest.LStream.EagerLStream;
import interfaceTest.LStream.LStream;
import interfaceTest.LStream.LazyLStream;
import interfaceTest.Pair;
import interfaceTest.Utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LazyMStream<K, V> implements MStream<K, V> {

	private Supplier<Stream<Map.Entry<K, V>>> upstream;

	public LazyMStream(Supplier<Stream<Map.Entry<K, V>>> upstream) {
		this.upstream = upstream;
	}
	//	public static <A,B> LazyMStream of(Map<A,B> map) {
	//		LazyMStream<A, B> abLazyMStream = new LazyMStream<>( map);
	//		return abLazyMStream;
	//	}

	public Map<K, V> get() {
		return upstream.get().collect(Collectors.toMap(entry -> entry.getKey(), value -> value.getValue()));
	}

	//	public <A> LazyMStream<K, A> remapValues(Function<V, A> remapper) {
	//		Map<K, V> data = this.data;
	//		Map<K, A> map = data.entrySet().stream().map(entry -> Pair.of(entry.getKey(), remapper.apply(entry.getValue())))
	//				.collect(Collectors.toMap(pair -> pair.getLeft(), pair -> pair.getRight()));
	//
	//		return MapData.of(map);
	//		return null;
	//	}

	//TODO sacar paso del collec to map
	@Override public <A> MStream<K, A> remapValues(Function<V, A> remapper) {
		return new LazyMStream<>(
				Utils.logFunction(() -> {
							//							Map<K, V> kvMap = get();
							//							List<Pair<K, A>> collect = kvMap.entrySet().stream().map(entry -> Pair.of(entry.getKey(), remapper.apply(entry.getValue())))
							//									.collect(Collectors.toList());
							//							EagerLStream<Person> value = (EagerLStream<Person> ) kvMap.entrySet().iterator().next().getValue();
							//							MStream<String, LStream<Person>> adressLStreamMStream = value.groupBy(a -> a.getAdress().getStreet());

							Stream<Map.Entry<K, A>> stream = upstream.get()
									.map(entry -> Pair.of(entry.getKey(), remapper.apply(entry.getValue())))
									.collect(Collectors.toMap(pair -> pair.getLeft(), pair -> pair.getRight()))
									.entrySet().stream();
							return stream;
						},
						"Remapping values"));

	}

	@Override public MStream<K, V> merge(MStream<K, V> toMerge, BinaryOperator<V> remapping) {
		return null;
	}

	@Override public MStream<K, V> filterKeys(Predicate<K> predicate) {
		return new LazyMStream<>(Utils.logFunction(() -> upstream.get().filter(entry -> predicate.test(entry.getKey())),
				"Filtering keys"));
	}

	@Override public MStream<K, V> filterValues(Predicate<V> predicate) {

		return new LazyMStream<>(Utils.logFunction(() -> upstream.get().filter(entry -> predicate.test(entry.getValue())),
				"Filtering values"));



	}

	@Override public LStream<V> getValues() {
		return new LazyLStream<>(Utils.logFunction(() -> upstream.get().map(entry -> entry.getValue()),
				"Getting values"));
	}

	@Override public LStream<K> getKeys() {
		return new LazyLStream<>(Utils.logFunction(() -> upstream.get().map(entry -> entry.getKey()),
				"Getting keys"));
	}


	@Override public <ValueType> LStream<LStream<ValueType>> getBuckets(Class<ValueType> clazz) {
		return recursiveFlattening(clazz);
	}

	public <ValueType> LStream<LStream<ValueType>> getBuckets() {
		return recursiveFlattening();
	}

	private <Value> LStream<LStream<Value>> recursiveFlattening() {
		return new LazyLStream<>(Utils.logFunction(() -> {
					Map<K, V> kvMap = get();
					LStream<LStream<Value>> resultHolder = EagerLStream.emptyList();
					addToLStreamAux(kvMap, resultHolder);
					return resultHolder.get().stream();
				},
				"Recursive flattening"));
	}

	private <Value> void addToLStreamAux(Map<?, ?> kvMap, LStream<LStream<Value>> result) {
		for (Object listOrMap : kvMap.values()) {
			if (listOrMap instanceof EagerMStream) {
				addToLStreamAux(((EagerMStream) listOrMap).get(), result);
			} else {
				((EagerLStream) result).add(listOrMap);
			}
		}
	}




	private <Value> LStream<LStream<Value>> recursiveFlattening(Class<Value> valueClass) {
		return new LazyLStream<>(Utils.logFunction(() -> {
					Map<K, V> kvMap = get();
					LStream<LStream<Value>> resultHolder = EagerLStream.emptyList();
					addToLStreamAux(kvMap, resultHolder, valueClass);
					return resultHolder.get().stream();
				},
				"Recursive flattening"));
	}

	//todo..podria devolver una lista comun, es solo para stremear
	//todo algun tipo de check de tipo..
	private <Value> void addToLStreamAux(Map<?, ?> kvMap, LStream<LStream<Value>> result, Class<Value> valueClass) {
		for (Object listOrMap : kvMap.values()) {
			if (listOrMap instanceof EagerMStream) {
				addToLStreamAux(((EagerMStream) listOrMap).get(), result, valueClass);
			} else {
//				if (!listOrMap.getClass().isAssignableFrom(valueClass) && !listOrMap.getClass().isAssignableFrom(EagerLStream.class)) {
//					throw new IllegalStateException("Error al obtener buckets. Se esperaba una clase distinta de: " + valueClass.getName());
//				}
				((EagerLStream) result).add(listOrMap);
			}
		}
	}


}
