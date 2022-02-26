package com.exampl.taskstep;

import com.exampl.taskstep.controllers.MainController;
import com.exampl.taskstep.models.Proxy;
import com.exampl.taskstep.models.ProxyType;
import com.exampl.taskstep.repos.ProxyRepository;
import com.exampl.taskstep.utils.ObjToJson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

/**
 * @date: Feb.25.2022
 * @author: Mihhail Daniljuk
 * @email: daniljukmihhail@gmail.com
 */

@AutoConfigureMockMvc
@WebMvcTest(MainController.class)
public class PostMappingTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProxyRepository proxyRepository;

    private ObjToJson converter = new ObjToJson();

    @Test
    void shouldCreateMockMvc() {
        assertThat(mockMvc).isNotNull();
    }


    @Test
    void postMethodReturnsAddedProxyTest() throws Exception {
        Proxy proxy = new Proxy(0,"Testname", ProxyType.HTTP, "TestHostname", 8080,
                           "TestUsername", "password", true);


        this.mockMvc.perform(MockMvcRequestBuilders.post("/proxies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Testname\",\"type\":\"HTTP\",\"hostname\":\"TestHostname\",\"port\":8080" +
                                ",\"username\":\"TestUsername\",\"password\":\"password\",\"active\":true}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(converter.convToJson(proxy) + " Proxy added"));
        verify(proxyRepository).save(any(Proxy.class));
    }


    @Test
    void postMethodThrowsExceptionsTest() throws  Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/proxies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Testname\",\"type\":\"HTTP\",\"hostname\":\"TestHostname\",\"port\":8080" +
                                ",\"username\":\"TestUsername\",\"password\":\"123\",\"active\":true}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("newProxy.proxy.password: Wrong password"));
    }



}
