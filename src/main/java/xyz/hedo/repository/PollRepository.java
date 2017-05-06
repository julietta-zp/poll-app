package xyz.hedo.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import xyz.hedo.entity.Poll;

/**
 * @author panic
 */
@Repository
public interface PollRepository extends CrudRepository<Poll, Integer> {

    @Query(value="SELECT p.id, p.title, p.content, p.author_email, p.hash, p.created_at, p.closed " +
            "FROM pollApp.polls AS p " +
            "WHERE p.hash = ?1",
            nativeQuery	= true)
    Poll findByHash(String pollHash);
}
