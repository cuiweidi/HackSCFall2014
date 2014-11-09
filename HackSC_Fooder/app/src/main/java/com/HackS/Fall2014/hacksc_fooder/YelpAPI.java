package com.HackS.Fall2014.hacksc_fooder;

import android.os.AsyncTask;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import java.lang.Exception;

public class YelpAPI extends AsyncTask<String, Void, Void> {
    private static final String API_HOST = "api.yelp.com";
    ArrayList<Restaurant> resultList;
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
    private static final String TOKEN = "IGbQbE1wUX4W_Z967zRca1c6Pn0RwWYx";
    private static final String TOKEN_SECRET = "kMnLlm0Ce5Jr6W5JUx9-KcXt_-M";

    OAuthService service;
    Token accessToken;
    public YelpAPI (String term, String location, int limit) {
        this.TERM = term;
        this.LOCATION = location;
        this.LIMIT = limit;
        resultList = new ArrayList<Restaurant> ();
    }

    protected Void doInBackground(String... terms) {
        //expListView = (ExpandableListView) findViewById(R.id.lvExp);


        service = new ServiceBuilder().provider(YelpApi2.class).apiKey(CONSUMER_KEY)
                .apiSecret(CONSUMER_SECRET).build();
        accessToken = new Token(TOKEN, TOKEN_SECRET);
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
        request.addQuerystringParameter("location", "Los Angeles, CA");
        request.addQuerystringParameter("term", "Ramen");
        request.addQuerystringParameter("limit", String.valueOf(10));
        service.signRequest(accessToken, request);
        Response response = request.send();
        String rawData = response.getBody();
//System.out.println(rawData);
        try {
            JSONObject json = new JSONObject(rawData);
            JSONArray businesses = json.getJSONArray("businesses");
           // System.out.println(businesses.length());

            for (int i = 0; i < businesses.length(); i++) {
                JSONObject thisBusiness = (JSONObject) businesses.get(i);
                //System.out.println(thisBusiness.getString("id"));
                //System.out.println(thisBusiness.has("error"));
                //if (thisBusiness.has("error")) continue;
                String thisBusinessID = thisBusiness.get("id").toString();
                //System.out.println(thisBusinessID);
                // Select the first business and display business details

                OAuthRequest request2 = new OAuthRequest(Verb.GET, "http://" + API_HOST + "/v2/business/"  + thisBusinessID);
                System.out.println("not exception yet");
                this.service.signRequest(accessToken, request2);
                //System.out.println("not exception yet");
                //System.out.println("not exception yet");
                Response response2 = request2.send();
                String businessResponseJSON = response2.getBody();
                //System.out.println("not exception yet");
                JSONObject obj = new JSONObject(businessResponseJSON);

                if (obj.has("error")) continue;
                System.out.println(businessResponseJSON);
                Restaurant newRes = new Restaurant();
                newRes.id = obj.getString("id");
                //System.out.println("not exception yet");
                if (!obj.has("display_phone")) {
                    newRes.displayPhone = "";
                } else newRes.displayPhone = obj.getString("display_phone");
                newRes.rating = obj.getDouble("rating");
                newRes.name=obj.getString("name");
                newRes.url = obj.getString("url");
                if (!obj.has("phone")) {
                    newRes.phone = 0;
                } else newRes.phone = Double.parseDouble(obj.getString("phone"));
                JSONObject location = (JSONObject) obj.get("location");
                String city = location.getString("city");
                String state = location.getString("state_code");
                newRes.cityState = city + ", " + state;
                JSONArray addresses = (JSONArray) location.get("address");
                newRes.address = (String) addresses.get(0); //address only, no city and no state
                newRes.zipCode = location.getString("postal_code");
                newRes.country = location.getString("country_code");
                String imageOriUrl = obj.getString("image_url");
                String[] tempSplit = imageOriUrl.split("/");
                tempSplit[tempSplit.length - 1] = "o.jpg";
                String imageNewUrl = "";
                for (int j = 0; j < tempSplit.length; j++) {
                    if (j == 0) {
                        imageNewUrl = imageNewUrl + tempSplit[j] + "/";
                    } else if (j == tempSplit.length - 1) {
                        imageNewUrl = imageNewUrl + tempSplit[j];
                    } else {
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
                //System.out.println("not exception yet");
            }
            for (int i = 0; i < resultList.size(); i++) {
                System.out.println(resultList.get(i).id + "" + resultList.get(i).imageUrl);
                System.out.println(resultList.get(i).address + ", " + resultList.get(i).cityState);
                System.out.println(resultList.get(i).mapUrl);
            }
        } catch (Exception E) {
            //System.out.println("EXCEPTION");
            resultList = null;
        }
        return null;
    }

    public ArrayList<Restaurant> getArrayList() {
        return resultList;
    }
}
