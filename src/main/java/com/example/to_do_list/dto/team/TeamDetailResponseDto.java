package com.example.to_do_list.dto.team;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class TeamDetailResponseDto {
    private long teamId;
    private String title;
    private String explanation;
    private List<UsersTodoDto> usersTodoDtos;

    @Builder
    public TeamDetailResponseDto(long teamId, String title, String explanation, List<UsersTodoDto> usersTodoDtos) {
        this.teamId = teamId;
        this.title = title;
        this.explanation = explanation;
        this.usersTodoDtos = usersTodoDtos;
    }
}
