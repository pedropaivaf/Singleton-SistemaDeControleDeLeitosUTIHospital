package org.example;

/**
 * Representa um paciente que pode ser alocado em um leito de UTI.
 *
 * <p>Cada paciente possui nome, CPF e um nível de prioridade que
 * determina sua posição na fila de espera.</p>
 *
 * @author Pedro
 */
public class Paciente {

    /**
     * Nível de prioridade do paciente para alocação de leitos.
     * A ordem de gravidade é: CRITICA &gt; ALTA &gt; MEDIA.
     */
    public enum Prioridade {
        /** Prioridade crítica — atendimento imediato. */
        CRITICA,
        /** Prioridade alta. */
        ALTA,
        /** Prioridade média. */
        MEDIA
    }

    private final String nome;
    private final String cpf;
    private final Prioridade prioridade;

    /**
     * Cria um novo paciente.
     *
     * @param nome       nome completo do paciente
     * @param cpf        CPF do paciente
     * @param prioridade nível de prioridade clínica
     */
    public Paciente(String nome, String cpf, Prioridade prioridade) {
        this.nome = nome;
        this.cpf = cpf;
        this.prioridade = prioridade;
    }

    /** @return nome do paciente */
    public String getNome() {
        return nome;
    }

    /** @return CPF do paciente */
    public String getCpf() {
        return cpf;
    }

    /** @return prioridade clínica do paciente */
    public Prioridade getPrioridade() {
        return prioridade;
    }

    @Override
    public String toString() {
        return "Paciente{nome='" + nome + "', cpf='" + cpf + "', prioridade=" + prioridade + "}";
    }
}
