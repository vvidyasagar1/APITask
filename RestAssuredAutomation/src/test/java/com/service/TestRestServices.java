package com.service;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.junit.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class TestRestServices {
	public String userId="";

	@Test
	public void test_GetRandomUser_Email() {

		RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
		RequestSpecification httpRequest = RestAssured.given();
		Response response = httpRequest.get("/users");
		// Retrieve the body of the Response
		ResponseBody body = response.getBody();
		// System.out.println("Response Body is: " + body.asString());
		JsonPath jsonPathEvaluator = response.jsonPath();
		List<Object> jsonResponseEmail = response.jsonPath().getList("email");
		List<Object> jsonResponseId = response.jsonPath().getList("id");

		userId=jsonResponseId.get(1).toString();
		System.out.println("Getting userId " + userId);
		System.out.println("Getting Email " + jsonResponseEmail.get(1));

	}

	@Test(dependsOnMethods = "test_GetRandomUser_Email")
	public void test_Validate_Get_PostUserIds() {

		RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
		RequestSpecification httpRequest = RestAssured.given();
		Response response = httpRequest.get("/posts?userId="+userId+"");
		ResponseBody body = response.getBody();
		JsonPath jsonPathEvaluator = response.jsonPath();
		List<Object> jsonResponseUserId = response.jsonPath().getList("id");

		for(int i=0;i<jsonResponseUserId.size();i++){
			if(Integer.parseInt(jsonResponseUserId.get(i).toString())>0 && Integer.parseInt(jsonResponseUserId.get(i).toString())<=100){
				Assert.assertTrue("The UserIds is in between 1 to 100",true);
			}else{
				Assert.assertTrue("The UserIds is not in between 1 to 100",false);
			}
		}


	}
	
	
	@Test(dependsOnMethods = "test_Validate_Get_PostUserIds")
	public void test_Validate_Post_NonEmpty_Body() {
		JSONObject reqparametes= new JSONObject();
		reqparametes.put("userId",userId);
		reqparametes.put("id","88");
		reqparametes.put("title","");
		reqparametes.put("body","This is sample post request");
		Response response= RestAssured.given().contentType("application/json").body(reqparametes).when().post("https://jsonplaceholder.typicode.com/posts");
		System.out.println("Response Status Code-->"+response.getStatusCode());
	}

}
