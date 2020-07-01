package cz.muni.fi.ib053.todomanager.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel(description = "DTO class used for retrieving details about the corresponding user.")
public class UserDTO {

        @NotNull
        @ApiModelProperty(notes = "Database generated id of the user", required = true)
        private Long id;

        @NotNull
        @ApiModelProperty(notes = "First name of the user", required = true)
        private String name;

        @NotNull
        @ApiModelProperty(notes = "Surname of the user", required = true)
        private String surname;

        @NotNull
        @ApiModelProperty(notes = "Username of the user", required = true)
        private String username;

}
