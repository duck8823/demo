package com.duck8823.service;

import com.duck8823.model.twitter.Filter;
import com.duck8823.model.twitter.FilterRepository;
import com.duck8823.model.twitter.Filters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by maeda on 2016/01/11.
 */
@Service
public class FilterService extends DataSourceService {

	@Autowired
	private FilterRepository filterRepository;

	public Optional<Filter> findById(Long id) {
		return filterRepository.findById(id);
	}

	public Filters list() {
		return filterRepository.list();
	}

	public void save(Filter filter, Filters filters) {
		filterRepository.save(filter);
		filters.add(filter);
	}

	public void delete(Long id, Filters filters) {
		Filter filter = findById(id).get();
		filterRepository.delete(filter);
		filters.remove(filter);
	}
}
