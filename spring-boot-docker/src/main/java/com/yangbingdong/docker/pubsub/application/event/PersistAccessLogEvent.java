package com.yangbingdong.docker.pubsub.application.event;

import com.yangbingdong.docker.domain.core.root.AccessLog;
import org.springframework.context.ApplicationEvent;

/**
 * @author ybd
 * @date 18-4-20
 * @contact yangbingdong1994@gmail.com
 */
public class PersistAccessLogEvent extends ApplicationEvent {
	private static final long serialVersionUID = -7774157479257156743L;

	public PersistAccessLogEvent(Object source) {
		super(source);
	}

	@Override
	public AccessLog getSource() {
		return (AccessLog) super.getSource();
	}
}
