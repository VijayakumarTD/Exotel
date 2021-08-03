package scripts;

import commonMethods.Keywords;
import commonMethods.Utils;

import okhttp3.Credentials;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import com.github.underscore.lodash.U;

import org.json.JSONException;
import org.json.JSONObject;


import org.codehaus.jackson.map.ObjectMapper;
import com.fasterxml.jackson.xml.XmlMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.ReadContext;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

import atu.testng.reports.ATUReports;
import atu.testng.reports.logging.LogAs;

public class Test_file extends Keywords {

	
	public void outbound()throws IOException, JSONException {
		
		try {
		
		
		String method=Utils.getDataFromTestData("outbound call", "Method");
		String header=Utils.getDataFromTestData("outbound call", "Header");
		String body =Utils.getDataFromTestData("outbound call", "Parameter");
		String apiName=Utils.getDataFromTestData("outbound call", "ApiName");
		String statusCode=Utils.getDataFromTestData("outbound call", "StatusCode");
		String subdomain=Utils.getDataFromTestData("outbound call", "Subdomain");
		String account_sid=Utils.getDataFromTestData("outbound call", "Account_sid");
		String status_value = Utils.getDataFromTestData("outbound call","Expected_result");

		//Splitting the credentials
		String[] a = header.split(":");
		String API_token = a[0];
		String API_password = a[1];
		
		
		
		//Splitting the body 
		String[] value = body.split(",");
		
		//Splitting the Numbers 
		String[] from = value[0].split(":");
		String[] callerId = value[1].split(":");
		String[] to = value[2].split(":");
		String from_result = from[1].replaceAll("^[\"']+|[\"']+$", "");
		String to_result = to[1].replaceAll("^[\"']+|[\"']+$", "");
		String callerid_result = callerId[1].replaceAll("^[\"']+|[\"']+$", "");
		
		
		//URL creation
		String URL = "https://"+ API_token + ":"+ API_password + subdomain +"/v1/Accounts/"+ account_sid +"/Calls/connect.json";	
		add1("URL = " + URL   , LogAs.PASSED, true, "API Key:" + API_token +'\n' + "API_Password" + API_password);
		
		
		 
	    //Post response outbound call  
		Response res=apiValidRequest(URL,method,header,body,apiName,Integer.parseInt(statusCode));
		
		
		JSONObject jsonObject = null;
		jsonObject = new JSONObject(res.asString());

		
		//Extracting the Response and validating
		JSONObject call = (JSONObject) jsonObject.get("Call");
		String sid_value =(String)call.get("Sid");
		
		
		String uri_value ="/v1/Accounts/"+ account_sid +"/Calls/"+ sid_value+".json" ; 
		
		
		String status = (String) call.get("Status");
		String accountsid =(String)call.get("AccountSid");
		String to_json = (String)call.get("To");
		String from_json=(String)call.get("From");
		String caller_json=(String)call.get("PhoneNumberSid");
		String uri_json = (String)call.get("Uri");
		

		
		add1("The value of sid ",LogAs.PASSED, true, sid_value);
		
		checkTwoString(status_value,status);
		checkTwoString(accountsid,account_sid);
		checkTwoString(from_result,from_json);
		checkTwoString(to_result,to_json);
		checkTwoString(callerid_result,caller_json);
		checkTwoString(uri_value, uri_json);
		
		

	
		Thread.sleep(35000);
		
		//URL creation using sid value
		String URL1 = "https://"+ API_token + ":"+ API_password + subdomain +"/v1/Accounts/"+ account_sid +"/Calls/"+sid_value+".json";
		
		
		add1("URL = " + URL1   , LogAs.PASSED, true, "API Key:" + API_token +'\n' + "API_Password" + API_password);
		
		
		
		String method1= "GET";
		
		//GET using to get the Call completed details
		Response res1 = RestAssured.get(URL1);
		//Response res1 = RestAssured.given().contentType("application/json").when().then().get(URL1);
		
		add("Response String", LogAs.PASSED, true,
				res1.print().toString(), (res1.getTime()));
		
		
		//Response res1 = RestAssured.given().when().contentType("application/json").get(URL1);
		System.out.println(res1.toString());
		
		JsonObject jsonObject11 = new JsonParser().parse(res1.asString()).getAsJsonObject();

        String pageName = jsonObject11.getAsJsonObject("Call").get("Status").toString().replaceAll("^[\"']+|[\"']+$", "");
        System.out.println(pageName);
        
        
       //Extracting data from Json
		String accountsid_get =jsonObject11.getAsJsonObject("Call").get("AccountSid").toString().replaceAll("^[\"']+|[\"']+$", "");
		String to_json_get = jsonObject11.getAsJsonObject("Call").get("To").toString().replaceAll("^[\"']+|[\"']+$", "");
		String from_json_get=jsonObject11.getAsJsonObject("Call").get("From").toString().replaceAll("^[\"']+|[\"']+$", "");
		String caller_json_get=jsonObject11.getAsJsonObject("Call").get("PhoneNumberSid").toString().replaceAll("^[\"']+|[\"']+$", "");
		String uri_json_get = jsonObject11.getAsJsonObject("Call").get("Uri").toString().replaceAll("^[\"']+|[\"']+$", "");
		String direction_get =jsonObject11.getAsJsonObject("Call").get("Direction").toString().replaceAll("^[\"']+|[\"']+$", "");
		String recording_get =jsonObject11.getAsJsonObject("Call").get("RecordingUrl").toString().replaceAll("^[\"']+|[\"']+$", "");
		String caller_name = jsonObject11.getAsJsonObject("Call").get("CallerName").toString().replaceAll("^[\"']+|[\"']+$", "");
		String answered_by =jsonObject11.getAsJsonObject("Call").get("AnsweredBy").toString().replaceAll("^[\"']+|[\"']+$", "");
		String price_get =jsonObject11.getAsJsonObject("Call").get("Price").toString().replaceAll("^[\"']+|[\"']+$", "");
        
        
		//Validating with the call details
		checkTwoString(accountsid,accountsid_get);
		checkTwoString(from_json_get,from_json);
		checkTwoString(to_json_get,to_json);
		checkTwoString(callerid_result,caller_json_get);
		checkTwoString(pageName,"completed");
		
		
		//Printing to Reports
		add1("Uri Value ",LogAs.PASSED, true, uri_json_get);
		add1("Direction  ",LogAs.PASSED, true, direction_get);
		add1("RecordingUrl ",LogAs.PASSED, true, recording_get);
		add1("CallerName ",LogAs.PASSED, true, caller_name);
		add1("AnsweredBy ",LogAs.PASSED, true, answered_by);
		add1("Price ",LogAs.PASSED, true, price_get);
		
		

		}catch(Exception e) {
			
			
			
			
		}

	}
	
	
	
