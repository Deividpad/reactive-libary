package com.santiagoposada.libraryreactive.usecase;

import com.santiagoposada.libraryreactive.entity.Resource;
import com.santiagoposada.libraryreactive.repository.ResourceRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReturnUseCaseTest {

    @MockBean
    private ResourceRepository resourceRepository;

    @SpyBean
    private ReturnUseCase returnUseCase;

    @Test
    void testApply() {

        var resourceFind = new Resource(
                "1",
                "Andres",
                "category1",
                "Ty",
                LocalDate.now(),
                10,
                10);

        Mockito.when(resourceRepository.findById(resourceFind.getId())).thenReturn(Mono.just(resourceFind));
        Mockito.when(resourceRepository.save(resourceFind)).thenReturn(Mono.just(resourceFind));
        Mono<String> stringMono = returnUseCase.apply(resourceFind.getId());

        StepVerifier.create(stringMono)
                .expectNext("The resource with id: 1was returned successfully")
                .expectComplete()
                .verify();
    }
}