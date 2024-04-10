package com.scaler.bookmyshow.Services;


import com.scaler.bookmyshow.Models.User;
import com.scaler.bookmyshow.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository)
    {
        this.userRepository=userRepository;
    }
    public User SignUp(String email,String password)
    {
        //check if user is already there or not

        User user=new User();
        user.setEmail(email);
        user.setPassword(password);


        User savedUser=userRepository.save(user);
        return savedUser;

    }
}
