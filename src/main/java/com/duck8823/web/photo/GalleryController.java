package com.duck8823.web.photo;

import com.duck8823.service.PhotoService;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import twitter4j.TwitterException;

import java.util.Date;
import java.util.Locale;

/**
 * Created by maeda on 2015/12/12.
 */
@Log4j
@Transactional
@Controller
@RequestMapping
public class GalleryController {

	@Autowired
	private PhotoService photoService;

	@RequestMapping
	public String top(Model model) throws TwitterException {
		model.addAttribute("photos", photoService.list());
		return "photo/gallery";
	}

	@ResponseBody
	@RequestMapping(value = "photo/{id}", produces = "image/jpeg")
	public byte[] show(@PathVariable Long id) {
		return photoService.findById(id).get().getImage();
	}

}
