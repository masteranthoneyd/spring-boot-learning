package com.yangbingdong.mail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("163")
public class SpringBootMailApplicationTests {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private TemplateEngine templateEngine;


	@Test
	public void contextLoads() {
	}

	@Test
	public void testSendSimple() {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("730493388@qq.com");
		message.setTo("masteranthoneyd@163.com");
		message.setSubject("标题：测试标题");
		message.setText("测试内容部份");
		javaMailSender.send(message);
	}


	@Test
	public void testSendTemplateByQQ() throws MessagingException {
		Context context = new Context();
		context.setVariable("id", "/archives");
		String email = templateEngine.process("email", context);

		MimeMessage message = javaMailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setFrom("730493388@qq.com");
		helper.setTo("masteranthoneyd@163.com");
		helper.setSubject("标题：测试标题");
		helper.setText(email, true);
		javaMailSender.send(message);
	}

	@Test
	public void testSendTemplateBy163() throws MessagingException {
		Context context = new Context();
		context.setVariable("id", "/archives");
		String email = templateEngine.process("email", context);
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setFrom("masteranthoneyd@163.com");
		helper.setTo("730493388@qq.com");
		helper.setSubject("标题：测试标题");
		helper.setText(email, true);
		javaMailSender.send(message);
	}


}

