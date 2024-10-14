package dev.hoainamtd.service.impl;

import dev.hoainamtd.dto.reuqest.UserRequestDTO;
import dev.hoainamtd.exception.ResourceNotFoundException;
import dev.hoainamtd.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public int addUser(UserRequestDTO requestDTO) {
        System.out.println("Save user to db");

        if (requestDTO.getFirstName().equals("Tay")) {
            throw new ResourceNotFoundException("Tay khong ton tai");
        }
        return 0;
    }
}
