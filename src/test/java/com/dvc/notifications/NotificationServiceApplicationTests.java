package com.dvc.notifications;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.dvc.notifications.config.TestConfig;

@SpringBootTest(classes = {NotificationServiceApplication.class, TestConfig.class})
@ActiveProfiles("test")
class NotificationServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
