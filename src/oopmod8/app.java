// Object Oriented Programming 
// Module 8 Programming Project
// Christopher Ehrhardt
// 11/26/2019
//
// I created a program that first connects to the data base then loads a simple menu interface 
// to allow you to perform all of the required tasks in a single session. 
// Option 1 will allow you to add a person to the database (step 4 of project).
// Option 2 will search the database for a person and return Person object (step 5 and 6 of project).
// Option 3 will delete a person from the database (step 8 of project).
// Option 4 will list all persons stored in database (used in other functions).
// Option 5 will create and print out an array list of person objects (step 7 of project).
// Option 6 will quit the program.

package oopmod8;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class app {

	static Scanner scanner = new Scanner(System.in);

	public static void createNewDatabase(String fileName) {
		String url = "jdbc:sqlite:/home/chris/Dropbox/Programming/Java/db/" + fileName;

		try (Connection conn = DriverManager.getConnection(url)) {
			if (conn != null) {
				DatabaseMetaData meta = conn.getMetaData();
				System.out.println("The driver name is " + meta.getDriverName());
				System.out.println("A new database has been created.");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void connect() {
		Connection conn = null;
		try {
			// db parameters
			String url = "jdbc:sqlite:/home/chris/Dropbox/Programming/Java/db/oopmod8.db";
			// create a connection to the database
			conn = DriverManager.getConnection(url);

			System.out.println("Connection to SQLite has been established");

			String sql = "CREATE TABLE IF NOT EXISTS oopmod8 (\n" + " id integer PRIMARY KEY,\n"
					+ " firstName text NOT NULL,\n" + " lastName text NOT NULL,\n" + " age real,\n" + " ssn real,\n"
					+ " creditCard real\n" + ");";

			Statement stmt = conn.createStatement();

			// create a new table
			stmt.execute(sql);

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	public static void insertPerson(Person person) {
		Connection conn = null;

		String url = "jdbc:sqlite:/home/chris/Dropbox/Programming/Java/db/oopmod8.db";
		// create a connection to the database
		try {
			conn = DriverManager.getConnection(url);

			// insert info
			String sqlInsert = "INSERT INTO oopmod8(firstName, lastName, age, ssn, creditCard) VALUES(?,?,?,?,?)";
			PreparedStatement pstmt = conn.prepareStatement(sqlInsert);
			pstmt.setString(1, person.firstName);
			pstmt.setString(2, person.lastName);
			pstmt.setInt(3, person.age);
			pstmt.setLong(4, person.ssn);
			pstmt.setLong(5, person.creditCard);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Person selectPerson() {
		Connection conn = null;

		String url = "jdbc:sqlite:/home/chris/Dropbox/Programming/Java/db/oopmod8.db";
		String fN = "";
		String lN = "";
		int age = 0;
		long ssn = 0;
		long CC = 0;

		try {
			conn = DriverManager.getConnection(url);

			System.out.print("\nPlease enter last name to find: ");
			String lastName = scanner.nextLine();

			// get info
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM oopmod8 WHERE lastName = ?");
			statement.setString(1, lastName);
			ResultSet rs2 = statement.executeQuery();

			fN = rs2.getString("firstName");
			lN = rs2.getString("lastName");
			age = rs2.getInt("age");
			ssn = rs2.getLong("ssn");
			CC = rs2.getLong("creditCard");

		} catch (SQLException e) {
			// e.printStackTrace();
			System.out.println("Database Lookup Error");
		}
		
		Person outPerson = new Person(fN, lN, age, ssn, CC);
		return outPerson;

	}

	public static void showInfo() {
		Connection conn = null;

		String url = "jdbc:sqlite:/home/chris/Dropbox/Programming/Java/db/oopmod8.db";
		// create a connection to the database
		try {
			conn = DriverManager.getConnection(url);

			// get info
			String sqlGetInfo = "SELECT id, firstName, lastName, age, ssn, creditCard FROM oopmod8";

			Statement stmt2 = conn.createStatement();
			ResultSet rs = stmt2.executeQuery(sqlGetInfo);

			// loop through the result set
			while (rs.next()) {
				System.out.println(rs.getInt("id") + "\t" + rs.getString("firstName") + "\t" + rs.getString("lastName")
						+ "\t" + rs.getInt("age") + "\t" + rs.getLong("ssn") + "\t" + rs.getLong("creditCard"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public static ArrayList<Object> findAllPeople() {
		ArrayList<Object> List = new ArrayList<Object>();
		
		Connection conn = null;

		String url = "jdbc:sqlite:/home/chris/Dropbox/Programming/Java/db/oopmod8.db";
		// create a connection to the database
		try {
			conn = DriverManager.getConnection(url);

			// get info
			String sqlGetInfo = "SELECT id, firstName, lastName, age, ssn, creditCard FROM oopmod8";

			Statement stmt2 = conn.createStatement();
			ResultSet rs = stmt2.executeQuery(sqlGetInfo);

			// loop through the result set
			while (rs.next()) {
				Person arrayItem = new Person();
				
				arrayItem.firstName = rs.getString("firstName");
				arrayItem.lastName = rs.getString("lastName");
				arrayItem.age = rs.getInt("age");
				arrayItem.ssn = rs.getLong("ssn");
				arrayItem.creditCard = rs.getLong("creditCard");
				
				List.add(arrayItem);
								
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return List;
	};

	public static Person createPerson() {
		String firstName;
		String lastName;
		int age = 0;
		long ssn = 0;
		long creditCard = 0;

		System.out.println("\nEntering New Record");

		System.out.print("\nPlease enter first name: ");
		firstName = scanner.nextLine();

		System.out.print("\nPlease enter last name: ");
		lastName = scanner.nextLine();

		System.out.print("\nPlease enter age: ");
		age = scanner.nextInt();

		System.out.print("\nPlease enter SSN: ");
		ssn = scanner.nextLong();

		System.out.print("\nPlease enter CC#: ");
		creditCard = scanner.nextLong();
		scanner.nextLine();

		Person newPerson = new Person(firstName, lastName, age, ssn, creditCard);

		return newPerson;
	}

	public static void deletePerson() {
		Connection conn = null;

		String url = "jdbc:sqlite:/home/chris/Dropbox/Programming/Java/db/oopmod8.db";
		// create a connection to the database
		try {
			conn = DriverManager.getConnection(url);

			// get info
			String sqlGetInfo = "SELECT id, firstName, lastName, age, ssn, creditCard FROM oopmod8";

			Statement stmt2 = conn.createStatement();
			ResultSet rs = stmt2.executeQuery(sqlGetInfo);

			// loop through the result set
			while (rs.next()) {
				System.out.println(rs.getInt("id") + "\t" + rs.getString("firstName") + "\t" + rs.getString("lastName")
						+ "\t" + rs.getInt("age") + "\t" + rs.getLong("ssn") + "\t" + rs.getLong("creditCard"));
			}
			
			System.out.println("\nEnter first name of person to remove:");
			String firstName = scanner.nextLine();
			System.out.println("Enter last name of person to remove:");
			String lastName = scanner.nextLine();
			
			String sqlDelete = "DELETE FROM oopmod8 WHERE firstName = ? and lastName = ?";
			PreparedStatement delete = conn.prepareStatement(sqlDelete);
			delete.setString(1, firstName);
			delete.setString(2, lastName);
			int count = delete.executeUpdate();
			
			if (count != 0) {
				System.out.println(count+" Person Deleted");
			}else {
				System.out.println("Person Deletion Failed");
			}
			
			
		} catch (SQLException e) {
			System.out.println("Database Lookup Error");		}

	}
	
	public static void mainMenu() {
		int menuLoop = 0;


		while (menuLoop != -1) {
			int menuSelection;
			System.out.println("\nMain Menu:");
			System.out.println("1. Add New Person");
			System.out.println("2. Search for Person");
			System.out.println("3. Delete Person");
			System.out.println("4. List all Persons");
			System.out.println("5. Generate full ArrayList");
			System.out.println("6. Quit");

			System.out.print("\nPlease make a selection:\n");

			menuSelection = scanner.nextInt();
			scanner.nextLine();
			System.out.println("");

			if (menuSelection == 1) {
				Person myPerson = createPerson();
				insertPerson(myPerson);
			} else if (menuSelection == 2) {
				Person selectedPerson = selectPerson();
				System.out.println(selectedPerson.toString());
			}else if (menuSelection == 3) { 
				deletePerson();
			}else if (menuSelection == 4) {
				showInfo();
			} else if (menuSelection == 5) {
				ArrayList<Object> allPeople = findAllPeople();
				 for(int i=0; i < allPeople.size(); i++){
			            System.out.println( allPeople.get(i) );
				 }
			} else if (menuSelection == 6) {
				menuLoop = -1;
			}
		}
	}

	public static void main(String[] args) {

		createNewDatabase("oopmod8.db");
		System.out.println("----------------------");
		
		connect();
		mainMenu();

		System.out.println("Exiting, Have a nice day!");
	}

}
