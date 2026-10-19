# Gerenciador de Tarefas: React + Java

Aplicação web de tarefas com **frontend em React (TypeScript, Vite)** e **backend em Java 21 com Spring Boot 4**. O projeto serve de estudo de **DDD**, **MVC**, **Clean Architecture**, **design patterns** e **clean code** nas duas pontas.

```
.
├── backend/   # API REST Spring Boot (Maven)
└── frontend/  # SPA React + TypeScript (Vite)
```

## Como executar

Pré-requisitos: Java 21 e Node 20+.

```bash
# terminal 1: API em http://localhost:8080
cd backend
./mvnw spring-boot:run

# terminal 2: interface em http://localhost:5173
cd frontend
npm install
npm run dev
```

O Vite repassa as chamadas `/api` para o backend, então não é preciso configurar CORS em desenvolvimento. O banco é H2 em memória (console em `http://localhost:8080/h2-console`, JDBC URL `jdbc:h2:mem:tarefas`).

## Testes

```bash
cd backend && ./mvnw verify          # domínio, casos de uso e API (MockMvc)
cd frontend && npm test && npm run lint
```

## API

| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/tasks?status=&priority=&overdue=&sort=` | lista com filtros; `sort` = `CREATION`, `PRIORITY` ou `DUE_DATE` |
| GET | `/api/tasks/{id}` | busca uma tarefa |
| POST | `/api/tasks` | cria (`title`, `description`, `priority`, `dueDate`) |
| PUT | `/api/tasks/{id}` | edita |
| POST | `/api/tasks/{id}/start` | `TODO → IN_PROGRESS` |
| POST | `/api/tasks/{id}/complete` | conclui |
| POST | `/api/tasks/{id}/reopen` | volta para `TODO` |
| DELETE | `/api/tasks/{id}` | remove |

Erros seguem o formato Problem Details (RFC 9457): 404 para tarefa inexistente, 422 para regra de negócio violada (por exemplo, iniciar uma tarefa concluída) e 400 para entrada inválida.

## Backend: arquitetura

```
com.julianogn.tarefas
├── domain/            # núcleo: sem Spring, sem JPA
│   ├── model/         # Task (agregado), TaskId, Title, Priority, TaskStatus
│   ├── event/         # TaskCreated, TaskStatusChanged, DomainEventPublisher (porta)
│   ├── exception/     # erros de negócio
│   └── repository/    # TaskRepository (porta)
├── application/       # TaskService (casos de uso), comandos, filtros, ordenação, DTO
├── infrastructure/    # adaptadores: JPA, eventos Spring, configuração de beans
└── interfaces/rest/   # controller REST, requests, tratamento de erros
```

- **DDD**: `Task` é o agregado e a única porta para mudar o estado; a máquina de estados fica em `TaskStatus`. `Title` e `TaskId` são value objects que se validam sozinhos. O agregado registra eventos de domínio que o serviço publica depois de salvar.
- **Clean Architecture / Hexagonal**: o domínio define portas (`TaskRepository`, `DomainEventPublisher`) e a infraestrutura fornece adaptadores. `TaskService` não depende do Spring; ele é registrado em `BeanConfiguration`.
- **MVC**: `TaskController` é o controller, o `TaskService` expõe o model e o record `TaskView` é a view serializada em JSON.

| Padrão | Onde |
|---|---|
| Repository + Adapter | `TaskRepository` → `JpaTaskRepositoryAdapter` |
| Data Mapper | `TaskEntityMapper` separa entidade JPA do modelo de domínio |
| Builder | `Task.Builder` para reconstruir agregados do banco |
| Factory Method | `Task.create`, `TaskId.generate` |
| Strategy | `TaskSortOrder` (cada ordenação traz seu `Comparator`) |
| Observer | eventos de domínio + `TaskActivityLogger` com `@EventListener` |
| Command / DTO | `CreateTaskCommand`, `UpdateTaskCommand`, `TaskView` |
| Dependency Injection | injeção por construtor em todas as classes |

## Frontend: arquitetura

```
src/
├── domain/          # tipos, regras de UI (ações válidas por status, validação) e porta TaskRepository
├── infrastructure/  # cliente HTTP e HttpTaskRepository (adaptador)
├── application/     # useTasks (controller) e injeção do repositório via Context
└── presentation/    # TasksPage e componentes (views)
```

- **MVC**: o hook `useTasks` é o controller (estado e intenções do usuário), os componentes são views puras e o domínio tipado é o model.
- **Repository + Adapter**: os componentes falam com a interface `TaskRepository`; trocar HTTP por outro armazenamento não muda a UI.
- **Dependency Injection**: `RepositoryProvider` entrega o repositório pela árvore de componentes, o que facilita testes.
- **Clean code**: componentes pequenos, rótulos centralizados, nenhuma regra de negócio dentro do JSX.
