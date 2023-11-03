package com.santiagoposada.libraryreactive.usecase;

import com.santiagoposada.libraryreactive.dto.ResourceDTO;
import com.santiagoposada.libraryreactive.entity.Resource;
import com.santiagoposada.libraryreactive.mapper.ResourceMapper;
import com.santiagoposada.libraryreactive.repository.ResourceRepository;
import org.bson.codecs.jsr310.LocalDateCodec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class UpdateUseCaseTest {

    private ResourceMapper resourceMapper;

    private ResourceRepository resourceRepository;

    UpdateUseCase updateUseCase;
    @BeforeEach
    public void setup() {
        resourceMapper = new ResourceMapper();
        resourceRepository = Mockito.mock(ResourceRepository.class);
        updateUseCase = new UpdateUseCase(resourceMapper, resourceRepository);
    }

    @Test
    void testApply() {

        var resourceDTO = new ResourceDTO();
        resourceDTO.setId("1");
        resourceDTO.setName("Andres");
        resourceDTO.setCategory("category1");
        resourceDTO.setType("Ty");
        resourceDTO.setLastBorrow(LocalDate.now());
        resourceDTO.setUnitsOwed(1);
        resourceDTO.setUnitsAvailable(1);

        var resourceFind = new Resource(
                "1",
                "Andres",
                "category1",
                "Ty",
                LocalDate.now(),
                1,
                1);

        Mockito.when(resourceRepository.save(Mockito.any())).thenReturn(Mono.just(resourceFind));
        StepVerifier.create(updateUseCase.apply(resourceDTO))
                .expectNextMatches(resourceUpdated -> {
                    assert resourceUpdated.getId().equals("1");
                    assert resourceUpdated.getName().equals("Andres");
                    assert resourceUpdated.getCategory().equals("category1");
                    assert resourceUpdated.getType().equals("Ty");
                    assert resourceUpdated.getLastBorrow().equals(LocalDate.now());
                    assert resourceUpdated.getUnitsOwed().equals(1);
                    assert resourceUpdated.getUnitsAvailable().equals(1);
                    return true;
        }).verifyComplete();
        Mockito.verify(resourceRepository).save(Mockito.any());
    }

}