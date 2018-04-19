package com.yangbingdong.springboot.common.utils.location;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author lanhuidong
 * @since 2017-09-05
 */
public class IP2LocationFreeGeo implements IP2Location {

	@Override
	public String ip2Location(String ip) {
		StringBuilder result = new StringBuilder();
		try {
			URLConnection connection = new URL("http://freegeoip.net/json/" + ip).openConnection();
			connection.getInputStream();
			connection.setConnectTimeout(5000);
			try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
				String s;
				while ((s = br.readLine()) != null) {
					result.append(s);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		FreeGeoResponse response = JSON.parseObject(result.toString(), FreeGeoResponse.class);
		return response.country_name + " " + response.region_name + " " + response.city;
	}

	@Override
	public void addIP2Location(IP2Location ip2Location) {
		throw new UnsupportedOperationException();
	}

	private class FreeGeoResponse {
		public String country_name;
		public String region_name;
		public String city;

		public String getCountry_name() {
			return country_name;
		}

		public FreeGeoResponse setCountry_name(String country_name) {
			this.country_name = country_name;
			return this;
		}

		public String getRegion_name() {
			return region_name;
		}

		public FreeGeoResponse setRegion_name(String region_name) {
			this.region_name = region_name;
			return this;
		}

		public String getCity() {
			return city;
		}

		public FreeGeoResponse setCity(String city) {
			this.city = city;
			return this;
		}
	}

}
