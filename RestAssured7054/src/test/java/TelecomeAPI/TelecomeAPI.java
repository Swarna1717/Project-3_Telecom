package TelecomeAPI;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import static org.hamcrest.Matchers.*;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
/*
 * given()-prerequisite
 * ------------------------------
 * header,cookies,request payload,authorization,path parameters,query parmeters
 * 
 * when()- Request
 * -----------------------------
 * GET,POST,PUT,PATCH,DELETE
 * 
 * then()- validation 
 * ---------------------------
 * status code,message,response time,payload,headers,cookies
 * 
 * 
 */

public class TelecomeAPI {
	 String tokenvalue;
	 String logintoken;
	 String id;
	 String dynamicEmail;
	 private ExtentReports extent;
	    private ExtentTest test;
	    
	   @BeforeClass
	    public void setup() {
	        extent = ExtentManager.getInstance(); // Initialize ExtentReports
	        RestAssured.baseURI = "https://jsonplaceholder.typicode.com"; // API Base URL
	    }
	 
@Test(priority=1)
  public void addNewUser() {
	 test = extent.createTest("Add New User Test");
	String useremail= "Swarna"+System.currentTimeMillis()+"@gmail.com";
	  Response res= given()
			  .log().all()
			  .header("Accept","application/json")
			  .header("Content-Type", "application/json")
			  .body("{ \n"
			  		+ "\"firstName\": \"Swarna\", \n"
			  		+ "\"lastName\": \"Mishra\", \n"
			  		+ "\"email\": \"" + useremail + "\", \n"
			  		+ "\"password\": \"myPassword\" \n"
			  		+ "}")
			  
			 .when().post("https://thinking-tester-contact-list.herokuapp.com/users");
			  
			  res.then()
			  .log().all()
	            .statusCode(201);
			  
			  tokenvalue=res.jsonPath().getString("token");
			  System.out.println("New User created with status code: "+tokenvalue);
			  System.out.println("User Profile get created with id: "+res.statusCode());
			  System.out.println("User Profile get created with id: "+res.statusLine());
			  Assert.assertEquals(res.getStatusCode(), 201,"Expected status code 201 for user creation");
			  Assert.assertTrue(res.getStatusLine().contains("Created"),"Status line should contain 'Created'");
  }
@Test(priority=2,dependsOnMethods ="addNewUser")
  public void getProfile()
  {
	test = extent.createTest("Get User Profile Test");
	  Response res=given()
	  .header("Content-Type","application/json")
	  .header("Accept","application/json")
	  .header("Authorization", "Bearer " + tokenvalue)
	  
	  .when().get("https://thinking-tester-contact-list.herokuapp.com/users/me");
	  
	  res.then()
	  .log().body()
	  .statusLine(containsString("OK"))
        .statusCode(200);
	  System.out.println("User Profile get created with id: "+res.statusCode());
	  System.out.println("User Profile get created with id: "+res.statusLine());
	  Assert.assertEquals(res.getStatusCode(), 200);
	  Assert.assertTrue(res.getStatusLine().contains("OK"),"Status line should contain 'OK'");
  }
@Test(priority=3,dependsOnMethods ="getProfile") 
  public void updateUser()
  {
	test = extent.createTest("Update User Test");
	 int counter = 1; 
	 dynamicEmail = "SwarnaQA" + System.currentTimeMillis() + "@gmail.com"; 
	 int currentCount = counter++;
     String name = "swarna" + currentCount;
	  Response res=given()
			  .header("Content-Type","application/json")
			  .header("Accept","application/json")
			  .header("Authorization", "Bearer " + tokenvalue)
	  .body("{ \n"
	  		+ "\"firstName\": \""+name+"\", \n"
	  		+ "\"lastName\": \"Mishra\", \n"
	  		+ "\"email\": \"" + dynamicEmail + "\", \n"
	  		+ "\"password\": \"myPassword\" \n"
	  		+ "} ")
	  
	  .when().patch("https://thinking-tester-contact-list.herokuapp.com/users/me");
	  
	  res.then().log().body() 
	  .statusLine(containsString("OK"))
      .statusCode(200);
	  String fname= res.jsonPath().getString("firstName");
	  System.out.println("User updated with status code : "+res.statusCode());
	  System.out.println("User updated with status code : "+fname);
	  Assert.assertEquals(res.getStatusCode(), 200);
	  Assert.assertTrue(res.getStatusLine().contains("OK"),"Status line should contain 'OK'");
	  Assert.assertEquals(name, fname);
	    
  }
@Test(priority=4,dependsOnMethods ="updateUser")
  public void loginUser()
  {
	test = extent.createTest("Login User Test");
	  Response res=given()
	  .header("Content-Type","application/json")
	  .header("Accept","application/json")
	  .body("{ \n"
	  		+ " \n"
	  		+ " \n"
	  		+ "\"email\": \""+ dynamicEmail +"\", \n"
	  		+ "\"password\": \"myPassword\" \n"
	  		+ "} ")
	  .when().post("https://thinking-tester-contact-list.herokuapp.com/users/login");
	  
	  res.then().log().body()
	  .statusLine(containsString("OK"))
      .statusCode(200);
	  
	  logintoken=res.jsonPath().getString("token");
	  System.out.println("User login with status code: "+res.statusCode());
	  System.out.println("User Profile get createed with id: "+res.statusLine());
	  Assert.assertEquals(res.getStatusCode(), 200);
	  Assert.assertTrue(res.getStatusLine().contains("OK"),"Status line should contain 'OK'");
  }
@Test(priority=5,dependsOnMethods ="loginUser")
  public void addContact()
  {
	test = extent.createTest("Add Contact Test");
	  Response res=given()
	  .header("Content-Type","application/json")
	  .header("Accept","application/json")
	  .header("Authorization", "Bearer " + tokenvalue)
	  .body("{ \n"
	  		+ "\"firstName\": \"Vihaan\", \n"
	  		+ "\"lastName\": \"Mishra\", \n"
	  		+ "\"birthdate\": \"2015-09-02\", \n"
	  		+ "\"email\": \"Vihaantest@gmail.com\", \n"
	  		+ "\"phone\": \"8005555555\", \n"
	  		+ "\"street1\": \"1 Main St.\", \n"
	  		+ "\"street2\": \"Apartment A\", \n"
	  		+ "\"city\": \"Anytown\", \n"
	  		+ "\"stateProvince\": \"KS\", \n"
	  		+ "\"postalCode\": \"12345\", \n"
	  		+ "\"country\": \"USA\" \n"
	  		+ "}")
	  
	  
	  
	  .when().post("https://thinking-tester-contact-list.herokuapp.com/contacts");
	  
	  res.then().log().body();
	  
	  id=res.jsonPath().getString("_id");
	  System.out.println("User contact created with status code: "+res.statusCode());
	  Assert.assertEquals(res.getStatusCode(), 201,"Expected status code 201 for user creation");
	  Assert.assertTrue(res.getStatusLine().contains("Created"),"Status line should contain 'Created'");
  }
  
@Test(priority=6,dependsOnMethods ="addContact") 
  
