package br.com.hyper.entities;

import br.com.hyper.constants.BaseUrls;
import br.com.hyper.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CUSTOMERS")
public class CustomerEntity extends BaseEntity implements Serializable, UserDetails {

    @Id
    @GeneratedValue(generator = "uuid")
    @JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "ID", updatable = false, nullable = false)
    private UUID id;

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

    @Column(name = "COVER_URL")
    private String coverUrl;

    @Column(name = "AVATAR_URL")
    private String avatarUrl = BaseUrls.AVATAR_URL;

    @Column(name = "BIOGRAPHY")
    private String biography;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "ROLE", nullable = false)
    private UserRole role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SUBSCRIPTION_ID", nullable = false)
    private SubscriptionEntity subscription;

    @Column(name = "IS_ARTIST", nullable = false)
    private Boolean isArtist;

    @Column(name = "IS_LABEL", nullable = false)
    private Boolean isLabel;

    @OneToOne(mappedBy = "customer")
    private ArtistEntity artistProfile;

    @OneToMany(mappedBy = "customer")
    private List<PlaylistEntity> playlists;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartEntity> carts = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return switch (this.role) {
            case ADMIN -> List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_CUSTOMER"),
                    new SimpleGrantedAuthority("ROLE_ARTIST")
            );
            case ARTIST -> List.of(
                    new SimpleGrantedAuthority("ROLE_ARTIST"),
                    new SimpleGrantedAuthority("ROLE_CUSTOMER")
            );
            default -> List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
        };
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