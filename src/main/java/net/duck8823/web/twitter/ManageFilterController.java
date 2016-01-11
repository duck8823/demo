package net.duck8823.web.twitter;

import lombok.extern.log4j.Log4j;
import net.duck8823.model.twitter.Filter;
import net.duck8823.model.twitter.FilterType;
import net.duck8823.model.twitter.Filters;
import net.duck8823.service.FilterService;
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
		filters = new Filters(filterService.list());
		model.addAttribute("filters", filters);
		model.addAttribute("filterTypes", FilterType.values());
		return "twitter/manageFilter";
	}

	@RequestMapping(method = RequestMethod.POST, path = "add")
	public String add(Model model, @Validated Filter filter, BindingResult result) {
		if(result.hasErrors()){
			log.warn("入力値にエラーがあります.");
			return "redirect:/";
		}
		filterService.save(filter);
		filters.add(filter);
		model.addAttribute("filters", filters);
		return "twitter/manageFilter :: filters";
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "delete/{id}")
	public String delete(Model model, @PathVariable Long id) {
		Filter filter = filterService.findById(id).get();
		filterService.delete(filter);
		filters.remove(filter);
		model.addAttribute("filters", filters);
		return "twitter/manageFilter :: filters";
	}
}
