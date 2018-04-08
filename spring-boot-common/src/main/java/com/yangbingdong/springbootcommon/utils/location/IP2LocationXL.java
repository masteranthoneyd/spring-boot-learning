package com.yangbingdong.springbootcommon.utils.location;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

/**
 * @author lan
 * @since 2014-11-15
 */
public class IP2LocationXL implements IP2Location {
	@Override
	public String ip2Location(String ip) {
		StringBuilder result = new StringBuilder();
		try {
			URLConnection connection = new URL("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js&ip=" + ip).openConnection();
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
		String json = result.substring(result.indexOf("=") + 1, result.length() - 1);
		LocationInfo locationInfo = JSON.parseObject(json, LocationInfo.class);
		return locationInfo.getCountry() + locationInfo.getProvince() + locationInfo.getCity();
	}

	@Override
	public void addIP2Location(IP2Location ip2Location) {
		throw new UnsupportedOperationException();
	}

	private static class LocationInfo {
		String country;
		String province;
		String city;

		public String getCountry() {
			return defaultIfNull(country, "");
		}

		public LocationInfo setCountry(String country) {
			this.country = country;
			return this;
		}

		public String getProvince() {
			return defaultIfNull(province, "");
		}

		public LocationInfo setProvince(String province) {
			this.province = province;
			return this;
		}

		public String getCity() {
			return defaultIfNull(city, "");
		}

		public LocationInfo setCity(String city) {
			this.city = city;
			return this;
		}
	}
}
