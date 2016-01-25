package com.duck8823.datasource.photo;

import com.duck8823.datasource.AbstractDatasource;
import com.duck8823.model.photo.*;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by maeda on 2016/01/23.
 */
@Repository
public class PlaceDatasource extends AbstractDatasource implements PlaceRepository {

	@Override
	public Optional<Place> findById(Long id) {
		return Optional.ofNullable(Place.class.cast(getSession().createCriteria(Place.class).add(Restrictions.eq(Place_.id.getName(), id)).uniqueResult()));
	}

	@Override
	public Places list() {
		return new Places(getSession().createCriteria(Place.class).list());
	}

	@Override
	public void save(Place place) {
		getSession().save(place);
	}

	@Override
	public void delete(Place place) {
		getSession().delete(place);
	}
}
