package com.springboot.online_exam_portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;

@SpringBootApplication
public class
ExamPortalApplication {

	public static void main(String[] args) {


		ConfigurableApplicationContext context =
				SpringApplication.run(ExamPortalApplication.class, args);

	}
}
