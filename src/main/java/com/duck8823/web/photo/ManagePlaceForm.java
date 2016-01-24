package com.duck8823.web.photo;

import com.duck8823.model.photo.Place;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by maeda on 2016/01/24.
 */
@Data
public class ManagePlaceForm {

	@NotNull
	private Long id;

	private Place place;

	private String comment;

}
