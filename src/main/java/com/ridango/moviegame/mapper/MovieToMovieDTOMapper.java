package com.ridango.moviegame.mapper;

import com.ridango.moviegame.dto.MovieDTO;
import com.ridango.moviegame.entity.Movie;
import org.mapstruct.Mapper;

@Mapper
public interface MovieToMovieDTOMapper {

    MovieDTO map(Movie movie);
}
