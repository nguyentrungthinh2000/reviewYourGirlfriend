package com.rygf.service;

import com.rygf.dao.UserRepository;
import com.rygf.dto.RegisterDTO;
import com.rygf.dto.UserDTO;
import com.rygf.entity.User;
import com.rygf.exception.DuplicateEntityException;
import com.rygf.exception.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService {
    
    
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    
    public void register(RegisterDTO registerDTO) {
        User temp = new User();
        temp.setEmail(registerDTO.getEmail());
        temp.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        temp.setEnabled(true); // cần fix
    
        try {
            userRepository.save(temp);
            SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                    temp.getEmail(),
                    temp.getPassword(),
                    new ArrayList<GrantedAuthority>()));
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            throw new DuplicateEntityException("Duplicated User\n" + e.getMessage());
        }
    }
    
    public void createOrUpdate(UserDTO userDTO) {
        User temp;

        if(userDTO.getId() != null) { // UPDATE USER
            Optional<User> opt = userRepository.findById(userDTO.getId());
            final var userId = userDTO.getId();
            opt.orElseThrow(() -> new EntityNotFoundException("User with id : " + userId + " is not exists !"));

            temp = opt.get();
            temp.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            temp.setEnabled(userDTO.isEnabled());
            temp.setRole(userDTO.getRole());

        } else { // CREATE NEW USER
            temp = new User();
            temp.setEmail(userDTO.getEmail());
            if(!userDTO.getPassword().isBlank())
                temp.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            temp.setEnabled(userDTO.isEnabled());
            temp.setRole(userDTO.getRole());
        }

        try {
            userRepository.save(temp);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            throw new DuplicateEntityException("Duplicated User\n" + e.getMessage());
        }
    }
    
    public User find(Long id) {
        Optional<User> opt = userRepository.findById(id);
        opt.orElseThrow(() -> new EntityNotFoundException("User with id : " + id + " is not exists !"));

        return opt.get();
    }
    
    public List<User> findAll() {
        ArrayList<User> users = new ArrayList<>();
        userRepository.findAll().forEach(user -> {
            users.add(user);
        });

        return users;
    }
    
    public void delete(Long id) {
        Optional<User> opt = userRepository.findById(id);
        opt.orElseThrow(() -> new EntityNotFoundException("User with id : " + id + " is not exists !"));

        User user = opt.get();
        userRepository.delete(user);
    }
    
    /*
     *  NOT CRUD
     * */
    public UserDTO findDto(Long id) {
        Optional<User> opt = userRepository.findById(id);
        opt.orElseThrow(() -> new EntityNotFoundException("User with id : " + id + " is not exists !"));
        UserDTO temp = new UserDTO();
        User user = opt.get();
        temp.setId(user.getId());
        temp.setEmail(user.getEmail());
        temp.setPassword(user.getPassword());
        temp.setEnabled(user.isEnabled());
        temp.setRole(user.getRole());
        return temp;
    }
    
//    public Page<User> findAllPaginated(int curPage, String orderBy, String orderDir) {
//        Sort orders;
//        Pageable pageable;
//        pageable = PageRequest.of(curPage - 1, pageSize);
//        if(orderBy != null) {
//            orders = Sort.by(orderBy);
//            if(orderDir != null) {
//                switch (orderDir) {
//                    case "asc":
//                        orders = orders.ascending();
//                        break;
//                    case "desc":
//                        orders = orders.descending();
//                        break;
//                    default:
//                        orders = orders.ascending();
//                }
//            }
//            pageable = PageRequest.of(curPage, pageSize, orders);
//        }
//        return userRepository.findAll(pageable);
//    }
    
}
