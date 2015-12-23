package net.duck8823.web.photo;

import net.duck8823.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import twitter4j.TwitterException;

/**
 * Created by maeda on 2015/12/12.
 */
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
