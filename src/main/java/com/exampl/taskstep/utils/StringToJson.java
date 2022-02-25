package com.exampl.taskstep.utils;

import com.exampl.taskstep.models.Proxy;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @date: Feb.25.2022
 * @author: Mihhail Daniljuk
 * @email: daniljukmihhail@gmail.com
 */

public class StringToJson {


    private ObjectMapper mapper = new ObjectMapper();

    public String convToJson(Proxy proxy) {
        try {
            String json = mapper.writeValueAsString(proxy);
            return json;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Can't convert";
        }
    }

    public String listToJsonString(Iterable<Proxy> proxies) {
        StringBuilder str = new StringBuilder("");
        for (Proxy el : proxies){
            str.append(el);
            str.append("\n");
        }
        return str.toString();
    }

}
