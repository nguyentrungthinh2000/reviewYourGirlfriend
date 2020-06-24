package com.rygf.entity;

import com.rygf.common.Formatter;
import com.rygf.common.GetLink;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@NoArgsConstructor
@AllArgsConstructor
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
    
    private String thumbnail;
    
    private LocalDate birthdate;
    
    @Column(columnDefinition = "text")
    private String bio;
    
    @CreationTimestamp
    private LocalDate createdDate;
    
    @UpdateTimestamp
    private LocalDate updatedDate;
    
    public String selfLinkPosts() {
        return GetLink.getUserPostsUri(id, Formatter.convertStrToSlug(getDisplayName()));
    }
    
    public String selfLinkThumbUri() {
        return GetLink.getUserProfileThumbUri(thumbnail);
    }
    
    public String selfLinkUpdate() {
        return GetLink.getDashboardUserUpdateUri(id);
    }
    
    public String selfLinkDelete() {
        return GetLink.getDashboardUserDeleteUri(id);
    }
}
