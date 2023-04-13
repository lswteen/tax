package com.jobis.tax.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jobis.tax.domain.user.repository.StringCryptoConverter;
import com.jobis.tax.domain.user.type.UserGender;
import com.jobis.tax.domain.user.type.UserRole;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column(name="reg_no")
    @Convert(converter = StringCryptoConverter.class)
    private String regNo;

    @Column
    private String nickname;

    @Column
    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(unique = true, name="user_id")
    private String userId;

    @Column
    @Enumerated(EnumType.STRING)
    private UserGender gender;

    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_mapping_id")
    )
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<RefreshToken> refreshTokens = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder(builderMethodName = "signUpBuilder")
    public User(String name, String nickname, String password, String phoneNumber, String userId, String regNo, String gender) {
        this.name = name;
        this.nickname = nickname;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.userId = userId;
        this.regNo = regNo;
        this.gender = UserGender.convertFrom(gender);
        this.roles = Stream.of(UserRole.USER)
                .collect(Collectors.toSet());
    }

    public String getGenderAsString() {
        String gender = null;

        if (Objects.nonNull(this.gender)) {
            gender = this.gender.name();
        }

        return gender;
    }

    public void setRefreshTokens(List<RefreshToken> refreshTokens) {
        this.refreshTokens = refreshTokens;
    }
}
