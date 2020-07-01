package cz.muni.fi.ib053.todomanager.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ApiModel(description = "DTO class used for adding a new task.")
public class NewTaskDTO {

        @NotNull
        @ApiModelProperty(notes = "Estimated time consumption", required = true)
        private Long estimatedFinishTime;

        @NotNull
        @ApiModelProperty(notes = "Order of the task", required = true)
        private Long orderIndex;

        @NotNull
        @ApiModelProperty(notes = "Task which needs to be finished before this one", required = true)
        private List<TaskDTO> prerequisites;

}
