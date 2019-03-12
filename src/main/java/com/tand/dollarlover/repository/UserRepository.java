package com.tand.dollarlover.repository;

import com.tand.dollarlover.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("UserRepository")
public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String email);
}
