package br.kaiofprates.poc_mediator;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "aws.sqs.enabled=false"
})
class PocMediatorApplicationTests {

	@Test
	void contextLoads() {
	}

}
