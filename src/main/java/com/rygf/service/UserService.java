package com.rygf.service;

import com.rygf.dao.RegisterTokenRepository;
import com.rygf.dao.ResetPasswordTokenRepository;
import com.rygf.dao.UserRepository;
import com.rygf.dto.RegisterDTO;
import com.rygf.dto.UserDTO;
import com.rygf.dto.UserPasswordDTO;
import com.rygf.dto.UserProfileDTO;
import com.rygf.entity.RegisterToken;
import com.rygf.entity.ResetPasswordToken;
import com.rygf.entity.User;
import com.rygf.event.SendRegistrationTokenEvent;
import com.rygf.event.SendResetPasswordTokenEvent;
import com.rygf.exception.DuplicateEntityException;
import com.rygf.exception.EntityNotFoundException;
import com.rygf.exception.InvalidTokenException;
import com.rygf.exception.MailSendingException;
import com.rygf.exception.UserSettingException;
import com.rygf.security.CustomUserDetails;
import com.rygf.security.CustomUserDetailsService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.MailException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
//...
@Transactional
@Service
public class UserService {
    
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ServletContext servletContext;
    private final CustomUserDetailsService userDetailsService;
    private final ApplicationEventPublisher eventPublisher;
    private final RegisterTokenRepository registerTokenRepository;
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;
    
    @Autowired
    protected AuthenticationManager authenticationManager;
    
    @Value("${profile_thumb.upload.path}")
    private String uploadPath;
    
    @Value("${image.upload.maxSize}")
    private int uploadMaxSize;
    
    public void register(RegisterDTO registerDTO, String serverURL) {
        User temp = new User();
        temp.setEmail(registerDTO.getEmail());
        temp.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        temp.setEnabled(false); // Chờ confirm mới kích hoạt
    
        try {
            userRepository.save(temp);
            SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                    temp.getEmail(),
                    temp.getPassword(),
                    new ArrayList<GrantedAuthority>()));
    
