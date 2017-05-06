package xyz.hedo.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import xyz.hedo.entity.Vote;

/**
 * @author panic
 */
@Repository
public interface VoteRepository extends CrudRepository<Vote, Integer> {

    @Query(value="SELECT v.id, v.option_id, v.created_at, v.ip " +
            "FROM pollApp.votes AS v, pollApp.options AS o " +
            "WHERE o.poll_id = (SELECT p.id from pollApp.polls AS p WHERE p.hash = ?1) " +
            "and v.option_id = o.id",
            nativeQuery	= true)
    Iterable<Vote> findByPoll(String pollHash);

    @Query(value = "SELECT v.* FROM pollApp.votes AS v, pollApp.options AS o, pollApp.polls AS p " +
            "WHERE v.ip = ?1 AND v.option_id = o.id AND o.poll_id = p.id AND p.hash = ?2",
            nativeQuery = true)
    Iterable<Vote> findByIpAndPoll(String ip, String pollHash);
}
