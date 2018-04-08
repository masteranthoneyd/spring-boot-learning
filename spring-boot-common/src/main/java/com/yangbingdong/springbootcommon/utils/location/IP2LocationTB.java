package com.yangbingdong.springbootcommon.utils.location;

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
public class IP2LocationTB implements IP2Location {

	@Override
	public String ip2Location(String ip) {
		StringBuilder result = new StringBuilder();
		try {
			URLConnection connection = new URL("http://ip.taobao.com/service/getIpInfo.php?ip=" + ip).openConnection();
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
		TBResponse response = JSON.parseObject(result.toString(), TBResponse.class);
		StringBuilder location = new StringBuilder();
		if ("0".equals(response.code)) {
			Map<String, String> data = response.data;
			location.append(data.get("country")).append(data.get("region")).append(data.get("city"));
		}
		return location.toString();
	}

	@Override
	public void addIP2Location(IP2Location ip2Location) {
		throw new UnsupportedOperationException();
	}

	private static class TBResponse {
		public String code;
		public Map<String, String> data;

		public String getCode() {
			return code;
		}

		public TBResponse setCode(String code) {
			this.code = code;
			return this;
		}

		public Map<String, String> getData() {
			return data;
		}

		public TBResponse setData(Map<String, String> data) {
			this.data = data;
			return this;
		}
	}

}
