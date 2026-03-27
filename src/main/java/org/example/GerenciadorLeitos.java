package org.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gerenciador central de leitos de UTI — implementado com o padrão <b>Singleton</b>.
 *
 * <h3>Padrão Singleton</h3>
 * <p><b>O que é:</b> o Singleton garante que exista apenas <em>uma única instância</em>
 * desta classe em toda a aplicação, fornecendo um ponto de acesso global a ela.</p>
 *
 * <p><b>Como identificar nesta classe:</b></p>
 * <ul>
 *   <li><b>Construtor privado</b> ({@code private GerenciadorLeitos()}) — impede
 *       que outras classes criem novas instâncias diretamente com {@code new}.</li>
 *   <li><b>Atributo estático privado</b> ({@code private static volatile GerenciadorLeitos instance})
 *       — armazena a referência única da instância.</li>
 *   <li><b>Método de acesso estático</b> ({@link #getInstance()}) — é o único caminho
 *       para obter a instância; utiliza <em>double-checked locking</em> para garantir
 *       thread-safety sem penalizar a performance após a primeira criação.</li>
 * </ul>
 *
 * <p><b>Por que usar Singleton aqui:</b> em um hospital, o controle de leitos de UTI
 * deve ser centralizado. Se houvesse múltiplas instâncias de gerenciador, seria possível
 * alocar o mesmo leito para dois pacientes simultaneamente, gerando inconsistência
 * grave. O Singleton assegura uma fonte única de verdade.</p>
 *
 * @author Pedro
 */
public class GerenciadorLeitos {

    /**
     * Instância única do gerenciador (Singleton).
     * O modificador {@code volatile} garante visibilidade entre threads.
     */
    private static volatile GerenciadorLeitos instance;

    /** Mapa de leitos indexados pelo seu id. */
    private final Map<Integer, Leito> leitos;

    /** Fila de espera com prioridade para pacientes sem leito disponível. */
    private final FilaEspera filaEspera;

    /**
     * Construtor privado — pilar do padrão Singleton.
     *
     * <p>Impede a criação de instâncias externas. Inicializa o hospital
     * com 10 leitos (ids de 1 a 10), todos com status LIVRE.</p>
     */
    private GerenciadorLeitos() {
        leitos = new HashMap<>();
        filaEspera = new FilaEspera();
        for (int i = 1; i <= 10; i++) {
            leitos.put(i, new Leito(i));
        }
    }

    /**
     * Retorna a instância única do gerenciador de leitos (Singleton).
     *
     * <p>Utiliza <em>double-checked locking</em> para ser thread-safe:
     * o bloco {@code synchronized} só é executado na primeira chamada,
     * evitando overhead desnecessário nas chamadas seguintes.</p>
     *
     * @return instância única de {@code GerenciadorLeitos}
     */
    public static GerenciadorLeitos getInstance() {
        if (instance == null) {
            synchronized (GerenciadorLeitos.class) {
                if (instance == null) {
                    instance = new GerenciadorLeitos();
                }
            }
        }
        return instance;
    }

    /**
     * Reseta a instância Singleton para {@code null}.
     *
     * <p><b>Uso exclusivo em testes</b> — permite que cada teste rode
     * com uma instância limpa, garantindo isolamento.</p>
     */
    public static void reset() {
        instance = null;
    }

    /**
     * Aloca um paciente a um leito específico.
     *
     * @param leitoId  id do leito desejado
     * @param paciente paciente a ser alocado
     * @throws LeitoException se o leito não existir ou já estiver ocupado
     */
    public void alocarLeito(int leitoId, Paciente paciente) {
        Leito leito = leitos.get(leitoId);
        if (leito == null) {
            throw new LeitoException("Leito " + leitoId + " não encontrado.");
        }
        if (leito.getStatus() != Leito.Status.LIVRE) {
            throw new LeitoException("Leito " + leitoId + " não está livre. Status atual: " + leito.getStatus());
        }
        leito.setStatus(Leito.Status.OCUPADO);
        leito.setPaciente(paciente);
    }

    /**
     * Libera um leito ocupado, desvinculando o paciente.
     *
     * @param leitoId id do leito a liberar
     * @throws LeitoException se o leito não existir ou já estiver livre
     */
    public void liberarLeito(int leitoId) {
        Leito leito = leitos.get(leitoId);
        if (leito == null) {
            throw new LeitoException("Leito " + leitoId + " não encontrado.");
        }
        if (leito.getStatus() == Leito.Status.LIVRE) {
            throw new LeitoException("Leito " + leitoId + " já está livre.");
        }
        leito.setStatus(Leito.Status.LIVRE);
        leito.setPaciente(null);
    }

    /**
     * Retorna a lista de leitos com status LIVRE.
     *
     * @return lista de leitos disponíveis
     */
    public List<Leito> getLeitosDisponiveis() {
        return leitos.values().stream()
                .filter(l -> l.getStatus() == Leito.Status.LIVRE)
                .toList();
    }

    /**
     * Retorna a fila de espera de pacientes.
     *
     * @return instância da fila de espera
     */
    public FilaEspera getFilaEspera() {
        return filaEspera;
    }

    /**
     * Retorna um leito pelo id.
     *
     * @param id identificador do leito
     * @return o leito correspondente, ou {@code null} se não existir
     */
    public Leito getLeito(int id) {
        return leitos.get(id);
    }
}
