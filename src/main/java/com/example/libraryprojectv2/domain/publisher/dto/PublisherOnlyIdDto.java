package com.example.libraryprojectv2.domain.publisher.dto;

import javax.validation.constraints.PositiveOrZero;

public record PublisherOnlyIdDto(@PositiveOrZero Long id) {}
