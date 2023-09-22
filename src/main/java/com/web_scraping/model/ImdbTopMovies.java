package com.web_scraping.model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Data
public class ImdbTopMovies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String movieName;
    private String year;
    private String duration;
    private String guidance_rating;
    private String rating;

    @Column(columnDefinition = "varchar(max)")
    private String description;




    public ImdbTopMovies(Long id, String movieName, String year, String duration, String guidance_rating
            , String rating, String description) {

        //check commit
        // imran is bloody programmer
        // kenju

        this.id = id;
        this.movieName = movieName;
        this.year = year;
        this.duration = duration;
        this.guidance_rating = guidance_rating;
        this.rating = rating;
        this.description = description;
    }


}
