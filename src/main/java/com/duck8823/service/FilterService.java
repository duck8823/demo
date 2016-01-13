package com.duck8823.service;

import com.duck8823.model.twitter.Filter;
import com.duck8823.model.twitter.FilterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by maeda on 2016/01/11.
 */
@Service
public class FilterService {

	@Autowired
	private FilterRepository filterRepository;

	public Optional<Filter> findById(Long id) {
		return filterRepository.findById(id);
	}

	public List<Filter> list() {
		return filterRepository.list();
	}

	public void save(Filter filter) {
		filterRepository.save(filter);
	}

	public void delete(Filter filter) {
		filterRepository.delete(filter);
	}
}
