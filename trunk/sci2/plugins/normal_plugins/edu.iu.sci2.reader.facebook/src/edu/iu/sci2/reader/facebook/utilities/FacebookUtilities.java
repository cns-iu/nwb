package edu.iu.sci2.reader.facebook.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.osgi.service.log.LogService;

public final class FacebookUtilities {
	public static final int DEFAULT_NUMBER_OF_RETRIES = 3;
	public static final int BUFFER_SIZE = 4096;
	public static final String DownloadHandler = null;

	private FacebookUtilities() {
	}

	// Checks if a token provided by Facebook is valid
	public static boolean isValidToken(String token, LogService logger) throws AlgorithmExecutionException {
		if (!token.isEmpty()) {
			token = "access_token=" + token;
			String data = CallAPI.DetailsAPI(token, logger);
			try {
				new JSONObject(new JSONTokener(data));
			} catch (JSONException e) {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	// Splits a comma separated date string and parses the day, month, and year
	public static String formatDate(String str) {
		String day, month, year;

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("January", "01");
		map.put("February", "02");
		map.put("March", "03");
		map.put("April", "04");
		map.put("May", "05");
		map.put("June", "06");
		map.put("July", "07");
		map.put("August", "08");
		map.put("September", "09");
		map.put("October", "10");
		map.put("November", "11");
		map.put("December", "12");

		if (str.contains(",")) {
			String[] array = str.split(",");
			String monthDay = array[0];
			String[] mDArray = monthDay.split(" ");

			month = map.get(mDArray[0]);
			year = array[1];
			day = mDArray[1];
			if (day.length() == 1)
				day = "0" + day;
			return (year + "-" + month + "-" + day);
		} else {
			String[] mDArray = str.split(" ");
			month = map.get(mDArray[0]);
			day = mDArray[1];
			if (day.length() == 1)
				day = "0" + day;
			return (month + "-" + day);

		}
	}

	// retrieves the name of the authenticated user
	public static String getMyName(String token, LogService logger) throws JSONException, AlgorithmExecutionException {
		String data = CallAPI.DetailsAPI(token, logger);
		JSONObject obj = new JSONObject(new JSONTokener(data));
		return obj.getString("name");
	}

	// retrieves the user id of the authenticated user
	public static String getMyId(String token, LogService logger) throws JSONException, AlgorithmExecutionException {
		String data = CallAPI.DetailsAPI(token, logger);
		JSONObject obj = new JSONObject(new JSONTokener(data));
		return obj.getString("id");
	}

	// asks for the name, state, country, and zip code of a friend's hometown
	// address. Then comma separates them and returns as one large string
	public static String getFriendsHometown(JSONObject currentResult)
			throws JSONException {
		String ht = null;
		if (!currentResult.isNull("hometown_location")) {
			JSONObject htObj = currentResult.getJSONObject("hometown_location");
			String name = "";
			String state = "";
			String country = "";
			String zip = "";
			if (!htObj.isNull("name")) {
				name = htObj.getString("name") + ",";
			}
			if (!htObj.isNull("state")) {
				state = htObj.getString("state") + ",";
			}
			if (!htObj.isNull("country")) {
				country = htObj.getString("country") + ",";
			}
			if (!htObj.isNull("zip")) {
				zip = htObj.getString("zip");
			}
			ht = removeDuplicates(name + state + country + zip);
		}
		return ht;
	}

	// checks for duplicate entries to remove incorrectly entered data, or
	// handles duplicate city/state names
	private static String removeDuplicates(String str) {
		HashSet<String> hset = new HashSet<String>();
		String[] strList = str.split(",");
		ArrayList<String> list = new ArrayList<String>();
		for (String val : strList) {
			val = val.trim();
			if (val.isEmpty())
				continue;
			if (!hset.contains(val)) {
				hset.add(val);
				list.add(val);
			}
		}
		String[] outStringList = list.toArray(new String[0]);
		String outString = Arrays.toString(outStringList).replace("[", "")
				.replace("]", "");
		return outString;
	}

	// asks for the name, state, country, and zip code of a friend's current
	// residence address. Then comma separates them and returns as one string
	// then removes any duplicate entries 
	public static String getFriendsCurrentLocation(JSONObject currentResult)
			throws JSONException {
		String cl = null;
		if (!currentResult.isNull("current_location")) {
			JSONObject htObj = currentResult.getJSONObject("current_location");
			String state = "";
			String name = "";
			String city = "";
			String country = "";
			String zip = "";
			if (!htObj.isNull("name")) {
				name = htObj.getString("name") + ",";
			}
			if (!htObj.isNull("city")) {
				city = htObj.getString("city") + ",";
			}
			if (!htObj.isNull("state")) {
				state = htObj.getString("state") + ",";
			}
			if (!htObj.isNull("country")) {
				country = htObj.getString("country") + ",";
			}
			if (!htObj.isNull("zip")) {
				zip = htObj.getString("zip");
			}
			cl = removeDuplicates(name + city + state + country + zip);
		}
		return cl;
	}

	//gets the latest status update from a user
	public static String getStatus(JSONObject currentResult)
			throws JSONException {
		String message = null;
		if (!currentResult.isNull("status")) {
			JSONObject jsonObj = currentResult.getJSONObject("status");
			message = jsonObj.getString("message");
		}
		return message;
	}

	//gets a list of a friend's interests, | separates them
	public static String getInterests(JSONObject currentResult)
			throws JSONException {
		String interests = null;
		if (!currentResult.isNull("interests")) {
			interests = currentResult.getString("interests").replace(",", "|");
		}
		return interests;
	}

	//retrieves a friend's birthday and uses formatDate to standardize it
	public static String getBirthday(JSONObject currentResult)
			throws JSONException {
		String birthday = null;
		if (!currentResult.isNull("birthday")) {
			birthday = formatDate(currentResult.getString("birthday"));
		}
		return birthday;
	}

	//gets friend's religious affiliation
	public static String getReligion(JSONObject currentResult)
			throws JSONException {
		String religion = null;
		if (!currentResult.isNull("religion")) {
			religion = currentResult.getString("religion");
		}
		return religion;
	}

	//gets friend's political affiliation
	public static String getPolitical(JSONObject currentResult)
			throws JSONException {
		String political = null;
		if (!currentResult.isNull("political")) {
			political = currentResult.getString("political");
		}
		return political;
	}

	//gets friend's relationship status
	public static String getRelationship(JSONObject currentResult)
			throws JSONException {
		String relationship = null;
		if (!currentResult.isNull("relationship_status")) {
			relationship = currentResult.getString("relationship_status");
		}
		return relationship;
	}
}
