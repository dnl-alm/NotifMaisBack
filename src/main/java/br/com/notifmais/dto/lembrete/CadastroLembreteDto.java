package br.com.notifmais.dto.lembrete;

import jakarta.validation.constraints.NotNull;

public class CadastroLembreteDto {

    @NotNull
    private int consultaId;

    public int getConsultaId() {
        return consultaId;
    }

    public void setConsultaId(Integer consultaId) {
        this.consultaId = consultaId;
    }
}
