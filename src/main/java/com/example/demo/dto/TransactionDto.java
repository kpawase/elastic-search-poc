package com.example.demo.dto;


public class TransactionDto {
	private String street;
	private String city;
	private int zip;
	private String state;
	private int beds;
	private int baths;
	private int sq__ft;
	private String type;
	private String sale_date;
	private int price;
	private double latitude;
	private double longitude;

	public TransactionDto() {
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getZip() {
		return zip;
	}

	public void setZip(int zip) {
		this.zip = zip;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getBeds() {
		return beds;
	}

	public void setBeds(int beds) {
		this.beds = beds;
	}

	public int getBaths() {
		return baths;
	}

	public void setBaths(int baths) {
		this.baths = baths;
	}

	public int getSq__ft() {
		return sq__ft;
	}

	public void setSq__ft(int sq__ft) {
		this.sq__ft = sq__ft;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSale_date() {
		return sale_date;
	}

	public void setSale_date(String sale_date) {
		this.sale_date = sale_date;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return "RealTransactionDto [street=" + street + ", city=" + city + ", zip=" + zip + ", state=" + state
				+ ", beds=" + beds + ", baths=" + baths + ", sq__ft=" + sq__ft + ", type=" + type + ", sale_date="
				+ sale_date + ", price=" + price + ", latitude=" + latitude + ", longitude=" + longitude + "]";
	}

	
}

