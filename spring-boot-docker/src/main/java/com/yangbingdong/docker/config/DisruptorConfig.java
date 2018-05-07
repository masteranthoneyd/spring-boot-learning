package com.yangbingdong.docker.config;

import com.lmax.disruptor.dsl.ProducerType;
import com.yangbingdong.docker.domain.core.root.AccessLog;
import com.yangbingdong.docker.pubsub.disruptor.event.log.AccessLogEvent;
import com.yangbingdong.springboot.common.utils.disruptor.DefaultDisruptorCommonComponents;
import com.yangbingdong.springboot.common.utils.disruptor.DisruptorEventHandler;
import com.yangbingdong.springboot.common.utils.disruptor.DisruptorPublisher;
import com.yangbingdong.springboot.common.utils.disruptor.DisruptorPublisherBuilderFactory;
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
