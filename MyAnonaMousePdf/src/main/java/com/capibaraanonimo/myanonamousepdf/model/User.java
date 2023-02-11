package com.capibaraanonimo.myanonamousepdf.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "user")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class User {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = {
                    @Parameter(
                            name = "uuid_gen_strategy_class",
                            value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
                    )
            }
    )
    @Column(columnDefinition = "uuid", name = "user_id")
    private UUID id;

    @NaturalId
    @Column(unique = true, updatable = false)
    private String username;

    private String email;

    @Builder.Default()
    private LocalDateTime creationDate = LocalDateTime.now();

    @OneToMany(fetch = FetchType.LAZY)
    private List<Book> uploadedBooks;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Book> savedBooks;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Roles> roles;
}
