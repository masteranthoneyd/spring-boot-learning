package com.yangbingdong.docker.domain.core.vo;

import com.alibaba.fastjson.JSONArray;

import javax.persistence.AttributeConverter;
import java.util.List;

/**
 * @author ybd
 * @date 18-4-23
 * @contact yangbingdong1994@gmail.com
 */
public class ReqReceiveDataConverter implements AttributeConverter<List<ReqReceiveData>, String> {
	@Override
	public String convertToDatabaseColumn(List<ReqReceiveData> attribute) {
		return JSONArray.toJSONString(attribute);
	}

	@Override
	public List<ReqReceiveData> convertToEntityAttribute(String dbData) {
		return JSONArray.parseArray(dbData, ReqReceiveData.class);
	}
}
