package Data;

import java.util.ArrayList;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

public class ListDataEager<T> implements ListDataBase<T> {

	public static <Q> ListDataEager<Q> of(ArrayList<Q> clazz) {
		return new ListDataEager<>();
	}

	@Override public ListData filter(Predicate<T> predicate) {
		return null;
	}

	@Override public <B> ListData map(Function<T, B> mapper) {
		return null;
	}

	@Override public ReducedData reduce(BinaryOperator<T> reducer) {
		return null;
	}

	@Override public ListData sort(Function<? super T, ? extends Comparable>... comparators) {
		return null;
	}
}