            eventPublisher.publishEvent(new SendRegistrationTokenEvent(this, temp, serverURL));
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            throw new DuplicateEntityException("Email : " + registerDTO.getEmail() + " is already taken");
        } catch (MailException e) {
            throw new MailSendingException("Mail sending has been interrupted");
        }
    }
    
    public void createOrUpdate(UserDTO userDTO) {
        User temp;

        if(userDTO.getId() != null) { // UPDATE USER
            Optional<User> opt = userRepository.findById(userDTO.getId());
            final var userId = userDTO.getId();
            opt.orElseThrow(() -> new EntityNotFoundException("User with id : " + userId + " is not exists !"));

            temp = opt.get();
            if(!userDTO.getPassword().isBlank())
                temp.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            temp.setEnabled(userDTO.isEnabled());
            temp.setRole(userDTO.getRole());

        } else { // CREATE NEW USER
            temp = new User();
            temp.setEmail(userDTO.getEmail());
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
    
    public boolean isUserExists(String email) {
        return userRepository.existsByEmail(email);
    }
    
    public User find(Long id) {
        Optional<User> opt = userRepository.findById(id);
        opt.orElseThrow(() -> new EntityNotFoundException("User with id : " + id + " is not exists !"));

        return opt.get();
    }
    
    public User findByEmail(String email) {
        Optional<User> opt = userRepository.findByEmail(email);
        opt.orElseThrow(() -> new EntityNotFoundException("Email : " + email + " is not exists !"));
        
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
        UserDTO dto = new UserDTO();
        User user = opt.get();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setEnabled(user.isEnabled());
        dto.setRole(user.getRole());
        return dto;
    }
    
    @PreAuthorize("isAuthenticated()")
    public UserProfileDTO findProfileDto() {
        CustomUserDetails principal =
            (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> opt = userRepository.findById(principal.getId());
        opt.orElseThrow(() -> new EntityNotFoundException("Fail in update profile dou to not found entity!"));
        UserProfileDTO dto = new UserProfileDTO();
        User user = opt.get();
    
        if(user.selfLinkThumbUri() != null)
            dto.setThumbnailUri(user.selfLinkThumbUri());
        dto.setDisplayName(user.getDisplayName());
        dto.setBirthdate(user.getBirthdate());
        dto.setBio(user.getBio());
        return dto;
    }
    
    @PreAuthorize("isAuthenticated()")
    public void updateProfile(UserProfileDTO dto) {
        CustomUserDetails principal =
            (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> opt = userRepository.findById(principal.getId());
        opt.orElseThrow(() -> new EntityNotFoundException("Fail in update profile due to not found entity!"));
    
        User user = opt.get();
        user.setDisplayName(dto.getDisplayName());
        user.setBirthdate(dto.getBirthdate());
        user.setBio(dto.getBio());
        
        deleteExistThumbnail();
        if(dto.getFinalDesFileName() != null) {
            //Thumbnail
            user.getThumbnail().setUri(dto.getFinalDesFileName());
            user.getThumbnail().setEmbedded(false); // not embed link
        } else if(dto.getFinalDesFileName() == null && dto.getEmbedThumbnailUri() != null) {
            user.getThumbnail().setUri(dto.getEmbedThumbnailUri());
            user.getThumbnail().setEmbedded(true); // embed link
        }
        userRepository.save(user);
    }
    
    public void deleteExistThumbnail() {
        CustomUserDetails principal =
            (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = find(principal.getId());
        final String thumbUri = user.getThumbnail().getUri();
        if(thumbUri == null || thumbUri.isBlank())
            return;
    
        if(user.getThumbnail().isEmbedded()) // Sử dụng Embedded --> không phải xóa
            return;
        String filePath = servletContext.getRealPath("") + uploadPath.concat(user.getThumbnail().getUri());
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch(IOException e) {
            log.warn("IOException : {}", e.getMessage());
        }
    }
    
    public void changePassword(UserPasswordDTO profile) throws UserSettingException {
        SecurityContext context = SecurityContextHolder.getContext();
        CustomUserDetails principal = (CustomUserDetails) context.getAuthentication().getPrincipal();
        User user = find(principal.getId());
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(principal.getUsername(), profile.getOldPassword()));
            
            // change password in db
            user.setPassword(passwordEncoder.encode(profile.newPassword));
            userRepository.save(user);
            
            // change auth token
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, (Object) null, userDetails.getAuthorities());
            context.setAuthentication(authToken);
        } catch (AuthenticationException e) {
            throw new UserSettingException("Old password is wrong !");
        }
    }
    
    public void verifyRegistrationToken(String token) {
        Optional<RegisterToken> opt = registerTokenRepository.findByToken(token);
        opt.orElseThrow(() -> new InvalidTokenException("Why are you sending us invalid token ?"));
    
        final RegisterToken tokenEntity = opt.get();
        User user = tokenEntity.getUser();
    
        if(tokenEntity.getExpirationDate().compareTo(tokenEntity.getCreatedDate()) <= 0)
            throw new InvalidTokenException("Your token has expired !");
    
        registerTokenRepository.delete(tokenEntity);
        user.setEnabled(true); // active User
        
        userRepository.save(user);
    }
    
    public void sendResetPasswordToken(String email, String serverURL) {
        try {
            User user = findByEmail(email);
            if(user.getVerificationToken() != null)
                if(user.getVerificationToken().getExpirationDate().compareTo(user.getVerificationToken().getCreatedDate()) > 0)
                    throw new InvalidTokenException("Token is already sent !");
                
            eventPublisher.publishEvent(new SendResetPasswordTokenEvent(this, user, serverURL));
        } catch (MailException e) {
            throw new MailSendingException("Mail sending has been interrupted");
        }
    }
    
    public void verifyResetPasswordToken(String token) {
        Optional<ResetPasswordToken> opt = resetPasswordTokenRepository.findByToken(token);
        opt.orElseThrow(() -> new InvalidTokenException("Why are you sending us invalid token ?"));
    
        final ResetPasswordToken tokenEntity = opt.get();
        User user = tokenEntity.getUser();
    
        if(tokenEntity.getExpirationDate().compareTo(tokenEntity.getCreatedDate()) <= 0)
            throw new InvalidTokenException("Your token has expired !");
    
        resetPasswordTokenRepository.delete(tokenEntity);
    
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, (Object) null, userDetails.getAuthorities()));
    }
    
    public void resetPassword(String password) {
        SecurityContext context = SecurityContextHolder.getContext();
        CustomUserDetails principal = (CustomUserDetails) context.getAuthentication().getPrincipal();
        User user = find(principal.getId());
        // change password in db
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
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
