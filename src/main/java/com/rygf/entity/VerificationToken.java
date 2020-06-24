package com.rygf.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@NoArgsConstructor
@Getter
@Setter
//...
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String token;
    
    @OneToOne
    private User user;
    
    @Column(nullable = false)
    private LocalDate expirationDate;
    
    @CreationTimestamp
    private LocalDate createdDate;
    
    public VerificationToken(String token, User user, LocalDate expiredDate) {
        this.token = token;
        this.user = user;
        this.expirationDate = expiredDate;
    }
}
