package com.yangbingdong.springbootdatajpa.edl.application.listener;

import com.yangbingdong.springbootdatajpa.domain.repository.UserJpaRepository;
import com.yangbingdong.springbootdatajpa.domain.root.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author ybd
 * @date 19-1-8
 * @contact yangbingdong1994@gmail.com
 */
@Component
public class InitDataListener implements ApplicationListener<ApplicationStartedEvent> {
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private UserJpaRepository userRepository;

	@Async
	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {
//		saveUser();
	}

	private void saveUser() {
		userRepository.save(new User().setName("ybd").setEmail("yba.com"));
		userRepository.save(new User().setName("ybd").setEmail("yangbingdong1.com"));
		userRepository.save(new User().setName("ybd1").setEmail("yangbingdong2.com"));
		userRepository.save(new User().setName("ybd2").setEmail("yangbingdong3.com"));
		userRepository.save(new User().setName("yqy").setEmail("yqy.com").setCreateTime(LocalDateTime.parse("2018-10-10 10:10:10", DATE_TIME_FORMATTER)));
	}

}
