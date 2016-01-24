package com.duck8823.web.photo;

import com.duck8823.beans.propertyEditors.DataSourceFinderEditor;
import com.duck8823.model.photo.Photo;
import com.duck8823.model.photo.Place;
import com.duck8823.model.photo.Places;
import com.duck8823.service.PhotoService;
import com.duck8823.service.PlaceService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
		places = new Places(placeService.list());
		model.addAttribute("places", places);
		model.addAttribute("photos", photoService.list());
		return "photo/managePhotoPlace";
	}

	@RequestMapping(method = RequestMethod.POST, path = "add")
	public String add(Model model, @Validated Place place, BindingResult result) {
		if(result.hasErrors()){
			throw new IllegalStateException("入力値にエラーがあります.");
		}
		placeService.save(place);
		places.add(place);
		model.addAttribute("places", places);
		return "photo/_managePlaceModal :: places";
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "delete/{id}")
	public synchronized String delete(Model model, @PathVariable Long id) {
		Place place = placeService.findById(id).get();
		placeService.delete(place);
		places.remove(place);
		model.addAttribute("places", places);
		return "photo/_managePlaceModal :: places";
	}

	@RequestMapping(method = RequestMethod.GET, path = "items")
	public String items(Model model, @RequestParam Long photoId) {
		Photo photo = photoService.findById(photoId).get();
		places = new Places(placeService.list());
		model.addAttribute("photo", photo);
		model.addAttribute("places", places);
		return "photo/_managePhotoModal :: selectPlace";
	}

	@RequestMapping(method = RequestMethod.POST, path = "update")
	public String update(Model model, @Validated ManagePlaceForm form, BindingResult result) {
		if(result.hasErrors()){
			throw new IllegalStateException("入力値にエラーがあります. : " + form);
		}
		Photo photo = photoService.findById(form.getId()).get();
		BeanUtils.copyProperties(form, photo);
		photoService.save(photo);
		return "redirect:";
	}
}
