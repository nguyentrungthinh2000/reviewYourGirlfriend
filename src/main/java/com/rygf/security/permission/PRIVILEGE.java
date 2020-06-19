package com.rygf.security.permission;

import lombok.Getter;

@Getter
public enum PRIVILEGE {
    POST_CREATE,
    POST_UPDATE,
    POST_READ,
    POST_DELETE,
    
    SUBJECT_CREATE,
    SUBJECT_UPDATE,
    SUBJECT_READ,
    SUBJECT_DELETE,
    
    ROLE_CREATE,
    ROLE_UPDATE,
    ROLE_READ,
    ROLE_DELETE,
    
    USER_CREATE,
    USER_UPDATE,
    USER_READ,
    USER_DELETE
}
