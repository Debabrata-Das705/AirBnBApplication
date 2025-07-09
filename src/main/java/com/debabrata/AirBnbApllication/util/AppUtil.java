package com.debabrata.AirBnbApllication.util;

import com.debabrata.AirBnbApllication.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class AppUtil {


        public static User getCurrentUser() {
            return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }

}
