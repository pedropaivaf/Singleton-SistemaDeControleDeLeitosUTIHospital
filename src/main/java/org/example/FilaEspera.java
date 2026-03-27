package org.example;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Fila de espera com prioridade para pacientes aguardando leito de UTI.
 *
 * <p>Ordena automaticamente os pacientes por gravidade:
 * CRITICA é atendido primeiro, seguido de ALTA e depois MEDIA.</p>
 *
 * @author Pedro
 */
public class FilaEspera {

    /**
     * Fila de prioridade interna que ordena por {@link Paciente.Prioridade#ordinal()}.
     * Quanto menor o ordinal, maior a gravidade (CRITICA = 0).
     */
    private final PriorityQueue<Paciente> fila;

    /** Cria uma fila de espera vazia. */
    public FilaEspera() {
        this.fila = new PriorityQueue<>(
                Comparator.comparingInt(p -> p.getPrioridade().ordinal())
        );
    }

    /**
     * Adiciona um paciente à fila de espera.
     *
     * @param paciente paciente a ser adicionado
     */
    public void adicionar(Paciente paciente) {
        fila.add(paciente);
    }

    /**
     * Remove e retorna o próximo paciente com maior prioridade.
     *
     * @return próximo paciente, ou {@code null} se a fila estiver vazia
     */
    public Paciente proximo() {
        return fila.poll();
    }

    /**
     * @return quantidade de pacientes na fila
     */
    public int tamanho() {
        return fila.size();
    }

    /**
     * @return {@code true} se a fila estiver vazia
     */
    public boolean isEmpty() {
        return fila.isEmpty();
    }
}
