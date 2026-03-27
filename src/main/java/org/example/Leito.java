package org.example;

/**
 * Representa um leito de UTI hospitalar.
 *
 * <p>Cada leito possui um identificador único, um status (LIVRE, OCUPADO ou
 * MANUTENCAO) e uma referência opcional ao paciente alocado.</p>
 *
 * @author Pedro
 */
public class Leito {

    /** Status possíveis de um leito de UTI. */
    public enum Status {
        /** Leito disponível para alocação. */
        LIVRE,
        /** Leito ocupado por um paciente. */
        OCUPADO,
        /** Leito em manutenção — indisponível temporariamente. */
        MANUTENCAO
    }

    private final int id;
    private Status status;
    private Paciente paciente;

    /**
     * Cria um novo leito com o id informado e status inicial LIVRE.
     *
     * @param id identificador único do leito
     */
    public Leito(int id) {
        this.id = id;
        this.status = Status.LIVRE;
        this.paciente = null;
    }

    /** @return identificador do leito */
    public int getId() {
        return id;
    }

    /** @return status atual do leito */
    public Status getStatus() {
        return status;
    }

    /**
     * Altera o status do leito.
     *
     * @param status novo status
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /** @return paciente alocado ou {@code null} se livre */
    public Paciente getPaciente() {
        return paciente;
    }

    /**
     * Vincula ou desvincula um paciente ao leito.
     *
     * @param paciente paciente a vincular, ou {@code null} para desvincular
     */
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    @Override
    public String toString() {
        return "Leito{id=" + id + ", status=" + status + ", paciente=" + paciente + "}";
    }
}