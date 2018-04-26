package com.yangbingdong.docker.pubsub.disruptor.event;

import com.yangbingdong.docker.domain.core.root.AccessLog;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ybd
 * @date 18-4-25
 * @contact yangbingdong1994@gmail.com
 */
@Data
@Accessors(chain = true)
public class AopLogEvent implements DisruptorEvent {
	private AccessLog accessLog;

	@Override
	public void clean() {
		this.accessLog = null;
	}
}
