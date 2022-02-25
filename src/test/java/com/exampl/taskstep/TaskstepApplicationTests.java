package com.exampl.taskstep;

import com.exampl.taskstep.controllers.MainController;
import com.exampl.taskstep.repos.ProxyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
class TaskstepApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProxyRepository proxyRepository;

	@Autowired
	private MainController controller;

	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}
}
