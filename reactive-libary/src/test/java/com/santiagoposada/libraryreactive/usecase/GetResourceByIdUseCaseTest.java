package com.santiagoposada.libraryreactive.usecase;

import com.santiagoposada.libraryreactive.entity.Resource;
import com.santiagoposada.libraryreactive.mapper.ResourceMapper;
import com.santiagoposada.libraryreactive.repository.ResourceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

class GetResourceByIdUseCaseTest {

    private ResourceMapper resourceMapper;

    private ResourceRepository resourceRepository;

    GetResourceByIdUseCase getResourceByIdUseCase;
    @BeforeEach
    public void setup() {
        resourceMapper = new ResourceMapper();
        resourceRepository = Mockito.mock(ResourceRepository.class);
        getResourceByIdUseCase = new GetResourceByIdUseCase(resourceMapper, resourceRepository);
    }

    @Test
    void testApply() {

        var resourceFind = new Resource(
                "1",
                "Andres",
                "category1",
                "Ty",
                LocalDate.now(),
                1,
                1);

        Mockito.when(resourceRepository.findById("1")).thenReturn(Mono.just(resourceFind));
        StepVerifier.create(getResourceByIdUseCase.apply("1"))
                .expectNextMatches(res -> {
                    assert res.getId().equals("1");
                    assert res.getName().equals("Andres");
                    assert res.getCategory().equals("category1");
                    assert res.getType().equals("Ty");
                    assert res.getLastBorrow().equals(LocalDate.now());
                    assert res.getUnitsOwed().equals(1);
                    assert res.getUnitsAvailable().equals(1);
                    return true;
        }).verifyComplete();
        Mockito.verify(resourceRepository).findById("1");
    }

}