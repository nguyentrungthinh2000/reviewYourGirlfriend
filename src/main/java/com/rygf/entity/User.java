package com.rygf.entity;

import com.rygf.common.Formatter;
import com.rygf.common.GetLink;
import java.time.LocalDate;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
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
@ToString(of = {"id", "email"})
@EqualsAndHashCode(of = "id")
//...
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @NotNull
    private boolean enabled;
    
    @ManyToOne
    private Role role;
    
    /* User Information */
    
    private String displayName;
    
    public String getDisplayName() {
        return displayName != null ? displayName : email;
    }
    
    @Column(nullable = false)
    @Embedded
    private Thumbnail thumbnail = new Thumbnail();
    
    private LocalDate birthdate;
    
    @Column(columnDefinition = "text")
    private String bio;
    
    @CreationTimestamp
    private LocalDate createdDate;
    
    @UpdateTimestamp
    private LocalDate updatedDate;
    
    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private VerificationToken verificationToken;
    
    /*
    *  Methods
    * */
    
    public String selfLinkPosts() {
        return GetLink.getUserPostsUri(id, Formatter.convertStrToUrl(getDisplayName()));
    }
    
    public String selfLinkThumbUri() {
        if(thumbnail.getUri() == null)
            return null;
    
        if(thumbnail.isEmbedded())
            return GetLink.getEmbedThumbUri(thumbnail.getUri());
    
        return GetLink.getUserProfileThumbUri(thumbnail.getUri());
    }
    
    public String selfLinkUpdate() {
        return GetLink.getDashboardUserUpdateUri(id);
    }
    
    public String selfLinkDelete() {
        return GetLink.getDashboardUserDeleteUri(id);
    }
}
