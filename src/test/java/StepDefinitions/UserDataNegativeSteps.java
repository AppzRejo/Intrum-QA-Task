package StepDefinitions;
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


public class UserDataNegativeSteps {
	
	private final String BASE_URL="https://gorest.co.in/public/v2/users";
	private final String AccessToken="?access-token=0309bd25eb8489143c98e46e5ee0355705349d86a0dbc307810245a0b7dd4579";
	private Response response;
	double randomNumber= Math.random();
	JSONObject requestJSonObject = new JSONObject();
	String jsonString ;
	static int id;
	
	@Given("Invalid user data are available from {string} and {int}")
	public void invalid_user_data_are_available_from_and(String SheetName, Integer rowNumber) throws CsvValidationException, IllegalArgumentException, IOException, InvalidFormatException {
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
	            requestJSonObject.put("email",".com");
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
	@When("I add new user Invalid data to list")
	public void i_add_new_user_invalid_data_to_list() {
		RestAssured.baseURI = BASE_URL;
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type","application/json").
		contentType(ContentType.JSON).
		accept(ContentType.JSON);

		response = request.body(requestJSonObject.toJSONString())
				.post(BASE_URL+AccessToken);
	}
	@Then("the {int} errorcode should be displayed")
	public void the_errorcode_should_be_displayed(Integer errorCode) {
		System.out.println("POST FAILURE ");
		Assert.assertEquals(errorCode,response.getStatusCode());
	}

	

	@When("I update the data with duplicate values")
	public void i_update_the_data_with_duplicate_values() {
		requestJSonObject.put("name", "Athi");	
		requestJSonObject.put("email",".com");
		RestAssured.baseURI = BASE_URL+"/"+id+AccessToken;
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type","application/json").
		contentType(ContentType.JSON).
		accept(ContentType.JSON);

		response = request.body(requestJSonObject.toJSONString())
				.put(BASE_URL+"/"+id+AccessToken);
	}
	@Then("the {int} error should be displayed")
	public void the_error_should_be_displayed(Integer error) {
	    System.out.println("PUT FAILURE ");
		Assert.assertEquals(response.getStatusCode(),error);
	}

	

	
}
