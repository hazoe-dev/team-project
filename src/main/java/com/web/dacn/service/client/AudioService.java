package com.web.dacn.service.client;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.web.dacn.dto.book.AudioDTO;
import com.web.dacn.dto.book.BookDTO;
import com.web.dacn.entity.book.Book;

public interface AudioService {

	public Page<BookDTO> findAudioBookByNameContainingAndCategories_slug(String search, String category,
			Pageable pageable);

	public Page<BookDTO> findAudioBookByCategories_slug(String category, Pageable pageable);

	public Page<BookDTO> findBookByBookNameContaining(String search, Pageable pageable);

	public Page<BookDTO> findAllAudioBook(Pageable pageable);

	public BookDTO findBookBySlug(String slug);

	public List<AudioDTO> findAudiosByBook(Long bookId);

	public boolean existsPdf(Long id);

	public boolean existsEbook(Long id);
}
