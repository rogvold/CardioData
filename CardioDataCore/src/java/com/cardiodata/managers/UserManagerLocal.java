package com.cardiodata.managers;

import com.cardiodata.core.jpa.ApiToken;
import com.cardiodata.core.jpa.User;
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

    public User registerUser(AccountTypeEnum aType, String login, String password) throws CardioDataException;
    
    public User registerTrainer(AccountTypeEnum aType, String login, String password) throws CardioDataException;

    public User getUserByToken(String token) throws CardioDataException;

    public User getUserById(Long userId) throws CardioDataException;

    public User updateUserProfile(Long userId, String firstName, String lastName) throws CardioDataException;
    
    public List<User> getAllTrainers();
    
    public List<User> getUserTrainers(Long userId) throws CardioDataException;
    
    public List<User> getTrainees(Long trainerId) throws CardioDataException;
    
    public UserGroup getTrainerDefaultGroup(Long trainerId) throws CardioDataException;
    
    
    
}