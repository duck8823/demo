package com.duck8823.web.photo;

import com.duck8823.model.photo.Photo;
import com.duck8823.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

/**
 * Created by maeda on 2015/12/16.
 */
@Transactional
@RequestMapping("rotate")
@Controller
public class RotateController {

	@Autowired
	private PhotoService photoService;

	@RequestMapping
	public String index(Model model){
		model.addAttribute("photos", photoService.list());
		return "photo/rotate";
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "{id}", method = RequestMethod.POST)
	public void rotate(@PathVariable Long id) throws IOException {
		Photo photo = photoService.findById(id).get();
		photo.rotate();
		photoService.save(photo);
	}
}
