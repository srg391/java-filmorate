package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@RequiredArgsConstructor
public class User {
    private Long id;
    @NotBlank(message = "Адрес электронной почты не соотвествует!")
    @Email(message = "Адрес электронной почты не соотвествует!")
    private String email;
    @NotBlank(message = "Логин не соотвествует!")
    private String login;
    private String name;
    @NotNull(message = "Дата рождения не соотвествует!")
    @PastOrPresent(message = "Дата рождения не соотвествует!")
    private LocalDate birthday;
    @JsonIgnore
    private Set<Long> friendIds = new HashSet<>();
}

