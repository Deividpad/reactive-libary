package com.santiagoposada.libraryreactive.routes;

import com.santiagoposada.libraryreactive.dto.ResourceDTO;
import com.santiagoposada.libraryreactive.entity.Resource;
import com.santiagoposada.libraryreactive.mapper.ResourceMapper;
import com.santiagoposada.libraryreactive.repository.ResourceRepository;
import com.santiagoposada.libraryreactive.usecase.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/*@WebFluxTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        ResourceRouter.class,
        ResourceMapper.class,
        CreateResourceUseCase.class,
        GetAllUseCase.class,
        GetResourceByIdUseCase.class,
        UpdateUseCase.class,
        DeleteResourceUseCase.class,
        CheckAvailabilityUseCase.class,
        GetByTypeUseCase.class,
        GetByCategoryUseCase.class,
        BorrowResourceUseCase.class,
        ReturnUseCase.class
})*/
@SpringBootTest
@AutoConfigureWebTestClient
class ResourceRouterTest {

    @MockBean
    private ResourceRepository resourceRepository;


    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testCreateResourceRoute() {

        var resourceDTO = new ResourceDTO();
        resourceDTO.setId("1");
        resourceDTO.setName("Andres");
        resourceDTO.setCategory("category1");
        resourceDTO.setType("Ty");
        resourceDTO.setLastBorrow(LocalDate.parse("2020-01-10"));
        resourceDTO.setUnitsOwed(1);
        resourceDTO.setUnitsAvailable(1);

        Resource resource1 = new Resource();
        resource1.setId("1");
        resource1.setName("Andres");
        resource1.setCategory("category1");
        resource1.setType("Ty");
        resource1.setLastBorrow(LocalDate.parse("2020-01-10"));
        resource1.setUnitsOwed(1);
        resource1.setUnitsAvailable(1);


        when(resourceRepository.save(any())).thenReturn(Mono.just(resource1));

        webTestClient.post()
                .uri("/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(resourceDTO), ResourceDTO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResourceDTO.class)
                .value(resource -> {
                    assertThat(resource.getId()).isEqualTo(resource1.getId());
                    assertThat(resource.getName()).isEqualTo(resource1.getName());
                    assertThat(resource.getType()).isEqualTo(resource1.getType());
                    assertThat(resource.getCategory()).isEqualTo(resource1.getCategory());
                    assertThat(resource.getUnitsAvailable()).isEqualTo(resource1.getUnitsAvailable());
                    assertThat(resource.getUnitsOwed()).isEqualTo(resource1.getUnitsOwed());
                    assertThat(resource.getLastBorrow()).isEqualTo(LocalDate.parse("2020-01-10"));
                });
    }

    @Test
    void testGetAllRouter() {

        Resource resource1 = new Resource();
        resource1.setId("1233435ff");
        resource1.setName("Nombre #1");
        resource1.setType("Tipo #1");
        resource1.setCategory("Area tematica #1");
        resource1.setUnitsAvailable(2);
        resource1.setUnitsOwed(0);
        resource1.setLastBorrow(LocalDate.parse("2020-01-10"));

        Resource resource2 = new Resource();
        resource2.setId("1233435ff2");
        resource2.setName("Nombre #2");
        resource2.setType("Tipo #2");
        resource2.setCategory("Area tematica #2");
        resource2.setUnitsAvailable(2);
        resource2.setUnitsOwed(0);
        resource2.setLastBorrow(LocalDate.parse("2020-01-12"));

        when(resourceRepository.findAll()).thenReturn(Flux.just(resource1, resource2));

        webTestClient.get()
                .uri("/resources")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(ResourceDTO.class)
                .value(resource -> {
                    assertThat(resource.get(0).getId()).isEqualTo("1233435ff");
                    assertThat(resource.get(0).getName()).isEqualTo("Nombre #1");
                    assertThat(resource.get(0).getType()).isEqualTo("Tipo #1");
                    assertThat(resource.get(0).getCategory()).isEqualTo("Area tematica #1");
                    assertThat(resource.get(0).getUnitsAvailable()).isEqualTo(2);
                    assertThat(resource.get(0).getUnitsOwed()).isEqualTo(0);
                    assertThat(resource.get(0).getLastBorrow()).isEqualTo(LocalDate.parse("2020-01-10"));

                    assertThat(resource.get(1).getId()).isEqualTo("1233435ff2");
                    assertThat(resource.get(1).getName()).isEqualTo("Nombre #2");
                    assertThat(resource.get(1).getType()).isEqualTo("Tipo #2");
                    assertThat(resource.get(1).getCategory()).isEqualTo("Area tematica #2");
                    assertThat(resource.get(1).getUnitsAvailable()).isEqualTo(2);
                    assertThat(resource.get(1).getUnitsOwed()).isEqualTo(0);
                    assertThat(resource.get(1).getLastBorrow()).isEqualTo(LocalDate.parse("2020-01-12"));
                });
    }

    @Test
    void testGetResourceById() {
        Resource resource1 = new Resource();
        resource1.setId("1233435ff");
        resource1.setName("Nombre #1");
        resource1.setType("Tipo #1");
        resource1.setCategory("Area tematica #1");
        resource1.setUnitsAvailable(2);
        resource1.setUnitsOwed(0);
        resource1.setLastBorrow(LocalDate.parse("2020-01-10"));


        when(resourceRepository.findById(resource1.getId())).thenReturn(Mono.just(resource1));

        webTestClient.get()
                .uri("/resource/1233435ff")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ResourceDTO.class)
                .value(resource -> {
                    assertThat(resource.getId()).isEqualTo("1233435ff");
                });
    }

