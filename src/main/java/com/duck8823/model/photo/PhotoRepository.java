package com.duck8823.model.photo;

import java.util.Optional;

/**
 * Created by maeda on 2015/12/12.
 */
public interface PhotoRepository {

	Optional<Photo> findById(Long id);

	Optional<Photo> random();

	Photos list();

	void save(Photo photo);

	void delete(Photo photo);

}
