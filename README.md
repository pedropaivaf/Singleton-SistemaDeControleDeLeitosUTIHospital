# Sistema de Controle de Leitos UTI - Padrão Singleton

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

## Casos de teste

| Teste | O que valida |
|-------|-------------|
| `testInstanciaUnica` | `getInstance()` retorna sempre a mesma referência |
| `testConcorrenciaThreads` | 100 threads simultâneas recebem a mesma instância |
| `testResetSingleton` | Após `reset()`, uma nova instância é criada |
| `testAlocarLeitoLivre` | Paciente alocado com sucesso, status muda para OCUPADO |
| `testAlocarLeitoOcupado` | Tentar alocar leito ocupado lança `LeitoException` |
| `testLiberarLeito` | Leito liberado volta para LIVRE |
| `testLiberarLeitoJaLivre` | Liberar leito já livre lança `LeitoException` |
| `testLeitoEmManutencao` | Leito em MANUTENCAO não pode ser alocado |
| `testFilaPrioridade` | Paciente CRITICA sai antes de MEDIA na fila |
| `testLeitosDisponiveis` | Contagem atualiza corretamente após alocações |

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
