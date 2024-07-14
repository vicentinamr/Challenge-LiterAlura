package com.literalura.bookcatalog.service;

import com.literalura.bookcatalog.model.domain.Author;
import com.literalura.bookcatalog.model.domain.Book;
import com.literalura.bookcatalog.model.dto.AuthorDto;
import com.literalura.bookcatalog.model.dto.BookDto;
import com.literalura.bookcatalog.model.dto.ResultDto;
import com.literalura.bookcatalog.provider.api.GutendexApi;
import com.literalura.bookcatalog.provider.repository.AuthorRepository;
import com.literalura.bookcatalog.provider.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class BookService {

    GutendexApi gutendexApi;
    DataConverter dataConverter;
    BookRepository bookRepository;
    AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.gutendexApi = new GutendexApi();
        this.dataConverter = new DataConverterImpl();
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public Book getBookByTitle(String title) {
        ResultDto bookResp = searchBookInGutendexApi(title);

        if (!bookResp.results().isEmpty()) {
            AuthorDto authorDto = bookResp.results().get(0).authors().get(0);
            Author savedAuthor = saveAuthor(new Author(authorDto));

            BookDto bookDto = bookResp.results().get(0);
            return saveBook(new Book(bookDto, savedAuthor));

        } else {
            System.out.println("Título não encontrado");
            return null;
        }
    }

    public ResultDto searchBookInGutendexApi(String title) {
        HttpRequest req = gutendexApi.createRequestToGetBook(title);
        HttpResponse<String> res = gutendexApi.getResponse(req);

        return dataConverter.obterDados(res.body(), ResultDto.class);
    }

    private Book saveBook(Book book) {
        if (!existsBookByTitle(book.getTitle())) {
            return bookRepository.save(book);
        }
        return bookRepository.findByTitle(book.getTitle());
    }

    private boolean existsBookByTitle(String title) {
        return bookRepository.existsByTitle(title);
    }

    private Author saveAuthor(Author author) {
        if (!existsAuthorByName(author.getName())) {
            return authorRepository.save(author);
        }
        return authorRepository.findByName(author.getName());
    }

    private boolean existsAuthorByName(String name) {
        return authorRepository.existsByName(name);
    }

    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    public List<Author> getAuthors() {
        return authorRepository.findAll();
    }

    public List<Author> getLivingAuthorsYear(Integer year) {
        return authorRepository.findLivingAuthorsByYear(year);
    }

    public List<Book> getBooksByLanguage(String language) {
        return bookRepository.findByLanguages(language);
    }
}
