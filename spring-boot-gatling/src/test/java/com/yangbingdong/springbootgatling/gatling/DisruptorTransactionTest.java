package com.yangbingdong.springbootgatling.gatling;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.yangbingdong.springbootgatling.model.Person;
import com.yangbingdong.springbootgatling.model.UserEvent;
import com.yangbingdong.springbootgatling.model.UserTranslator;
import com.yangbingdong.springbootgatling.repository.PersonsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author ybd
 * @date 18-2-2
 * @contact yangbingdong1994@gmail.com
 */
@RunWith(SpringRunner.class)
//@SpringBootTest
@DataJpaTest
public class DisruptorTransactionTest {

	@Autowired
	private PersonsRepository repository;

	@SuppressWarnings("unchecked")
	@Test
	@Transactional
	@Rollback(false)
	public void testT() throws InterruptedException {
		int bufferSize = 1 << 8;

		Disruptor<UserEvent> disruptor = new Disruptor<>(UserEvent::new, bufferSize, Executors.defaultThreadFactory(), ProducerType.MULTI, new YieldingWaitStrategy());

		disruptor.handleEventsWith((EventHandler<UserEvent>) (event, sequence, endOfBatch) -> {
			if (sequence == 3) {
				throw new IllegalArgumentException("非法！！！！！！！！");
			} else {
				System.out.println("event: " + event);
				repository.save(event.getPerson());
			}
		});

		UserTranslator userTranslator = new UserTranslator();

		disruptor.start();

		new Thread(() -> produce(disruptor, userTranslator, 0, 10)).start();

		TimeUnit.SECONDS.sleep(1);
	}

	private void produce(Disruptor<UserEvent> disruptor, UserTranslator userTranslator, int i, int i2) {
		try {
			RingBuffer<UserEvent> ringBuffer = disruptor.getRingBuffer();
			for (long l = i; l < i2; l++) {
				Person arg0 = new Person();
				arg0.setFirstName(String.valueOf(l));
				ringBuffer.publishEvent(userTranslator, arg0);
				TimeUnit.MILLISECONDS.sleep(20);
			}
		} catch (Exception e) {
			System.out.println("catch error.............");
			e.printStackTrace();
		}
	}
}
