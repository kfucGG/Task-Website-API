package ru.kolomiec.taskspring.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.kolomiec.taskspring.dto.ResponseMessageDTO;
import ru.kolomiec.taskspring.dto.TaskDTO;
import ru.kolomiec.taskspring.entity.PersonDetailsSecurityEntity;
import ru.kolomiec.taskspring.entity.Task;
import ru.kolomiec.taskspring.services.interfaces.TaskService;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Task Controller", description = "Controllers which allows add/get/delete tasks")
@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
@Slf4j
public class TaskRestController {

    private final TaskService taskService;

    @GetMapping("/all-tasks")
    @Operation(summary = "return all task which owner by auth person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "returns tasks"),
            @ApiResponse(responseCode = "400", description = "person does not have tasks")
    })
    public ResponseEntity<List<TaskDTO>> getAllTasks(@AuthenticationPrincipal PersonDetailsSecurityEntity authPerson) {
        log.info(String.format("Person: %s try to getting all his tasks", authPerson.getUsername()));
        return ResponseEntity.ok(
                taskService.getAllTaskByPersonUsername(authPerson.getUsername())
                        .stream()
                        .map(TaskDTO::new)
                        .collect(Collectors.toList())
                );
    }

    @PostMapping("/add-task")
    @Operation(summary = "add task to auth person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "task is added"),
            @ApiResponse(responseCode = "400", description = "not valid task")
    })
    public ResponseEntity<ResponseMessageDTO> addNewTaskToPerson(@RequestBody @Valid TaskDTO taskDTO,
                                                                 @AuthenticationPrincipal PersonDetailsSecurityEntity authPerson) {
        log.info(String.format("Person: %s try to add task", authPerson.getUsername()));
        taskService.saveTaskToPerson(authPerson, taskDTO);
        return ResponseEntity.ok(new ResponseMessageDTO("new task is saved"));
    }

    @DeleteMapping("/{task_id}")
    @Operation(summary = "delete task by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "delete task by id"),
            @ApiResponse(responseCode = "400", description = "person dont have task with such id")
    })
    public ResponseEntity<ResponseMessageDTO> deletePersonTask(@AuthenticationPrincipal PersonDetailsSecurityEntity authPerson,
                                                               @PathVariable("task_id") Long taskId) {
        log.info(String.format("Person: %s try to remove task with id %s",
                authPerson.getUsername(), taskId));
        taskService.deleteTaskById(taskId);
        return new ResponseEntity<>(new ResponseMessageDTO("task with id %s is deleted"
                .formatted(taskId)),
                HttpStatus.NO_CONTENT);
    }
}
