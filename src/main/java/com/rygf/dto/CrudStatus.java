package com.rygf.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CrudStatus {
    private String message;
    private String cssClass;
    
    public CrudStatus(STATUS status) {
        this.message = status.getMessage();
        this.cssClass = status.getCssClass();
    }
    
    @Getter
    public enum STATUS {
        CREATE_SUCCESS("Created successfully <3 !", "success"),
        CREATE_UNSUCCESS("Created unsuccessfully :((", "danger"),
        UPDATE_SUCCESS("Created successfully <3 !", "success"),
        UPDATE_UNSUCCESS("Created unsuccessfully :((", "danger"),
        DELETE_SUCCESS("Delete successfully <3 !", "success"),
        DELETE_UNSUCCESS("Delete unsuccessfully :((", "danger");
    
        private String message;
        private String cssClass;
    
        STATUS(String message, String cssClass) {
            this.message = message;
            this.cssClass = cssClass;
        }
    }
}
