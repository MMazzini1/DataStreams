package interfaceTest.LStream;

import interfaceTest.MStream.MStream;
import interfaceTest.Utils;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LazyLStream<T> implements LStream<T> {


	private Supplier<Stream<T>> upstream;

	public LazyLStream(Supplier<Stream<T>> sourceStream) {
		this.upstream = sourceStream;
	}



	//TODO eliminar, lo estoy usando para el empty pero prob deberia ser privado
	public LazyLStream() {
	}

	public static <A> LazyLStream<A> of(Collection<A> coll){
		return new LazyLStream<A>(() -> (Stream<A>) coll.stream());
	}



	protected LazyLStream<T> addAll(LazyLStream<T> other){

		return null;
	}

	protected void add(T val){


	}




	@Override public <K> MStream<K, LStream<T>> groupBy(Function<T, K> classifier) {
		return Utils.groupBy(upstream, classifier);
	}

	@Override public <K1, K2> MStream<K1, MStream<K2, LStream<T>>> groupBy(Function<T, K1> classifier1, Function<T, K2> classifier2) {
		return null;
	}

	@Override public LStream<T> findDuplicates() {
		return null;
	}

	@Override public LStream<T> sort(Function<? super T, ? extends Comparable>... comparators) {
		return null;
	}

	@Override public LStream<T> sort(Comparator<T>... comparators) {
		return null;
	}

	@Override public LStream<T> filter(Predicate<T> predicate) {
		return new LazyLStream<>(
				logFunction(() -> upstream.get().filter(predicate::test), "Filtering"));
	}

	//TODO se debe pasar el colector
	@Override public Collection<T> get() {
		return upstream.get().collect(Collectors.toList());
	}

	@Override public List<T> getAsList() {
		return null;
	}

	@Override public <B> LStream<B> map(Function<T, B> mapper) {
		return new LazyLStream<>(logFunction(() -> upstream.get().map(mapper::apply), "Mapping"));
	}

	@Override public Optional<T> reduce(BinaryOperator<T> reducer) {
		return Optional.empty();
	}

	@Override public boolean unique() {
		return false;
	}

	@Override public ArrayList<T> uniqueBy(BiPredicate<T, T> matcher) {
		return null;
	}

	@Override public void forEach(Consumer<T> consumer) {

	}

	@Override public HashSet<T> symmetricDiference(LStream<T> other) {
		return null;
	}

	@Override public LStream<T> removeDuplicates() {
		return null;
	}

	@Override public boolean has(T item) {
		return false;
	}


	//TODO el orden queda al reves
	private <B> Supplier<Stream<B>> logFunction(Supplier<Stream<B>> supplier, String toPrint) {
		return () -> {
			Stream<B> stream = supplier.get();
			System.out.println(toPrint);
			return stream;
		};
	}


}
