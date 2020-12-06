package structure_streams;



public class ReferencePipeline<IN,OUT>  extends AbstractPipeline<IN, OUT, Stream<OUT>>
		implements Stream<OUT> {

	public ReferencePipeline(List<?> list) {
		super(list);
	}

	static class Head<IN,OUT>  extends ReferencePipeline<IN,OUT> {
		Head(List<?> list) {
			super(list);
		}
	}


}
