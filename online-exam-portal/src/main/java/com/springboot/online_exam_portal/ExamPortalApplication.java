package com.springboot.online_exam_portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;

@SpringBootApplication
public class ExamPortalApplication {

	public static void main(String[] args) {

		// ── START ──────────────────────────────────────────────────────────────
		// Boots the Spring context + embedded Tomcat on port 9090.
		// Server stays alive as long as this process runs.
		// Stop in IDE / press Ctrl+C in terminal → server shuts down immediately.
		ConfigurableApplicationContext context =
				SpringApplication.run(ExamPortalApplication.class, args);

		// ── SERVER READY ───────────────────────────────────────────────────────
		// This message appears once Tomcat is fully up and all APIs are reachable.
		System.out.println("\n╔══════════════════════════════════════════════════════╗");
		System.out.println(  "║   ✅  Online Exam Portal  —  SERVER IS RUNNING       ║");
		System.out.println(  "║                                                      ║");
		System.out.println(  "║   Base URL  :  http://localhost:9090                 ║");
		System.out.println(  "║   API Root  :  http://localhost:9090/api/exams       ║");
		System.out.println(  "║                                                      ║");
		System.out.println(  "║   ⛔  Stop the server (IDE stop / Ctrl+C)           ║");
		System.out.println(  "║      → Postman & browser will stop responding        ║");
		System.out.println(  "╚══════════════════════════════════════════════════════╝\n");

		// ── SHUTDOWN LISTENER ──────────────────────────────────────────────────
		// Fires only when the Spring ApplicationContext is actually closing
		// (IDE stop button, Ctrl+C).  Prints a clear "server is off" message.
		context.addApplicationListener(event -> {
			if (event instanceof ContextClosedEvent) {
				System.out.println("\n╔══════════════════════════════════════════════════════╗");
				System.out.println(  "║   ⛔  Online Exam Portal  —  SERVER IS STOPPED       ║");
				System.out.println(  "║                                                      ║");
				System.out.println(  "║   All Postman / browser requests will now fail.      ║");
				System.out.println(  "║   Run ExamPortalApplication again to restart.        ║");
				System.out.println(  "╚══════════════════════════════════════════════════════╝\n");
			}
		});
	}
}
