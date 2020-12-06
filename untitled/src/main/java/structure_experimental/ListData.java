package structure_experimental;

import TestModel.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * B = tipo
 *
 * El helper expone el tipo deseado (Type)
 * Wrapped Type es el tipo interno del ultimo ListData de la lista.
 *
 *
 * Repensar esto; porque
 *     <R> Stream<R> map(Function<? super T, ? extends R> mapper);
 *     el super y el extends
 */
public class ListData<Type, WrappedType> implements Helper<Type> {

	public static void main(String[] args) {

		List<Person> persons = new ArrayList<>();
//		Helper<Person> personList = ListData.of(persons);

		ListData<Person, Person> of = ListData.of(persons);

//		ListData<ListData<Person>, Person>> asd = null;


		/**
		 * ListData<<ListData<Person>> list;     Type = ListData<Person>  Wrapped = <Person>
		 *
		 * ListData<Person> reduced = list.reduce( (person1, person2) -> bla)      Type = <Person>  Wrapped = <Person>
		 *
		 *
		 * ListData<<ListData<<ListData<Person>>> list;     Type = ListData<<ListData<Person>>  Wrapped = <Person>
		 *
		 * ListData<<ListData<<ListData<Person>>> reduced = list.reduce( (person1, person2) -> bla)      Type = ListData<<ListData<Person>>  Wrapped = <Person>
		 *
		 *
		 */

	}

	private List<Type> data;

	private ListWrapper<Type, WrappedType> wrapper;

	static class Head<Type, WrappedType> extends ListData<Type, WrappedType>   {
		public Head(List<Type> data) {
			super(data);
		}
	}

	public ListData(List<Type> data) {
		this.data = data;
	}

//	public static <A> Helper<A> of(List<A> list){
//		return new Head<A,A>(list);
//	}

	public static <A> ListData<A,A> of(List<A> list){
				return new Head<A,A>(list);
			}

	@Override public <B> Helper<B> map(Function<Type, B> mapper){
		return null;
	}

	/**
	 * ListData<ListData<String></String>
	 * ListData<ListData<String>,String>
	 *
	 * data = ListData<String></String>
	 * @param reducer
	 * @return
	 */
//	public ListData<?, WrappedType> reduceForList(BinaryOperator<? extends WrappedType> reducer) {
//			return null;
//
//	}
//
//
//	public ListData<?, WrappedType> reduceForList(BinaryOperator<? extends WrappedType> reducer) {
//		return null;
//
//	}







}
