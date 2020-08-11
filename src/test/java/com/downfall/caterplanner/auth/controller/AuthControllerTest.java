package com.downfall.caterplanner.auth.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    protected MockMvc mvc;

//    protected ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void authGoogle() throws Exception {
        String idToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjY1YjNmZWFhZDlkYjBmMzhiMWI0YWI5NDU1M2ZmMTdkZTRkZDRkNDkiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI4MjQ1NTc5MTMxODctZWVmMGZuNW9obGM4Nm83a2s2M29rM21qdWNvODN0a2YuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI4MjQ1NTc5MTMxODctazhvbnJodjNudDBxNWNiZnZjMmhmNmdhbXNwanNlbzEuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDYxNjE3NjE1MTI5NDEyNTM2OTIiLCJlbWFpbCI6InNpbm55dW0wMjAzQGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJuYW1lIjoi7IOB7J2866-465SU7Ja06rOg65Ox7ZWZ6rWQLey1nOyLoOuFkCIsInBpY3R1cmUiOiJodHRwczovL2xoNC5nb29nbGV1c2VyY29udGVudC5jb20vLWtfS0xBUXBfbG1nL0FBQUFBQUFBQUFJL0FBQUFBQUFBQUFBL0FNWnV1Y214VDBwWjQyNnJPTWNnR2RWTnZQV0kxVjhyc1Evczk2LWMvcGhvdG8uanBnIiwiZ2l2ZW5fbmFtZSI6Ii3stZzsi6DrhZAiLCJmYW1pbHlfbmFtZSI6IuyDgeydvOuvuOuUlOyWtOqzoOuTse2Vmeq1kCIsImxvY2FsZSI6ImtvIiwiaWF0IjoxNTk0NTUxMjcwLCJleHAiOjE1OTQ1NTQ4NzB9.SkEbvo3Brb8KCnZ5G_R415phLi114NzYWFCdURlgIODjMiFA2JdbWqN16OnOK9DfjkUSKf6YihEE1GO9XXTrYDbqqelQ2uzIWcngn8dvlYwYaUniUk9wTUM8F4DQSTM1Kx-RMwgaGlqWAf_qnY494jco7GfFqiBF5Gs-ntL_HZ2C6UdWny4JTa7lN3EDf2O6MIv71y_xIyHw0zq7BMH5KNuXDtTQeYsE0Ob0PVifOWwJpn1NPx8jbXoZXEXsBP3Q4vXh3kL8tcgSv9SR4JIHlGFPX1fE5lAC2757Q_JUX6CFKdUEpX_3y7f963Gz3DhhxfX9BmKhI2BuZyyvxV7YLg";
        mvc.perform(
                post("/auth/google")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"idToken\" : \"" + idToken +"\"}"))
                .andDo(print())
                .andExpect(status().isOk());
    }

//    protected String toJson(Object o) throws JsonProcessingException {
//        return objectMapper.writeValueAsString(o);
//    }

}