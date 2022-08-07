package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@RequiredArgsConstructor
public class User {
    @NotBlank(message = "Логин не соотвествует!")
    private String login;
    private String name;
    private Long id;
    @NotBlank(message = "Адрес электронной почты не соотвествует!")
    @Email(message = "Адрес электронной почты не соотвествует!")
    private String email;
    @NotNull(message = "Дата рождения не соотвествует!")
    @Past(message = "Дата рождения не соотвествует!")
    private LocalDate birthday;
}

