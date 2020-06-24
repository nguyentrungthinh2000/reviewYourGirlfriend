package com.rygf.common;

public class GetLink {
    
    /*
     *   DASHBOARD LINK
     * */
    public static final String DASHBOARD_USER_READ_URI = "/dashboard/users";
    public static final String DASHBOARD_USER_CREATE_URI = "/dashboard/users/create";
    public static final String DASHBOARD_USER_UPDATE_URI = "/dashboard/users/%s/update";
    public static final String DASHBOARD_USER_DELETE_URI = "/dashboard/users/%s/delete";
    public static final String DASHBOARD_USER_PROCESS_URI = "/dashboard/users/submit";
    public static final String DASHBOARD_USER_PROFILE_PROCESS_URI = "/dashboard/users/profile/submit";
    public static final String DASHBOARD_USER_CHANGE_PASSWORD_PROCESS_URI = "/dashboard/users/password/submit";
    
    public static final String DASHBOARD_POST_READ_URI = "/dashboard/posts";
    public static final String DASHBOARD_POST_CREATE_URI = "/dashboard/posts/create";
    public static final String DASHBOARD_POST_UPDATE_URI = "/dashboard/posts/%s/update";
    public static final String DASHBOARD_POST_DELETE_URI = "/dashboard/posts/%s/delete";
    public static final String DASHBOARD_POST_PROCESS_URI = "/dashboard/posts/submit";
    
    public static final String DASHBOARD_SUBJECT_READ_URI = "/dashboard/subjects";
    public static final String DASHBOARD_SUBJECT_CREATE_URI = "/dashboard/subjects/create";
    public static final String DASHBOARD_SUBJECT_UPDATE_URI = "/dashboard/subjects/%s/update";
    public static final String DASHBOARD_SUBJECT_DELETE_URI = "/dashboard/subjects/%s/delete";
    public static final String DASHBOARD_SUBJECT_PROCESS_URI = "/dashboard/subjects/submit";
    
    public static final String DASHBOARD_ROLE_READ_URI = "/dashboard/roles";
    public static final String DASHBOARD_ROLE_CREATE_URI = "/dashboard/roles/create";
    public static final String DASHBOARD_ROLE_UPDATE_URI = "/dashboard/roles/%s/update";
    public static final String DASHBOARD_ROLE_DELETE_URI = "/dashboard/roles/%s/delete";
    public static final String DASHBOARD_ROLE_PROCESS_URI = "/dashboard/roles/submit";
    
    
    // User
    public static String getDashboardUserUpdateUri(Long userId) {
        return String.format(DASHBOARD_USER_UPDATE_URI, userId.toString());
    }
    
    public static String getDashboardUserDeleteUri(Long userId) {
        return String.format(DASHBOARD_USER_DELETE_URI, userId.toString());
    }
    
    // Post
    public static String getDashboardPostUpdateUri(Long postId) {
        return String.format(DASHBOARD_POST_UPDATE_URI, postId.toString());
    }
    
    public static String getDashboardPostDeleteUri(Long postId) {
        return String.format(DASHBOARD_POST_DELETE_URI, postId.toString());
    }
    
    // Subject
    public static String getDashboardSubjectUpdateUri(Long subjectId) {
        return String.format(DASHBOARD_SUBJECT_UPDATE_URI, subjectId.toString());
    }
    
    public static String getDashboardSubjectDeleteUri(Long subjectId) {
        return String.format(DASHBOARD_SUBJECT_DELETE_URI, subjectId.toString());
    }
    
    // Role
    public static String getDashboardRoleUpdateUri(Long roleId) {
        return String.format(DASHBOARD_ROLE_UPDATE_URI, roleId.toString());
    }
    
    public static String getDashboardRoleDeleteUri(Long roleId) {
        return String.format(DASHBOARD_ROLE_DELETE_URI, roleId.toString());
    }
    
    
    /*
    *   NAV LINK
    * */
    public static final String HOMEPAGE_URI = "/";
    public static final String DASHBOARD_URI = "/dashboard";
    public static final String LOGIN_URI = "/login";
    public static final String REGISTER_URI = "/register";
    public static final String LOGOUT_URI = "/logout";
    public static final String SETTING_PROFILE_URI = "/settings/profile";
    public static final String SETTING_PASSWORD_URI = "/settings/password";
    
    /*
     *   DYNAMIC LINK
     * */
    static final String USER_POST_URI = "/users/%s/posts";
    static final String SUBJECT_DETAIL_URI = "/subjects/%s";
    static final String POST_DETAIL_URI = "/posts/%s";
    
    /*
     *   THUMBNAIL LINK
     * */
    static final String USER_PROFILE_THUMB_URI = "/img/profile_thumb/%s";
    static final String POST_THUMB_URI = "/img/post_thumb/%s";
    static final String SUBJECT_THUMB_URI = "/img/post_thumb/%s";
    
    public static String getUserPostsUri(Long userId) {
        return String.format(USER_POST_URI, userId.toString());
    }
    
    public static String getSubjectDetailUri(Long subjectId) {
        return String.format(SUBJECT_DETAIL_URI, subjectId.toString());
    }
    
    public static String getPostDetailUri(Long postId) {
        return String.format(POST_DETAIL_URI, postId.toString());
    }
    
    public static String getUserProfileThumbUri(String filename) {
        return String.format(USER_PROFILE_THUMB_URI, filename);
    }
    
    public static String getPostThumbUri(String filename) {
        return String.format(POST_THUMB_URI, filename);
    }
    
    public static String getSubjectThumbUri(String filename) {
        return String.format(SUBJECT_THUMB_URI, filename);
    }
    
}
