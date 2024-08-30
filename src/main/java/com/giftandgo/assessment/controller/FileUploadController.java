package com.giftandgo.assessment.controller;

import com.giftandgo.assessment.component.InputRecordValidator;
import com.giftandgo.assessment.component.InputToJSONOutputConverter;
import com.giftandgo.assessment.data.InputData;
import com.giftandgo.assessment.error.exception.InvalidRecordException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.validation.SimpleErrors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FileUploadController {
    private final Logger logger = LoggerFactory.getLogger(FileUploadController.class);
    private final InputRecordValidator inputRecordValidator = new InputRecordValidator();
    private final InputToJSONOutputConverter inputToJSONOutputConverter = new InputToJSONOutputConverter();

    private final Boolean skipValidation;

    public FileUploadController(@Value("${features.file-upload-skip-validation}") Boolean skipValidation) {
        this.skipValidation = skipValidation;
    }

    @PostMapping(value = "/api/upload", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file

    ) {
        logger.warn("File uploaded: " + file.getOriginalFilename());

        try (Reader reader = new InputStreamReader(file.getInputStream())) {
            Iterable<CSVRecord> records = CSVFormat.Builder.create()
                    .setDelimiter('|')
                    .build()
                    .parse(reader);

            List<InputData> inputDataList = new ArrayList<>();

            for (CSVRecord record : records) {
                SimpleErrors simpleErrors = new SimpleErrors(record);
                if (!skipValidation)
                    inputRecordValidator.validate(record, simpleErrors);
                if (simpleErrors.hasErrors()) {
                    logger.error("Invalid record: " + simpleErrors.getAllErrors());
                    throw new InvalidRecordException("Invalid record", simpleErrors.getAllErrors().stream()
                            .map(DefaultMessageSourceResolvable::getCode).collect(Collectors.toList()));
                }
                inputDataList.add(new InputData(
                        record.get(0),
                        record.get(1),
                        record.get(2),
                        record.get(3),
                        record.get(4),
                        Double.parseDouble(record.get(5)),
                        Double.parseDouble(record.get(6)
                        )));
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(inputToJSONOutputConverter.convert(inputDataList).getBytes());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename("OutcomeFile.json")
                    .build());
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(outputStream.size())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new ByteArrayResource(outputStream.toByteArray()));

        } catch (IOException e) {
            logger.error("Error parsing CSV file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error parsing CSV file: " + e.getMessage());
        }
    }
}
