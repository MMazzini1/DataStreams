package Data;


import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

//clase utilitaria para trabajar con streams
public class StreamUtils {

	//	public <T,R extends Collection> R filterAndList(R collection, Predicate<T> predicate) {
	//		return filterAndCollect(collection, predicate, Collectors.toList());
	//	}

	public static <T, R extends Collection<T>> List<T> filterAndCollect(R collection, Predicate<T> predicate) {
		return filter(collection, predicate).collect(Collectors.toList());
	}

	public static <T, R> List<R> map(Collection<T> collection, Function<T, R> mapper) {
		return collection.stream().map(el -> mapper.apply(el)).collect(Collectors.toList());
	}

	public static <A, B, C> Stream<C> zipped(List<A> lista, List<B> listb, BiFunction<A, B, C> zipper) {
		int shortestLength = Math.min(lista.size(), listb.size());
		return IntStream.range(0, shortestLength).mapToObj(i -> {
			return zipper.apply(lista.get(i), listb.get(i));
		});
	}

	public static <A, B> void zippedForEach(List<A> lista, List<B> listb, BiConsumer<A, B> zipper) {
		int shortestLength = Math.min(lista.size(), listb.size());
		IntStream.range(0, shortestLength).forEach(i -> {
			zipper.accept(lista.get(i), listb.get(i));
		});
	}

	public static <T> List<T> flatten(Collection<? extends Collection<T>> listOfLists) {
		return listOfLists.stream().flatMap(list -> list.stream()).collect(Collectors.toList());

	}

	public static <T> Predicate<T> positive(Function<T, BigDecimal> f) {
		Predicate<T> beanPredicate = bean -> f.apply(bean).compareTo(BigDecimal.ZERO) > 0;
		return beanPredicate;
	}

	public static <T> Predicate<T> negative(Function<T, BigDecimal> f) {
		Predicate<T> beanPredicate = bean -> f.apply(bean).compareTo(BigDecimal.ZERO) < 0;
		return beanPredicate;
	}

	public static <A, B> Map<B, List<A>> groupBy(List<A> list, Function<A, B> classifier, Collector<? super A, Object, List<A>> downstream) {
		return list.stream().collect(Collectors.groupingBy(classifier::apply, downstream));
	}

	public static <A, B> Map<B, List<A>> groupBy(List<A> list, Function<A, B> classifier) {
		return list.stream().collect(Collectors.groupingBy(classifier::apply));
	}

	public static <A, B, C> Map<B, Map<C, List<A>>> groupBy(List<A> list, Function<A, B> classifier1, Function<A, C> classifier2) {
		return list.stream().collect(Collectors.groupingBy(classifier1::apply,
				Collectors.groupingBy(classifier2)));
	}

	//Implementacion vacia de mergeFunction. Cuando sabes que NO se va a llamar porque NO va a haber colision de keys.
	public static <T> BinaryOperator<T> empty() {
		BinaryOperator<T> any = (a, b) -> null;
		return any;
	}

	//Implementacion de mergeFunction cuando hay colision y no te importa cual de los dos values queda.
	public static <T> BinaryOperator<T> any() {
		BinaryOperator<T> any = (a, b) -> a;
		return any;
	}

	public StreamUtils() {
	}

	public static <T> List<T> asList(T line) {
		ArrayList<T> list = new ArrayList<>();
		list.add(line);
		return list;
	}

	/**
	 * Creacion de un map a partir de un keymapper y un valuemapper
	 * throws java.lang.IllegalStateException si hay duplicate keys
	 */
	public static <K, V, R> Map<K, V> toMap(Collection<R> collection, Function<R, K> keyMapper, Function<R, V> valueMapper) {
		return collection.stream().collect(Collectors.toMap(keyMapper, valueMapper));
	}

	public static <K, V, R> Map<K, V> toMap(Collection<R> collection, Function<R, K> keyMapper, Function<R, V> valueMapper, BinaryOperator<V> mergeFunction) {
		return collection.stream().collect(Collectors.toMap(keyMapper, valueMapper, mergeFunction));
	}

	/**
	 * modifica el mapa destiny pasado por parametro
	 * mergea un mapa de origen (mergeable) con uno de destino (destiny), ambos con keys del mismo tipo,
	 * pero el mapa de destiny con otro tipo de value
	 * Debe proveerse la funcion valueFunction que mapea un value de tipo K (mergeable) a tipo R (destiny)
	 * Debe proveerse la mergeFunction que mergea dos values de tipo R cuando hay colisiones.
	 * //todo
	 * ESTA API la podria separar
	 * en put, merge, y putAndMerge
	 * Tambien podria aprovechar la interface mergeBean
	 */
	public static <T, R, K> void mergeOne(Map<T, R> destiny, Map<T, K> mergeable, Function<K, R> valueFunction, BiFunction<R, R, R> mergeFunction) {
		mergeable.entrySet().stream().forEach(entry ->
				destiny.merge(entry.getKey(), valueFunction.apply(entry.getValue()), (a, b) -> mergeFunction.apply(a, b)));

	}

	public static <T> List<T> filterAndList(Collection<T> collection, Predicate<T> predicate) {
		return filterAndCollect(collection, predicate, Collectors.toList());
	}

	public static <T, R extends Collection> R filterAndCollect(Collection<T> collection, Predicate<T> predicate, Collector<T, ?, R> collector) {
		return filter(collection, predicate).collect(collector);
	}

	public static <T> Stream<T> filter(Collection<T> collection, Predicate<T> predicate) {
		return collection.stream().filter(predicate);
	}

	public static <T> BigDecimal mapAndSum(Collection<T> collection, Function<T, BigDecimal> mapper) {
		return collection.stream().map(el -> mapper.apply(el)).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
	}

	/**
	 * Mergea dos mapas devolviendo un mapa del mismo tipo,
	 * Cuando una key está repetida se aplica la remapping function que mergea los valores.
	 * <p>
	 * Ejemplo: map1<District,BigDecimal>, map2<District,BigDecimal>, (a,b) -> a.add(b)
	 *
	 * @return
	 */
	public static <T, R> Map<T, R> mergeTwoMaps(Map<T, R> mapOne, Map<T, R> mapTwo, BiFunction<R, R, R> remapping) {

		Map<T, R> result = new HashMap<>();

		mergeOneMap(mapOne, remapping, result);
		mergeOneMap(mapTwo, remapping, result);

		return result;

	}

	private static <T, R> void mergeOneMap(Map<T, R> mapOne, BiFunction<R, R, R> remapping, Map<T, R> result) {
		mapOne.entrySet().stream().forEach(entry -> result.merge(entry.getKey(),
				entry.getValue(), (prev, curr) -> remapping.apply(prev, curr)));
	}



	public static BigDecimal[] sumBigDecimalArrays(BigDecimal[] arr1, BigDecimal[] arr2) {
		if (arr1.length != arr2.length) {
			throw new IllegalStateException("Los array pasados deben tener el mismo tamaño");
		}

		BigDecimal[] result = new BigDecimal[arr1.length];
		for (int i = 0; i < arr1.length; i++) {
			BigDecimal one = arr1[i];
			BigDecimal two = arr2[i];
			result[i] = one.add(two);
		}
		return result;

	}
}