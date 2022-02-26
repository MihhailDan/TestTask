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

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @date: Feb.25.2022
 * @author: Mihhail Daniljuk
 * @email: daniljukmihhail@gmail.com
 */


@AutoConfigureMockMvc
@WebMvcTest(MainController.class)
public class DeleteMappingTest {

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
    void deleteEmptyProxyReturnsBadRequestTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/proxies/{id}", 10))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("There is no proxy with such ID"));
    }

    @Test
    void deleteProxyDeletesTest() throws Exception {
        Proxy proxy = new Proxy(0,"Testname", ProxyType.HTTP, "TestHostname", 8080,
                "TestUsername", "password", true);

        // ?
        when(proxyRepository.findById(0l)).thenReturn(Optional.of(proxy));

        mockMvc.perform(MockMvcRequestBuilders.post("/proxies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Testname\",\"type\":\"HTTP\",\"hostname\":\"TestHostname\",\"port\":8080" +
                        ",\"username\":\"TestUsername\",\"password\":\"password\",\"active\":true}"));

        mockMvc.perform(MockMvcRequestBuilders.delete("/proxies/{id}", 0)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string( converter.convToJson(proxy) + " Proxy deleted"));

        verify(proxyRepository).deleteById(any(Long.class));
    }
}
