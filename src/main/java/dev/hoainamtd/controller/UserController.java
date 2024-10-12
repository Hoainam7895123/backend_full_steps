package dev.hoainamtd.controller;

import dev.hoainamtd.dto.response.UserRequestDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @PostMapping("/")
    public String addUser(@RequestBody UserRequestDTO user) {
        return "User added";
    }

    @PutMapping("/{userId}")
    public String updateUser(@PathVariable int userId, @RequestBody UserRequestDTO user) {
        System.out.println("Update user");
        return "User updated";
    }

    @PatchMapping("/{userId}")
    public String updateStatus(@PathVariable boolean enable) {
        return "User Status changed";
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable int userId){
        return "User deleted";
    }

    @GetMapping("/{userId}")
    public UserRequestDTO getUser(@PathVariable int userId) {
        return new UserRequestDTO("Tay", "Java", "admin@tayjava.vn", "0123456789");
    }

    @GetMapping("/list")
    public List<UserRequestDTO> getAllUser(
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return List.of(new UserRequestDTO("Tay", "Java", "phone", "email"),
                new UserRequestDTO("Tay1", "Java", "phone", "email"));
    }
}
