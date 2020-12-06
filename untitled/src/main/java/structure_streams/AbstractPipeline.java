package structure_streams;



public class AbstractPipeline<IN, E_OUT, S extends BaseStream<E_OUT, S>>
		extends PipelineHelper<E_OUT> implements BaseStream<E_OUT, S> {

	public AbstractPipeline(List<?> list) {

	}
}
