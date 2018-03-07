package com.yangbingdong.springbootdatajpa.config;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.yangbingdong.springbootdatajpa.util.Constant;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;

import java.math.BigInteger;
import java.nio.charset.Charset;

/**
 * @author ybd
 * @date 18-3-7
 * @contact yangbingdong1994@gmail.com
 */
@Configuration
public class WebMvcMessageConvertConfig {
	@Bean
	public HttpMessageConverters fastJsonHttpMessageConverter() {
		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

		SerializeConfig serializeConfig = SerializeConfig.globalInstance;
		serializeConfig.put(BigInteger.class, ToStringSerializer.instance);
		serializeConfig.put(Long.class, ToStringSerializer.instance);
		serializeConfig.put(Long.TYPE, ToStringSerializer.instance);

		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setCharset(Charset.forName(Constant.CHARSET));
		fastJsonConfig.setSerializeConfig(serializeConfig);
//		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
		fastJsonConfig.setDateFormat(Constant.DATE_FORMAT);

		fastConverter.setFastJsonConfig(fastJsonConfig);
		return new HttpMessageConverters((HttpMessageConverter<?>) fastConverter);
	}
}
