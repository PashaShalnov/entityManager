package telran.java2022.book.dao;

import java.util.List;
import java.util.Optional;

import telran.java2022.book.model.Publisher;

public interface PublisherRepository {

//	@Query("select distinct p.publisherName from Book b join b.authors a join b.publisher p where a.name=?1")
	List<String> findPublishersByAuthor(String authorName);
	
	Optional<Publisher> findById(String publisher);

	Publisher save(Publisher publisher);
}
