package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para o {@link GerenciadorLeitos} — Singleton de controle de leitos UTI.
 *
 * <p><b>Padrão Singleton nos testes:</b> o método {@link GerenciadorLeitos#reset()}
 * é chamado antes de cada teste ({@code @BeforeEach}) para garantir isolamento,
 * já que o Singleton mantém estado compartilhado entre chamadas.</p>
 *
 * @author Pedro
 */
class GerenciadorLeitosTest {

    /**
     * Reseta a instância Singleton antes de cada teste,
     * garantindo que cada caso inicie com estado limpo.
     */
    @BeforeEach
    void setUp() {
        GerenciadorLeitos.reset();
    }

    /**
     * Verifica que {@link GerenciadorLeitos#getInstance()} retorna sempre
     * a mesma referência — propriedade fundamental do padrão Singleton.
     *
     * <p><b>Singleton:</b> o construtor privado impede criação com {@code new};
     * {@code getInstance()} é o único ponto de acesso e sempre devolve a mesma instância.</p>
     */
    @Test
    void testInstanciaUnica() {
        GerenciadorLeitos g1 = GerenciadorLeitos.getInstance();
        GerenciadorLeitos g2 = GerenciadorLeitos.getInstance();
        assertSame(g1, g2, "getInstance() deve retornar a mesma instância (Singleton)");
    }

    /**
     * Testa que 100 threads concorrentes obtêm a mesma instância do Singleton.
     *
     * <p><b>Singleton thread-safe:</b> o double-checked locking com {@code volatile}
     * garante que, mesmo sob alta concorrência, apenas uma instância é criada.</p>
     *
     * @throws InterruptedException se alguma thread for interrompida
     */
    @Test
    void testConcorrenciaThreads() throws InterruptedException {
        int numThreads = 100;
        Set<GerenciadorLeitos> instancias = Collections.newSetFromMap(new ConcurrentHashMap<>());
        CountDownLatch latch = new CountDownLatch(numThreads);
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                instancias.add(GerenciadorLeitos.getInstance());
                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();

        assertEquals(1, instancias.size(),
                "Todas as 100 threads devem obter a mesma instância Singleton");
    }

    /**
     * Testa a alocação bem-sucedida de um paciente a um leito livre.
     * O status do leito deve mudar para OCUPADO e o paciente deve estar vinculado.
     */
    @Test
    void testAlocarLeitoLivre() {
        GerenciadorLeitos gerenciador = GerenciadorLeitos.getInstance();
        Paciente paciente = new Paciente("João", "111.111.111-11", Paciente.Prioridade.ALTA);

        gerenciador.alocarLeito(1, paciente);

        Leito leito = gerenciador.getLeito(1);
        assertEquals(Leito.Status.OCUPADO, leito.getStatus());
        assertEquals(paciente, leito.getPaciente());
    }

    /**
     * Testa que alocar um paciente em leito já ocupado lança {@link LeitoException}.
     */
    @Test
    void testAlocarLeitoOcupado() {
        GerenciadorLeitos gerenciador = GerenciadorLeitos.getInstance();
        Paciente p1 = new Paciente("João", "111.111.111-11", Paciente.Prioridade.ALTA);
        Paciente p2 = new Paciente("Maria", "222.222.222-22", Paciente.Prioridade.MEDIA);

        gerenciador.alocarLeito(1, p1);

        assertThrows(LeitoException.class, () -> gerenciador.alocarLeito(1, p2),
                "Alocar leito já ocupado deve lançar LeitoException");
    }

    /**
     * Testa que liberar um leito ocupado restaura o status para LIVRE
     * e desvincula o paciente.
     */
    @Test
    void testLiberarLeito() {
        GerenciadorLeitos gerenciador = GerenciadorLeitos.getInstance();
        Paciente paciente = new Paciente("João", "111.111.111-11", Paciente.Prioridade.ALTA);

        gerenciador.alocarLeito(1, paciente);
        gerenciador.liberarLeito(1);

        Leito leito = gerenciador.getLeito(1);
        assertEquals(Leito.Status.LIVRE, leito.getStatus());
        assertNull(leito.getPaciente());
    }

    /**
     * Testa que a fila de espera respeita a prioridade clínica:
     * CRITICA deve ser atendido antes de MEDIA.
     */
    @Test
    void testFilaPrioridade() {
        GerenciadorLeitos gerenciador = GerenciadorLeitos.getInstance();
        FilaEspera fila = gerenciador.getFilaEspera();

        Paciente media = new Paciente("Carlos", "333.333.333-33", Paciente.Prioridade.MEDIA);
        Paciente critica = new Paciente("Ana", "444.444.444-44", Paciente.Prioridade.CRITICA);

        fila.adicionar(media);
        fila.adicionar(critica);

        Paciente primeiro = fila.proximo();
        assertEquals(Paciente.Prioridade.CRITICA, primeiro.getPrioridade(),
                "Paciente com prioridade CRITICA deve ser atendido primeiro");
    }

    /**
     * Testa que a contagem de leitos disponíveis atualiza corretamente
     * após alocações.
     */
    @Test
    void testLeitosDisponiveis() {
        GerenciadorLeitos gerenciador = GerenciadorLeitos.getInstance();

        assertEquals(10, gerenciador.getLeitosDisponiveis().size(),
                "Inicialmente todos os 10 leitos devem estar disponíveis");

        gerenciador.alocarLeito(1, new Paciente("A", "000", Paciente.Prioridade.MEDIA));
        gerenciador.alocarLeito(2, new Paciente("B", "001", Paciente.Prioridade.ALTA));

        assertEquals(8, gerenciador.getLeitosDisponiveis().size(),
                "Após 2 alocações, devem restar 8 leitos disponíveis");
    }

    /**
     * Testa que liberar um leito que já está livre lança {@link LeitoException}.
     */
    @Test
    void testLiberarLeitoJaLivre() {
        GerenciadorLeitos gerenciador = GerenciadorLeitos.getInstance();

        assertThrows(LeitoException.class, () -> gerenciador.liberarLeito(1),
                "Liberar leito já livre deve lançar LeitoException");
    }
}
