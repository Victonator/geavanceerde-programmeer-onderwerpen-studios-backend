package fact.it.studioservice;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class StudioControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AnimeStudioRepository animeStudioRepository;

    private ObjectMapper mapper = new ObjectMapper();

    private AnimeStudio animeStudio1 = new AnimeStudio("Studio1",20);
    private AnimeStudio animeStudio2 = new AnimeStudio("Studio2",33);
    private AnimeStudio animeStudio3 = new AnimeStudio("Studio3",2);

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
    void givenStudios_whenGetStudiosByName_thenReturnJsonStudios() throws Exception {
        mockMvc.perform(get("/studios/{name}","studio"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name",is("Studio1")))
                .andExpect(jsonPath("$[0].seriesAmount",is(20)))
                .andExpect(jsonPath("$[1].name",is("Studio2")))
                .andExpect(jsonPath("$[1].seriesAmount",is(33)));
    }

    @Test
    void givenStudios_whenGetStudiosBySeriesAmount_thenReturnJsonStudios() throws Exception {
        mockMvc.perform(get("/studios/seriesProduced/{amount}","20"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name",is("Studio1")))
                .andExpect(jsonPath("$[0].seriesAmount",is(20)));
    }

    @Test
    void givenStudios_whenGetStudios_thenReturnJsonStudios() throws Exception {
        mockMvc.perform(get("/studios"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name",is("Studio1")))
                .andExpect(jsonPath("$[0].seriesAmount",is(20)))
                .andExpect(jsonPath("$[1].name",is("Studio2")))
                .andExpect(jsonPath("$[1].seriesAmount",is(33)));
    }

    @Test
    void givenStudio_whenAddStudio_thenReturnJsonStudio() throws Exception {
        mockMvc.perform(post("/studios")
                        .content(mapper.writeValueAsString(animeStudio3))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is("Studio3")))
                .andExpect(jsonPath("$.seriesAmount",is(2)));
    }

    @Test
    void givenStudio_whenUpdateStudio_thenReturnJsonStudio() throws Exception {
        AnimeStudio changedAnimeStudio3 = animeStudio3;

        mockMvc.perform(put("/studios/{Id}",animeStudio1.getId())
                .content(mapper.writeValueAsString(changedAnimeStudio3))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is("Studio3")))
                .andExpect(jsonPath("$.seriesAmount",is(2)));
    }

    @Test
    void givenStudio_whenDeleteStudio_thenStatusOk() throws Exception {
        mockMvc.perform(delete("/studios/{Id}",animeStudio2.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void givenNoStudio_whenDeleteStudio_thenStatusNotFound() throws Exception {
        mockMvc.perform(delete("/studios/{Id}",-10))
                .andExpect(status().isNotFound());
    }
}
