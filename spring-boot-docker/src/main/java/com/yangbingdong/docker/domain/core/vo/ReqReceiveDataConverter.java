package com.yangbingdong.docker.domain.core.vo;

import javax.persistence.AttributeConverter;
import java.util.List;

import static com.alibaba.fastjson.JSON.parseArray;
import static com.alibaba.fastjson.JSON.toJSONString;

/**
 * @author ybd
 * @date 18-4-23
 * @contact yangbingdong1994@gmail.com
 */
public class ReqReceiveDataConverter implements AttributeConverter<List<ReqReceiveData>, String> {
	@Override
	public String convertToDatabaseColumn(List<ReqReceiveData> attribute) {
		return toJSONString(attribute);
	}

	@Override
	public List<ReqReceiveData> convertToEntityAttribute(String dbData) {
		return parseArray(dbData, ReqReceiveData.class);
	}
}
