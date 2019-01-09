package com.yangbingdong.springbootdatajpa.edl.application.listener;

import com.yangbingdong.springbootdatajpa.domain.repository.PersonRepository;
import com.yangbingdong.springbootdatajpa.domain.repository.UserRepository;
import com.yangbingdong.springbootdatajpa.domain.root.Person;
import com.yangbingdong.springbootdatajpa.domain.root.User;
import com.yangbingdong.springbootdatajpa.domain.vo.Sex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.IntStream;

/**
 * @author ybd
 * @date 19-1-8
 * @contact yangbingdong1994@gmail.com
 */
@Component
public class InitDataListener implements ApplicationListener<ApplicationStartedEvent> {
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private UserRepository userRepository;

	@Async
	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {
		IntStream.range(0,10)
				 .forEach(this::savePerson);
		saveUser();
	}

	private void saveUser() {
		userRepository.save(new User().setName("ybd").setEmail("yba.com"));
		userRepository.save(new User().setName("yqy").setEmail("yqy.com").setCreateTime(LocalDateTime.parse("2018-10-10 10:10:10", DATE_TIME_FORMATTER)));
	}

	@Transactional(rollbackFor = Exception.class)
	public void savePerson(int i) {
		personRepository.save(new Person().setName(String.valueOf(i))
										  .setSex(i % 2 == 0 ? Sex.MALE : Sex.FEMALE));
	}
}
