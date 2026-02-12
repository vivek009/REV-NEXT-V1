package com.revnext.domain.user;

import com.revnext.constants.UserStatus;
import com.revnext.domain.BaseData;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseData {

    @Id
    @Column(name = "user_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    @Column(name = "user_name", updatable = true, nullable = false, unique = true)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "mobile_number", nullable = false)
    private String mobileNumber;

    @Column(name = "name")
    private String name;

    @Column(name = "department")
    private String department;

    @Column(name = "circle_name")
    private String circleName;

    @Column(name = "division_name")
    private String divisionName;

    @Column(name = "email", unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status = UserStatus.ACTIVE;

    @Column(name = "refresh_token", nullable = false, unique = true)
    private String refreshToken;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    @Column(name = "shop_name")
    private String shopName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public String getRolesAsString() {
        if (roles == null || roles.isEmpty()) {
            return "";
        }
        return roles.stream()
                .map(role -> role.getName().toString()) // Assumes Role has getName() and Roles has getValue()
                .collect(Collectors.joining(", "));
    }

    public String getRolesAsValue() {
        if (roles == null || roles.isEmpty()) {
            return "";
        }
        return roles.stream()
                .map(role -> role.getName().getValue()) // Assumes Role has getName() and Roles has getValue()
                .collect(Collectors.joining(", "));
    }

    public String paymentBy(String tubewellCircleName, String tubewellDivisionName) {
        StringBuilder result = new StringBuilder();

        // Add roles
        if (roles != null && !roles.isEmpty()) {
            String rolesStr = roles.stream()
                    .map(role -> role.getName().toString())
                    .collect(Collectors.joining(", "));
            result.append(rolesStr);
        }

        // Determine fallback circle and division
        String finalCircle = (tubewellCircleName != null && !tubewellCircleName.trim().isEmpty())
                ? tubewellCircleName
                : this.circleName;

        String finalDivision = (tubewellDivisionName != null && !tubewellDivisionName.trim().isEmpty())
                ? tubewellDivisionName
                : this.divisionName;

        // Add comma only if both circle and division are available
        if (finalCircle != null && !finalCircle.trim().isEmpty()
                && finalDivision != null && !finalDivision.trim().isEmpty()) {
            if (result.length() > 0) {
                result.append(", ");
            }
            result.append(finalCircle).append(", ").append(finalDivision);
        }

        return result.toString();
    }

}
