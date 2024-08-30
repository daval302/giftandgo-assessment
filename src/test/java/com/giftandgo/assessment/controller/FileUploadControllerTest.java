package com.giftandgo.assessment.controller;

import com.giftandgo.assessment.component.InputRecordValidator;
import com.giftandgo.assessment.component.InputToJSONOutputConverter;
import com.giftandgo.assessment.error.ExceptionHandling;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FileUploadControllerTest {

    private MockMvc mockMvc;
    private MultipartFile multipartFile;

    @Mock
    private InputRecordValidator inputRecordValidator;

    @Mock
    private InputToJSONOutputConverter inputToJSONOutputConverter;
    @InjectMocks
    private FileUploadController fileUploadController;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(fileUploadController, "skipValidation", false);

        multipartFile = Mockito.mock(MultipartFile.class);
        mockMvc = MockMvcBuilders.standaloneSetup(fileUploadController)
                .setControllerAdvice(new ExceptionHandling())
                .build();
    }


    @Test
    void uploadFileSuccess() throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String data = """
                18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1
                3ce2d17b-e66a-4c1e-bca3-40eb1c9222c7|2X2D24|Mike Smith|Likes Grape|Drives an SUV|35.0|95.5
                1afb6f5d-a7c2-4311-a92d-974f3180ff5e|3X3D35|Jenny Walters|Likes Avocados|Rides AScooter|8.5|15.3
                """;
        baos.write(data.getBytes(StandardCharsets.UTF_8));
        ByteArrayInputStream is = new ByteArrayInputStream(baos.toByteArray());
        Mockito.when(multipartFile.getInputStream()).thenReturn(is);



        ResponseEntity<?> response = fileUploadController.uploadFile(multipartFile);
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
    }
}
