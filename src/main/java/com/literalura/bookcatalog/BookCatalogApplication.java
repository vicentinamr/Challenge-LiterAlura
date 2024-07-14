package com.literalura.bookcatalog;

import com.literalura.bookcatalog.model.domain.Author;
import com.literalura.bookcatalog.model.domain.Book;
import com.literalura.bookcatalog.provider.repository.AuthorRepository;
import com.literalura.bookcatalog.provider.repository.BookRepository;
import com.literalura.bookcatalog.service.BookService;
import com.literalura.bookcatalog.service.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class BookCatalogApplication implements CommandLineRunner {

	@Autowired
	BookRepository bookRepository;
	@Autowired
	AuthorRepository authorRepository;

	public static void main(String[] args) {
		SpringApplication.run(BookCatalogApplication.class, args);
	}

	@Override
	public void run(String... args) {
		var menu = new Menu();
		BookService bookService = new BookService(bookRepository, authorRepository);

		while (true) {

			switch (menu.showMenu()) {
				case "1" -> getBookByTitle(menu, bookService);
				case "2" -> getBooks(menu, bookService);
				case "3" -> getAuthors(bookService);
				case "4" -> getLivingAuthorsYear(menu, bookService);
				case "5" -> {
					System.out.println("Digitou 5");
					getBooksByLanguage(menu, bookService);
				}
				case "0" -> {
					System.out.println("Finalizando programa...");
					System.exit(0);
				}
				default -> System.out.println("Opção inválida");
			}
		}

	}

	private void getBookByTitle(Menu menu, BookService bookService) {
		String title = menu.askTitle();
		Book book = bookService.getBookByTitle(title);
		if (book != null) {
			menu.showBookInfo(book);
		}
	}

	private void getBooks(Menu menu, BookService bookService) {
		List<Book> books = bookService.getBooks();
		books.forEach(menu::showBookInfo);
	}

	private void getAuthors(BookService bookService) {
		List<Author> authors = bookService.getAuthors();
		authors.forEach(System.out::println);
	}

	private void getLivingAuthorsYear(Menu menu, BookService bookService) {
		Integer year = menu.askYear();
		List<Author> authors = bookService.getLivingAuthorsYear(year);
		if (!authors.isEmpty()) {
			authors.forEach(System.out::println);
		} else {
			System.out.println("Não foi encontrado nenhum registro de autor vivo neste ano específico.");
		}
	}


	private void getBooksByLanguage(Menu menu, BookService bookService) {
		String language = menu.askLanguage();
		List<Book> books = bookService.getBooksByLanguage(language);
		if (!books.isEmpty()) {
			books.forEach(menu::showBookInfo);
		} else {
			System.out.println("Não foi encontrado nenhum registro de livro correspondente a este idioma.");
		}
	}

}
