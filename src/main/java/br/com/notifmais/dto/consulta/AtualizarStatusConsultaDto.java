package br.com.notifmais.dto.consulta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class AtualizarStatusConsultaDto {

    @NotBlank
    @Pattern(regexp = "[CD]", message = "statusConfirmacao deve ser C ou D")
    private String statusConfirmacao;

    public String getStatusConfirmacao() {
        return statusConfirmacao;
    }

    public void setStatusConfirmacao(String statusConfirmacao) {
        this.statusConfirmacao = statusConfirmacao;
    }
}
