package com.rygf.entity;


import com.rygf.common.GetLink;
import com.rygf.security.permission.PRIVILEGE;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
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
@ToString(of = {"id", "name"})
@EqualsAndHashCode(of = "id")
//...
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    @OneToMany(mappedBy = "role")
    private Collection<User> users = new HashSet<>();
    
//    @ManyToMany(fetch = FetchType.EAGER)
//    private Collection<Privilege> privileges = new HashSet<>();
    
    @ElementCollection(targetClass = PRIVILEGE.class, fetch = FetchType.LAZY)
    @JoinTable(name = "role_privileges")
    @Column(name = "privilege", nullable = false)
    @Enumerated(EnumType.STRING)
    private Collection<PRIVILEGE> privileges = new HashSet<>();
    
    @CreationTimestamp
    private LocalDate createdDate;
    
    @UpdateTimestamp
    private LocalDate updatedDate;
    
    @PreRemove
    protected void preRemove() {
        users.stream().forEach(u -> u.setRole(null));
//        privileges.stream().forEach(pri -> pri.getRoles().remove(this));
    }
    
    public String selfLinkUpdate() {
        return GetLink.getDashboardRoleUpdateUri(id);
    }
    
    public String selfLinkDelete() {
        return GetLink.getDashboardRoleDeleteUri(id);
    }
}
