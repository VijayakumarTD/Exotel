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
import atu.testng.reports.logging.LogAs;;

public class OutboundCall extends Keywords {
	
	
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

	
	
	
	
	
	
	public void EXTC_011(){
		
		
		try {
			
			
			String method=Utils.getDataFromTestData("EXTC_011", "Method");
			String header=Utils.getDataFromTestData("EXTC_011", "Header");
			String body =Utils.getDataFromTestData("EXTC_011", "Parameter");
			String apiName=Utils.getDataFromTestData("EXTC_011", "ApiName");
			String statusCode=Utils.getDataFromTestData("EXTC_011", "StatusCode");
			String subdomain=Utils.getDataFromTestData("EXTC_011", "Subdomain");
			String account_sid=Utils.getDataFromTestData("EXTC_011", "Account_sid");
			String status_value = Utils.getDataFromTestData("EXTC_011","Expected_result");
			String output = Utils.getDataFromTestData("EXTC_011","Output");
			System.out.println(output);

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
			
			
			JSONObject rest = (JSONObject) jsonObject.get("RestException");
			String Message =(String)rest.get("Message");
			System.out.println(Message);
			
			
			add1("Output",LogAs.PASSED, true, Message);
			
			String[] Values = Message.split(";");
			
			
			
			checkTwoString(Values[0],output);
			
			
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}

	
	public void EXTC_012(){
		
		
		try {
			
			
			String method=Utils.getDataFromTestData("EXTC_012", "Method");
			String header=Utils.getDataFromTestData("EXTC_012", "Header");
			String body =Utils.getDataFromTestData("EXTC_012", "Parameter");
			String apiName=Utils.getDataFromTestData("EXTC_012", "ApiName");
			String statusCode=Utils.getDataFromTestData("EXTC_012", "StatusCode");
			String subdomain=Utils.getDataFromTestData("EXTC_012", "Subdomain");
			String account_sid=Utils.getDataFromTestData("EXTC_012", "Account_sid");
			String status_value = Utils.getDataFromTestData("EXTC_012","Expected_result");
			String output = Utils.getDataFromTestData("EXTC_012","Output");
			System.out.println(output);

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
			
			
			JSONObject rest = (JSONObject) jsonObject.get("RestException");
			String Message =(String)rest.get("Message");
			System.out.println(Message);
			
			
			add1("Output",LogAs.PASSED, true, Message);
			
			String[] Values = Message.split(";");
			
			
			
			checkTwoString(Values[0],output);
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}

	
	
	public void EXTC_013(){
		
		
		try {
			
			
			String method=Utils.getDataFromTestData("EXTC_013", "Method");
			String header=Utils.getDataFromTestData("EXTC_013", "Header");
			String body =Utils.getDataFromTestData("EXTC_013", "Parameter");
			String apiName=Utils.getDataFromTestData("EXTC_013", "ApiName");
			String statusCode=Utils.getDataFromTestData("EXTC_013", "StatusCode");
			String subdomain=Utils.getDataFromTestData("EXTC_013", "Subdomain");
			String account_sid=Utils.getDataFromTestData("EXTC_013", "Account_sid");
			String status_value = Utils.getDataFromTestData("EXTC_013","Expected_result");
			String output = Utils.getDataFromTestData("EXTC_013","Output");
			System.out.println(output);
			
			
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
			
			JSONObject rest = (JSONObject) jsonObject.get("RestException");
			String Message =(String)rest.get("Message");
			System.out.println(Message);
			
			add1("Output",LogAs.PASSED, true, Message);
			
			
			
			
			checkTwoString(Message,output);
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}


	
	
	



}
