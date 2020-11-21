package Data;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//Todo para nested maps, probablemente MapData deberia ser un tipo anidado
public class MapData<K,V> {

//	private class ValueDatType<J>{
//		private Class<J> data;
//	}




	private Map<K,V> data;

	private MapData(Map<K, V> data) {
		this.data = data;
	}

	public static <K,V> MapData<K, V> of(Map<K,V> map){
		return new MapData<K,V>(map);
	}

	public MapData<K, V> merge(MapData<K,V> toMerge, BinaryOperator<V> remapping){
		Stream<Map.Entry<K, V>> combinedStream = Stream.concat(data.entrySet().stream(), toMerge.data.entrySet().stream());
		Map<K, V> collect = combinedStream.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, remapping));
		return MapData.of(collect);

	}


	//devuelve lista de listas
	public <Value> ListData<List<Value>> getFlattenedValues(Class<Value> valueClass) {
		List<List<Value>> values = (List<List<Value>>) recursiveFlat(valueClass);
		return ListData.of(values);
	}


	private <Value> List<List<Value>> recursiveFlat(Class<Value> valueClass) {
		List<List<Value>> resultHolder = new ArrayList<>();
		addToListAux(data, resultHolder, valueClass);
		return resultHolder;
	}

	//recursively add to list
	private <Value> void addToListAux(Map<?, ?> map, List<List<Value>> list, Class<Value> valueClass) {
		for (Object o:map.values()) {
			if (o instanceof Map) {
				addToListAux((Map<Object, Object>)o, list, valueClass);
			} else {
				list.add((List<Value>) o);
			}
		}
	}



	public Map<K, V> get() {
		if (data == null){
			throw new NullPointerException("Get de listData devuelve null");
		}
		return data;
	}

	public ListData<V> getValuesAsList() {
		List<V> collect = data.values().stream().collect(Collectors.toList());
		return ListData.of(collect);
	}




	//devuelve el mapa asociado a una key.
//	public MapData<V,?> getKeyMapValues(K k){
//		V v = data.get(k);
//		if (v instanceof Map){
//			Map<V,?> map = (Map<V,?>)  v;
//			return MapData.of(map);
//		}
//		else {
//			return this; //not a map
//		}
//	}

	public <A> MapData<K, A> remapValues(Function<V,A> remapper) {
		Map<K, V> data = this.data;
		Map<K, A> map = data.entrySet().stream().map(entry -> Pair.of(entry.getKey(), remapper.apply(entry.getValue())))
				.collect(Collectors.toMap(pair -> pair.getLeft(), pair -> pair.getRight()));

		return MapData.of(map);

	}

	public MapData<K,V> filterKeys(Predicate<K> predicate){
		Map<K, V> filteredKeys = data.entrySet().stream().filter(entry -> predicate.test(entry.getKey())).collect(Collectors.toMap(
				entry -> entry.getKey(), entry -> entry.getValue()));
		return MapData.of(filteredKeys);
	}


	//solo si no es un mapa
	public ListData<V> toList(Predicate<K> predicate){
		List<V> collect = data.values().stream().collect(Collectors.toList());
		return ListData.of(collect);
	}

	//
//	public ListData<V> toListRec(Predicate<K> predicate){
//		List<V> collect = data.values().stream().collect(Collectors.toList());
//		Class<T> clazz = (Class<T>) ((ParameterizedType) getClass()
//				.getGenericSuperclass()).getActualTypeArguments()[0];
//		if (clazz.isAssignableFrom(Map.class)){
//
//		}
//
//		return ListData.of(collect);
//	}

	public ListData<K> getKeys() {
		return ListData.of(data.keySet().stream().collect(Collectors.toList()));
	}




	private static class Helper<T> {
		private final ListData<T> foo;
		private T t;
		private Helper(ListData<T> foo) {
			this.foo = foo;
		}
		static <T> Helper<T> create(ListData<T> foo) {
			return new Helper<T>(foo);
		}
		public void execute(T t){}
	}

	private final Helper<?> helper = new Helper<>(ListData.empty(Class.class));
	private <A> void execute(A a){
//		helper.execute(a);

	}


	/** Tira excepcion si el mapData no tiene una lista como Valor (V), o si la funcion pasada opera sobre un tipo distinto al contenido en la lista (
	 * o sea, el tipo de ListValue debe ser === al tipo de V).
	 * @return*/
	//TODO deberia tener tipado
	public <A, ListValue> MapData<K, List<A>> remapEachValueTypeSafe(Function<ListValue , A> remapper) {
		Map<K, V> data = this.data;
		Map<K, List<A>> remapped = data.entrySet().stream().map(entry -> Pair.of(entry.getKey(),
				((List<ListValue>) entry.getValue()).stream().map(listValue -> remapper.apply(listValue)).collect(Collectors.toList())))
				.collect(Collectors.toMap(pair -> pair.getLeft(), pair -> pair.getRight()));
		return MapData.of(remapped);
	}



	/** Tira excepcion si el mapData no tiene una lista como Valor (V), o si la funcion pasada opera sobre un tipo distinto al contenido en la lista (
	 * o sea, el tipo de ListValue debe ser === al tipo de V).
	 * @return*/
	//TODO deberia tener tipado
	public <A, ListValue> MapData<K, List<A>> remapEachValue(Function<ListValue , A> remapper) {
		Map<K, V> data = this.data;
		Map<K, List<A>> remapped = data.entrySet().stream().map(entry -> Pair.of(entry.getKey(),
				((List<ListValue>) entry.getValue()).stream().map(listValue -> remapper.apply(listValue)).collect(Collectors.toList())))
				.collect(Collectors.toMap(pair -> pair.getLeft(), pair -> pair.getRight()));
		return MapData.of(remapped);
	}


	/** Tira excepcion si el mapData no tiene una lista como Valor (V), o si la funcion pasada opera sobre un tipo distinto al contenido en la lista (
	 * o sea, el tipo de ListValue debe ser === al tipo de V).
	 * @return*/
	public <A, ListValue> MapData<K, Optional<ListValue>> reduceValues(BinaryOperator<ListValue> reducer) {
		Map<K, V> data = this.data;
		Map<K, Optional<ListValue>> reduced = data.entrySet().stream().map(entry -> Pair.of(entry.getKey(),
				((List<ListValue>) entry.getValue()).stream().reduce(reducer::apply)))
				.collect(Collectors.toMap(pair -> pair.getLeft(), pair -> pair.getRight()));
		return MapData.of(reduced);
	}






}
