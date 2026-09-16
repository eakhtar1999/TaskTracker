package com.tasktracker;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createReturnsCreatedTaskWithLocationHeader() throws Exception {
        String payload = """
                {"title": "Practice SQL joins", "description": "Inner/left/right join drills", "status": "PENDING"}
                """;

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Practice SQL joins"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void createWithBlankTitleFailsValidation() throws Exception {
        String payload = """
                {"title": "", "description": "no title"}
                """;

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"));
    }

    @Test
    void getByIdReturnsTaskWhenExists() throws Exception {
        String payload = """
                {"title": "Fetch me back", "description": "round trip check", "status": "PENDING"}
                """;

        String location = mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andReturn().getResponse().getHeader("Location");

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Fetch me back"));
    }

    @Test
    void getByIdReturns404WhenMissing() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void listCanBeFilteredByStatus() throws Exception {
        mockMvc.perform(get("/api/v1/tasks").param("status", "PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    @Test
    void updateModifiesExistingTask() throws Exception {
        String createPayload = """
                {"title": "Temp task", "description": "before update", "status": "PENDING"}
                """;

        String location = mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPayload))
                .andReturn().getResponse().getHeader("Location");

        String updatePayload = """
                {"title": "Temp task - updated", "description": "after update", "status": "COMPLETED"}
                """;

        mockMvc.perform(put(location)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatePayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Temp task - updated"))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void updateNonExistentTaskReturns404() throws Exception {
        String payload = """
                {"title": "Does not matter", "description": "no such task", "status": "PENDING"}
                """;

        mockMvc.perform(put("/api/v1/tasks/999999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteRemovesTask() throws Exception {
        String createPayload = """
                {"title": "To be deleted", "description": "temporary", "status": "PENDING"}
                """;

        String location = mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPayload))
                .andReturn().getResponse().getHeader("Location");

        mockMvc.perform(delete(location))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(location))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteNonExistentTaskReturns404() throws Exception {
        mockMvc.perform(delete("/api/v1/tasks/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createWithInvalidStatusEnumReturns400() throws Exception {
        String payload = """
                {"title": "Bad status test", "description": "invalid enum", "status": "NOTASTATUS"}
                """;

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listWithInvalidStatusQueryParamReturns400() throws Exception {
        mockMvc.perform(get("/api/v1/tasks").param("status", "NOTASTATUS"))
                .andExpect(status().isBadRequest());
    }
}