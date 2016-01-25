package com.duck8823.model.twitter;

import java.util.Optional;

/**
 * Created by maeda on 2016/01/11.
 */
public interface FilterRepository {

	Optional<Filter> findById(Long id);

	Filters list();

	void save(Filter filter);

	void delete(Filter filter);
}
