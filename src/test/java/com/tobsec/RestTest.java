package com.tobsec;

import com.tobsec.context.AppConfig;
import com.tobsec.service.UserService;
import com.tobsec.service.UserServiceImpl;

import com.tobsec.common.Log;
import com.tobsec.service.exception.*;
import java.net.*;
import java.time.*;
import java.time.format.*;
import org.springframework.web.util.*;
import java.util.*;
import java.net.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Before;
import java.io.UnsupportedEncodingException;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import java.nio.charset.Charset;
import org.slf4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import org.springframework.http.converter.*;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=AppConfig.class)
public class RestTest implements ParentTest  {

    @Log
    protected Logger restLogger;

    @Value("#{serviceProps['open.serviceKey']}") 
    private String serviceKey;

    @Value("#{serviceProps['open.uri']}") 
    private String uriString;

    @Value("#{serviceProps['open.gubun']}") 
    private String gubun;

    @Value("#{serviceProps['open.pageNo']}") 
    private int pageNo;

    @Value("#{serviceProps['open.numOfRows']}") 
    private int numOfRows;

    @Value("#{serviceProps['open.nx']}") 
    private int nx;

    @Value("#{serviceProps['open.ny']}") 
    private int ny;

    @Before
    public void setUp() {
        
    }

    @Test
    public void serviceKeyTest() {
        assertThat(serviceKey, is(notNullValue()));

        assertThat(serviceKey.length(), is(greaterThan(10)));
    }

    @Test
    public void restTemplateTest() throws Exception {
        RestTemplate template = new RestTemplate();

        uriString = uriString + gubun + "";

        LocalDateTime currentDateTime = LocalDateTime.now();

        String baseDate = currentDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String baseTime = currentDateTime.format(DateTimeFormatter.ofPattern("HHmm"));

        baseTime = "2000";

        UriComponents components = UriComponentsBuilder.fromHttpUrl(uriString)
                                    .query("serviceKey=" + serviceKey)
                                    .query("_type=json")
                                    .query("numOfRows=" + numOfRows)
                                    .query("nx=" + nx)
                                    .query("ny=" + ny)
                                    .query("base_time=" + baseTime)
                                    .query("base_date=" + baseDate)
                                    .build(true);
        
        URI uri = components.toUri();

        restLogger.info(uri.toString());

        String jsonString = template.getForObject(uri, String.class);

        restLogger.info(jsonString);

        JsonNode rootNode = new ObjectMapper().readTree(jsonString);

        JsonNode response = rootNode.get("response");

        JsonNode resultMsg = response.get("header").get("resultMsg");
        
        String msg = resultMsg.asText();

        assertThat(msg, is("OK"));

        Iterator<JsonNode> itemNode = response.get("body").get("items").get("item").elements();

        String category = "";
        double fcstValue = 0.0D;

        while(itemNode.hasNext()) {
            JsonNode item = itemNode.next();

            category = item.get("category").textValue();
            fcstValue = item.get("fcstValue").asDouble(0.0D);

            restLogger.info("Category : " + category + ", Value : " + fcstValue);
        }
    }

}