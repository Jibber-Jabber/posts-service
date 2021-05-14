package edu.austral.ingsis.jj.postsservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "post")
@Data
@NoArgsConstructor
public class Post extends AbstractEntity {

    private String content;

    private String username;

    @CreationTimestamp
    private LocalDateTime creationDate;

}
