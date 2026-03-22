package com.fidc.cdc.kogito.integration.security;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fidc.cdc.kogito.support.ApiContractTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class SelfAccountIntegrationTest extends ApiContractTestBase {

    @Test
    void shouldUpdateOwnEmailAndPasswordAndRequireNewPasswordAfterChange() throws Exception {
        mockMvc.perform(patch("/api/v1/usuarios/me")
                        .with(httpBasic("operador", "operador123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"operador+ajustado@fidc.local"}
                                """))
                .andExpect(status().isNoContent());

        mockMvc.perform(post("/api/v1/usuarios/me/alteracao-senha")
                        .with(httpBasic("operador", "operador123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"senhaAtual":"operador123","novaSenha":"novaSenha123"}
                                """))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/usuarios/me")
                        .with(httpBasic("operador", "operador123")))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/v1/usuarios/me")
                        .with(httpBasic("operador", "novaSenha123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("operador+ajustado@fidc.local"));
    }

    @Test
    void shouldRejectPasswordChangeWhenCurrentPasswordDoesNotMatch() throws Exception {
        mockMvc.perform(post("/api/v1/usuarios/me/alteracao-senha")
                        .with(httpBasic("analista", "analista123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"senhaAtual":"senha-errada","novaSenha":"analistaNova123"}
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail", containsString("senha atual")));
    }

    @Test
    void shouldAcceptExplicitLogoutResource() throws Exception {
        mockMvc.perform(delete("/api/v1/usuarios/me/sessao")
                        .with(httpBasic("auditor", "auditor123")))
                .andExpect(status().isNoContent());
    }
}
