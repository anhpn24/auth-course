package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import com.example.demo.security.UserDetailsServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@Import(UserDetailsServiceImpl.class)
public class SareetaApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<ModifyCartRequest> json;

    //<editor-fold desc="NOT AUTHENTICATE">
    @Test
    public void addToCartNotAuthentication() throws Exception {
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setUsername("test");
        cartRequest.setItemId(1);
        cartRequest.setQuantity(1);
        mvc.perform(
                post(new URI("/api/cart/addToCart"))
                        .content(json.write(cartRequest).getJson())
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

    @Test
    public void removeFromCartNotAuthentication() throws Exception {
        mvc.perform(post(new URI("/api/cart/removeFromCart")))
                .andExpect(status().isForbidden());
    }

    @Test
    public void listItemsNotAuthentication() throws Exception {
        mvc.perform(get(new URI("/api/item")))
                .andExpect(status().isForbidden());
    }

    @Test
    public void findItemByIdNotAuthentication() throws Exception {
        mvc.perform(get(new URI("/api/item/1")))
                .andExpect(status().isForbidden());
    }

    @Test
    public void findItemByNameNotAuthentication() throws Exception {
        mvc.perform(get(new URI("/api/item/name/Round%20Widget")))
                .andExpect(status().isForbidden());
    }

    @Test
    public void submitOrderNotAuthentication() throws Exception {
        mvc.perform(post(new URI("/api/order/submit/test")))
                .andExpect(status().isForbidden());
    }

    @Test
    public void listHistoryOrderNotAuthentication() throws Exception {
        mvc.perform(get(new URI("/api/order/history/test")))
                .andExpect(status().isForbidden());
    }

    @Test
    public void findUserByIdNotAuthentication() throws Exception {
        mvc.perform(get(new URI("/api/user/test")))
                .andExpect(status().isForbidden());
    }

    @Test
    public void findUserByNameNotAuthentication() throws Exception {
        mvc.perform(get(new URI("/api/user/id/1")))
                .andExpect(status().isForbidden());
    }
    //</editor-fold>

    //<editor-fold desc="AUTHENTICATED">
    @Test
    @WithMockUser
    public void addToCart() throws Exception {
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setUsername("test");
        cartRequest.setItemId(1);
        cartRequest.setQuantity(1);

        mvc.perform(
                post(new URI("/api/cart/addToCart"))
                        .content(json.write(cartRequest).getJson())
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void removeFromCart() throws Exception {
        mvc.perform(post(new URI("/api/cart/removeFromCart")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void listItems() throws Exception {
        mvc.perform(get(new URI("/api/item")))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void findItemById() throws Exception {
        mvc.perform(get(new URI("/api/item/1")))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void findItemByName() throws Exception {
        mvc.perform(get(new URI("/api/item/name/Round%20Widget" )))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void submitOrder() throws Exception {
        mvc.perform(post(new URI("/api/order/submit/test")))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void listHistoryOrder() throws Exception {
        mvc.perform(get(new URI("/api/order/history/test")))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void findUserById() throws Exception {
        mvc.perform(get(new URI("/api/user/test")))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void findUserByName() throws Exception {
        mvc.perform(get(new URI("/api/user/id/1")))
                .andExpect(status().isNotFound());
    }
    //</editor-fold>

}
