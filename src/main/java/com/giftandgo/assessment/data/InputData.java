package com.giftandgo.assessment.data;

public record InputData(
        String uuid,
        String id,
        String name,
        String likes,
        String transport,
        Double avgSpeed,
        Double topSpeed) {
}
