package TestModel;

import java.util.List;

public class Person {

	private Integer age;
	private String name;
	private Long id;
	private Adress adress;

	public List list = null;

	public Person() {
	}

	public Person(Integer age, String name, Long id, Adress adress) {
		this.age = age;
		this.name = name;
		this.id = id;
		this.adress = adress;
	}

	public Person(Long id) {
		this.id = id;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Adress getAdress() {
		return adress;
	}

	public void setAdress(Adress adress) {
		this.adress = adress;
	}

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Person))
			return false;

		Person person = (Person) o;

		if (getAge() != null ? !getAge().equals(person.getAge()) : person.getAge() != null)
			return false;
		if (getName() != null ? !getName().equals(person.getName()) : person.getName() != null)
			return false;
		if (getId() != null ? !getId().equals(person.getId()) : person.getId() != null)
			return false;
		if (getAdress() != null ? !getAdress().equals(person.getAdress()) : person.getAdress() != null)
			return false;
		return list != null ? list.equals(person.list) : person.list == null;
	}

	@Override public int hashCode() {
		int result = getAge() != null ? getAge().hashCode() : 0;
		result = 31 * result + (getName() != null ? getName().hashCode() : 0);
		result = 31 * result + (getId() != null ? getId().hashCode() : 0);
		result = 31 * result + (getAdress() != null ? getAdress().hashCode() : 0);
		result = 31 * result + (list != null ? list.hashCode() : 0);
		return result;
	}
}
