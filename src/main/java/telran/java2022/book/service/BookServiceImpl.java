package telran.java2022.book.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java2022.book.dao.AuthorRepository;
import telran.java2022.book.dao.BookRepository;
import telran.java2022.book.dao.PublisherRepository;
import telran.java2022.book.dto.AuthorDto;
import telran.java2022.book.dto.BookDto;
import telran.java2022.book.dto.exceptions.EntityNotFoundException;
import telran.java2022.book.model.Author;
import telran.java2022.book.model.Book;
import telran.java2022.book.model.Publisher;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

	final BookRepository bookRepository;
	final AuthorRepository authorRepository;
	final PublisherRepository publisherRepository;
	final ModelMapper modelMapper;
	final Author undefinedAuthor = new Author("undefined", null);


	@Override
	@Transactional
	public boolean addBook(BookDto bookDto) {
		if (bookRepository.existsById(bookDto.getIsbn())) {
			return false;
		}
		// Publisher
		Publisher publisher = publisherRepository.findById(bookDto.getPublisher())
				.orElseGet(() -> publisherRepository.save(new Publisher(bookDto.getPublisher())));
		//Author
		Set<Author> authors = bookDto.getAuthors().stream()
						.map(a -> authorRepository.findById(a.getName())
						.orElseGet(() -> authorRepository.save(new Author(a.getName(), a.getBirthDate()))))
						.collect(Collectors.toSet());
		Book book = new Book(bookDto.getIsbn(), bookDto.getTitle(), authors, publisher);
		bookRepository.save(book);
		return true;
	}

	@Override
	public BookDto findBookByIsbn(String isbn) {
		Book book = bookRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);
		return modelMapper.map(book, BookDto.class);
		}

	@Override
	@Transactional
	public BookDto removeBook(String isbn) {
		Book book = bookRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);
		bookRepository.deleteById(isbn);
		return modelMapper.map(book, BookDto.class);
	}

	@Override
	@Transactional
	public BookDto updateBook(String isbn, String title) {
		Book book = bookRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);
		book.setTitle(title);
		return modelMapper.map(book, BookDto.class);
	}

	@Override
	public Iterable<BookDto> findBooksByAuthor(String authorName) {
		return bookRepository.findBooksByAuthorsName(authorName)	
				.map(b -> modelMapper.map(b, BookDto.class))
				.collect(Collectors.toList());
	}

	@Override
//	@Transactional(readOnly = true)
	public Iterable<BookDto> findBooksByPublisher(String publisherName) {
		return bookRepository.findByPublisherPublisherName(publisherName)
						.map(b -> modelMapper.map(b, BookDto.class))
						.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	//TODO check edd's solution
	public Iterable<AuthorDto> findBookAuthors(String isbn) {		
		Book book = bookRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);
		return book.getAuthors().stream()
				.map(a -> modelMapper.map(a, AuthorDto.class))
				.collect(Collectors.toList());
//		Book book = bookRepository.findById(isbn).orElseThrow(EntityNotFoundException::new);
//		List<Author> authors = new ArrayList<>();
//		authors.addAll(book.getAuthors());
//		return	authors.stream()
//						.map(m -> modelMapper.map(m, AuthorDto.class))
//						.collect(Collectors.toList());
	}

	@Override
//	@Transactional(readOnly = true)
	public Iterable<String> findPublishersByAuthor(String authorName) {
		return publisherRepository.findPublishersByAuthor(authorName);
		
	}

	@Override
	@Transactional
	public AuthorDto removeAuthor(String authorName) {
		Author author = authorRepository.findById(authorName).orElseThrow(EntityNotFoundException::new);
		authorRepository.delete(author);
		return modelMapper.map(author, AuthorDto.class);
	}
}
