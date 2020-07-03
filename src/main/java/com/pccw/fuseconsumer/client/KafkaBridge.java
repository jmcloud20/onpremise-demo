package com.pccw.fuseconsumer.client;

import com.google.gson.Gson;
import com.pccw.fuseconsumer.model.CreateConsumerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Collections;

@Component
@ConfigurationProperties("kafka.bridge")
public class KafkaBridge {

    private Logger logger = LoggerFactory.getLogger(KafkaBridge.class.getName());

    // Config Strings
    private String endpoint;
    private String key;
    private String token;
    private String instance;
    private String consumer;
    private String headerAccept;
    private String headerAcceptValue;
    private String consumerName;
    private String format;

    private RestTemplate restTemplate;

    private final String operation = "consumers";

    public KafkaBridge(RestTemplateBuilder restTemplateBuilder) {
        //this.restTemplate = restTemplateBuilder.build();
        this.restTemplate = new RestTemplate(this.proxySettings());
    }

    public SimpleClientHttpRequestFactory proxySettings(){
        SimpleClientHttpRequestFactory clientHttpReq = new SimpleClientHttpRequestFactory();
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("corpvpn.pccw.com", 8080));
        clientHttpReq.setProxy(proxy);
        return clientHttpReq;
    }
    // Create consumer
    /**
     * URL: https://p2.uamp.hkt.com/rubykafka/consumers/ruby-consumer-group
     * Body:
     * {
     *     "name":"ruby-fuse-consumer-test",
     *     "format":"json"
     * }
     *
     * Returns the following if consumer group exists already.
     * {
     *     "error_code": 409,
     *     "message": "A consumer instance with the specified name already exists in the Kafka Bridge."
     * }
     *
     * Returns the following if successful
     * {
     *     "instance_id": "ruby-fuse-consumer-test",
     *     "base_uri": "https://x.uamp.hkt.com:443/rubykafka/consumers/ruby-consumer-group/instances/ruby-fuse-consumer-test"
     * }
     *
     * Method Type: POST
     * Headers:
     * content-type: application/vnd.kafka.v2+json
     * x-api-key: 800690dcd95e1f52529b648c0b47721c
     */
    public void createConsumer(){

        logger.info("Create consumer");

        logger.info("Create object for body.");
        Gson gson = new Gson();
        CreateConsumerDTO consumerDTO = CreateConsumerDTO.builder()
                .format(this.format)
                .name(this.consumerName)
                .build();

        HttpHeaders headers = createHttpHeader();
        HttpEntity<String> entity = new HttpEntity<String>(gson.toJson(consumerDTO), headers);


        logger.info("Create consumer URL");
        String url = endpoint+"/"+this.instance+"/"+operation+"/"+this.consumer;


        logger.info("Generated URL is: "+url);
        ResponseEntity<String> responseEntity = this.restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        logger.info("Result: "+responseEntity.getBody());
        logger.info("Status code: "+responseEntity.getStatusCode());

    }

    private HttpHeaders createHttpHeader() {
        logger.info("Create HTTP header");
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.parseMediaType(headerAcceptValue)));
        headers.set(this.key, this.token);
        return headers;
    }


    public void readMessage(){
        logger.info("Read kafka messages.");

        HttpHeaders headers = this.createHttpHeader();
        HttpEntity<String> entity = new HttpEntity<String>("body", headers);

        logger.info("Create URL");
        String url = endpoint+"/"+instance+"/"+operation+"/"+consumer+"/instances/ruby-consumer/records";

        logger.info("Generated URL is: "+url);
        ResponseEntity<String> responseEntity = this.restTemplate.exchange(url, HttpMethod.GET,entity,String.class);

        logger.info("Result: "+responseEntity);
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    public void setHeaderAccept(String headerAccept) {
        this.headerAccept = headerAccept;
    }

    public void setHeaderAcceptValue(String headerAcceptValue) {
        this.headerAcceptValue = headerAcceptValue;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    // Subscribe consumer group
    /**
     * URL: https://p2.uamp.hkt.com/rubykafka/consumers/[consumer-group]/instances/[consumer]/subscription
     * Body:
     * {
     *     "topics":"ruby-topic"
     * }
     *
     * Return:
     * {
     *     "topics": [
     *         "ruby-topic"
     *     ],
     *     "partitions": [
     *         {
     *             "ruby-topic": [
     *                 2,
     *                 0,
     *                 1
     *             ]
     *         }
     *     ]
     * }
     *
     * Method: GET
     * Headers:
     * content-type: application/vnd.kafka.v2+json
     * x-api-key: 800690dcd95e1f52529b648c0b47721c
     */

    // Read message

}
