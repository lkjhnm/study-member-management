package com.grasstudy.user.repository;

import com.grasstudy.user.entity.Authentication;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticationRepository extends ReactiveCrudRepository<Authentication, String> {

}
