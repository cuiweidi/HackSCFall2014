package com.HackS.Fall2014.hacksc_fooder;

import com.HackS.Fall2014.hacksc_fooder.Restaurant;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

public class YelpAPI {
	
  private static final String API_HOST = "api.yelp.com";
  
  private static String TERM;
  private static String LOCATION;
  private static int LIMIT;
  private static final String SEARCH_PATH = "/v2/search";
  private static final String BUSINESS_PATH = "/v2/business";

  //Note: Credentials all belong to Jiayang Miao, not a safely usable way to acceess
  //Yelp, though it is acceptable at current point
  private static final String CONSUMER_KEY = "qAAptIGxYfY0H5L52Gr-ww";
  private static final String CONSUMER_SECRET = "Eo530imS86aTdc-57dDZqnVzURY";
  private static final String TOKEN = "5Avtvp_KWS9T-9S76Kbamki-pbxGchr8";
  private static final String TOKEN_SECRET = "L_NxplHyLEVUo0oczI1FMUEaoBI";
  
  OAuthService service;
  Token accessToken;

  /**
   * Setup the Yelp API OAuth credentials.
   * @param consumerKey Consumer key
   * @param consumerSecret Consumer secret
   * @param token Token
   * @param tokenSecret Token secret
   */
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

  /**
   * Creates and sends a request to the Search API by term and location.
   * <p>
   * See <a href="http://www.yelp.com/developers/documentation/v2/search_api">Yelp Search API V2</a>
   * for more info.
   * 
   * @param term <tt>String</tt> of the search term to be queried
   * @param location <tt>String</tt> of the location
   * @return <tt>String</tt> JSON Response
   */
  public String searchForBusinessesByLocation(String term, String location) {
    OAuthRequest request = createOAuthRequest(SEARCH_PATH);
    request.addQuerystringParameter("term", term);
    request.addQuerystringParameter("location", location);
    request.addQuerystringParameter("limit", String.valueOf(LIMIT));
    return sendRequestAndGetResponse(request);
  }

  /**
   * Creates and sends a request to the Business API by business ID.
   * <p>
   * See <a href="http://www.yelp.com/developers/documentation/v2/business">Yelp Business API V2</a>
   * for more info.
   * 
   * @param businessID <tt>String</tt> business ID of the requested business
   * @return <tt>String</tt> JSON Response
   */
  public String searchByBusinessId(String businessID) {
    OAuthRequest request = createOAuthRequest(BUSINESS_PATH + "/" + businessID);
    return sendRequestAndGetResponse(request);
  }

  /**
   * Creates and returns an {@link OAuthRequest} based on the API endpoint specified.
   * 
   * @param path API endpoint to be queried
   * @return <tt>OAuthRequest</tt>
   */
  private OAuthRequest createOAuthRequest(String path) {
    OAuthRequest request = new OAuthRequest(Verb.GET, "http://" + API_HOST + path);
    return request;
  }

  /**
   * Sends an {@link OAuthRequest} and returns the {@link Response} body.
   * 
   * @param request {@link OAuthRequest} corresponding to the API request
   * @return <tt>String</tt> body of API response
   */
  private String sendRequestAndGetResponse(OAuthRequest request) {
    System.out.println("Querying " + request.getCompleteUrl() + " ...");
    this.service.signRequest(this.accessToken, request);
    Response response = request.send();
    return response.getBody();
  }

  /**
   * Queries the Search API based on the command line arguments and takes the first result to query
   * the Business API.
   * 
   * @param yelpApi <tt>YelpAPI</tt> service instance
   * @param yelpApiCli <tt>YelpAPICLI</tt> command line arguments
   */
  
  private static ArrayList<Restaurant> queryAPI(YelpAPI yelpApi) {
    String searchResponseJSON =
        yelpApi.searchForBusinessesByLocation(TERM, LOCATION);

    	//JSONParser parser = new JSONParser();
      JSONObject response = new JSONObject(searchResponseJSON);

//ARRAY PART
    JSONArray businesses = (JSONArray) response.get("businesses");
    
    ArrayList<Restaurant> resultList = new ArrayList<Restaurant> ();
    
    for(int i=0; i<LIMIT; i++) {
    	JSONObject thisBusiness = (JSONObject) businesses.get(i);
    	String thisBusinessID = thisBusiness.get("id").toString();
    	//System.out.println(String.format(
    	//		"%s businesses found, querying business info for the top result \"%s\" ...",
    	//		businesses.length(), thisBusinessID));

    	// Select the first business and display business details
    	String businessResponseJSON = yelpApi.searchByBusinessId(thisBusinessID.toString());
    	System.out.println(String.format("Result for business \"%s\" found:", thisBusinessID));
    	JSONObject obj = new JSONObject(businessResponseJSON);
    	
    	Restaurant newRes = new Restaurant();
    		newRes.id = obj.getString("id");
    		newRes.displayPhone = obj.getString("display_phone");
    		newRes.rating = obj.getDouble("rating");
    		newRes.url = obj.getString("url");
    		newRes.phone = Double.parseDouble(obj.getString("phone"));
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
    		
    		resultList.add(newRes);
    }
    for (int i=0; i<resultList.size(); i++) {
    	System.out.println(resultList.get(i).id + "" + resultList.get(i).imageUrl);
    }
    return resultList;
  }

/**
   * Command-line interface for the sample Yelp API runner.
   *//*
  private static class YelpAPICLI {
    @Parameter(names = {"-q", "--term"}, description = "Search Query Term")
    public String term = DEFAULT_TERM;

    @Parameter(names = {"-l", "--location"}, description = "Location to be Queried")
    public String location = DEFAULT_LOCATION;
  }
*/
  /**
   * Main entry for sample Yelp API requests.
   * <p>
   * After entering your OAuth credentials, execute <tt><b>run.sh</b></tt> to run this example.
   */
  public static void main(String[] args) {
    //YelpAPICLI yelpApiCli = new YelpAPICLI();
    //new JCommander(yelpApiCli, args);

    YelpAPI yelpApi = new YelpAPI("Ramen", "Los Angeles, CA", 10);
    queryAPI(yelpApi);
  }
}