package Data;

import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

public interface ListDataBase<T> {
	ListData filter(Predicate<T> predicate);

	<B> ListData map(Function<T, B> mapper);

	ReducedData reduce(BinaryOperator<T> reducer);

	ListData sort(Function<? super T, ? extends Comparable>... comparators);
}
