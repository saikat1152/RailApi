package com.jbl.t24.rest.api.repository.base;
import java.sql.Timestamp;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jbl.t24.rest.api.model.base.Company;
import com.jbl.t24.rest.api.model.base.UserAccount;



@Repository
@Transactional
public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {
	UserAccount findByUserEmail(String userEmail);
	UserAccount findByUserBankId(String userBankId);

    // @Query("SELECT u FROM UserAccount u WHERE u.company is null")
    List<UserAccount> findByCompanyIsNull();

    List<UserAccount> findByCompanyIsNotNull();


	@Query("UPDATE UserAccount u SET u.failedAttempt = ?1 WHERE u.userEmail = ?2")
    @Modifying
    public void updateFailedAttempts(int failAttempts, String userEmail);

	@Query("UPDATE UserAccount u SET u.accountNonLocked = ?1 WHERE u.userEmail = ?2")
    @Modifying
    public void updateAccountLocked(Boolean accountNonLocked, String userEmail);

	@Query("UPDATE UserAccount u SET u.lockTime = ?1 WHERE u.userEmail = ?2")
    @Modifying
    public void updateLockeDate(Timestamp lockTime, String userEmail);

    public UserAccount findByUserEmailAndCompany(String userEmail, Company companyId);

    List<UserAccount> findByCompany(Company companyId);

    List<UserAccount> findByCompanyNot(Company companyId);

}