import type { Task, TaskAction, TaskInput } from '../../domain/task'
import { TaskItem } from './TaskItem'

interface Props {
  tasks: Task[]
  onTransition: (id: string, action: TaskAction) => Promise<void>
  onUpdate: (id: string, input: TaskInput) => Promise<void>
  onRemove: (id: string) => Promise<void>
}

export function TaskList({ tasks, ...handlers }: Props) {
  if (tasks.length === 0) {
    return <p className="empty">Nenhuma tarefa encontrada.</p>
  }
  return (
    <ul className="task-list">
      {tasks.map((task) => (
        <TaskItem key={task.id} task={task} {...handlers} />
      ))}
    </ul>
  )
}
