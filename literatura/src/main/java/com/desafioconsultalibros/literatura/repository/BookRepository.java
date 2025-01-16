package com.desafioconsultalibros.literatura.repository;

import com.desafioconsultalibros.literatura.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
