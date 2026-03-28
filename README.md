# Sistema de Controle de Leitos UTI - Padrão Singleton

![Testes](https://img.shields.io/badge/Testes-10%2F10%20passando-brightgreen)
![Java](https://img.shields.io/badge/Java-21-orange)
![JUnit](https://img.shields.io/badge/JUnit-5-blue)

Projeto desenvolvido em Java 21 com Maven para a disciplina de Engenharia de Software. O objetivo é demonstrar o padrão de projeto **Singleton** aplicado a um cenário real: o gerenciamento centralizado de leitos de UTI em um hospital.

## Por que Singleton?

Em um hospital, o controle de leitos precisa ser único e centralizado. Se existissem múltiplas instâncias do gerenciador, poderia acontecer de dois pacientes serem alocados no mesmo leito ao mesmo tempo. O Singleton resolve isso garantindo que exista apenas uma instância do `GerenciadorLeitos` em toda a aplicação.

## Como identificar o Singleton no código

- **Construtor privado** em `GerenciadorLeitos` — ninguém cria instância com `new`
- **Atributo estático `instance`** com `volatile` — armazena a única referência
- **Método `getInstance()`** com double-checked locking — thread-safe sem perder performance

## Estrutura do projeto

```
src/main/java/org/example/
├── GerenciadorLeitos.java   → Singleton principal, gerencia o mapa de leitos e a fila de espera
├── Leito.java               → Representa um leito (id, status, paciente)
├── Paciente.java            → Representa um paciente (nome, cpf, prioridade)
├── FilaEspera.java          → Fila de prioridade (CRITICA > ALTA > MEDIA)
└── LeitoException.java      → Exceção para operações inválidas

src/test/java/org/example/
└── GerenciadorLeitosTest.java → 10 casos de teste JUnit 5
```

## Resultado dos Testes

| # | Teste | O que valida | Resultado |
|---|-------|-------------|-----------|
| 1 | testInstanciaUnica | `getInstance()` retorna sempre a mesma referência | :white_check_mark: Passou |
| 2 | testConcorrenciaThreads | 100 threads simultâneas recebem a mesma instância | :white_check_mark: Passou |
| 3 | testResetSingleton | Após `reset()`, uma nova instância é criada | :white_check_mark: Passou |
| 4 | testAlocarLeitoLivre | Paciente alocado com sucesso, status muda para OCUPADO | :white_check_mark: Passou |
| 5 | testAlocarLeitoOcupado | Tentar alocar leito ocupado lança `LeitoException` | :white_check_mark: Passou |
| 6 | testLiberarLeito | Leito liberado volta para LIVRE | :white_check_mark: Passou |
| 7 | testLiberarLeitoJaLivre | Liberar leito já livre lança `LeitoException` | :white_check_mark: Passou |
| 8 | testLeitoEmManutencao | Leito em MANUTENCAO não pode ser alocado | :white_check_mark: Passou |
| 9 | testFilaPrioridade | Paciente CRITICA sai antes de MEDIA na fila | :white_check_mark: Passou |
| 10 | testLeitosDisponiveis | Contagem atualiza corretamente após alocações | :white_check_mark: Passou |

## Como rodar

Abrir o projeto no IntelliJ com Java 21 configurado e executar:

```
mvn test
```

Ou clicar com botão direito em `GerenciadorLeitosTest` → Run.

## Tecnologias

- Java 21
- Maven
- JUnit 5.10.2
