package com.yangbingdong.docker.config;

import com.lmax.disruptor.dsl.ProducerType;
import com.yangbingdong.docker.domain.core.root.AccessLog;
import com.yangbingdong.docker.pubsub.disruptor.core.DefaultDisruptorCommonComponents;
import com.yangbingdong.docker.pubsub.disruptor.core.DisruptorEventHandler;
import com.yangbingdong.docker.pubsub.disruptor.core.DisruptorPublisher;
import com.yangbingdong.docker.pubsub.disruptor.core.DisruptorPublisherBuilderFactory;
import com.yangbingdong.docker.pubsub.disruptor.event.log.AccessLogEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ybd
 * @date 18-5-7
 * @contact yangbingdong1994@gmail.com
 */
@Configuration
public class DisruptorConfig {
	@Resource
	private List<DisruptorEventHandler<AccessLogEvent>> eventHandlers = new ArrayList<>(16);

	@Bean
	public DisruptorPublisher<AccessLog> accessLogEventPublisher() {
		return DisruptorPublisherBuilderFactory.getInstance()
											   .getBuilder(AccessLogEvent.class)
											   .disruptorCommonComponents(new DefaultDisruptorCommonComponents()
													   .setProducerType(ProducerType.MULTI))
											   .disruptorEventHandlers(eventHandlers)
											   .translatorOneArg((event, sequence, accessLog) -> event.setSource(accessLog))
											   .build();
	}
}