	public void statusCallback() {
		try {
			
			String method=Utils.getDataFromTestData("outbound call", "Method");
			String header=Utils.getDataFromTestData("outbound call", "Header");
			String body =Utils.getDataFromTestData("outbound call", "Parameter");
			String apiName=Utils.getDataFromTestData("outbound call", "ApiName");
			String statusCode=Utils.getDataFromTestData("outbound call", "StatusCode");
			String subdomain=Utils.getDataFromTestData("outbound call", "Subdomain");
			String account_sid=Utils.getDataFromTestData("outbound call", "Account_sid");
			String status_value = Utils.getDataFromTestData("outbound call","Expected_result");

			//Splitting the credentials
			String[] a = header.split(":");
			String API_token = a[0];
			String API_password = a[1];
			
			
			
			//Splitting the body 
			String[] value = body.split(",");
			
			//Splitting the Numbers 
			String[] from = value[0].split(":");
			String[] callerId = value[1].split(":");
			String[] to = value[2].split(":");
			String from_result = from[1].replaceAll("^[\"']+|[\"']+$", "");
			String to_result = to[1].replaceAll("^[\"']+|[\"']+$", "");
			String callerid_result = callerId[1].replaceAll("^[\"']+|[\"']+$", "");
			
			
			//URL creation
			String URL = "https://"+ API_token + ":"+ API_password + subdomain +"/v1/Accounts/"+ account_sid +"/Calls/connect.json";	
			add1("URL = " + URL   , LogAs.PASSED, true, "API Key:" + API_token +'\n' + "API_Password" + API_password);
			
			
			 
		    //Post response outbound call  
			Response res=status_reuse(URL,method,header,body,apiName,Integer.parseInt(statusCode));
			
			
			JSONObject jsonObject = null;
			jsonObject = new JSONObject(res.asString());

			
			//Extracting the Response and validating
			JSONObject call = (JSONObject) jsonObject.get("Call");
			String sid_value =(String)call.get("Sid");
			
			add1("The value of sid ",LogAs.PASSED, true, sid_value);
			
			String to_json = (String)call.get("To");
			String from_json=(String)call.get("From");
			String caller_json=(String)call.get("PhoneNumberSid");

			
			Thread.sleep(35000);
			
			//URL creation using sid value
			String URL1 = "http://ec2-3-109-100-2.ap-south-1.compute.amazonaws.com/callback_json/"+ sid_value +".json";
			
			add1("AWS link",LogAs.PASSED, true, URL1);

			
			String method1= "GET";
			
			//GET using to get the Call completed details
			Response res1 = RestAssured.get(URL1);
			
			
			add("Response String", LogAs.PASSED, true,
					res1.print().toString(), (res1.getTime()));
			
			
			
			System.out.println(res1.toString());
			
			String str = res1.getBody().asString();
			
		    String str2 = U.jsonToXml(str); 
		    System.out.println(str2);
		    
		 //   add1("XML response",LogAs.PASSED, true, str2);
		    
		    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		    InputSource src = new InputSource();
		    src.setCharacterStream(new StringReader(str2));

		    Document doc = builder.parse(src);
		    String leg0_oncall = doc.getElementsByTagName("OnCallDuration").item(0).getTextContent();
		    add1("Leg 0_OnCallDuration",LogAs.PASSED, true, leg0_oncall);
		    
		    String leg0_status = doc.getElementsByTagName("Status").item(1).getTextContent();
		    add1("Leg 0_Status",LogAs.PASSED, true, leg0_status);
		    String leg1_oncall = doc.getElementsByTagName("OnCallDuration").item(1).getTextContent();
		    add1("Leg 1_OnCallDuration",LogAs.PASSED, true, leg1_oncall);
		    String leg1_status = doc.getElementsByTagName("Status").item(2).getTextContent();
		    add1("Leg 1_Status",LogAs.PASSED, true, leg1_status);
		    
		    String RecordingUrl = doc.getElementsByTagName("RecordingUrl").item(0).getTextContent();
		    add1("RecordingUrl",LogAs.PASSED, true, RecordingUrl);
		    
		    String Call_sid = doc.getElementsByTagName("CallSid").item(0).getTextContent();
		    add1("Callsid",LogAs.PASSED, true, Call_sid);
		    
		    String to_number = doc.getElementsByTagName("To").item(0).getTextContent();
		    add1("To Number",LogAs.PASSED, true, to_number);
		    
		    String from_number = doc.getElementsByTagName("From").item(0).getTextContent();
			add1("From Number", LogAs.PASSED, true, from_number);
		    
		    String Phone_sid = doc.getElementsByTagName("PhoneNumberSid").item(0).getTextContent();
		    add1("CallerId",LogAs.PASSED, true, Phone_sid);
		    
		    
		    checkTwoString(Call_sid,sid_value);
		    checkTwoString(to_number,to_json);
		    checkTwoString(from_number,from_json);
		    checkTwoString(Phone_sid,caller_json);
		    
		    
		    
		    FileWriter file = new FileWriter(System.getProperty("user.dir")+"/output.json");
			file.write(str2); 
            file.flush();
		    

					
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
	
		
}
