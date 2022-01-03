package fact.it.studioservice.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AnimeStudio {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    private String name;

    private int seriesAmount;

    public AnimeStudio(){}

    public AnimeStudio(String name, int seriesAmount){
        setName(name);
        setSeriesAmount(seriesAmount);
    }

    public AnimeStudio(AnimeStudioDTO animeStudioDTO) {
        setId(animeStudioDTO.getId());
        setName(animeStudioDTO.getName());
        setSeriesAmount(animeStudioDTO.getSeriesAmount());
    }

    public int getId() {
        return id;
    }

    public void setId(int id){this.id = id;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeriesAmount() {
        return seriesAmount;
    }

    public void setSeriesAmount(int seriesAmount) {
        this.seriesAmount = seriesAmount;
    }
}
