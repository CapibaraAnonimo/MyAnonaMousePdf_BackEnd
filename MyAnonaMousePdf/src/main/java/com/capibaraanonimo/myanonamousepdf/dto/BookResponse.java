package com.capibaraanonimo.myanonamousepdf.dto;

import com.capibaraanonimo.myanonamousepdf.model.Book;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponse {
    private UUID id;

    private LocalDateTime uploadDate;

    private UserResponse uploader;

    private long amountDownloads;

    private String category;

    @Builder.Default()
    private boolean vip = false;

    private String book, title, author, description;

    public static BookResponse of(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .uploadDate(book.getUploadDate())
                .uploader(UserResponse.fromUser(book.getUploader()))
                .amountDownloads(book.getAmountDownloads())
                .category(book.getCategory().getName())
                .vip(book.isVip())
                .book(book.getBook())
                .title(book.getTitle())
                .author(book.getAuthor())
                .description(book.getDescription())
                .build();
    }
}
