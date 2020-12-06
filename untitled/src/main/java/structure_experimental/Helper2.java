package structure_experimental;

public  interface Helper2<T,Q>{

	Helper2<Helper<T>, Q> wrap();

	T unwrap(Class<Q> original);
}
