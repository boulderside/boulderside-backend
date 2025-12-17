package com.line7studio.boulderside.usecase.search.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = BoulderDetails.class, name = "BOULDER"),
    @JsonSubTypes.Type(value = RouteDetails.class, name = "ROUTE"),
    @JsonSubTypes.Type(value = BoardPostDetails.class, name = "BOARD_POST"),
    @JsonSubTypes.Type(value = MatePostDetails.class, name = "MATE_POST")
})
public interface SearchItemDetails {
}