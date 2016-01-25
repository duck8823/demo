package com.duck8823.web.photo;

import com.duck8823.beans.propertyEditors.DataSourceFinderEditor;
import com.duck8823.model.photo.Place;
import com.duck8823.model.photo.Places;
import com.duck8823.service.PhotoService;
import com.duck8823.service.PlaceService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

/**
 * Created by maeda on 2016/01/23.
 */
@Log4j
@Transactional
@RequestMapping("manage/photo/place")
@Controller
public class ManagePlaceController {

	@Autowired
	private PhotoService photoService;

	@Autowired
	private PlaceService placeService;

	@Autowired
	private Places places;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Place.class, new DataSourceFinderEditor(placeService, true));
	}

	@RequestMapping
	public String index(Model model){
		places = placeService.list();
		model.addAttribute("places", places);
		model.addAttribute("photos", photoService.list());
		return "photo/managePhotoPlace";
	}

	@RequestMapping(method = RequestMethod.POST, path = "add")
	public String add(Model model, @Validated Place place, BindingResult result) {
		if(result.hasErrors()){
			throw new IllegalStateException("入力値にエラーがあります.");
		}
		placeService.save(place, places);
		model.addAttribute("places", places);
		return "photo/_managePlaceModal :: places";
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "delete/{id}")
	public synchronized String delete(Model model, @PathVariable Long id) {
		placeService.delete(id, places);
		model.addAttribute("places", places);
		return "photo/_managePlaceModal :: places";
	}

	@RequestMapping(method = RequestMethod.GET, path = "items")
	public String items(Model model, @RequestParam Long photoId) {
		model.addAttribute("photo", photoService.get(photoId));
		model.addAttribute("places", placeService.list());
		return "photo/_managePhotoModal :: selectPlace";
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(method = RequestMethod.POST, path = "addInfo")
	public void addInfo(Model model, @Validated ManagePlaceForm form, BindingResult result) {
		if(result.hasErrors()){
			throw new IllegalStateException("入力値にエラーがあります. : " + form);
		}
		photoService.addInfo(form);
	}
}
