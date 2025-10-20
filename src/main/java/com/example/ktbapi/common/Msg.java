package com.example.ktbapi.common;

public final class Msg {
    private Msg() {}

    public static final class Success {
        private Success() {}
        public static final String POSTS_FETCH       = "posts_fetch_success";
        public static final String POST_FETCH        = "post_fetch_success";
        public static final String POST_CREATE       = "post_create_success";
        public static final String POST_UPDATE       = "post_update_success";

        public static final String COMMENT_CREATE    = "comment_create_success";
        public static final String COMMENT_UPDATE    = "comment_update_success";
        public static final String COMMENT_DELETE    = "comment_delete_success";

        public static final String LIKE_ADD          = "like_add_success";
        public static final String LIKE_REMOVE       = "like_remove_success";
        public static final String LIKE_STATUS       = "like_status_success";

        public static final String LOGIN             = "login_success";
        public static final String SIGNUP            = "signup_success";
        public static final String PROFILE_UPDATE    = "profile_update_success";
        public static final String PASSWORD_UPDATE   = "password_update_success";
    }

    public static final class Error {
        private Error() {}
        public static final String INVALID_REQUEST   = "invalid_request";
        public static final String UNAUTHORIZED      = "unauthorized";
        public static final String POST_NOT_FOUND    = "post_not_found";
        public static final String COMMENT_NOT_FOUND = "comment_not_found";
        public static final String USER_NOT_FOUND    = "user_not_found";
        public static final String ALREADY_LIKED     = "already_liked";
        public static final String NOT_LIKED         = "not_liked";
        public static final String INVALID_CURRENT_PW= "invalid_current_password";
        public static final String PASSWORD_MISMATCH = "password_mismatch";
        public static final String INTERNAL_ERROR    = "internal_error";
    }
}
