package com.jbl.t24.rest.api.service.base;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jbl.t24.rest.api.model.base.Company;
import com.jbl.t24.rest.api.model.base.UserAccount;
import com.jbl.t24.rest.api.repository.base.UserAccountRepository;


@Service
@Transactional
public class UserAccountService extends BaseService<UserAccount> {

    @Autowired
    private UserAccountRepository userAccountRepository;

    public UserAccountService(UserAccountRepository useraccountRepository) {
        super.setRepository(useraccountRepository);
    }

    public UserAccount findOneByEmail(String userEmail) {
        return userAccountRepository.findByUserEmail(userEmail);
    }

    public UserAccount findOneByBankId(String bankId) {
        return userAccountRepository.findByUserBankId(bankId);
    }

    public List<UserAccount> findAllUserAccount(){
        return userAccountRepository.findAll();
    }

    public List<UserAccount> findByCompany(Company company) {
        return userAccountRepository.findByCompany(company);
    }

    public List<UserAccount> findByCompanyNot(Company company) {
        return userAccountRepository.findByCompanyNot(company);
    }

    public static final int MAX_FAILED_ATTEMPTS = 5;
    private static final long LOCK_TIME_DURATION = 15 * 60 * 1000; // 24 hours
    @Autowired
    private UserAccountRepository repo;
    public void increaseFailedAttempts(UserAccount user) {
        int newFailAttempts = user.getFailedAttempt() + 1;
        repo.updateFailedAttempts(newFailAttempts, user.getUserEmail());
    }
    public void lock(UserAccount user) {
        boolean newAccountNonLocked = false;
        Timestamp newLockTime = new Timestamp(System.currentTimeMillis());
        repo.updateAccountLocked(newAccountNonLocked, user.getUserEmail());
        repo.updateLockeDate(newLockTime, user.getUserEmail());
    }
    public void resetFailedAttempts(String userEmail) {
        repo.updateFailedAttempts(0, userEmail);
    }
    public boolean unlockWhenTimeExpired(UserAccount user) {
        long lockTimeInMillis = user.getLockTime().getTime();
        long currentTimeInMillis = System.currentTimeMillis();
        if (lockTimeInMillis + LOCK_TIME_DURATION < currentTimeInMillis) {
            boolean newAccountNonLocked = true;
            Timestamp newLockTime = null;
            int newFailAttempts = 0;
            repo.updateAccountLocked(newAccountNonLocked, user.getUserEmail());
            repo.updateLockeDate(newLockTime, user.getUserEmail());
            repo.updateFailedAttempts(newFailAttempts, user.getUserEmail());
            return true;
        }
        return false;
    }
    public List<UserAccount> findByCompanyIsNull() {
        return repo.findByCompanyIsNull();
    }
    public List<UserAccount> findByCompanyIsNotNull() {
        return repo.findByCompanyIsNotNull();
    }
}
