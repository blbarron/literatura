package com.desafioconsultalibros.literatura.repository;

import com.desafioconsultalibros.literatura.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    // Encuentra autores vivos en un año específico
    List<Author> findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(int year, int year2);
}
