package com.web.dacn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.dacn.entity.book.Pdf;

 @Repository

public interface PdfRepository extends JpaRepository<Pdf, Long> {
	 PdfRepository save(PdfRepository pdfEntity);
	 List<Pdf> findByBookId(int id);
	boolean existsByBookId(Long bookId);
}
