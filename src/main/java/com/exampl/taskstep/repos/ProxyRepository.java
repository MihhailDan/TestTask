package com.exampl.taskstep.repos;

import com.exampl.taskstep.models.Proxy;
import com.exampl.taskstep.models.ProxyType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @date: Feb.22.2022
 * @author: Mihhail Daniljuk
 * @email: daniljukmihhail@gmail.com
 */

@Repository
public interface ProxyRepository extends PagingAndSortingRepository<Proxy, Long> {

    @Query("select p from Proxy p where p.name = ?1 and p.type = ?2")
    public Optional<Proxy> findByNameAndType(String name, ProxyType type);

}
