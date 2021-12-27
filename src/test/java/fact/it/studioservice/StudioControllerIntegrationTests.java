package fact.it.studioservice;

import fact.it.studioservice.model.AnimeStudio;
import fact.it.studioservice.repository.AnimeStudioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class StudioControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AnimeStudioRepository animeStudioRepository;

    private AnimeStudio animeStudio1 = new AnimeStudio("Studio1",20);
    private AnimeStudio animeStudio2 = new AnimeStudio("Studio2",33);

    @BeforeEach
    public void beforeAllTests() {
        animeStudioRepository.deleteAll();
        animeStudioRepository.save(animeStudio1);
        animeStudioRepository.save(animeStudio2);
    }

    @AfterEach
    public void afterAllTests() {
        //Watch out with deleteAll() methods when you have other data in the test database!
        animeStudioRepository.deleteAll();
    }

    @Test
    public void givenStudios_whenGetStudiosByName_thenReturnJsonStudios() throws Exception {
        mockMvc.perform(get("/studios/{name}","Studio"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name",is("Studio1")))
                .andExpect(jsonPath("$[0].seriesAmount",is(20)))
                .andExpect(jsonPath("$[1].name",is("Studio2")))
                .andExpect(jsonPath("$[1].seriesAmount",is(33)));
    }

    @Test
    public void givenStudios_whenGetStudiosBySeriesAmount_thenReturnJsonStudios() throws Exception {
        mockMvc.perform(get("/studios/seriesProduced/{amount}","20"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name",is("Studio1")))
                .andExpect(jsonPath("$[0].seriesAmount",is(20)));
    }


}
