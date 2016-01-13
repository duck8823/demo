package com.duck8823.model.twitter;

import java.util.List;
import java.util.Optional;

/**
 * Created by maeda on 2016/01/11.
 */
public interface FilterRepository {

	Optional<Filter> findById(Long id);

	List<Filter> list();

	void save(Filter filter);

	void delete(Filter filter);
}
