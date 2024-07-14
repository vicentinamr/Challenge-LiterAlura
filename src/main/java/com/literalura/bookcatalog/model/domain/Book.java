package com.literalura.bookcatalog.model.domain;

import com.literalura.bookcatalog.model.dto.BookDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.StringJoiner;

@Entity
@Table(name = "BOOKS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String languages;
    private Integer downloads;

    @ManyToOne
    @JoinColumn(name = "AUTHOR_ID", referencedColumnName = "ID")
    private Author author;

    public Book(BookDto bookDto, Author author) {
        this.title = bookDto.title();
        this.languages = bookDto.languages().get(0);
        this.downloads = bookDto.downloads();
        this.author = author;
    }

    @Override
    public String toString() {
        return this.title;
    }
}
