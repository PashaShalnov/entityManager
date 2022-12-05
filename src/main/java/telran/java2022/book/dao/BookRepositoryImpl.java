package telran.java2022.book.dao;

import java.util.Optional;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import telran.java2022.book.model.Book;

@Repository
public class BookRepositoryImpl implements BookRepository {

	@PersistenceContext
	EntityManager em;
	
	@Override
	public Stream<Book> findBooksByAuthorsName(String authorName) {
		TypedQuery<Book> query = em.createQuery("select b from Book b join b.authors a where a.name=?1", Book.class);
		query.setParameter(1, authorName);
		return query.getResultList().stream();
	}

	@Override
	public Stream<Book> findByPublisherPublisherName(String publisherName) {
		TypedQuery<Book> query = em.createQuery("select b from Book b join b.publisher bp where bp.publisherName=?1", Book.class);
		query.setParameter(1, publisherName);
		return query.getResultList().stream();
		
	}

	@Override
	public boolean existsById(String isbn) {
		return em.find(Book.class, isbn) != null;
	}

	@Override
	public Optional<Book> findById(String isbn) {
		return Optional.ofNullable(em.find(Book.class, isbn));
	}

	@Override
//	@Transactional  закомменчено то, как в спринге работает save
	public Book save(Book book) {
		em.persist(book);
//		em.merge(book);
		return book;
	}

	@Override
	@Transactional
	public void deleteById(String isbn) {
		Book book = em.find(Book.class, isbn);
		em.remove(book);
	}

}
