package br.com.notifmais.dto.consulta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class CadastroConsultaDto {

    @NotBlank
    private int paciente;

    @NotBlank
    private int medico;

    @NotBlank
    private LocalDateTime dataHora;

    @NotBlank
    @Size(max = 100)
    private String tipo;

    @NotBlank
    @Pattern(regexp = "[CPD]", message = "statusConfirmacao deve ser C, P ou D")
    private String statusConfirmacao;

    public int getPaciente() {
        return paciente;
    }

    public void setPaciente(int paciente) {
        this.paciente = paciente;
    }

    public int getMedico() {
        return medico;
    }

    public void setMedico(int medico) {
        this.medico = medico;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getStatusConfirmacao() {
        return statusConfirmacao;
    }

    public void setStatusConfirmacao(String statusConfirmacao) {
        this.statusConfirmacao = statusConfirmacao;
    }
}
