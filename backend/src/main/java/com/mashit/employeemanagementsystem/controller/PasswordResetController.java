package com.mashit.employeemanagementsystem.controller;

import com.mashit.employeemanagementsystem.dto.ForgotPasswordRequest;
import com.mashit.employeemanagementsystem.dto.PasswordResetResponse;
import com.mashit.employeemanagementsystem.dto.ResetPasswordRequest;
import com.mashit.employeemanagementsystem.service.PasswordResetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PasswordResetController {

    private final PasswordResetService PasswordResetService;

    /**
     * Initiate password reset - send email with token
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<PasswordResetResponse> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {
        PasswordResetResponse response = PasswordResetService.forgotPassword(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Validate reset token
     */
    @GetMapping("/validate-reset-token")
    public ResponseEntity<PasswordResetResponse> validateResetToken(
            @RequestParam String token) {
        boolean isValid = PasswordResetService.validateToken(token);

        if (isValid) {
            return ResponseEntity.ok(new PasswordResetResponse(
                    "Token is valid",
                    true
            ));
        } else {
            return ResponseEntity.badRequest().body(new PasswordResetResponse(
                    "Invalid or expired token",
                    false
            ));
        }
    }

    /**
     * Reset password using token
     */
    @PostMapping("/reset-password")
    public ResponseEntity<PasswordResetResponse> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {
        try {
            PasswordResetResponse response = PasswordResetService.resetPassword(request);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new PasswordResetResponse(
                    e.getMessage(),
                    false
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new PasswordResetResponse(
                    "Failed to reset password. Please try again.",
                    false
            ));
        }
    }
}