package ru.kolomiec.taskspring.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.kolomiec.taskspring.dto.ResponseMessageDTO;
import ru.kolomiec.taskspring.dto.TaskDTO;
import ru.kolomiec.taskspring.entity.PersonDetailsSecurityEntity;
import ru.kolomiec.taskspring.entity.Task;
import ru.kolomiec.taskspring.services.interfaces.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    @GetMapping("/all-tasks")
    public ResponseEntity<List<Task>> getAllTasks(@AuthenticationPrincipal PersonDetailsSecurityEntity authPerson) {
        return ResponseEntity.ok(taskService.getAllTaskByPersonUsername(authPerson.getUsername()));
    }

    @PostMapping("/add-task")
    public ResponseEntity<ResponseMessageDTO> addNewTaskToPerson(@RequestBody TaskDTO taskDTO,
                                                                 @AuthenticationPrincipal PersonDetailsSecurityEntity authPerson) {
        taskService.saveTaskToPerson(authPerson, taskDTO);
        return ResponseEntity.ok(new ResponseMessageDTO("new task is saved"));
    }

    @DeleteMapping("/{taskId}/delete-task")
    public ResponseEntity<ResponseMessageDTO> deletePersonTask(@AuthenticationPrincipal PersonDetailsSecurityEntity authPerson,
                                                               @PathVariable("taskId") Long taskId) {
        taskService.deleteTaskOwnedByPerson(authPerson, taskId);
        return ResponseEntity.ok(new ResponseMessageDTO(new StringBuilder("task with taskId ").append(taskId).append("is deleted").toString()));
    }
}
