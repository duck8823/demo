package com.duck8823.datasource.twitter;

import com.duck8823.datasource.AbstractDatasource;
import com.duck8823.model.twitter.Filter;
import com.duck8823.model.twitter.FilterRepository;
import com.duck8823.model.twitter.Filter_;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by maeda on 2016/01/11.
 */
@Repository
public class FilterDatasource extends AbstractDatasource implements FilterRepository {

	@Override
	public Optional<Filter> findById(Long id) {
		return Optional.ofNullable(Filter.class.cast(getSession().createCriteria(Filter.class).add(Restrictions.eq(Filter_.id.getName(), id)).uniqueResult()));
	}

	@Override
	public List<Filter> list() {
		return Collections.unmodifiableList(getSession().createCriteria(Filter.class).list());
	}

	@Override
	public void save(Filter filter) {
		getSession().save(filter);
	}

	@Override
	public void delete(Filter filter) {
		getSession().delete(filter);
	}

}
