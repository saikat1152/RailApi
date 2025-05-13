package com.jbl.t24.rest.api.model.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.jbl.t24.rest.api.audit.Auditable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "privilege")
@NamedQuery(name = "Privilege.findAll", query = "SELECT p FROM Privilege p")
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
@Audited
public class Privilege extends Auditable implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "priv_id", unique = true, nullable = false)
    private int privId;

    @Column(length = 255)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY 
    // cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
    //         CascadeType.REFRESH }
            )
    @JoinTable(name = "roles_privileges", joinColumns = {
            @JoinColumn(name = "privilege_id", nullable = false) }, inverseJoinColumns = {
                    @JoinColumn(name = "role_id", nullable = false) })
    @NotAudited
    private List<Role> roles;

    public void addRole(Role role) {
        if (this.roles == null)
            this.roles = new ArrayList<>();
        this.roles.add(role);
    }

    public void addAllRole(List<Role> roles) {
        if (this.roles == null)
            this.roles = new ArrayList<>();
        this.roles.addAll(roles);
    }
}