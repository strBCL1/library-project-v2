package com.example.libraryprojectv2.domain.publisher.dto;

import javax.validation.constraints.Positive;

public record PublisherOnlyIdDto(@Positive Long id) {}
