package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@EqualsAndHashCode (exclude = {"name"})
@AllArgsConstructor
@RequiredArgsConstructor
public class Genre {
    @NotNull
    private final int id;
    @NotNull
    private String name;
}
