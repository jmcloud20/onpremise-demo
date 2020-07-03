package com.pccw.fuseconsumer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReadMessageDTO {
    private String topic;
    private String key;
    private String value;
    private String partition;
    private String offset;
}
