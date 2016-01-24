package com.duck8823.model.photo;

import java.util.List;
import java.util.Optional;

/**
 * Created by maeda on 2016/01/23.
 */
public interface PlaceRepository {

	Optional<Place> findById(Long id);

	List<Place> list();

	void save(Place place);

	void delete(Place place);
}
