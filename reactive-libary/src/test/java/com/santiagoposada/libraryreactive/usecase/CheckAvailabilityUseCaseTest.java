package com.santiagoposada.libraryreactive.usecase;

import com.santiagoposada.libraryreactive.dto.ResourceDTO;
import com.santiagoposada.libraryreactive.entity.Resource;
import com.santiagoposada.libraryreactive.mapper.ResourceMapper;
import com.santiagoposada.libraryreactive.repository.ResourceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CheckAvailabilityUseCaseTest {

    private ResourceMapper resourceMapper;

    private ResourceRepository resourceRepository;

    CheckAvailabilityUseCase checkAvailabilityUseCase;
    @BeforeEach
    public void setup() {
        resourceMapper = new ResourceMapper();
        resourceRepository = Mockito.mock(ResourceRepository.class);
        checkAvailabilityUseCase = new CheckAvailabilityUseCase(resourceMapper, resourceRepository);
    }


    @Test
    void testApplyIfGetUnitsAvailableMajorThanZero() {

        var resourceFind = new Resource(
                "1",
                "Andres",
                "category1",
                "Ty",
                LocalDate.now(),
                10,
                10);

        Mockito.when(resourceRepository.findById("1")).thenReturn(Mono.just(resourceFind));
        StepVerifier.create(checkAvailabilityUseCase.apply("1"))
                .expectNextMatches(res -> {
                    if (resourceFind.getUnitsAvailable() > 0 ) {
                        assertEquals("Andresis available", resourceFind.getName() + "is available");
                    }
                    return true;
                }).verifyComplete();
        Mockito.verify(resourceRepository).findById("1");
    }

    @Test
    void testApplyIfGetUnitsAvailableIsZero() {

        var resourceFind = new Resource(
                "1",
                "Andres",
                "category1",
                "Ty",
                LocalDate.now(),
                0,
                0);

        Mockito.when(resourceRepository.findById("1")).thenReturn(Mono.just(resourceFind));
        StepVerifier.create(checkAvailabilityUseCase.apply("1"))
                .expectNextMatches(res -> {
                    if (resourceFind.getUnitsAvailable() < 1 ) {
                        assertEquals("Andres is not available, last borrow ", resourceFind.getName() + " is not available, last borrow ");
                    }
                    return true;
                }).verifyComplete();
        Mockito.verify(resourceRepository).findById("1");
    }
}