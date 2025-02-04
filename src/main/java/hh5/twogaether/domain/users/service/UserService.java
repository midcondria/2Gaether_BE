package hh5.twogaether.domain.users.service;

import hh5.twogaether.domain.gmail.EmailService;
import hh5.twogaether.domain.users.dto.EmailOnlyDto;
import hh5.twogaether.domain.users.dto.LoginRequestDto;
import hh5.twogaether.domain.users.dto.SignUpRequestDto;
import hh5.twogaether.domain.users.entity.User;
import hh5.twogaether.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

import static hh5.twogaether.exception.message.ExceptionMessage.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Transactional
    public void createUser(SignUpRequestDto signupRequestDto) throws Exception {
        User user = getEncryptedUser(signupRequestDto);
        userRepository.save(user);
        emailService.sendSimpleMessage(user.getNickname(), user.getUsername());
    }

    @Transactional(readOnly = true)
    public User login(LoginRequestDto loginRequestDto) {
        User users = userRepository.findByUsername(loginRequestDto.getEmail())
                .orElseThrow(() -> new BadCredentialsException(INCORRECT_SIGN_IN_TRY.getDescription()));
        passwordValidation(loginRequestDto, users);
        checkEmailValidation(users);
        return users;
    }

    @Transactional(readOnly = true)
    public void checkEmailDuplication(String email) {
        Optional<EmailOnlyDto> foundEmail = userRepository.findByEmail(email);
        if (foundEmail.isPresent()) {
            throw new IllegalArgumentException(EXISTED_EMAIL.getDescription());
        }
    }

    private User getEncryptedUser(SignUpRequestDto signupRequestDto) {
        String encryptPassword = passwordEncoder.encode(signupRequestDto.getPassword());
        User user = new User(new SignUpRequestDto(signupRequestDto, encryptPassword));
        return user;
    }

    private static void checkEmailValidation(User users) {
        if (users.getEmailCheck() == 0) {
            throw new BadCredentialsException(INVALID_EMAIL_ACCOUNT.getDescription());
        }
    }

    private void passwordValidation(LoginRequestDto loginRequestDto, User users) {
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), users.getPassword())) {
            throw new BadCredentialsException(INCORRECT_SIGN_IN_TRY.getDescription());
        }
    }
}
