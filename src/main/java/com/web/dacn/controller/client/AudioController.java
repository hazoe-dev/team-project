package com.web.dacn.controller.client;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.web.dacn.dto.book.AudioDTO;
import com.web.dacn.dto.book.BookDTO;
import com.web.dacn.entity.book.Book;
import com.web.dacn.service.client.AudioService;

@Controller
@RequestMapping("/audio")
public class AudioController {
	@Autowired
	private AudioService audioService;

	@GetMapping("/{slug}")
	private ModelAndView getAudioBook(@PathVariable(value = "slug") String slug) {
		
		ModelAndView modelAndView = new ModelAndView();
		try {
			BookDTO bookDTO = audioService.findBookBySlug(slug);
			if(bookDTO == null)
				modelAndView.setViewName("404");
			
			List<AudioDTO> audioDTOs = audioService.findAudiosByBook(bookDTO.getId());
			
			if(audioDTOs == null || audioDTOs.size()<1) {
				modelAndView.setViewName("404");
			}
			
			AudioDTO audioSelected = audioDTOs.get(0);
			audioDTOs.remove(0);
			modelAndView.addObject("bookDTO", bookDTO);
			modelAndView.addObject("audioSelected", audioSelected);
			modelAndView.addObject("audioDTOs", audioDTOs);
			
			modelAndView.addObject("existsPdf", audioService.existsPdf(bookDTO.getId()));
			modelAndView.addObject("existsEbook", audioService.existsEbook(bookDTO.getId()));
			
			modelAndView.setViewName("audioPage");
		} catch (Exception e) {
			modelAndView.setViewName("404");
		}
		return modelAndView;
	}
	
	@GetMapping("/all")
	public ModelAndView getAudioBooks (
			@RequestParam(name ="s", required = false) String search,
			@RequestParam(name ="sort", required = false) String sort,
			@RequestParam(name= "page") Optional<Integer> page,
			@RequestParam(name= "size") Optional<Integer> size,
			@RequestParam(name = "category", required = false) String category
			) {
		ModelAndView mav = new ModelAndView("listAudioPage");		
		
		int pageSize = size.orElse(9);
		mav.addObject("size", pageSize);
		int currentPage = page.orElse(1);
		Pageable pageable;
		if(StringUtils.hasText(sort)) {
			String field = sort.trim().split("_")[0];
			String type =  sort.trim().split("_")[1];
			if(type.contentEquals("desc")) {
				pageable = PageRequest.of(currentPage-1, pageSize, Sort.by(field).descending());
			}else {
				pageable = PageRequest.of(currentPage-1, pageSize, Sort.by(field).ascending());
			}
			mav.addObject("sort", sort);
		}else {
			pageable = PageRequest.of(currentPage-1, pageSize, Sort.by("id").descending());
		}
		Page<BookDTO> results = null;
		
		if (StringUtils.hasText(category) && StringUtils.hasText(search)) {
			results = audioService.findAudioBookByNameContainingAndCategories_slug(search,category, pageable);
			mav.addObject("search", search);
			mav.addObject("category", category);
		}else if (StringUtils.hasText(search)) {
			results = audioService.findBookByBookNameContaining(search, pageable);
			mav.addObject("search", search);
		}else if (StringUtils.hasText(category)){
			results = audioService.findAudioBookByCategories_slug(category, pageable);
			mav.addObject("category", category);
		}  else {
			results = audioService.findAllAudioBook(pageable);
		}
		int totalPages = results.getTotalPages();
		if (totalPages > 0) {
			int start = Math.max(1, currentPage-2);
			int end = Math.min(currentPage + 2, totalPages);
			if (totalPages > 5) {
				if (end == totalPages)
					start = end - 5;
				else if (start == 1)
					end = start + 5;
			}
			List<Integer> pages = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
			mav.addObject("pages", pages);
		} 
		mav.addObject("results", results);
		
		return mav;
	}
}
