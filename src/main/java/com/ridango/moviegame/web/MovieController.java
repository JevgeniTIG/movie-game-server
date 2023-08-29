package com.ridango.moviegame.web;

import com.ridango.moviegame.dto.MovieDTO;
import com.ridango.moviegame.payload.GuessResponse;
import com.ridango.moviegame.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin
public class MovieController {

	private MovieService movieService;

	public MovieController(@Autowired MovieService movieService) {
		this.movieService = movieService;
	}


	@GetMapping("/movie-game/two-movies-for-comparison")
	public List<MovieDTO> getMoviesForComparison(@RequestParam() String difficultyMode,
												 @RequestParam() String categoryMode,
												 @RequestParam() Long movieWithHigherValueId) {

		return movieService.getMoviesForComparison(difficultyMode, categoryMode, movieWithHigherValueId);
	}


	@GetMapping("/movie-game/guess")
	public GuessResponse guessMovie(@RequestParam() String categoryMode,
									@RequestParam() Long supposedHigherValueMovieId,
									@RequestParam() Long supposedLowerValueMovieId,
									@RequestParam() int indexOfMovie) {
		return movieService.evaluateUserGuess(categoryMode, supposedHigherValueMovieId, supposedLowerValueMovieId, indexOfMovie);
	}


	@GetMapping("/movie-game/movie/get/{id}")
	public MovieDTO get(@PathVariable() Long id) {
		return movieService.get(id);
	}


}
