package com.literalura.bookcatalog.provider.repository;

import com.literalura.bookcatalog.model.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByTitle(String title);

    Book findByTitle(String title);

    List<Book> findByLanguages(String language);
}
