package com.yangbingdong.springbootgatling.model;

import com.lmax.disruptor.EventTranslatorOneArg;

/**
 * @author ybd
 * @date 18-2-2
 * @contact yangbingdong1994@gmail.com
 */
public class UserTranslator implements EventTranslatorOneArg<UserEvent, Person> {
	@Override
	public void translateTo(UserEvent event, long sequence, Person person) {
		event.setPerson(person);
	}
}
