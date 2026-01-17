package com.example.banking_system;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@Transactional
public abstract class BankingSystemApplicationTests {
	@Autowired
	Cleaner cleaner;

	@AfterEach
	void contextLoads() {
		//temporarily disabled due to not applying cache yet
//		cleaner.clearAllCaches();
	}

}
