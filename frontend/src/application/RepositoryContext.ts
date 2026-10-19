import { createContext } from 'react'
import type { TaskRepository } from '../domain/taskRepository'

export const RepositoryContext = createContext<TaskRepository | null>(null)
