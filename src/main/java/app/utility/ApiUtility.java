package app.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.model.Official;

/**
 * Utility class that calls the google civics API to find elected officials.
 * 
 * @author matt
 */
@Component
public class ApiUtility {
	
	/**
	 * Makes GET request to google API to capture information of officials given a specified address.
	 * @param address
	 * @return
	 * @throws MalformedURLException 
	 * @throws ProtocolException 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public Map<String, Object> retrieveRepresentativeInfoByAddress(String address) throws IOException {
		address = address.replace(" ", "%20");
		URL obj = new URL("https://www.googleapis.com/civicinfo/v2/representatives?address=" + address + 
				"&includeOffices=true&key=AIzaSyC8dj7swOUYCkexey7aNhwROCbEX2UGQUU");
		
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		if (con.getResponseCode() == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			ObjectMapper mapper = new ObjectMapper();
			
			@SuppressWarnings("unchecked")
			Map<String,Object> apiRetrievalResult = mapper.readValue(response.toString(), Map.class);
			return apiRetrievalResult;
		} else {
			// Do something . . .
			return null;
		}
	}

	/**
	 * Makes GET request to google API to capture information of officials given a specified address.
	 * @param address
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
//	public Map<String, Object> retrieveRepresentativeInfoByAddress(String address) throws JsonParseException, JsonMappingException, IOException {
//		//address = address.replace(" ", "%20");
//	    String uri = "https://www.googleapis.com/civicinfo/v2/representatives";
//	    this.sendGET();
//	    
//	    UriComponentsBuilder builder = UriComponentsBuilder
//	    	    .fromUriString(uri)
//	    	    // Add query parameter
//	    	    .queryParam("address", address)
//	    	    .queryParam("includeOffices", "true")
//	    	    .queryParam("key", "AIzaSyC8dj7swOUYCkexey7aNhwROCbEX2UGQUU");
//	    
//	    RestTemplate restTemplate = new RestTemplate();
//	    String response = restTemplate.getForObject(builder.toUriString(), String.class);
//	    ObjectMapper mapper = new ObjectMapper();
//	    
//	    @SuppressWarnings("unchecked")
//		Map<String,Object> apiRetrievalResult = mapper.readValue(response, Map.class);
//	    return apiRetrievalResult;
//	}
	
	/**
	 * Combines the results of getOffices and getOfficialsNames to create and return a list of Officials.
	 * 
	 * @param officialsNames
	 * @param offices
	 * @return
	 */
	public List<Official> createListOfOfficials(List<String> officialsNames, Map<String, List<Integer>> offices) {
		List<Official> officials = new ArrayList<>();
		for (int i = 0; i < officialsNames.size(); i++) {
			for (Map.Entry<String, List<Integer>> entry : offices.entrySet()) {
				if (entry.getValue().contains(i)) {
					officials.add(new Official(officialsNames.get(i), entry.getKey()));
				}
			}
		}
		
		return officials;
	}
	
	/**
	 * Captures and returns a map of the offices that the officials inhabit.
	 * (key = office name, value = list of indices that correlate to the indices
	 * of the officials names captured by the getOfficialsNames method)
	 * 
	 * @param queryResult
	 */
	public Map<String, List<Integer>> getOffices(Map<String, Object> queryResult) {
		@SuppressWarnings("unchecked")
		ArrayList<LinkedHashMap<Object, Object>> offices = (ArrayList<LinkedHashMap<Object, Object>>) queryResult.get("offices");
		
		// Removing president and vice president
		offices.remove(0);
		offices.remove(0);
		
		Map<String, List<Integer>> officesMap = new HashMap<>();
		for (LinkedHashMap<Object, Object> map : offices) {
			@SuppressWarnings("unchecked")
			List<Integer> officialIndices = (List<Integer>) map.get("officialIndices");
			String officeName = (String) map.get("name");
			officesMap.put(officeName, officialIndices);
		}
		
		return officesMap;
	}
	
	/**
	 * Captures and returns the list of elected officials names from the google API response JSON.
	 *  
	 * @param queryResult
	 * @return
	 */
	public List<String> getOfficialsNames(Map<String, Object> queryResult) {
		@SuppressWarnings("unchecked")
		ArrayList<LinkedHashMap<Object, Object>> officials = (ArrayList<LinkedHashMap<Object, Object>>) queryResult.get("officials");
		
		List<String> officialsNames = new ArrayList<>();
		for (LinkedHashMap<Object, Object> map : officials) {
			officialsNames.add((String) map.get("name"));
		}
		
		return officialsNames;
	}
}
