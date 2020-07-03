package com.pccw.fuseconsumer.controller;

import com.pccw.fuseconsumer.client.KafkaBridge;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    private KafkaBridge bridge;

    public TestController(KafkaBridge bridge) {
        this.bridge = bridge;
    }


    @GetMapping("/test")
    public void testRestTemplate(){
        bridge.createConsumer();
        //bridge.readMessage();
    }
}
