package fact.it.studioservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import fact.it.studioservice.model.AnimeStudio;
import fact.it.studioservice.repository.AnimeStudioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import javax.print.attribute.standard.Media;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class StudioControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnimeStudioRepository animeStudioRepository;

    private ObjectMapper mapper = new ObjectMapper();

    private AnimeStudio animeStudio1 = new AnimeStudio("Studio1",20);
    private AnimeStudio animeStudio2 = new AnimeStudio("Studio2",33);
    private AnimeStudio animeStudio3 = new AnimeStudio("Studio3",2);

    private List<AnimeStudio> AnimeStudios = Arrays.asList(animeStudio1, animeStudio2);
    private List<AnimeStudio> AnimeStudio2List = Arrays.asList(animeStudio2);

    @Test
    void givenStudios_whenGetStudiosByAmount_thenReturnJsonStudios() throws Exception {
        given(animeStudioRepository.findAnimeStudioBySeriesAmount(33)).willReturn(AnimeStudio2List);
        mockMvc.perform(get("/studios/seriesProduced/{amount}",33))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name",is("Studio2")))
                .andExpect(jsonPath("$[0].seriesAmount",is(33)));
    }

    @Test
    void givenStudios_whenGetStudiosByName_thenReturnJsonStudios() throws Exception {
        given(animeStudioRepository.findAnimeStudiosByNameContaining("Studio")).willReturn(AnimeStudios);
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
    void givenStudios_whenGetStudios_thenReturnJsonStudios() throws Exception {
        given(animeStudioRepository.findAll()).willReturn(AnimeStudios);
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
        given(animeStudioRepository.save(animeStudio3)).willReturn(animeStudio3);
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
        changedAnimeStudio3.setSeriesAmount(5);
        given(animeStudioRepository.findAnimeStudioById(animeStudio3.getId())).willReturn(changedAnimeStudio3);
        mockMvc.perform(put("/studios/{Id}",animeStudio3.getId())
                .content(mapper.writeValueAsString(changedAnimeStudio3))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is("Studio3")))
                .andExpect(jsonPath("$.seriesAmount",is(5)));
    }

    @Test
    void givenStudio_whenDeleteStudio_thenStatusOk() throws Exception {
        given(animeStudioRepository.findAnimeStudioById(3)).willReturn(animeStudio3);
        mockMvc.perform(delete("/studios/{Id}",3))
                .andExpect(status().isOk());
    }

    @Test
    void givenNoStudio_whenDeleteStudio_thenStatusNotFound() throws Exception {
        given(animeStudioRepository.findAnimeStudioById(3)).willReturn(null);
        mockMvc.perform(delete("/studios/{Id}",3))
                .andExpect(status().isNotFound());
    }
}