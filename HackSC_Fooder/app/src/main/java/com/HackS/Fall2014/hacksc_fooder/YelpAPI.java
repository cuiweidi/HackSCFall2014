package com.HackS.Fall2014.hacksc_fooder;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

//Read current city by Androids native API and pass them into a fixed string into constructor
class Restaurant {
	String id;
	String displayPhone;
	double rating;
	String url;
	double phone;
	String imageUrl;
	String cityState;
	String address;
	String zipCode;
	String country;
	String mapUrl;
}

public class YelpAPI{
  private static final String API_HOST = "api.yelp.com";
  
  private static String TERM;
  private static String LOCATION;
  private static int LIMIT;
  private static final String SEARCH_PATH = "/v2/search";
  private static final String BUSINESS_PATH = "/v2/business";
  //private static final String BUSINESS_PATH = "/v2/business/urlencode(";
  
  //Note: Credentials all belong to Jiayang Miao, not a safely usable way to acceess
  //Yelp, though it is acceptable at current point
  private static final String CONSUMER_KEY = "qAAptIGxYfY0H5L52Gr-ww";
  private static final String CONSUMER_SECRET = "Eo530imS86aTdc-57dDZqnVzURY";
  private static final String TOKEN = "5Avtvp_KWS9T-9S76Kbamki-pbxGchr8";
  private static final String TOKEN_SECRET = "L_NxplHyLEVUo0oczI1FMUEaoBI";
  
  OAuthService service;
  Token accessToken;

  //Modified version: use all Jiayang's credentials and change parameter li  
  public YelpAPI(String term, String location, int limit) {
    this.service =
        new ServiceBuilder().provider(TwoStepOAuth.class).apiKey(CONSUMER_KEY)
            .apiSecret(CONSUMER_SECRET).build();
    this.accessToken = new Token(TOKEN, TOKEN_SECRET);
    TERM = term;
    LOCATION = location;
    LIMIT = limit;
  }

  public String searchForBusinessesByLocation(String term, String location) {
    OAuthRequest request = createOAuthRequest(SEARCH_PATH);
    request.addQuerystringParameter("term", term);
    request.addQuerystringParameter("location", location);
    request.addQuerystringParameter("limit", String.valueOf(LIMIT));
      System.out.println("Mark2");
    return sendRequestAndGetResponse(request);
  }

  public String searchByBusinessId(String businessID) {
  	//String healBusinessID = businessID.replace("\u0026", "&");
  	//System.out.println(healBusinessID);
	OAuthRequest request = createOAuthRequest(BUSINESS_PATH + "/" + businessID);
    //OAuthRequest request = createOAuthRequest(BUSINESS_PATH + businessID + ")");
    return sendRequestAndGetResponse(request);
  }

  private OAuthRequest createOAuthRequest(String path) {
	//System.out.println(path);
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://" + API_HOST + path);
    return request;
  }

  private String sendRequestAndGetResponse(OAuthRequest request) {
    System.out.println("Querying " + request.getCompleteUrl() + " ...");
    this.service.signRequest(this.accessToken, request);
    Response response = request.send();
    return response.getBody();
  }
  
  protected ArrayList<Restaurant> queryAPI(YelpAPI yelpApi) {
      ArrayList<Restaurant> resultList= new ArrayList<Restaurant> ();
      try{
          System.out.println("Mark");
    String searchResponseJSON =
        yelpApi.searchForBusinessesByLocation(TERM, LOCATION);
    	//JSONParser parser = new JSONParser();
          System.out.println("Mark");
      JSONObject response = new JSONObject(searchResponseJSON);
          System.out.println("Mark");
//ARRAY PART
      //System.out.println(searchResponseJSON);
     // if (!response.has("error")) {
    		JSONArray businesses = (JSONArray) response.get("businesses");
      //}
    //System.out.println(businesses.length());
          System.out.println("Mark");
    
    for(int i=0; i<businesses.length(); i++) {
    	JSONObject thisBusiness = (JSONObject) businesses.get(i);
    	//System.out.println(thisBusiness.getString("id"));
    	//System.out.println(thisBusiness.has("error"));
    	//if (thisBusiness.has("error")) continue;
	    	String thisBusinessID = thisBusiness.get("id").toString();
	    	System.out.println(thisBusinessID);
	    	// Select the first business and display business details

	    	String businessResponseJSON = yelpApi.searchByBusinessId(thisBusinessID);
	    	//System.out.println(String.format("Result for business \"%s\" found:", thisBusinessID));
	    	JSONObject obj = new JSONObject(businessResponseJSON);
	    	if (obj.has("error")) continue;
	    	System.out.println(businessResponseJSON);
	    	Restaurant newRes = new Restaurant();
	    		newRes.id = obj.getString("id");
	    		if (!obj.has("displayPhone")) {
	    			newRes.displayPhone = "";
	    		}
	    		else newRes.displayPhone = obj.getString("display_phone");
	    		newRes.rating = obj.getDouble("rating");
	    		newRes.url = obj.getString("url");
	    		if (!obj.has("phone")) {
	    			newRes.phone = 0;
	    		}
	    		else newRes.phone = Double.parseDouble(obj.getString("phone"));
	    		JSONObject location = (JSONObject) obj.get("location");
	    			String city = location.getString("city");
	    			String state = location.getString("state_code");
	    		newRes.cityState = city + ", " + state;
	    		JSONArray addresses = (JSONArray) location.get("address");
	    		newRes.address =  (String) addresses.get(0); //address only, no city and no state
	    		newRes.zipCode = location.getString("postal_code");
	    		newRes.country = location.getString("country_code");
	    			String imageOriUrl = obj.getString("image_url");
	    			String [] tempSplit = imageOriUrl.split("/");
	    			tempSplit[tempSplit.length-1] = "o.jpg";
	    			String imageNewUrl = "";
	    			for(int j=0; j<tempSplit.length; j++) {
	    				if (j==0) {
	    					imageNewUrl = imageNewUrl + tempSplit[j] + "/";
	    				}
	    				else if (j== tempSplit.length-1) {
	    					imageNewUrl = imageNewUrl + tempSplit[j];
	    				}
	    				else {
	    					imageNewUrl = imageNewUrl + tempSplit[j] + "/";
	    				}
	    			}
	    		newRes.imageUrl = imageNewUrl;
	    		String part1 = "http://maps.googleapis.com/maps/api/staticmap?&zoom=13&scale=1&size=400x400&maptype=roadmap&markers=color:red%7C";
	    		String addressKai = newRes.address.replace(" ", "+");
	    		String cityStateKai = newRes.cityState.replace(" ", "");
	    		String part2 = addressKai + "," + cityStateKai;
	    		String part3 = "&sensor=true_or_false";
	    		newRes.mapUrl = part1 + part2 + part3;
	    		resultList.add(newRes);
        System.out.println("Mark");
    }
      }catch(Exception e){
          System.out.println("Error: "+e.getMessage());
      }
    for (int i=0; i<resultList.size(); i++) {
    	//System.out.println(resultList.get(i).id + "" + resultList.get(i).imageUrl);
    	//System.out.println(resultList.get(i).address + ", " + resultList.get(i).cityState);
    	//System.out.println(resultList.get(i).mapUrl);
    }
    return resultList;
  }

  //Example of how to make YelpAPI object and do the Query
  //public static void main(String[] args) {
    //YelpAPI yelpApi = new YelpAPI("ramen", "Los Angeles, CA", 10);
   // ArrayList<Restaurant> resultList = yelpApi.queryAPI(yelpApi);
  //}
}