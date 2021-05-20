package edu.austral.ingsis.jj.postsservice.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "post")
@Data
@NoArgsConstructor
public class Post extends AbstractEntity {

    private String content;

    private String userId;

    @CreationTimestamp
    private LocalDateTime creationDate;

}
