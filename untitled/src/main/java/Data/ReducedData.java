package Data;

import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ReducedData<T,Q> {

	private final BinaryOperator<T> reducer;
	private Supplier<Stream<T>> dataStream;
	private SourceHolder<Q> source = new SourceHolder<Q>();

	public ReducedData(Supplier<Stream<T>> dataStream,SourceHolder<Q> holder, BinaryOperator<T> reducer) {
		this.dataStream = dataStream;
		this.source = holder;
		this.reducer = reducer;
	}

	public Optional<T> evaluate(List<Q> toEval){
		source.setStreamSource(toEval);
		Optional<T> reduced = dataStream.get().reduce(reducer::apply);
		return reduced;
	}



}
