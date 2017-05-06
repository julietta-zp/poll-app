package xyz.hedo.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Set;

/**
 * @author panic
 */
@Entity
@Table(name = "polls")
public class Poll extends ResourceSupport implements DomainObject{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    @NotEmpty
    @Size(min=3, max=90)
    private String title;

    @Column(name = "content")
    @NotEmpty
    private String content;

    @Column(name = "author_email")
    @NotEmpty
    @Email
    private String email;

    @Column(name = "hash")
    private String hash;

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "closed")
    private boolean closed;

    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="poll_id")
    @JsonManagedReference
    @OrderBy
    @NotEmpty
    @Size(min=2, max=6)
    private Set<Option> options;


    public Poll() {
    }

    public Integer getPollId() {
        return id;
    }

    public void setPollId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public Set<Option> getOptions() {
        return options;
    }

    public void setOptions(Set<Option> options) {
        this.options = options;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
