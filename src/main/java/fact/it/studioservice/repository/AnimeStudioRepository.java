package fact.it.studioservice.repository;

import fact.it.studioservice.model.AnimeStudio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimeStudioRepository extends JpaRepository<AnimeStudio, String> {
    List<AnimeStudio> findAnimeStudiosByNameContainingIgnoreCase(String name);
    AnimeStudio findAnimeStudioById(int ID);
    List<AnimeStudio> findAnimeStudioBySeriesAmount(int amount);
}
