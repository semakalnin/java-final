package com.example.library.repository;

import com.example.library.model.Borrower;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BorrowerRepository extends CrudRepository<Borrower, UUID> {
}
