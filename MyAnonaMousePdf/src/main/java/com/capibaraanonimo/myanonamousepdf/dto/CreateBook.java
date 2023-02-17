package com.capibaraanonimo.myanonamousepdf.dto;

import com.capibaraanonimo.myanonamousepdf.validation.single.annotations.CategoryExist;
import com.capibaraanonimo.myanonamousepdf.validation.single.annotations.UserExist;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBook {
    @UserExist(message = "{createBook.uploader.exist}")
    private UUID uploader;

    @CategoryExist(message = "{createBook.category.exist}")
    private UUID category;

    @NotEmpty(message = "{createBook.title.empty}")
    private  String title;

    @NotEmpty(message = "{createBook.author.empty}")
    private String author;

    @NotEmpty(message = "{createBook.description.empty}")
    private String description;
}
