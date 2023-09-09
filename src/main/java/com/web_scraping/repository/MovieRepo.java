package com.web_scraping.repository;
import com.web_scraping.model.ImdbTopMovies;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepo extends JpaRepository<ImdbTopMovies, Long> {

}
