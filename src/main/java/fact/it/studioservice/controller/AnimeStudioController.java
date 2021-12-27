package fact.it.studioservice.controller;

import fact.it.studioservice.model.AnimeStudio;
import fact.it.studioservice.model.AnimeStudioDTO;
import fact.it.studioservice.repository.AnimeStudioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
public class AnimeStudioController {

    @Autowired
    private AnimeStudioRepository animeStudioRepository;

    @PostConstruct
    public void fillDB() {
        if (animeStudioRepository.count() == 0) {
            animeStudioRepository.save(new AnimeStudio("Studio Ghibli", 52));
            animeStudioRepository.save(new AnimeStudio("CoMix Wave Films", 86));
            animeStudioRepository.save(new AnimeStudio("TNK", 54));
            animeStudioRepository.save(new AnimeStudio("Kyoto Animation", 133));
            animeStudioRepository.save(new AnimeStudio("White Fox", 44));
        }
    }

    @GetMapping("/studios")
    public List<AnimeStudio> getStudios() {
        return animeStudioRepository.findAll();
    }


    @GetMapping("/studios/{name}")
    public List<AnimeStudio> getStudiosByName(@PathVariable String name) {
        return animeStudioRepository.findAnimeStudiosByNameContaining(name);
    }

    @GetMapping("/studios/seriesProduced/{amount}")
    public List<AnimeStudio> getStudiosBySeriesProduced(@PathVariable int amount) {
        return animeStudioRepository.findAnimeStudioBySeriesAmount(amount);
    }

    @PostMapping("/studios")
    public AnimeStudio addStudio(@RequestBody AnimeStudioDTO animeStudioDTO) {
        AnimeStudio animeStudio = new AnimeStudio(animeStudioDTO);
        animeStudioRepository.save(animeStudio);
        return animeStudio;
    }

    @PutMapping("/studios")
    public AnimeStudio updateStudio(@RequestBody AnimeStudioDTO animeStudioDTO) {
        AnimeStudio updatedStudio = new AnimeStudio(animeStudioDTO);
        AnimeStudio studio = animeStudioRepository.findAnimeStudioById(updatedStudio.getId());
        studio.setName(updatedStudio.getName());
        studio.setSeriesAmount(updatedStudio.getSeriesAmount());
        animeStudioRepository.save(studio);
        return studio;
    }

    @DeleteMapping("/studios/{Id}")
    public ResponseEntity deleteStudio(@PathVariable int Id) {
        AnimeStudio studio = animeStudioRepository.findAnimeStudioById(Id);
        if (studio != null) {
            animeStudioRepository.delete(studio);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
