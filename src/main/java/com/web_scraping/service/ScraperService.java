package com.web_scraping.service;
import com.web_scraping.model.ImdbTopMovies;
import com.web_scraping.repository.MovieRepo;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
@Service
@AllArgsConstructor
public class ScraperService {

    @Autowired
    private MovieRepo movieRepo;
    private static final String URL = "https://www.imdb.com/chart/top/";
    private final ChromeDriver driver;
    private final String imdbNameXapth="//*[@id=\"__next\"]/main/div/div[3]/section/div/div[2]/div/ul/";

    @PostConstruct
    void postConstruct(){
        scrape();
    }

    public void scrape() {
        driver.get(URL);

        List<ImdbTopMovies> movies = new ArrayList<>();
        List<String> moviesURLs = new ArrayList<>();
        for (int i = 1; i <= 250 ; i++) {

            final WebElement words = driver.findElement(By.xpath(imdbNameXapth+"li[" + i + "]/div[2]/div/div/div[1]/a/h3"));
//            final WebElement words = driver.findElement(By.cssSelector(".sc-b85248f1-0 > div  a"));
            String movielist = words.getText();
            String [] split = movielist.split("[.]");

            final WebElement year = driver.findElement(By.xpath("//*[@id=\"__next\"]/main/div/div[3]/section/div/div[2]/div/ul/li[" + i + "]/div[2]/div/div/div[2]/span[1]"));
            final WebElement duration = driver.findElement(By.xpath("//*[@id=\"__next\"]/main/div/div[3]/section/div/div[2]/div/ul/li[" + i +"]/div[2]/div/div/div[2]/span[2]"));

            String guidance_rating;
            try {
                 guidance_rating = driver.findElement(By.xpath("//*[@id=\"__next\"]/main/div/div[3]/section/div/div[2]/div/ul/li["+i+"]/div[2]/div/div/div[2]/span[3]")).getText();
            }catch (Exception e){
                guidance_rating ="Null";
            }

            final WebElement rating = driver.findElement(By.xpath("//*[@id=\"__next\"]/main/div/div[3]/section/div/div[2]/div/ul/li["+i+"]/div[2]/div/div/span/div/span"));
            String rate = rating.getText();
            String [] rate_all = rate.split("[(]");

            final WebElement description = driver.findElement(By.xpath(imdbNameXapth + "li[" + i + "]/div[2]/div/div/div[1]/a"));
            String singleUrl = description.getAttribute("href");
            moviesURLs.add(singleUrl);

            movies.add(new ImdbTopMovies(null, split[1], year.getText(), duration.getText(), guidance_rating, rate_all[0], null));

        }
        int urlNumber = 0;
        for (String link : moviesURLs) {
            driver.get(link);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0,4000)");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
            try {
                String descriptionText = (wait.until(ExpectedConditions
                        .presenceOfElementLocated(By.cssSelector(".sc-ab73d3b3-0 > div > div>div>div"))).getText());

                movies.get(urlNumber).setDescription(descriptionText);

                urlNumber+=1;
            } catch (Exception e) {
                System.out.println("Failed to get description"+e.getMessage());
            }
        }
        System.out.println(movies);
        movieRepo.saveAll(movies);
        driver.quit();

   }
}
