package structure_streams;


public class List<E> {

	public Stream<E> stream() {
		return StreamSupport.stream(this);
	}
}
