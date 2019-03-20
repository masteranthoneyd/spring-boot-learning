package com.yangbingdong.redis.util;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ybd
 * @date 18-9-28
 * @contact yangbingdong1994@gmail.com
 */
public class ProtostuffSerializer implements Serializer {
	private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();
	private static Objenesis objenesis = new ObjenesisStd(true);

	@SuppressWarnings("unchecked")
	private static <T> Schema<T> getSchema(Class<T> cls) {
		return (Schema<T>) cachedSchema.computeIfAbsent(cls, RuntimeSchema::createFrom);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> byte[] serialize(T obj) {
		LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
		try {
			Class<T> cls = (Class<T>) obj.getClass();
			return ProtostuffIOUtil.toByteArray(obj, getSchema(cls), buffer);
		} finally {
			buffer.clear();
		}
	}

	@Override
	public <T> T deserialize(byte[] data, Class<T> cls) {
		T message = objenesis.newInstance(cls);
		ProtostuffIOUtil.mergeFrom(data, message, getSchema(cls));
		return message;
	}

}
