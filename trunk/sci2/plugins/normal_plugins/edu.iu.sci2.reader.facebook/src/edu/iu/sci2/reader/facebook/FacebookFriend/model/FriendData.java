package edu.iu.sci2.reader.facebook.FacebookFriend.model;

import java.util.ArrayList;
import java.util.List;

public class FriendData {
	private String name;
	private Long id;
	private String interest;
	private String status;
	private String homeTownLocation;
	private String currentLocation;
	private String religion;
	private String politicalView;
	private String birthday;

	private String relationShipStatus;
	private String gender;
	private List<String> events;
	
	public FriendData(Long id){
		   this.id= id;
		   this.name = "";
		   this.interest = "";
		   this.status ="";
		   this.homeTownLocation="";
		   this.religion = "";
		   this.politicalView ="";
		   this.birthday ="";
		   this.relationShipStatus ="";
		   this.gender = "";
		   this.events = new ArrayList<String>();
	   }
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getHomeTownLocation() {
		return homeTownLocation;
	}

	public void setHomeTownLocation(String homeTownLocation) {
		this.homeTownLocation = homeTownLocation;
	}

	public String getReligion() {
		return religion;
	}

	public void setReligion(String religion) {
		this.religion = religion;
	}

	public String getPoliticalView() {
		return politicalView;
	}

	public void setPoliticalView(String politicalView) {
		this.politicalView = politicalView;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getRelationShipStatus() {
		return relationShipStatus;
	}

	public void setRelationShipStatus(String relationShipStatus) {
		this.relationShipStatus = relationShipStatus;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public List<String> getEventList() {
		return events;
	}
	
	public void addEvent(String event) {
		 events.add(event);
	}

	public void setEvents(List<String> events) {
		this.events = events;
	}   
	
	public String getCurrentLocation() {
		return currentLocation;
	}
	public void setCurrentLocation(String currentLocation) {
		this.currentLocation = currentLocation;
	}
}
