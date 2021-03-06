package com.korogi.rest.mapper.anime;

import static com.korogi.core.domain.testdata.AnimeTestData.steinsGate_notPersisted;
import static org.assertj.core.api.Assertions.assertThat;

import com.korogi.core.domain.Anime;
import com.korogi.core.util.FieldAssertionUtil;
import com.korogi.dto.AnimeDTO;
import com.korogi.rest.mapper.ResourceMapperTestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

/**
 * @author Daan Peelman
 */
class AnimeResourceMapperTest {
    private AnimeResourceMapperImpl mapper = new AnimeResourceMapperImpl();

    @Test
    void toDTOResource() throws Exception {
        Anime animeToMap = steinsGate_notPersisted().build();
        EntityModel<AnimeDTO> mappedResource = mapper.toDTOResource(animeToMap);

        assertThat(mappedResource).isNotNull();
        assertThat(mappedResource.getContent()).isNotNull();
        new FieldAssertionUtil(animeToMap, mappedResource.getContent())
            .expectFieldValue("TYPE", "anime")
            .assertAllFieldValuesAreEqual();

        assertThat(mappedResource.getLinks())
            .isNotNull()
            .isNotEmpty()
            .hasSize(5)
            .extracting(ResourceMapperTestUtil::toLinkWithNoAffordances)
            .containsExactly(
                Link.of("/anime/{id}", "self"),
                Link.of("/anime/{id}/prequal", "prequal"),
                Link.of("/anime/{id}/sequal", "sequal"),
                Link.of("/anime/{id}/episodes", "episodes"),
                Link.of("/anime/{id}/personages", "personages")
            );
    }
}