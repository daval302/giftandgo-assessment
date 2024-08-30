package com.giftandgo.assessment.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdConverter;
import com.giftandgo.assessment.data.InputData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InputToJSONOutputConverter extends StdConverter<Collection<InputData>, String> {
    private final Logger logger = LoggerFactory.getLogger(InputToJSONOutputConverter.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convert(Collection<InputData> value) {

        List<Map<String, ? extends Serializable>> outputList = value.stream().map(inputData -> Map.of(
                "name", inputData.name(),
                "transport", inputData.transport(),
                "topSpeed", inputData.topSpeed()
        )).collect(Collectors.toList());

        try {
            return objectMapper.writeValueAsString(outputList);
        } catch (JsonProcessingException e) {
            logger.error("Failed to convert InputData to JSON", e);
            throw new RuntimeException(e);
        }
    }
}
