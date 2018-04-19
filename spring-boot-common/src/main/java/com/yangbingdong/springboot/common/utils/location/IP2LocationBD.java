package com.yangbingdong.springboot.common.utils.location;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * @author lan
 * @since 2014-11-15
 */
public class IP2LocationBD implements IP2Location {

	private String ak = "kyL9Mp2jKvTBxFWENHq6fp78";

	public void setAk(String ak) {
		this.ak = ak;
	}

	@Override
	public String ip2Location(String ip) {
		StringBuilder result = new StringBuilder();
		try {
			URLConnection connection = new URL("http://api.map.baidu.com/location/ip?ak=" + ak + "&coor=bd09ll&ip=" + ip)
					.openConnection();
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
		BDResponse response = JSON.parseObject(result.toString(), BDResponse.class);
		String location = "";
		if ("0".equals(response.status)) {
			location = response.content.address;
		}
		return location;
	}

	@Override
	public void addIP2Location(IP2Location ip2Location) {
		throw new UnsupportedOperationException();
	}

	private class BDResponse {
		public String address;
		public String status;
		public Content content;

		public class Content {
			public String address;
			public Map<String, String> address_detail;
		}

		public String getAddress() {
			return address;
		}

		public BDResponse setAddress(String address) {
			this.address = address;
			return this;
		}

		public String getStatus() {
			return status;
		}

		public BDResponse setStatus(String status) {
			this.status = status;
			return this;
		}

		public Content getContent() {
			return content;
		}

		public BDResponse setContent(Content content) {
			this.content = content;
			return this;
		}
	}

}
