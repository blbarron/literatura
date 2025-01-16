package com.desafioconsultalibros.literatura;

import com.desafioconsultalibros.literatura.entity.Author;
import com.desafioconsultalibros.literatura.entity.Book;
import com.desafioconsultalibros.literatura.model.BookResponse;
import com.desafioconsultalibros.literatura.repository.AuthorRepository;
import com.desafioconsultalibros.literatura.repository.BookRepository;
import com.desafioconsultalibros.literatura.service.GutendexService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class GutendexApplication implements CommandLineRunner {

    private final GutendexService gutendexService;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    private BookResponse selectedBook = null;

    public GutendexApplication(GutendexService gutendexService, AuthorRepository authorRepository, BookRepository bookRepository) {
        this.gutendexService = gutendexService;
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(GutendexApplication.class, args);
    }

    @Override
    public void run(String... args) {
        runApplication();
    }

    private void runApplication() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("************************");
            System.out.println("Menu:");
            System.out.println("1. Buscar por título");
            if (selectedBook != null) {
                System.out.println("2. Agregar título seleccionado");
            }
            System.out.println("3. Listar libros registrados");
            System.out.println("4. Listar autores registrados");
            System.out.println("5. Listar autores vivos en un determinado año");
            System.out.println("6. Listar libros por idioma");
            System.out.println("7. Salir");
            System.out.println("************************");

            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            switch (opcion) {
                case 1:
                    buscarPorTitulo(scanner);
                    break;
                case 2:
                    if (selectedBook != null) {
                        agregarTituloSeleccionado();
                    } else {
                        System.out.println("Opción no válida.");
                    }
                    break;
                case 3:
                    listarLibrosRegistrados();
                    break;
                case 4:
                    listarAutoresRegistrados();
                    break;
                case 5:
                    listarAutoresVivos(scanner);
                    break;
                case 6:
                    listarLibrosPorIdioma(scanner);
                    break;
                case 7:
                    System.out.println("Saliendo del programa...");
                    return;
                default:
                    System.out.println("Opción no válida. Inténtelo nuevamente.");
            }
        }
    }

    private void buscarPorTitulo(Scanner scanner) {
        System.out.print("Ingrese el título del libro que desea buscar: ");
        String titulo = scanner.nextLine();

        try {
            var response = gutendexService.searchBooksByTitle(titulo);

            if (response.getResults().isEmpty()) {
                System.out.println("No se encontraron libros con el título especificado.");
                return;
            }

            System.out.println("Libros encontrados:");
            for (int i = 0; i < response.getResults().size(); i++) {
                var bookResponse = response.getResults().get(i);
                System.out.println(i + ") Título: " + bookResponse.getTitle());
                System.out.println("   Autores: ");
                bookResponse.getAuthors().forEach(author -> System.out.println("   - " + author.getName()));
                System.out.println("   Idioma: " + bookResponse.getLanguages());
                System.out.println("   Número de descargas: " + bookResponse.getDownloadCount());
                System.out.println("______________________________________________");
            }

            System.out.println("Total de libros encontrados: " + response.getCount());

            System.out.print("¿Desea agregar un libro de la lista? (s/n): ");
            String opcion = scanner.nextLine().trim().toLowerCase();

            if ("s".equals(opcion)) {
                System.out.print("Ingrese el número de índice del libro que desea seleccionar: ");
                int index = scanner.nextInt();
                scanner.nextLine(); // Limpiar el buffer

                if (index >= 0 && index < response.getResults().size()) {
                    selectedBook = response.getResults().get(index);

                    System.out.println("Has seleccionado el libro:");
                    System.out.println("Título: " + selectedBook.getTitle());
                    System.out.println("Authors:");
                    selectedBook.getAuthors().forEach(author ->
                        System.out.println(" - " + author.getName())
                    );
                    System.out.println("Idioma: " + selectedBook.getLanguages());
                    System.out.println("Número de descargas: " + selectedBook.getDownloadCount());
                } else {
                    System.out.println("Índice inválido. No se seleccionó ningún libro.");
                }
            } else if ("n".equals(opcion)) {
                System.out.println("No se seleccionó ningún libro. Volviendo al menú principal.");
            } else {
                System.out.println("Opción no válida. Volviendo al menú principal.");
            }
        } catch (Exception e) {
            System.out.println("Ocurrió un error al buscar el libro: " + e.getMessage());
        }
    }

    private void agregarTituloSeleccionado() {
        if (selectedBook == null) {
            System.out.println("No hay un título seleccionado.");
            return;
        }

        // Create and save the book to the database
        Book bookToSave = new Book(
            selectedBook.getTitle(),
            selectedBook.getLanguages().isEmpty() ? "Unknown" : selectedBook.getLanguages().get(0),
            selectedBook.getDownloadCount()
        );

        bookRepository.save(bookToSave);

        System.out.println("El libro ha sido guardado en la base de datos:");
        System.out.println("Título: " + bookToSave.getTitle());
        System.out.println("Idioma: " + bookToSave.getLanguage());
        System.out.println("Número de descargas: " + bookToSave.getDownloadCount());
    }

    private void listarLibrosRegistrados() {
        List<Book> allBooks = bookRepository.findAll();
        if (allBooks.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            allBooks.forEach(book -> {
                System.out.println("Título: " + book.getTitle());
                System.out.println("Idioma: " + book.getLanguage());
                System.out.println("Número de descargas: " + book.getDownloadCount());
                System.out.println("______________________________________________");
            });
        }
    }

    private void listarAutoresRegistrados() {
        List<Author> allAuthors = authorRepository.findAll();
        if (allAuthors.isEmpty()) {
            System.out.println("No hay autores registrados.");
        } else {
            allAuthors.forEach(author -> {
                System.out.println("Nombre: " + author.getName());
                System.out.println("Año de nacimiento: " + author.getBirthYear());
                System.out.println("Año de muerte: " + (author.getDeathYear() == null ? "Vivo" : author.getDeathYear()));
                System.out.println("______________________________________________");
            });
        }
    }

    private void listarAutoresVivos(Scanner scanner) {
        System.out.print("Ingrese el año para buscar autores vivos: ");
        int year = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer

        List<Author> autoresVivos = authorRepository.findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(year, year);

        if (autoresVivos.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año " + year + ".");
        } else {
            autoresVivos.forEach(author -> System.out.println("Nombre: " + author.getName()));
        }
    }

    private void listarLibrosPorIdioma(Scanner scanner) {
        System.out.print("Ingrese el idioma para buscar libros (EN, ES, FR, PT): ");
        String language = scanner.nextLine().trim().toUpperCase();

        List<Book> librosPorIdioma = bookRepository.findAll().stream()
            .filter(book -> book.getLanguage().equalsIgnoreCase(language))
            .toList();

        if (librosPorIdioma.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma " + language + ".");
        } else {
            librosPorIdioma.forEach(book -> System.out.println("Título: " + book.getTitle()));
        }
    }
}