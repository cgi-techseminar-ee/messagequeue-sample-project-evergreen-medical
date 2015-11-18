package com.cgi.seminar.repository;

import com.cgi.seminar.domain.EntityWithExternalId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface EntityWithExternalIdRepository<T extends EntityWithExternalId> extends JpaRepository<T, Long> {

    T findByExternalId(String externalId);
}
