package com.exampl.taskstep;

import com.exampl.taskstep.controllers.MainController;
import com.exampl.taskstep.models.Proxy;
import com.exampl.taskstep.models.ProxyType;
import com.exampl.taskstep.repos.ProxyRepository;
import com.exampl.taskstep.utils.StringToJson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

/**
 * @date: Feb.25.2022
 * @author: Mihhail Daniljuk
 * @email: daniljukmihhail@gmail.com
 */

@AutoConfigureMockMvc
@WebMvcTest(MainController.class)
public class GetMappingsTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProxyRepository proxyRepository;

    private StringToJson converter = new StringToJson();

    @Test
    void shouldCreateMockMvc() {
        assertThat(mockMvc).isNotNull();
    }

//    @Test
//    void getEmptyProxyReturnsBadRequest() throws Exception {
//        this.mockMvc.perform(MockMvcRequestBuilders.get("/proxies/1"))
//                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andExpect(MockMvcResultMatchers.content().string("There is no proxy with such id"));
//    }

    @Test
    void getProxyReturnsOkAndProxyTest() throws Exception {
        Proxy proxy = new Proxy(0,"Testname", ProxyType.HTTP, "TestHostname", 8080,
                "TestUsername", "password", true);

        when(proxyRepository.findById(0l)).thenReturn(Optional.of(proxy));

        this.mockMvc.perform(MockMvcRequestBuilders.post("/proxies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Testname\",\"type\":\"HTTP\",\"hostname\":\"TestHostname\",\"port\":8080" +
                        ",\"username\":\"TestUsername\",\"password\":\"password\",\"active\":true}"));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/proxies/0"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(converter.convToJson(proxy)));
    }
}
