package com.example.banking_system.common;

import com.example.banking_system.BankingSystemApplication;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

//@Import({TestcontainersConfiguration.class, Cleaner.class})
@Import(TestcontainersConfiguration.class)
@SpringBootTest(classes = BankingSystemApplication.class)
@Transactional
public abstract class IntegrationTest {
//	@Autowired
//	Cleaner cleaner;

	@AfterEach
	void contextLoads() {
		//temporarily disabled due to not applying cache yet
//		cleaner.clearAllCaches();
	}

}
