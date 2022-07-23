package com.example.externalapi.service;

import com.example.externalapi.model.Film;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class FilmService {

    @Autowired
    private RestTemplate restTemplate = new RestTemplate();

    Film[] filmArray = restTemplate.getForObject("https://ghibliapi.herokuapp.com/films/", Film[].class);




    public Object[] findAllFilmscomplete() {
        return restTemplate.getForObject("https://ghibliapi.herokuapp.com/films/", Object[].class);
    }

    public Film[] findAllFilms() {
        return restTemplate.getForObject("https://ghibliapi.herokuapp.com/films/", Film[].class);
    }

    public List<Film> findFilmsByTitle(String title) {
        List<Film> filmList = new ArrayList<>();
        Film[] forObject = restTemplate.getForObject("https://ghibliapi.herokuapp.com/films/", Film[].class);
        for (int i = 0; i < forObject.length; i++) {
            Film film = forObject[i];
            String englishTitle = film.getTitle().toLowerCase();
            String romajiTitle = film.getOriginal_title_romanised().toLowerCase();
            if (englishTitle.contains(title.toLowerCase()) || romajiTitle.contains(title.toLowerCase())) {
                filmList.add(film);
            }
        }
        return filmList;
    }

    public Map<Integer, String> findDirectors() {
        Map<Integer, String> map = new HashMap<>();
        List<Film> filmList = Arrays.asList(filmArray);
        int mapkey = 1;
        for (Film film : filmList) {
            String director = film.getDirector();
            if (!map.containsValue(director)) {
                map.put(mapkey, director);
                mapkey++;
            }
        }
        return map;
    }

    public List<Film> findFilmsByNewest() {
        List<Film> filmList = Arrays.asList(filmArray);
        sortFilmsByReleaseDate(filmList);
        return filmList;
    }

    private List<Film> sortFilmsByReleaseDate(List<Film> list) {
        list.sort(Comparator.comparing(film -> film.getRelease_date()));
        Collections.reverse(list);
        return list;
    }
}
