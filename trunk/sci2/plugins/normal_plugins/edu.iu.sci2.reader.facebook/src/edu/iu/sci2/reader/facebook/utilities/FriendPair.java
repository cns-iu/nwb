package edu.iu.sci2.reader.facebook.utilities;

import java.util.ArrayList;

public class FriendPair {

	private String name1;
	private String name2;
	private ArrayList<String> commonEventList;

	private Long numOfMutualFriends;

	public FriendPair(String name1, String name2) {
		this.name1 = name1;
		this.name2 = name2;
		this.commonEventList = new ArrayList<String>();
		this.numOfMutualFriends = (long) 0;
	}

	public String getName1() {
		return name1;
	}

	public void setName1(String name1) {
		this.name1 = name1;
	}

	public String getName2() {
		return name2;
	}

	public void setName2(String name2) {
		this.name2 = name2;
	}

	public void setCommonEvent(String commonEvent) {
		this.commonEventList.add(commonEvent);
	}

	public String getCommonEventList() {
		String listString = "";

		for (String s : commonEventList) {
			listString += s + "|";
		}

		return listString;
	}

	public Integer getNumberOfCommonEvents() {
		return commonEventList.size();
	}

	public Long getNumOfMutualFriends() {
		return numOfMutualFriends;
	}

	public void setNumOfMutualFriends(Long numOfMutualFriends) {
		this.numOfMutualFriends = numOfMutualFriends;
	}
}
