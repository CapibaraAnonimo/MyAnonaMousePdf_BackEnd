package com.capibaraanonimo.myanonamousepdf.service;

import com.capibaraanonimo.myanonamousepdf.dto.CreateBook;
import com.capibaraanonimo.myanonamousepdf.errors.exceptions.ListEntityNotFoundException;
import com.capibaraanonimo.myanonamousepdf.model.Book;
import com.capibaraanonimo.myanonamousepdf.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final StorageService storageService;

    public List<Book> findAll() {
        List<Book> books = bookRepository.findAll();
        if (books.isEmpty()) {
            throw new ListEntityNotFoundException(Book.class);
        }
        return books;
    }

    public Book save(CreateBook book, MultipartFile file) {
        String filename = storageService.store(file);

        return bookRepository.save(
                Book.builder()
                        .uploader(userService.findById(book.getUploader()))
                        .category(categoryService.findById(book.getCategory()))
                        .book(filename)
                        .title(book.getTitle())
                        .author(book.getAuthor())
                        .description(book.getDescription())
                        .build());
    }
}
