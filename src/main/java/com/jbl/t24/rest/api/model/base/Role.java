package com.jbl.t24.rest.api.model.base;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.jbl.t24.rest.api.annotation.NameUnique;
import com.jbl.t24.rest.api.audit.Auditable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@DynamicUpdate
@Table(name = "roles")
@NamedQuery(name = "Role.findAll", query = "SELECT r FROM Role r")
@Getter
@Setter
@NoArgsConstructor
@Audited
// REV : Revision ID – Foriegn key Reference to the REVINFO.REV
// REVTYPE : numeric values for the type of operation on entity – 0 (Add), 1
// (Update), and 2 (Delete)
public class Role extends Auditable implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", unique = true, nullable = false)
    private int roleId;

    @NotBlank(message = "Role Name is Required!")
    @NameUnique(groups = OnCreate.class)
    @Column(name = "role_name", length = 255, nullable = false, unique = true)
    private String roleName;

    @ManyToMany(fetch = FetchType.LAZY
    // cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
    //         CascadeType.REFRESH }
            )
    @JoinTable(name = "roles_privileges", joinColumns = {
            @JoinColumn(name = "role_id", nullable = false) }, inverseJoinColumns = {
                    @JoinColumn(name = "privilege_id", nullable = false) })
    @NotAudited
    private List<Privilege> privileges;

    @ManyToMany(fetch = FetchType.LAZY
    // cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
    //         CascadeType.REFRESH }
            )
    @JoinTable(name = "users_roles", joinColumns = {
            @JoinColumn(name = "role_id", nullable = false) }, inverseJoinColumns = {
                    @JoinColumn(name = "user_id", nullable = false) })
    @NotAudited
    private Set<UserAccount> userAccounts = new HashSet<>();

    public void addUser(UserAccount user) {
        this.userAccounts.add(user);
    }

    public Role(Integer roleId) {
        this.roleId = roleId;
    }

    public Role(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString(){
        return this.roleName;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + roleId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (getClass() != obj.getClass()))
			return false;
		Role other = (Role) obj;
		if (roleId != other.roleId)
			return false;
		return true;
	}


}