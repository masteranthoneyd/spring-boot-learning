package com.yangbingdong.docker.domain.core.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ybd
 * @date 18-4-23
 * @contact yangbingdong1994@gmail.com
 */
@Data
@Accessors(chain = true)
public class ReqReceiveData {
	private String name;
	private Object value;
	private String type;

	public ReqReceiveData setJsonValue(Object value) {
		setValue(JSONObject.toJSON(value));
		return this;
	}
}
