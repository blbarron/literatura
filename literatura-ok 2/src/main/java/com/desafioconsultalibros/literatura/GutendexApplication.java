package com.desafioconsultalibros.literatura;

import com.desafioconsultalibros.literatura.entity.Author;
import com.desafioconsultalibros.literatura.entity.Book;
import com.desafioconsultalibros.literatura.model.BookResponse;
import com.desafioconsultalibros.literatura.model.GutendexResponse;
import com.desafioconsultalibros.literatura.repository.AuthorRepository;
import com.desafioconsultalibros.literatura.repository.BookRepository;
import com.desafioconsultalibros.literatura.service.GutendexService;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class GutendexApplication {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GutendexService gutendexService;

    private BookResponse selectedBook = null;

    public GutendexApplication(BookRepository bookRepository, AuthorRepository authorRepository, GutendexService gutendexService) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.gutendexService = gutendexService;
    }

    public static void main(String[] args) {
        // Inicializar el contexto de Spring manualmente
        @SuppressWarnings("resource")
        ApplicationContext context = new AnnotationConfigApplicationContext(LiteraturaApplication.class);

        // Obtener el bean de la aplicación
        GutendexApplication app = context.getBean(GutendexApplication.class);

        // Ejecutar la lógica principal
        app.run();
    }

    public void run() {
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
                default:
                    System.out.println("Opción no válida. Inténtelo nuevamente.");
            }
        }
    }

    private void buscarPorTitulo(Scanner scanner) {
        System.out.print("Ingrese el título del libro que desea buscar: ");
        String titulo = scanner.nextLine();

        try {
            GutendexResponse response = gutendexService.searchBooksByTitle(titulo);

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
        List<Book> books = bookRepository.findAll();
        if (books.isEmpty()) {
            System.out.println("No hay libros registrados en la base de datos.");
        } else {
            books.forEach(book -> {
                System.out.println("Título: " + book.getTitle());
                System.out.println("Idioma: " + book.getLanguage());
                System.out.println("Número de descargas: " + book.getDownloadCount());
                System.out.println("______________________________________________");
            });
        }
    }

    private void listarAutoresRegistrados() {
        List<Author> authors = authorRepository.findAll();
        if (authors.isEmpty()) {
            System.out.println("No hay autores registrados en la base de datos.");
        } else {
            authors.forEach(author -> {
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
    
        List<Author> autoresVivos = authorRepository.findAll().stream()
            .filter(author -> author.getBirthYear() <= year && (author.getDeathYear() == null || author.getDeathYear() > year))
            .toList();
    
        if (autoresVivos.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año " + year + ".");
        } else {
            System.out.println("Autores vivos en el año " + year + ":");
            autoresVivos.forEach(author -> System.out.println("- " + author.getName()));
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
            System.out.println("Libros en el idioma " + language + ":");
            librosPorIdioma.forEach(book -> System.out.println("- " + book.getTitle()));
        }
    }

}
