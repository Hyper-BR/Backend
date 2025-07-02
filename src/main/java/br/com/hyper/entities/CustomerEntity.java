package br.com.hyper.entities;

import br.com.hyper.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CUSTOMERS")
public class CustomerEntity extends BaseEntity implements Serializable, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUSTOMERS_SEQ")
    @SequenceGenerator(name = "CUSTOMERS_SEQ", sequenceName = "CUSTOMERS_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "AVATAR_URL")
    private String avatar;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "COUNTRY", nullable = false)
    private String country;

    @Column(name = "BIRTH_DATE", nullable = false)
    private String birthDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "SUBSCRIPTION_ID", nullable = false)
    private SubscriptionEntity subscription;

    @JoinColumn(name = "ROLE", nullable = false)
    private UserRole role;

    @OneToMany(mappedBy = "customer")
    private List<ArtistEntity> artists;

//    @OneToMany(mappedBy = "customer")
//    private List<PlaylistEntity> playlists;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == UserRole.ADMIN) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_CUSTOMER"), new SimpleGrantedAuthority("ROLE_ARTIST"));
        } else if(this.role == UserRole.ARTIST) {
            return List.of(new SimpleGrantedAuthority("ROLE_ARTIST"), new SimpleGrantedAuthority("ROLE_CUSTOMER"));
        }
        return List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
