package com.ridango.moviegame.mapper;


import com.ridango.moviegame.dto.MovieDTO;
import com.ridango.moviegame.entity.Movie;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MovieToMovieDTOImpl implements MovieToMovieDTOMapper {


    @Override

    public MovieDTO map(Movie movie) {
        if (movie == null) {
            return null;
        }
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(movie.getId());
        movieDTO.setOriginalTitle(movie.getOriginalTitle());
        movieDTO.setVoteCount(movie.getVoteCount());

        return movieDTO;
    }

    public List<MovieDTO> mapAll(List<Movie> movies) {
        List<MovieDTO> movieDTOS = new ArrayList<>();
        movies.forEach(movie -> {
            MovieDTO movieDTO = map(movie);
            movieDTOS.add(movieDTO);
        });
        return movieDTOS;
    }
}
