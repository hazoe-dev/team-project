package com.web.dacn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.web.dacn.entity.book.Audio;
import com.web.dacn.entity.book.Book;
@Repository
public interface AudioRepository extends JpaRepository<Audio, Long> {
	@Query(value="select * from audio where book_id=?1",nativeQuery = true)
	List<Audio> findByBookId(int bookId);

	List<Audio> findByBookIdOrderByPriorityAsc(Long bookId);

}

