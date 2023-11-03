package com.santiagoposada.libraryreactive.usecase;

import com.santiagoposada.libraryreactive.dto.ResourceDTO;
import com.santiagoposada.libraryreactive.entity.Resource;
import com.santiagoposada.libraryreactive.mapper.ResourceMapper;
import com.santiagoposada.libraryreactive.repository.ResourceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BorrowResourceUseCaseTest {

    private ResourceRepository resourceRepository;
    private UpdateUseCase updateUseCase;
    private BorrowResourceUseCase borrowResourceUseCase;
    private ResourceMapper resourceMapper;

    @BeforeEach
    void setup() {
        resourceMapper = mock(ResourceMapper.class);
        resourceRepository = mock(ResourceRepository.class);
        updateUseCase = mock(UpdateUseCase.class);
        borrowResourceUseCase = new BorrowResourceUseCase(resourceMapper,resourceRepository,updateUseCase);
    }

    @Test
    void applyAvailable() {
        Resource resource = new Resource();
        resource.setId("1233435ff");
        resource.setName("Nombre #1");
        resource.setType("Tipo #1");
        resource.setCategory("Area tematica #1");
        resource.setUnitsAvailable(2);
        resource.setUnitsOwed(0);
        resource.setLastBorrow(LocalDate.parse("2020-01-10"));

        ResourceDTO resourceDTO = new ResourceDTO();
        resourceDTO.setId(resource.getId());
        resourceDTO.setName(resource.getName());
        resourceDTO.setType(resource.getType());
        resourceDTO.setCategory(resource.getCategory());
        resourceDTO.setUnitsAvailable(resource.getUnitsAvailable());
        resourceDTO.setUnitsOwed(resource.getUnitsOwed());
        resourceDTO.setLastBorrow(resource.getLastBorrow());


        when(resourceRepository.findById(resource.getId())).thenReturn(Mono.just(resource));
        //Mockito.when(resourceRepository.save(resource)).thenReturn(Mono.just(resource));
        when(resourceMapper.fromResourceEntityToDTO()).thenReturn(r -> resourceDTO);
        when(updateUseCase.apply(resourceDTO)).thenReturn(Mono.just(resourceDTO));
        Mono<String> stringMono = borrowResourceUseCase.apply(resource.getId());

        StepVerifier.create(stringMono)
                .expectNext("The resource Nombre #1 has been borrowed, there are 1 units available")
                .verifyComplete();
    }

    @Test
    void applyNoAvailable() {
        Resource resource = new Resource();

        resource.setId("1233435ff");
        resource.setName("Nombre #1");
        resource.setType("Tipo #1");
        resource.setCategory("Area tematica #1");
        resource.setUnitsAvailable(0);
        resource.setUnitsOwed(5);
        resource.setLastBorrow(LocalDate.parse("2020-01-10"));

        ResourceDTO resourceDTO = new ResourceDTO();
        resourceDTO.setId(resource.getId());
        resourceDTO.setName(resource.getName());
        resourceDTO.setType(resource.getType());
        resourceDTO.setCategory(resource.getCategory());
        resourceDTO.setUnitsAvailable(resource.getUnitsAvailable());
        resourceDTO.setUnitsOwed(resource.getUnitsOwed());
        resourceDTO.setLastBorrow(resource.getLastBorrow());

        when(resourceRepository.findById(resource.getId())).thenReturn(Mono.just(resource));;
        when(resourceMapper.fromResourceEntityToDTO()).thenReturn(r -> resourceDTO);
        when(updateUseCase.apply(resourceDTO)).thenReturn(Mono.just(resourceDTO));
        Mono<String> stringMono = borrowResourceUseCase.apply(resource.getId());

        StepVerifier.create(stringMono)
                .expectNext("There arent units left to be borrow of that resource")
                .expectComplete()
                .verify();
    }
}