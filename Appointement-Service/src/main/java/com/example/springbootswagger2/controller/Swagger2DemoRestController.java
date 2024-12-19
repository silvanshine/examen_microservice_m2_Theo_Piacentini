package com.example.springbootswagger2.controller;

import com.example.springbootswagger2.model.Student;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;
import java.util.Vector;

// TODO describe this class using @Api annotation from swagger
// TODO this is a Rest Controller
@RestController
@EnableSwagger2
@Api(value="student", description="aaaaaa")
public class Swagger2DemoRestController {


    // TODO create student List : name, class, country
    static private final Vector<Student> students = new Vector<>();

    static {
        // TODO add some students
        students.add(new Student("a", "Egypt", "A"));
        students.add(new Student("b", "Egypt", "B"));
        students.add(new Student("c", "France", "B"));
    }


    // TODO add http response for getStudent operation : 200 / 401 / 403 / 404 (use ApiResponses annotation)


    // TODO endpoint getStudent (return student List)

    // TODO Get Student by name
    // TODO describe this endpoint using swagger
    @RequestMapping(value = "/getStudent/{name}")
    @ApiOperation(value = "get student by name", response = Student.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!")
    })
    public Student getStudent(@PathVariable(value = "name") String name) {
        Vector<Student> result = new Vector<>();
        for (Student student : students) {
            if (student.getName().equals(name)) {
                result.add(student);
            }
        }
        return result.get(0);
    }

    // TODO get Student by country
    @ApiOperation(value = "get Student by country", response = Student.class)
    @RequestMapping(value = "/getStudentByCountry/{country}")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!")
    })
    public List<Student> getStudentByCountry(@PathVariable(value = "country") String country) {
        Vector<Student> result = new Vector<>();

        for (Student student : students) {
            if (student.getCountry().equals(country)) {
                result.add(student);
            }
        }
        return result;
    }

    // TODO get Student by class
    // TODO ping the right endpoint
    @ApiOperation(value = "get Student by class", response = Student.class)
    @RequestMapping(value = "/getStudentByClass/{cls}")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!")
    })
    public List<Student> getStudentByClass(@PathVariable(value = "cls") String cls) {
        Vector<Student> result = new Vector<>();

        for (Student student : students) {
            if (student.getCls().equals(cls)) {
                result.add(student);
            }
        }
        // TODO impl method to get Student by Class

        return result;
    }
}
