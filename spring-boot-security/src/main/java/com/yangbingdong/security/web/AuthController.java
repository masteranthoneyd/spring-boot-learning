package com.yangbingdong.security.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:yangbingdong1994@gmail.com">yangbingdong</a>
 * @since
 */
@RestController
public class AuthController {

    @GetMapping("/auth")
    public String auth() {
        return "Success";
    }
}
