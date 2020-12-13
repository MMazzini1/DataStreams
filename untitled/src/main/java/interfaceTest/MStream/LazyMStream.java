package interfaceTest.MStream;

import interfaceTest.LStream.EagerLStream;
import interfaceTest.LStream.LStream;
import interfaceTest.LStream.LazyLStream;
import interfaceTest.Pair;
import interfaceTest.StreamUtils;
import interfaceTest.Utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.*;
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
		return upstream.get().collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));
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

	//https://stackoverflow.com/questions/43138164/java-8-streams-grouping-into-single-value
	@Override public MStream<K, V> merge(MStream<K, V> toMerge, BinaryOperator<V> remapping) {
		return new LazyMStream<>(Utils.logFunction(() ->
				Stream.concat(this.upstream.get(), getStream(toMerge))
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, remapping)).
						entrySet().stream(), "merge")
		);
	}

	@Override public MStream<K, LStream<V>> mergeAsLists(MStream<K, V> toMerge) {
		LazyMStream<K, LStream<V>> merge = new LazyMStream<>(Utils.logFunction(() ->
				Stream.concat(this.upstream.get(), getStream(toMerge))
						.collect(Collectors.toMap(entry -> entry.getKey(),
								entry -> EagerLStream.of(entry.getValue()), (l1, l2)
										-> l1)).
						entrySet().stream(), "merge as lists")
		);
		return merge;
	}

	private <KK, VV> Stream<Map.Entry<KK, VV>> getStream(MStream<KK, VV> other) {
		if (other instanceof LazyMStream) {
			LazyMStream<KK, VV> otherLazy = (LazyMStream<KK, VV>) other;
			return otherLazy.upstream.get();
		} else {
			EagerMStream<KK, VV> otherEager = (EagerMStream<KK, VV>) other;
			return otherEager.get().entrySet().stream();
		}
	}




	//right Join, usar leftJoin al reves
	//outer Join, hacer 2 lefts joins partiendo de las dos listas
	@Override public <VV, A> MStream<K, A> leftJoin(MStream<K, VV> other, BiFunction<V, VV, A> remapping, Function<V,A> defaultMapping) {
		return new LazyMStream<>(Utils.logFunction(() ->
				{
					Map<K, V> thisMap = get();
					Map<K, VV> otherMap = other.get();
					Set<Map.Entry<K, V>> entries = thisMap.entrySet();
					HashMap<K, A> result = new HashMap<>();
					for (Map.Entry<K, V> thisEntry : entries) {
						VV vv = otherMap.get(thisEntry.getKey());
						if (vv != null) {
							result.put( thisEntry.getKey(), remapping.apply(thisEntry.getValue(), vv));
						} else {
							result.put(thisEntry.getKey(), defaultMapping.apply(thisEntry.getValue()));
						}
					}
					return result.entrySet().stream();

				}
				,
				"Inner join"));
	}


	@Override public <VV, A> MStream<K, A> innerJoin(MStream<K, VV> other, BiFunction<V, VV, A> remapping) {
		return new LazyMStream<>(Utils.logFunction(() ->
				{
					Map<K, V> thisMap = get();
					Map<K, VV> otherMap = other.get();
					Set<Map.Entry<K, V>> entries = thisMap.entrySet();
					HashMap<K, A> result = new HashMap<>();
					for (Map.Entry<K, V> thisEntry : entries) {
						VV vv = otherMap.get(thisEntry.getKey());
						if (vv != null) {
							result.put( thisEntry.getKey(), remapping.apply(thisEntry.getValue(), vv));
						}
					}
					return result.entrySet().stream();

				}
				,
				"Inner join"));
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
