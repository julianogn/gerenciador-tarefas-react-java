import { useTasks } from '../application/useTasks'
import { TaskFilters } from './components/TaskFilters'
import { TaskForm } from './components/TaskForm'
import { TaskList } from './components/TaskList'

/** View (MVC): renders what the controller hook exposes and forwards user intents to it. */
export function TasksPage() {
  const { tasks, filter, loading, error, setFilter, create, update, transition, remove } = useTasks()

  return (
    <main className="container">
      <h1>Gerenciador de Tarefas</h1>
      <TaskForm submitLabel="Adicionar" onSubmit={create} />
      <TaskFilters filter={filter} onChange={setFilter} />
      {error && <p className="error">{error}</p>}
      {loading ? (
        <p className="empty">Carregando...</p>
      ) : (
        <TaskList tasks={tasks} onTransition={transition} onUpdate={update} onRemove={remove} />
      )}
    </main>
  )
}
