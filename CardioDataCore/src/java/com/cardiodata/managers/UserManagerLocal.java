package com.cardiodata.managers;

import com.cardiodata.core.jpa.ApiToken;
import com.cardiodata.core.jpa.User;
import com.cardiodata.core.jpa.UserAccount;
import com.cardiodata.core.jpa.UserGroup;
import com.cardiodata.enums.AccountTypeEnum;
import com.cardiodata.exceptions.CardioDataException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author rogvold
 */
@Local
public interface UserManagerLocal {

    public User loginUser(AccountTypeEnum aType, String login, String password) throws CardioDataException;

    public User loginUserByEmail(String email, String password) throws CardioDataException;
    
    public User getUserByEmail(String email) throws CardioDataException;
    
    public UserAccount createUserAccount(AccountTypeEnum type, Long userId, String login, String password) throws CardioDataException;
    
    public UserAccount changePassword(Long userId, AccountTypeEnum type, String newPassword) throws CardioDataException;
    
    public UserAccount getUserEmailAccountByUserId(Long userId) throws CardioDataException;

    public User registerUser(AccountTypeEnum aType, String login, String password) throws CardioDataException;
    
    public User registerTrainer(AccountTypeEnum aType, String login, String password) throws CardioDataException;

    public User getUserByToken(String token) throws CardioDataException;

    public User getUserById(Long userId) throws CardioDataException;

    public User updateUserProfile(Long userId, String firstName, String lastName) throws CardioDataException;
    
    public User updateUserProfile(String token, String serializedUser) throws CardioDataException;
    
    public List<User> getAllTrainers();
    
    public List<User> getUserTrainers(Long userId) throws CardioDataException;
    
    public List<User> getUsersInDefaultGroup(Long trainerId) throws CardioDataException;
    
    public UserGroup getTrainerDefaultGroup(Long trainerId) throws CardioDataException;
    
    
    
}