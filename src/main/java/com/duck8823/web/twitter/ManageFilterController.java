package com.duck8823.web.twitter;

import com.duck8823.model.twitter.Filter;
import com.duck8823.model.twitter.FilterType;
import com.duck8823.model.twitter.Filters;
import com.duck8823.service.FilterService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by maeda on 2016/01/11.
 */
@Transactional
@Log4j
@RequestMapping("manage/twitter/filter")
@Controller
public class ManageFilterController {

	@Autowired
	private FilterService filterService;

	@Autowired
	private Filters filters;

	@RequestMapping(method = RequestMethod.GET)
	public String index(Model model){
		filters = filterService.list();
		model.addAttribute("filters", filters);
		model.addAttribute("filterTypes", FilterType.values());
		return "twitter/manageFilter";
	}

	@RequestMapping(method = RequestMethod.POST, path = "add")
	public String add(Model model, @Validated Filter filter, BindingResult result) {
		if(result.hasErrors()){
			throw new IllegalStateException("入力値に問題があります.");
		}
		filterService.save(filter, filters);
		model.addAttribute("filters", filters);
		return "twitter/manageFilter :: filters";
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "delete/{id}")
	public synchronized String delete(Model model, @PathVariable Long id) {
		filterService.delete(id, filters);
		model.addAttribute("filters", filters);
		return "twitter/manageFilter :: filters";
	}
}
