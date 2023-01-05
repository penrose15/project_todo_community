package com.example.to_do_list.dto.todo;

import com.example.to_do_list.domain.Todo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class TodoUpdateDto {
    private String title;
    private String content;
    private boolean status;
    private boolean expose;
    private LocalDate endDate;

    @Builder
    public TodoUpdateDto(String title, String content, boolean status, boolean expose, LocalDate endDate) {
        this.title = title;
        this.content = content;
        this.status = status;
        this.expose = expose;
        this.endDate = endDate;
    }

    public Todo toEntity() {
        return Todo.builder()
                .title(title)
                .content(content)
                .status(status)
                .expose(expose)
                .endDate(endDate)
                .build();
    }
}