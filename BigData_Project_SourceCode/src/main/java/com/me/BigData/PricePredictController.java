package com.me.BigData;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

@Controller
public class PricePredictController {

	public PricePredictController() {
		// TODO Auto-generated constructor stub
	}


	@RequestMapping(value = "costPrediction.htm", method = RequestMethod.POST)
	public void predictDelay(HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, String> paramhMap= new HashMap<String, String>();

		paramhMap.put("pickuplon", request.getParameter("us3-lon"));
		paramhMap.put("pickuplat", request.getParameter("us3-lat"));
		paramhMap.put("dropofflon", request.getParameter("us1-lon"));
		paramhMap.put("dropofflat",request.getParameter("us1-lat"));
		paramhMap.put("passenger",request.getParameter("passenger"));
		paramhMap.put("datetime",request.getParameter("datetime"));
		
		String jsonReturn1= createRequest(paramhMap,"price");
		String jsonReturn2= createRequest(paramhMap,"surcharge");
		JSONObject json= null;
		PrintWriter out = null;
		try {
			json = new JSONObject(jsonReturn1);
			out = response.getWriter();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		out.println(jsonReturn1+"#@"+jsonReturn2);
	}
	
	private String createRequest(HashMap hmap,String data) {
		hmap=retrieveInputParameter(hmap);
		String restData = null;
		 if(data == "price"){
			 
			 String urlRestWebService= "https://ussouthcentral.services.azureml.net/workspaces/d326091671f24305a0ab2cc832c15649/services/2d0183ec4ee543fc9cb3c161be497e65/execute?api-version=2.0&details=true" ;
				HttpHeaders headers = new HttpHeaders();
				headers.add("Authorization", "Bearer LEyy4yQNHjptK9vQcb6/jzLfDT1GIZErdOFwekOECkzQWXBsh21zrfw/leogEW6BYaA0aM09buz8N29Be+Qk8A==");
				headers.add("Content-Length", "100000");
				headers.add("Content-Type", "application/json");
				String requestJson=null;
				
				
				requestJson = "{'Inputs': {'input2': {'ColumnNames': ['date','weekday','weekinyear', 'hour', 'totalamount','passenger','pickuplon','pickuplat','dropofflon','dropofflat'],'Values':[['"+hmap.get("date")+"','"+hmap.get("weekday")+"','"+hmap.get("weekinyear")+"','"+hmap.get("hour")+"','0','"+hmap.get("passenger")+"','"+hmap.get("pickuplon")+"','"+hmap.get("pickuplat")+"','"+hmap.get("dropofflon")+"','"+hmap.get("dropofflat")+"']]}},'GlobalParameters': {}}";
				
				
				HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
				System.out.println("entity" + entity);
				
				RestTemplate restTemplate = new RestTemplate();
				restData = restTemplate.postForObject(urlRestWebService, entity, String.class);
				System.out.println("Data" + restData);
				
			 
		 }else{
			 	String urlRestWebService= "https://ussouthcentral.services.azureml.net/workspaces/d326091671f24305a0ab2cc832c15649/services/3359333c6a42490db5528e6bbd49a3ee/execute?api-version=2.0&details=true" ;
				HttpHeaders headers = new HttpHeaders();
				headers.add("Authorization", "Bearer Kh6MSXV0dpf3ywDWLyt2iZmbFxSOHRQn8ODcJQZ4a4w19cf6B3/RBlCD9fnqkv+4rkgcv/vmGqD9429a31rWNg==");
				headers.add("Content-Length", "100000");
				headers.add("Content-Type", "application/json");
				String requestJson=null;
				
				requestJson = "{'Inputs': {'input1': {'ColumnNames': ['date','weekday','weekinyear', 'hour', 'surcharge','passenger','pickuplon','pickuplat','dropofflon','dropofflat'],'Values':[['"+hmap.get("date")+"','"+hmap.get("weekday")+"','"+hmap.get("weekinyear")+"','"+hmap.get("hour")+"','0','"+hmap.get("passenger")+"','"+hmap.get("pickuplon")+"','"+hmap.get("pickuplat")+"','"+hmap.get("dropofflon")+"','"+hmap.get("dropofflat")+"']]}},'GlobalParameters': {}}";
		 
				HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
				System.out.println("entity" + entity);
				
				RestTemplate restTemplate = new RestTemplate();
				restData = restTemplate.postForObject(urlRestWebService, entity, String.class);
				System.out.println("Data" + restData);
		 }
		 return restData;
		
	}
	
	private HashMap retrieveInputParameter(HashMap hmap){
		String datetime= (String) hmap.get("datetime");
		hmap.putAll(setStartDate(datetime));	
		return hmap;
		
	}
	
	private HashMap setStartDate(String newDate){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		HashMap hmap = new HashMap();
		try {
			Date sDate=formatter.parse(newDate);
			Calendar cal = Calendar.getInstance();
			cal.setTime(sDate);  
			
			int date= cal.get(Calendar.DAY_OF_MONTH);
			int weekday = cal.get(Calendar.DAY_OF_WEEK);
			int weekinyear = cal.get(Calendar.WEEK_OF_MONTH) + 1;
			int hour = cal.get(Calendar.HOUR_OF_DAY);
			
				
			hmap.put("date", date);
			hmap.put("weekday", weekday);
			hmap.put("weekinyear", weekinyear);
			hmap.put("hour", hour);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hmap;
	}
	
	
}
