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
public class PlaceDatasource extends AbstractDatasource<Place> implements PlaceRepository {

	@Override
	public Optional<Place> findById(Long id) {
		return Optional.ofNullable(Place.class.cast(criteria().add(Restrictions.eq(Place_.id.getName(), id)).uniqueResult()));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Places list() {
		return new Places(super.findAll());
	}

	@Override
	public void save(Place place){
		super.save(place);
	}

	@Override
	public void delete(Place place) {
		super.delete(place);
	}

}
