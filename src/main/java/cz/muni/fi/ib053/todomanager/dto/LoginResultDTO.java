package cz.muni.fi.ib053.todomanager.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "DTO class containing result of the logging attempt.")
public class LoginResultDTO {

        @ApiModelProperty(notes = "Result of the logging attempt", required = true)
        private boolean loggedIn;

}
