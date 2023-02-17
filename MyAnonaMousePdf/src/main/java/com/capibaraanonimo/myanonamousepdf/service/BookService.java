package com.capibaraanonimo.myanonamousepdf.service;

import com.capibaraanonimo.myanonamousepdf.dto.CreateBook;
import com.capibaraanonimo.myanonamousepdf.errors.exceptions.BookNameNotFoundException;
import com.capibaraanonimo.myanonamousepdf.errors.exceptions.ListEntityNotFoundException;
import com.capibaraanonimo.myanonamousepdf.model.Book;
import com.capibaraanonimo.myanonamousepdf.repository.BookRepository;
import com.capibaraanonimo.myanonamousepdf.search.spec.BookSpecificationBuilder;
import com.capibaraanonimo.myanonamousepdf.search.util.SearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

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

    public void save(Book book) {
        bookRepository.save(book);
    }

    public Page<Book> search(List<SearchCriteria> params, Pageable pageable) {
        BookSpecificationBuilder personSpecificationBuilder =
                new BookSpecificationBuilder(params);
        Specification<Book> spec = personSpecificationBuilder.build();
        Page<Book> books = bookRepository.findAll(spec, pageable);

        if (books.isEmpty())
            throw new ListEntityNotFoundException(Book.class);
        return books;
    }

    public void addDownload(String name) {
        Optional<Book> optionalBook = bookRepository.findBookByBook(name);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            book.incrementDownloads();
            this.save(book);
        } else
            throw new BookNameNotFoundException(name);
    }
}
