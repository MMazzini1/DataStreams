package TestModel;

public class Person {

	private Integer age;
	private String name;
	private Long id;
	private Adress adress;



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
}