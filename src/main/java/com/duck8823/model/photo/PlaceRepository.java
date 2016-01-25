package com.duck8823.model.photo;

import java.util.Optional;

/**
 * Created by maeda on 2016/01/23.
 */
public interface PlaceRepository {

	Optional<Place> findById(Long id);

	Places list();

	void save(Place place);

	void delete(Place place);
}
