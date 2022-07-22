package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@RequiredArgsConstructor
public class User {
    private String login;
    private String name;
    private Long id;
    private String email;
    private LocalDate birthday;
}

