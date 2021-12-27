package fact.it.studioservice;

import fact.it.studioservice.model.AnimeStudio;
import fact.it.studioservice.repository.AnimeStudioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class StudioControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnimeStudioRepository animeStudioRepository;

    private AnimeStudio animeStudio1 = new AnimeStudio("Studio1",20);
    private AnimeStudio animeStudio2 = new AnimeStudio("Studio2",33);

    private List<AnimeStudio> AnimeStudios = Arrays.asList(animeStudio1, animeStudio2);
    private List<AnimeStudio> AnimeStudio2List = Arrays.asList(animeStudio2);

    @Test
    public void givenStudios_whenGetStudiosByAmount_thenReturnJsonStudios() throws Exception {
        given(animeStudioRepository.findAnimeStudioBySeriesAmount(33)).willReturn(AnimeStudio2List);
        mockMvc.perform(get("/studios/seriesProduced/{amount}",33))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name",is("Studio2")))
                .andExpect(jsonPath("$[0].seriesAmount",is(33)));
    }

    @Test
    public void givenStudios_whenGetStudiosByName_thenReturnJsonStudios() throws Exception {
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

}