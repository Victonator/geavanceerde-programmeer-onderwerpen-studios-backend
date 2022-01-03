package fact.it.studioservice.model;

public class AnimeStudioDTO {
    private int id;
    private String name;
    private int seriesAmount;

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

    public void setSeriesAmount(int amount) {
        this.seriesAmount = amount;
    }
}
