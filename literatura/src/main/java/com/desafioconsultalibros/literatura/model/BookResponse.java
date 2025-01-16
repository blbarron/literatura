package com.desafioconsultalibros.literatura.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BookResponse {
    private int id;
    private String title;
    private List<Author> authors;
    private List<String> languages;
    private int downloadCount;

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    @JsonProperty("download_count")
    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    // Clase interna para autores
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Author {
        private String name;
        private Integer birthYear;
        private Integer deathYear;

        // Getters y Setters

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @JsonProperty("birth_year")
        public Integer getBirthYear() {
            return birthYear;
        }

        public void setBirthYear(Integer birthYear) {
            this.birthYear = birthYear;
        }

        @JsonProperty("death_year")
        public Integer getDeathYear() {
            return deathYear;
        }

        public void setDeathYear(Integer deathYear) {
            this.deathYear = deathYear;
        }
    }
}

