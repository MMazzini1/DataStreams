package interfaceTest.TestModel;

public class Adress {

	private String street;
	private Integer number;
	private Integer floor;

	public Adress(String street, Integer number, Integer floor) {
		this.street = street;
		this.number = number;
		this.floor = floor;
	}

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Adress))
			return false;

		Adress adress = (Adress) o;

		if (street != null ? !street.equals(adress.street) : adress.street != null)
			return false;
		if (number != null ? !number.equals(adress.number) : adress.number != null)
			return false;
		return floor != null ? floor.equals(adress.floor) : adress.floor == null;
	}

	@Override public int hashCode() {
		int result = street != null ? street.hashCode() : 0;
		result = 31 * result + (number != null ? number.hashCode() : 0);
		result = 31 * result + (floor != null ? floor.hashCode() : 0);
		return result;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getFloor() {
		return floor;
	}

	public void setFloor(Integer floor) {
		this.floor = floor;
	}
}
