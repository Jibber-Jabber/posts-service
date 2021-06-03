package edu.austral.ingsis.jj.postsservice.model;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "comment")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment extends AbstractEntity{

    @CreationTimestamp
    private LocalDateTime creationDate;

    private String content;

    private String userId;

    @ManyToOne
    private Post post;
}
