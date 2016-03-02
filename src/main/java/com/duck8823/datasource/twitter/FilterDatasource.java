package com.duck8823.datasource.twitter;

import com.duck8823.datasource.AbstractDatasource;
import com.duck8823.model.twitter.Filter;
import com.duck8823.model.twitter.FilterRepository;
import com.duck8823.model.twitter.Filter_;
import com.duck8823.model.twitter.Filters;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by maeda on 2016/01/11.
 */
@Repository
public class FilterDatasource extends AbstractDatasource<Filter> implements FilterRepository {

	@Override
	public Optional<Filter> findById(Long id) {
		return null;
	}

	@Override
	public Filters list() {
		return new Filters(super.findAll());
	}

	@Override
	public void save(Filter filter){
		super.save(filter);
	}

	@Override
	public void delete(Filter entity) {
		super.delete(entity);
	}

}
