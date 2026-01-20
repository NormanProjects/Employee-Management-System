package com.mashit.employeemanagementsystem.service;

import com.mashit.employeemanagementsystem.dto.ForgotPasswordRequest;
import com.mashit.employeemanagementsystem.dto.PasswordResetResponse;
import com.mashit.employeemanagementsystem.dto.ResetPasswordRequest;
import com.mashit.employeemanagementsystem.entity.PasswordResetToken;
import com.mashit.employeemanagementsystem.entity.User;
import com.mashit.employeemanagementsystem.exception.ResourceNotFoundException;
import com.mashit.employeemanagementsystem.repository.PasswordResetTokenRepository;
import com.mashit.employeemanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    // private final EmailService emailService; // TODO: Implement email service

    /**
     * Initiate password reset process
     * Generates a token and sends email (email service to be implemented)
     */
    @Transactional
    public PasswordResetResponse forgotPassword(ForgotPasswordRequest request) {
        // Find user by email
        User user = userRepository.findByUsername(request.getEmail())
                .or(() -> userRepository.findByEmployeeEmail(request.getEmail()))
                .orElse(null);

        // Always return success message for security (don't reveal if user exists)
        if (user == null) {
            return new PasswordResetResponse(
                    "If an account exists with this email, you will receive a password reset link shortly.",
                    true
            );
        }

        // Delete any existing tokens for this user
        tokenRepository.findByUser(user).ifPresent(tokenRepository::delete);

        // Generate reset token
        String token = UUID.randomUUID().toString();

        // Create and save token
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setUsed(false);
        tokenRepository.save(resetToken);

        // TODO: Send email with reset link
        // String resetUrl = "http://localhost:4200/reset-password?token=" + token;
        // emailService.sendPasswordResetEmail(user.getEmail(), resetUrl);

        System.out.println("Password Reset Token: " + token); // For testing
        System.out.println("Reset URL: http://localhost:4200/reset-password?token=" + token);

        return new PasswordResetResponse(
                "If an account exists with this email, you will receive a password reset link shortly.",
                true
        );
    }

    /**
     * Validate reset token
     */
    public boolean validateToken(String token) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElse(null);

        if (resetToken == null) {
            return false;
        }

        return !resetToken.isExpired() && !resetToken.isUsed();
    }

    /**
     * Reset password using token
     */
    @Transactional
    public PasswordResetResponse resetPassword(ResetPasswordRequest request) {
        // Find token
        PasswordResetToken resetToken = tokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid reset token"));

        // Check if token is expired
        if (resetToken.isExpired()) {
            throw new IllegalStateException("Reset token has expired");
        }

        // Check if token is already used
        if (resetToken.isUsed()) {
            throw new IllegalStateException("Reset token has already been used");
        }

        // Get user and update password
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // Mark token as used
        resetToken.setUsed(true);
        tokenRepository.save(resetToken);

        return new PasswordResetResponse(
                "Password has been reset successfully. You can now login with your new password.",
                true
        );
    }

    /**
     * Clean up expired tokens (should be run periodically)
     */
    @Transactional
    public void cleanupExpiredTokens() {
        tokenRepository.deleteByExpiryDateBefore(LocalDateTime.now());
    }
}