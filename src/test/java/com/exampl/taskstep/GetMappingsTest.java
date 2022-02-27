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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
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
public class GetMappingsTest {

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
    void getEmptyProxyReturnsBadRequest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/proxies/1"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("There is no proxy with such id"));
        verify(proxyRepository).findById(1l);
    }

    @Test
    void getProxyReturnsProxyTest() throws Exception {
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

        verify(proxyRepository).findById(0l);
    }

    @Test
    void getProxyByNameAndTypeTest() throws  Exception {
        Proxy proxy = new Proxy(0,"Testname", ProxyType.HTTP, "TestHostname", 8080,
                "TestUsername", "password", true);

        Proxy proxy2 = new Proxy(0,"Testname2", ProxyType.HTTPS, "TestHostname2", 8080,
                "TestUsername2", "password", true);

        when(proxyRepository.findByNameAndType("Testname", ProxyType.HTTP)).thenReturn(Optional.of(proxy));

        this.mockMvc.perform(MockMvcRequestBuilders.post("/proxies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Testname\",\"type\":\"HTTP\",\"hostname\":\"TestHostname\",\"port\":8080" +
                        ",\"username\":\"TestUsername\",\"password\":\"password\",\"active\":true}"));

        mockMvc.perform(MockMvcRequestBuilders.get("/proxies/get/{name}/{type}", "Testname", "HTTP"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(converter.convToJson(proxy)));
        verify(proxyRepository).findByNameAndType("Testname",ProxyType.HTTP);
    }

    @Test
    void getProxyByNameAndTypeFailsTest() throws  Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/proxies/get/abc/HTTP"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("There is no proxy with these name and type parameters"));
        verify(proxyRepository).findByNameAndType("abc", ProxyType.HTTP);
    }

    @Test
    void getAllProxiesWithPagination() throws Exception {
        Proxy proxy = new Proxy(0,"Testname", ProxyType.HTTP, "TestHostname", 8080,
                "TestUsername", "password", true);
        Proxy proxy2 = new Proxy(1,"Testname2", ProxyType.HTTP, "TestHostname2", 9009,
                "TestUsername2", "password", false);

        ArrayList<Proxy> lst1 = new ArrayList<>();
        lst1.add(proxy);
        lst1.add(proxy2);
        Page<Proxy> pg1 = new PageImpl<>(lst1);

        ArrayList<Proxy> lst2 = new ArrayList<>();
        lst2.add(proxy);
        Page<Proxy> pg2 = new PageImpl<>(lst2);

        ArrayList<Proxy> lst3 = new ArrayList<>();
        lst3.add(proxy);
        Page<Proxy> pg3 = new PageImpl<>(lst3);

        Pageable paging = PageRequest.of(0,2);
        Pageable paging2 = PageRequest.of(0,1);
        Pageable paging3 = PageRequest.of(1,1);

        when(proxyRepository.findAll(paging)).thenReturn(pg1);
        when(proxyRepository.findAll(paging2)).thenReturn(pg2);
        when(proxyRepository.findAll(paging3)).thenReturn(pg3);

        mockMvc.perform(MockMvcRequestBuilders.get("/proxies/{pageNum}/{pageSize}", 0, 2))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(converter.listToJsonString(pg1)));

        mockMvc.perform(MockMvcRequestBuilders.get("/proxies/{pageNum}/{pageSize}", 0, 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(converter.listToJsonString(pg2)));

        mockMvc.perform(MockMvcRequestBuilders.get("/proxies/{pageNum}/{pageSize}", 1, 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(converter.listToJsonString(pg3)));

        verify(proxyRepository).findAll(paging);
        verify(proxyRepository).findAll(paging2);
        verify(proxyRepository).findAll(paging3);
    }
}
