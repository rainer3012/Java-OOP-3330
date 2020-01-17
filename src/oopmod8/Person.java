package oopmod8;

public class Person extends app{

	public String firstName;
	public String lastName;
	public int age;
	public long ssn;
	public long creditCard;
	
	public Person() {
	}

	public Person(String firstName, String lastName, int age, long ssn, long creditCard) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.ssn = ssn;
		this.creditCard = creditCard;
	}

	@Override
	public String toString() {
		return firstName + " " + lastName + "\n" + age + "\n" + ssn + "\n" + creditCard + "\n";
	}

}
