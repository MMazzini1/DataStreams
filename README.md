# DataStreams
Libreria para facilitar transformaciones de listas y mapas. 
Híbrido entre collections y stream. Inspirtado en la programación funcional y los streams de java 8.


(En desarrollo)

La librería cuenta de dos abstracciones:
LStream (interface) (representa una colección)
MStream (interface) (representa un mapa)

Ambas abstracciones ofrecen operaciones de alto nivel, comúnes al querer manipular y transformas datos contenidos en listas y mapas. 

Se ofrecen dos implementaciones para cada una, una lazy y otra eager.

Las implementación lazy no realizan ningún calculo hasta que no se realice un "get", el cuál dispara todas las operaciones. Esto funciona utilizando un Supplier<Stream<T>> en el que se van concatenando todas las operaciones. 

La implementación eager realiza las operaciones en el momento, y wrappea una colección o un mapa sobre el que opera.

La librería permite reusar fácilmente una misma serie de operaciones, a diferencia de lo que sucede con los streams comunes, los cuales no pueden operarse más de una vez sin recibir un "java.lang.IllegalStateException: stream has already been operated upon or closed". 

También se cuenta con varias operaciones que permiten transformas LStream en MStream (por ejemplo, groupBy) o viceversa (por ejemplo, getLists), las cuales pueden resultar muy utiles en ciertos casos.
  
 
API LSTREAM

	T getOne();

	LStream<T> intersection(LStream<T> other);

	Collection<T> get();

	List<T> getAsList();

	Set<T> getSet();

	<K> MStream<K, T> groupByAsOne(Function<T, K> classifier);

	<K> MStream<K, LStream<T>> groupBy(Function<T, K> classifier);

	<K1, K2> MStream<K1, MStream<K2, T>> groupByAsOne(Function<T, K1> classifier1, Function<T, K2> classifier2);

	<K1, K2> MStream<K1, MStream<K2, LStream<T>>> groupBy(Function<T, K1> classifier1, Function<T, K2> classifier2);

	<K1, K2,K3> MStream<K1, MStream<K2, MStream<K3, T>>> groupByAsOne(Function<T, K1> classifier1, Function<T, K2> classifier2, Function<T, K3> classifier3);

	<K1, K2,K3> MStream<K1, MStream<K2, MStream<K3, LStream<T>>>> groupBy(Function<T, K1> classifier1, Function<T, K2> classifier2, Function<T, K3> classifier3);

	LStream<LStream<T>> groupBy(Function<T, ?>... getters);

	LStream<T> getUniqueValuesBy(Function<T, ?>... getters);

	LStream<T> getUniqueValues();

	LStream<T> keepDuplicateValues();

	LStream<T> getDuplicateValueInstances();

	LStream<T> removeDuplicates();

	LStream<Boolean> hasUniqueValuesBy(Function<T, ?>... getters);

	LStream<Long> countAppearancesBy(T item, BiPredicate<T, T> matcher);

	MStream<T, Long> getCountMapBy(Function<T, ?>... getters);

	MStream<T, Long> getCountMap();

	LStream<T> sort(Function<? super T, ? extends Comparable>... comparators);

	LStream<T> sort(Comparator<T>... comparators);

	LStream<T> filter(Predicate<T> predicate);

	<B> LStream<B> map(Function<T, B> mapper);

	LStream<Optional<T>> reduce(BinaryOperator<T> reducer);

	LStream<T> reduceUnwrapped(BinaryOperator<T> reducer);

	LStream<Boolean> hasUniqueValues();

	LStream<T> addAll(LStream<T> other);

	LStream<T> SymmetricDifference(LStream<T> other);

	LStream<T> distinct();

	LStream<Boolean> has(T item);

	LStream<T> first();

	LStream<Integer> size();

	void add(T val);

	LStream<Long> countAppearances(T item);
  

 
API MSTREAM

	Map<K,V> get();

	V get(K key);

	<A> MStream<K,A> remapValues(Function<V, A> remapper);

	MStream<K,V> merge(MStream<K,V> toMerge, BinaryOperator<V> remapping);

	MStream<K, LStream<V>> mergeAsLists(MStream<K, V> toMerge);

	<VV, A> MStream<K, A> leftJoin(MStream<K, VV> other, BiFunction<V, VV, A> remapping, Function<V,A> defaultMapping);

	<VV, A> MStream<K, A> innerJoin(MStream<K, VV> other, BiFunction<V, VV, A> remapping);

	MStream<K,V> filterKeys(Predicate<K> predicate);

	MStream<K,V> filterValues(Predicate<V> predicate);

	LStream<V> getValues();

	<ValueType> LStream<LStream<ValueType>> getLists(Class<ValueType> clazz);

	LStream<K> getKeys();





