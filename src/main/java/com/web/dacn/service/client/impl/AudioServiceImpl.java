package com.web.dacn.service.client.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.web.dacn.dto.book.AudioDTO;
import com.web.dacn.dto.book.BookDTO;
import com.web.dacn.dto.user.AuthorDTO;
import com.web.dacn.entity.book.Audio;
import com.web.dacn.entity.book.Book;
import com.web.dacn.entity.user.Author;
import com.web.dacn.repository.AudioRepository;
import com.web.dacn.repository.BookRepository;
import com.web.dacn.repository.OnlineRepository;
import com.web.dacn.repository.PdfRepository;
import com.web.dacn.service.client.AudioService;

@Service
public class AudioServiceImpl implements AudioService{
	
	@Autowired
	private AudioRepository audioRepository;
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private PdfRepository pdfRepository;

	@Autowired
	private OnlineRepository onlineRepository;
	
	@Override
	public Page<BookDTO> findAudioBookByNameContainingAndCategories_slug(String search, String category,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<BookDTO> findAudioBookByCategories_slug(String category, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<BookDTO> findBookByBookNameContaining(String search, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<BookDTO> findAllAudioBook(Pageable pageable) {
		 Page<BookDTO> dtos  = bookRepository.findDistinctBookByAudiosIsNotNull(pageable).map(b->{
			 BookDTO dto = new BookDTO();
				dto.setId(b.getId());
			    return dto;
		 });
		    return dtos;
	}

	@Override
	public BookDTO findBookBySlug(String slug) {
		Optional<Book> optional = bookRepository.findBookBySlug(slug);
		
		if(optional.isPresent()) {
			BookDTO dto = new BookDTO();
			Book book = optional.get();
			dto.setId(book.getId());
			dto.setName(book.getName());
			dto.setSlug(book.getSlug());
			dto.setThumbnail(book.getThumbnail());
			if(book.getAuthors()!= null && book.getAuthors().size()>0) {
				for (Author author : book.getAuthors()) {
					AuthorDTO authorDTO = new AuthorDTO();
					authorDTO.setId(author.getId());
					authorDTO.setSlug(author.getSlug());
					authorDTO.setFullname(author.getFullname());
					Set<AuthorDTO> authors = new HashSet<>();
					authors.add(authorDTO);
					dto.setAuthors(authors);
				}
			}
			dto.setDescription(book.getDescription());
			return dto;
		}
		return null;
	}

//	@Override
//	public AudioDTO findAudioByBookAndChapter(Long bookId, Integer chapter) {
//		List<Audio> audios = audioRepository.findFisrt1ByBookIdOrderByPriorityAsc (bookId);
//
//		return null;
//	}

	@Override
	public List<AudioDTO> findAudiosByBook(Long bookId) {
		List<Audio> audios = audioRepository.findByBookIdOrderByPriorityAsc (bookId);
		List<AudioDTO> results= new ArrayList<>();
		audios.stream().forEach(audio->{
			AudioDTO dto = new AudioDTO();
			dto.setId(audio.getId());
			dto.setPriority(audio.getPriority());
			dto.setName(audio.getName());
			dto.setUrl(audio.getUrl());
			results.add(dto);
		});
		return results;
	}

	@Override
	public boolean existsPdf(Long bookId) {
		return pdfRepository.existsByBookId(bookId);
	}

	@Override
	public boolean existsEbook(Long bookId) {
		return onlineRepository.existsByBookId(bookId);
	}
	
	
}
