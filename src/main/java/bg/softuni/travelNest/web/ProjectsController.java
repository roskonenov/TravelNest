package bg.softuni.travelNest.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProjectsController {
    @GetMapping("/projects")
    public String getProjects(){
        return "projects";
    }
}
