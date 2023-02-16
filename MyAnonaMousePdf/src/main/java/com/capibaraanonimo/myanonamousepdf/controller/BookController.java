package com.capibaraanonimo.myanonamousepdf.controller;

import com.capibaraanonimo.myanonamousepdf.dto.CreateBook;
import com.capibaraanonimo.myanonamousepdf.model.Book;
import com.capibaraanonimo.myanonamousepdf.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController()
@RequestMapping("/book")
@RequiredArgsConstructor()
public class BookController {
    private final BookService bookService;

    @GetMapping() //TODO añadir búsqueda
    public List<Book> getAllBooks() {
        return bookService.findAll();
    }

    @PostMapping() //TODO arreglar unsupported media type
    public Book postBook(@RequestPart("file") MultipartFile file, @RequestPart("book") @Valid CreateBook book) {
        return bookService.save(book, file);
    }
}