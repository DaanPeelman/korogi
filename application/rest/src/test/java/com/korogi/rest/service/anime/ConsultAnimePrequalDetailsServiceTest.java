package com.korogi.rest.service.anime;

import static com.korogi.core.util.ArrayUtil.concatenate;
import static com.korogi.dto.AnimeDTO.newAnimeDTO;
import static com.korogi.rest.service.matcher.DTOMatchers.containsResourceLinks;
import static com.korogi.rest.service.matcher.DTOMatchers.matchesAnimeDTO;
import static com.korogi.rest.service.matcher.DTOMatchers.matchesErrorDTO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.korogi.dto.AnimeDTO;
import com.korogi.rest.service.BaseServiceTest;
import org.junit.Test;

public class ConsultAnimePrequalDetailsServiceTest extends BaseServiceTest {
    private static final String URL = "/anime/{id}/prequal";

    /**
     * When consulting an existing an Anime's prequal's details, it should return:
     * OK http status
     * JSON content type
     * a JSON corresponding to the prequal of the Anime with the given id in the database
     * all default HATEOAS links plus the sequal link
     */
    @Test
    @DatabaseSetup("/com/korogi/rest/service/anime/ConsultAnimePrequalDetailsServiceTest_consultAnimePrequalDetails.xml")
    public void consultAnimePrequalDetails() throws Exception {
        AnimeDTO expectedAnimeDTO = newAnimeDTO()
                .nameEnglish("Steins;Gate")
                .nameRomanized("Steins;Gate")
                .startAir(LocalDate.of(2011, 4, 6))
                .endAir(LocalDate.of(2011, 9, 14))
                .synopsis("Steins;Gate synopsis here")
                .backdropUrl("http://backdrop.url.be/steins.gate")
                .posterUrl("http://poster.url.be/steins.gate")
                .build();

        performAndPrint(get(URL, 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", matchesAnimeDTO(expectedAnimeDTO)))
                .andExpect(jsonPath("$.links", containsResourceLinks(concatenate(EXPECTED_ANIME_DETAILS_LINKS, "sequal"))));

        hibernateStatisticsUtil.assertAmountOfQuerriesExecuted(3);
    }

    /**
     * When consulting an existing an Anime's prequal's details that has a prequal and a sequal, it should return:
     * OK http status
     * JSON content type
     * a JSON corresponding to the prequal of the Anime with the given id in the database
     * all default HATEOAS links plus the prequal and sequal link
     */
    @Test
    @DatabaseSetup("/com/korogi/rest/service/anime/ConsultAnimePrequalDetailsServiceTest_consultAnimePrequalDetails.xml")
    public void consultAnimePrequalDetails_withPrequalAndSequal() throws Exception {
        AnimeDTO expectedAnimeDTO = newAnimeDTO()
                .nameEnglish("Steins;Gate: Egoistic Poriomania")
                .nameRomanized("Steins;Gate: Oukoubakko no Poriomania")
                .startAir(LocalDate.of(2012, 2, 22))
                .endAir(LocalDate.of(2012, 2, 22))
                .synopsis("Steins;Gate: Egoistic Poriomania synopsis here")
                .backdropUrl("http://backdrop.url.be/steins.gate.egoistic.poriomania")
                .posterUrl("http://poster.url.be/steins.gate.egoistic.poriomania")
                .build();

        performAndPrint(get(URL, 3))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", matchesAnimeDTO(expectedAnimeDTO)))
                .andExpect(jsonPath("$.links", containsResourceLinks(concatenate(EXPECTED_ANIME_DETAILS_LINKS, "prequal", "sequal"))));

        hibernateStatisticsUtil.assertAmountOfQuerriesExecuted(3);
    }

    /**
     * When consulting an existing an Anime that has no prequal its details, it should return:
     * NOT FOUND http status
     * JSON content type
     * a JSON corresponding to an Error with status 'Not Found', code '404' and message saying 'Resource was not found'
     */
    @Test
    @DatabaseSetup("/com/korogi/rest/service/anime/ConsultAnimePrequalDetailsServiceTest_consultAnimePrequalDetails.xml")
    public void consultAnimePrequalDetails_hasNoPrequal() throws Exception {
        performAndPrint(get(URL, 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", matchesErrorDTO(RESOURCE_NOT_FOUND_ERROR_DTO)));

        hibernateStatisticsUtil.assertAmountOfQuerriesExecuted(1);
    }

    /**
     * When consulting a not existing Anime's details, it should return:
     * NOT FOUND http status
     * JSON content type
     * a JSON corresponding to an Error with status 'Not Found', code '404' and message saying 'Resource was not found'
     */
    @Test
    @DatabaseSetup("/com/korogi/rest/service/anime/ConsultAnimePrequalDetailsServiceTest_consultAnimePrequalDetails.xml")
    public void consultAnimePrequalDetails_notExisting() throws Exception {
        performAndPrint(get(URL, 99))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", matchesErrorDTO(RESOURCE_NOT_FOUND_ERROR_DTO)));

        hibernateStatisticsUtil.assertAmountOfQuerriesExecuted(1);
    }
}