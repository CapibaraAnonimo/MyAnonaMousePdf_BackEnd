package com.capibaraanonimo.myanonamousepdf.controller;

import com.capibaraanonimo.myanonamousepdf.dto.BookResponse;
import com.capibaraanonimo.myanonamousepdf.dto.CreateBook;
import com.capibaraanonimo.myanonamousepdf.search.util.SearchCriteria;
import com.capibaraanonimo.myanonamousepdf.search.util.SearchCriteriaExtractor;
import com.capibaraanonimo.myanonamousepdf.service.BookService;
import com.capibaraanonimo.myanonamousepdf.service.StorageService;
import com.capibaraanonimo.myanonamousepdf.utils.MediaTypeUrlResource;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController()
@RequestMapping("/book")
@RequiredArgsConstructor()
public class BookController {
    private final BookService bookService;
    private final StorageService storageService;

    @GetMapping() //TODO añadir orden y lo mismo debería poner errores para por si ponen un igual
    public Page<BookResponse> getAllBooks(@RequestParam(value = "search", defaultValue = "") String search,
                                          @PageableDefault(size = 20, page = 0) Pageable pageable) {
        List<SearchCriteria> params = SearchCriteriaExtractor.extractSearchCriteriaList(search);

        return new PageImpl<>(bookService.search(params, pageable).stream().map(BookResponse::of).toList());
    }

    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        bookService.addDownload(filename);
        MediaTypeUrlResource resource = (MediaTypeUrlResource) storageService.loadAsResource(filename);

        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", resource.getType())
                .body(resource);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse postBook(@RequestPart("file") MultipartFile file, @RequestPart("book") @Valid CreateBook book) {
        return BookResponse.of(bookService.save(book, file));
    }
}