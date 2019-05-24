package app.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.model.Official;
import app.utility.ApiUtility;

/**
 * Main controller class that reacts to front-end POST requests
 * 
 * @author matt
 */
@RestController
public class MainController {
	
	@Autowired
	ApiUtility apiUtility;

	/**
	 * This endpoint is hit when a user searches with an address. It responds with a list of Officials using the ApiUtility class.
	 * 
	 * @param address
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@CrossOrigin(origins = "http://localhost:3000")
	@RequestMapping(value = "/address")
	public @ResponseBody List<Official> addressAPI(@RequestParam String address) throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> queryResult = apiUtility.retrieveRepresentativeInfoByAddress(address);
		
		List<String> officialsNames = apiUtility.getOfficialsNames(queryResult);
		Map<String, List<Integer>> offices = apiUtility.getOffices(queryResult);

		List<Official> listOfOfficials = apiUtility.createListOfOfficials(officialsNames, offices);
		return listOfOfficials;
	}
}