  public void getContactList()
  {
	test = extent.createTest("Get ContactList Test");
	  Response res=given()
			  .header("Content-Type","application/json")
			  .header("Accept","application/json")
			  .header("Authorization","Bearer "+logintoken)
			  
			  .when().get("https://thinking-tester-contact-list.herokuapp.com/contacts");
	  
	  res.then().log().body();
	  
	  System.out.println("Contact list with code: "+res.statusCode());
	  Assert.assertEquals(res.getStatusCode(), 200);
	  Assert.assertTrue(res.getStatusLine().contains("OK"),"Status line should contain 'OK'");
	  
			  
  }
  
@Test(priority=7,dependsOnMethods ="getContactList") 
  public void getContact()
  {
	test = extent.createTest("Get Contact Test");
	  Response res=given()
  .header("Content-Type","application/json")
  .header("Accept","application/json")
  .header("Authorization","Bearer "+logintoken)
  
  .when().get("https://thinking-tester-contact-list.herokuapp.com/contacts");

res.then().log().body();

System.out.println("Contact data with code: "+res.statusCode());
Assert.assertEquals(res.getStatusCode(), 200);
Assert.assertTrue(res.getStatusLine().contains("OK"),"Status line should contain 'OK'");
	  
  }
@Test(priority=8,dependsOnMethods ="getContact")
  public void updateContact()
  {
	test = extent.createTest("Update Contact Test");
	  Response res=given()
	  .header("Content-Type","application/json")
	  .header("Accept","application/json")
	  .header("Authorization","Bearer "+logintoken)
	  
	  .body("{ \n"
	  		+ "\"firstName\": \"Amy\", \n"
	  		+ "\"lastName\": \"Miller\",\n"
	  		+ " \n"
	  		+ " \n"
	  		+ "\"birthdate\": \"1992-02-02\", \n"
	  		+ "\"email\": \"amiller@fake.com\", \n"
	  		+ "\"phone\": \"8005554242\", \n"
	  		+ "\"street1\": \"13 School St.\", \n"
	  		+ "\"street2\": \"Apt. 5\", \n"
	  		+ "\"city\": \"Washington\", \n"
	  		+ "\"stateProvince\": \"QC\", \n"
	  		+ "\"postalCode\": \"A1A1A1\", \n"
	  		+ "\"country\": \"Canada\" \n"
	  		+ "} ")
	  
	  
	  
	  .when().put("https://thinking-tester-contact-list.herokuapp.com/contacts/"+id);
	  
	  res.then().log().body();
	  String responseEmail = res.jsonPath().getString("email");
	  System.out.println("User updated with code: "+res.statusCode());
	  Assert.assertEquals(res.getStatusCode(), 200);
	  Assert.assertTrue(res.getStatusLine().contains("OK"),"Status line should contain 'OK'");
	  Assert.assertEquals(responseEmail, "amiller@fake.com", "The email in the response matches the request payload");
	  
  }
  
@Test(priority=9,dependsOnMethods ="updateContact") 
  public void updateContactpatch()
  {
	test = extent.createTest("Partial Update Contact Test");
	  Response res=given()
			  .header("Content-Type","application/json")
			  .header("Accept","application/json")
			  .header("Authorization","Bearer "+logintoken)
			  .body("{ \n"
			  		+ "\"firstName\": \"Anna\", \n"
			  		+ "\"lastName\": \"Miller\"} ")
			  .when().put("https://thinking-tester-contact-list.herokuapp.com/contacts/"+id);
	  
	  res.then().log().body();
	  String fname= res.jsonPath().getString("firstName");
	  System.out.println("User updated with code: "+res.statusCode());
	  Assert.assertEquals(fname, "Anna", "The email in the response matches the request payload");
  }
@Test(priority=10,dependsOnMethods ="updateContactpatch")
  public void logoutUser()
  {
	test = extent.createTest("Logout Test");
	  Response res=given()
	  .header("Content-Type","application/json")
	  .header("Accept","application/json")
	  .header("Authorization","Bearer "+logintoken)
	  .when().post("https://thinking-tester-contact-list.herokuapp.com/users/logout");
	  
	  res.then().log().body();
	  System.out.println("User logout with code: "+res.statusCode());
	  Assert.assertEquals(res.getStatusCode(), 200);
	  Assert.assertTrue(res.getStatusLine().contains("OK"),"Status line should contain 'OK'");
  }
@AfterClass
public void tearDown() {
    extent.flush(); // Generate the Extent report
}
}
