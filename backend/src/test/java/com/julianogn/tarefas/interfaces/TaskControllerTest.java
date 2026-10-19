package com.julianogn.tarefas.interfaces;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void fullLifecycle() throws Exception {
        String body = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title": "Estudar DDD", "priority": "HIGH", "dueDate": "2030-01-01"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("TODO"))
                .andReturn().getResponse().getContentAsString();
        String id = JsonPath.read(body, "$.id");

        mockMvc.perform(post("/api/tasks/{id}/start", id))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
        mockMvc.perform(post("/api/tasks/{id}/complete", id))
                .andExpect(jsonPath("$.status").value("DONE"));
        mockMvc.perform(post("/api/tasks/{id}/start", id))
                .andExpect(status().isUnprocessableContent());

        mockMvc.perform(get("/api/tasks").param("status", "DONE"))
                .andExpect(jsonPath("$[?(@.id == '%s')]".formatted(id)).exists());

        mockMvc.perform(delete("/api/tasks/{id}", id)).andExpect(status().isNoContent());
        mockMvc.perform(get("/api/tasks/{id}", id)).andExpect(status().isNotFound());
    }

    @Test
    void rejectsBlankTitle() throws Exception {
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title": " ", "priority": "LOW"}
                                """))
                .andExpect(status().isBadRequest());
    }
}
