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

        // Trying to delete non-existent proxy
        mockMvc.perform(MockMvcRequestBuilders.delete("/proxies/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("There is no proxy with such Id"));
        verify(proxyRepository).findById(1l);
    }

    @Test
    void deleteProxyDeletesTest() throws Exception {
        Proxy proxy = new Proxy(0,"Testname", ProxyType.HTTP, "TestHostname", 8080,
                "TestUsername", "password", true);

        // This is rather question, without this line of code test is not passed, ServerResponse 400 instead of 200.
        when(proxyRepository.findById(0l)).thenReturn(Optional.of(proxy));

        // Adding proxy to the DB
        mockMvc.perform(MockMvcRequestBuilders.post("/proxies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Testname\",\"type\":\"HTTP\",\"hostname\":\"TestHostname\",\"port\":8080" +
                        ",\"username\":\"TestUsername\",\"password\":\"password\",\"active\":true}"));

        // Deleting proxy from DB
        mockMvc.perform(MockMvcRequestBuilders.delete("/proxies/{id}", 0)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string( converter.convToJson(proxy) + " Proxy deleted"));

        verify(proxyRepository).deleteById(0l);
        verify(proxyRepository).findById(0l);
    }
}
