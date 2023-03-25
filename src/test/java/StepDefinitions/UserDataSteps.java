package StepDefinitions;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.simple.JSONObject;
import org.testng.Assert;

import com.opencsv.exceptions.CsvValidationException;
import commonUtil.CSVtoExcel;
import commonUtil.ExcelReader;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class UserDataSteps {
	
	private final String BASE_URL="https://gorest.co.in/public/v2/users";
	private final String AccessToken="?access-token=0309bd25eb8489143c98e46e5ee0355705349d86a0dbc307810245a0b7dd4579";
	private Response response;
	double randomNumber= Math.random();
	JSONObject requestJSonObject = new JSONObject();
	String jsonString ;
	static int id;
	
	
	
	@Given("user data are available from {string} and {int}")
	public void user_data_are_available_from_and(String SheetName, Integer rowNumber) throws CsvValidationException, IllegalArgumentException, IOException, InvalidFormatException {
		//Converting CSV to Excel
				CSVtoExcel file = new CSVtoExcel();
		        String strSource = "C:/Users/LENOVO/Appz_WorkSpace/CucumberJava/src/main/java/UserDataBook.csv";
		        String ConvertedFile = "C:/Users/LENOVO/Appz_WorkSpace/CucumberJava/src/main/java/";
		        file.convertCsvToExcel(strSource, ConvertedFile, ".xlsx");
				
		        //Read Excel file 
		        ExcelReader reader= new ExcelReader();	
			    List<Map<String,String>> testData=reader.getData(ConvertedFile+"/UserDataBook.xlsx",SheetName );
			          
			            //BASE_URL =BASE_URL+AccessToken;
			    		//Create request JSON Object
			            requestJSonObject.put("name", testData.get(rowNumber).get("name"));
			            requestJSonObject.put("email",randomNumber+testData.get(rowNumber).get("email"));
			            requestJSonObject.put("gender",testData.get(rowNumber).get("gender"));
			    		requestJSonObject.put("status",testData.get(rowNumber).get("status"));
			    		System.out.println(requestJSonObject);
			    		
			    		RestAssured.baseURI = BASE_URL;
			    		RequestSpecification request = RestAssured.given();
			    		response = request.get(BASE_URL);

			    	    jsonString = response.asString();			    		
			    	    String responseString=jsonString.substring(7,13);			    	    
			    		id= Integer.parseInt(responseString);	    		
			    		System.out.println("id________"+id);
	}

	@When("I add new user data to list")
	public void i_add_new_user_data_to_list() {
		RestAssured.baseURI = BASE_URL;
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type","application/json").
		contentType(ContentType.JSON).
		accept(ContentType.JSON);

		response = request.body(requestJSonObject.toJSONString())
				.post(BASE_URL+AccessToken);
	}

	@Then("the user data should be added")
	public void the_user_data_should_be_added() {
		Assert.assertEquals(201, response.getStatusCode());
	}

	@When("I update the data")
	public void i_update_the_data() {
		System.out.println(BASE_URL+"/"+id+AccessToken+"url");
		requestJSonObject.put("name", "Athi");	
		requestJSonObject.put("email",randomNumber+"@mail.com");
		RestAssured.baseURI = BASE_URL+"/"+id+AccessToken;
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type","application/json").
		contentType(ContentType.JSON).
		accept(ContentType.JSON);

		response = request.body(requestJSonObject.toJSONString())
				.put(BASE_URL+"/"+id+AccessToken);
	}

	@Then("the list should be updated")
	public void the_list_should_be_updated() {
		Assert.assertEquals(response.getStatusCode(),200 );
	}

	@When("I change the data")
	public void i_change_the_data() {
		requestJSonObject.put("name", "Athira");
		requestJSonObject.put("email",randomNumber+"@mail.com");		
		RestAssured.baseURI = BASE_URL+"/"+id+AccessToken;
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type","application/json").
		contentType(ContentType.JSON).
		accept(ContentType.JSON);

		response = request.body(requestJSonObject.toJSONString())
				.put(BASE_URL+"/"+id+AccessToken);
	}

	@When("i remove data from list")
	public void i_remove_data_from_list() {
		RestAssured.baseURI = BASE_URL+"/"+id+AccessToken;
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type","application/json").
		contentType(ContentType.JSON).
		accept(ContentType.JSON);

		response = request.delete(BASE_URL+"/"+id+AccessToken);
	}

	@Then("the user data should be removed")
	public void the_user_data_should_be_removed() {
		Assert.assertEquals(response.getStatusCode(),204);
	}
	
	@When("i get the deleted user data")
	public void i_get_the_deleted_user_data() {
		RestAssured.baseURI = BASE_URL+"/"+id+AccessToken;
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type","application/json").
		contentType(ContentType.JSON).
		accept(ContentType.JSON);

		response = request.get(BASE_URL+"/"+id+AccessToken);
	}

	@Then("the error should be displayed")
	public void the_error_should_be_displayed() {
		
		//Verifying the status Code
		Assert.assertEquals(response.getStatusCode(),404);
		
		//Verifying the message
		jsonString = response.asString();		
		String errorMessage = JsonPath.from(jsonString).get("message");		
 		Assert.assertEquals(errorMessage, "Resource not found"); 	    	
 		
	}



}
