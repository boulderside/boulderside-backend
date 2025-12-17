package com.line7studio.boulderside.controller.approach.request;

public record CreatePointRequest(
    Integer orderIndex,
    String name,
    String description,
    String note
) {}