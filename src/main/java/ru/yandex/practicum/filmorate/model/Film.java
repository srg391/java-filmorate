package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@RequiredArgsConstructor
public class Film {
    private Long id;
    @NotBlank(message = "Название не соотвествует!")
    private String name;
    @NotBlank(message = "Описание не соотвествует!")
    private String description;
    @NotNull(message = "Дата релиза не соотвествует!")
    private LocalDate releaseDate;
    @NotNull(message = "Дата релиза не соотвествует!")
    @Positive(message = "Продолжительность не соотвествует!")
    private Integer duration;
    @JsonIgnore
    Set<Long> userIds = new HashSet<>();
}

