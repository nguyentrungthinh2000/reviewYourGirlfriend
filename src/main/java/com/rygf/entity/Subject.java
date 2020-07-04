package com.rygf.entity;

import com.rygf.common.Formatter;
import com.rygf.common.GetLink;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@NoArgsConstructor
@Getter
@Setter
@ToString(of = {"id", "title"})
@EqualsAndHashCode(of = "id")
//...
@Entity
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private String about;
    
    @Column(nullable = false)
    @Embedded
    private Thumbnail thumbnail = new Thumbnail();
    
    @OneToMany(mappedBy = "subject")
    private Collection<Post> posts = new HashSet<>();
    
    @CreationTimestamp
    private LocalDate createdDate;
    
    @UpdateTimestamp
    private LocalDate updatedDate;
    
    public String getFormatTitle(int maxWord) {
        return Formatter.formatString(this.title, maxWord);
    }
    
    public String getFormatAbout(int maxWord) {
        return Formatter.formatString(this.about, maxWord);
    }
    
    @PreRemove
    public void preRemove() {
        posts.stream().forEach(post -> post.setSubject(null));
    }
    
    public String selfLinkDetail() {
        return GetLink.getSubjectDetailUri(id, Formatter.convertStrToUrl(title));
    }
    
    public String selfLinkThumbUri() {
        if(thumbnail.getUri() == null)
            return null;
        
        if(thumbnail.isEmbedded())
            return GetLink.getEmbedThumbUri(thumbnail.getUri());
        
        return GetLink.getSubjectThumbUri(thumbnail.getUri());
    }
    
    public String selfLinkUpdate() {
        return GetLink.getDashboardSubjectUpdateUri(id);
    }
    
    public String selfLinkDelete() {
        return GetLink.getDashboardSubjectDeleteUri(id);
    }
    
}
