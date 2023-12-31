package com.ridango.moviegame.service;

import com.ridango.moviegame.constant.GameCategoryConstant;
import com.ridango.moviegame.constant.GameDifficultyConstant;
import com.ridango.moviegame.dto.MovieDTO;
import com.ridango.moviegame.entity.Movie;
import com.ridango.moviegame.mapper.MovieToMovieDTOImpl;
import com.ridango.moviegame.payload.GuessResponse;
import com.ridango.moviegame.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class MovieService {

	private final MovieRepository movieRepository;
	private final MovieToMovieDTOImpl movieMapper;

	public MovieService(@Autowired MovieRepository movieRepository,
						@Autowired MovieToMovieDTOImpl movieMapper) {
		this.movieRepository = movieRepository;
		this.movieMapper = movieMapper;
	}

	Random random = new Random();
	double closeRankValue = 0.02;

	public List<MovieDTO> getMoviesForComparison(String difficultyMode, String categoryMode, Long movieWithHigherValueId) {


		List<Movie> movies = movieRepository.findAll();

		Movie firstMovie = new Movie();

		/* it is not first round of the game, first movie exists */
		if (movieWithHigherValueId != 0) {
			firstMovie = movies.stream().
					filter(m -> m.getId().equals(movieWithHigherValueId))
					.findAny()
					.orElse(null);
		}
		/* first movie does not yet exist, meaning it is a first round of the game */
		else {
			firstMovie = movies.get(random.nextInt(movies.size() - 1));
		}

		/* firstly, we need to remove our first movie from the list to avoid id duplications with a second movie */
		Movie finalFirstMovie = firstMovie;
		movies.removeIf(m -> {
			assert finalFirstMovie != null;
			return m.getId().equals(finalFirstMovie.getId());
		});

		Movie secondMovie = getSecondMovieForComparison(movies, firstMovie, difficultyMode, categoryMode);

		List<Movie> twoMoviesForComparison = new ArrayList<>();
		twoMoviesForComparison.add(firstMovie);
		twoMoviesForComparison.add(secondMovie);

		return movieMapper.mapAll(twoMoviesForComparison);

	}


	private Movie getSecondMovieForComparison(List<Movie> movies, Movie firstMovie, String difficultyMode, String categoryMode) {

		/* game beginner mode logic*/
		if (Objects.equals(difficultyMode, GameDifficultyConstant.BEGINNER_MODE)) {
			return filterListOfMoviesByGameCategory(1, categoryMode, movies, firstMovie);
		}
		/* game advanced mode logic */
		else {
			return filterListOfMoviesByGameCategory(closeRankValue, categoryMode, movies, firstMovie);
		}

	}

	private Movie filterListOfMoviesByGameCategory(double closeRankValue, String categoryMode, List<Movie> movies, Movie firstMovie) {
		List<Movie> filteredMovies;

		/* advanced mode */
		if (closeRankValue != 1) {

			double dynamicCloseRankValue = closeRankValue;
			filteredMovies = filterMoviesForAdvancedMode(movies, firstMovie, categoryMode, dynamicCloseRankValue);

			/* If filtered movies has no elements we need to increase close rank value a bit to try to
			find at least one movie to choose from.
			**/
			while (filteredMovies.size() < 1) {
				dynamicCloseRankValue = dynamicCloseRankValue + closeRankValue;
				filteredMovies = filterMoviesForAdvancedMode(movies, firstMovie, categoryMode, dynamicCloseRankValue);
			}

		}
		/* beginner mode*/
		else {

			filteredMovies = switch (categoryMode) {

				/* filtering by popularity  */
				case GameCategoryConstant.POPULARITY -> movies.stream()
						.filter(m ->
								(m.getPopularity() != firstMovie.getPopularity()))
						.collect(Collectors.toList());
				/* filtering by run time*/
				case GameCategoryConstant.RUN_TIME -> movies.stream()
						.filter(m ->
								(m.getRuntime() != firstMovie.getRuntime()))
						.collect(Collectors.toList());
				/* filtering by revenue */
				case GameCategoryConstant.REVENUE -> movies.stream()
						.filter(m ->
								(!Objects.equals(m.getRevenue(), firstMovie.getRevenue())))
						.collect(Collectors.toList());
				/* filtering by vote average */
				default -> movies.stream()
						.filter(m ->
								(m.getVoteAverage() != firstMovie.getVoteAverage()))
						.collect(Collectors.toList());

			};
		}

		return filteredMovies.get(random.nextInt(filteredMovies.size() - 1));
	}


	private List<Movie> filterMoviesForAdvancedMode(List<Movie> movies, Movie firstMovie, String categoryMode, double dynamicCloseRankValue) {

		return switch (categoryMode) {

			/* filtering by popularity  */
			case GameCategoryConstant.POPULARITY -> movies.stream()
					.filter(m ->
							((firstMovie.getPopularity() - m.getPopularity() < firstMovie.getPopularity() * dynamicCloseRankValue) &&
									(m.getPopularity() - firstMovie.getPopularity() < firstMovie.getPopularity() * dynamicCloseRankValue) &&
									m.getPopularity() != firstMovie.getPopularity()))
					.collect(Collectors.toList());
			/* filtering by run time*/
			case GameCategoryConstant.RUN_TIME -> movies.stream()
					.filter(m ->
							((firstMovie.getRuntime() - m.getRuntime() < firstMovie.getRuntime() * dynamicCloseRankValue) &&
									(m.getRuntime() - firstMovie.getRuntime() < firstMovie.getRuntime() * dynamicCloseRankValue) &&
									m.getRuntime() != firstMovie.getRuntime()))
					.collect(Collectors.toList());
			/* filtering by revenue */
			case GameCategoryConstant.REVENUE -> movies.stream()
					.filter(m ->
							((firstMovie.getRevenue() - m.getRevenue() < firstMovie.getRevenue() * dynamicCloseRankValue) &&
									(m.getRevenue() - firstMovie.getRevenue() < firstMovie.getRevenue() * dynamicCloseRankValue) &&
									!Objects.equals(m.getRevenue(), firstMovie.getRevenue())))
					.collect(Collectors.toList());
			/* filtering by vote average */
			default -> movies.stream()
					.filter(m ->
							((firstMovie.getVoteAverage() - m.getVoteAverage() < firstMovie.getVoteAverage() * dynamicCloseRankValue) &&
									(m.getVoteAverage() - firstMovie.getVoteAverage() < firstMovie.getVoteAverage() * dynamicCloseRankValue) &&
									m.getVoteAverage() != firstMovie.getVoteAverage()))
					.collect(Collectors.toList());

		};

	}




	public GuessResponse evaluateUserGuess(String categoryMode, Long supposedHigherValueMovieId, Long supposedLowerValueMovieId, int indexOfMovie) {

		List<Movie> movies = movieRepository.findAll();
		Movie firstMovie = movies.stream().
				filter(m -> m.getId().equals(supposedHigherValueMovieId))
				.findAny()
				.orElse(null);
		Movie secondMovie = movies.stream().
				filter(m -> m.getId().equals(supposedLowerValueMovieId))
				.findAny()
				.orElse(null);
		assert firstMovie != null;
		assert secondMovie != null;

		GuessResponse guessResponse = new GuessResponse();
		boolean isCorrect;

		switch (categoryMode) {
			/* comparing by vote average */
			case GameCategoryConstant.VOTE_AVERAGE -> {
				isCorrect = firstMovie.getVoteAverage() > secondMovie.getVoteAverage();

				guessResponse.setSupposedHigherValue(firstMovie.getVoteAverage());
				guessResponse.setSupposedLowerValue(secondMovie.getVoteAverage());
			}
			/* comparing by popularity */
			case GameCategoryConstant.POPULARITY -> {
				isCorrect = firstMovie.getPopularity() > secondMovie.getPopularity();

				guessResponse.setSupposedHigherValue(firstMovie.getPopularity());
				guessResponse.setSupposedLowerValue(secondMovie.getPopularity());
			}
			/* comparing by run time */
			case GameCategoryConstant.RUN_TIME -> {
				isCorrect = firstMovie.getRuntime() > secondMovie.getRuntime();
				guessResponse.setSupposedHigherValue(firstMovie.getRuntime());
				guessResponse.setSupposedLowerValue(secondMovie.getRuntime());
			}
			/* comparing by revenue */
			default -> {
				isCorrect = firstMovie.getRevenue() > secondMovie.getRevenue();
				guessResponse.setSupposedHigherValue(firstMovie.getRevenue());
				guessResponse.setSupposedLowerValue(secondMovie.getRevenue());
			}
		}

		/* here we update movies' vote count values */
		firstMovie.setVoteCount(firstMovie.getVoteCount() + 1);
		secondMovie.setVoteCount(secondMovie.getVoteCount() + 1);

		List<Movie> moviesToBeUpdated = new ArrayList<>();
		moviesToBeUpdated.add(firstMovie);
		moviesToBeUpdated.add(secondMovie);
		movieRepository.saveAllAndFlush(moviesToBeUpdated);


		guessResponse.setCorrect(isCorrect);
		guessResponse.setIndexOfMovie(indexOfMovie);

		return guessResponse;

	}

	public MovieDTO get(Long id) {
		return movieMapper.map(movieRepository.findMovieById(id));
	}
}
