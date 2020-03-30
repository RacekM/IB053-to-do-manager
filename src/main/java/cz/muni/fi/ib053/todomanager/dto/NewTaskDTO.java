package cz.muni.fi.ib053.todomanager.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class NewTaskDTO {
    @NotNull
    private Long estimatedFinishTime;
    @NotNull
    private Long orderIndex;
    @NotNull
    private UserDTO owner;
    @NotNull
    private List<TaskDTO> prerequisites;
}
