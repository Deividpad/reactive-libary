package com.santiagoposada.libraryreactive.usecase;

import com.santiagoposada.libraryreactive.mapper.ResourceMapper;
import com.santiagoposada.libraryreactive.repository.ResourceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class DeleteResourceUseCaseTest {

    private DeleteResourceUseCase deleteResourceUseCase;

    private ResourceMapper resourceMapper;


    private ResourceRepository resourceRepository;

    @BeforeEach
    public void setup(){
        resourceMapper = new ResourceMapper();
        resourceRepository = mock(ResourceRepository.class);
        deleteResourceUseCase = new DeleteResourceUseCase(resourceMapper, resourceRepository);

    }

    @Test
    void testApply() {
        when(resourceRepository.deleteById(Mockito.<String>any())).thenReturn(Mono.empty());
        StepVerifier.create(deleteResourceUseCase.apply(Mockito.<String>any())).verifyComplete();
        verify(resourceRepository).deleteById(Mockito.<String>any());

    }
}