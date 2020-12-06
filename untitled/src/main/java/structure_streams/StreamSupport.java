package structure_streams;



public class StreamSupport {

	public static <T> Stream<T> stream(List<T> list) {
		return new ReferencePipeline.Head<>(list);
	}
}
