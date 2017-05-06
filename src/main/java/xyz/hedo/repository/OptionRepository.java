package xyz.hedo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import xyz.hedo.entity.Option;

/**
 * @author panic
 */
@Repository
public interface OptionRepository extends CrudRepository<Option, Integer> {
}
