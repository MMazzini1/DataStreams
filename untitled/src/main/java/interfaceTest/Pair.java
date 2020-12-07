package interfaceTest;

public class Pair<K,V> {

	public K left;
	public V right;

	public  static <K,V> Pair<K,V> of(K left, V right) {
		Pair<K,V> pair = new Pair();
		pair.left = left;
		pair.right = right;
		return pair;
	}

	public K getLeft() {
		return left;
	}

	public V getRight() {
		return right;
	}
}
