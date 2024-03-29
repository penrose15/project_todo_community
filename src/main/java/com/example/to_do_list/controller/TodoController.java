package com.example.to_do_list.controller;

import com.example.to_do_list.common.security.userdetails.CustomUserDetails;
import com.example.to_do_list.dto.response.MultiResponseDto;
import com.example.to_do_list.dto.todo.*;
import com.example.to_do_list.service.TodoService;
import com.example.to_do_list.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/todo")
public class TodoController {

    private final TodoService todoService;
    private final UsersService usersService;

    @PostMapping("/posts")
    public ResponseEntity<Long> save(@RequestBody @Valid TodoSaveDto request,
                     @AuthenticationPrincipal CustomUserDetails user) {

        return new ResponseEntity<>(todoService.save(request,user.getUsername()), HttpStatus.CREATED);
    }

    @PatchMapping("/posts/{id}")
    public ResponseEntity<Long> update(@PathVariable Long id,
                       @RequestBody TodoUpdateDto request,
                       @AuthenticationPrincipal CustomUserDetails user) {
        String email = user.getEmail();
//        Long usersId = 1L;
        return new ResponseEntity<>(todoService.update(id, request, email), HttpStatus.OK);
    }

    @GetMapping("/posts/category")
    public ResponseEntity<Long> changeCategory(@RequestParam Long todoId,
                                               @RequestParam Long categoryId,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        usersService.findByEmail(userDetails.getUsername());
        Long response = todoService.changeCategories(todoId, categoryId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<TodoResponseDto> findById(@PathVariable Long id,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        usersService.findByEmail(userDetails.getUsername());
        return new ResponseEntity<>(todoService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/posts/done/{id}")
    public ResponseEntity<Long> todoDone(@PathVariable Long id,
                            @AuthenticationPrincipal CustomUserDetails user) {

//        Long usersId = 1L;
        return new ResponseEntity<>(todoService.changeStatus(id, user.getUsername()), HttpStatus.OK);
    }

    @GetMapping("/posts/today")
    public ResponseEntity findToday(@RequestParam int page,
                                     @RequestParam int size,
                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long usersId = usersService.findByEmail(userDetails.getEmail()); //verify

        Page<TodoResponsesDto> response = todoService.findByDate(page-1, size, LocalDate.now(), usersId);
        List<TodoResponsesDto> list = response.getContent();

        return new ResponseEntity<>(new MultiResponseDto<>(list, response), HttpStatus.OK);
    }

    //실행한 todo는 보관함으로 이동
    @GetMapping("/posts/storage")
    public ResponseEntity findAlreadyDone(@RequestParam int page,
                                          @RequestParam int size,
                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        Page<TodoResponsesDto> response = todoService.findByStatusIsDone(page-1, size, userDetails.getEmail());
        List<TodoResponsesDto> list = response.getContent();

        return new ResponseEntity<>(new MultiResponseDto<>(list, response), HttpStatus.OK);
    }

    @GetMapping("/posts/days")
    public ResponseEntity findNextDay(@RequestParam int page,
                                      @RequestParam int size,
                                      @RequestParam String date,
                                      @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long usersId = usersService.findByEmail(customUserDetails.getEmail()); //verify

        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        Page<TodoResponsesDto> request = todoService.findByDate(page-1, size, localDate, usersId);
        List<TodoResponsesDto> list = request.getContent();

        return new ResponseEntity<>(new MultiResponseDto<>(list, request), HttpStatus.OK);
    }

    @GetMapping("/search") //todorepositoryImpl eqExpose 에러 --> 해결
    public ResponseEntity searchTodo(@RequestParam int page,
                                     @RequestParam int size,
                                     @RequestParam(required = false) String title,
                                     @RequestParam(required = false) String content,
                                     @RequestParam(required = false) Integer priority,
                                     @RequestParam(required = false) String expose,
                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long usersId = usersService.findByEmail(userDetails.getEmail());

        Page<TodoResponsesDto> request = todoService.searchByTitleOrContents(page, size, title, content, priority, expose, usersId);
        List<TodoResponsesDto> list = request.getContent();

        return new ResponseEntity(new MultiResponseDto<>(list, request), HttpStatus.OK);
    }

    @GetMapping("/keyword") //todorepositoryImpl eqExpose 에러 --> 해결
    public ResponseEntity search(@RequestParam int page,
                                     @RequestParam int size,
                                     @RequestParam(required = false) String keyword,
                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long usersId = usersService.findByEmail(userDetails.getEmail());

        Page<TodoResponsesDto> request = todoService.search(page, size, keyword, usersId);
        List<TodoResponsesDto> list = request.getContent();

        return new ResponseEntity(new MultiResponseDto<>(list, request), HttpStatus.OK);
    }



    @DeleteMapping("/posts/{id}") //삭제가 안되는데....? --> 수정함 테스트 ㄱㄱ --> 해결!
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id,
                                           @AuthenticationPrincipal CustomUserDetails user
    ) {
        todoService.deleteTodo(id, user.getUsername());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/posts") //삭제가 안된다...? --> 해결!
    public ResponseEntity<Void> deleteTodos(@RequestBody TodoIdsDto ids,
                                            @AuthenticationPrincipal CustomUserDetails user) {
        todoService.deleteTodos(ids.getIds(), user.getUsername());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
