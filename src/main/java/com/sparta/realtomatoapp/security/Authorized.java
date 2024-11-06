package com.sparta.realtomatoapp.security;

import com.sparta.realtomatoapp.user.entity.UserRole;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Authorized {
    UserRole value();

}
