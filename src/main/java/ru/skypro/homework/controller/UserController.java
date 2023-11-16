package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.component.UserAuth;
import ru.skypro.homework.dto.user.NewPasswordUser;
import ru.skypro.homework.dto.user.UserChangeDto;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.exception.NotFoundUserException;
import ru.skypro.homework.helper.WorkWithImage;
import ru.skypro.homework.service.UserService;

import java.io.IOException;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/${user.url}")
@Tag(name = "Пользователи")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Value("${user.image}")
    private String path;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Обновление пароля")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(schema = @Schema(hidden = true))
                    }),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = {@Content(schema = @Schema(hidden = true))
                    }),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = {@Content(schema = @Schema(hidden = true))
                    })
    })
    @PostMapping("/set_password")
    public ResponseEntity<?> setPassword(@RequestBody NewPasswordUser newPassword,
                                         @AuthenticationPrincipal UserAuth userDetails) {
        try {
            if (userService.changePassword(newPassword, userDetails)) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (NotFoundUserException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(summary = "Получение информации об авторизованном пользователе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserDto.class))
                    }),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = {@Content(schema = @Schema(hidden = true))
                    })
    })
    @GetMapping("/me")
    public ResponseEntity<?> getUser(@AuthenticationPrincipal UserAuth userDetails) {
        try {
            return ResponseEntity.ok(userService.getUserDto(userDetails));
        } catch (NotFoundUserException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(summary = "Обновление информации об авторизованном пользователе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserChangeDto.class))
                    }),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = {@Content(schema = @Schema(hidden = true))
                    })
    })
    @PatchMapping("/me")
    public ResponseEntity<?> putUser(@RequestBody UserChangeDto userChange,
                                     @AuthenticationPrincipal UserAuth userDetails) {
        try {
            return ResponseEntity.ok(userService.putUser(userChange, userDetails));
        } catch (NotFoundUserException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(summary = "Обновление аватара авторизованного пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(schema = @Schema(hidden = true))
                    }),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = {@Content(schema = @Schema(hidden = true))
                    })
    })
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> putUserImage(@RequestParam MultipartFile image,
                                          @AuthenticationPrincipal UserAuth userDetails) {
        try {
            userService.putUserImage(image, userDetails);
            return ResponseEntity.ok().build();
        } catch (NotFoundUserException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (IOException e) {
            LOGGER.error("Exception: '" + e.getMessage() + "'", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/${user.image}/{image_path}")
    public ResponseEntity<?> getImage(@PathVariable("image_path") String imagePath) {
        try {
            return ResponseEntity.ok(WorkWithImage.loadImage(path + '\\' + imagePath));
        } catch (IOException e) {
            LOGGER.error("Error writing file to output stream. Exception: '" + e.getMessage() + "'", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

