package com.rygf.entity;

import com.rygf.common.Formatter;
import com.rygf.common.GetLink;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    @Embedded
    private Thumbnail thumbnail = new Thumbnail();
    
    @Column(nullable = false, unique = true)
    private String title;
    
    @Column(nullable = false, columnDefinition = "text")
    private String description;
    
    @Column(nullable = false, columnDefinition = "text")
    private String content;
    
    @CreationTimestamp
    private LocalDate createdDate;
    
    @UpdateTimestamp
    private LocalDate updatedDate;
    
    @ManyToOne
    private Subject subject;
    
    @OneToOne
    private User author;
    
    public String getFormatTitle(int maxWord) {
        return Formatter.formatString(this.title, maxWord);
    }
    
    public String getFormatDescription(int maxWord) {
        return Formatter.formatString(this.description, maxWord);
    }
    
    public String selfLinkDetail() {
        return GetLink.getPostDetailUri(id, Formatter.convertStrToSlug(title));
    }
    
    public String selfLinkThumbUri() {
        if(thumbnail.getUri() == null)
            return null;
        
        if(thumbnail.isEmbedded())
            return GetLink.getEmbedThumbUri(thumbnail.getUri());
        
        return GetLink.getPostThumbUri(thumbnail.getUri());
    }
    
    public String getHashTag() {
        return "#" + Formatter.convertStrToSlug(this.title);
    }
    
    public String selfLinkUpdate() {
        return GetLink.getDashboardPostUpdateUri(id);
    }
    
    public String selfLinkDelete() {
        return GetLink.getDashboardPostDeleteUri(id);
    }
}
