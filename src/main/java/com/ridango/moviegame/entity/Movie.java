package com.ridango.moviegame.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
public class Movie {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Column(name = "original_title")
	private String originalTitle;

	@Column(name = "overview")
	private String overview;

	@Column(name = "popularity")
	private double popularity;

	@Column(name = "release_date")
	private LocalDate releaseDate;

	@Column(name = "revenue")
	private Long revenue;

	@Column(name = "runtime")
	private int runtime;

	@Column(name = "tagline")
	private String tagline;

	@Column(name = "title")
	private String title;

	@Column(name = "vote_average")
	private double voteAverage;

	@Column(name = "vote_count")
	private int voteCount;
}


