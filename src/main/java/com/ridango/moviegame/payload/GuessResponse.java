package com.ridango.moviegame.payload;

import lombok.Data;

@Data
public class GuessResponse {
	private boolean isCorrect;
	private double supposedHigherValue;
	private double supposedLowerValue;
	private int indexOfMovie;

}
