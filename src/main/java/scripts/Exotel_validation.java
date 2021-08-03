package scripts;

import static org.testng.Assert.fail;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;



import commonMethods.Keywords;


public class Exotel_validation extends Keywords {
	
	/*
	public void statuscallback() {
		
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
			
			
			String URL = "https://"+ API_token + ":"+ API_password + subdomain +"/v1/Accounts/"+ account_sid +"/Calls/connect.json";	
			add1("URL = " + URL   , LogAs.PASSED, true, "API Key:" + API_token +'\n' + "API_Password" + API_password);
			
			
			Response res=status_reuse(URL, method, header, apiName, Integer.parseInt(statusCode));
			
			
			
			 JSONObject jsonObject = new JSONObject(res.asString());

			
			//Extracting the Response and validating
			JSONObject call = (JSONObject) jsonObject.get("Call");
			String sid_value =(String)call.get("Sid");
			System.out.println(sid_value);
			
			
			Thread.sleep(50000);
			
			
			String uri_value ="http://ec2-3-109-100-2.ap-south-1.compute.amazonaws.com/callback_json/"+ sid_value +".json";
			
			String method1= "GET";
			
	
			Response res1 = RestAssured.given().when().get(uri_value).then().extract().response();
			
		//	Response res2 = RestAssured.get(uri_value);
			
			
			
			FileWriter file = new FileWriter(System.getProperty("user.dir")+"/output.json");
			file.write(res1.asString()); 
            file.flush();
			
            
            
			/*
			
			JsonObject jsonObject11 = new JsonParser().parse(res1.getBody().asString()).getAsJsonObject();
			
			
			FileWriter file = new FileWriter(System.getProperty("user.dir")+"/output.json");
			file.write(jsonObject11.toString());
	        file.close();
	        
	        
	        
	        String test = System.getProperty("user.dir")+"/output.json";
			
	        
	        JSONParser parser = new JSONParser();
	        FileReader fileReader = new FileReader(test); 
	        System.out.println(fileReader);
	        JSONObject json = (JSONObject) parser.parse(fileReader);
	        
	        
	        JSONObject leg = (JSONObject) jsonObject.get("Legs");
			String duration =(String)leg.get("OnCallDuration");
			System.out.println(duration);
			

			
			
			String value = res1.getBody().asString();
			System.out.println(value);
			
		//	mockresponse(value);
			
			
			
			
			
			
			
		}catch (Exception e) {
			
		
		
		}
		
		
		
		
		
	}
	
	
	public void mockresponse() throws JSONException {
		
		try {
			
			
			 JSONParser jsonParser = new JSONParser();
			 
			 Object obj = jsonParser.parse(new FileReader(System.getProperty("user.dir")+"/output.json"));
			 
			 
			 JSONObject jsonObject =  (JSONObject) obj;
			 
			 
			 
			 
			 
			 
			 
			 
			 FileReader reader = new FileReader(System.getProperty("user.dir")+"/output.json");
			 
			 Object obj = jsonParser.parse(reader);
			 
	         JSONArray employeeList = (JSONArray) obj;
	         System.out.println(employeeList);
	         
	           
			
			
			
			JsonObject jsonObject11 = new JsonParser().parse(value).getAsJsonObject();
			
			
			String pageName = jsonObject11.getAsJsonObject("Legs").get("OnCallDuration").toString();
	        System.out.println(pageName);

			
		} catch (Exception e) {
			Assert.fail();
		}
		
		
		
		
        
        
		
	}
	
	
	*/
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			URL url = new URL("http://ec2-3-109-100-2.ap-south-1.compute.amazonaws.com/callback_json/270f2c0093f409b5c0af31f99ae7157s.json");
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			String str = "";
			while (null != (str = br.readLine())) {
				System.out.println(str);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		   
	}
}
	
	
	


