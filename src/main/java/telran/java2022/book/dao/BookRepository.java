package telran.java2022.book.dao;

import java.util.Optional;
import java.util.stream.Stream;

import telran.java2022.book.model.Book;

public interface BookRepository {

	Stream<Book> findBooksByAuthorsName(String authorName);

	Stream<Book> findByPublisherPublisherName(String publisherName);

	boolean existsById(String isbn);

	Optional<Book> findById(String isbn);

	Book save(Book book);

	void deleteById(String isbn);
	
}
