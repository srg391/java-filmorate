package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.NonFinal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
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
    @Size(max = 200)
    private String description;
    @NotNull(message = "Дата релиза не соотвествует!")
    private LocalDate releaseDate;
    @NotNull(message = "Продолжительность не соотвествует!")
    @Positive(message = "Продолжительность не соотвествует!")
    private Integer duration;
    private Mpa mpa;
    private Set<Long> likes;
    private LinkedHashSet<Genre> genres;

    public int rate() {
        return likes.size();
    }
}

