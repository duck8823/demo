package com.duck8823.service;

import com.duck8823.model.photo.Place;
import com.duck8823.model.photo.PlaceRepository;
import com.duck8823.model.photo.Places;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by maeda on 2016/01/23.
 */
@Log4j
@Service
public class PlaceService extends DataSourceService {

	@Autowired
	private PlaceRepository placeRepository;


	@Transactional(readOnly = true)
	public Optional<Place> findById(Long id) {
		return placeRepository.findById(id);
	}

	public Places list() {
		return placeRepository.list();
	}

	public void save(Place place, Places places) {
		placeRepository.save(place);
		places.add(place);
	}

	public void delete(Long id, Places places) {
		Place place = placeRepository.findById(id).get();
		placeRepository.delete(place);
		places.remove(place);
	}

}
