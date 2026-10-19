import type { Task, TaskAction, TaskFilter, TaskInput } from './task'

/** Port: the UI depends on this contract, not on HTTP details. */
export interface TaskRepository {
  list(filter: TaskFilter): Promise<Task[]>
  create(input: TaskInput): Promise<Task>
  update(id: string, input: TaskInput): Promise<Task>
  transition(id: string, action: TaskAction): Promise<Task>
  remove(id: string): Promise<void>
}
