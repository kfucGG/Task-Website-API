package ru.kolomiec.taskspring.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kolomiec.taskspring.entity.Task;
import ru.kolomiec.taskspring.services.TaskService;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/{id}")
    public ResponseEntity<List<Task>> sendUserTask(@PathVariable("id") Long id) {
        return ResponseEntity.ok(taskService.getAllTaskByUserId(id));
    }
}
