package deepmanapps.kouizer.rest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ForwardingController {

    /**
     * This method maps any path that:
     * 1. Does not start with /api (so we don't block your backend REST endpoints)
     * 2. Does not contain a dot (so we don't block static files like .js, .css, .png)
     * * It forwards those requests to index.html, allowing Angular to handle the routing.
     */
    @RequestMapping(value = "/**/{path:[^\\.]*}")
    public String redirect() {
        // Forward to the home page so that Angular router is preserved.
        return "forward:/index.html";
    }
}