    @Test
    void testUpdateResourceRoute() {

        var resourceDTO = new ResourceDTO();
        resourceDTO.setId("1");
        resourceDTO.setName("Andres");
        resourceDTO.setCategory("category1");
        resourceDTO.setType("Ty");
        resourceDTO.setLastBorrow(LocalDate.parse("2020-01-10"));
        resourceDTO.setUnitsOwed(1);
        resourceDTO.setUnitsAvailable(1);

        Resource resource1 = new Resource();
        resource1.setId("1");
        resource1.setName("Andres");
        resource1.setCategory("category1");
        resource1.setType("Ty");
        resource1.setLastBorrow(LocalDate.parse("2020-01-10"));
        resource1.setUnitsOwed(1);
        resource1.setUnitsAvailable(1);

        when(resourceRepository.findById(resource1.getId())).thenReturn(Mono.just(resource1));
        when(resourceRepository.save(any())).thenReturn(Mono.just(resource1));

        webTestClient.put()
                .uri("/update")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(resourceDTO), ResourceDTO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResourceDTO.class)
                .value(resource -> {
                    assertThat(resource.getId()).isEqualTo(resource1.getId());
                    assertThat(resource.getName()).isEqualTo(resource1.getName());
                    assertThat(resource.getType()).isEqualTo(resource1.getType());
                    assertThat(resource.getCategory()).isEqualTo(resource1.getCategory());
                    assertThat(resource.getUnitsAvailable()).isEqualTo(resource1.getUnitsAvailable());
                    assertThat(resource.getUnitsOwed()).isEqualTo(resource1.getUnitsOwed());
                    assertThat(resource.getLastBorrow()).isEqualTo(LocalDate.parse("2020-01-10"));
                });
    }

    @Test
    void testDeleteResourceToute() {

        var resourceDTO = new ResourceDTO();
        resourceDTO.setId("1");

        when(resourceRepository.deleteById(resourceDTO.getId())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/delete/" + resourceDTO.getId())
                .exchange()
                .expectStatus().isAccepted()
                .expectBody()
                .isEmpty();

        verify(resourceRepository).deleteById(resourceDTO.getId());

    }

    @Test
    void testCheckForAvailabilityRoute() {
        Resource resource1 = new Resource();
        resource1.setId("1233435ff");
        resource1.setName("Nombre #1");
        resource1.setType("Tipo #1");
        resource1.setCategory("Area tematica #1");
        resource1.setUnitsAvailable(2);
        resource1.setUnitsOwed(0);
        resource1.setLastBorrow(LocalDate.parse("2020-01-10"));


        when(resourceRepository.findById(resource1.getId())).thenReturn(Mono.just(resource1));

        webTestClient.get()
                .uri("/availability/" + resource1.getId())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .value(resource -> {
                    assertThat(resource).isEqualTo(resource1.getName() + "is available");
                });
    }
}