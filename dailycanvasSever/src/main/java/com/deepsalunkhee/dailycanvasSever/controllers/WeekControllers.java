package com.deepsalunkhee.dailycanvasSever.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.deepsalunkhee.dailycanvasSever.controllers.dto.TodoDTO;
import com.deepsalunkhee.dailycanvasSever.controllers.dto.WeeKListItemDTO;
import com.deepsalunkhee.dailycanvasSever.controllers.dto.WeekDTO;
import com.deepsalunkhee.dailycanvasSever.models.*;
import com.deepsalunkhee.dailycanvasSever.services.*;


@RestController
@RequestMapping("/api/v1")
public class WeekControllers {

    // creating logger for this class
    private static final Logger logger = LoggerFactory.getLogger(WeekControllers.class);
  

    private final WeekServices weekServices;
    private final DayServices dayServices;
    private final TodoServices todoServices;
    private final UserServices userServices;

    @Autowired
    public WeekControllers(WeekServices weekServices, DayServices dayServices, TodoServices todoServices, UserServices userServices) {
        this.weekServices = weekServices;
        this.dayServices = dayServices;
        this.todoServices = todoServices;
        this.userServices = userServices;
    }

    @PostMapping("/create-week")
    public WeekDTO createWeek(@RequestAttribute("email") String attributeEmail, @RequestBody Map<String, String> request) {
       
        logger.info("Request body: {}", request);
        String email = attributeEmail;
        String startDateString = request.get("startDate");

        LocalDate startDate = LocalDate.parse(startDateString);
        logger.info("Creating week for user with email: {}", email);
        logger.info("Start date: {}", startDate);

        WeekModel createdWeek = weekServices.createWeek(email, startDate);
        logger.info("Week created successfully: {}", createdWeek);

        String[] daysOfWeek = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
        List<WeekDTO.DayInfo> dayInfos = new ArrayList<>();

        for (String dayName : daysOfWeek) {
            DayModel createdDay = dayServices.createDay(createdWeek.getId(), dayName);
            logger.info("Day created: {}", dayName);
            List<TodoDTO> todos = new ArrayList<>(); // Initialize empty list of todos
            dayInfos.add(new WeekDTO.DayInfo(dayName, createdDay.getId(), todos));
        }

        WeekDTO response = new WeekDTO();
        response.setWeekId(createdWeek.getId());
        response.setWeekStartDate(createdWeek.getStartDate());
        response.setDays(dayInfos);

        logger.info("Response: {}", response);

        return response;
    }

    @GetMapping("/get-a-week")
    public WeekDTO getWeeksByUserId(@RequestParam String id) {
        UUID userId = UUID.fromString(id);
        logger.info("Fetching weeks for user with ID: {}", userId);

        WeekModel week = weekServices.getWeeksById(userId);
        logger.info("Week fetched: {}", week);

        List<DayModel> days = dayServices.getDaysByWeekId(week.getId());
        logger.info("Days fetched: {}", days);

        List<WeekDTO.DayInfo> dayInfos = new ArrayList<>();
        for (DayModel day : days) {
            WeekDTO.DayInfo dayInfo = new WeekDTO.DayInfo(day.getDayOfWeek(), day.getId(), new ArrayList<>());
            dayInfo.setDay(day.getDayOfWeek());
            dayInfo.setDayId(day.getId());
            dayInfo.setTodos(new ArrayList<>());

            List<TodoModel> todos = todoServices.getTodosByDayId(day.getId());
            logger.info("Todos fetched for day {}: {}", day.getDayOfWeek(), todos);
            for (TodoModel todo : todos) {
                TodoDTO todoDTO = new TodoDTO(
                        todo.getId(),
                        day.getId(),
                        todo.getContent(),
                        todo.isActionApplied(),
                        todo.getActionType(),
                        todo.getTextColor(),
                        todo.getFontSize(),
                        todo.getScratchColor(),
                        todo.getPosition());
                dayInfo.getTodos().add(todoDTO);
            }

            logger.info("DayInfo created: {}", dayInfo);
            dayInfos.add(dayInfo);
        }

        WeekDTO response = new WeekDTO();
        response.setWeekId(week.getId());
        response.setWeekStartDate(week.getStartDate());
        response.setDays(dayInfos);

        logger.info("Final response: {}", response);
        return response;
    }


    @PostMapping("/update-brain-sorter")
    public String updateBrainSorter(@RequestBody Map<String, Object> request) {
        try {
            UUID weekId = UUID.fromString((String) request.get("weekId"));
            String brainSorterData = (String) request.get("brainSorterData");

            weekServices.updateBrainSorterData(weekId, brainSorterData);

            return "Brain Sorter data updated successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to update Brain Sorter data.";
        }
    }

    @GetMapping("/get-brain-sorter")
    public String getBrainSorter(@RequestParam String weekId) {
        try {
            UUID id = UUID.fromString(weekId);
            WeekModel week = weekServices.getWeeksById(id);
            return week.getBrainSorterData();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/get-week-list")
    public List<WeeKListItemDTO> getWeekList(@RequestAttribute("email") String email) {
        logger.info("Fetching week list for user with email: {}", email);

        //get the user by email
        Optional<UserModel> user = userServices.getUserByEmail(email);

        if (user.isPresent()) {
            List<WeekModel> weeks = user.get().getWeeks();
            logger.info("Weeks fetched: {}", weeks);

            List<WeeKListItemDTO> weekList = new ArrayList<>();
            for (WeekModel week : weeks) {
                WeeKListItemDTO weekItem = new WeeKListItemDTO(
                        week.getId().toString(),
                        week.getStartDate().toString(),
                        week.getWeekName(),
                        week.getNote(),
                        week.getTemplateType()
                );
                weekList.add(weekItem);
            }

            logger.info("Week list created: {}", weekList);
            return weekList;
        } else {
            logger.warn("User not found with email: {}", email);
            return new ArrayList<>();
        }


        
    }
    



}
